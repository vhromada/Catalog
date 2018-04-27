package cz.vhromada.catalog.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cz.vhromada.catalog.domain.Episode;
import cz.vhromada.catalog.domain.Genre;
import cz.vhromada.catalog.domain.Season;
import cz.vhromada.catalog.domain.Show;
import cz.vhromada.catalog.repository.ShowRepository;
import cz.vhromada.catalog.utils.ShowUtils;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.test.service.MovableServiceTest;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.cache.Cache;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A class represents test for class {@link ShowService}.
 *
 * @author Vladimir Hromada
 */
class ShowServiceTest extends MovableServiceTest<Show> {

    /**
     * Instance of {@link ShowRepository}
     */
    @Mock
    private ShowRepository showRepository;

    /**
     * Test method for {@link ShowService#ShowService(ShowRepository, Cache)} with null repository for shows.
     */
    @Test
    void constructor_NullShowRepository() {
        assertThatThrownBy(() -> new ShowService(null, getCache())).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link ShowService#ShowService(ShowRepository, Cache)} with null cache.
     */
    @Test
    void constructor_NullCache() {
        assertThatThrownBy(() -> new ShowService(showRepository, null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Override
    protected JpaRepository<Show, Integer> getRepository() {
        return showRepository;
    }

    @Override
    protected MovableService<Show> getMovableService() {
        return new ShowService(showRepository, getCache());
    }

    @Override
    protected String getCacheKey() {
        return "shows";
    }

    @Override
    protected Show getItem1() {
        return ShowUtils.newShowWithSeasons(1);
    }

    @Override
    protected Show getItem2() {
        return ShowUtils.newShowWithSeasons(2);
    }

    @Override
    protected Show getAddItem() {
        return ShowUtils.newShowDomain(null);
    }

    @Override
    protected Show getCopyItem() {
        final Show show = ShowUtils.newShowWithSeasons(null);
        for (final Genre genre : show.getGenres()) {
            genre.setId(1);
            genre.setPosition(0);
        }
        for (final Season season : show.getSeasons()) {
            season.setPosition(0);
            for (final Episode episode : season.getEpisodes()) {
                episode.setPosition(0);
            }
        }
        show.setPicture(1);
        show.setPosition(0);

        return show;
    }

    @Override
    protected Class<Show> getItemClass() {
        return Show.class;
    }

    @Override
    protected void assertDataDeepEquals(final Show expected, final Show actual) {
        ShowUtils.assertShowDeepEquals(expected, actual);
    }

}
