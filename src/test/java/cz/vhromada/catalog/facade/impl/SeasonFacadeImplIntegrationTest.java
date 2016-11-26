package cz.vhromada.catalog.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.common.EpisodeUtils;
import cz.vhromada.catalog.common.Language;
import cz.vhromada.catalog.common.SeasonUtils;
import cz.vhromada.catalog.common.ShowUtils;
import cz.vhromada.catalog.common.TestConstants;
import cz.vhromada.catalog.domain.Episode;
import cz.vhromada.catalog.domain.Season;
import cz.vhromada.catalog.entity.SeasonTO;
import cz.vhromada.catalog.entity.ShowTO;
import cz.vhromada.catalog.facade.SeasonFacade;
import cz.vhromada.catalog.util.CollectionUtils;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;

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
@ContextConfiguration("classpath:testFacadeContext.xml")
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
     * Test method for {@link SeasonFacade#add(ShowTO, SeasonTO)}.
     */
    @Test
    @DirtiesContext
    public void testAdd() {
        final Season expectedSeason = SeasonUtils.newSeason(SeasonUtils.SEASONS_COUNT + 1);
        expectedSeason.setPosition(Integer.MAX_VALUE);

        seasonFacade.add(ShowUtils.newShowTO(1), SeasonUtils.newSeasonTO(null));

        final Season addedSeason = SeasonUtils.getSeason(entityManager, SeasonUtils.SEASONS_COUNT + 1);
        SeasonUtils.assertSeasonDeepEquals(expectedSeason, addedSeason);

        assertEquals(SeasonUtils.SEASONS_COUNT + 1, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link SeasonFacade#add(ShowTO, SeasonTO)} with null TO for show.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullShowTO() {
        seasonFacade.add(null, SeasonUtils.newSeasonTO(null));
    }

    /**
     * Test method for {@link SeasonFacade#add(ShowTO, SeasonTO)} with null TO for season.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullSeasonTO() {
        seasonFacade.add(ShowUtils.newShowTO(1), null);
    }

    /**
     * Test method for {@link SeasonFacade#add(ShowTO, SeasonTO)} with show with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullId() {
        seasonFacade.add(ShowUtils.newShowTO(null), SeasonUtils.newSeasonTO(null));
    }

    /**
     * Test method for {@link SeasonFacade#add(ShowTO, SeasonTO)} with season with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NotNullId() {
        seasonFacade.add(ShowUtils.newShowTO(1), SeasonUtils.newSeasonTO(1));
    }

    /**
     * Test method for {@link SeasonFacade#add(ShowTO, SeasonTO)} with season with not positive number of season.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NotPositiveNumber() {
        final SeasonTO season = SeasonUtils.newSeasonTO(null);
        season.setNumber(0);

        seasonFacade.add(ShowUtils.newShowTO(1), season);
    }

    /**
     * Test method for {@link SeasonFacade#add(ShowTO, SeasonTO)} with season with bad minimum starting year.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadMinimumStartYear() {
        final SeasonTO season = SeasonUtils.newSeasonTO(null);
        season.setStartYear(TestConstants.BAD_MIN_YEAR);

        seasonFacade.add(ShowUtils.newShowTO(1), season);
    }

    /**
     * Test method for {@link SeasonFacade#add(ShowTO, SeasonTO)} with season with bad maximum starting year.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadMaximumStartYear() {
        final SeasonTO season = SeasonUtils.newSeasonTO(null);
        season.setStartYear(TestConstants.BAD_MAX_YEAR);

        seasonFacade.add(ShowUtils.newShowTO(1), season);
    }

    /**
     * Test method for {@link SeasonFacade#add(ShowTO, SeasonTO)} with season with bad minimum ending year.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadMinimumEndYear() {
        final SeasonTO season = SeasonUtils.newSeasonTO(null);
        season.setEndYear(TestConstants.BAD_MIN_YEAR);

        seasonFacade.add(ShowUtils.newShowTO(1), season);
    }

    /**
     * Test method for {@link SeasonFacade#add(ShowTO, SeasonTO)} with season with bad maximum ending year.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadMaximumEndYear() {
        final SeasonTO season = SeasonUtils.newSeasonTO(null);
        season.setEndYear(TestConstants.BAD_MAX_YEAR);

        seasonFacade.add(ShowUtils.newShowTO(1), season);
    }

    /**
     * Test method for {@link SeasonFacade#add(ShowTO, SeasonTO)} with season with starting year greater than ending year.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadYear() {
        final SeasonTO season = SeasonUtils.newSeasonTO(null);
        season.setStartYear(season.getEndYear() + 1);

        seasonFacade.add(ShowUtils.newShowTO(1), season);
    }

    /**
     * Test method for {@link SeasonFacade#add(ShowTO, SeasonTO)} with season with null language.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullLanguage() {
        final SeasonTO season = SeasonUtils.newSeasonTO(null);
        season.setLanguage(null);

        seasonFacade.add(ShowUtils.newShowTO(1), season);
    }

    /**
     * Test method for {@link SeasonFacade#add(ShowTO, SeasonTO)} with season with null subtitles.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullSubtitles() {
        final SeasonTO season = SeasonUtils.newSeasonTO(null);
        season.setSubtitles(null);

        seasonFacade.add(ShowUtils.newShowTO(1), season);
    }

    /**
     * Test method for {@link SeasonFacade#add(ShowTO, SeasonTO)} with season with subtitles with null value.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadSubtitles() {
        final SeasonTO season = SeasonUtils.newSeasonTO(null);
        season.setSubtitles(CollectionUtils.newList(Language.CZ, null));

        seasonFacade.add(ShowUtils.newShowTO(1), season);
    }

    /**
     * Test method for {@link SeasonFacade#add(ShowTO, SeasonTO)} with season with null note.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullNote() {
        final SeasonTO season = SeasonUtils.newSeasonTO(null);
        season.setNote(null);

        seasonFacade.add(ShowUtils.newShowTO(1), season);
    }

    /**
     * Test method for {@link SeasonFacade#add(ShowTO, SeasonTO)} with season with not existing show.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testAdd_NotExistingShow() {
        seasonFacade.add(ShowUtils.newShowTO(Integer.MAX_VALUE), SeasonUtils.newSeasonTO(null));
    }

    /**
     * Test method for {@link SeasonFacade#update(SeasonTO)}.
     */
    @Test
    @DirtiesContext
    public void testUpdate() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);

        seasonFacade.update(season);

        final Season updatedSeason = SeasonUtils.getSeason(entityManager, 1);
        SeasonUtils.assertSeasonDeepEquals(season, updatedSeason);

        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link SeasonFacade#update(SeasonTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        seasonFacade.update(null);
    }

    /**
     * Test method for {@link SeasonFacade#update(SeasonTO)} with season with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullId() {
        seasonFacade.update(SeasonUtils.newSeasonTO(null));
    }

    /**
     * Test method for {@link SeasonFacade#update(SeasonTO)} with season with not positive number of season.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NotPositiveNumber() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        season.setNumber(0);

        seasonFacade.update(season);
    }

    /**
     * Test method for {@link SeasonFacade#update(SeasonTO)} with season with bad minimum starting year.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_BadMinimumStartYear() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        season.setStartYear(TestConstants.BAD_MIN_YEAR);

        seasonFacade.update(season);
    }

    /**
     * Test method for {@link SeasonFacade#update(SeasonTO)} with season with bad maximum starting year.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_BadMaximumStartYear() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        season.setStartYear(TestConstants.BAD_MAX_YEAR);

        seasonFacade.update(season);
    }

    /**
     * Test method for {@link SeasonFacade#update(SeasonTO)} with season with bad minimum ending year.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_BadMinimumEndYear() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        season.setEndYear(TestConstants.BAD_MIN_YEAR);

        seasonFacade.update(season);
    }

    /**
     * Test method for {@link SeasonFacade#update(SeasonTO)} with season with bad maximum ending year.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_BadMaximumEndYear() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        season.setEndYear(TestConstants.BAD_MAX_YEAR);

        seasonFacade.update(season);
    }

    /**
     * Test method for {@link SeasonFacade#update(SeasonTO)} with season with starting year greater than ending year.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_BadYear() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        season.setStartYear(season.getEndYear() + 1);

        seasonFacade.update(season);
    }

    /**
     * Test method for {@link SeasonFacade#update(SeasonTO)} with season with null language.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullLanguage() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        season.setLanguage(null);

        seasonFacade.update(season);
    }

    /**
     * Test method for {@link SeasonFacade#update(SeasonTO)} with season with null subtitles.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullSubtitles() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        season.setSubtitles(null);

        seasonFacade.update(season);
    }

    /**
     * Test method for {@link SeasonFacade#update(SeasonTO)} with season with subtitles with null value.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_BadSubtitles() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        season.setSubtitles(CollectionUtils.newList(Language.CZ, null));

        seasonFacade.update(season);
    }

    /**
     * Test method for {@link SeasonFacade#update(SeasonTO)} with season with null note.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullNote() {
        final SeasonTO season = SeasonUtils.newSeasonTO(1);
        season.setNote(null);

        seasonFacade.update(season);
    }

    /**
     * Test method for {@link SeasonFacade#update(SeasonTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testUpdate_BadId() {
        seasonFacade.update(SeasonUtils.newSeasonTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#remove(SeasonTO)}.
     */
    @Test
    @DirtiesContext
    public void testRemove() {
        seasonFacade.remove(SeasonUtils.newSeasonTO(1));

        assertNull(SeasonUtils.getSeason(entityManager, 1));

        assertEquals(SeasonUtils.SEASONS_COUNT - 1, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT - EpisodeUtils.EPISODES_PER_SEASON_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link SeasonFacade#remove(SeasonTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        seasonFacade.remove(null);
    }

    /**
     * Test method for {@link SeasonFacade#remove(SeasonTO)} with season with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testRemove_NullId() {
        seasonFacade.remove(SeasonUtils.newSeasonTO(null));
    }

    /**
     * Test method for {@link SeasonFacade#remove(SeasonTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testRemove_BadId() {
        seasonFacade.remove(SeasonUtils.newSeasonTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#duplicate(SeasonTO)}.
     */
    @Test
    @DirtiesContext
    public void testDuplicate() {
        final Season season = SeasonUtils.getSeason(1);
        season.setId(SeasonUtils.SEASONS_COUNT + 1);
        for (final Episode episode : season.getEpisodes()) {
            episode.setId(EpisodeUtils.EPISODES_COUNT + season.getEpisodes().indexOf(episode) + 1);
        }

        seasonFacade.duplicate(SeasonUtils.newSeasonTO(1));

        final Season duplicatedSeason = SeasonUtils.getSeason(entityManager, SeasonUtils.SEASONS_COUNT + 1);
        SeasonUtils.assertSeasonDeepEquals(season, duplicatedSeason);

        assertEquals(SeasonUtils.SEASONS_COUNT + 1, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT + EpisodeUtils.EPISODES_PER_SEASON_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link SeasonFacade#duplicate(SeasonTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        seasonFacade.duplicate(null);
    }

    /**
     * Test method for {@link SeasonFacade#duplicate(SeasonTO)} with season with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testDuplicate_NullId() {
        seasonFacade.duplicate(SeasonUtils.newSeasonTO(null));
    }

    /**
     * Test method for {@link SeasonFacade#duplicate(SeasonTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testDuplicate_BadId() {
        seasonFacade.duplicate(SeasonUtils.newSeasonTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#moveUp(SeasonTO)}.
     */
    @Test
    @DirtiesContext
    public void testMoveUp() {
        final Season season1 = SeasonUtils.getSeason(1, 1);
        season1.setPosition(1);
        final Season season2 = SeasonUtils.getSeason(1, 2);
        season2.setPosition(0);

        seasonFacade.moveUp(SeasonUtils.newSeasonTO(2));

        SeasonUtils.assertSeasonDeepEquals(season1, SeasonUtils.getSeason(entityManager, 1));
        SeasonUtils.assertSeasonDeepEquals(season2, SeasonUtils.getSeason(entityManager, 2));
        for (int i = 3; i <= SeasonUtils.SEASONS_COUNT; i++) {
            SeasonUtils.assertSeasonDeepEquals(SeasonUtils.getSeason(i), SeasonUtils.getSeason(entityManager, i));
        }

        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link SeasonFacade#moveUp(SeasonTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullArgument() {
        seasonFacade.moveUp(null);
    }

    /**
     * Test method for {@link SeasonFacade#moveUp(SeasonTO)} with season with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_NullId() {
        seasonFacade.moveUp(SeasonUtils.newSeasonTO(null));
    }

    /**
     * Test method for {@link SeasonFacade#moveUp(SeasonTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_NotMovableArgument() {
        seasonFacade.moveUp(SeasonUtils.newSeasonTO(1));
    }

    /**
     * Test method for {@link SeasonFacade#moveUp(SeasonTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveUp_BadId() {
        seasonFacade.moveUp(SeasonUtils.newSeasonTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#moveDown(SeasonTO)}.
     */
    @Test
    @DirtiesContext
    public void testMoveDown() {
        final Season season1 = SeasonUtils.getSeason(1, 1);
        season1.setPosition(1);
        final Season season2 = SeasonUtils.getSeason(1, 2);
        season2.setPosition(0);

        seasonFacade.moveDown(SeasonUtils.newSeasonTO(1));

        SeasonUtils.assertSeasonDeepEquals(season1, SeasonUtils.getSeason(entityManager, 1));
        SeasonUtils.assertSeasonDeepEquals(season2, SeasonUtils.getSeason(entityManager, 2));
        for (int i = 3; i <= SeasonUtils.SEASONS_COUNT; i++) {
            SeasonUtils.assertSeasonDeepEquals(SeasonUtils.getSeason(i), SeasonUtils.getSeason(entityManager, i));
        }

        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link SeasonFacade#moveDown(SeasonTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullArgument() {
        seasonFacade.moveDown(null);
    }

    /**
     * Test method for {@link SeasonFacade#moveDown(SeasonTO)} with season with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_NullId() {
        seasonFacade.moveDown(SeasonUtils.newSeasonTO(null));
    }

    /**
     * Test method for {@link SeasonFacade#moveDown(SeasonTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_NotMovableArgument() {
        seasonFacade.moveDown(SeasonUtils.newSeasonTO(SeasonUtils.SEASONS_COUNT));
    }

    /**
     * Test method for {@link SeasonFacade#moveDown(SeasonTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveDown_BadId() {
        seasonFacade.moveDown(SeasonUtils.newSeasonTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link SeasonFacade#findSeasonsByShow(ShowTO)}.
     */
    @Test
    public void testFindSeasonsByShow() {
        for (int i = 1; i <= ShowUtils.SHOWS_COUNT; i++) {
            final List<Season> expectedSeasons = SeasonUtils.getSeasons(i);
            final List<SeasonTO> actualSeasons = seasonFacade.findSeasonsByShow(ShowUtils.newShowTO(i));
            SeasonUtils.assertSeasonListDeepEquals(actualSeasons, expectedSeasons);
        }

        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link SeasonFacade#findSeasonsByShow(ShowTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFindSeasonsByShow_NullArgument() {
        seasonFacade.findSeasonsByShow(null);
    }

    /**
     * Test method for {@link SeasonFacade#findSeasonsByShow(ShowTO)} with show with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testFindSeasonsByShow_NullId() {
        seasonFacade.findSeasonsByShow(ShowUtils.newShowTO(null));
    }

    /**
     * Test method for {@link SeasonFacade#findSeasonsByShow(ShowTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testFindSeasonsByShow_BadId() {
        seasonFacade.findSeasonsByShow(ShowUtils.newShowTO(Integer.MAX_VALUE));
    }

}
