package nl.infodation.wildflywrapper.common;

import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.scriptsupport.CLI;
import org.jboss.dmr.ModelNode;

import nl.infodation.wildflywrapper.actions.ActionResult;

public class WildflyConnector {
	
	private static final long TIMEOUT_PERIOD = 60 * 1000; // 1 MINUTE

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
	
	/**
	 * Tries to execute a read-only command.  
	 * @return true when command could be executed.
	 */
	public boolean ping() {
		String s = getWildFlyResponse(jbossCLI.cmd("/:read-attribute(name=launch-type)"));
		if (s != null) {
			// {"outcome" => "success","result" => "STANDALONE"} 
			return s.contains("{\"outcome\" => \"success\")");
		}
		return false;
	}
	
	public ActionResult reload() {
		String command = "reload";
		ActionResult ar = new ActionResult(true,"RELOADED", null);
		try {
			ar.wildflyMessage = executeCLICommand(command);
		} catch (Exception e) {
			ar.resultMessage = "Reload failed due to " + e.getLocalizedMessage();
			ar.resultStatus = true;
			ar.ex =e;
			return ar;
		}
		long timeout = System.currentTimeMillis() + TIMEOUT_PERIOD;
		while (!ping() && System.currentTimeMillis() < timeout) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// noop
			}
		}
		if (System.currentTimeMillis() >= timeout) {
			return new ActionResult(false, "Unable to ping wildfly " + JBOSS_HOST + ":" + JBOSS_ADMIN_PORT + " after 10 seconds. Reload failed", null);
		}
		return new ActionResult(true, "Reload successful", null);
	}
	

	private synchronized boolean isConnected() {
		if (jbossCLI != null) {
			CommandContext ctx = jbossCLI.getCommandContext();
			if (ctx != null) {
				if (!ctx.isTerminated()) {
					if (ping()) {
						return true;
					}
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
		try {
			disconnect();
		} catch (Throwable e) { // workaround for CLI disconnect bug
			new RuntimeException("Failed to close connection",e).printStackTrace();
		}
		super.finalize();
	}
}
