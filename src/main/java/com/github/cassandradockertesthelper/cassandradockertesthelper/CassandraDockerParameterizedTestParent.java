package com.github.cassandradockertesthelper.cassandradockertesthelper;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

    /**
     * Cassandra seed ips to hit for this test.
     */
    private List<String> cassandraSeeds;
    /**
     * Cassandra port to hit for this test.
     */
    private int cassandraPort;

    /**
     * Cassandra version that is running on the above seeds/port.
     */
    private String cassandraVersion;

    /**
     * Docker file for this particular test.
     */
    private File dockerFile;

    public CassandraDockerParameterizedTestParent(File dockerFile)
    {
        this.cassandraVersion = dockerFile.getName();
        this.dockerFile = dockerFile;
        cassandraSeeds = new ArrayList<>();
    }

    @Parameterized.Parameters(name = "Docker File: {0}")
    public final Collection<File[]> generateParameters()
    {
        File[] availibleDockerFiles = CassandraDockerParameterizedTestParent.getAvailibleDockerFiles();
        List<String> cassandraVersionsToTests = getCassandraVersions();
        boolean testAllVersions = false;
        if (cassandraVersionsToTests == null)
        {
            testAllVersions = true;
        }
        List<File[]> toReturn = new ArrayList<>();
        for (File f : availibleDockerFiles)
        {
            if (testAllVersions || cassandraVersionsToTests.contains(f.getName().substring(0, 8)))
            {
                File[] fileArray = new File[1];
                fileArray[0] = f;
                toReturn.add(fileArray);
            }
        }
        return toReturn;
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
        cassandraSeeds.add(DockerHelper.getDockerIp(dockerId));
        return dockerId;
    }

    public void spinDownCassandraDockerBox(String containerId)
    {
        cassandraSeeds.remove(DockerHelper.getDockerIp(containerId));
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
     * Cassandra port to hit for this test.
     *
     * @return the cassandraPort
     */
    public int getCassandraPort()
    {
        return cassandraPort;
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

    public abstract List<String> getCassandraVersions();
}
