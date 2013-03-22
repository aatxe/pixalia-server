package us.aaronweiss.pixalia.server.tools;

public class Constants {
	public static final String LOG_LOCATION = "System.err";
	public static final String LOG_LEVEL = "info";
	public static final int MAX_EVENT_THREADS = Runtime.getRuntime().availableProcessors() * 4;
	public static final long EVENT_THREAD_KEEPALIVE = 5000;
	public static final int SERVER_PORT = 2832;
}
