package nl.infodation.wildflywrapper.actions;

import nl.infodation.wildflywrapper.common.*;

public class DeploymentActions {
	private WildflyConnector wildfly;

	public DeploymentActions(WildflyConnector wildflyConnector) {
		wildfly = wildflyConnector;
	}

	public ActionResult deploymentExist(String appName) {
		ActionResult ar = new ActionResult(true, "Application " + appName + " exists on server", null);
		String command = "deployment-info";
		try {
			ar.wildflyMessage = wildfly.executeCLICommand(command);
			if (ar.wildflyMessage.indexOf(appName) < 0) {
				ar.resultMessage = "Application " + appName + " does not exist on server";
				ar.resultStatus = false;
			}
		} catch (Exception ex) {
			ar.setExceptionActionResult(false, ex);
		}
		return ar;
	}

	public ActionResult undeploy(String appName) {
		ActionResult ar = new ActionResult(true, "Application " + appName + " undeployed", null);
		String command = "undeploy " + appName;
		try {
			ar.wildflyMessage = wildfly.executeCLICommand(command);
			if (ar.wildflyMessage.indexOf("{\"outcome\" => \"failed\"") >= 0) {
				ar.resultMessage = "Error undeploying " + appName;
				ar.resultStatus = false;
			}
		} catch (Exception ex) {
			ar.setExceptionActionResult(false, ex);
		}
		return ar;
	}

	public ActionResult deploy(String pathToFile) {
		ActionResult ar = new ActionResult(true, "Application " + pathToFile + " successfuly deployed.", null);
		String command = "deploy " + pathToFile;

		try {
			ar.wildflyMessage = wildfly.executeCLICommand(command);
			if (ar.wildflyMessage.indexOf("{\"outcome\" => \"failed\"") >= 0) {
				ar.resultStatus = false;
				ar.resultMessage = "Deployment FAILED for application " + pathToFile;
			}
		} catch (Exception ex) {
			ar.setExceptionActionResult(false, ex);
		}
		return ar;
	}
}
