package nl.infodation.wildflywrapper.actions;

import nl.infodation.wildflywrapper.common.*;

public class CustomCommandAction {
	private WildflyConnector wildfly;

	public CustomCommandAction(WildflyConnector wildflyConnector) {
		wildfly = wildflyConnector;
	}

	public ActionResult executeCustomCommand(String command) {
		ActionResult ar = new ActionResult(true, "Command executed", null);
		try {
			ar.wildflyMessage = wildfly.executeCLICommand(command);
		} catch (Exception ex) {
			ar.setExceptionActionResult(false, ex);
		}
		return ar;
	}
}
