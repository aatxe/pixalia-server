package us.aaronweiss.pixalia.server.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configuration {
    private static final Logger logger = LoggerFactory.getLogger(Configuration.class);
	private static Properties config;
	
	public static void load() throws IOException {
		config = new Properties();
		try {
			config.load(new FileInputStream(new File("rsc/server.cfg")));
			logger.info("Loaded rsc/server.cfg");
		} catch (FileNotFoundException e) {
			logger.info("Could not find rsc/server.cfg");
			config.put("server_port", String.valueOf(Constants.SERVER_PORT));
			config.put("log_location", String.valueOf(Constants.LOG_LOCATION));
			config.put("log_level", String.valueOf(Constants.LOG_LEVEL));
			config.put("max_event_threads", String.valueOf(Constants.MAX_EVENT_THREADS));
			config.put("event_thread_keepalive", String.valueOf(Constants.EVENT_THREAD_KEEPALIVE));
			config.store(new FileOutputStream(new File("rsc/server.cfg")), " Pixalia Configuration");
			logger.info("Created new rsc/server.cfg");
		}
	}
	
	public static int serverPort() {
		return Integer.parseInt(config.getProperty("server_port"));
	}
	
	public static String logLocation() {
		return config.getProperty("log_location");
	}
	
	public static String logLevel() {
		return config.getProperty("log_level");
	}
	
	public static int maxEventThreads() {
		return Integer.parseInt(config.getProperty("max_event_threads"));
	}
	
	public static long eventThreadKeepAlive() {
		return Long.parseLong(config.getProperty("event_thread_keepalive"));
	}
}
