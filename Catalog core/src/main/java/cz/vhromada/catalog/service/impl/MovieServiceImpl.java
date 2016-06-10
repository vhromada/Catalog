package cz.vhromada.catalog.service.impl;

import java.util.ArrayList;
import java.util.stream.Collectors;

import cz.vhromada.catalog.entities.Medium;
import cz.vhromada.catalog.entities.Movie;
import cz.vhromada.catalog.repository.MovieRepository;

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
public class MovieServiceImpl extends AbstractCatalogService<Movie> {

    /**
     * Creates a new instance of MovieServiceImpl.
     *
     * @param movieRepository repository for movies
     * @param cache           cache
     * @throws IllegalArgumentException if repository for movies is null
     *                                  or cache is null
     */
    @Autowired
    public MovieServiceImpl(final MovieRepository movieRepository,
            @Value("#{cacheManager.getCache('catalogCache')}") final Cache cache) {
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
        newMovie.setMedia(data.getMedia().stream().map(MovieServiceImpl::duplicateMedium).collect(Collectors.toList()));

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
