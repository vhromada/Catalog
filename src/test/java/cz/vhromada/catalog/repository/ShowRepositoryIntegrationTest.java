package cz.vhromada.catalog.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.domain.Season;
import cz.vhromada.catalog.domain.Show;
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

        assertSoftly(softly -> {
            softly.assertThat(ShowUtils.getShowsCount(entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT);
            softly.assertThat(SeasonUtils.getSeasonsCount(entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT);
            softly.assertThat(EpisodeUtils.getEpisodesCount(entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT);
        });
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

        assertThat(showRepository.findById(Integer.MAX_VALUE).isPresent()).isFalse();

        assertSoftly(softly -> {
            softly.assertThat(ShowUtils.getShowsCount(entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT);
            softly.assertThat(SeasonUtils.getSeasonsCount(entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT);
            softly.assertThat(EpisodeUtils.getEpisodesCount(entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT);
        });
    }

    /**
     * Test method for add show.
     */
    @Test
    void add() {
        final Show show = ShowUtils.newShowDomain(null);
        show.setPosition(ShowUtils.SHOWS_COUNT);
        show.setGenres(Collections.singletonList(GenreUtils.getGenre(entityManager, 1)));

        showRepository.save(show);

        assertThat(show.getId()).isEqualTo(ShowUtils.SHOWS_COUNT + 1);

        final Show addedShow = ShowUtils.getShow(entityManager, ShowUtils.SHOWS_COUNT + 1);
        final Show expectedAddedShow = ShowUtils.newShowDomain(null);
        expectedAddedShow.setId(ShowUtils.SHOWS_COUNT + 1);
        expectedAddedShow.setPosition(ShowUtils.SHOWS_COUNT);
        expectedAddedShow.setGenres(Collections.singletonList(GenreUtils.getGenreDomain(1)));
        ShowUtils.assertShowDeepEquals(expectedAddedShow, addedShow);

        assertSoftly(softly -> {
            softly.assertThat(ShowUtils.getShowsCount(entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT + 1);
            softly.assertThat(SeasonUtils.getSeasonsCount(entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT);
            softly.assertThat(EpisodeUtils.getEpisodesCount(entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT);
        });
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

        assertSoftly(softly -> {
            softly.assertThat(ShowUtils.getShowsCount(entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT);
            softly.assertThat(SeasonUtils.getSeasonsCount(entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT);
            softly.assertThat(EpisodeUtils.getEpisodesCount(entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT);
        });
    }

    /**
     * Test method for update show with added season.
     */
    @Test
    void update_AddedSeason() {
        final Season season = SeasonUtils.newSeasonDomain(null);
        season.setPosition(SeasonUtils.SEASONS_COUNT);
        entityManager.persist(season);

        final Show show = ShowUtils.getShow(entityManager, 1);
        show.getSeasons().add(season);

        showRepository.save(show);

        final Show updatedShow = ShowUtils.getShow(entityManager, 1);
        final Season expectedSeason = SeasonUtils.newSeasonDomain(null);
        expectedSeason.setId(SeasonUtils.SEASONS_COUNT + 1);
        expectedSeason.setPosition(SeasonUtils.SEASONS_COUNT);
        final Show expectedUpdatedShow = ShowUtils.getShow(1);
        expectedUpdatedShow.getSeasons().add(expectedSeason);
        ShowUtils.assertShowDeepEquals(expectedUpdatedShow, updatedShow);

        assertSoftly(softly -> {
            softly.assertThat(ShowUtils.getShowsCount(entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT);
            softly.assertThat(SeasonUtils.getSeasonsCount(entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT + 1);
            softly.assertThat(EpisodeUtils.getEpisodesCount(entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT);
        });
    }

    /**
     * Test method for remove show.
     */
    @Test
    void remove() {
        final int seasonsCount = ShowUtils.getShow(1).getSeasons().size();
        final int episodesCount = seasonsCount * EpisodeUtils.EPISODES_PER_SEASON_COUNT;

        showRepository.delete(ShowUtils.getShow(entityManager, 1));

        assertThat(ShowUtils.getShow(entityManager, 1)).isNull();

        assertSoftly(softly -> {
            softly.assertThat(ShowUtils.getShowsCount(entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT - 1);
            softly.assertThat(SeasonUtils.getSeasonsCount(entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT - seasonsCount);
            softly.assertThat(EpisodeUtils.getEpisodesCount(entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT - episodesCount);
        });
    }

    /**
     * Test method for remove all shows.
     */
    @Test
    void removeAll() {
        showRepository.deleteAll();

        assertSoftly(softly -> {
            softly.assertThat(ShowUtils.getShowsCount(entityManager)).isEqualTo(0);
            softly.assertThat(SeasonUtils.getSeasonsCount(entityManager)).isEqualTo(0);
            softly.assertThat(EpisodeUtils.getEpisodesCount(entityManager)).isEqualTo(0);
        });
    }

}
