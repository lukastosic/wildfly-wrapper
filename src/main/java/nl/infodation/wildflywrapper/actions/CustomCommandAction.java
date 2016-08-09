package nl.infodation.wildflywrapper.actions;

import nl.infodation.wildflywrapper.common.*;

public class CustomCommandAction 
{
	private WildflyConnector wildfly;
	
	public CustomCommandAction(WildflyConnector wildflyConnector)
    {
        wildfly = wildflyConnector;
    }
	
	public ActionResult executeCustomCommand(String command)
	{
		ActionResult ar = new ActionResult();
		try
		{
			ar.wildflyMessage = wildfly.executeCLICommand(command);
			ar.resultStatus = true;
			ar.resultMessage = "Command executed";
			ar.exceptionExist = false;
		}
		catch(Exception ex)
		{
			ar.setExceptionActionResult(false, ex.getMessage());
		}
		return ar;
	}
}
