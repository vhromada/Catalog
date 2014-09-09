package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.facade.MusicFacade;
import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.MusicTO;
import cz.vhromada.catalog.facade.validators.MusicTOValidator;
import cz.vhromada.catalog.service.MusicService;
import cz.vhromada.catalog.service.SongService;
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
 * A class represents implementation of service for music.
 *
 * @author Vladimir Hromada
 */
@Component("musicFacade")
@Transactional
public class MusicFacadeImpl implements MusicFacade {

	/** Service for music */
	@Autowired
	private MusicService musicService;

	/** Service for songs */
	@Autowired
	private SongService songService;

	/** Conversion service */
	@Autowired
	@Qualifier("coreConversionService")
	private ConversionService conversionService;

	/** Validator for TO for music */
	@Autowired
	private MusicTOValidator musicTOValidator;

	/**
	 * Returns service for music.
	 *
	 * @return service for music
	 */
	public MusicService getMusicService() {
		return musicService;
	}

	/**
	 * Sets a new value to service for music.
	 *
	 * @param musicService new value
	 */
	public void setMusicService(final MusicService musicService) {
		this.musicService = musicService;
	}

	/**
	 * Returns service for songs.
	 *
	 * @return service for songs
	 */
	public SongService getSongService() {
		return songService;
	}

	/**
	 * Sets a new value to service for songs.
	 *
	 * @param songService new value
	 */
	public void setSongService(final SongService songService) {
		this.songService = songService;
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
	 * Returns validator for TO for music.
	 *
	 * @return validator for TO for music
	 */
	public MusicTOValidator getMusicTOValidator() {
		return musicTOValidator;
	}

	/**
	 * Sets a new value to validator for TO for music.
	 *
	 * @param musicTOValidator new value
	 */
	public void setMusicTOValidator(final MusicTOValidator musicTOValidator) {
		this.musicTOValidator = musicTOValidator;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for music isn't set
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void newData() {
		Validators.validateFieldNotNull(musicService, "Service for music");

		try {
			musicService.newData();
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for music isn't set
	 *                                  or service for songs isn't set
	 *                                  or conversion service isn't set
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public List<MusicTO> getMusic() {
		Validators.validateFieldNotNull(musicService, "Service for music");
		Validators.validateFieldNotNull(songService, "Service for songs");
		Validators.validateFieldNotNull(conversionService, "Conversion service");

		try {
			final List<MusicTO> musicList = new ArrayList<>();
			for (Music music : musicService.getMusic()) {
				musicList.add(convertMusicToMusicTO(music));
			}
			return musicList;
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for music isn't set
	 *                                  or service for songs isn't set
	 *                                  or conversion service isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public MusicTO getMusic(final Integer id) {
		Validators.validateFieldNotNull(musicService, "Service for music");
		Validators.validateFieldNotNull(songService, "Service for songs");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateArgumentNotNull(id, "ID");

		try {
			return convertMusicToMusicTO(musicService.getMusic(id));
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for music isn't set
	 *                                  or conversion service isn't set
	 *                                  or validator for TO for music isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void add(final MusicTO music) {
		Validators.validateFieldNotNull(musicService, "Service for music");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateFieldNotNull(musicTOValidator, "Validator for TO for music");
		musicTOValidator.validateNewMusicTO(music);

		try {
			final Music musicEntity = conversionService.convert(music, Music.class);
			musicService.add(musicEntity);
			if (musicEntity.getId() == null) {
				throw new FacadeOperationException("service tier doesn't set ID.");
			}
			music.setId(musicEntity.getId());
			music.setPosition(musicEntity.getPosition());
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for music isn't set
	 *                                  or conversion service isn't set
	 *                                  or validator for TO for music isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void update(final MusicTO music) {
		Validators.validateFieldNotNull(musicService, "Service for music");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateFieldNotNull(musicTOValidator, "Validator for TO for music");
		musicTOValidator.validateExistingMusicTO(music);
		try {
			final Music musicEntity = conversionService.convert(music, Music.class);
			Validators.validateExists(musicService.exists(musicEntity), "TO for music");

			musicService.update(musicEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for music isn't set
	 *                                  or validator for TO for music isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void remove(final MusicTO music) {
		Validators.validateFieldNotNull(musicService, "Service for music");
		Validators.validateFieldNotNull(musicTOValidator, "Validator for TO for music");
		musicTOValidator.validateMusicTOWithId(music);
		try {
			final Music musicEntity = musicService.getMusic(music.getId());
			Validators.validateExists(musicEntity, "TO for music");

			musicService.remove(musicEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for music isn't set
	 *                                  or validator for TO for music isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void duplicate(final MusicTO music) {
		Validators.validateFieldNotNull(musicService, "Service for music");
		Validators.validateFieldNotNull(musicTOValidator, "Validator for TO for music");
		musicTOValidator.validateMusicTOWithId(music);
		try {
			final Music oldMusic = musicService.getMusic(music.getId());
			Validators.validateExists(oldMusic, "TO for music");

			musicService.duplicate(oldMusic);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for music isn't set
	 *                                  or validator for TO for music isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void moveUp(final MusicTO music) {
		Validators.validateFieldNotNull(musicService, "Service for music");
		Validators.validateFieldNotNull(musicTOValidator, "Validator for TO for music");
		musicTOValidator.validateMusicTOWithId(music);
		try {
			final Music musicEntity = musicService.getMusic(music.getId());
			Validators.validateExists(musicEntity, "TO for music");
			final List<Music> musicList = musicService.getMusic();
			Validators.validateMoveUp(musicList, musicEntity, "music");

			musicService.moveUp(musicEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for music isn't set
	 *                                  or validator for TO for music isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void moveDown(final MusicTO music) {
		Validators.validateFieldNotNull(musicService, "Service for music");
		Validators.validateFieldNotNull(musicTOValidator, "Validator for TO for music");
		musicTOValidator.validateMusicTOWithId(music);
		try {
			final Music musicEntity = musicService.getMusic(music.getId());
			Validators.validateExists(musicEntity, "TO for music");
			final List<Music> musicList = musicService.getMusic();
			Validators.validateMoveDown(musicList, musicEntity, "music");

			musicService.moveDown(musicEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for music isn't set
	 *                                  or conversion service isn't set
	 *                                  or validator for TO for music isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean exists(final MusicTO music) {
		Validators.validateFieldNotNull(musicService, "Service for music");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateFieldNotNull(musicTOValidator, "Validator for TO for music");
		musicTOValidator.validateMusicTOWithId(music);
		try {

			return musicService.exists(conversionService.convert(music, Music.class));
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for music isn't set
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void updatePositions() {
		Validators.validateFieldNotNull(musicService, "Service for music");

		try {
			musicService.updatePositions();
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for music isn't set
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public int getTotalMediaCount() {
		Validators.validateFieldNotNull(musicService, "Service for music");

		try {
			return musicService.getTotalMediaCount();
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for music isn't set
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public Time getTotalLength() {
		Validators.validateFieldNotNull(musicService, "Service for music");

		try {
			return musicService.getTotalLength();
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for music isn't set
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public int getSongsCount() {
		Validators.validateFieldNotNull(musicService, "Service for music");

		try {
			return musicService.getSongsCount();
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * Converts entity music to TO for music.
	 *
	 * @param music converting entity music
	 * @return converted TO for music
	 */
	private MusicTO convertMusicToMusicTO(final Music music) {
		final MusicTO musicTO = conversionService.convert(music, MusicTO.class);
		if (musicTO != null) {
			musicTO.setSongsCount(songService.findSongsByMusic(music).size());
			musicTO.setTotalLength(songService.getTotalLengthByMusic(music));
		}
		return musicTO;
	}

}
