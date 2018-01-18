package cz.vhromada.catalog.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.domain.Season;
import cz.vhromada.catalog.domain.Show;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.EpisodeUtils;
import cz.vhromada.catalog.utils.GenreUtils;
import cz.vhromada.catalog.utils.SeasonUtils;
import cz.vhromada.catalog.utils.ShowUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents integration test for class {@link ShowRepository}.
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
@Transactional
@Rollback
class ShowRepositoryIntegrationTest {

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
    void getShows() {
        final List<Show> shows = showRepository.findAll(Sort.by("position", "id"));

        ShowUtils.assertShowsDeepEquals(ShowUtils.getShows(), shows);

        assertEquals(ShowUtils.SHOWS_COUNT, ShowUtils.getShowsCount(entityManager));
        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for get show.
     */
    @Test
    void getShow() {
        for (int i = 1; i <= ShowUtils.SHOWS_COUNT; i++) {
            final Show show = showRepository.findById(i).orElse(null);

            ShowUtils.assertShowDeepEquals(ShowUtils.getShow(i), show);
        }

        assertFalse(showRepository.findById(Integer.MAX_VALUE).isPresent());

        assertEquals(ShowUtils.SHOWS_COUNT, ShowUtils.getShowsCount(entityManager));
        assertEquals(SeasonUtils.SEASONS_COUNT, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(EpisodeUtils.EPISODES_COUNT, EpisodeUtils.getEpisodesCount(entityManager));
    }

    /**
     * Test method for add show.
     */
    @Test
    void add() {
        final Show show = ShowUtils.newShowDomain(null);
        show.setGenres(CollectionUtils.newList(GenreUtils.getGenre(entityManager, 1)));

        showRepository.save(show);

        assertEquals(Integer.valueOf(ShowUtils.SHOWS_COUNT + 1), show.getId());

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
    void update_Data() {
        final Show show = ShowUtils.updateShow(entityManager, 1);

        showRepository.save(show);

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
    void update_AddedSeason() {
        final Season season = SeasonUtils.newSeasonDomain(null);
        entityManager.persist(season);

        final Show show = ShowUtils.getShow(entityManager, 1);
        show.getSeasons().add(season);

        showRepository.save(show);

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
    void remove() {
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
    void removeAll() {
        showRepository.deleteAll();

        assertEquals(0, ShowUtils.getShowsCount(entityManager));
        assertEquals(0, SeasonUtils.getSeasonsCount(entityManager));
        assertEquals(0, EpisodeUtils.getEpisodesCount(entityManager));
    }

}
