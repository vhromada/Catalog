package cz.vhromada.catalog.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.entity.Game;

/**
 * A class represents utility class for games.
 *
 * @author Vladimir Hromada
 */
public final class GameUtils {

    /**
     * Count of games
     */
    public static final int GAMES_COUNT = 3;

    /**
     * ID
     */
    public static final Integer ID = 1;

    /**
     * Position
     */
    public static final Integer POSITION = 10;

    /**
     * Game name
     */
    private static final String GAME = "Game ";

    /**
     * Creates a new instance of GameUtils.
     */
    private GameUtils() {
    }

    /**
     * Returns game.
     *
     * @param id ID
     * @return game
     */
    public static cz.vhromada.catalog.domain.Game newGame(final Integer id) {
        final cz.vhromada.catalog.domain.Game game = new cz.vhromada.catalog.domain.Game();
        updateGame(game);
        if (id != null) {
            game.setId(id);
            game.setPosition(id - 1);
        }

        return game;
    }

    /**
     * Updates game fields.
     *
     * @param game game
     */
    public static void updateGame(final cz.vhromada.catalog.domain.Game game) {
        game.setName("Name");
        game.setWikiEn("enWiki");
        game.setWikiCz("czWiki");
        game.setMediaCount(1);
        game.setCrack(true);
        game.setSerialKey(true);
        game.setPatch(true);
        game.setTrainer(true);
        game.setTrainerData(true);
        game.setEditor(true);
        game.setSaves(true);
        game.setOtherData("Other data");
        game.setNote("Note");
    }

    /**
     * Returns game.
     *
     * @param id ID
     * @return game
     */
    public static Game newGameTO(final Integer id) {
        final Game game = new Game();
        updateGameTO(game);
        if (id != null) {
            game.setId(id);
            game.setPosition(id - 1);
        }

        return game;
    }

    /**
     * Updates game fields.
     *
     * @param game game
     */
    public static void updateGameTO(final Game game) {
        game.setName("Name");
        game.setWikiEn("enWiki");
        game.setWikiCz("czWiki");
        game.setMediaCount(1);
        game.setCrack(true);
        game.setSerialKey(true);
        game.setPatch(true);
        game.setTrainer(true);
        game.setTrainerData(true);
        game.setEditor(true);
        game.setSaves(true);
        game.setOtherData("Other data");
        game.setNote("Note");
    }

    /**
     * Returns games.
     *
     * @return games
     */
    public static List<cz.vhromada.catalog.domain.Game> getGames() {
        final List<cz.vhromada.catalog.domain.Game> games = new ArrayList<>();
        for (int i = 0; i < GAMES_COUNT; i++) {
            games.add(getGame(i + 1));
        }

        return games;
    }

    /**
     * Returns game for index.
     *
     * @param index index
     * @return game for index
     */
    public static cz.vhromada.catalog.domain.Game getGame(final int index) {
        final cz.vhromada.catalog.domain.Game game = new cz.vhromada.catalog.domain.Game();
        game.setId(index);
        game.setName(GAME + index + " name");
        game.setWikiEn(GAME + index + " English Wikipedia");
        game.setWikiCz(GAME + index + " Czech Wikipedia");
        game.setMediaCount(index);
        game.setCrack(index != 1);
        game.setSerialKey(index != 1);
        game.setPatch(index != 1);
        game.setTrainer(index != 1);
        game.setTrainerData(index == 3);
        game.setEditor(index == 3);
        game.setSaves(index == 3);
        game.setOtherData(index == 3 ? GAME + "3 other data" : "");
        game.setNote(index == 3 ? GAME + "3 note" : "");
        game.setPosition(index - 1);

        return game;
    }

    /**
     * Returns game.
     *
     * @param entityManager entity manager
     * @param id            game ID
     * @return game
     */
    public static cz.vhromada.catalog.domain.Game getGame(final EntityManager entityManager, final int id) {
        return entityManager.find(cz.vhromada.catalog.domain.Game.class, id);
    }

    /**
     * Returns game with updated fields.
     *
     * @param entityManager entity manager
     * @param id            game ID
     * @return game with updated fields
     */
    public static cz.vhromada.catalog.domain.Game updateGame(final EntityManager entityManager, final int id) {
        final cz.vhromada.catalog.domain.Game game = getGame(entityManager, id);
        updateGame(game);
        game.setPosition(POSITION);

        return game;
    }

    /**
     * Returns count of games.
     *
     * @param entityManager entity manager
     * @return count of games
     */
    public static int getGamesCount(final EntityManager entityManager) {
        return entityManager.createQuery("SELECT COUNT(g.id) FROM Game g", Long.class).getSingleResult().intValue();
    }

    /**
     * Asserts games deep equals.
     *
     * @param expected expected games
     * @param actual   actual games
     */
    public static void assertGamesDeepEquals(final List<cz.vhromada.catalog.domain.Game> expected, final List<cz.vhromada.catalog.domain.Game> actual) {
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        if (!expected.isEmpty()) {
            for (int i = 0; i < expected.size(); i++) {
                assertGameDeepEquals(expected.get(i), actual.get(i));
            }
        }
    }

    /**
     * Asserts game deep equals.
     *
     * @param expected expected game
     * @param actual   actual game
     */
    public static void assertGameDeepEquals(final cz.vhromada.catalog.domain.Game expected, final cz.vhromada.catalog.domain.Game actual) {
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getWikiEn(), actual.getWikiEn());
        assertEquals(expected.getWikiCz(), actual.getWikiCz());
        assertEquals(expected.getCrack(), actual.getCrack());
        assertEquals(expected.getSerialKey(), actual.getSerialKey());
        assertEquals(expected.getPatch(), actual.getPatch());
        assertEquals(expected.getTrainer(), actual.getTrainer());
        assertEquals(expected.getTrainerData(), actual.getTrainerData());
        assertEquals(expected.getEditor(), actual.getEditor());
        assertEquals(expected.getSaves(), actual.getSaves());
        assertEquals(expected.getOtherData(), actual.getOtherData());
        assertEquals(expected.getNote(), actual.getNote());
        assertEquals(expected.getPosition(), actual.getPosition());
    }

    /**
     * Asserts games deep equals.
     *
     * @param expected expected list of game
     * @param actual   actual games
     */
    public static void assertGameListDeepEquals(final List<Game> expected, final List<cz.vhromada.catalog.domain.Game> actual) {
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        if (!expected.isEmpty()) {
            for (int i = 0; i < expected.size(); i++) {
                assertGameDeepEquals(expected.get(i), actual.get(i));
            }
        }
    }

    /**
     * Asserts game deep equals.
     *
     * @param expected expected game
     * @param actual   actual game
     */
    public static void assertGameDeepEquals(final Game expected, final cz.vhromada.catalog.domain.Game actual) {
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getWikiEn(), actual.getWikiEn());
        assertEquals(expected.getWikiCz(), actual.getWikiCz());
        assertEquals(expected.getMediaCount(), actual.getMediaCount());
        assertEquals(expected.getCrack(), actual.getCrack());
        assertEquals(expected.getSerialKey(), actual.getSerialKey());
        assertEquals(expected.getPatch(), actual.getPatch());
        assertEquals(expected.getTrainer(), actual.getTrainer());
        assertEquals(expected.getTrainerData(), actual.getTrainerData());
        assertEquals(expected.getEditor(), actual.getEditor());
        assertEquals(expected.getSaves(), actual.getSaves());
        assertEquals(expected.getOtherData(), actual.getOtherData());
        assertEquals(expected.getNote(), actual.getNote());
        assertEquals(expected.getPosition(), actual.getPosition());
    }

}
