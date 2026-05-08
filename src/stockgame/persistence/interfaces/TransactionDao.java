package stockgame.persistence.interfaces;

import stockgame.domain.Transaction;

import java.util.List;

public interface TransactionDao {

    void create(Transaction transaction);

    List<Transaction> getAll();
}
