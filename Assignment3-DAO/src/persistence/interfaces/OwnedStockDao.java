package persistence.interfaces;

import domain.OwnedStock;

import java.util.List;

public interface OwnedStockDao {

    void create(OwnedStock ownedStock);

    void update(OwnedStock ownedStock);

    OwnedStock getById(int id);

    List<OwnedStock> getAll();

    void delete(int id);

}
