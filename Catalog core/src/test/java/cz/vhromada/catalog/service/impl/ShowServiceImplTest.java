package cz.vhromada.catalog.service.impl;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.EpisodeUtils;
import cz.vhromada.catalog.commons.SeasonUtils;
import cz.vhromada.catalog.commons.ShowUtils;
import cz.vhromada.catalog.entities.Episode;
import cz.vhromada.catalog.entities.Season;
import cz.vhromada.catalog.entities.Show;
import cz.vhromada.catalog.repository.ShowRepository;
import cz.vhromada.catalog.service.CatalogService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.cache.Cache;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A class represents test for class {@link ShowServiceImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class ShowServiceImplTest extends AbstractServiceTest<Show> {

    /**
     * Instance of {@link ShowRepository}
     */
    @Mock
    private ShowRepository showRepository;

    /**
     * Test method for {@link ShowServiceImpl#ShowServiceImpl(ShowRepository, Cache)} with null repository for shows.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullShowRepository() {
        new ShowServiceImpl(null, getCache());
    }

    /**
     * Test method for {@link ShowServiceImpl#ShowServiceImpl(ShowRepository, Cache)} with null cache.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullCache() {
        new ShowServiceImpl(showRepository, null);
    }

    @Override
    protected JpaRepository<Show, Integer> getRepository() {
        return showRepository;
    }

    @Override
    protected CatalogService<Show> getCatalogService() {
        return new ShowServiceImpl(showRepository, getCache());
    }

    @Override
    protected String getCacheKey() {
        return "shows";
    }

    @Override
    protected Show getItem1() {
        return setSeasons(ShowUtils.newShow(1));
    }

    @Override
    protected Show getItem2() {
        return setSeasons(ShowUtils.newShow(2));
    }

    @Override
    protected Show getAddItem() {
        return ShowUtils.newShow(null);
    }

    @Override
    protected Show getCopyItem() {
        final Show show = ShowUtils.newShow(1);
        show.setId(null);
        setSeasons(show);

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

    /**
     * Sets seasons to show.
     *
     * @param show show
     * @return show with seasons
     */
    private static Show setSeasons(final Show show) {
        final Integer id = show.getId();
        final Season season = SeasonUtils.newSeason(id);
        if (id == null) {
            season.setPosition(0);
        }
        setEpisodes(season);
        show.setSeasons(CollectionUtils.newList(season));

        return show;
    }

    /**
     * Sets episodes to season.
     *
     * @param season season
     */
    private static void setEpisodes(final Season season) {
        final Integer id = season.getId();
        final Episode episode = EpisodeUtils.newEpisode(id);
        if (id == null) {
            episode.setPosition(0);
        }
        season.setEpisodes(CollectionUtils.newList(episode));
    }

}
