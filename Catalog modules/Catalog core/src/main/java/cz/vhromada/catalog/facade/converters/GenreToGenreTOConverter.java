package cz.vhromada.catalog.facade.converters;

import cz.vhromada.catalog.dao.entities.Genre;
import cz.vhromada.catalog.facade.to.GenreTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A class represents converter from entity genre to TO for genre.
 *
 * @author Vladimir Hromada
 */
@Component("genreToGenreTOConverter")
public class GenreToGenreTOConverter implements Converter<Genre, GenreTO> {

	@Override
	public GenreTO convert(final Genre source) {
		if (source == null) {
			return null;
		}

		final GenreTO genre = new GenreTO();
		genre.setId(source.getId());
		genre.setName(source.getName());
		return genre;
	}

}
