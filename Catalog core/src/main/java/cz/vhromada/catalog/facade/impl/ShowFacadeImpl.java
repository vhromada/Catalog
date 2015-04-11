package cz.vhromada.catalog.facade.impl;

import java.util.Collections;
import java.util.List;

import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.entities.Show;
import cz.vhromada.catalog.facade.ShowFacade;
import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.to.ShowTO;
import cz.vhromada.catalog.facade.validators.ShowTOValidator;
import cz.vhromada.catalog.service.GenreService;
import cz.vhromada.catalog.service.ShowService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.converters.Converter;
import cz.vhromada.validators.Validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents implementation of facade for shows.
 *
 * @author Vladimir Hromada
 */
@Component("showFacade")
@Transactional
public class ShowFacadeImpl implements ShowFacade {

    /**
     * Service for shows argument
     */
    private static final String SHOW_SERVICE_ARGUMENT = "Service for shows";

    /**
     * Service for genres argument
     */
    private static final String GENRE_SERVICE_ARGUMENT = "Service for genres";

    /**
     * Converter argument
     */
    private static final String CONVERTER_ARGUMENT = "Converter";

    /**
     * Validator for TO for show argument
     */
    private static final String SHOW_TO_VALIDATOR_ARGUMENT = "Validator for TO for show";

    /**
     * Show argument
     */
    private static final String SHOW_ARGUMENT = "show";

    /**
     * TO for show argument
     */
    private static final String SHOW_TO_ARGUMENT = "TO for show";

    /**
     * TO for genre argument
     */
    private static final String GENRE_TO_ARGUMENT = "TO for genre";

    /**
     * ID argument
     */
    private static final String ID_ARGUMENT = "ID";

    /**
     * Message for {@link FacadeOperationException}
     */
    private static final String FACADE_OPERATION_EXCEPTION_MESSAGE = "Error in working with service tier.";

    /**
     * Message for not setting ID
     */
    private static final String NOT_SET_ID_EXCEPTION_MESSAGE = "Service tier doesn't set ID.";

    /**
     * Service for shows
     */
    private ShowService showService;

    /**
     * Service for genres
     */
    private GenreService genreService;

    /**
     * Converter
     */
    private Converter converter;

    /**
     * Validator for TO for show
     */
    private ShowTOValidator showTOValidator;

    /**
     * Creates a new instance of ShowFacadeImpl.
     *
     * @param showService     service for shows
     * @param genreService    service for genres
     * @param converter       converter
     * @param showTOValidator validator for TO for show
     * @throws IllegalArgumentException if service for shows is null
     *                                  or service for genres is null
     *                                  or converter is null
     *                                  or validator for TO for show is null
     */
    @Autowired
    public ShowFacadeImpl(final ShowService showService,
            final GenreService genreService,
            @Qualifier("catalogDozerConverter") final Converter converter,
            final ShowTOValidator showTOValidator) {
        Validators.validateArgumentNotNull(showService, SHOW_SERVICE_ARGUMENT);
        Validators.validateArgumentNotNull(genreService, GENRE_SERVICE_ARGUMENT);
        Validators.validateArgumentNotNull(converter, CONVERTER_ARGUMENT);
        Validators.validateArgumentNotNull(showTOValidator, SHOW_TO_VALIDATOR_ARGUMENT);

        this.showService = showService;
        this.genreService = genreService;
        this.converter = converter;
        this.showTOValidator = showTOValidator;
    }

    /**
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void newData() {
        try {
            showService.newData();
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<ShowTO> getShows() {
        try {
            final List<ShowTO> shows = converter.convertCollection(showService.getShows(), ShowTO.class);
            Collections.sort(shows);
            return shows;
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public ShowTO getShow(final Integer id) {
        Validators.validateArgumentNotNull(id, ID_ARGUMENT);

        try {
            return converter.convert(showService.getShow(id), ShowTO.class);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     * @throws FacadeOperationException                              {@inheritDoc}
     */
    @Override
    public void add(final ShowTO show) {
        showTOValidator.validateNewShowTO(show);
        try {
            for (final GenreTO genre : show.getGenres()) {
                Validators.validateExists(genreService.getGenre(genre.getId()), GENRE_TO_ARGUMENT);
            }

            final Show showEntity = converter.convert(show, Show.class);
            showService.add(showEntity);
            if (showEntity.getId() == null) {
                throw new FacadeOperationException(NOT_SET_ID_EXCEPTION_MESSAGE);
            }
            show.setId(showEntity.getId());
            show.setPosition(showEntity.getPosition());
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     * @throws FacadeOperationException                                  {@inheritDoc}
     */
    @Override
    public void update(final ShowTO show) {
        showTOValidator.validateExistingShowTO(show);
        try {
            final Show showEntity = converter.convert(show, Show.class);
            Validators.validateExists(showService.exists(showEntity), SHOW_TO_ARGUMENT);
            for (final GenreTO genre : show.getGenres()) {
                Validators.validateExists(genreService.getGenre(genre.getId()), GENRE_TO_ARGUMENT);
            }

            showService.update(showEntity);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     * @throws FacadeOperationException                                  {@inheritDoc}
     */
    @Override
    public void remove(final ShowTO show) {
        showTOValidator.validateShowTOWithId(show);
        try {
            final Show showEntity = showService.getShow(show.getId());
            Validators.validateExists(showEntity, SHOW_TO_ARGUMENT);

            showService.remove(showEntity);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     * @throws FacadeOperationException                              {@inheritDoc}
     */
    @Override
    public void duplicate(final ShowTO show) {
        showTOValidator.validateShowTOWithId(show);
        try {
            final Show oldShow = showService.getShow(show.getId());
            Validators.validateExists(oldShow, SHOW_TO_ARGUMENT);

            showService.duplicate(oldShow);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     * @throws FacadeOperationException                                  {@inheritDoc}
     */
    @Override
    public void moveUp(final ShowTO show) {
        showTOValidator.validateShowTOWithId(show);
        try {
            final Show showEntity = showService.getShow(show.getId());
            Validators.validateExists(showEntity, SHOW_TO_ARGUMENT);
            final List<Show> shows = showService.getShows();
            Validators.validateMoveUp(shows, showEntity, SHOW_ARGUMENT);

            showService.moveUp(showEntity);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     * @throws FacadeOperationException                                  {@inheritDoc}
     */
    @Override
    public void moveDown(final ShowTO show) {
        showTOValidator.validateShowTOWithId(show);
        try {
            final Show showEntity = showService.getShow(show.getId());
            Validators.validateExists(showEntity, SHOW_TO_ARGUMENT);
            final List<Show> shows = showService.getShows();
            Validators.validateMoveDown(shows, showEntity, SHOW_ARGUMENT);

            showService.moveDown(showEntity);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     * @throws FacadeOperationException                              {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean exists(final ShowTO show) {
        showTOValidator.validateShowTOWithId(show);
        try {

            return showService.exists(converter.convert(show, Show.class));
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void updatePositions() {
        try {
            showService.updatePositions();
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Time getTotalLength() {
        try {
            return showService.getTotalLength();
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public int getSeasonsCount() {
        try {
            return showService.getSeasonsCount();
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public int getEpisodesCount() {
        try {
            return showService.getEpisodesCount();
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

}
