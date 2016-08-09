package nl.infodation.wildflywrapper.actions;

import nl.infodation.wildflywrapper.common.*;

public class DataSourceActions 
{
	private String DB_CONNECTION_NAME;
	private String DB_JNDI_NAME;
	private String DB_DRIVER_NAME;
	private String DB_HOST;		
	private String DB_PORT;
	private String DB_NAME;
	private String DB_USERNAME;
	private String DB_PASSWORD;
	
	private WildflyConnector wildfly;
	
	public DataSourceActions(String dbConnectionName, String dbJNDIName, String dbDriverName, String dbHost, String dbPort, String dbName, String dbUsername, String dbPassword, WildflyConnector wildflyConnector)
    {
        DB_CONNECTION_NAME = dbConnectionName;
        DB_JNDI_NAME = dbJNDIName;
        DB_DRIVER_NAME = dbDriverName;
        DB_HOST = dbHost;
        DB_PORT = dbPort;
        DB_NAME = dbName;
        DB_USERNAME = dbUsername;
        DB_PASSWORD = dbPassword;
        
        wildfly = wildflyConnector;
    }
	
	public boolean dataSourceNameExist()
	{
		String command = "/subsystem=datasources/data-source="+DB_CONNECTION_NAME+":read-resource";
		String response = wildfly.executeCLICommand(command);
		if (response.indexOf("{\"outcome\" => \"success\"") >= 0)
		{
			return true;
		}
		return false;
	}
	
	public boolean jndiNameExist()
	{
		String command = "/subsystem=datasources:read-resource(recursive=true)";
		String response = wildfly.executeCLICommand(command);
		if (response.indexOf("\"jndi-name\" => \""+DB_JNDI_NAME+"\"") >= 0)
		{
			return true;
		}
		return false;
	}
	
	public boolean jdbcDriverExist()
	{
		String command = "/subsystem=datasources/jdbc-driver="+DB_DRIVER_NAME+":read-resource(recursive=true)";
		String response = wildfly.executeCLICommand(command);
		if (response.indexOf("{\"outcome\" => \"success\"") >= 0)
		{
			return true;
		}
		return false;
	}
	
	public ActionResult createJDBCDataConnection()
	{
		ActionResult ar = new ActionResult();
		
		String dbUrl = "jdbc:mysql://"+DB_HOST+":"+DB_PORT+"/"+DB_NAME;
		try
		{
			if(!dataSourceNameExist() && !jndiNameExist() && jdbcDriverExist())
			{
				String command = "data-source add --jndi-name="+DB_JNDI_NAME+" --name="+DB_CONNECTION_NAME+
						" --connection-url="+dbUrl+" --driver-name="+DB_DRIVER_NAME+" --user-name="+DB_USERNAME+" --password="+DB_PASSWORD;
				
				ar.wildflyMessage = wildfly.executeCLICommand(command);
				if (ar.wildflyMessage.indexOf("{\"outcome\" => \"success\"") >= 0)
				{
					ar.exceptionExist = false;
					ar.resultMessage = "JDBC data source created";
					ar.resultStatus = true;
					return ar;
				}
				ar.exceptionExist = false;
				ar.resultMessage = "JDBC data source NOT created";
				ar.resultStatus = false;
				return ar;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ar.setExceptionActionResult(false, e.getMessage());
		}
		return ar;
	}
	
	public ActionResult removeJDBCDataSource()
	{
		ActionResult ar = new ActionResult();
		
		
		String command = "data-source remove --name=mariaDS";
		try
		{
			ar.wildflyMessage = wildfly.executeCLICommand(command);
			if (ar.wildflyMessage.indexOf("{\"outcome\" => \"success\"") >= 0)
			{
				if (ar.wildflyMessage.indexOf("\"process-state\" => \"reload-required\"") >= 0)
				{
					command = "reload";
					try
					{
						ar.wildflyMessage = wildfly.executeCLICommand(command);
						ar.exceptionExist = false;
						ar.resultMessage = "Data source removed and reloaded exexuted";
						ar.resultStatus = true;
					}
					catch(Exception e)
					{
						ar.exceptionExist = true;
						ar.resultMessage = "Data source removed, but reload failed. Exception details "+e.getMessage();
						ar.resultStatus = true;
					}
				}
				else
				{
					ar.resultMessage = "Data source removed, reload not necessary.";
					ar.resultStatus = true;
					ar.exceptionExist = false;
				}
				return ar;
			}			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ar.resultMessage = e.getMessage();
			ar.exceptionExist = true;
			ar.resultStatus = false;
		}
		return ar;
	}
	
	
}
