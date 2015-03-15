package cz.vhromada.catalog.service.impl;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.MovieDAO;
import cz.vhromada.catalog.dao.entities.Medium;
import cz.vhromada.catalog.dao.entities.Movie;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.catalog.service.MovieService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.validators.Validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of service for movies.
 *
 * @author Vladimir Hromada
 */
@Component("movieService")
public class MovieServiceImpl extends AbstractService<Movie> implements MovieService {

    /**
     * DAO for movies field
     */
    private static final String MOVIE_DAO_ARGUMENT = "DAO for movies";

    /**
     * Cache for movies field
     */
    private static final String MOVIE_CACHE_ARGUMENT = "Cache for movies";

    /**
     * Movie argument
     */
    private static final String MOVIE_ARGUMENT = "Movie";

    /**
     * ID argument
     */
    private static final String ID_ARGUMENT = "ID";

    /**
     * Message for {@link ServiceOperationException}
     */
    private static final String SERVICE_OPERATION_EXCEPTION_MESSAGE = "Error in working with DAO tier.";

    /**
     * Cache key for list of movies
     */
    private static final String MOVIES_CACHE_KEY = "movies";

    /**
     * Cache key for movie
     */
    private static final String MOVIE_CACHE_KEY = "movie";

    /**
     * DAO for movies
     */
    private MovieDAO movieDAO;

    /**
     * Cache for movies
     */
    private Cache movieCache;

    /**
     * Creates a new instance of MovieServiceImpl.
     *
     * @param movieDAO   DAO for movies
     * @param movieCache cache for movies
     * @throws IllegalArgumentException if DAO for movies is null
     *                                  or cache for movies is null
     */
    @Autowired
    public MovieServiceImpl(final MovieDAO movieDAO,
            @Value("#{cacheManager.getCache('movieCache')}") final Cache movieCache) {
        Validators.validateArgumentNotNull(movieDAO, MOVIE_DAO_ARGUMENT);
        Validators.validateArgumentNotNull(movieCache, MOVIE_CACHE_ARGUMENT);

        this.movieDAO = movieDAO;
        this.movieCache = movieCache;
    }

    /**
     * {@inheritDoc}
     *
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void newData() {
        try {
            for (final Movie movie : getCachedMovies(false)) {
                movieDAO.remove(movie);
            }
            movieCache.clear();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public List<Movie> getMovies() {
        try {
            return getCachedMovies(true);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public Movie getMovie(final Integer id) {
        Validators.validateArgumentNotNull(id, ID_ARGUMENT);

        try {
            return getCachedMovie(id, true);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void add(final Movie movie) {
        Validators.validateArgumentNotNull(movie, MOVIE_ARGUMENT);

        try {
            movieDAO.add(movie);
            addMovieToCache(movie);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void update(final Movie movie) {
        Validators.validateArgumentNotNull(movie, MOVIE_ARGUMENT);

        try {
            movieDAO.update(movie);
            movieCache.clear();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void remove(final Movie movie) {
        Validators.validateArgumentNotNull(movie, MOVIE_ARGUMENT);

        try {
            movieDAO.remove(movie);
            removeObjectFromCache(movieCache, MOVIES_CACHE_KEY, movie);
            movieCache.evict(movie.getId());
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void duplicate(final Movie movie) {
        Validators.validateArgumentNotNull(movie, MOVIE_ARGUMENT);

        try {
            final Movie newMovie = new Movie();
            newMovie.setCzechName(movie.getCzechName());
            newMovie.setOriginalName(movie.getOriginalName());
            newMovie.setYear(movie.getYear());
            newMovie.setLanguage(movie.getLanguage());
            newMovie.setSubtitles(new ArrayList<>(movie.getSubtitles()));
            newMovie.setMedia(createMedia(movie.getMedia()));
            newMovie.setCsfd(movie.getCsfd());
            newMovie.setImdbCode(movie.getImdbCode());
            newMovie.setWikiEn(movie.getWikiEn());
            newMovie.setWikiCz(movie.getWikiCz());
            newMovie.setPicture(movie.getPicture());
            newMovie.setNote(movie.getNote());
            newMovie.setGenres(new ArrayList<>(movie.getGenres()));
            movieDAO.add(newMovie);
            newMovie.setPosition(movie.getPosition());
            movieDAO.update(newMovie);
            addMovieToCache(newMovie);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void moveUp(final Movie movie) {
        Validators.validateArgumentNotNull(movie, MOVIE_ARGUMENT);

        try {
            final List<Movie> movies = getCachedMovies(false);
            final Movie otherMovie = movies.get(movies.indexOf(movie) - 1);
            switchPosition(movie, otherMovie);
            movieDAO.update(movie);
            movieDAO.update(otherMovie);
            movieCache.clear();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void moveDown(final Movie movie) {
        Validators.validateArgumentNotNull(movie, MOVIE_ARGUMENT);

        try {
            final List<Movie> movies = getCachedMovies(false);
            final Movie otherMovie = movies.get(movies.indexOf(movie) + 1);
            switchPosition(movie, otherMovie);
            movieDAO.update(movie);
            movieDAO.update(otherMovie);
            movieCache.clear();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public boolean exists(final Movie movie) {
        Validators.validateArgumentNotNull(movie, MOVIE_ARGUMENT);

        try {
            return getCachedMovie(movie.getId(), true) != null;
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void updatePositions() {
        try {
            final List<Movie> movies = getCachedMovies(false);
            for (int i = 0; i < movies.size(); i++) {
                final Movie movie = movies.get(i);
                movie.setPosition(i);
                movieDAO.update(movie);
            }
            movieCache.clear();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public int getTotalMediaCount() {
        try {
            final List<Movie> movies = getCachedMovies(true);
            int sum = 0;
            for (final Movie movie : movies) {
                sum += movie.getMedia().size();
            }
            return sum;
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public Time getTotalLength() {
        try {
            final List<Movie> movies = getCachedMovies(true);
            int sum = 0;
            for (final Movie movie : movies) {
                for (final Medium medium : movie.getMedia()) {
                    sum += medium.getLength();
                }
            }
            return new Time(sum);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    @Override
    protected List<Movie> getData() {
        return movieDAO.getMovies();
    }

    @Override
    protected Movie getData(final Integer id) {
        return movieDAO.getMovie(id);
    }

    /**
     * Returns list of movies.
     *
     * @param cached true if returned data from DAO should be cached
     * @return list of movies
     */
    private List<Movie> getCachedMovies(final boolean cached) {
        return getCachedObjects(movieCache, MOVIES_CACHE_KEY, cached);
    }

    /**
     * Returns movie with ID or null if there isn't such movie.
     *
     * @param id     ID
     * @param cached true if returned data from DAO should be cached
     * @return movie with ID or null if there isn't such movie
     */
    private Movie getCachedMovie(final Integer id, final boolean cached) {
        return getCachedObject(movieCache, MOVIE_CACHE_KEY, id, cached);
    }

    /**
     * Adds movie to cache.
     *
     * @param movie movie
     */
    private void addMovieToCache(final Movie movie) {
        addObjectToListCache(movieCache, MOVIES_CACHE_KEY, movie);
        addObjectToCache(movieCache, MOVIE_CACHE_KEY + movie.getId(), movie);
    }

    /**
     * Creates new copy of media from another media.
     *
     * @param media media
     * @return new copy of media
     */
    private static List<Medium> createMedia(final List<Medium> media) {
        final List<Medium> newMedia = new ArrayList<>();
        for (final Medium medium : media) {
            final Medium newMedium = new Medium();
            newMedium.setNumber(medium.getNumber());
            newMedium.setLength(medium.getLength());
            newMedia.add(newMedium);
        }
        return newMedia;
    }

    /**
     * Switch position of movies.
     *
     * @param movie1 1st movie
     * @param movie2 2nd movie
     */
    private static void switchPosition(final Movie movie1, final Movie movie2) {
        final int position = movie1.getPosition();
        movie1.setPosition(movie2.getPosition());
        movie2.setPosition(position);
    }

}
