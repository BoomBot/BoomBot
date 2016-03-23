package net.lomeli.boombot.lib;

import com.google.common.collect.Lists;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.Date;
import java.util.List;

public class Logger {
    public static PrintStream out = System.out;
    private static List<String> logMsg = Lists.newArrayList();

    public static void log(LogInfo info, Object message, Object... args) {
        String msg = String.format("[%s][%s]: %s", new Date(), info.getName(), String.format(message.toString(), args));
        out.println(msg);
        logMsg.add(msg);
    }

    public static void info(Object message, Object... args) {
        log(LogInfo.INFO, message, args);
    }

    public static void debug(Object message, Object... args) {
        log(LogInfo.DEBUG, message, args);
    }

    public static void warn(Object message, Object...args) {
        log(LogInfo.WARN, message, args);
    }

    public static void warn(Object message, Exception e, Object... args) {
        warn(message, args);
        e.printStackTrace();
    }

    public static void error(Object message, Object...args) {
        log(LogInfo.ERROR, message, args);
    }

    public static void error(Object message, Exception e, Object... args) {
        error(message, args);
        e.printStackTrace();
        logMsg.add(e.getLocalizedMessage());
    }

    public static void writeLogFile(File folder, File file) {
        try {
            if (!folder.exists()) folder.mkdir();
            if (file == null || logMsg.isEmpty()) return;
            if (file.exists()) file.delete();
            FileWriter stream = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(stream);
            for (int i = 0; i < logMsg.size(); i++) {
                bufferedWriter.write(logMsg.get(i));
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
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
