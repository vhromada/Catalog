package cz.vhromada.catalog.facade.converters;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.dao.entities.Genre;
import cz.vhromada.catalog.dao.entities.Medium;
import cz.vhromada.catalog.dao.entities.Movie;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.to.MovieTO;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A class represents converter from entity movie to TO for movie.
 *
 * @author Vladimir Hromada
 */
@Component("movieToMovieTOConverter")
public class MovieToMovieTOConverter implements Converter<Movie, MovieTO> {

    /** Converter from entity medium to integer */
    private MediumToIntegerConverter mediumConverter;

    /** Converter from entity genre to TO for genre */
    private GenreToGenreTOConverter genreConverter;

    /**
     * Creates a new instance of MovieToMovieTOConverter.
     *
     * @param mediumConverter converter from entity medium to integer
     * @param genreConverter converter from entity genre to TO for genre
     * @throws IllegalArgumentException if converter from entity medium to integer is null
     *                                  or converter from entity genre to TO for genre is null
     */
    @Autowired
    public MovieToMovieTOConverter(final MediumToIntegerConverter mediumConverter,
            final GenreToGenreTOConverter genreConverter) {
        Validators.validateArgumentNotNull(mediumConverter, "Medium converter");
        Validators.validateArgumentNotNull(genreConverter, "Genre converter");

        this.mediumConverter = mediumConverter;
        this.genreConverter = genreConverter;
    }

    @Override
    public MovieTO convert(final Movie source) {
        if (source == null) {
            return null;
        }

        final MovieTO movie = new MovieTO();
        movie.setId(source.getId());
        movie.setCzechName(source.getCzechName());
        movie.setOriginalName(source.getOriginalName());
        movie.setYear(source.getYear());
        movie.setLanguage(source.getLanguage());
        movie.setSubtitles(source.getSubtitles());
        movie.setCsfd(source.getCsfd());
        movie.setImdbCode(source.getImdbCode());
        movie.setWikiEn(source.getWikiEn());
        movie.setWikiCz(source.getWikiCz());
        movie.setPicture(source.getPicture());
        movie.setNote(source.getNote());
        movie.setPosition(source.getPosition());
        final List<Integer> media = new ArrayList<>();
        if (source.getMedia() != null) {
            for (final Medium medium : source.getMedia()) {
                media.add(mediumConverter.convert(medium));
            }
        }
        movie.setMedia(media);
        final List<GenreTO> genres = new ArrayList<>();
        if (source.getGenres() != null) {
            for (final Genre genre : source.getGenres()) {
                genres.add(genreConverter.convert(genre));
            }
        }
        movie.setGenres(genres);
        return movie;
    }

}
