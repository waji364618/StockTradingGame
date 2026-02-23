package shared.configuration;

public class AppConfig {

    private static AppConfig instance;

    private final int startingBalance = 10000;
    private final double transactionFee = 0.02;
    private final int updateFrequencyInMs = 5000;
    private final double stockResetValue = 100.0;

    private AppConfig() {
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    public int getStartingBalance() {
        return startingBalance;
    }

    public double getTransactionFee() {
        return transactionFee;
    }

    public int getUpdateFrequencyInMs() {
        return updateFrequencyInMs;
    }

    public double getStockResetValue() {
        return stockResetValue;
    }

}
