package nl.infodation.wildflywrapper.actions;

import nl.infodation.wildflywrapper.common.*;

public class DataSourceActions {
	private String DB_CONNECTION_NAME;
	private String DB_JNDI_NAME;
	private String DB_DRIVER_NAME;
	private String DB_HOST;
	private String DB_PORT;
	private String DB_NAME;
	private String DB_USERNAME;
	private String DB_PASSWORD;

	private WildflyConnector wildfly;

	public DataSourceActions(String dbConnectionName, String dbJNDIName, String dbDriverName, String dbHost,
			String dbPort, String dbName, String dbUsername, String dbPassword, WildflyConnector wildflyConnector) {
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

	public boolean dataSourceNameExist() {
		String command = "/subsystem=datasources/data-source=" + DB_CONNECTION_NAME + ":read-resource";
		String response = wildfly.executeCLICommand(command);
		if (response.indexOf("{\"outcome\" => \"success\"") >= 0) {
			return true;
		}
		return false;
	}

	public boolean jndiNameExist() {
		String command = "/subsystem=datasources:read-resource(recursive=true)";
		String response = wildfly.executeCLICommand(command);
		if (response.indexOf("\"jndi-name\" => \"" + DB_JNDI_NAME + "\"") >= 0) {
			return true;
		}
		return false;
	}

	public boolean jdbcDriverExist() {
		String command = "/subsystem=datasources/jdbc-driver=" + DB_DRIVER_NAME + ":read-resource(recursive=true)";
		String response = wildfly.executeCLICommand(command);
		if (response.indexOf("{\"outcome\" => \"success\"") >= 0) {
			return true;
		}
		return false;
	}

	public ActionResult createJDBCDataConnection() {
		ActionResult ar = new ActionResult(true, "JDBC data source created", null);

		String dbUrl = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
		try {
			if (!dataSourceNameExist() && !jndiNameExist() && jdbcDriverExist()) {
				String command = "data-source add --jndi-name=" + DB_JNDI_NAME + " --name=" + DB_CONNECTION_NAME
						+ " --connection-url=" + dbUrl + " --driver-name=" + DB_DRIVER_NAME + " --user-name="
						+ DB_USERNAME + " --password=" + DB_PASSWORD;

				ar.wildflyMessage = wildfly.executeCLICommand(command);
				if (ar.wildflyMessage.indexOf("{\"outcome\" => \"success\"") >= 0) {
					return ar;
				} else {
					ar.resultMessage = "JDBC data source NOT created";
					ar.resultStatus = false;
					return ar;
				}
			} else {
				ar.resultMessage="Datasource not created";
				ar.resultStatus=false;
			}
		} catch (Exception e) {
			ar.setExceptionActionResult(false, e);
		}
		return ar;
	}

	public ActionResult removeJDBCDataSource() {
		ActionResult ar = new ActionResult(true, "Data source removed and reloaded exexuted", null);

		String command = "data-source remove --name=" + DB_CONNECTION_NAME;
		try {
			ar.wildflyMessage = wildfly.executeCLICommand(command);
			if (ar.wildflyMessage.indexOf("{\"outcome\" => \"success\"") >= 0) {
				if (ar.wildflyMessage.indexOf("\"process-state\" => \"reload-required\"") >= 0) {
					command = "reload";
					try {
						ar.wildflyMessage = wildfly.executeCLICommand(command);
					} catch (Exception e) {
						ar.resultMessage = "Data source removed, but reload failed due to " + e.getLocalizedMessage();
						ar.resultStatus = true;
						ar.ex =e;
					}
				} else {
					ar.resultMessage = "Data source removed, reload not necessary.";
				}
				return ar;
			}
		} catch (Exception e) {
			ar.resultMessage = e.getMessage();
			ar.resultStatus = false;
			ar.ex =e;
		}
		return ar;
	}

}
