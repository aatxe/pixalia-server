package us.aaronweiss.pixalia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.aaronweiss.pixalia.server.core.Server;
import us.aaronweiss.pixalia.server.tools.Configuration;

public class PixaliaServer {
private static final Logger logger = LoggerFactory.getLogger(PixaliaServer.class);
	
    /**
	 * @param args
	 */
	public static void main(String[] args) {
		Server server = null;
		try {
			Configuration.load();
			if (Configuration.logLocation().equals("System.out") && Configuration.logLocation().equals("System.err"))
				System.out.println("Logging to " + Configuration.logLocation());
			System.setProperty("org.slf4j.simpleLogger.logFile", Configuration.logLocation());
			System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", Configuration.logLevel());
			server = Server.getInstance();
			server.bind(Configuration.serverPort());
		} catch (Exception e) {
			logger.error("An unexpected error has occurred.", e);
			if (server != null)
				server.shutdown();
		}
	}
}
