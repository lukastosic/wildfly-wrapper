package nl.infodation.wildflywrapper.actions;

public class ActionResult {
	
	public boolean resultStatus = true;
	public String resultMessage;
	public String wildflyMessage;
	public Exception ex = null;
	
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
	
	public void setExceptionActionResult(boolean status, Exception  exception)
	{
		if (exception == null) {
			setExceptionActionResult(status, "UNDEFINED ERROR", exception);
		} else {
			setExceptionActionResult(status, "ERROR due to " + exception.getLocalizedMessage(), exception);
		}
	}

	public void setExceptionActionResult(boolean status,String msg,  Exception  exception)
	{
		resultStatus = status;
		resultMessage = msg;
		ex = exception;
		if (ex != null)
		wildflyMessage = ex.getLocalizedMessage();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ActionResult [resultStatus=" + resultStatus + ", "
				+ (resultMessage != null ? "resultMessage=" + resultMessage + ", " : "")
				+ (wildflyMessage != null ? "wildflyMessage=" + wildflyMessage + ", " : "")
				+ (ex != null ? "ex=" + ex : "") + "]";
	}
	
}
