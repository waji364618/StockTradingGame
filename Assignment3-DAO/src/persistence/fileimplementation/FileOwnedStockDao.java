package persistence.fileimplementation;

import domain.OwnedStock;
import persistence.interfaces.OwnedStockDao;

import java.util.List;

public class FileOwnedStockDao implements OwnedStockDao {

    private static int nextId = 1;

    private final FileUnitOfWork uow;

    public FileOwnedStockDao(FileUnitOfWork uow) {
        this.uow = uow;
        calculateNextId();
    }

    @Override
    public void create(OwnedStock ownedStock) {

        OwnedStock newOwnedStock =
                new OwnedStock(
                        nextId++,
                        ownedStock.getPortfolioId(),
                        ownedStock.getStockSymbol(),
                        ownedStock.getNumberOfShares()
                );

        uow.getOwnedStocks().add(newOwnedStock);
    }

    @Override
    public void update(OwnedStock ownedStock) {

        List<OwnedStock> ownedStocks = uow.getOwnedStocks();

        for (int i = 0; i < ownedStocks.size(); i++) {
            if (ownedStocks.get(i).getId() == ownedStock.getId()) {
                ownedStocks.set(i, ownedStock);
                return;
            }
        }
    }

    @Override
    public OwnedStock getById(int id) {

        for (OwnedStock os : uow.getOwnedStocks()) {
            if (os.getId() == id) {
                return os;
            }
        }
        return null;
    }

    @Override
    public List<OwnedStock> getAll() {
        return uow.getOwnedStocks();
    }

    @Override
    public void delete(int id) {
        uow.getOwnedStocks().removeIf(os -> os.getId() == id);
    }

    private void calculateNextId() {

        List<OwnedStock> ownedStocks = uow.getOwnedStocks();

        for (OwnedStock os : ownedStocks) {
            if (os.getId() >= nextId) {
                nextId = os.getId() + 1;
            }
        }
    }
}


