package cz.vhromada.catalog.facade.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import cz.vhromada.catalog.domain.Episode;
import cz.vhromada.catalog.entity.Season;
import cz.vhromada.catalog.entity.Show;
import cz.vhromada.catalog.utils.SeasonUtils;
import cz.vhromada.catalog.utils.ShowUtils;
import cz.vhromada.common.facade.MovableChildFacade;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.test.facade.MovableChildFacadeTest;
import cz.vhromada.common.validator.MovableValidator;
import cz.vhromada.converter.Converter;

import org.junit.jupiter.api.Test;

/**
 * A class represents test for class {@link SeasonFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
class SeasonFacadeImplTest extends MovableChildFacadeTest<Season, cz.vhromada.catalog.domain.Season, Show, cz.vhromada.catalog.domain.Show> {

    /**
     * Test method for {@link SeasonFacadeImpl#SeasonFacadeImpl(MovableService, Converter, MovableValidator, MovableValidator)} with null service for
     * shows.
     */
    @Test
    void constructor_NullShowService() {
        assertThatThrownBy(() -> new SeasonFacadeImpl(null, getConverter(), getParentMovableValidator(), getChildMovableValidator()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link SeasonFacadeImpl#SeasonFacadeImpl(MovableService, Converter, MovableValidator, MovableValidator)} with null converter.
     */
    @Test
    void constructor_NullConverter() {
        assertThatThrownBy(() -> new SeasonFacadeImpl(getMovableService(), null, getParentMovableValidator(), getChildMovableValidator()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link SeasonFacadeImpl#SeasonFacadeImpl(MovableService, Converter, MovableValidator, MovableValidator)} with null validator for show.
     */
    @Test
    void constructor_NullShowValidator() {
        assertThatThrownBy(() -> new SeasonFacadeImpl(getMovableService(), getConverter(), null, getChildMovableValidator()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link SeasonFacadeImpl#SeasonFacadeImpl(MovableService, Converter, MovableValidator, MovableValidator)} with null validator for season.
     */
    @Test
    void constructor_NullSeasonValidator() {
        assertThatThrownBy(() -> new SeasonFacadeImpl(getMovableService(), getConverter(), getParentMovableValidator(), null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Override
    protected MovableChildFacade<Season, Show> getMovableChildFacade() {
        return new SeasonFacadeImpl(getMovableService(), getConverter(), getParentMovableValidator(), getChildMovableValidator());
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
        final cz.vhromada.catalog.domain.Season season = SeasonUtils.newSeasonWithEpisodes(id);
        for (final Episode episode : season.getEpisodes()) {
            episode.setPosition(0);
        }

        return season;
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
