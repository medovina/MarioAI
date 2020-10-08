package engine.helper;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MarioLog {
	
	public static final Level INITIAL_LOG_LEVEL = Level.INFO;

	public static final Logger LOG;
	
	private static boolean enabled = true;
	
	private static Level lastLevel = INITIAL_LOG_LEVEL;
	
	public static void error(String msg) {
		log(Level.SEVERE, msg);
	}
	
	public static void warn(String msg) {
		log(Level.WARNING, msg);
	}
	
	public static void info(String msg) {
		log(Level.INFO, msg);
	}
	
	public static void fine(String msg) {
		log(Level.FINE, msg);
	}
	
	public static void trace(String msg) {
		log(Level.FINEST, msg);
	}
	
	public static void log(Level level, String msg) {
		LOG.log(level, msg);
	}
	
	public static void enable() {
		if (enabled) return;
		enabled = true;
		LOG.setLevel(lastLevel);
	}
	
	static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%4$s] %1$tT.%1$tL %5$s%n");
		LOG = Logger.getAnonymousLogger();
		LOG.setLevel(INITIAL_LOG_LEVEL);
		enable();
	}
	
}
