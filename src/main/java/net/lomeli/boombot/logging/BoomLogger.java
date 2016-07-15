package net.lomeli.boombot.logging;

import net.lomeli.boombot.logging.Logger.LogInfo;

public class BoomLogger {
    private static Logger logger = new Logger("BoomBot");

    public static void log(LogInfo info, Object message, Object... args) {
        logger.log(info, message, args);
    }

    public static void info(Object message, Object... args) {
        logger.info(message, args);
    }

    public static void debug(Object message, Object... args) {
        logger.debug(message, args);
    }

    public static void warn(Object message, Object... args) {
        logger.warn(message, args);
    }

    public static void warn(Object message, Exception e, Object... args) {
        logger.warn(message, e, args);
    }

    public static void error(Object message, Object... args) {
        logger.error(message, args);
    }

    public static void error(Object message, Exception e, Object... args) {
        logger.error(message, e, args);
    }
}
