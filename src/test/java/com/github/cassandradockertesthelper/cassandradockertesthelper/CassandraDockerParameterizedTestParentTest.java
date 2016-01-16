/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.cassandradockertesthelper.cassandradockertesthelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jeffrey
 */
public class CassandraDockerParameterizedTestParentTest extends CassandraDockerParameterizedTestParent
{

    /**
     * Logger for this class.
     */
    private static final Logger logger = LoggerFactory.getLogger(CassandraDockerParameterizedTestParentTest.class);

    private String dockerId;

    public CassandraDockerParameterizedTestParentTest(File dockerFile)
    {
        super(dockerFile);
    }

    @BeforeClass
    public static void setUpClass()
    {
        cassandraVersions = getCassandraVersions();//this breaks so many programming conventions that it makes my head hurt.
    }

    @AfterClass
    public static void tearDownClass()
    {
    }

    @Before
    public void setUp()
    {
        dockerId = super.spinUpNewCassandraDockerBox();
    }


    public static List<String> getCassandraVersions(){
        List<String> versions = new ArrayList<>();
        versions.add("2.0.7");
        versions.add("2.0.8");
        versions.add("2.0.9");
        versions.add("2.0.10");
        versions.add("2.0.11");
        return versions;
    }

    @Test
    public void testCassandraParamaterizedTests()
    {
        logger.info("Testing with cassandra.");
        assertTrue(true);
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
