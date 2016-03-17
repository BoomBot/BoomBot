package net.lomeli.boombot.lib;

import java.io.PrintStream;
import java.util.Date;

public class Logger {
    public static PrintStream out = System.out;

    public static void log(LogInfo info, Object message, Object... args) {
        out.printf("[%s][%s]: %s\n", new Date(), info.getName(), String.format(message.toString(), args));
    }

    public static void info(Object message, Object...args) {
        log(LogInfo.INFO, message, args);
    }

    public static void debug(Object message, Object...args) {
        log(LogInfo.DEBUG, message, args);
    }

    public static void warn(Object message, Exception e, Object...args) {
        log(LogInfo.WARN, message, args);
        e.printStackTrace();
    }

    public static void error(Object message, Exception e, Object...args) {
        log(LogInfo.ERROR, message, args);
        e.printStackTrace();
    }

    public enum LogInfo {
        INFO("INFO"), DEBUG("DEBUG"), WARN("WARNING"), ERROR("ERROR");
        private final String name;

        LogInfo(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
