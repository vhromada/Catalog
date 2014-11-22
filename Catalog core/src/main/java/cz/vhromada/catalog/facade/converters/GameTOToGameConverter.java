package cz.vhromada.catalog.facade.converters;

import cz.vhromada.catalog.dao.entities.Game;
import cz.vhromada.catalog.facade.to.GameTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A class represents converter from TO for game to entity game.
 *
 * @author Vladimir Hromada
 */
@Component("gameTOToGameConverter")
public class GameTOToGameConverter implements Converter<GameTO, Game> {

    @Override
    public Game convert(final GameTO source) {
        if (source == null) {
            return null;
        }

        final Game game = new Game();
        game.setId(source.getId());
        game.setName(source.getName());
        game.setWikiEn(source.getWikiEn());
        game.setWikiCz(source.getWikiCz());
        game.setMediaCount(source.getMediaCount());
        game.setCrack(source.hasCrack());
        game.setSerialKey(source.hasSerialKey());
        game.setPatch(source.hasPatch());
        game.setTrainer(source.hasTrainer());
        game.setTrainerData(source.hasTrainerData());
        game.setEditor(source.hasEditor());
        game.setSaves(source.haveSaves());
        game.setOtherData(source.getOtherData());
        game.setNote(source.getNote());
        game.setPosition(source.getPosition());
        return game;
    }

}
