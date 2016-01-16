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
package com.github.cassandradockertesthelper.cassandradockertesthelper;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.After;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Parent class for all tests you wish to use Cassandra Docker instances for
 * testing.
 *
 * @author jeffrey
 */
@RunWith(value = Parameterized.class)
public abstract class CassandraDockerParameterizedTestParent
{
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
     * Cassandra version that is running on the above seeds/port.
     */
    private String cassandraVersion;

    /**
     * Docker file for this particular test.
     */
    private File dockerFile;

    protected static List<String> cassandraVersions;

    /**
     * Spin down all our docker boxes that we have spun up during this specific
     * test. Feel free to override this with your own functionality if you wish
     * to access this docker files post-test for some reason.
     */
    @After
    public void tearDown()
    {
        //copy off the docker ids as they are being removed by the spin down method; we can't iterate through a list that is being modified.
        List<String> dockerIdCopy = dockerIds;
        for (int i = 0; i < dockerIdCopy.size(); i++)
        {
            String id = dockerIdCopy.get(i);
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
        File[] availibleDockerFiles = CassandraDockerParameterizedTestParent.getAvailibleDockerFiles();
        List<String> cassandraVersionsToTests = cassandraVersions;
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

    public CassandraDockerParameterizedTestParent(File dockerFile)
    {
        this.cassandraVersion = dockerFile.getName();
        this.dockerFile = dockerFile;
        this.cassandraSeeds = new ArrayList<>();
        this.dockerIds = new ArrayList<>();
    }

    /**
     * Spins up a new Cassandra docker box with the specified version. Do NOT
     * forget to spin it back down in a finally block or an @AfterTest.
     *
     * @return The docker id of the box. It is important to save this off so
     * that you can spin the box back down or access information about it.
     */
    public String spinUpNewCassandraDockerBox()
    {
        String dockerId = DockerHelper.spinUpDockerBox(dockerFile.getName(), dockerFile);
        dockerIds.add(dockerId);
        cassandraSeeds.add(DockerHelper.getDockerIp(dockerId));
        return dockerId;
    }

    public void spinDownCassandraDockerBox(String containerId)
    {
        cassandraSeeds.remove(DockerHelper.getDockerIp(containerId));
        dockerIds.remove(containerId);
        DockerHelper.spinDownDockerBox(containerId);
    }

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
     * Cassandra version that is running on the above seeds/port.
     *
     * @return the cassandraVersion
     */
    public String getCassandraVersion()
    {
        return cassandraVersion;
    }
    
    public String getTestName(){
        return name.getMethodName();
    }
}
