package stockgame.persistence.interfaces;

import stockgame.domain.OwnedStock;

import java.util.List;

public interface OwnedStockDao {

    void create(OwnedStock ownedStock);

    void update(OwnedStock ownedStock);

    OwnedStock getById(int id);

    List<OwnedStock> getAll();
    OwnedStock getBySymbol(String symbol); //Ny til assignment6

    void delete(int id);

}
