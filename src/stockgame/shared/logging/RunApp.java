package stockgame.shared.logging;

public class RunApp {
    public static void main(String[] args) {

        // get singleton instance
        Logger logger = Logger.getInstance();

        // set console output
        logger.setOutput(new ConsoleLogOutput());

        // log messages (String levels)
        logger.log("INFO", "Application started");
        logger.log("WARNING", "Stock not found");
        logger.log("ERROR", "Failed to save data");
    }
}
