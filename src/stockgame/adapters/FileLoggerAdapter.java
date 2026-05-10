package stockgame.adapters;

import stockgame.library.FileLogOutputter;

public class FileLoggerAdapter {

    private final FileLogOutputter outputter;

    public FileLoggerAdapter() {

        outputter = new FileLogOutputter("logs/app.log", "INFO");
    }

    public void log(String level, String message) {

        switch (level.toUpperCase()) {

            case "INFO":
                outputter.logInfo(message);
                break;

            case "WARNING":
                outputter.logWarning(message);
                break;

            case "ERROR":
                outputter.logError(message);
                break;
        }
    }
}
