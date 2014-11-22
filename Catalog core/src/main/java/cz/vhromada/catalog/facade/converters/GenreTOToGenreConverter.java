package cz.vhromada.catalog.facade.converters;

import cz.vhromada.catalog.dao.entities.Genre;
import cz.vhromada.catalog.facade.to.GenreTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A class represents converter from TO for genre to entity genre.
 *
 * @author Vladimir Hromada
 */
@Component("genreTOToGenreConverter")
public class GenreTOToGenreConverter implements Converter<GenreTO, Genre> {

    @Override
    public Genre convert(final GenreTO source) {
        if (source == null) {
            return null;
        }

        final Genre genre = new Genre();
        genre.setId(source.getId());
        genre.setName(source.getName());
        return genre;
    }

}
