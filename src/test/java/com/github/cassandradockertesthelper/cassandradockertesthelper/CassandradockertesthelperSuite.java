/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.cassandradockertesthelper.cassandradockertesthelper;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author jeffrey
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(
{
    com.github.cassandradockertesthelper.cassandradockertesthelper.DockerHelperTest.class, com.github.cassandradockertesthelper.cassandradockertesthelper.CassandraDockerParameterizedTestParentTest.class
})
public class CassandradockertesthelperSuite
{

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp() throws Exception
    {
    }

    @After
    public void tearDown() throws Exception
    {
    }
    
}
