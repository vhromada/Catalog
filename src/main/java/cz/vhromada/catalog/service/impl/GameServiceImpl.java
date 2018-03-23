package cz.vhromada.catalog.service.impl;

import cz.vhromada.catalog.domain.Game;
import cz.vhromada.catalog.repository.GameRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of service for games.
 *
 * @author Vladimir Hromada
 */
@Component("gameService")
public class GameServiceImpl extends AbstractCatalogService<Game> {

    /**
     * Creates a new instance of GameServiceImpl.
     *
     * @param gameRepository repository for games
     * @param cache          cache
     * @throws IllegalArgumentException if repository games is null
     *                                  or cache is null
     */
    @Autowired
    public GameServiceImpl(final GameRepository gameRepository, @Value("#{cacheManager.getCache('catalogCache')}") final Cache cache) {
        super(gameRepository, cache, "games");
    }

    @Override
    protected Game getCopy(final Game data) {
        final Game newGame = new Game();
        newGame.setName(data.getName());
        newGame.setWikiEn(data.getWikiEn());
        newGame.setWikiCz(data.getWikiCz());
        newGame.setMediaCount(data.getMediaCount());
        newGame.setCrack(data.getCrack());
        newGame.setSerialKey(data.getSerialKey());
        newGame.setPatch(data.getPatch());
        newGame.setTrainer(data.getTrainer());
        newGame.setTrainerData(data.getTrainerData());
        newGame.setEditor(data.getEditor());
        newGame.setSaves(data.getSaves());
        newGame.setOtherData(data.getOtherData());
        newGame.setNote(data.getNote());
        newGame.setPosition(data.getPosition());

        return newGame;
    }

}
