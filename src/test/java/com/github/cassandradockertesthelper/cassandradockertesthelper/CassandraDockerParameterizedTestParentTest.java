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
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test class for CassandraDockerParameterizedTestParent. Also is an example on
 * how to properly use CassandraDockerParameterizedTestParent and demonstrates
 * the features of this test library. Look for comments that are marked with
 * "*you must do this*" for hints on how to use
 * CassandraDockerParameterizedTestParent correctly.
 *
 * @author jeffrey
 */
public class CassandraDockerParameterizedTestParentTest extends CassandraDockerParameterizedTestParent//note that we are extending the parent here: *you must do this*
{

    /**
     * Logger for this class.
     */
    private static final Logger logger = LoggerFactory.getLogger(CassandraDockerParameterizedTestParentTest.class);

    /**
     * Constructor. Must follow this format. The file parameter is provided by
     * the parameterized tests (defined by generateParameters in the parent
     * class). *you must do this*
     *
     * @param dockerFile Parameter for this test. It's the docker file that
     * represents the version of Cassandra we are testing.
     */
    public CassandraDockerParameterizedTestParentTest(File dockerFile)
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
        /* if you want to set specific versions of cassandra you want your tests 
         to run against, you must set the static variable "cassandraVersions" in 
         a @BeforeClass method. *you must do this* if you want to set the 
         cassandra versions. If you do not set this, the test will run against all
         cassandra versions availible. 
         */
        cassandraVersions = getCassandraVersions();//this breaks so many programming conventions that it makes my head hurt.
    }


    @Before
    public void setUp()
    {
        //Generally, you will want to Spin up your cassandra box as part of your setup method. However, for this test particular, we are not going to.
        //String dockerId = super.spinUpNewCassandraDockerBox();
    }

    /**
     * Gets a simple String List of all the Cassandra versions we want to test
     * against. See setUpClass() and related comments.
     *
     * @return A list of Cassandra versions to test against.
     */
    public static List<String> getCassandraVersions()
    {
        List<String> versions = new ArrayList<>();
        versions.add("2.0.7");
        versions.add("2.0.8");
        versions.add("2.0.9");
        versions.add("2.0.10");
        versions.add("2.0.11");
        return versions;
    }

    /**
     * Example test with lots of method calls. 
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
        
        
    }

//    /**
//     * Test of generateParameters method, of class CassandraDockerParameterizedTestParent.
//     */
//    @Test
//    public void testGenerateParameters()
//    {
//        System.out.println("generateParameters");
//        CassandraDockerParameterizedTestParent instance = null;
//        Collection expResult = null;
//        Collection result = instance.generateParameters();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of spinUpNewCassandraDockerBox method, of class CassandraDockerParameterizedTestParent.
//     */
//    @Test
//    public void testSpinUpNewCassandraDockerBox()
//    {
//        System.out.println("spinUpNewCassandraDockerBox");
//        CassandraDockerParameterizedTestParent instance = null;
//        String expResult = "";
//        String result = instance.spinUpNewCassandraDockerBox();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of spinDownCassandraDockerBox method, of class CassandraDockerParameterizedTestParent.
//     */
//    @Test
//    public void testSpinDownCassandraDockerBox()
//    {
//        System.out.println("spinDownCassandraDockerBox");
//        String containerId = "";
//        CassandraDockerParameterizedTestParent instance = null;
//        instance.spinDownCassandraDockerBox(containerId);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getAvailibleDockerFiles method, of class CassandraDockerParameterizedTestParent.
//     */
//    @Test
//    public void testGetAvailibleDockerFiles()
//    {
//        System.out.println("getAvailibleDockerFiles");
//        File[] expResult = null;
//        File[] result = CassandraDockerParameterizedTestParent.getAvailibleDockerFiles();
//        assertArrayEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getCassandraSeeds method, of class CassandraDockerParameterizedTestParent.
//     */
//    @Test
//    public void testGetCassandraSeeds()
//    {
//        System.out.println("getCassandraSeeds");
//        CassandraDockerParameterizedTestParent instance = null;
//        List<String> expResult = null;
//        List<String> result = instance.getCassandraSeeds();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getCassandraPort method, of class CassandraDockerParameterizedTestParent.
//     */
//    @Test
//    public void testGetCassandraPort()
//    {
//        System.out.println("getCassandraPort");
//        CassandraDockerParameterizedTestParent instance = null;
//        int expResult = 0;
//        int result = instance.getCassandraPort();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getCassandraVersion method, of class CassandraDockerParameterizedTestParent.
//     */
//    @Test
//    public void testGetCassandraVersion()
//    {
//        System.out.println("getCassandraVersion");
//        CassandraDockerParameterizedTestParent instance = null;
//        String expResult = "";
//        String result = instance.getCassandraVersion();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getCassandraVersions method, of class CassandraDockerParameterizedTestParent.
//     */
//    @Test
//    public void testGetCassandraVersions()
//    {
//        System.out.println("getCassandraVersions");
//        CassandraDockerParameterizedTestParent instance = null;
//        List<String> expResult = null;
//        List<String> result = instance.getCassandraVersions();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    public class CassandraDockerParameterizedTestParentImpl extends CassandraDockerParameterizedTestParent
//    {
//
//        public CassandraDockerParameterizedTestParentImpl()
//        {
//            super(null);
//        }
//
//        public List<String> getCassandraVersions()
//        {
//            return null;
//        }
//    }
//
//    public class CassandraDockerParameterizedTestParentImpl extends CassandraDockerParameterizedTestParent
//    {
//
//        public CassandraDockerParameterizedTestParentImpl()
//        {
//            super(null);
//        }
//
//        public List<String> getCassandraVersions()
//        {
//            return null;
//        }
//    }
}
