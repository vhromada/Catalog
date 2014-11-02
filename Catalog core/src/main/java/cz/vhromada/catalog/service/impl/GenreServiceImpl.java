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
		Validators.validateFieldNotNull(genreDAO, "DAO for genres");
		Validators.validateFieldNotNull(genreCache, "Cache for genres");

		try {
			for (Genre genre : getCachedGenres(false)) {
				genreDAO.remove(genre);
			}
			genreCache.clear();
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException("Error in working with DAO tier.", ex);
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
		Validators.validateFieldNotNull(genreDAO, "DAO for genres");
		Validators.validateFieldNotNull(genreCache, "Cache for genres");

		try {
			return getCachedGenres(true);
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException("Error in working with DAO tier.", ex);
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
		Validators.validateFieldNotNull(genreDAO, "DAO for genres");
		Validators.validateFieldNotNull(genreCache, "Cache for genres");
		Validators.validateArgumentNotNull(id, "ID");

		try {
			return getCachedGenre(id);
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException("Error in working with DAO tier.", ex);
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
		Validators.validateFieldNotNull(genreDAO, "DAO for genres");
		Validators.validateFieldNotNull(genreCache, "Cache for genres");
		Validators.validateArgumentNotNull(genre, "Genre");

		try {
			genreDAO.add(genre);
			addObjectToListCache(genreCache, "genres", genre);
			addObjectToCache(genreCache, "genre" + genre.getId(), genre);
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException("Error in working with DAO tier.", ex);
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
		Validators.validateFieldNotNull(genreDAO, "DAO for genres");
		Validators.validateFieldNotNull(genreCache, "Cache for genres");
		Validators.validateArgumentNotNull(genres, "List of genre names");

		try {
			for (String genre : genres) {
				final Genre genreEntity = new Genre();
				genreEntity.setName(genre);
				genreDAO.add(genreEntity);
			}
			genreCache.clear();
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException("Error in working with DAO tier.", ex);
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
		Validators.validateFieldNotNull(genreDAO, "DAO for genres");
		Validators.validateFieldNotNull(genreCache, "Cache for genres");
		Validators.validateArgumentNotNull(genre, "Genre");

		try {
			genreDAO.update(genre);
			genreCache.clear();
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException("Error in working with DAO tier.", ex);
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
		Validators.validateFieldNotNull(genreDAO, "DAO for genres");
		Validators.validateFieldNotNull(genreCache, "Cache for genres");
		Validators.validateArgumentNotNull(genre, "Genre");

		try {
			genreDAO.remove(genre);
			removeObjectFromCache(genreCache, "genres", genre);
			genreCache.evict(genre.getId());
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException("Error in working with DAO tier.", ex);
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
		Validators.validateFieldNotNull(genreDAO, "DAO for genres");
		Validators.validateFieldNotNull(genreCache, "Cache for genres");
		Validators.validateArgumentNotNull(genre, "Genre");

		try {
			return getCachedGenre(genre.getId()) != null;
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException("Error in working with DAO tier.", ex);
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
		return getCachedObjects(genreCache, "genres", cached);
	}

	/**
	 * Returns genre with ID or null if there isn't such genre.
	 *
	 * @param id ID
	 * @return genre with ID or null if there isn't such genre
	 */
	private Genre getCachedGenre(final Integer id) {
		return getCachedObject(genreCache, "genre", id, true);
	}

}
