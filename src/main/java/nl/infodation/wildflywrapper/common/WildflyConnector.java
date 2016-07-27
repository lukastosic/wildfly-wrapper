package nl.infodation.wildflywrapper.common;

import org.jboss.as.cli.scriptsupport.*;
import org.jboss.dmr.ModelNode;

public class WildflyConnector {
	
	private String JBOSS_HOST;
	private int JBOSS_ADMIN_PORT;
	private String JBOSS_ADMIN_USERNAME;
	private String JBOSS_ADMIN_PASSWORD;
	
	private CLI jbossCLI;
	private CLI.Result cliResult;
	
	public WildflyConnector(String host, int port, String uname, String password)
	{
		JBOSS_HOST = host;
		JBOSS_ADMIN_PORT = port;
		JBOSS_ADMIN_USERNAME = uname;
		JBOSS_ADMIN_PASSWORD = password;
	}
	
	private void connect()
	{
		if(jbossCLI == null)
		{
			jbossCLI = CLI.newInstance();
			jbossCLI.connect(JBOSS_HOST, JBOSS_ADMIN_PORT, JBOSS_ADMIN_USERNAME, JBOSS_ADMIN_PASSWORD.toCharArray());
		}
	}
	
	private String getWildFlyResponse(CLI.Result result) 
	{		
       	ModelNode modelNode = result.getResponse();
    	String response = modelNode.asString();     	
    	return response;
    }
	
	public String executeCLICommand(String command)
	{
		connect();
		cliResult = jbossCLI.cmd(command);
		return getWildFlyResponse(cliResult);
	}

}
