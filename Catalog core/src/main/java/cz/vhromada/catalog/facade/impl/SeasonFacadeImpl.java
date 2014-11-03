package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.facade.SeasonFacade;
import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.catalog.facade.to.SerieTO;
import cz.vhromada.catalog.facade.validators.SeasonTOValidator;
import cz.vhromada.catalog.facade.validators.SerieTOValidator;
import cz.vhromada.catalog.service.EpisodeService;
import cz.vhromada.catalog.service.SeasonService;
import cz.vhromada.catalog.service.SerieService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents implementation of facade for seasons.
 *
 * @author Vladimir Hromada
 */
@Component("seasonFacade")
@Transactional
public class SeasonFacadeImpl implements SeasonFacade {

	/** Service for series field */
	private static final String SERIE_SERVICE_FIELD = "Service for series";

	/** Service for seasons field */
	private static final String SEASON_SERVICE_FIELD = "Service for seasons";

	/** Service for episodes field */
	private static final String EPISODE_SERVICE_FIELD = "Service for episodes";

	/** Conversion service field */
	private static final String CONVERSION_SERVICE_FIELD = "Conversion service";

	/** Validator for TO for serie field */
	private static final String SERIE_TO_VALIDATOR_FIELD = "Validator for TO for serie";

	/** Validator for TO for season field */
	private static final String SEASON_TO_VALIDATOR_FIELD = "Validator for TO for season";

	/** Season argument */
	private static final String SEASON_ARGUMENT = "season";

	/** TO for serie argument */
	private static final String SERIE_TO_ARGUMENT = "TO for serie";

	/** TO for season argument */
	private static final String SEASON_TO_ARGUMENT = "TO for season";

	/** ID argument */
	private static final String ID_ARGUMENT = "ID";

	/** Message for {@link FacadeOperationException} */
	private static final String FACADE_OPERATION_EXCEPTION_MESSAGE = "Error in working with service tier.";

	/** Message for not setting ID */
	private static final String NOT_SET_ID_EXCEPTION_MESSAGE = "Service tier doesn't set ID.";

	/** Service for series */
	@Autowired
	private SerieService serieService;

	/** Service for seasons */
	@Autowired
	private SeasonService seasonService;

	/** Service for episodes */
	@Autowired
	private EpisodeService episodeService;

	/** Conversion service */
	@Autowired
	@Qualifier("coreConversionService")
	private ConversionService conversionService;

	/** Validator for TO for serie */
	@Autowired
	private SerieTOValidator serieTOValidator;

	/** Validator for TO for season */
	@Autowired
	private SeasonTOValidator seasonTOValidator;

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
	 * Returns validator for TO for season.
	 *
	 * @return validator for TO for season
	 */
	public SeasonTOValidator getSeasonTOValidator() {
		return seasonTOValidator;
	}

	/**
	 * Sets a new value to validator for TO for season.
	 *
	 * @param seasonTOValidator new value
	 */
	public void setSeasonTOValidator(final SeasonTOValidator seasonTOValidator) {
		this.seasonTOValidator = seasonTOValidator;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for seasons isn't set
	 *                                  or service for episodes isn't set
	 *                                  or conversion service isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public SeasonTO getSeason(final Integer id) {
		Validators.validateFieldNotNull(seasonService, SEASON_SERVICE_FIELD);
		Validators.validateFieldNotNull(episodeService, EPISODE_SERVICE_FIELD);
		Validators.validateFieldNotNull(conversionService, CONVERSION_SERVICE_FIELD);
		Validators.validateArgumentNotNull(id, ID_ARGUMENT);

		try {
			return convertSeasonToSeasonTO(seasonService.getSeason(id));
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for series isn't set
	 *                                  or service for seasons isn't set
	 *                                  or conversion service isn't set
	 *                                  or validator for TO for season isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  {@inheritDoc}
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void add(final SeasonTO season) {
		Validators.validateFieldNotNull(serieService, SERIE_SERVICE_FIELD);
		Validators.validateFieldNotNull(seasonService, SEASON_SERVICE_FIELD);
		Validators.validateFieldNotNull(conversionService, CONVERSION_SERVICE_FIELD);
		Validators.validateFieldNotNull(seasonTOValidator, SEASON_TO_VALIDATOR_FIELD);
		seasonTOValidator.validateNewSeasonTO(season);
		try {
			final Serie serie = serieService.getSerie(season.getSerie().getId());
			Validators.validateExists(serie, SERIE_TO_ARGUMENT);

			final Season seasonEntity = conversionService.convert(season, Season.class);
			seasonEntity.setSerie(serie);
			seasonService.add(seasonEntity);
			if (seasonEntity.getId() == null) {
				throw new FacadeOperationException(NOT_SET_ID_EXCEPTION_MESSAGE);
			}
			season.setId(seasonEntity.getId());
			season.setPosition(seasonEntity.getPosition());
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for series isn't set
	 *                                  or service for seasons isn't set
	 *                                  or conversion service isn't set
	 *                                  or validator for TO for season isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  {@inheritDoc}
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void update(final SeasonTO season) {
		Validators.validateFieldNotNull(serieService, SERIE_SERVICE_FIELD);
		Validators.validateFieldNotNull(seasonService, SEASON_SERVICE_FIELD);
		Validators.validateFieldNotNull(conversionService, CONVERSION_SERVICE_FIELD);
		Validators.validateFieldNotNull(seasonTOValidator, SEASON_TO_VALIDATOR_FIELD);
		seasonTOValidator.validateExistingSeasonTO(season);
		try {
			final Season seasonEntity = conversionService.convert(season, Season.class);
			Validators.validateExists(seasonService.exists(seasonEntity), SEASON_TO_ARGUMENT);
			final Serie serie = serieService.getSerie(season.getSerie().getId());
			Validators.validateExists(serie, SERIE_TO_ARGUMENT);

			seasonEntity.setSerie(serie);
			seasonService.update(seasonEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for seasons isn't set
	 *                                  or validator for TO for season isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  {@inheritDoc}
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void remove(final SeasonTO season) {
		Validators.validateFieldNotNull(seasonService, SEASON_SERVICE_FIELD);
		Validators.validateFieldNotNull(seasonTOValidator, SEASON_TO_VALIDATOR_FIELD);
		seasonTOValidator.validateSeasonTOWithId(season);
		try {
			final Season seasonEntity = seasonService.getSeason(season.getId());
			Validators.validateExists(seasonEntity, SEASON_TO_ARGUMENT);

			seasonService.remove(seasonEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for seasons isn't set
	 *                                  or validator for TO for season isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  {@inheritDoc}
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void duplicate(final SeasonTO season) {
		Validators.validateFieldNotNull(seasonService, SEASON_SERVICE_FIELD);
		Validators.validateFieldNotNull(seasonTOValidator, SEASON_TO_VALIDATOR_FIELD);
		seasonTOValidator.validateSeasonTOWithId(season);
		try {
			final Season oldSeason = seasonService.getSeason(season.getId());
			Validators.validateExists(oldSeason, SEASON_TO_ARGUMENT);

			seasonService.duplicate(oldSeason);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for seasons isn't set
	 *                                  or validator for TO for season isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  {@inheritDoc}
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void moveUp(final SeasonTO season) {
		Validators.validateFieldNotNull(seasonService, SEASON_SERVICE_FIELD);
		Validators.validateFieldNotNull(seasonTOValidator, SEASON_TO_VALIDATOR_FIELD);
		seasonTOValidator.validateSeasonTOWithId(season);
		try {
			final Season seasonEntity = seasonService.getSeason(season.getId());
			Validators.validateExists(seasonEntity, SEASON_TO_ARGUMENT);
			final List<Season> seasons = seasonService.findSeasonsBySerie(seasonEntity.getSerie());
			Validators.validateMoveUp(seasons, seasonEntity, SEASON_ARGUMENT);

			seasonService.moveUp(seasonEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for seasons isn't set
	 *                                  or validator for TO for season isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  {@inheritDoc}
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void moveDown(final SeasonTO season) {
		Validators.validateFieldNotNull(seasonService, SEASON_SERVICE_FIELD);
		Validators.validateFieldNotNull(seasonTOValidator, SEASON_TO_VALIDATOR_FIELD);
		seasonTOValidator.validateSeasonTOWithId(season);
		try {
			final Season seasonEntity = seasonService.getSeason(season.getId());
			Validators.validateExists(seasonEntity, SEASON_TO_ARGUMENT);
			final List<Season> seasons = seasonService.findSeasonsBySerie(seasonEntity.getSerie());
			Validators.validateMoveDown(seasons, seasonEntity, SEASON_ARGUMENT);

			seasonService.moveDown(seasonEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for seasons isn't set
	 *                                  or conversion service isn't set
	 *                                  or validator for TO for season isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean exists(final SeasonTO season) {
		Validators.validateFieldNotNull(seasonService, SEASON_SERVICE_FIELD);
		Validators.validateFieldNotNull(conversionService, CONVERSION_SERVICE_FIELD);
		Validators.validateFieldNotNull(seasonTOValidator, SEASON_TO_VALIDATOR_FIELD);
		seasonTOValidator.validateSeasonTOWithId(season);
		try {

			return seasonService.exists(conversionService.convert(season, Season.class));
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for series isn't set
	 *                                  or service for seasons isn't set
	 *                                  or service for episodes isn't set
	 *                                  or conversion service isn't set
	 *                                  or validator for TO for serie isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws cz.vhromada.validators.exceptions.ValidationException
	 *                                  {@inheritDoc}
	 * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
	 *                                  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public List<SeasonTO> findSeasonsBySerie(final SerieTO serie) {
		Validators.validateFieldNotNull(serieService, SERIE_SERVICE_FIELD);
		Validators.validateFieldNotNull(seasonService, SEASON_SERVICE_FIELD);
		Validators.validateFieldNotNull(episodeService, EPISODE_SERVICE_FIELD);
		Validators.validateFieldNotNull(conversionService, CONVERSION_SERVICE_FIELD);
		Validators.validateFieldNotNull(serieTOValidator, SERIE_TO_VALIDATOR_FIELD);
		serieTOValidator.validateSerieTOWithId(serie);
		try {
			final Serie serieEntity = serieService.getSerie(serie.getId());
			Validators.validateExists(serieEntity, SERIE_TO_ARGUMENT);

			final List<SeasonTO> seasons = new ArrayList<>();
			for (Season season : seasonService.findSeasonsBySerie(serieEntity)) {
				seasons.add(convertSeasonToSeasonTO(season));
			}
			Collections.sort(seasons);
			return seasons;
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
		}
	}

	/**
	 * Converts entity season to TO for season.
	 *
	 * @param season converting entity season
	 * @return converted TO for season
	 */
	private SeasonTO convertSeasonToSeasonTO(final Season season) {
		final SeasonTO seasonTO = conversionService.convert(season, SeasonTO.class);
		if (seasonTO != null) {
			seasonTO.setEpisodesCount(episodeService.findEpisodesBySeason(season).size());
			seasonTO.setTotalLength(episodeService.getTotalLengthBySeason(season));
		}
		return seasonTO;
	}

}
