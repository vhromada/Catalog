package cz.vhromada.catalog.service;

import java.util.ArrayList;
import java.util.stream.Collectors;

import cz.vhromada.catalog.domain.Medium;
import cz.vhromada.catalog.domain.Movie;
import cz.vhromada.catalog.repository.MovieRepository;
import cz.vhromada.common.service.AbstractMovableService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

/**
 * A class represents service for movies.
 *
 * @author Vladimir Hromada
 */
@Component("movieService")
public class MovieService extends AbstractMovableService<Movie> {

    /**
     * Creates a new instance of MovieService.
     *
     * @param movieRepository repository for movies
     * @param cache           cache
     * @throws IllegalArgumentException if repository for movies is null
     *                                  or cache is null
     */
    @Autowired
    public MovieService(final MovieRepository movieRepository, @Value("#{cacheManager.getCache('catalogCache')}") final Cache cache) {
        super(movieRepository, cache, "movies");
    }

    @Override
    protected Movie getCopy(final Movie data) {
        final Movie newMovie = new Movie();
        newMovie.setCzechName(data.getCzechName());
        newMovie.setOriginalName(data.getOriginalName());
        newMovie.setYear(data.getYear());
        newMovie.setLanguage(data.getLanguage());
        newMovie.setSubtitles(new ArrayList<>(data.getSubtitles()));
        newMovie.setCsfd(data.getCsfd());
        newMovie.setImdbCode(data.getImdbCode());
        newMovie.setWikiEn(data.getWikiEn());
        newMovie.setWikiCz(data.getWikiCz());
        newMovie.setPicture(data.getPicture());
        newMovie.setNote(data.getNote());
        newMovie.setGenres(new ArrayList<>(data.getGenres()));
        newMovie.setPosition(data.getPosition());
        newMovie.setMedia(data.getMedia().stream().map(MovieService::duplicateMedium).collect(Collectors.toList()));

        return newMovie;
    }

    /**
     * Duplicates medium.
     *
     * @param medium medium for duplication
     * @return duplicated medium
     */
    private static Medium duplicateMedium(final Medium medium) {
        final Medium newMedium = new Medium();
        newMedium.setNumber(medium.getNumber());
        newMedium.setLength(medium.getLength());

        return newMedium;
    }

}
