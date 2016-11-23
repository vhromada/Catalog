package cz.vhromada.catalog.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.common.EpisodeUtils;
import cz.vhromada.catalog.common.SeasonUtils;
import cz.vhromada.catalog.domain.Episode;
import cz.vhromada.catalog.entity.EpisodeTO;
import cz.vhromada.catalog.entity.SeasonTO;
import cz.vhromada.catalog.facade.EpisodeFacade;
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
 * A class represents integration test for class {@link EpisodeFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeContext.xml")
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
     * Test method for {@link EpisodeFacade#add(SeasonTO, EpisodeTO)}.
     */
    @Test
    @DirtiesContext
    public void testAdd() {
        final Episode expectedEpisode = EpisodeUtils.newEpisode(EpisodeUtils.EPISODES_COUNT + 1);
        expectedEpisode.setPosition(Integer.MAX_VALUE);

        episodeFacade.add(SeasonUtils.newSeasonTO(1), EpisodeUtils.newEpisodeTO(null));

        final Episode addedEpisode = EpisodeUtils.getEpisode(entityManager, EpisodeUtils.EPISODES_COUNT + 1);
        EpisodeUtils.assertEpisodeDeepEquals(expectedEpisode, addedEpisode);

        assertEquals(EpisodeUtils.EPISODES_COUNT + 1, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link EpisodeFacade#add(SeasonTO, EpisodeTO)} with null TO for season.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullSeasonTO() {
        episodeFacade.add(null, EpisodeUtils.newEpisodeTO(null));
    }

    /**
     * Test method for {@link EpisodeFacade#add(SeasonTO, EpisodeTO)} with null TO for episode.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullEpisodeTO() {
        episodeFacade.add(SeasonUtils.newSeasonTO(1), null);
    }

    /**
     * Test method for {@link EpisodeFacade#add(SeasonTO, EpisodeTO)} with season with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullId() {
        episodeFacade.add(SeasonUtils.newSeasonTO(null), EpisodeUtils.newEpisodeTO(null));
    }

    /**
     * Test method for {@link EpisodeFacade#add(SeasonTO, EpisodeTO)} with episode with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NotNullId() {
        episodeFacade.add(SeasonUtils.newSeasonTO(1), EpisodeUtils.newEpisodeTO(1));
    }

    /**
     * Test method for {@link EpisodeFacade#add(SeasonTO, EpisodeTO)} with episode with not positive number of episode.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NotPositiveNumber() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(null);
        episode.setNumber(0);

        episodeFacade.add(SeasonUtils.newSeasonTO(1), episode);
    }

    /**
     * Test method for {@link EpisodeFacade#add(SeasonTO, EpisodeTO)} with episode with null name.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullName() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(null);
        episode.setName(null);

        episodeFacade.add(SeasonUtils.newSeasonTO(1), episode);
    }

    /**
     * Test method for {@link EpisodeFacade#add(SeasonTO, EpisodeTO)} with episode with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_EmptyName() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(null);
        episode.setName("");

        episodeFacade.add(SeasonUtils.newSeasonTO(1), episode);
    }

    /**
     * Test method for {@link EpisodeFacade#add(SeasonTO, EpisodeTO)} with episode with negative length.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NegativeLength() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(null);
        episode.setLength(-1);

        episodeFacade.add(SeasonUtils.newSeasonTO(1), episode);
    }

    /**
     * Test method for {@link EpisodeFacade#add(SeasonTO, EpisodeTO)} with episode with null note.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullNote() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(null);
        episode.setNote(null);

        episodeFacade.add(SeasonUtils.newSeasonTO(1), episode);
    }

    /**
     * Test method for {@link EpisodeFacade#add(SeasonTO, EpisodeTO)} with episode with not existing season.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testAdd_NotExistingSeason() {
        episodeFacade.add(SeasonUtils.newSeasonTO(Integer.MAX_VALUE), EpisodeUtils.newEpisodeTO(null));
    }

    /**
     * Test method for {@link EpisodeFacade#update(EpisodeTO)}.
     */
    @Test
    @DirtiesContext
    public void testUpdate() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(1);

        episodeFacade.update(episode);

        final Episode updatedEpisode = EpisodeUtils.getEpisode(entityManager, 1);
        EpisodeUtils.assertEpisodeDeepEquals(episode, updatedEpisode);

        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link EpisodeFacade#update(EpisodeTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        episodeFacade.update(null);
    }

    /**
     * Test method for {@link EpisodeFacade#update(EpisodeTO)} with episode with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullId() {
        episodeFacade.update(EpisodeUtils.newEpisodeTO(null));
    }

    /**
     * Test method for {@link EpisodeFacade#update(EpisodeTO)} with episode with not positive number of episode.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NotPositiveNumber() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(1);
        episode.setNumber(0);

        episodeFacade.update(episode);
    }

    /**
     * Test method for {@link EpisodeFacade#update(EpisodeTO)} with episode with null name.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullName() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(1);
        episode.setName(null);

        episodeFacade.update(episode);
    }

    /**
     * Test method for {@link EpisodeFacade#update(EpisodeTO)} with episode with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_EmptyName() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(1);
        episode.setName(null);

        episodeFacade.update(episode);
    }

    /**
     * Test method for {@link EpisodeFacade#update(EpisodeTO)} with episode with negative length.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NegativeLength() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(1);
        episode.setLength(-1);

        episodeFacade.update(episode);
    }

    /**
     * Test method for {@link EpisodeFacade#update(EpisodeTO)} with episode with null note.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullNote() {
        final EpisodeTO episode = EpisodeUtils.newEpisodeTO(1);
        episode.setNote(null);

        episodeFacade.update(episode);
    }

    /**
     * Test method for {@link EpisodeFacade#update(EpisodeTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testUpdate_BadId() {
        episodeFacade.update(EpisodeUtils.newEpisodeTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#remove(EpisodeTO)}.
     */
    @Test
    @DirtiesContext
    public void testRemove() {
        episodeFacade.remove(EpisodeUtils.newEpisodeTO(1));

        assertNull(EpisodeUtils.getEpisode(entityManager, 1));

        assertEquals(EpisodeUtils.EPISODES_COUNT - 1, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link EpisodeFacade#remove(EpisodeTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        episodeFacade.remove(null);
    }

    /**
     * Test method for {@link EpisodeFacade#remove(EpisodeTO)} with episode with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testRemove_NullId() {
        episodeFacade.remove(EpisodeUtils.newEpisodeTO(null));
    }

    /**
     * Test method for {@link EpisodeFacade#remove(EpisodeTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testRemove_BadId() {
        episodeFacade.remove(EpisodeUtils.newEpisodeTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#duplicate(EpisodeTO)}.
     */
    @Test
    @DirtiesContext
    public void testDuplicate() {
        final Episode episode = EpisodeUtils.getEpisode(1);
        episode.setId(EpisodeUtils.EPISODES_COUNT + 1);

        episodeFacade.duplicate(EpisodeUtils.newEpisodeTO(1));

        final Episode duplicatedEpisode = EpisodeUtils.getEpisode(entityManager, EpisodeUtils.EPISODES_COUNT + 1);
        EpisodeUtils.assertEpisodeDeepEquals(episode, duplicatedEpisode);

        assertEquals(EpisodeUtils.EPISODES_COUNT + 1, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link EpisodeFacade#duplicate(EpisodeTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        episodeFacade.duplicate(null);
    }

    /**
     * Test method for {@link EpisodeFacade#duplicate(EpisodeTO)} with episode with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testDuplicate_NullId() {
        episodeFacade.duplicate(EpisodeUtils.newEpisodeTO(null));
    }

    /**
     * Test method for {@link EpisodeFacade#duplicate(EpisodeTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testDuplicate_BadId() {
        episodeFacade.duplicate(EpisodeUtils.newEpisodeTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(EpisodeTO)}.
     */
    @Test
    @DirtiesContext
    public void testMoveUp() {
        final Episode episode1 = EpisodeUtils.getEpisode(1);
        episode1.setPosition(1);
        final Episode episode2 = EpisodeUtils.getEpisode(2);
        episode2.setPosition(0);

        episodeFacade.moveUp(EpisodeUtils.newEpisodeTO(2));

        EpisodeUtils.assertEpisodeDeepEquals(episode1, EpisodeUtils.getEpisode(entityManager, 1));
        EpisodeUtils.assertEpisodeDeepEquals(episode2, EpisodeUtils.getEpisode(entityManager, 2));
        for (int i = 3; i <= EpisodeUtils.EPISODES_COUNT; i++) {
            EpisodeUtils.assertEpisodeDeepEquals(EpisodeUtils.getEpisode(i), EpisodeUtils.getEpisode(entityManager, i));
        }

        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(EpisodeTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullArgument() {
        episodeFacade.moveUp(null);
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(EpisodeTO)} with episode with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_NullId() {
        episodeFacade.moveUp(EpisodeUtils.newEpisodeTO(null));
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(EpisodeTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_NotMovableArgument() {
        episodeFacade.moveUp(EpisodeUtils.newEpisodeTO(1));
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(EpisodeTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveUp_BadId() {
        episodeFacade.moveUp(EpisodeUtils.newEpisodeTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(EpisodeTO)}.
     */
    @Test
    @DirtiesContext
    public void testMoveDown() {
        final Episode episode1 = EpisodeUtils.getEpisode(1);
        episode1.setPosition(1);
        final Episode episode2 = EpisodeUtils.getEpisode(2);
        episode2.setPosition(0);

        episodeFacade.moveDown(EpisodeUtils.newEpisodeTO(1));

        EpisodeUtils.assertEpisodeDeepEquals(episode1, EpisodeUtils.getEpisode(entityManager, 1));
        EpisodeUtils.assertEpisodeDeepEquals(episode2, EpisodeUtils.getEpisode(entityManager, 2));
        for (int i = 3; i <= EpisodeUtils.EPISODES_COUNT; i++) {
            EpisodeUtils.assertEpisodeDeepEquals(EpisodeUtils.getEpisode(i), EpisodeUtils.getEpisode(entityManager, i));
        }

        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(EpisodeTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullArgument() {
        episodeFacade.moveDown(null);
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(EpisodeTO)} with episode with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_NullId() {
        episodeFacade.moveDown(EpisodeUtils.newEpisodeTO(null));
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(EpisodeTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_NotMovableArgument() {
        episodeFacade.moveDown(EpisodeUtils.newEpisodeTO(EpisodeUtils.EPISODES_COUNT));
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(EpisodeTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveDown_BadId() {
        episodeFacade.moveDown(EpisodeUtils.newEpisodeTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link EpisodeFacade#findEpisodesBySeason(SeasonTO)}.
     */
    @Test
    public void testFindEpisodesBySeason() {
        for (int i = 0; i < SeasonUtils.SEASONS_COUNT; i++) {
            final int showNumber = i / SeasonUtils.SEASONS_PER_SHOW_COUNT + 1;
            final int seasonNumber = i % SeasonUtils.SEASONS_PER_SHOW_COUNT + 1;
            EpisodeUtils.assertEpisodeListDeepEquals(episodeFacade.findEpisodesBySeason(SeasonUtils.newSeasonTO(i + 1)),
                    EpisodeUtils.getEpisodes(showNumber, seasonNumber));
        }

        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for {@link EpisodeFacade#findEpisodesBySeason(SeasonTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFindEpisodesBySeason_NullArgument() {
        episodeFacade.findEpisodesBySeason(null);
    }

    /**
     * Test method for {@link EpisodeFacade#findEpisodesBySeason(SeasonTO)} with season with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testFindEpisodesBySeason_NullId() {
        episodeFacade.findEpisodesBySeason(SeasonUtils.newSeasonTO(null));
    }

    /**
     * Test method for {@link EpisodeFacade#findEpisodesBySeason(SeasonTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testFindEpisodesBySeason_BadId() {
        episodeFacade.findEpisodesBySeason(SeasonUtils.newSeasonTO(Integer.MAX_VALUE));
    }

}
