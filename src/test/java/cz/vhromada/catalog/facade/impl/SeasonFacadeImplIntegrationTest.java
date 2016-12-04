package cz.vhromada.catalog.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.CatalogConfiguration;
import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.common.Language;
import cz.vhromada.catalog.domain.Episode;
import cz.vhromada.catalog.entity.Season;
import cz.vhromada.catalog.entity.Show;
import cz.vhromada.catalog.facade.SeasonFacade;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.EpisodeUtils;
import cz.vhromada.catalog.utils.SeasonUtils;
import cz.vhromada.catalog.utils.ShowUtils;
import cz.vhromada.catalog.utils.TestConstants;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents integration test for class {@link SeasonFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CatalogConfiguration.class, CatalogTestConfiguration.class })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class SeasonFacadeImplIntegrationTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private EntityManager entityManager;

    /**
     * Instance of (@link SeasonFacade}
     */
    @Autowired
    private SeasonFacade seasonFacade;

    /**
     * Test method for {@link SeasonFacade#getSeason(Integer)}.
     */
    @Test
    public void testGetSeason() {
        for (int i = 1; i <= SeasonUtils.SEASONS_COUNT; i++) {
            SeasonUtils.assertSeasonDeepEquals(seasonFacade.getSeason(i), SeasonUtils.getSeason(i));
        }

        assertNull(seasonFacade.getSeason(Integer.MAX_VALUE));

        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link SeasonFacade#getSeason(Integer)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetSeason_NullArgument() {
        seasonFacade.getSeason(null);
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)}.
     */
    @Test
    @DirtiesContext
    public void testAdd() {
        final cz.vhromada.catalog.domain.Season expectedSeason = SeasonUtils.newSeasonDomain(SeasonUtils.SEASONS_COUNT + 1);
        expectedSeason.setPosition(Integer.MAX_VALUE);

        seasonFacade.add(ShowUtils.newShow(1), SeasonUtils.newSeason(null));

        final cz.vhromada.catalog.domain.Season addedSeason = SeasonUtils.getSeason(entityManager, SeasonUtils.SEASONS_COUNT + 1);
        SeasonUtils.assertSeasonDeepEquals(expectedSeason, addedSeason);

        assertEquals(SeasonUtils.SEASONS_COUNT + 1, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with null TO for show.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullShowTO() {
        seasonFacade.add(null, SeasonUtils.newSeason(null));
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with null TO for season.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullSeasonTO() {
        seasonFacade.add(ShowUtils.newShow(1), null);
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with show with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullId() {
        seasonFacade.add(ShowUtils.newShow(null), SeasonUtils.newSeason(null));
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with season with not null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NotNullId() {
        seasonFacade.add(ShowUtils.newShow(1), SeasonUtils.newSeason(1));
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with season with not positive number of season.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NotPositiveNumber() {
        final Season season = SeasonUtils.newSeason(null);
        season.setNumber(0);

        seasonFacade.add(ShowUtils.newShow(1), season);
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with season with bad minimum starting year.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_BadMinimumStartYear() {
        final Season season = SeasonUtils.newSeason(null);
        season.setStartYear(TestConstants.BAD_MIN_YEAR);

        seasonFacade.add(ShowUtils.newShow(1), season);
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with season with bad maximum starting year.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_BadMaximumStartYear() {
        final Season season = SeasonUtils.newSeason(null);
        season.setStartYear(TestConstants.BAD_MAX_YEAR);

        seasonFacade.add(ShowUtils.newShow(1), season);
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with season with bad minimum ending year.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_BadMinimumEndYear() {
        final Season season = SeasonUtils.newSeason(null);
        season.setEndYear(TestConstants.BAD_MIN_YEAR);

        seasonFacade.add(ShowUtils.newShow(1), season);
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with season with bad maximum ending year.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_BadMaximumEndYear() {
        final Season season = SeasonUtils.newSeason(null);
        season.setEndYear(TestConstants.BAD_MAX_YEAR);

        seasonFacade.add(ShowUtils.newShow(1), season);
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with season with starting year greater than ending year.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_BadYear() {
        final Season season = SeasonUtils.newSeason(null);
        season.setStartYear(season.getEndYear() + 1);

        seasonFacade.add(ShowUtils.newShow(1), season);
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with season with null language.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullLanguage() {
        final Season season = SeasonUtils.newSeason(null);
        season.setLanguage(null);

        seasonFacade.add(ShowUtils.newShow(1), season);
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with season with null subtitles.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullSubtitles() {
        final Season season = SeasonUtils.newSeason(null);
        season.setSubtitles(null);

        seasonFacade.add(ShowUtils.newShow(1), season);
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with season with subtitles with null value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_BadSubtitles() {
        final Season season = SeasonUtils.newSeason(null);
        season.setSubtitles(CollectionUtils.newList(Language.CZ, null));

        seasonFacade.add(ShowUtils.newShow(1), season);
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with season with null note.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullNote() {
        final Season season = SeasonUtils.newSeason(null);
        season.setNote(null);

        seasonFacade.add(ShowUtils.newShow(1), season);
    }

    /**
     * Test method for {@link SeasonFacade#add(Show, Season)} with season with not existing show.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NotExistingShow() {
        seasonFacade.add(ShowUtils.newShow(Integer.MAX_VALUE), SeasonUtils.newSeason(null));
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)}.
     */
    @Test
    @DirtiesContext
    public void testUpdate() {
        final Season season = SeasonUtils.newSeason(1);

        seasonFacade.update(season);

        final cz.vhromada.catalog.domain.Season updatedSeason = SeasonUtils.getSeason(entityManager, 1);
        SeasonUtils.assertSeasonDeepEquals(season, updatedSeason);

        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        seasonFacade.update(null);
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with season with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullId() {
        seasonFacade.update(SeasonUtils.newSeason(null));
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with season with not positive number of season.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NotPositiveNumber() {
        final Season season = SeasonUtils.newSeason(1);
        season.setNumber(0);

        seasonFacade.update(season);
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with season with bad minimum starting year.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_BadMinimumStartYear() {
        final Season season = SeasonUtils.newSeason(1);
        season.setStartYear(TestConstants.BAD_MIN_YEAR);

        seasonFacade.update(season);
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with season with bad maximum starting year.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_BadMaximumStartYear() {
        final Season season = SeasonUtils.newSeason(1);
        season.setStartYear(TestConstants.BAD_MAX_YEAR);

        seasonFacade.update(season);
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with season with bad minimum ending year.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_BadMinimumEndYear() {
        final Season season = SeasonUtils.newSeason(1);
        season.setEndYear(TestConstants.BAD_MIN_YEAR);

        seasonFacade.update(season);
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with season with bad maximum ending year.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_BadMaximumEndYear() {
        final Season season = SeasonUtils.newSeason(1);
        season.setEndYear(TestConstants.BAD_MAX_YEAR);

        seasonFacade.update(season);
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with season with starting year greater than ending year.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_BadYear() {
        final Season season = SeasonUtils.newSeason(1);
        season.setStartYear(season.getEndYear() + 1);

        seasonFacade.update(season);
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with season with null language.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullLanguage() {
        final Season season = SeasonUtils.newSeason(1);
        season.setLanguage(null);

        seasonFacade.update(season);
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with season with null subtitles.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullSubtitles() {
        final Season season = SeasonUtils.newSeason(1);
        season.setSubtitles(null);

        seasonFacade.update(season);
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with season with subtitles with null value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_BadSubtitles() {
        final Season season = SeasonUtils.newSeason(1);
        season.setSubtitles(CollectionUtils.newList(Language.CZ, null));

        seasonFacade.update(season);
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with season with null note.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullNote() {
        final Season season = SeasonUtils.newSeason(1);
        season.setNote(null);

        seasonFacade.update(season);
    }

    /**
     * Test method for {@link SeasonFacade#update(Season)} with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_BadId() {
        seasonFacade.update(SeasonUtils.newSeason(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#remove(Season)}.
     */
    @Test
    @DirtiesContext
    public void testRemove() {
        seasonFacade.remove(SeasonUtils.newSeason(1));

        assertNull(SeasonUtils.getSeason(entityManager, 1));

        assertEquals(SeasonUtils.SEASONS_COUNT - 1, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT - EpisodeUtils.EPISODES_PER_SEASON_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link SeasonFacade#remove(Season)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        seasonFacade.remove(null);
    }

    /**
     * Test method for {@link SeasonFacade#remove(Season)} with season with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullId() {
        seasonFacade.remove(SeasonUtils.newSeason(null));
    }

    /**
     * Test method for {@link SeasonFacade#remove(Season)} with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_BadId() {
        seasonFacade.remove(SeasonUtils.newSeason(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#duplicate(Season)}.
     */
    @Test
    @DirtiesContext
    public void testDuplicate() {
        final cz.vhromada.catalog.domain.Season season = SeasonUtils.getSeason(1);
        season.setId(SeasonUtils.SEASONS_COUNT + 1);
        for (final Episode episode : season.getEpisodes()) {
            episode.setId(EpisodeUtils.EPISODES_COUNT + season.getEpisodes().indexOf(episode) + 1);
        }

        seasonFacade.duplicate(SeasonUtils.newSeason(1));

        final cz.vhromada.catalog.domain.Season duplicatedSeason = SeasonUtils.getSeason(entityManager, SeasonUtils.SEASONS_COUNT + 1);
        SeasonUtils.assertSeasonDeepEquals(season, duplicatedSeason);

        assertEquals(SeasonUtils.SEASONS_COUNT + 1, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT + EpisodeUtils.EPISODES_PER_SEASON_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link SeasonFacade#duplicate(Season)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        seasonFacade.duplicate(null);
    }

    /**
     * Test method for {@link SeasonFacade#duplicate(Season)} with season with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullId() {
        seasonFacade.duplicate(SeasonUtils.newSeason(null));
    }

    /**
     * Test method for {@link SeasonFacade#duplicate(Season)} with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_BadId() {
        seasonFacade.duplicate(SeasonUtils.newSeason(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#moveUp(Season)}.
     */
    @Test
    @DirtiesContext
    public void testMoveUp() {
        final cz.vhromada.catalog.domain.Season season1 = SeasonUtils.getSeason(1, 1);
        season1.setPosition(1);
        final cz.vhromada.catalog.domain.Season season2 = SeasonUtils.getSeason(1, 2);
        season2.setPosition(0);

        seasonFacade.moveUp(SeasonUtils.newSeason(2));

        SeasonUtils.assertSeasonDeepEquals(season1, SeasonUtils.getSeason(entityManager, 1));
        SeasonUtils.assertSeasonDeepEquals(season2, SeasonUtils.getSeason(entityManager, 2));
        for (int i = 3; i <= SeasonUtils.SEASONS_COUNT; i++) {
            SeasonUtils.assertSeasonDeepEquals(SeasonUtils.getSeason(i), SeasonUtils.getSeason(entityManager, i));
        }

        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link SeasonFacade#moveUp(Season)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullArgument() {
        seasonFacade.moveUp(null);
    }

    /**
     * Test method for {@link SeasonFacade#moveUp(Season)} with season with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullId() {
        seasonFacade.moveUp(SeasonUtils.newSeason(null));
    }

    /**
     * Test method for {@link SeasonFacade#moveUp(Season)} with not movable argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NotMovableArgument() {
        seasonFacade.moveUp(SeasonUtils.newSeason(1));
    }

    /**
     * Test method for {@link SeasonFacade#moveUp(Season)} with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_BadId() {
        seasonFacade.moveUp(SeasonUtils.newSeason(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#moveDown(Season)}.
     */
    @Test
    @DirtiesContext
    public void testMoveDown() {
        final cz.vhromada.catalog.domain.Season season1 = SeasonUtils.getSeason(1, 1);
        season1.setPosition(1);
        final cz.vhromada.catalog.domain.Season season2 = SeasonUtils.getSeason(1, 2);
        season2.setPosition(0);

        seasonFacade.moveDown(SeasonUtils.newSeason(1));

        SeasonUtils.assertSeasonDeepEquals(season1, SeasonUtils.getSeason(entityManager, 1));
        SeasonUtils.assertSeasonDeepEquals(season2, SeasonUtils.getSeason(entityManager, 2));
        for (int i = 3; i <= SeasonUtils.SEASONS_COUNT; i++) {
            SeasonUtils.assertSeasonDeepEquals(SeasonUtils.getSeason(i), SeasonUtils.getSeason(entityManager, i));
        }

        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link SeasonFacade#moveDown(Season)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullArgument() {
        seasonFacade.moveDown(null);
    }

    /**
     * Test method for {@link SeasonFacade#moveDown(Season)} with season with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullId() {
        seasonFacade.moveDown(SeasonUtils.newSeason(null));
    }

    /**
     * Test method for {@link SeasonFacade#moveDown(Season)} with not movable argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NotMovableArgument() {
        seasonFacade.moveDown(SeasonUtils.newSeason(SeasonUtils.SEASONS_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#moveDown(Season)} with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_BadId() {
        seasonFacade.moveDown(SeasonUtils.newSeason(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#findSeasonsByShow(Show)}.
     */
    @Test
    public void testFindSeasonsByShow() {
        for (int i = 1; i <= ShowUtils.SHOWS_COUNT; i++) {
            final List<cz.vhromada.catalog.domain.Season> expectedSeasons = SeasonUtils.getSeasons(i);
            final List<Season> actualSeasons = seasonFacade.findSeasonsByShow(ShowUtils.newShow(i));
            SeasonUtils.assertSeasonListDeepEquals(actualSeasons, expectedSeasons);
        }

        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link SeasonFacade#findSeasonsByShow(Show)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFindSeasonsByShow_NullArgument() {
        seasonFacade.findSeasonsByShow(null);
    }

    /**
     * Test method for {@link SeasonFacade#findSeasonsByShow(Show)} with show with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFindSeasonsByShow_NullId() {
        seasonFacade.findSeasonsByShow(ShowUtils.newShow(null));
    }

    /**
     * Test method for {@link SeasonFacade#findSeasonsByShow(Show)} with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFindSeasonsByShow_BadId() {
        seasonFacade.findSeasonsByShow(ShowUtils.newShow(Integer.MAX_VALUE));
    }

}
