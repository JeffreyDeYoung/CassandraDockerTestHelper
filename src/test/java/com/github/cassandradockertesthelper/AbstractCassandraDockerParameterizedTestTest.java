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

import java.io.File;
import java.util.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test class for AbstractCassandraDockerParameterizedTest. Also is an example
 * on how to properly use CassandraDockerParameterizedTestParent and
 * demonstrates the features of this test library. Look for comments that are
 * marked with "*you must do this*" for hints on how to use
 * CassandraDockerParameterizedTestParent correctly.
 *
 * @author jeffrey
 */
public class AbstractCassandraDockerParameterizedTestTest extends AbstractCassandraDockerParameterizedTest
//note that we are extending the parent here: *you must do this*
{

    /**
     * Logger for this class.
     */
    private static final Logger logger = LoggerFactory.getLogger(AbstractCassandraDockerParameterizedTestTest.class);

    /**
     * Constructor. Must follow this format. The file parameter is provided by
     * the parameterized tests (defined by generateParameters in the parent
     * class). *you must do this*
     *
     * @param dockerFile Parameter for this test. It's the docker file that
     * represents the version of Cassandra we are testing.
     */
    public AbstractCassandraDockerParameterizedTestTest(File dockerFile)
    {
        super(dockerFile);//call to super class to actually setup this test. *you must do this*
    }

    /**
     * Set up class method. This method is static and will run prior to any
     * tests, but only once: at class establishment.
     */
    @BeforeClass
    public static void setUpClass()
    {
    }

    @Before
    public void setUp()
    {
        //Generally, you will want to Spin up your cassandra box as part of your setup method. However, for this test particular, we are not going to.
        //String dockerId = super.spinUpNewCassandraDockerBox();
    }

    /**
     * Example test with lots of method calls. Read though this method if you
     * want to know how to use the parent class.
     */
    @Test
    public void testCassandraParamaterizedTests()
    {
        assertNotNull(super.getTestName());
        logger.info("Testing with Cassandra. Test name (with paramter name)" + super.getTestName());//Shows how to get the test name with the current parameter
        assertNotNull(super.getCassandraVersion());
        logger.info("Testing Cassandra version: " + super.getCassandraVersion());//Shows how to get the Cassandra version we are testing against.
        String firstDockerId = super.spinUpNewCassandraDockerBox();//creates a NEW docker instance with the specified version of cassandra for this parameterized test run.
        assertNotNull(firstDockerId);
        logger.info("Created new Cassandra Docker box: " + firstDockerId);
        //note, we are NOT spining down the box we created. We are relying on the parent class to do that automatically at the end of this test
        String firstIp = DockerHelper.getDockerIp(firstDockerId);//we can get the ip from the docker helper based on the instance id
        assertNotNull(firstIp);
        logger.info("Ip of new Cassandra box: " + firstIp);
        //or we can get the ip as a list of seeds
        List<String> seeds = super.getCassandraSeeds();
        assertEquals(1, seeds.size());//we should have one see at this point
        assertEquals(firstIp, seeds.get(0));

        //lets add a second cassandra box (note, this does not actually join them in a cluster;
        //that's up to you. See CassandraCurator for automation if you want it .)
        String secondDockerId = super.spinUpNewCassandraDockerBox();
        assertNotNull(secondDockerId);
        logger.info("Created second new Cassandra Docker box: " + secondDockerId);
        seeds = super.getCassandraSeeds();
        assertEquals(2, seeds.size());//we should have one see at this point
        String secondIp = DockerHelper.getDockerIp(secondDockerId);
        logger.info("Ip of new Cassandra box: " + secondIp);
        assertEquals(secondIp, seeds.get(1));

        //the afterTest in the parent class should clean up all the instances created during each test run.
    }

    /**
     * Test of getAvailibleDockerFiles method, of class
     * CassandraDockerParameterizedTestParent.
     */
    @Test
    public void testGetAvailibleDockerFiles()
    {
        System.out.println("getAvailibleDockerFiles");
        File[] result = AbstractCassandraDockerParameterizedTest.getAvailibleDockerFiles();
        assertTrue(result.length > 1);//make sure we have at least one availible docker file (to make sure we are reading them properly from the fs)
    }

}
