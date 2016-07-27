package nl.infodation.wildflywrapper.datasource;

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
	
	public DataSourceActions(String dbConnectionName, String dbJNDIName, String dbDriverName, String dbHost, String dbPort, String dbName, String dbUsername, String dbPassword)
    {
        DB_CONNECTION_NAME = dbConnectionName;
        DB_JNDI_NAME = dbJNDIName;
        DB_DRIVER_NAME = dbDriverName;
        DB_HOST = dbHost;
        DB_PORT = dbPort;
        DB_NAME = dbName;
        DB_USERNAME = dbUsername;
        DB_PASSWORD = dbPassword;
    }
	
	public boolean dataSourceNameExist(WildflyConnector wildfly)
	{
		String command = "/subsystem=datasources/data-source="+DB_CONNECTION_NAME+":read-resource";
		String response = wildfly.executeCLICommand(command);
		if (response.indexOf("{\"outcome\" => \"success\"") >= 0)
		{
			return true;
		}
		return false;
	}
	
	public boolean jndiNameExist(WildflyConnector wildfly)
	{
		String command = "/subsystem=datasources:read-resource(recursive=true)";
		String response = wildfly.executeCLICommand(command);
		if (response.indexOf("\"jndi-name\" => \""+DB_JNDI_NAME+"\"") >= 0)
		{
			return true;
		}
		return false;
	}
	
	public boolean jdbcDriverExist(WildflyConnector wildfly)
	{
		String command = "/subsystem=datasources/jdbc-driver="+DB_DRIVER_NAME+":read-resource(recursive=true)";
		String response = wildfly.executeCLICommand(command);
		if (response.indexOf("{\"outcome\" => \"success\"") >= 0)
		{
			return true;
		}
		return false;
	}
	
	public boolean createJDBCDataConnection(WildflyConnector wildfly)
	{
		String dbUrl = "jdbc:mysql://"+DB_HOST+":"+DB_PORT+"/"+DB_NAME;
		try
		{
			if(!dataSourceNameExist(wildfly) && !jndiNameExist(wildfly) && jdbcDriverExist(wildfly))
			{
				String command = "data-source add --jndi-name="+DB_JNDI_NAME+" --name="+DB_CONNECTION_NAME+
						" --connection-url="+dbUrl+" --driver-name="+DB_DRIVER_NAME+" --user-name="+DB_USERNAME+" --password="+DB_PASSWORD;
				
				String response = wildfly.executeCLICommand(command);
				if (response.indexOf("{\"outcome\" => \"success\"") >= 0)
				{
					return true;
				}
				return false;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean removeJDBCDataSource(WildflyConnector wildfly)
	{
		String command = "data-source remove --name=mariaDS";
		try
		{
			String response = wildfly.executeCLICommand(command);
			if (response.indexOf("{\"outcome\" => \"success\"") >= 0)
			{
				if (response.indexOf("\"process-state\" => \"reload-required\"") >= 0)
				{
					command = "reload";
					response = wildfly.executeCLICommand(command);					
				}
				return true;
			}			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	
}
