package cz.vhromada.catalog.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.CatalogConfiguration;
import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.domain.Season;
import cz.vhromada.catalog.domain.Show;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.EpisodeUtils;
import cz.vhromada.catalog.utils.GenreUtils;
import cz.vhromada.catalog.utils.SeasonUtils;
import cz.vhromada.catalog.utils.ShowUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents integration test for class {@link ShowRepository}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CatalogConfiguration.class, CatalogTestConfiguration.class })
@Transactional
@Rollback
public class ShowRepositoryIntegrationTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private EntityManager entityManager;

    /**
     * Instance of {@link ShowRepository}
     */
    @Autowired
    private ShowRepository showRepository;

    /**
     * Test method for get shows.
     */
    @Test
    public void testGetShows() {
        final List<Show> shows = showRepository.findAll(new Sort("position", "id"));

        ShowUtils.assertShowsDeepEquals(ShowUtils.getShows(), shows);

        assertEquals(ShowUtils.SHOWS_COUNT, ShowUtils.getShowsCount(entityManager));
        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for get show.
     */
    @Test
    public void testGetShow() {
        for (int i = 1; i <= ShowUtils.SHOWS_COUNT; i++) {
            final Show show = showRepository.findOne(i);

            ShowUtils.assertShowDeepEquals(ShowUtils.getShow(i), show);
        }

        assertNull(showRepository.findOne(Integer.MAX_VALUE));

        assertEquals(ShowUtils.SHOWS_COUNT, ShowUtils.getShowsCount(entityManager));
        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for add show.
     */
    @Test
    public void testAdd() {
        final Show show = ShowUtils.newShowDomain(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.getGenre(entityManager, 1)));

        showRepository.saveAndFlush(show);

        assertNotNull(show.getId());
        assertEquals(ShowUtils.SHOWS_COUNT + 1, show.getId().intValue());

        final Show addedShow = ShowUtils.getShow(entityManager, ShowUtils.SHOWS_COUNT + 1);
        final Show expectedAddedShow = ShowUtils.newShowDomain(null);
        expectedAddedShow.setId(ShowUtils.SHOWS_COUNT + 1);
        expectedAddedShow.setGenres(CollectionUtils.newList(GenreUtils.getGenreDomain(1)));
        ShowUtils.assertShowDeepEquals(expectedAddedShow, addedShow);

        assertEquals(ShowUtils.SHOWS_COUNT + 1, ShowUtils.getShowsCount(entityManager));
        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for update show with updated data.
     */
    @Test
    public void testUpdate_Data() {
        final Show show = ShowUtils.updateShow(entityManager, 1);

        showRepository.saveAndFlush(show);

        final Show updatedShow = ShowUtils.getShow(entityManager, 1);
        final Show expectedUpdatedShow = ShowUtils.getShow(1);
        ShowUtils.updateShow(expectedUpdatedShow);
        expectedUpdatedShow.setPosition(ShowUtils.POSITION);
        ShowUtils.assertShowDeepEquals(expectedUpdatedShow, updatedShow);

        assertEquals(ShowUtils.SHOWS_COUNT, ShowUtils.getShowsCount(entityManager));
        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for update show with added season.
     */
    @Test
    public void testUpdate_AddedSeason() {
        final Season season = SeasonUtils.newSeasonDomain(null);
        entityManager.persist(season);

        final Show show = ShowUtils.getShow(entityManager, 1);
        show.getSeasons().add(season);

        showRepository.saveAndFlush(show);

        final Show updatedShow = ShowUtils.getShow(entityManager, 1);
        final Season expectedSeason = SeasonUtils.newSeasonDomain(null);
        expectedSeason.setId(SeasonUtils.SEASONS_COUNT + 1);
        final Show expectedUpdatedShow = ShowUtils.getShow(1);
        expectedUpdatedShow.getSeasons().add(expectedSeason);
        ShowUtils.assertShowDeepEquals(expectedUpdatedShow, updatedShow);

        assertEquals(ShowUtils.SHOWS_COUNT, ShowUtils.getShowsCount(entityManager));
        assertEquals(SeasonUtils.SEASONS_COUNT + 1, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for remove show.
     */
    @Test
    public void testRemove() {
        final int seasonsCount = ShowUtils.getShow(1).getSeasons().size();
        final int episodesCount = seasonsCount * EpisodeUtils.EPISODES_PER_SEASON_COUNT;

        showRepository.delete(ShowUtils.getShow(entityManager, 1));

        assertNull(ShowUtils.getShow(entityManager, 1));

        assertEquals(ShowUtils.SHOWS_COUNT - 1, ShowUtils.getShowsCount(entityManager));
        assertEquals(SeasonUtils.SEASONS_COUNT - seasonsCount, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT - episodesCount, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for remove all shows.
     */
    @Test
    public void testRemoveAll() {
        showRepository.deleteAll();

        assertEquals(0, ShowUtils.getShowsCount(entityManager));
        assertEquals(0, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(0, EpisodeUtils.getEpisodesCount(entityManager));
    }

}
