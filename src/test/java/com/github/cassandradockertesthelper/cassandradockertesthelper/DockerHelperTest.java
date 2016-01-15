package com.github.cassandradockertesthelper.cassandradockertesthelper;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNotSame;
import static junit.framework.TestCase.assertTrue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jeffrey
 */
@RunWith(value = Parameterized.class)
public class DockerHelperTest
{

    /**
     * Logger for this class.
     */
    private static final Logger logger = LoggerFactory.getLogger(DockerHelperTest.class);

    /**
     * Docker file that this particular test is running with.
     */
    private final File dockerFile;
    /*
     * @param dockerFile DockerFile to test against.
     */

    public DockerHelperTest(File dockerFile)
    {
        this.dockerFile = dockerFile;
    }

    @BeforeClass
    public static void setUpClass()
    {
    }

    @AfterClass
    public static void tearDownClass()
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    @Parameterized.Parameters(name = "Cassandra file: {0}")
    public static final Collection<File[]> generateDockerFileNames()
    {
        File dir = new File("./src/test/resources/docker/");
        File[] cassandraDockerFiles = dir.listFiles(new FilenameFilter()
        {

            @Override
            public boolean accept(File dir, String name)
            {
                return (name.startsWith("cassandra") && !name.endsWith("~"));//cassandra docker boxes that are not gedit backups                
            }
        });
        List<File[]> toReturn = new ArrayList<>();
        for (File f : cassandraDockerFiles)
        {
            File[] fileArray = new File[1];
            fileArray[0] = f;
            toReturn.add(fileArray);
        }
        return toReturn;
    }

    @Test
    public void testCycle() throws Exception
    {
        logger.info("Testing docker helper. " + dockerFile.getName());
        String id = DockerHelper_Old.spinUpDockerBox(dockerFile.getName(), dockerFile);
        assertTrue(DockerHelper_Old.isBoxRunning(id));
        assertNotNull(DockerHelper_Old.getDockerIp(id));
        DockerHelper_Old.spinDownDockerBox(id);
        assertFalse(DockerHelper_Old.isBoxRunning(id));
    }

    @Test
    public void testCycleTwoBoxes() throws Exception
    {
        logger.info("Testing docker helper with two boxes. " + dockerFile.getName());
        String id1 = DockerHelper_Old.spinUpDockerBox(dockerFile.getName(), dockerFile);
        String id2 = DockerHelper_Old.spinUpDockerBox(dockerFile.getName(), dockerFile);
        assertTrue(DockerHelper_Old.isBoxRunning(id1));
        assertTrue(DockerHelper_Old.isBoxRunning(id2));
        assertNotNull(DockerHelper_Old.getDockerIp(id1));
        assertNotNull(DockerHelper_Old.getDockerIp(id2));
        assertNotSame(DockerHelper_Old.getDockerIp(id1), DockerHelper_Old.getDockerIp(id2));
        DockerHelper_Old.spinDownDockerBox(id1);
        assertFalse(DockerHelper_Old.isBoxRunning(id1));
        assertTrue(DockerHelper_Old.isBoxRunning(id2));
        DockerHelper_Old.spinDownDockerBox(id2);
        assertFalse(DockerHelper_Old.isBoxRunning(id2));
    }

}
