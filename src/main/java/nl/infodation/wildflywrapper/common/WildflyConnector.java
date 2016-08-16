package nl.infodation.wildflywrapper.common;

import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.scriptsupport.CLI;
import org.jboss.dmr.ModelNode;

public class WildflyConnector {

	private String JBOSS_HOST;
	private int JBOSS_ADMIN_PORT;
	private String JBOSS_ADMIN_USERNAME;
	private String JBOSS_ADMIN_PASSWORD;

	private CLI jbossCLI;
	private CLI.Result cliResult;

	public WildflyConnector(String host, int port, String uname, String password) {
		JBOSS_HOST = host;
		JBOSS_ADMIN_PORT = port;
		JBOSS_ADMIN_USERNAME = uname;
		JBOSS_ADMIN_PASSWORD = password;
	}

	private void connect() {
		if (jbossCLI == null) {
			jbossCLI = CLI.newInstance();
			jbossCLI.connect(JBOSS_HOST, JBOSS_ADMIN_PORT, JBOSS_ADMIN_USERNAME, JBOSS_ADMIN_PASSWORD.toCharArray());
		}
	}

	/**
	 * Disconnect from server. Does nothing when already disconnected.
	 */
	private synchronized void disconnect() {
		if (isConnected()) {
			jbossCLI.disconnect();
		}
	}

	private synchronized boolean isConnected() {
		if (jbossCLI != null) {
			CommandContext ctx = jbossCLI.getCommandContext();
			if (ctx != null) {
				if (!ctx.isTerminated()) {
					return true;
				}
			}
		}
		return false;
	}

	private String getWildFlyResponse(CLI.Result result) {
		ModelNode modelNode = result.getResponse();
		String response = modelNode.asString();
		return response;
	}

	public String executeCLICommand(String command) {
		connect();
		cliResult = jbossCLI.cmd(command);
		return getWildFlyResponse(cliResult);
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WildflyConnector [" + (JBOSS_HOST != null ? "JBOSS_HOST=" + JBOSS_HOST + ", " : "")
				+ "JBOSS_ADMIN_PORT=" + JBOSS_ADMIN_PORT + ", "
				+ (JBOSS_ADMIN_USERNAME != null ? "JBOSS_ADMIN_USERNAME=" + JBOSS_ADMIN_USERNAME : "") + "]";
	}

	/**
	 * Disconnect when this object is reaped
	 * 
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		disconnect();
		super.finalize();
	}

}
