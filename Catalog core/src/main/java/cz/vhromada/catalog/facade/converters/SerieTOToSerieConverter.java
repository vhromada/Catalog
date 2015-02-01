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
    private GenreTOToGenreConverter converter;

    /**
     * Creates a new instance of SerieTOToSerieConverter.
     *
     * @param converter converter from TO for genre to entity genre
     * @throws IllegalArgumentException if converter from TO for genre to entity genree is null
     */
    @Autowired
    public SerieTOToSerieConverter(final GenreTOToGenreConverter converter) {
        Validators.validateArgumentNotNull(converter, "Converter");

        this.converter = converter;
    }

    @Override
    public Serie convert(final SerieTO source) {
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
