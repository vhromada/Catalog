package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import cz.vhromada.validators.Validators;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents implementation of facade for episodes.
 *
 * @author Vladimir Hromada
 */
@Component("episodeFacade")
@Transactional
public class EpisodeFacadeImpl implements EpisodeFacade {

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

	/** Validator for TO for season */
	@Autowired
	private SeasonTOValidator seasonTOValidator;

	/** Validator for TO for episode */
	@Autowired
	private EpisodeTOValidator episodeTOValidator;

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
	 * Returns validator for TO for episode.
	 *
	 * @return validator for TO for episode
	 */
	public EpisodeTOValidator getEpisodeTOValidator() {
		return episodeTOValidator;
	}

	/**
	 * Sets a new value to validator for TO for episode.
	 *
	 * @param episodeTOValidator new value
	 */
	public void setEpisodeTOValidator(final EpisodeTOValidator episodeTOValidator) {
		this.episodeTOValidator = episodeTOValidator;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for episodes isn't set
	 *                                  or conversion service isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public EpisodeTO getEpisode(final Integer id) {
		Validators.validateFieldNotNull(episodeService, "Service for episodes");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateArgumentNotNull(id, "ID");

		try {
			return conversionService.convert(episodeService.getEpisode(id), EpisodeTO.class);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for season isn't set
	 *                                  or service for episodes isn't set
	 *                                  or conversion service isn't set
	 *                                  or validator for TO for episode isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void add(final EpisodeTO episode) {
		Validators.validateFieldNotNull(seasonService, "Service for seasons");
		Validators.validateFieldNotNull(episodeService, "Service for episodes");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateFieldNotNull(episodeTOValidator, "Validator for TO for episode");
		episodeTOValidator.validateNewEpisodeTO(episode);
		try {
			final Season season = seasonService.getSeason(episode.getSeason().getId());
			Validators.validateExists(season, "TO for season");

			final Episode episodeEntity = conversionService.convert(episode, Episode.class);
			episodeEntity.setSeason(season);
			episodeService.add(episodeEntity);
			if (episodeEntity.getId() == null) {
				throw new FacadeOperationException("Service tier doesn't set ID.");
			}
			episode.setId(episodeEntity.getId());
			episode.setPosition(episodeEntity.getPosition());
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for seasons isn't set
	 *                                  or service for episodes isn't set
	 *                                  or conversion service isn't set
	 *                                  or validator for TO for episode isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void update(final EpisodeTO episode) {
		Validators.validateFieldNotNull(seasonService, "Service for seasons");
		Validators.validateFieldNotNull(episodeService, "Service for episodes");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateFieldNotNull(episodeTOValidator, "Validator for TO for episode");
		episodeTOValidator.validateExistingEpisodeTO(episode);
		try {
			final Episode episodeEntity = conversionService.convert(episode, Episode.class);
			Validators.validateExists(episodeService.exists(episodeEntity), "TO for episode");
			final Season season = seasonService.getSeason(episode.getSeason().getId());
			Validators.validateExists(season, "TO for season");

			episodeEntity.setSeason(season);
			episodeService.update(episodeEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for episodes isn't set
	 *                                  or validator for TO for episode isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void remove(final EpisodeTO episode) {
		Validators.validateFieldNotNull(episodeService, "Service for episodes");
		Validators.validateFieldNotNull(episodeTOValidator, "Validator for TO for episode");
		episodeTOValidator.validateEpisodeTOWithId(episode);
		try {
			final Episode episodeEntity = episodeService.getEpisode(episode.getId());
			Validators.validateExists(episodeEntity, "TO for episode");

			episodeService.remove(episodeEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for episodes isn't set
	 *                                  or validator for TO for episode isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void duplicate(final EpisodeTO episode) {
		Validators.validateFieldNotNull(episodeService, "Service for episodes");
		Validators.validateFieldNotNull(episodeTOValidator, "Validator for TO for episode");
		episodeTOValidator.validateEpisodeTOWithId(episode);
		try {
			final Episode oldEpisode = episodeService.getEpisode(episode.getId());
			Validators.validateExists(oldEpisode, "TO for episode");

			episodeService.duplicate(oldEpisode);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for episodes isn't set
	 *                                  or validator for TO for episode isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void moveUp(final EpisodeTO episode) {
		Validators.validateFieldNotNull(episodeService, "Service for episodes");
		Validators.validateFieldNotNull(episodeTOValidator, "Validator for TO for episode");
		episodeTOValidator.validateEpisodeTOWithId(episode);
		try {
			final Episode episodeEntity = episodeService.getEpisode(episode.getId());
			Validators.validateExists(episodeEntity, "TO for episode");
			final List<Episode> episodes = episodeService.findEpisodesBySeason(episodeEntity.getSeason());
			Validators.validateMoveUp(episodes, episodeEntity, "episode");

			episodeService.moveUp(episodeEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for episodes isn't set
	 *                                  or validator for TO for episode isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void moveDown(final EpisodeTO episode) {
		Validators.validateFieldNotNull(episodeService, "Service for episodes");
		Validators.validateFieldNotNull(episodeTOValidator, "Validator for TO for episode");
		episodeTOValidator.validateEpisodeTOWithId(episode);
		try {
			final Episode episodeEntity = episodeService.getEpisode(episode.getId());
			Validators.validateExists(episodeEntity, "TO for episode");
			final List<Episode> episodes = episodeService.findEpisodesBySeason(episodeEntity.getSeason());
			Validators.validateMoveDown(episodes, episodeEntity, "episode");

			episodeService.moveDown(episodeEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for episodes isn't set
	 *                                  or conversion service isn't set
	 *                                  or validator for TO for episode isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean exists(final EpisodeTO episode) {
		Validators.validateFieldNotNull(episodeService, "Service for episodes");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateFieldNotNull(episodeTOValidator, "Validator for TO for episode");
		episodeTOValidator.validateEpisodeTOWithId(episode);
		try {

			return episodeService.exists(conversionService.convert(episode, Episode.class));
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for seasons isn't set
	 *                                  or service for episodes isn't set
	 *                                  or conversion service isn't set
	 *                                  or validator for TO for season isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public List<EpisodeTO> findEpisodesBySeason(final SeasonTO season) {
		Validators.validateFieldNotNull(seasonService, "Service for seasons");
		Validators.validateFieldNotNull(episodeService, "Service for episodes");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateFieldNotNull(seasonTOValidator, "Validator for TO for season");
		seasonTOValidator.validateSeasonTOWithId(season);
		try {
			final Season seasonEntity = seasonService.getSeason(season.getId());
			Validators.validateExists(seasonEntity, "TO for season");

			final List<EpisodeTO> episodes = new ArrayList<>();
			for (Episode episode : episodeService.findEpisodesBySeason(seasonEntity)) {
				episodes.add(conversionService.convert(episode, EpisodeTO.class));
			}
			Collections.sort(episodes);
			return episodes;
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

}

