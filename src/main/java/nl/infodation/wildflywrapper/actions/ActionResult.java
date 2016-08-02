package nl.infodation.wildflywrapper.actions;

public class ActionResult {
	
	public boolean resultStatus;
	public String resultMessage;
	public String wildflyMessage;
	public boolean exceptionExist;
	
	public ActionResult()
	{
		exceptionExist = false;
		resultStatus = true;
	}
	
	public ActionResult(boolean status, String message, String wfMessage)
	{
		resultStatus = status;
		resultMessage = message;
		wildflyMessage = wfMessage;
	}
	
	public void setActionResult(boolean status, String message, String wfMessage)
	{
		resultStatus = status;
		resultMessage = message;
		wildflyMessage = wfMessage;
	}
	
	public void setExceptionActionResult(boolean status, String ex)
	{
		resultStatus = status;
		wildflyMessage = ex;
		resultMessage = "ERROR";
		exceptionExist = true;
	}
}
