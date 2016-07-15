package net.lomeli.boombot.logging;

import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.PrintStream;
import java.util.Date;
import java.util.List;

import net.lomeli.boombot.BoomBot;

public class Logger {
    private static List<String> logMsg = Lists.newArrayList();
    private PrintStream out = System.out;
    private String owner;

    public Logger(String owner) {
        this.owner = owner;
    }

    public void log(LogInfo info, Object message, Object... args) {
        String msg = String.format("[%s][%s][%s]: %s", new Date(), info.getName(), owner, String.format(message.toString(), args));
        out.println(msg);
        logMsg.add(msg);
    }

    public void info(Object message, Object... args) {
        log(LogInfo.INFO, message, args);
    }

    public void debug(Object message, Object... args) {
        if (BoomBot.debug)
            log(LogInfo.DEBUG, message, args);
    }

    public void warn(Object message, Object... args) {
        log(LogInfo.WARN, message, args);
    }

    public void warn(Object message, Exception e, Object... args) {
        warn(message, args);
        e.printStackTrace();
    }

    public void error(Object message, Object... args) {
        log(LogInfo.ERROR, message, args);
    }

    public void error(Object message, Exception e, Object... args) {
        error(message, args);
        e.printStackTrace();
        logMsg.add(e.getLocalizedMessage());
    }

    public static void writeLogFile(File folder, File file) {
        try {
            if (!folder.exists()) folder.mkdir();
            if (file == null || logMsg.isEmpty()) return;
            if (file.exists()) file.delete();
            FileUtils.writeLines(file, logMsg, true);
            logMsg.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
