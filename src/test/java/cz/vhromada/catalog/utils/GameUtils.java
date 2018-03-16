package cz.vhromada.catalog.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

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
    public static final int ID = 1;

    /**
     * Position
     */
    public static final int POSITION = 10;

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
        assertSoftly(softly -> {
            softly.assertThat(expected).isNotNull();
            softly.assertThat(actual).isNotNull();
        });
        assertThat(expected.size()).isEqualTo(actual.size());
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
    @SuppressWarnings("Duplicates")
    public static void assertGameDeepEquals(final cz.vhromada.catalog.domain.Game expected, final cz.vhromada.catalog.domain.Game actual) {
        assertSoftly(softly -> {
            softly.assertThat(expected).isNotNull();
            softly.assertThat(actual).isNotNull();
        });
        assertSoftly(softly -> {
            softly.assertThat(expected.getId()).isEqualTo(actual.getId());
            softly.assertThat(expected.getName()).isEqualTo(actual.getName());
            softly.assertThat(expected.getWikiEn()).isEqualTo(actual.getWikiEn());
            softly.assertThat(expected.getWikiCz()).isEqualTo(actual.getWikiCz());
            softly.assertThat(expected.getMediaCount()).isEqualTo(actual.getMediaCount());
            softly.assertThat(expected.getCrack()).isEqualTo(actual.getCrack());
            softly.assertThat(expected.getSerialKey()).isEqualTo(actual.getSerialKey());
            softly.assertThat(expected.getPatch()).isEqualTo(actual.getPatch());
            softly.assertThat(expected.getTrainer()).isEqualTo(actual.getTrainer());
            softly.assertThat(expected.getTrainerData()).isEqualTo(actual.getTrainerData());
            softly.assertThat(expected.getEditor()).isEqualTo(actual.getEditor());
            softly.assertThat(expected.getSaves()).isEqualTo(actual.getSaves());
            softly.assertThat(expected.getOtherData()).isEqualTo(actual.getOtherData());
            softly.assertThat(expected.getNote()).isEqualTo(actual.getNote());
            softly.assertThat(expected.getPosition()).isEqualTo(actual.getPosition());
        });
    }

    /**
     * Asserts games deep equals.
     *
     * @param expected expected list of game
     * @param actual   actual games
     */
    public static void assertGameListDeepEquals(final List<Game> expected, final List<cz.vhromada.catalog.domain.Game> actual) {
        assertSoftly(softly -> {
            softly.assertThat(expected).isNotNull();
            softly.assertThat(actual).isNotNull();
        });
        assertThat(expected.size()).isEqualTo(actual.size());
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
    @SuppressWarnings("Duplicates")
    public static void assertGameDeepEquals(final Game expected, final cz.vhromada.catalog.domain.Game actual) {
        assertSoftly(softly -> {
            softly.assertThat(expected).isNotNull();
            softly.assertThat(actual).isNotNull();
        });
        assertSoftly(softly -> {
            softly.assertThat(actual.getId()).isEqualTo(expected.getId());
            softly.assertThat(actual.getName()).isEqualTo(expected.getName());
            softly.assertThat(actual.getWikiEn()).isEqualTo(expected.getWikiEn());
            softly.assertThat(actual.getWikiCz()).isEqualTo(expected.getWikiCz());
            softly.assertThat(actual.getMediaCount()).isEqualTo(expected.getMediaCount());
            softly.assertThat(actual.getCrack()).isEqualTo(expected.getCrack());
            softly.assertThat(actual.getSerialKey()).isEqualTo(expected.getSerialKey());
            softly.assertThat(actual.getPatch()).isEqualTo(expected.getPatch());
            softly.assertThat(actual.getTrainer()).isEqualTo(expected.getTrainer());
            softly.assertThat(actual.getTrainerData()).isEqualTo(expected.getTrainerData());
            softly.assertThat(actual.getEditor()).isEqualTo(expected.getEditor());
            softly.assertThat(actual.getSaves()).isEqualTo(expected.getSaves());
            softly.assertThat(actual.getOtherData()).isEqualTo(expected.getOtherData());
            softly.assertThat(actual.getNote()).isEqualTo(expected.getNote());
            softly.assertThat(actual.getPosition()).isEqualTo(expected.getPosition());
        });
    }

}
