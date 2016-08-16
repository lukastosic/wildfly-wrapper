package nl.infodation.wildflywrapper.actions;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import nl.infodation.wildflywrapper.common.WildflyConnector;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeploymentActionsTest {

	private WildflyConnector wildfly;
    private DeploymentActions actions;
    
    // Place some dummy war file that you want to test with
    String appName = "authenticationserver.war";
    String appPath = "C:\\temp\\" + appName;
    
    @Before
    public void PrepareForTest()
    {
    	wildfly = new WildflyConnector("localhost", 9990, "admin", "password");
        actions = new DeploymentActions(wildfly);
    }
  
    
    @Test
    public void test_DeployFile()
    {       	
        assertEquals("Expected to make deployment", true, actions.deploy(appPath).resultStatus);        
    	assertEquals("Check if name exist (already deployed)", true, actions.deploymentExist(appName).resultStatus);
    	assertEquals("Expected to undeploy app ", true, actions.undeploy(appName).resultStatus);
    	assertEquals("Check if name exist (undeployed)", false, actions.deploymentExist(appName).resultStatus);
    }
 }
