package stockgame.persistence.fileimplementation;

import stockgame.domain.Transaction;
import stockgame.persistence.interfaces.TransactionDao;

import java.util.List;

public class FileTransactionDao implements TransactionDao {

    private static int nextId = 1;
    private final FileUnitOfWork uow;

    public FileTransactionDao(FileUnitOfWork uow) {
        this.uow = uow;
        calculateNextId();
    }

    @Override
    public void create(Transaction transaction) {

        Transaction newTransaction = new Transaction(
                nextId++,
                transaction.getPortfolioId(),
                transaction.getStockSymbol(),
                transaction.getType(),
                transaction.getQuantity(),
                transaction.getPricePerShare(),
                transaction.getTotalAmount(),
                transaction.getFee(),
                transaction.getTimestamp()
        );

        uow.getTransactions().add(newTransaction);
    }

    @Override
    public List<Transaction> getAll() {
        return uow.getTransactions();
    }

    private void calculateNextId() {
        for (Transaction t : uow.getTransactions()) {
            if (t.getId() >= nextId) {
                nextId = t.getId() + 1;
            }
        }
    }
}