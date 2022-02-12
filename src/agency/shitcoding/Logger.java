package agency.shitcoding;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private final String LOGFILE_DATE = "YYYYMMdd_HH_mm_ss-";
    private final String TIMESTAMP_DATE = "HH:mm:ss";
    public static boolean DEBUGGING = true;
    private BufferedWriter writer;

    private static File logFile;

    public Logger() {
        File logDir = new File("logs");
        logDir.mkdir();
        SimpleDateFormat formatter = new SimpleDateFormat(LOGFILE_DATE);
        try {
            for (int i = 0; i < 256; i++) {
                logFile = new File(logDir.getAbsolutePath()
                        + File.pathSeparator
                        + formatter.format(new Date())
                        + i
                        + ".log");
                if (!logFile.exists()) {
                    logFile.createNewFile();
                    break;
                }
                if (i == 255) {
                    throw new IOException("Too many log files with such timestamp.");
                }
            }
        } catch (IOException e) {
            logFile = null;
            System.err.println("Could not create a log file.");
        }
    }

    public void debug(String toLog) {
        if (DEBUGGING)
        System.out.println(formatMessage("DEBUG", toLog));
    }

    public void info(String toLog) {
        String s = formatMessage("INFO", toLog);
        System.out.println(s);
        logToFile(s);
    }

    public void warn(String toLog) {
        String s = formatMessage("WARN", toLog);
        System.out.println(s);
        logToFile(s);
    }

    public void error(String toLog) {
        String s = formatMessage("ERROR", toLog);
        System.err.println(s);
        logToFile(s);
    }

    public void fatal(String toLog) {
        String s = formatMessage("FATAL", toLog);
        System.err.println(s);
        logToFile(s);
    }

    private void logToFile(String s) {
        if (logFile == null)
            return;
        try {
            if (writer == null) {
                writer = new BufferedWriter(new FileWriter(logFile));
            }
            writer.write(s);
            writer.newLine();
            writer.flush();
            } catch (IOException e) {
                logFile = null;
                writer = null;
                System.err.println("Failed to write to logfile. Logging to file is disabled.");
            }
    }

    private String formatMessage(String prefix, String toLog) {
        SimpleDateFormat formatter = new SimpleDateFormat(TIMESTAMP_DATE);
        return '[' + prefix + ']' +
                '(' +
                formatter.format(new Date()) +
                "): ";
    }
}
