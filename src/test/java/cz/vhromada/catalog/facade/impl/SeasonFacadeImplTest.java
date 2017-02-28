package cz.vhromada.catalog.facade.impl;

import java.util.List;

import cz.vhromada.catalog.entity.Season;
import cz.vhromada.catalog.entity.Show;
import cz.vhromada.catalog.facade.CatalogChildFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.SeasonUtils;
import cz.vhromada.catalog.utils.ShowUtils;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.converters.Converter;

import org.junit.Test;

/**
 * A class represents test for class {@link SeasonFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
public class SeasonFacadeImplTest extends AbstractChildFacadeTest<Season, cz.vhromada.catalog.domain.Season, Show, cz.vhromada.catalog.domain.Show> {

    /**
     * Test method for {@link SeasonFacadeImpl#SeasonFacadeImpl(CatalogService, Converter, CatalogValidator, CatalogValidator)} with null service for
     * shows.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullShowService() {
        new SeasonFacadeImpl(null, getConverter(), getParentCatalogValidator(), getChildCatalogValidator());
    }

    /**
     * Test method for {@link SeasonFacadeImpl#SeasonFacadeImpl(CatalogService, Converter, CatalogValidator, CatalogValidator)} with null converter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullConverter() {
        new SeasonFacadeImpl(getCatalogService(), null, getParentCatalogValidator(), getChildCatalogValidator());
    }

    /**
     * Test method for {@link SeasonFacadeImpl#SeasonFacadeImpl(CatalogService, Converter, CatalogValidator, CatalogValidator)} with null validator for show.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullShowValidator() {
        new SeasonFacadeImpl(getCatalogService(), getConverter(), null, getChildCatalogValidator());
    }

    /**
     * Test method for {@link SeasonFacadeImpl#SeasonFacadeImpl(CatalogService, Converter, CatalogValidator, CatalogValidator)} with null validator for season.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullSeasonValidator() {
        new SeasonFacadeImpl(getCatalogService(), getConverter(), getParentCatalogValidator(), null);
    }

    @Override
    protected CatalogChildFacade<Season, Show> getCatalogChildFacade() {
        return new SeasonFacadeImpl(getCatalogService(), getConverter(), getParentCatalogValidator(), getChildCatalogValidator());
    }

    @Override
    protected Show newParentEntity(final Integer id) {
        return ShowUtils.newShow(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Show newParentDomain(final Integer id) {
        return ShowUtils.newShowWithSeasons(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Show newParentDomainWithChildren(final Integer id, final List<cz.vhromada.catalog.domain.Season> children) {
        final cz.vhromada.catalog.domain.Show music = newParentDomain(id);
        music.setSeasons(children);

        return music;
    }

    @Override
    protected Season newChildEntity(final Integer id) {
        return SeasonUtils.newSeason(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Season newChildDomain(final Integer id) {
        return SeasonUtils.newSeasonWithEpisodes(id);
    }

    @Override
    protected Class<Show> getParentEntityClass() {
        return Show.class;
    }

    @Override
    protected Class<cz.vhromada.catalog.domain.Show> getParentDomainClass() {
        return cz.vhromada.catalog.domain.Show.class;
    }

    @Override
    protected Class<Season> getChildEntityClass() {
        return Season.class;
    }

    @Override
    protected Class<cz.vhromada.catalog.domain.Season> getChildDomainClass() {
        return cz.vhromada.catalog.domain.Season.class;
    }

    @Override
    protected void assertParentDeepEquals(final cz.vhromada.catalog.domain.Show expected, final cz.vhromada.catalog.domain.Show actual) {
        ShowUtils.assertShowDeepEquals(expected, actual);
    }

}
