package stockgame.business.stockmarket.services;

import stockgame.business.events.StockUpdateEvent;
import stockgame.domain.OwnedStock;
import stockgame.persistence.fileimplementation.FileOwnedStockDao;
import stockgame.persistence.fileimplementation.FileUnitOfWork;
import stockgame.shared.logging.Logger;


public class StockBankruptService {

    private final FileUnitOfWork uow;
    private final FileOwnedStockDao ownedStockDao;
    private final Logger logger = Logger.getInstance();


    public StockBankruptService(FileUnitOfWork uow) {

        this.uow = uow;
        this.ownedStockDao = new FileOwnedStockDao(uow);
    }

    public void onStockUpdate(StockUpdateEvent event) {

        if (!event.getState().equals("Bankrupt")) {
            return;
        }

        uow.beginTransaction();

        for (OwnedStock os : ownedStockDao.getAll()) {

            if (os.getStockSymbol()
                    .equals(event.getSymbol())) {

                os.setNumberOfShares(0);

                ownedStockDao.update(os);

                logger.log(
                        "INFO",
                        "Stock bankrupt: " +
                                event.getSymbol()
                );
            }
        }

        uow.commit();

    }
}
