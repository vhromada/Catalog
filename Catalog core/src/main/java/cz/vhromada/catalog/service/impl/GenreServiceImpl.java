package cz.vhromada.catalog.service.impl;

import java.util.List;

import cz.vhromada.catalog.dao.GenreDAO;
import cz.vhromada.catalog.dao.entities.Genre;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.catalog.service.GenreService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of service for genres.
 *
 * @author Vladimir Hromada
 */
@Component("genreService")
public class GenreServiceImpl extends AbstractService<Genre> implements GenreService {

	/** DAO for genres field */
	private static final String GENRE_DAO_FIELD = "DAO for genres";

	/** Cache for genres field */
	private static final String GENRE_CACHE_FIELD = "Cache for genres";

	/** Genre argument */
	private static final String GENRE_ARGUMENT = "Genre";

	/** ID argument */
	private static final String ID_ARGUMENT = "ID";

	/** Genre names argument */
	private static final String GENRE_NAMES_ARGUMENT = "List of genre names";

	/** Message for {@link ServiceOperationException} */
	private static final String SERVICE_OPERATION_EXCEPTION_MESSAGE = "Error in working with DAO tier.";

	/** Cache key for list of genres */
	private static final String GENRES_CACHE_KEY = "genres";

	/** Cache key for genre */
	private static final String GENRE_CACHE_KEY = "genre";

	/** DAO for genres */
	@Autowired
	private GenreDAO genreDAO;

	/** Cache for genres */
	@Value("#{cacheManager.getCache('genreCache')}")
	private Cache genreCache;

	/**
	 * Returns DAO for genres.
	 *
	 * @return DAO for genres
	 */
	public GenreDAO getGenreDAO() {
		return genreDAO;
	}

	/**
	 * Sets a new value to DAO for genres.
	 *
	 * @param genreDAO new value
	 */
	public void setGenreDAO(final GenreDAO genreDAO) {
		this.genreDAO = genreDAO;
	}

	/**
	 * Returns cache for genres.
	 *
	 * @return cache for genres
	 */
	public Cache getGenreCache() {
		return genreCache;
	}

	/**
	 * Sets a new value to cache for genres.
	 *
	 * @param genreCache new value
	 */
	public void setGenreCache(final Cache genreCache) {
		this.genreCache = genreCache;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException     if DAO for genres isn't set
	 *                                   or cache for genres isn't set
	 * @throws ServiceOperationException {@inheritDoc}
	 */
	@Override
	public void newData() {
		Validators.validateFieldNotNull(genreDAO, GENRE_DAO_FIELD);
		Validators.validateFieldNotNull(genreCache, GENRE_CACHE_FIELD);

		try {
			for (Genre genre : getCachedGenres(false)) {
				genreDAO.remove(genre);
			}
			genreCache.clear();
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException     if DAO for genres isn't set
	 *                                   or cache for genres isn't set
	 * @throws ServiceOperationException {@inheritDoc}
	 */
	@Override
	public List<Genre> getGenres() {
		Validators.validateFieldNotNull(genreDAO, GENRE_DAO_FIELD);
		Validators.validateFieldNotNull(genreCache, GENRE_CACHE_FIELD);

		try {
			return getCachedGenres(true);
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException     if DAO for genres isn't set
	 *                                   or cache for genres isn't set
	 * @throws IllegalArgumentException  {@inheritDoc}
	 * @throws ServiceOperationException {@inheritDoc}
	 */
	@Override
	public Genre getGenre(final Integer id) {
		Validators.validateFieldNotNull(genreDAO, GENRE_DAO_FIELD);
		Validators.validateFieldNotNull(genreCache, GENRE_CACHE_FIELD);
		Validators.validateArgumentNotNull(id, ID_ARGUMENT);

		try {
			return getCachedGenre(id);
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException     if DAO for genres isn't set
	 *                                   or cache for genres isn't set
	 * @throws IllegalArgumentException  {@inheritDoc}
	 * @throws ServiceOperationException {@inheritDoc}
	 */
	@Override
	public void add(final Genre genre) {
		Validators.validateFieldNotNull(genreDAO, GENRE_DAO_FIELD);
		Validators.validateFieldNotNull(genreCache, GENRE_CACHE_FIELD);
		Validators.validateArgumentNotNull(genre, GENRE_ARGUMENT);

		try {
			genreDAO.add(genre);
			addObjectToListCache(genreCache, GENRES_CACHE_KEY, genre);
			addObjectToCache(genreCache, GENRE_CACHE_KEY + genre.getId(), genre);
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException     if DAO for genres isn't set
	 *                                   or cache for genres isn't set
	 * @throws IllegalArgumentException  {@inheritDoc}
	 * @throws ServiceOperationException {@inheritDoc}
	 */
	@Override
	public void add(final List<String> genres) {
		Validators.validateFieldNotNull(genreDAO, GENRE_DAO_FIELD);
		Validators.validateFieldNotNull(genreCache, GENRE_CACHE_FIELD);
		Validators.validateArgumentNotNull(genres, GENRE_NAMES_ARGUMENT);

		try {
			for (String genre : genres) {
				final Genre genreEntity = new Genre();
				genreEntity.setName(genre);
				genreDAO.add(genreEntity);
			}
			genreCache.clear();
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException     if DAO for genres isn't set
	 *                                   or cache for genres isn't set
	 * @throws IllegalArgumentException  {@inheritDoc}
	 * @throws ServiceOperationException {@inheritDoc}
	 */
	@Override
	public void update(final Genre genre) {
		Validators.validateFieldNotNull(genreDAO, GENRE_DAO_FIELD);
		Validators.validateFieldNotNull(genreCache, GENRE_CACHE_FIELD);
		Validators.validateArgumentNotNull(genre, GENRE_ARGUMENT);

		try {
			genreDAO.update(genre);
			genreCache.clear();
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException     if DAO for genres isn't set
	 *                                   or cache for genres isn't set
	 * @throws IllegalArgumentException  {@inheritDoc}
	 * @throws ServiceOperationException {@inheritDoc}
	 */
	@Override
	public void remove(final Genre genre) {
		Validators.validateFieldNotNull(genreDAO, GENRE_DAO_FIELD);
		Validators.validateFieldNotNull(genreCache, GENRE_CACHE_FIELD);
		Validators.validateArgumentNotNull(genre, GENRE_ARGUMENT);

		try {
			genreDAO.remove(genre);
			removeObjectFromCache(genreCache, GENRES_CACHE_KEY, genre);
			genreCache.evict(genre.getId());
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException     if DAO for genres isn't set
	 *                                   or cache for genres isn't set
	 * @throws IllegalArgumentException  {@inheritDoc}
	 * @throws ServiceOperationException {@inheritDoc}
	 */
	@Override
	public boolean exists(final Genre genre) {
		Validators.validateFieldNotNull(genreDAO, GENRE_DAO_FIELD);
		Validators.validateFieldNotNull(genreCache, GENRE_CACHE_FIELD);
		Validators.validateArgumentNotNull(genre, GENRE_ARGUMENT);

		try {
			return getCachedGenre(genre.getId()) != null;
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
		}
	}

	@Override
	protected List<Genre> getData() {
		return genreDAO.getGenres();
	}

	@Override
	protected Genre getData(final Integer id) {
		return genreDAO.getGenre(id);
	}

	/**
	 * Returns list of genres.
	 *
	 * @param cached true if returned data from DAO should be cached
	 * @return list of genres
	 */
	private List<Genre> getCachedGenres(final boolean cached) {
		return getCachedObjects(genreCache, GENRES_CACHE_KEY, cached);
	}

	/**
	 * Returns genre with ID or null if there isn't such genre.
	 *
	 * @param id ID
	 * @return genre with ID or null if there isn't such genre
	 */
	private Genre getCachedGenre(final Integer id) {
		return getCachedObject(genreCache, GENRE_CACHE_KEY, id, true);
	}

}
