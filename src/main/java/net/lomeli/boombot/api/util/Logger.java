package net.lomeli.boombot.api.util;

import com.google.common.base.Strings;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.lomeli.boombot.api.BoomAPI;

public class Logger {
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
    private static final String BASE_FORMAT = "[%time%] [%level%] [%owner%]: %message%\n";
    private static final File LOG_FOLDER = new File("logs");
    private static final File LATEST_LOG = new File(LOG_FOLDER, "latest.log");
    private static PrintStream outStream;

    private String owner;

    public Logger(String owner) {
        this.owner = owner;
    }

    private String properFormat(Level level, String finalMsg) {
        String out = BASE_FORMAT;
        return out.replace("%time%", TIME_FORMAT.format(new Date())).replace("%level%", level.getTag())
                .replace("%owner%", owner).replace("%message%", finalMsg);
    }

    public void log(Level level, String msg, Object... args) {
        String out = (args != null && args.length > 0) ? String.format(msg, args) : msg;
        out = properFormat(level, out);
        toOutStream(out);
        writeToLog(out);
    }

    public void info(String msg, Object... args) {
        log(Level.INFO, msg, args);
    }

    public void warn(String msg, Object... args) {
        log(Level.WARNING, msg, args);
    }

    public void debug(String msg, Object... args) {
        if (BoomAPI.debugMode)
            log(Level.DEBUG, msg, args);
    }

    public void error(String msg, Object... args) {
        log(Level.FATAL, msg, args);
    }

    public void error(String msg, Exception ex, Object... args) {
        StackTraceElement[] elements = ex.getStackTrace();
        log(Level.FATAL, msg, args);
        if (!Strings.isNullOrEmpty(ex.getLocalizedMessage()))
            error(ex.getLocalizedMessage());
        for (StackTraceElement ele : elements) {
            error("\tat " + ele);
        }
    }

    public void toOutStream(String msg) {
        if (outStream == null) outStream = System.out;
        outStream.print(msg);
    }

    private static void writeToLog(String msg) {
        if (!LOG_FOLDER.exists()) LOG_FOLDER.mkdir();
        try {
            FileUtils.write(LATEST_LOG, msg, "UTF-8", true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void setupLogFolder() {
        if (!LOG_FOLDER.exists()) LOG_FOLDER.mkdir();
        if (LATEST_LOG.exists()) {
            try {
                Path logPath = Paths.get(LATEST_LOG.getCanonicalPath());
                BasicFileAttributes attrib = Files.readAttributes(logPath, BasicFileAttributes.class);
                FileTime time = attrib.creationTime();
                String name = String.format("%s.gz", time.toInstant().toString().replace(":", "-"));
                IOUtil.gzipFile(LATEST_LOG, new File(LOG_FOLDER, name));
                LATEST_LOG.delete();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public enum Level {
        ALL("Finest", 0, false),
        TRACE("Trace", 1, false),
        DEBUG("Debug", 2, false),
        INFO("Info", 3, false),
        WARNING("Warning", 4, true),
        FATAL("Fatal", 5, true),
        OFF("NO-LOGGING", 6, true);

        private String msg;
        private int pri;
        private boolean isError;

        Level(String message, int priority, boolean isError) {
            this.msg = message;
            this.pri = priority;
            this.isError = isError;
        }

        /**
         * Returns the Log-Tag (e.g. Fatal)
         *
         * @return the logTag
         */
        public String getTag() {
            return msg;
        }

        /**
         * Returns the numeric priority of this loglevel, with 0 being the lowest
         *
         * @return the level-priority
         */
        public int getPriority() {
            return pri;
        }

        /**
         * Returns whether this LOG-level should be treated like an error or not
         *
         * @return boolean true, if this LOG-level is an error-level
         */
        public boolean isError() {
            return isError;
        }
    }
}
