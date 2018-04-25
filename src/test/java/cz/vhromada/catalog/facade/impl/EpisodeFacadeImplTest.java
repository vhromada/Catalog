package cz.vhromada.catalog.facade.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import cz.vhromada.catalog.domain.Show;
import cz.vhromada.catalog.entity.Episode;
import cz.vhromada.catalog.entity.Season;
import cz.vhromada.catalog.utils.EpisodeUtils;
import cz.vhromada.catalog.utils.SeasonUtils;
import cz.vhromada.catalog.utils.ShowUtils;
import cz.vhromada.common.facade.MovableChildFacade;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.test.facade.MovableChildFacadeTest;
import cz.vhromada.common.utils.CollectionUtils;
import cz.vhromada.common.validator.MovableValidator;
import cz.vhromada.converter.Converter;

import org.junit.jupiter.api.Test;

/**
 * A class represents test for class {@link EpisodeFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
class EpisodeFacadeImplTest extends MovableChildFacadeTest<Episode, cz.vhromada.catalog.domain.Episode, Season, Show> {

    /**
     * Test method for {@link EpisodeFacadeImpl#EpisodeFacadeImpl(MovableService, Converter, MovableValidator, MovableValidator)} with null service for shows.
     */
    @Test
    void constructor_NullShowService() {
        assertThatThrownBy(() -> new EpisodeFacadeImpl(null, getConverter(), getParentMovableValidator(), getChildMovableValidator()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link EpisodeFacadeImpl#EpisodeFacadeImpl(MovableService, Converter, MovableValidator, MovableValidator)} with null converter.
     */
    @Test
    void constructor_NullConverter() {
        assertThatThrownBy(() -> new EpisodeFacadeImpl(getMovableService(), null, getParentMovableValidator(), getChildMovableValidator()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link EpisodeFacadeImpl#EpisodeFacadeImpl(MovableService, Converter, MovableValidator, MovableValidator)} with null validator for
     * season.
     */
    @Test
    void constructor_NullSeasonValidator() {
        assertThatThrownBy(() -> new EpisodeFacadeImpl(getMovableService(), getConverter(), null, getChildMovableValidator()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link EpisodeFacadeImpl#EpisodeFacadeImpl(MovableService, Converter, MovableValidator, MovableValidator)} with null validator for
     * episode.
     */
    @Test
    void constructor_NullEpisodeValidator() {
        assertThatThrownBy(() -> new EpisodeFacadeImpl(getMovableService(), getConverter(), getParentMovableValidator(), null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Override
    protected boolean isFirstChild() {
        return false;
    }

    @Override
    protected MovableChildFacade<Episode, Season> getMovableChildFacade() {
        return new EpisodeFacadeImpl(getMovableService(), getConverter(), getParentMovableValidator(), getChildMovableValidator());
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
