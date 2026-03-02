package persistence.interfaces;

import domain.Stock;

import java.util.List;

public interface StockDao {

    void create(Stock stock);

    void update(Stock stock);

    Stock getBySymbol(String symbol);

    List<Stock> getAll();

    void delete(String symbol);
}
