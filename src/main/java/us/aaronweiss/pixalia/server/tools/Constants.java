package us.aaronweiss.pixalia.server.tools;

public class Constants {
	public static final String LOG_LOCATION = "System.err";
	public static final String LOG_LEVEL = "all";
	public static final int MAX_EVENT_THREADS = Runtime.getRuntime().availableProcessors() * 4;
	public static final long EVENT_THREAD_KEEPALIVE = 5000;
	public static final int SERVER_PORT = 2832;
	public static final int MAX_FRAME_LENGTH = 300;
	public static final int HANDLER_COUNT = 8;
	public static final String CHANNEL_PIXAL_ATTR_NAME = "pixal";
}
