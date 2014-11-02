package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.facade.SerieFacade;
import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.to.SerieTO;
import cz.vhromada.catalog.facade.validators.SerieTOValidator;
import cz.vhromada.catalog.service.EpisodeService;
import cz.vhromada.catalog.service.GenreService;
import cz.vhromada.catalog.service.SeasonService;
import cz.vhromada.catalog.service.SerieService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.validators.Validators;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents implementation of facade for series.
 *
 * @author Vladimir Hromada
 */
@Component("serieFacade")
@Transactional
public class SerieFacadeImpl implements SerieFacade {

	/** Service for series */
	@Autowired
	private SerieService serieService;

	/** Service for seasons */
	@Autowired
	private SeasonService seasonService;

	/** Service for episodes */
	@Autowired
	private EpisodeService episodeService;

	/** Service for genres */
	@Autowired
	private GenreService genreService;

	/** Conversion service */
	@Autowired
	@Qualifier("coreConversionService")
	private ConversionService conversionService;

	/** Validator for TO for serie */
	@Autowired
	private SerieTOValidator serieTOValidator;

	/**
	 * Returns service for series.
	 *
	 * @return service for series
	 */
	public SerieService getSerieService() {
		return serieService;
	}

	/**
	 * Sets a new value to service for series.
	 *
	 * @param serieService new value
	 */
	public void setSerieService(final SerieService serieService) {
		this.serieService = serieService;
	}

	/**
	 * Returns service for seasons.
	 *
	 * @return service for seasons
	 */
	public SeasonService getSeasonService() {
		return seasonService;
	}

	/**
	 * Sets a new value to service for seasons.
	 *
	 * @param seasonService new value
	 */
	public void setSeasonService(final SeasonService seasonService) {
		this.seasonService = seasonService;
	}

	/**
	 * Returns service for episodes.
	 *
	 * @return service for episodes
	 */
	public EpisodeService getEpisodeService() {
		return episodeService;
	}

	/**
	 * Sets a new value to service for episodes.
	 *
	 * @param episodeService new value
	 */
	public void setEpisodeService(final EpisodeService episodeService) {
		this.episodeService = episodeService;
	}

	/**
	 * Returns service for genres.
	 *
	 * @return service for genres
	 */
	public GenreService getGenreService() {
		return genreService;
	}

	/**
	 * Sets a new value to service for genres.
	 *
	 * @param genreService new value
	 */
	public void setGenreService(final GenreService genreService) {
		this.genreService = genreService;
	}

	/**
	 * Returns conversion service.
	 *
	 * @return conversion service
	 */
	public ConversionService getConversionService() {
		return conversionService;
	}

	/**
	 * Sets a new value to conversion service.
	 *
	 * @param conversionService new value
	 */
	public void setConversionService(final ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	/**
	 * Returns validator for TO for serie.
	 *
	 * @return validator for TO for serie
	 */
	public SerieTOValidator getSerieTOValidator() {
		return serieTOValidator;
	}

	/**
	 * Sets a new value to validator for TO for serie.
	 *
	 * @param serieTOValidator new value
	 */
	public void setSerieTOValidator(final SerieTOValidator serieTOValidator) {
		this.serieTOValidator = serieTOValidator;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for series isn't set
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void newData() {
		Validators.validateFieldNotNull(serieService, "Service for series");

		try {
			serieService.newData();
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for series isn't set
	 *                                  or service for seasons isn't set
	 *                                  or service for episodes isn't set
	 *                                  or conversion service isn't set
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public List<SerieTO> getSeries() {
		Validators.validateFieldNotNull(serieService, "Service for series");
		Validators.validateFieldNotNull(seasonService, "Service for seasons");
		Validators.validateFieldNotNull(episodeService, "Service for episodes");
		Validators.validateFieldNotNull(conversionService, "Conversion service");

		try {
			final List<SerieTO> series = new ArrayList<>();
			for (Serie serie : serieService.getSeries()) {
				series.add(convertSerieToSerieTO(serie));
			}
			Collections.sort(series);
			return series;
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for series isn't set
	 *                                  or service for seasons isn't set
	 *                                  or service for episodes isn't set
	 *                                  or conversion service isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public SerieTO getSerie(final Integer id) {
		Validators.validateFieldNotNull(serieService, "Service for series");
		Validators.validateFieldNotNull(seasonService, "Service for seasons");
		Validators.validateFieldNotNull(episodeService, "Service for episodes");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateArgumentNotNull(id, "ID");

		try {
			return convertSerieToSerieTO(serieService.getSerie(id));
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for series isn't set
	 *                                  or service for genres isn't set
	 *                                  or conversion service isn't set
	 *                                  or validator for TO for serie isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void add(final SerieTO serie) {
		Validators.validateFieldNotNull(serieService, "Service for series");
		Validators.validateFieldNotNull(genreService, "Service for genres");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateFieldNotNull(serieTOValidator, "Validator for TO for serie");
		serieTOValidator.validateNewSerieTO(serie);
		try {
			for (GenreTO genre : serie.getGenres()) {
				Validators.validateExists(genreService.getGenre(genre.getId()), "TO for genre");
			}

			final Serie serieEntity = conversionService.convert(serie, Serie.class);
			serieService.add(serieEntity);
			if (serieEntity.getId() == null) {
				throw new FacadeOperationException("Service tier doesn't set ID.");
			}
			serie.setId(serieEntity.getId());
			serie.setPosition(serieEntity.getPosition());
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for series isn't set
	 *                                  or service for genres isn't set
	 *                                  or conversion service isn't set
	 *                                  or validator for TO for serie isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void update(final SerieTO serie) {
		Validators.validateFieldNotNull(serieService, "Service for series");
		Validators.validateFieldNotNull(genreService, "Service for genres");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateFieldNotNull(serieTOValidator, "Validator for TO for serie");
		serieTOValidator.validateExistingSerieTO(serie);
		try {
			final Serie serieEntity = conversionService.convert(serie, Serie.class);
			Validators.validateExists(serieService.exists(serieEntity), "TO for serie");
			for (GenreTO genre : serie.getGenres()) {
				Validators.validateExists(genreService.getGenre(genre.getId()), "TO for genre");
			}

			serieService.update(serieEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for series isn't set
	 *                                  or validator for TO for serie isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void remove(final SerieTO serie) {
		Validators.validateFieldNotNull(serieService, "Service for series");
		Validators.validateFieldNotNull(serieTOValidator, "Validator for TO for serie");
		serieTOValidator.validateSerieTOWithId(serie);
		try {
			final Serie serieEntity = serieService.getSerie(serie.getId());
			Validators.validateExists(serieEntity, "TO for serie");

			serieService.remove(serieEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for series isn't set
	 *                                  or validator for TO for serie isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void duplicate(final SerieTO serie) {
		Validators.validateFieldNotNull(serieService, "Service for series");
		Validators.validateFieldNotNull(serieTOValidator, "Validator for TO for serie");
		serieTOValidator.validateSerieTOWithId(serie);
		try {
			final Serie oldSerie = serieService.getSerie(serie.getId());
			Validators.validateExists(oldSerie, "TO for serie");

			serieService.duplicate(oldSerie);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for series isn't set
	 *                                  or validator for TO for serie isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void moveUp(final SerieTO serie) {
		Validators.validateFieldNotNull(serieService, "Service for series");
		Validators.validateFieldNotNull(serieTOValidator, "Validator for TO for serie");
		serieTOValidator.validateSerieTOWithId(serie);
		try {
			final Serie serieEntity = serieService.getSerie(serie.getId());
			Validators.validateExists(serieEntity, "TO for serie");
			final List<Serie> series = serieService.getSeries();
			Validators.validateMoveUp(series, serieEntity, "serie");

			serieService.moveUp(serieEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for series isn't set
	 *                                  or validator for TO for serie isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void moveDown(final SerieTO serie) {
		Validators.validateFieldNotNull(serieService, "Service for series");
		Validators.validateFieldNotNull(serieTOValidator, "Validator for TO for serie");
		serieTOValidator.validateSerieTOWithId(serie);
		try {
			final Serie serieEntity = serieService.getSerie(serie.getId());
			Validators.validateExists(serieEntity, "TO for serie");
			final List<Serie> series = serieService.getSeries();
			Validators.validateMoveDown(series, serieEntity, "serie");

			serieService.moveDown(serieEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for series isn't set
	 *                                  or conversion service isn't set
	 *                                  or validator for TO for serie isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean exists(final SerieTO serie) {
		Validators.validateFieldNotNull(serieService, "Service for series");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateFieldNotNull(serieTOValidator, "Validator for TO for serie");
		serieTOValidator.validateSerieTOWithId(serie);
		try {

			return serieService.exists(conversionService.convert(serie, Serie.class));
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for series isn't set
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void updatePositions() {
		Validators.validateFieldNotNull(serieService, "Service for series");

		try {
			serieService.updatePositions();
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for series isn't set
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public Time getTotalLength() {
		Validators.validateFieldNotNull(serieService, "Service for series");

		try {
			return serieService.getTotalLength();
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for series isn't set
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public int getSeasonsCount() {
		Validators.validateFieldNotNull(serieService, "Service for series");

		try {
			return serieService.getSeasonsCount();
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for series isn't set
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public int getEpisodesCount() {
		Validators.validateFieldNotNull(serieService, "Service for series");

		try {
			return serieService.getEpisodesCount();
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * Converts entity serie to TO for serie.
	 *
	 * @param serie entity serie
	 * @return converted TO for serie
	 */
	private SerieTO convertSerieToSerieTO(final Serie serie) {
		final SerieTO serieTO = conversionService.convert(serie, SerieTO.class);
		if (serieTO != null) {
			final List<Season> seasons = seasonService.findSeasonsBySerie(serie);
			int count = 0;
			int length = 0;
			for (Season season : seasons) {
				count += episodeService.findEpisodesBySeason(season).size();
				length += episodeService.getTotalLengthBySeason(season).getLength();
			}
			serieTO.setSeasonsCount(seasons.size());
			serieTO.setEpisodesCount(count);
			serieTO.setTotalLength(new Time(length));
		}
		return serieTO;
	}

}
