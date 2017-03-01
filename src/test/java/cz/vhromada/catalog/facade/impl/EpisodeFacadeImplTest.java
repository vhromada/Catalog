package cz.vhromada.catalog.facade.impl;

import java.util.List;

import cz.vhromada.catalog.domain.Show;
import cz.vhromada.catalog.entity.Episode;
import cz.vhromada.catalog.entity.Season;
import cz.vhromada.catalog.facade.CatalogChildFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.EpisodeUtils;
import cz.vhromada.catalog.utils.SeasonUtils;
import cz.vhromada.catalog.utils.ShowUtils;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.converters.Converter;

import org.junit.Test;

/**
 * A class represents test for class {@link EpisodeFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
public class EpisodeFacadeImplTest extends AbstractChildFacadeTest<Episode, cz.vhromada.catalog.domain.Episode, Season, Show> {

    /**
     * Test method for {@link EpisodeFacadeImpl#EpisodeFacadeImpl(CatalogService, Converter, CatalogValidator, CatalogValidator)} with null service for shows.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullShowService() {
        new EpisodeFacadeImpl(null, getConverter(), getParentCatalogValidator(), getChildCatalogValidator());
    }

    /**
     * Test method for {@link EpisodeFacadeImpl#EpisodeFacadeImpl(CatalogService, Converter, CatalogValidator, CatalogValidator)} with null converter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullConverter() {
        new EpisodeFacadeImpl(getCatalogService(), null, getParentCatalogValidator(), getChildCatalogValidator());
    }

    /**
     * Test method for {@link EpisodeFacadeImpl#EpisodeFacadeImpl(CatalogService, Converter, CatalogValidator, CatalogValidator)} with null validator for
     * season.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullSeasonValidator() {
        new EpisodeFacadeImpl(getCatalogService(), getConverter(), null, getChildCatalogValidator());
    }

    /**
     * Test method for {@link EpisodeFacadeImpl#EpisodeFacadeImpl(CatalogService, Converter, CatalogValidator, CatalogValidator)} with null validator for
     * episode.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullEpisodeValidator() {
        new EpisodeFacadeImpl(getCatalogService(), getConverter(), getParentCatalogValidator(), null);
    }

    @Override
    protected boolean isFirstChild() {
        return false;
    }

    @Override
    protected CatalogChildFacade<Episode, Season> getCatalogChildFacade() {
        return new EpisodeFacadeImpl(getCatalogService(), getConverter(), getParentCatalogValidator(), getChildCatalogValidator());
    }

    @Override
    protected Season newParentEntity(final Integer id) {
        return SeasonUtils.newSeason(id);
    }

    @Override
    protected Show newParentDomain(final Integer id) {
        return ShowUtils.newShowWithSeasons(id);
    }

    @Override
    protected Show newParentDomainWithChildren(final Integer id, final List<cz.vhromada.catalog.domain.Episode> children) {
        final cz.vhromada.catalog.domain.Season season = SeasonUtils.newSeasonDomain(id);
        season.setEpisodes(children);

        final Show show = ShowUtils.newShowDomain(id);
        show.setSeasons(CollectionUtils.newList(season));

        return show;
    }

    @Override
    protected Episode newChildEntity(final Integer id) {
        return EpisodeUtils.newEpisode(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Episode newChildDomain(final Integer id) {
        return EpisodeUtils.newEpisodeDomain(id);
    }

    @Override
    protected Class<Season> getParentEntityClass() {
        return Season.class;
    }

    @Override
    protected Class<Show> getParentDomainClass() {
        return Show.class;
    }

    @Override
    protected Class<Episode> getChildEntityClass() {
        return Episode.class;
    }

    @Override
    protected Class<cz.vhromada.catalog.domain.Episode> getChildDomainClass() {
        return cz.vhromada.catalog.domain.Episode.class;
    }

    @Override
    protected void assertParentDeepEquals(final Show expected, final Show actual) {
        ShowUtils.assertShowDeepEquals(expected, actual);
    }

}
