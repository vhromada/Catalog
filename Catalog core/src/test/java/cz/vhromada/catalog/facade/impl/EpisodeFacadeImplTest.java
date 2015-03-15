package cz.vhromada.catalog.facade.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.facade.EpisodeFacade;
import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.EpisodeTO;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.catalog.facade.validators.EpisodeTOValidator;
import cz.vhromada.catalog.facade.validators.SeasonTOValidator;
import cz.vhromada.catalog.service.EpisodeService;
import cz.vhromada.catalog.service.SeasonService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.converters.Converter;
import cz.vhromada.test.DeepAsserts;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

/**
 * A class represents test for class {@link EpisodeFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class EpisodeFacadeImplTest extends ObjectGeneratorTest {

    /**
     * Instance of {@link SeasonService}
     */
    @Mock
    private SeasonService seasonService;

    /**
     * Instance of {@link EpisodeService}
     */
    @Mock
    private EpisodeService episodeService;

    /**
     * Instance of {@link Converter}
     */
    @Mock
    private Converter converter;

    /**
     * Instance of {@link SeasonTOValidator}
     */
    @Mock
    private SeasonTOValidator seasonTOValidator;

    /**
     * Instance of {@link EpisodeTOValidator}
     */
    @Mock
    private EpisodeTOValidator episodeTOValidator;

    /**
     * Instance of (@link EpisodeFacade}
     */
    private EpisodeFacade episodeFacade;

    /**
     * Initializes facade for episodes.
     */
    @Before
    public void setUp() {
        episodeFacade = new EpisodeFacadeImpl(seasonService, episodeService, converter, seasonTOValidator, episodeTOValidator);
    }

    /**
     * Test method for {@link EpisodeFacadeImpl#EpisodeFacadeImpl(SeasonService, EpisodeService, Converter, SeasonTOValidator, EpisodeTOValidator)}
     * with null service for seasons.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullSeasonService() {
        new EpisodeFacadeImpl(null, episodeService, converter, seasonTOValidator, episodeTOValidator);
    }

    /**
     * Test method for {@link EpisodeFacadeImpl#EpisodeFacadeImpl(SeasonService, EpisodeService, Converter, SeasonTOValidator, EpisodeTOValidator)}
     * with null service for episodes.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullEpisodeService() {
        new EpisodeFacadeImpl(seasonService, null, converter, seasonTOValidator, episodeTOValidator);
    }

    /**
     * Test method for {@link EpisodeFacadeImpl#EpisodeFacadeImpl(SeasonService, EpisodeService, Converter, SeasonTOValidator, EpisodeTOValidator)}
     * with null converter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullConverter() {
        new EpisodeFacadeImpl(seasonService, episodeService, null, seasonTOValidator, episodeTOValidator);
    }

    /**
     * Test method for {@link EpisodeFacadeImpl#EpisodeFacadeImpl(SeasonService, EpisodeService, Converter, SeasonTOValidator, EpisodeTOValidator)}
     * with null validator for TO for season.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullSeasonTOValidator() {
        new EpisodeFacadeImpl(seasonService, episodeService, converter, null, episodeTOValidator);
    }

    /**
     * Test method for {@link EpisodeFacadeImpl#EpisodeFacadeImpl(SeasonService, EpisodeService, Converter, SeasonTOValidator, EpisodeTOValidator)}
     * with null validator for TO for episode.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullEpisodeTOValidator() {
        new EpisodeFacadeImpl(seasonService, episodeService, converter, seasonTOValidator, null);
    }

    /**
     * Test method for {@link EpisodeFacade#getEpisode(Integer)} with existing episode.
     */
    @Test
    public void testGetEpisodeWithExistingEpisode() {
        final Episode episode = generate(Episode.class);
        final EpisodeTO episodeTO = generate(EpisodeTO.class);
        when(episodeService.getEpisode(anyInt())).thenReturn(episode);
        when(converter.convert(any(Episode.class), eq(EpisodeTO.class))).thenReturn(episodeTO);

        DeepAsserts.assertEquals(episodeTO, episodeFacade.getEpisode(episodeTO.getId()));

        verify(episodeService).getEpisode(episodeTO.getId());
        verify(converter).convert(episode, EpisodeTO.class);
        verifyNoMoreInteractions(episodeService, converter);
    }

    /**
     * Test method for {@link EpisodeFacade#getEpisode(Integer)} with not existing episode.
     */
    @Test
    public void testGetEpisodeWithNotExistingEpisode() {
        when(episodeService.getEpisode(anyInt())).thenReturn(null);
        when(converter.convert(any(Episode.class), eq(EpisodeTO.class))).thenReturn(null);

        assertNull(episodeFacade.getEpisode(Integer.MAX_VALUE));

        verify(episodeService).getEpisode(Integer.MAX_VALUE);
        verify(converter).convert(null, EpisodeTO.class);
        verifyNoMoreInteractions(episodeService, converter);
    }

    /**
     * Test method for {@link EpisodeFacade#getEpisode(Integer)} with null argument.
     */
    @Test
    public void testGetEpisodeWithNullArgument() {
        try {
            episodeFacade.getEpisode(null);
            fail("Can't get episode with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(episodeService, converter);
    }

    /**
     * Test method for {@link EpisodeFacade#getEpisode(Integer)} with exception in service tier.
     */
    @Test
    public void testGetEpisodeWithServiceTierException() {
        doThrow(ServiceOperationException.class).when(episodeService).getEpisode(anyInt());

        try {
            episodeFacade.getEpisode(Integer.MAX_VALUE);
            fail("Can't get episode with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(episodeService).getEpisode(Integer.MAX_VALUE);
        verifyNoMoreInteractions(episodeService);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link EpisodeFacade#add(EpisodeTO)}.
     */
    @Test
    public void testAdd() {
        final Episode episode = generate(Episode.class);
        episode.setId(null);
        final EpisodeTO episodeTO = generate(EpisodeTO.class);
        episodeTO.setId(null);
        final int id = generate(Integer.class);
        final int position = generate(Integer.class);
        when(seasonService.getSeason(anyInt())).thenReturn(generate(Season.class));
        doAnswer(setEpisodeIdAndPosition(id, position)).when(episodeService).add(any(Episode.class));
        when(converter.convert(any(EpisodeTO.class), eq(Episode.class))).thenReturn(episode);

        episodeFacade.add(episodeTO);

        DeepAsserts.assertEquals(id, episode.getId());
        DeepAsserts.assertEquals(position, episode.getPosition());

        verify(seasonService).getSeason(episodeTO.getSeason().getId());
        verify(episodeService).add(episode);
        verify(episodeTOValidator).validateNewEpisodeTO(episodeTO);
        verify(converter).convert(episodeTO, Episode.class);
        verifyNoMoreInteractions(seasonService, episodeService, converter, episodeTOValidator);
    }

    /**
     * Test method for {@link EpisodeFacade#add(EpisodeTO)} with null argument.
     */
    @Test
    public void testAddWithNullArgument() {
        doThrow(IllegalArgumentException.class).when(episodeTOValidator).validateNewEpisodeTO(any(EpisodeTO.class));

        try {
            episodeFacade.add(null);
            fail("Can't add episode with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verify(episodeTOValidator).validateNewEpisodeTO(null);
        verifyNoMoreInteractions(episodeTOValidator);
        verifyZeroInteractions(seasonService, episodeService, converter);
    }

    /**
     * Test method for {@link EpisodeFacade#add(EpisodeTO)} with argument with bad data.
     */
    @Test
    public void testAddWithBadArgument() {
        final EpisodeTO episode = generate(EpisodeTO.class);
        episode.setId(null);
        doThrow(ValidationException.class).when(episodeTOValidator).validateNewEpisodeTO(any(EpisodeTO.class));

        try {
            episodeFacade.add(episode);
            fail("Can't add episode with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(episodeTOValidator).validateNewEpisodeTO(episode);
        verifyNoMoreInteractions(episodeTOValidator);
        verifyZeroInteractions(seasonService, episodeService, converter);
    }

    /**
     * Test method for {@link EpisodeFacade#add(EpisodeTO)} with not existing argument.
     */
    @Test
    public void testAddWithNotExistingArgument() {
        final EpisodeTO episode = generate(EpisodeTO.class);
        episode.setId(null);
        when(seasonService.getSeason(anyInt())).thenReturn(null);

        try {
            episodeFacade.add(episode);
            fail("Can't add episode with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(seasonService).getSeason(episode.getSeason().getId());
        verify(episodeTOValidator).validateNewEpisodeTO(episode);
        verifyNoMoreInteractions(seasonService, episodeTOValidator);
        verifyZeroInteractions(episodeService, converter);
    }

    /**
     * Test method for {@link EpisodeFacade#add(EpisodeTO)} with service tier not setting ID.
     */
    @Test
    public void testAddWithNotServiceTierSettingID() {
        final Episode episode = generate(Episode.class);
        episode.setId(null);
        final EpisodeTO episodeTO = generate(EpisodeTO.class);
        episodeTO.setId(null);
        when(seasonService.getSeason(anyInt())).thenReturn(generate(Season.class));
        when(converter.convert(any(EpisodeTO.class), eq(Episode.class))).thenReturn(episode);

        try {
            episodeFacade.add(episodeTO);
            fail("Can't add episode with service tier not setting ID.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(seasonService).getSeason(episodeTO.getSeason().getId());
        verify(episodeService).add(episode);
        verify(converter).convert(episodeTO, Episode.class);
        verify(episodeTOValidator).validateNewEpisodeTO(episodeTO);
        verifyNoMoreInteractions(seasonService, episodeService, converter, episodeTOValidator);
    }

    /**
     * Test method for {@link EpisodeFacade#add(EpisodeTO)} with exception in service tier.
     */
    @Test
    public void testAddWithServiceTierException() {
        final EpisodeTO episode = generate(EpisodeTO.class);
        episode.setId(null);
        doThrow(ServiceOperationException.class).when(seasonService).getSeason(anyInt());

        try {
            episodeFacade.add(episode);
            fail("Can't add episode with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(seasonService).getSeason(episode.getSeason().getId());
        verify(episodeTOValidator).validateNewEpisodeTO(episode);
        verifyNoMoreInteractions(seasonService, episodeTOValidator);
        verifyZeroInteractions(episodeService, converter);
    }

    /**
     * Test method for {@link EpisodeFacade#update(EpisodeTO)}.
     */
    @Test
    public void testUpdate() {
        final Episode episode = generate(Episode.class);
        final EpisodeTO episodeTO = generate(EpisodeTO.class);
        when(seasonService.getSeason(anyInt())).thenReturn(generate(Season.class));
        when(episodeService.exists(any(Episode.class))).thenReturn(true);
        when(converter.convert(any(EpisodeTO.class), eq(Episode.class))).thenReturn(episode);

        episodeFacade.update(episodeTO);

        verify(seasonService).getSeason(episodeTO.getSeason().getId());
        verify(episodeService).exists(episode);
        verify(episodeService).update(episode);
        verify(converter).convert(episodeTO, Episode.class);
        verify(episodeTOValidator).validateExistingEpisodeTO(episodeTO);
        verifyNoMoreInteractions(seasonService, episodeService, converter, episodeTOValidator);
    }

    /**
     * Test method for {@link EpisodeFacade#update(EpisodeTO)} with null argument.
     */
    @Test
    public void testUpdateWithNullArgument() {
        doThrow(IllegalArgumentException.class).when(episodeTOValidator).validateExistingEpisodeTO(any(EpisodeTO.class));

        try {
            episodeFacade.update(null);
            fail("Can't update episode with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verify(episodeTOValidator).validateExistingEpisodeTO(null);
        verifyNoMoreInteractions(episodeTOValidator);
        verifyZeroInteractions(seasonService, episodeService, converter);
    }

    /**
     * Test method for {@link EpisodeFacade#update(EpisodeTO)} with argument with bad data.
     */
    @Test
    public void testUpdateWithBadArgument() {
        final EpisodeTO episode = generate(EpisodeTO.class);
        doThrow(ValidationException.class).when(episodeTOValidator).validateExistingEpisodeTO(any(EpisodeTO.class));

        try {
            episodeFacade.update(episode);
            fail("Can't update episode with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(episodeTOValidator).validateExistingEpisodeTO(episode);
        verifyNoMoreInteractions(episodeTOValidator);
        verifyZeroInteractions(seasonService, episodeService, converter);
    }

    /**
     * Test method for {@link EpisodeFacade#update(EpisodeTO)} with not existing argument.
     */
    @Test
    public void testUpdateWithNotExistingArgument() {
        final Episode episode = generate(Episode.class);
        final EpisodeTO episodeTO = generate(EpisodeTO.class);
        when(episodeService.exists(any(Episode.class))).thenReturn(false);
        when(converter.convert(any(EpisodeTO.class), eq(Episode.class))).thenReturn(episode);

        try {
            episodeFacade.update(episodeTO);
            fail("Can't update episode with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(episodeService).exists(episode);
        verify(converter).convert(episodeTO, Episode.class);
        verify(episodeTOValidator).validateExistingEpisodeTO(episodeTO);
        verifyNoMoreInteractions(episodeService, converter, episodeTOValidator);
        verifyZeroInteractions(seasonService);
    }

    /**
     * Test method for {@link EpisodeFacade#update(EpisodeTO)} with exception in service tier.
     */
    @Test
    public void testUpdateWithServiceTierException() {
        final Episode episode = generate(Episode.class);
        final EpisodeTO episodeTO = generate(EpisodeTO.class);
        doThrow(ServiceOperationException.class).when(episodeService).exists(any(Episode.class));
        when(converter.convert(any(EpisodeTO.class), eq(Episode.class))).thenReturn(episode);

        try {
            episodeFacade.update(episodeTO);
            fail("Can't update episode with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(episodeService).exists(episode);
        verify(converter).convert(episodeTO, Episode.class);
        verify(episodeTOValidator).validateExistingEpisodeTO(episodeTO);
        verifyNoMoreInteractions(episodeService, converter, episodeTOValidator);
        verifyZeroInteractions(seasonService);
    }

    /**
     * Test method for {@link EpisodeFacade#remove(EpisodeTO)}.
     */
    @Test
    public void testRemove() {
        final Episode episode = generate(Episode.class);
        final EpisodeTO episodeTO = generate(EpisodeTO.class);
        when(episodeService.getEpisode(anyInt())).thenReturn(episode);

        episodeFacade.remove(episodeTO);

        verify(episodeService).getEpisode(episodeTO.getId());
        verify(episodeService).remove(episode);
        verify(episodeTOValidator).validateEpisodeTOWithId(episodeTO);
        verifyNoMoreInteractions(episodeService, episodeTOValidator);
    }

    /**
     * Test method for {@link EpisodeFacade#remove(EpisodeTO)} with null argument.
     */
    @Test
    public void testRemoveWithNullArgument() {
        doThrow(IllegalArgumentException.class).when(episodeTOValidator).validateEpisodeTOWithId(any(EpisodeTO.class));

        try {
            episodeFacade.remove(null);
            fail("Can't remove episode with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verify(episodeTOValidator).validateEpisodeTOWithId(null);
        verifyNoMoreInteractions(episodeTOValidator);
        verifyZeroInteractions(episodeService);
    }

    /**
     * Test method for {@link EpisodeFacade#remove(EpisodeTO)} with argument with bad data.
     */
    @Test
    public void testRemoveWithBadArgument() {
        final EpisodeTO episode = generate(EpisodeTO.class);
        doThrow(ValidationException.class).when(episodeTOValidator).validateEpisodeTOWithId(any(EpisodeTO.class));

        try {
            episodeFacade.remove(episode);
            fail("Can't remove episode with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(episodeTOValidator).validateEpisodeTOWithId(episode);
        verifyNoMoreInteractions(episodeTOValidator);
        verifyZeroInteractions(episodeService);
    }

    /**
     * Test method for {@link EpisodeFacade#remove(EpisodeTO)} with not existing argument.
     */
    @Test
    public void testRemoveWithNotExistingArgument() {
        final EpisodeTO episode = generate(EpisodeTO.class);
        when(episodeService.getEpisode(anyInt())).thenReturn(null);

        try {
            episodeFacade.remove(episode);
            fail("Can't remove episode with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(episodeService).getEpisode(episode.getId());
        verify(episodeTOValidator).validateEpisodeTOWithId(episode);
        verifyNoMoreInteractions(episodeService, episodeTOValidator);
    }

    /**
     * Test method for {@link EpisodeFacade#remove(EpisodeTO)} with exception in service tier.
     */
    @Test
    public void testRemoveWithServiceTierException() {
        final EpisodeTO episode = generate(EpisodeTO.class);
        doThrow(ServiceOperationException.class).when(episodeService).getEpisode(anyInt());

        try {
            episodeFacade.remove(episode);
            fail("Can't remove episode with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(episodeService).getEpisode(episode.getId());
        verify(episodeTOValidator).validateEpisodeTOWithId(episode);
        verifyNoMoreInteractions(episodeService, episodeTOValidator);
    }

    /**
     * Test method for {@link EpisodeFacade#duplicate(EpisodeTO)}.
     */
    @Test
    public void testDuplicate() {
        final Episode episode = generate(Episode.class);
        final EpisodeTO episodeTO = generate(EpisodeTO.class);
        when(episodeService.getEpisode(anyInt())).thenReturn(episode);

        episodeFacade.duplicate(episodeTO);

        verify(episodeService).getEpisode(episodeTO.getId());
        verify(episodeService).duplicate(episode);
        verify(episodeTOValidator).validateEpisodeTOWithId(episodeTO);
        verifyNoMoreInteractions(episodeService, episodeTOValidator);
    }

    /**
     * Test method for {@link EpisodeFacade#duplicate(EpisodeTO)} with null argument.
     */
    @Test
    public void testDuplicateWithNullArgument() {
        doThrow(IllegalArgumentException.class).when(episodeTOValidator).validateEpisodeTOWithId(any(EpisodeTO.class));

        try {
            episodeFacade.duplicate(null);
            fail("Can't duplicate episode with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verify(episodeTOValidator).validateEpisodeTOWithId(null);
        verifyNoMoreInteractions(episodeTOValidator);
        verifyZeroInteractions(episodeService);
    }

    /**
     * Test method for {@link EpisodeFacade#duplicate(EpisodeTO)} with argument with bad data.
     */
    @Test
    public void testDuplicateWithBadArgument() {
        final EpisodeTO episode = generate(EpisodeTO.class);
        doThrow(ValidationException.class).when(episodeTOValidator).validateEpisodeTOWithId(any(EpisodeTO.class));

        try {
            episodeFacade.duplicate(episode);
            fail("Can't duplicate episode with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(episodeTOValidator).validateEpisodeTOWithId(episode);
        verifyNoMoreInteractions(episodeTOValidator);
        verifyZeroInteractions(episodeService);
    }

    /**
     * Test method for {@link EpisodeFacade#duplicate(EpisodeTO)} with not existing argument.
     */
    @Test
    public void testDuplicateWithNotExistingArgument() {
        final EpisodeTO episode = generate(EpisodeTO.class);
        when(episodeService.getEpisode(anyInt())).thenReturn(null);

        try {
            episodeFacade.duplicate(episode);
            fail("Can't duplicate episode with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(episodeService).getEpisode(episode.getId());
        verify(episodeTOValidator).validateEpisodeTOWithId(episode);
        verifyNoMoreInteractions(episodeService, episodeTOValidator);
    }

    /**
     * Test method for {@link EpisodeFacade#duplicate(EpisodeTO)} with exception in service tier.
     */
    @Test
    public void testDuplicateWithServiceTierException() {
        final EpisodeTO episode = generate(EpisodeTO.class);
        doThrow(ServiceOperationException.class).when(episodeService).getEpisode(anyInt());

        try {
            episodeFacade.duplicate(episode);
            fail("Can't duplicate episode with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(episodeService).getEpisode(episode.getId());
        verify(episodeTOValidator).validateEpisodeTOWithId(episode);
        verifyNoMoreInteractions(episodeService, episodeTOValidator);
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(EpisodeTO)}.
     */
    @Test
    public void testMoveUp() {
        final Episode episode = generate(Episode.class);
        final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), episode);
        final EpisodeTO episodeTO = generate(EpisodeTO.class);
        when(episodeService.getEpisode(anyInt())).thenReturn(episode);
        when(episodeService.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);

        episodeFacade.moveUp(episodeTO);

        verify(episodeService).getEpisode(episodeTO.getId());
        verify(episodeService).findEpisodesBySeason(episode.getSeason());
        verify(episodeService).moveUp(episode);
        verify(episodeTOValidator).validateEpisodeTOWithId(episodeTO);
        verifyNoMoreInteractions(episodeService, episodeTOValidator);
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(EpisodeTO)} with null argument.
     */
    @Test
    public void testMoveUpWithNullArgument() {
        doThrow(IllegalArgumentException.class).when(episodeTOValidator).validateEpisodeTOWithId(any(EpisodeTO.class));

        try {
            episodeFacade.moveUp(null);
            fail("Can't move up episode with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verify(episodeTOValidator).validateEpisodeTOWithId(null);
        verifyNoMoreInteractions(episodeTOValidator);
        verifyZeroInteractions(episodeService);
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(EpisodeTO)} with argument with bad data.
     */
    @Test
    public void testMoveUpWithBadArgument() {
        final EpisodeTO episode = generate(EpisodeTO.class);
        doThrow(ValidationException.class).when(episodeTOValidator).validateEpisodeTOWithId(any(EpisodeTO.class));

        try {
            episodeFacade.moveUp(episode);
            fail("Can't move up episode with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(episodeTOValidator).validateEpisodeTOWithId(episode);
        verifyNoMoreInteractions(episodeTOValidator);
        verifyZeroInteractions(episodeService);
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(EpisodeTO)} with not existing argument.
     */
    @Test
    public void testMoveUpWithNotExistingArgument() {
        final EpisodeTO episode = generate(EpisodeTO.class);
        when(episodeService.getEpisode(anyInt())).thenReturn(null);

        try {
            episodeFacade.moveUp(episode);
            fail("Can't move up episode with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(episodeService).getEpisode(episode.getId());
        verify(episodeTOValidator).validateEpisodeTOWithId(episode);
        verifyNoMoreInteractions(episodeService, episodeTOValidator);
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(EpisodeTO)} with not moveable argument.
     */
    @Test
    public void testMoveUpWithNotMoveableArgument() {
        final Episode episode = generate(Episode.class);
        final List<Episode> episodes = CollectionUtils.newList(episode, mock(Episode.class));
        final EpisodeTO episodeTO = generate(EpisodeTO.class);
        when(episodeService.getEpisode(anyInt())).thenReturn(episode);
        when(episodeService.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);

        try {
            episodeFacade.moveUp(episodeTO);
            fail("Can't move up episode with not thrown ValidationException for not moveable argument.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(episodeService).getEpisode(episodeTO.getId());
        verify(episodeService).findEpisodesBySeason(episode.getSeason());
        verify(episodeTOValidator).validateEpisodeTOWithId(episodeTO);
        verifyNoMoreInteractions(episodeService, episodeTOValidator);
    }

    /**
     * Test method for {@link EpisodeFacade#moveUp(EpisodeTO)} with exception in service tier.
     */
    @Test
    public void testMoveUpWithServiceTierException() {
        final EpisodeTO episode = generate(EpisodeTO.class);
        doThrow(ServiceOperationException.class).when(episodeService).getEpisode(anyInt());

        try {
            episodeFacade.moveUp(episode);
            fail("Can't move up episode with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(episodeService).getEpisode(episode.getId());
        verify(episodeTOValidator).validateEpisodeTOWithId(episode);
        verifyNoMoreInteractions(episodeService, episodeTOValidator);
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(EpisodeTO)}.
     */
    @Test
    public void testMoveDown() {
        final Episode episode = generate(Episode.class);
        final List<Episode> episodes = CollectionUtils.newList(episode, mock(Episode.class));
        final EpisodeTO episodeTO = generate(EpisodeTO.class);
        when(episodeService.getEpisode(anyInt())).thenReturn(episode);
        when(episodeService.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);

        episodeFacade.moveDown(episodeTO);

        verify(episodeService).getEpisode(episodeTO.getId());
        verify(episodeService).findEpisodesBySeason(episode.getSeason());
        verify(episodeService).moveDown(episode);
        verify(episodeTOValidator).validateEpisodeTOWithId(episodeTO);
        verifyNoMoreInteractions(episodeService, episodeTOValidator);
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(EpisodeTO)} with null argument.
     */
    @Test
    public void testMoveDownWithNullArgument() {
        doThrow(IllegalArgumentException.class).when(episodeTOValidator).validateEpisodeTOWithId(any(EpisodeTO.class));

        try {
            episodeFacade.moveDown(null);
            fail("Can't move down episode with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verify(episodeTOValidator).validateEpisodeTOWithId(null);
        verifyNoMoreInteractions(episodeTOValidator);
        verifyZeroInteractions(episodeService);
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(EpisodeTO)} with argument with bad data.
     */
    @Test
    public void testMoveDownWithBadArgument() {
        final EpisodeTO episode = generate(EpisodeTO.class);
        doThrow(ValidationException.class).when(episodeTOValidator).validateEpisodeTOWithId(any(EpisodeTO.class));

        try {
            episodeFacade.moveDown(episode);
            fail("Can't move down episode with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(episodeTOValidator).validateEpisodeTOWithId(episode);
        verifyNoMoreInteractions(episodeTOValidator);
        verifyZeroInteractions(episodeService);
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(EpisodeTO)} with not existing argument.
     */
    @Test
    public void testMoveDownWithNotExistingArgument() {
        final EpisodeTO episode = generate(EpisodeTO.class);
        when(episodeService.getEpisode(anyInt())).thenReturn(null);

        try {
            episodeFacade.moveDown(episode);
            fail("Can't move down episode with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(episodeService).getEpisode(episode.getId());
        verify(episodeTOValidator).validateEpisodeTOWithId(episode);
        verifyNoMoreInteractions(episodeService, episodeTOValidator);
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(EpisodeTO)} with not moveable argument.
     */
    @Test
    public void testMoveDownWithNotMoveableArgument() {
        final Episode episode = generate(Episode.class);
        final List<Episode> episodes = CollectionUtils.newList(mock(Episode.class), episode);
        final EpisodeTO episodeTO = generate(EpisodeTO.class);
        when(episodeService.getEpisode(anyInt())).thenReturn(episode);
        when(episodeService.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);

        try {
            episodeFacade.moveDown(episodeTO);
            fail("Can't move down episode with not thrown ValidationException for not moveable argument.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(episodeService).getEpisode(episodeTO.getId());
        verify(episodeService).findEpisodesBySeason(episode.getSeason());
        verify(episodeTOValidator).validateEpisodeTOWithId(episodeTO);
        verifyNoMoreInteractions(episodeService, episodeTOValidator);
    }

    /**
     * Test method for {@link EpisodeFacade#moveDown(EpisodeTO)} with exception in service tier.
     */
    @Test
    public void testMoveDownWithServiceTierException() {
        final EpisodeTO episode = generate(EpisodeTO.class);
        doThrow(ServiceOperationException.class).when(episodeService).getEpisode(anyInt());

        try {
            episodeFacade.moveDown(episode);
            fail("Can't move down episode with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(episodeService).getEpisode(episode.getId());
        verify(episodeTOValidator).validateEpisodeTOWithId(episode);
        verifyNoMoreInteractions(episodeService, episodeTOValidator);
    }

    /**
     * Test method for {@link EpisodeFacade#exists(EpisodeTO)} with existing episode.
     */
    @Test
    public void testExistsWithExistingEpisode() {
        final Episode episode = generate(Episode.class);
        final EpisodeTO episodeTO = generate(EpisodeTO.class);
        when(episodeService.exists(any(Episode.class))).thenReturn(true);
        when(converter.convert(any(EpisodeTO.class), eq(Episode.class))).thenReturn(episode);

        assertTrue(episodeFacade.exists(episodeTO));

        verify(episodeService).exists(episode);
        verify(converter).convert(episodeTO, Episode.class);
        verify(episodeTOValidator).validateEpisodeTOWithId(episodeTO);
        verifyNoMoreInteractions(episodeService, converter, episodeTOValidator);
    }

    /**
     * Test method for {@link EpisodeFacade#exists(EpisodeTO)} with not existing episode.
     */
    @Test
    public void testExistsWithNotExistingEpisode() {
        final Episode episode = generate(Episode.class);
        final EpisodeTO episodeTO = generate(EpisodeTO.class);
        when(episodeService.exists(any(Episode.class))).thenReturn(false);
        when(converter.convert(any(EpisodeTO.class), eq(Episode.class))).thenReturn(episode);

        assertFalse(episodeFacade.exists(episodeTO));

        verify(episodeService).exists(episode);
        verify(converter).convert(episodeTO, Episode.class);
        verify(episodeTOValidator).validateEpisodeTOWithId(episodeTO);
        verifyNoMoreInteractions(episodeService, converter, episodeTOValidator);
    }

    /**
     * Test method for {@link EpisodeFacade#exists(EpisodeTO)} with null argument.
     */
    @Test
    public void testExistsWithNullArgument() {
        doThrow(IllegalArgumentException.class).when(episodeTOValidator).validateEpisodeTOWithId(any(EpisodeTO.class));

        try {
            episodeFacade.exists(null);
            fail("Can't exists episode with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verify(episodeTOValidator).validateEpisodeTOWithId(null);
        verifyNoMoreInteractions(episodeTOValidator);
        verifyZeroInteractions(episodeService, converter);
    }

    /**
     * Test method for {@link EpisodeFacade#exists(EpisodeTO)} with argument with bad data.
     */
    @Test
    public void testExistsWithBadArgument() {
        final EpisodeTO episode = generate(EpisodeTO.class);
        doThrow(ValidationException.class).when(episodeTOValidator).validateEpisodeTOWithId(any(EpisodeTO.class));

        try {
            episodeFacade.exists(episode);
            fail("Can't exists episode with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(episodeTOValidator).validateEpisodeTOWithId(episode);
        verifyNoMoreInteractions(episodeTOValidator);
        verifyZeroInteractions(episodeService, converter);
    }

    /**
     * Test method for {@link EpisodeFacade#exists(EpisodeTO)} with exception in service tier.
     */
    @Test
    public void testExistsWithServiceTierException() {
        final Episode episode = generate(Episode.class);
        final EpisodeTO episodeTO = generate(EpisodeTO.class);
        doThrow(ServiceOperationException.class).when(episodeService).exists(any(Episode.class));
        when(converter.convert(any(EpisodeTO.class), eq(Episode.class))).thenReturn(episode);

        try {
            episodeFacade.exists(episodeTO);
            fail("Can't exists episode with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(episodeService).exists(episode);
        verify(converter).convert(episodeTO, Episode.class);
        verify(episodeTOValidator).validateEpisodeTOWithId(episodeTO);
        verifyNoMoreInteractions(episodeService, converter, episodeTOValidator);
    }

    /**
     * Test method for {@link EpisodeFacade#findEpisodesBySeason(SeasonTO)}.
     */
    @Test
    public void testFindEpisodesBySeason() {
        final Season season = generate(Season.class);
        final List<Episode> episodes = CollectionUtils.newList(generate(Episode.class), generate(Episode.class));
        final SeasonTO seasonTO = generate(SeasonTO.class);
        final List<EpisodeTO> episodesList = CollectionUtils.newList(generate(EpisodeTO.class), generate(EpisodeTO.class));
        when(seasonService.getSeason(anyInt())).thenReturn(season);
        when(episodeService.findEpisodesBySeason(any(Season.class))).thenReturn(episodes);
        when(converter.convertCollection(episodes, EpisodeTO.class)).thenReturn(episodesList);

        DeepAsserts.assertEquals(episodesList, episodeFacade.findEpisodesBySeason(seasonTO));

        verify(seasonService).getSeason(seasonTO.getId());
        verify(episodeService).findEpisodesBySeason(season);
        verify(converter).convertCollection(episodes, EpisodeTO.class);
        verify(seasonTOValidator).validateSeasonTOWithId(seasonTO);
        verifyNoMoreInteractions(seasonService, episodeService, converter, seasonTOValidator);
    }

    /**
     * Test method for {@link EpisodeFacade#findEpisodesBySeason(SeasonTO)} with null argument.
     */
    @Test
    public void testFindEpisodesBySeasonWithNullArgument() {
        doThrow(IllegalArgumentException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));

        try {
            episodeFacade.findEpisodesBySeason(null);
            fail("Can't find episodes by season with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verify(seasonTOValidator).validateSeasonTOWithId(null);
        verifyNoMoreInteractions(seasonTOValidator);
        verifyZeroInteractions(seasonService, episodeService, converter);
    }

    /**
     * Test method for {@link EpisodeFacade#findEpisodesBySeason(SeasonTO)} with argument with bad data.
     */
    @Test
    public void testFindEpisodesBySeasonWithBadArgument() {
        final SeasonTO season = generate(SeasonTO.class);
        doThrow(ValidationException.class).when(seasonTOValidator).validateSeasonTOWithId(any(SeasonTO.class));

        try {
            episodeFacade.findEpisodesBySeason(season);
            fail("Can't find episodes by season with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(seasonTOValidator).validateSeasonTOWithId(season);
        verifyNoMoreInteractions(seasonTOValidator);
        verifyZeroInteractions(seasonService, episodeService, converter);
    }

    /**
     * Test method for {@link EpisodeFacade#findEpisodesBySeason(SeasonTO)} with not existing argument.
     */
    @Test
    public void testFindEpisodesBySeasonWithNotExistingArgument() {
        final SeasonTO season = generate(SeasonTO.class);
        when(seasonService.getSeason(anyInt())).thenReturn(null);

        try {
            episodeFacade.findEpisodesBySeason(season);
            fail("Can't find episodes by season with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(seasonService).getSeason(season.getId());
        verify(seasonTOValidator).validateSeasonTOWithId(season);
        verifyNoMoreInteractions(seasonService, seasonTOValidator);
        verifyZeroInteractions(episodeService, converter);
    }

    /**
     * Test method for {@link EpisodeFacade#findEpisodesBySeason(SeasonTO)} with exception in service tier.
     */
    @Test
    public void testFindEpisodesBySeasonWithServiceTierException() {
        final SeasonTO season = generate(SeasonTO.class);
        doThrow(ServiceOperationException.class).when(seasonService).getSeason(anyInt());

        try {
            episodeFacade.findEpisodesBySeason(season);
            fail("Can't find episodes by season with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(seasonService).getSeason(season.getId());
        verify(seasonTOValidator).validateSeasonTOWithId(season);
        verifyNoMoreInteractions(seasonService, seasonTOValidator);
        verifyZeroInteractions(episodeService, converter);
    }

    /**
     * Sets episode's ID and position.
     *
     * @param id       ID
     * @param position position
     * @return mocked answer
     */
    private static Answer<Void> setEpisodeIdAndPosition(final Integer id, final int position) {
        return new Answer<Void>() {

            @Override
            public Void answer(final InvocationOnMock invocation) {
                final Episode episode = (Episode) invocation.getArguments()[0];
                episode.setId(id);
                episode.setPosition(position);
                return null;
            }

        };
    }

}
