/*
 * Copyright 2016 Jeffrey DeYoung.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.cassandradockertesthelper;

import com.github.cassandrasshutils.command.RemoteCommandDao;
import com.github.cassandrasshutils.command.impl.SSHCommandDaoImpl;
import com.github.cassandrasshutils.exceptions.ConnectionException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.After;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parent class for all tests that you wish to use Cassandra Docker instances
 * for testing. There are three ways to specify the versions of Cassandra to
 * test against: 1. Temporarily rename or remove the versions of Cassandra you
 * do not wish to test against from the src/test/resources/docker/ directory. If
 * renaming, the files shouldn't start with the string "cassandra" 2. Set the
 * 'com.github.cassandradockertesthelper.cassandraversions' system property with
 * a comma (no spaces) separated list of Cassandra versions at test startup. Ex:
 * "-Dcom.github.cassandracurator.cassandraversions=2.1.0,2.1.9" 3. Do Nothing.
 * All available versions of Cassandra will be tested against.
 *
 * @author jeffrey
 */
@RunWith(value = Parameterized.class)
public abstract class AbstractCassandraDockerParameterizedTest
{

    /**
     * Logger for this class.
     */
    private static final Logger logger = LoggerFactory.getLogger(AbstractCassandraDockerParameterizedTest.class);

    /**
     * Rule for getting the test name.
     */
    @Rule
    public final TestName name = new TestName();

    /**
     * Cassandra seed ips to hit for this test.
     */
    private List<String> cassandraSeeds;

    /**
     * Docker IDs that are currently running.
     */
    private List<String> dockerIds;

    /**
     * Cassandra version that is running on the above seeds.
     */
    private String cassandraVersion;

    /**
     * Docker file for this particular test.
     */
    private File dockerFile;

    /**
     * Spin down all our docker boxes that we have spun up during this specific
     * test. Feel free to override this with your own functionality if you wish
     * to access this docker files post-test for some reason.
     */
    @After
    public void tearDown()
    {
        logger.debug("Spinning down all docker instances post-test.");
        //copy off the docker ids as they are being removed by the spin down method; 
        //we can't iterate through a list that is being modified.        
        String[] dockerIdCopy = new String[dockerIds.size()];
        for (int i = 0; i < dockerIds.size(); i++)
        {
            dockerIdCopy[i] = dockerIds.get(i);
        }
        for (int i = 0; i < dockerIdCopy.length; i++)
        {
            String id = dockerIdCopy[i];
            this.spinDownCassandraDockerBox(id);
        }
    }

    /**
     * Generates the parameters for each of our parameterized tests. Each item
     * in the resulting collection represents the parameters for a single test.
     * The test will be re-run for each item.
     *
     * @return A collection of files that will be provided to the constructor of
     * the child class; one at a time.
     */
    @Parameterized.Parameters(name = "Docker File: {0}")
    public static final Collection<File[]> generateParameters()
    {
        File[] availibleDockerFiles = AbstractCassandraDockerParameterizedTest.getAvailibleDockerFiles();
        List<String> cassandraVersionsToTests = getCassandraVersions();
        boolean testAllVersions = false;
        if (cassandraVersionsToTests == null)
        {
            testAllVersions = true;
        }
        List<File[]> toReturn = new ArrayList<>();
        for (File f : availibleDockerFiles)
        {
            String dockerCassandraVersion = f.getName().substring(9);
            if (testAllVersions || cassandraVersionsToTests.contains(dockerCassandraVersion))
            {
                File[] fileArray = new File[1];
                fileArray[0] = f;
                toReturn.add(fileArray);
            }
        }
        return toReturn;
    }

    /**
     * Constructor. This is a parameterized test, so this is where the docker
     * file information gets injected.
     *
     * @param dockerFile Docker file that represents a Cassandra box that we
     * will use to replicate Cassandra for this test.
     */
    public AbstractCassandraDockerParameterizedTest(File dockerFile)
    {
        this.cassandraVersion = dockerFile.getName().substring(9);
        this.dockerFile = dockerFile;
        this.cassandraSeeds = new ArrayList<>();
        this.dockerIds = new ArrayList<>();
    }

    /**
     * Spins up a new Cassandra docker box with the specified version.
     *
     * @return The docker id of the box.
     */
    public String spinUpNewCassandraDockerBox()
    {
        logger.info("Spinning up Cassandra Docker Box:--------------------------------\r\n\tCassandra version:" + this.cassandraVersion + " for test: " + this.getTestName());
        String dockerId = DockerHelper.spinUpDockerBox(dockerFile.getName(), dockerFile);
        dockerIds.add(dockerId);
        String ip = DockerHelper.getDockerIp(dockerId);
        cassandraSeeds.add(ip);
        RemoteCommandDao command = new SSHCommandDaoImpl(ip, "root", 22, "./src/test/resources/docker/insecure_key", null);
        try
        {
            command.connect();
            command.sendCommand("/etc/cassandra/setcassandraip.sh");//set the ips in the cassandra yaml correctly; I can't get the dockerfile to do this automatically -- this will probably bite us in the future
        } catch (ConnectionException | IOException e)
        {
            //throw a runtime exception; generally this is bad practice, but it will only ever be used by a test in this case;
            //we will want to fail the test case if this ever happens anyway
            throw new RuntimeException(e);
        } finally
        {
            command.logOff();
        }
        return dockerId;
    }

    /**
     * Spins down a Cassandra box. Will be called automatically for all test
     * established docker instances at the end of each test.
     *
     * @param containerId
     */
    public void spinDownCassandraDockerBox(String containerId)
    {
        logger.info("Spinning down Cassandra Docker Box: Cassandra version:" + this.cassandraVersion + " for test: " + this.getTestName());
        cassandraSeeds.remove(DockerHelper.getDockerIp(containerId));
        dockerIds.remove(containerId);
        DockerHelper.spinDownDockerBox(containerId);
    }

    /**
     * Gets all the available docker files. Looks in
     * ./src/test/resources/docker/.
     *
     * @return An array of all the available docker files.
     */
    public static File[] getAvailibleDockerFiles()
    {
        File dir = new File("./src/test/resources/docker/");
        return dir.listFiles(new FilenameFilter()
        {

            @Override
            public boolean accept(File dir, String name)
            {
                return (name.startsWith("cassandra") && !name.endsWith("~"));//cassandra docker boxes that are not gedit temp backups                
            }
        });
    }

    /**
     * Cassandra seed ips to hit for this test.
     *
     * @return the cassandraIp
     */
    public List<String> getCassandraSeeds()
    {
        return cassandraSeeds;
    }

    /**
     * Cassandra version that is running on the above seeds.
     *
     * @return the cassandraVersion
     */
    public String getCassandraVersion()
    {
        return cassandraVersion;
    }

    /**
     * Gets the name of the specific test that is running. Includes the
     * parameter.
     *
     * @return The name and parameter of the specific test that is running.
     */
    public String getTestName()
    {
        return name.getMethodName();
    }

    /**
     * Cassandra versions to test against. See comment on the field above.
     *
     * @return the cassandraVersions
     */
    public static List<String> getCassandraVersions()
    {

//        
//        String systemSetCassandraVersions = System.getProperty("com.github.cassandradockertesthelper.cassandraversions");
//        if (systemSetCassandraVersions != null)
//        {
//            return Arrays.asList(systemSetCassandraVersions.split(","));
//        } else
//        {
//            return cassandraVersions;
//        }
        List<String> versions = new ArrayList<String>();
        versions.add("2.0.6");
        return versions;
    }
}
