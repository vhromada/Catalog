package cz.vhromada.catalog.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

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
    public static cz.vhromada.catalog.domain.Game newGameDomain(final Integer id) {
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
    @SuppressWarnings("Duplicates")
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
    public static Game newGame(final Integer id) {
        final Game game = new Game();
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
    @SuppressWarnings("Duplicates")
    public static void updateGame(final Game game) {
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
    @SuppressWarnings("SameParameterValue")
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
        assertThat(actual, is(notNullValue()));
        assertThat(actual.size(), is(expected.size()));
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
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getId(), is(expected.getId()));
        assertThat(actual.getName(), is(expected.getName()));
        assertThat(actual.getWikiEn(), is(expected.getWikiEn()));
        assertThat(actual.getWikiCz(), is(expected.getWikiCz()));
        assertThat(actual.getCrack(), is(expected.getCrack()));
        assertThat(actual.getSerialKey(), is(expected.getSerialKey()));
        assertThat(actual.getPatch(), is(expected.getPatch()));
        assertThat(actual.getTrainer(), is(expected.getTrainer()));
        assertThat(actual.getTrainerData(), is(expected.getTrainerData()));
        assertThat(actual.getEditor(), is(expected.getEditor()));
        assertThat(actual.getSaves(), is(expected.getSaves()));
        assertThat(actual.getOtherData(), is(expected.getOtherData()));
        assertThat(actual.getNote(), is(expected.getNote()));
        assertThat(actual.getPosition(), is(expected.getPosition()));
    }

    /**
     * Asserts games deep equals.
     *
     * @param expected expected list of game
     * @param actual   actual games
     */
    public static void assertGameListDeepEquals(final List<Game> expected, final List<cz.vhromada.catalog.domain.Game> actual) {
        assertThat(actual, is(notNullValue()));
        assertThat(actual.size(), is(expected.size()));
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
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getId(), is(expected.getId()));
        assertThat(actual.getName(), is(expected.getName()));
        assertThat(actual.getWikiEn(), is(expected.getWikiEn()));
        assertThat(actual.getWikiCz(), is(expected.getWikiCz()));
        assertThat(actual.getMediaCount(), is(expected.getMediaCount()));
        assertThat(actual.getCrack(), is(expected.getCrack()));
        assertThat(actual.getSerialKey(), is(expected.getSerialKey()));
        assertThat(actual.getPatch(), is(expected.getPatch()));
        assertThat(actual.getTrainer(), is(expected.getTrainer()));
        assertThat(actual.getTrainerData(), is(expected.getTrainerData()));
        assertThat(actual.getEditor(), is(expected.getEditor()));
        assertThat(actual.getSaves(), is(expected.getSaves()));
        assertThat(actual.getOtherData(), is(expected.getOtherData()));
        assertThat(actual.getNote(), is(expected.getNote()));
        assertThat(actual.getPosition(), is(expected.getPosition()));
    }

}
