package cz.vhromada.catalog.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.entity.Episode;
import cz.vhromada.catalog.entity.Season;
import cz.vhromada.catalog.facade.EpisodeFacade;
import cz.vhromada.catalog.utils.EpisodeUtils;
import cz.vhromada.catalog.utils.SeasonUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents integration test for class {@link EpisodeFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testCatalogContext.xml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class EpisodeFacadeImplIntegrationTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private EntityManager entityManager;

    /**
     * Instance of (@link EpisodeFacade}
     */
    @Autowired
    private EpisodeFacade episodeFacade;

    /**
     * Test method for {@link EpisodeFacade#getEpisode(Integer)}.
     */
    @Test
    public void testGetEpisode() {
        for (int i = 1; i <= EpisodeUtils.EPISODES_COUNT; i++) {
            EpisodeUtils.assertEpisodeDeepEquals(episodeFacade.getEpisode(i), EpisodeUtils.getEpisode(i));
        }

        assertNull(episodeFacade.getEpisode(Integer.MAX_VALUE));

        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link EpisodeFacade#getEpisode(Integer)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetEpisode_NullArgument() {
        episodeFacade.getEpisode(null);
    }

    /**
     * Test method for {@link EpisodeFacade#add(Season, Episode)}.
     */
    @Test
    @DirtiesContext
    public void testAdd() {
        final cz.vhromada.catalog.domain.Episode expectedEpisode = EpisodeUtils.newEpisodeDomain(EpisodeUtils.EPISODES_COUNT + 1);
        expectedEpisode.setPosition(Integer.MAX_VALUE);

        episodeFacade.add(SeasonUtils.newSeason(1), EpisodeUtils.newEpisode(null));

        final cz.vhromada.catalog.domain.Episode addedEpisode = EpisodeUtils.getEpisode(entityManager, EpisodeUtils.EPISODES_COUNT + 1);
        EpisodeUtils.assertEpisodeDeepEquals(expectedEpisode, addedEpisode);

        assertEquals(EpisodeUtils.EPISODES_COUNT + 1, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with null TO for season.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullSeasonTO() {
        episodeFacade.add(null, EpisodeUtils.newEpisode(null));
    }

    /**
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with null TO for episode.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullEpisodeTO() {
        episodeFacade.add(SeasonUtils.newSeason(1), null);
    }

    /**
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with season with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullId() {
        episodeFacade.add(SeasonUtils.newSeason(null), EpisodeUtils.newEpisode(null));
    }

    /**
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with episode with not null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NotNullId() {
        episodeFacade.add(SeasonUtils.newSeason(1), EpisodeUtils.newEpisode(1));
    }

    /**
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with episode with not positive number of episode.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NotPositiveNumber() {
        final Episode episode = EpisodeUtils.newEpisode(null);
        episode.setNumber(0);

        episodeFacade.add(SeasonUtils.newSeason(1), episode);
    }

    /**
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with episode with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullName() {
        final Episode episode = EpisodeUtils.newEpisode(null);
        episode.setName(null);

        episodeFacade.add(SeasonUtils.newSeason(1), episode);
    }

    /**
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with episode with empty string as name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_EmptyName() {
        final Episode episode = EpisodeUtils.newEpisode(null);
        episode.setName("");

        episodeFacade.add(SeasonUtils.newSeason(1), episode);
    }

    /**
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with episode with negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NegativeLength() {
        final Episode episode = EpisodeUtils.newEpisode(null);
        episode.setLength(-1);

        episodeFacade.add(SeasonUtils.newSeason(1), episode);
    }

    /**
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with episode with null note.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullNote() {
        final Episode episode = EpisodeUtils.newEpisode(null);
        episode.setNote(null);

        episodeFacade.add(SeasonUtils.newSeason(1), episode);
    }

    /**
     * Test method for {@link EpisodeFacade#add(Season, Episode)} with episode with not existing season.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NotExistingSeason() {
        episodeFacade.add(SeasonUtils.newSeason(Integer.MAX_VALUE), EpisodeUtils.newEpisode(null));
    }

    /**
     * Test method for {@link EpisodeFacade#update(Episode)}.
     */
    @Test
    @DirtiesContext
    public void testUpdate() {
        final Episode episode = EpisodeUtils.newEpisode(1);

        episodeFacade.update(episode);

        final cz.vhromada.catalog.domain.Episode updatedEpisode = EpisodeUtils.getEpisode(entityManager, 1);
        EpisodeUtils.assertEpisodeDeepEquals(episode, updatedEpisode);

        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link EpisodeFacade#update(Episode)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        episodeFacade.update(null);
    }

    /**
     * Test method for {@link EpisodeFacade#update(Episode)} with episode with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullId() {
        episodeFacade.update(EpisodeUtils.newEpisode(null));
    }

    /**
     * Test method for {@link EpisodeFacade#update(Episode)} with episode with not positive number of episode.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NotPositiveNumber() {
        final Episode episode = EpisodeUtils.newEpisode(1);
        episode.setNumber(0);

        episodeFacade.update(episode);
    }

    /**
     * Test method for {@link EpisodeFacade#update(Episode)} with episode with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullName() {
        final Episode episode = EpisodeUtils.newEpisode(1);
        episode.setName(null);

        episodeFacade.update(episode);
    }

    /**
     * Test method for {@link EpisodeFacade#update(Episode)} with episode with empty string as name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_EmptyName() {
        final Episode episode = EpisodeUtils.newEpisode(1);
        episode.setName(null);

        episodeFacade.update(episode);
    }

    /**
     * Test method for {@link EpisodeFacade#update(Episode)} with episode with negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NegativeLength() {
        final Episode episode = EpisodeUtils.newEpisode(1);
        episode.setLength(-1);

        episodeFacade.update(episode);
    }

    /**
     * Test method for {@link EpisodeFacade#update(Episode)} with episode with null note.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullNote() {
        final Episode episode = EpisodeUtils.newEpisode(1);
        episode.setNote(null);

        episodeFacade.update(episode);
    }

    /**
     * Test method for {@link EpisodeFacade#update(Episode)} with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_BadId() {
        episodeFacade.update(EpisodeUtils.newEpisode(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#remove(Episode)}.
     */
    @Test
    @DirtiesContext
    public void testRemove() {
        episodeFacade.remove(EpisodeUtils.newEpisode(1));

        assertNull(EpisodeUtils.getEpisode(entityManager, 1));

        assertEquals(EpisodeUtils.EPISODES_COUNT - 1, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link EpisodeFacade#remove(Episode)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        episodeFacade.remove(null);
    }

    /**
     * Test method for {@link EpisodeFacade#remove(Episode)} with episode with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullId() {
        episodeFacade.remove(EpisodeUtils.newEpisode(null));
    }

    /**
     * Test method for {@link EpisodeFacade#remove(Episode)} with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_BadId() {
        episodeFacade.remove(EpisodeUtils.newEpisode(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#duplicate(Episode)}.
     */
    @Test
    @DirtiesContext
    public void testDuplicate() {
        final cz.vhromada.catalog.domain.Episode episode = EpisodeUtils.getEpisode(1);
        episode.setId(EpisodeUtils.EPISODES_COUNT + 1);

        episodeFacade.duplicate(EpisodeUtils.newEpisode(1));

        final cz.vhromada.catalog.domain.Episode duplicatedEpisode = EpisodeUtils.getEpisode(entityManager, EpisodeUtils.EPISODES_COUNT + 1);
        EpisodeUtils.assertEpisodeDeepEquals(episode, duplicatedEpisode);

        assertEquals(EpisodeUtils.EPISODES_COUNT + 1, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link EpisodeFacade#duplicate(Episode)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        episodeFacade.duplicate(null);
    }

    /**
     * Test method for {@link EpisodeFacade#duplicate(Episode)} with episode with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullId() {
        episodeFacade.duplicate(EpisodeUtils.newEpisode(null));
    }

    /**
     * Test method for {@link EpisodeFacade#duplicate(Episode)} with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_BadId() {
        episodeFacade.duplicate(EpisodeUtils.newEpisode(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(Episode)}.
     */
    @Test
    @DirtiesContext
    public void testMoveUp() {
        final cz.vhromada.catalog.domain.Episode episode1 = EpisodeUtils.getEpisode(1);
        episode1.setPosition(1);
        final cz.vhromada.catalog.domain.Episode episode2 = EpisodeUtils.getEpisode(2);
        episode2.setPosition(0);

        episodeFacade.moveUp(EpisodeUtils.newEpisode(2));

        EpisodeUtils.assertEpisodeDeepEquals(episode1, EpisodeUtils.getEpisode(entityManager, 1));
        EpisodeUtils.assertEpisodeDeepEquals(episode2, EpisodeUtils.getEpisode(entityManager, 2));
        for (int i = 3; i <= EpisodeUtils.EPISODES_COUNT; i++) {
            EpisodeUtils.assertEpisodeDeepEquals(EpisodeUtils.getEpisode(i), EpisodeUtils.getEpisode(entityManager, i));
        }

        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(Episode)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullArgument() {
        episodeFacade.moveUp(null);
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(Episode)} with episode with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullId() {
        episodeFacade.moveUp(EpisodeUtils.newEpisode(null));
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(Episode)} with not movable argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NotMovableArgument() {
        episodeFacade.moveUp(EpisodeUtils.newEpisode(1));
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(Episode)} with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_BadId() {
        episodeFacade.moveUp(EpisodeUtils.newEpisode(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(Episode)}.
     */
    @Test
    @DirtiesContext
    public void testMoveDown() {
        final cz.vhromada.catalog.domain.Episode episode1 = EpisodeUtils.getEpisode(1);
        episode1.setPosition(1);
        final cz.vhromada.catalog.domain.Episode episode2 = EpisodeUtils.getEpisode(2);
        episode2.setPosition(0);

        episodeFacade.moveDown(EpisodeUtils.newEpisode(1));

        EpisodeUtils.assertEpisodeDeepEquals(episode1, EpisodeUtils.getEpisode(entityManager, 1));
        EpisodeUtils.assertEpisodeDeepEquals(episode2, EpisodeUtils.getEpisode(entityManager, 2));
        for (int i = 3; i <= EpisodeUtils.EPISODES_COUNT; i++) {
            EpisodeUtils.assertEpisodeDeepEquals(EpisodeUtils.getEpisode(i), EpisodeUtils.getEpisode(entityManager, i));
        }

        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(Episode)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullArgument() {
        episodeFacade.moveDown(null);
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(Episode)} with episode with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullId() {
        episodeFacade.moveDown(EpisodeUtils.newEpisode(null));
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(Episode)} with not movable argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NotMovableArgument() {
        episodeFacade.moveDown(EpisodeUtils.newEpisode(EpisodeUtils.EPISODES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(Episode)} with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_BadId() {
        episodeFacade.moveDown(EpisodeUtils.newEpisode(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#findEpisodesBySeason(Season)}.
     */
    @Test
    public void testFindEpisodesBySeason() {
        for (int i = 0; i < SeasonUtils.SEASONS_COUNT; i++) {
            final int showNumber = i / SeasonUtils.SEASONS_PER_SHOW_COUNT + 1;
            final int seasonNumber = i % SeasonUtils.SEASONS_PER_SHOW_COUNT + 1;
            EpisodeUtils.assertEpisodeListDeepEquals(episodeFacade.findEpisodesBySeason(SeasonUtils.newSeason(i + 1)),
                    EpisodeUtils.getEpisodes(showNumber, seasonNumber));
        }

        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link EpisodeFacade#findEpisodesBySeason(Season)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFindEpisodesBySeason_NullArgument() {
        episodeFacade.findEpisodesBySeason(null);
    }

    /**
     * Test method for {@link EpisodeFacade#findEpisodesBySeason(Season)} with season with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFindEpisodesBySeason_NullId() {
        episodeFacade.findEpisodesBySeason(SeasonUtils.newSeason(null));
    }

    /**
     * Test method for {@link EpisodeFacade#findEpisodesBySeason(Season)} with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFindEpisodesBySeason_BadId() {
        episodeFacade.findEpisodesBySeason(SeasonUtils.newSeason(Integer.MAX_VALUE));
    }

}
