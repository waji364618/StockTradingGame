package persistence;

import domain.Portfolio;
import persistence.fileimplementation.FilePortfolioDao;
import persistence.fileimplementation.FileUnitOfWork;

public class TestPersistence {

    public static void main(String[] args) {

        //  Opret UnitOfWork (mappen "data" bliver lavet automatisk)
        FileUnitOfWork uow = new FileUnitOfWork("data");

        //  Start transaction
        uow.beginTransaction();

        //  Opret DAO
        FilePortfolioDao portfolioDao = new FilePortfolioDao(uow);

        //  Opret ny portfolio
        Portfolio p = new Portfolio(0, 10000.0);

        portfolioDao.create(p);

        //  Commit (skriv til fil)
        uow.commit();

        System.out.println("Portfolio saved to file!");



        // TEST: Læs data igen
        uow.beginTransaction();

        for (Portfolio portfolio : portfolioDao.getAll()) {
            System.out.println("Portfolio ID: " +
                    portfolio.getId() +
                    " Balance: " +
                    portfolio.getCurrentBalance());
        }

        uow.rollback();
    }

}
