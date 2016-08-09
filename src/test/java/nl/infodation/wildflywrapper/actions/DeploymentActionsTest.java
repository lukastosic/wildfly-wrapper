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
    String appName = "test-deployment.war";
    String appPath = "D:\\Workspace\\test-deployment.war";
    
    @Before
    public void PrepareForTest()
    {
    	wildfly = new WildflyConnector("10.4.1.218", 9601, "admin", "password");
        actions = new DeploymentActions(wildfly);
    }
  
    
    @Test
    public void test_1_DeployFile()
    {       	
        assertEquals("Expected to make deployment", true, actions.deploy(appPath).resultStatus);        
    }
    
    @Test
    public void test_2_CheckIfNameExists()
    {
    	assertEquals("Check if name exist (already deployed)", true, actions.deploymentExist(appName).resultStatus);
    }
    
    @Test
    public void test_3_UndeployApp()
    {
    	assertEquals("Expected to undeploy app ", true, actions.undeploy(appName).resultStatus);
    }
    
    @Test
    public void test_4_CheckForNameAfterUndeploy()
    {
    	assertEquals("Check if name exist (undeployed)", false, actions.deploymentExist(appName).resultStatus);
    }
}
