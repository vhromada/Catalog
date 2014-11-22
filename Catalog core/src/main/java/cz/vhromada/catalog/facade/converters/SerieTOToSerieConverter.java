package cz.vhromada.catalog.facade.converters;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.dao.entities.Genre;
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.to.SerieTO;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A class represents converter from TO for serie to entity serie.
 *
 * @author Vladimir Hromada
 */
@Component("serieTOToSerieConverter")
public class SerieTOToSerieConverter implements Converter<SerieTO, Serie> {

    /** Converter from TO for genre to entity genre */
    @Autowired
    private GenreTOToGenreConverter converter;

    /**
     * Returns converter from TO for genre to entity genre.
     *
     * @return converter from TO for genre to entity genre
     */
    public GenreTOToGenreConverter getConverter() {
        return converter;
    }

    /**
     * Sets a new value to converter from TO for genre to entity genre.
     *
     * @param converter new value
     */
    public void setConverter(final GenreTOToGenreConverter converter) {
        this.converter = converter;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws IllegalStateException    if converter from TO for genre to entity genre isn't set
     */
    @Override
    public Serie convert(final SerieTO source) {
        Validators.validateFieldNotNull(converter, "Converter");

        if (source == null) {
            return null;
        }

        final Serie serie = new Serie();
        serie.setId(source.getId());
        serie.setCzechName(source.getCzechName());
        serie.setOriginalName(source.getOriginalName());
        serie.setCsfd(source.getCsfd());
        serie.setImdbCode(source.getImdbCode());
        serie.setWikiEn(source.getWikiEn());
        serie.setWikiCz(source.getWikiCz());
        serie.setPicture(source.getPicture());
        serie.setNote(source.getNote());
        serie.setPosition(source.getPosition());
        final List<Genre> genres = new ArrayList<>();
        if (source.getGenres() != null) {
            for (final GenreTO genre : source.getGenres()) {
                genres.add(converter.convert(genre));
            }
        }
        serie.setGenres(genres);
        return serie;
    }

}
