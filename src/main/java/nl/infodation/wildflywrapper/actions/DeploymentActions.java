package nl.infodation.wildflywrapper.actions;

import nl.infodation.wildflywrapper.common.*;

public class DeploymentActions 
{
	private WildflyConnector wildfly;
	
	public DeploymentActions(WildflyConnector wildflyConnector)
    {
        wildfly = wildflyConnector;
    }
	
	public ActionResult deploymentExist(String appName)
	{
		ActionResult ar = new ActionResult();		
		String command = "deployment-info";		
		try
		{
			ar.wildflyMessage = wildfly.executeCLICommand(command);
			if(ar.wildflyMessage.indexOf(appName) >= 0)
			{
				ar.resultMessage = "Application "+appName+" exists on server";				
			}
			else
			{
				ar.resultMessage = "Application "+appName + " does not exist on server";
				ar.resultStatus = false;
			}
		}
		catch(Exception ex)
		{
			ar.setExceptionActionResult(false, ex.getMessage());
		}
		return ar;
	}
	
	public ActionResult undeploy(String appName)
	{
		ActionResult ar = new ActionResult();		
		String command = "undeploy "+appName;
		try
		{
			ar.wildflyMessage = wildfly.executeCLICommand(command);					
			if(ar.wildflyMessage.indexOf("{\"outcome\" => \"failed\"") >= 0)
			{				
				ar.resultMessage = "Error undeploying "+appName;
				ar.resultStatus = false;
			}
			else
			{
				ar.resultMessage = "Application "+appName+" undeployed";
			}
		}
		catch(Exception ex)
		{
			ar.setExceptionActionResult(false, ex.getMessage());
		}
		return ar;
	}
	
	public ActionResult deploy(String pathToFile)
	{
		ActionResult ar = new ActionResult();	
		String command = "deploy "+pathToFile;
		
		try
		{
			ar.wildflyMessage = wildfly.executeCLICommand(command);
			if(ar.wildflyMessage.indexOf("{\"outcome\" => \"failed\"") >= 0)
			{			
				ar.resultStatus = false;
				ar.resultMessage = "Deployment FAILED for application "+pathToFile;
			}
			else
			{
				ar.resultMessage = "Application "+pathToFile+" successfuly deployed.";
			}
		}
		catch(Exception ex)
		{
			ar.setExceptionActionResult(false, ex.getMessage());
		}
		return ar;
	}
}
