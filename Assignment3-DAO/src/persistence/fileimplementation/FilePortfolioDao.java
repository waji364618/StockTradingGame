package persistence.fileimplementation;

import domain.Portfolio;
import persistence.interfaces.PortfolioDao;

import java.util.List;

public class FilePortfolioDao implements PortfolioDao {

    private static int nextId = 1;

    private final FileUnitOfWork uow;

    public FilePortfolioDao(FileUnitOfWork uow) {
        this.uow = uow;
        calculateNextId();
    }

    @Override
    public void create(Portfolio portfolio) {

        Portfolio newPortfolio =
                new Portfolio(nextId++, portfolio.getCurrentBalance());

        uow.getPortfolios().add(newPortfolio);
    }

    @Override
    public void update(Portfolio portfolio) {
        List<Portfolio> portfolios = uow.getPortfolios();

        for (int i = 0; i < portfolios.size(); i++) {
            if (portfolios.get(i).getId() == portfolio.getId()) {
                portfolios.set(i, portfolio);
                return;
            }
        }
    }

    @Override
    public Portfolio getById(int id) {
        for (Portfolio portfolio : uow.getPortfolios()) {
            if (portfolio.getId() == id) {
                return portfolio;
            }
        }
        return null;
    }

    @Override
    public List<Portfolio> getAll() {
        return uow.getPortfolios();
    }

    @Override
    public void delete(int id) {
        uow.getPortfolios().removeIf(p -> p.getId() == id);
    }

    private void calculateNextId() {
        List<Portfolio> portfolios = uow.getPortfolios();

        for (Portfolio portfolio : portfolios) {
            if (portfolio.getId() >= nextId) {
                nextId = portfolio.getId() + 1;
            }
        }
    }
}

