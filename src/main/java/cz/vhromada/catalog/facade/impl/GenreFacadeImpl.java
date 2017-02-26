package cz.vhromada.catalog.facade.impl;

import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.facade.GenreFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.converters.Converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of facade for genres.
 *
 * @author Vladimir Hromada
 */
@Component("genreFacade")
public class GenreFacadeImpl extends AbstractParentCatalogFacade<Genre, cz.vhromada.catalog.domain.Genre> implements GenreFacade {

    /**
     * Creates a new instance of GenreFacadeImpl.
     *
     * @param genreService   service for genres
     * @param converter      converter
     * @param genreValidator validator for genre
     * @throws IllegalArgumentException if service for genres is null
     *                                  or converter is null
     *                                  or validator for genre is null
     */
    @Autowired
    public GenreFacadeImpl(final CatalogService<cz.vhromada.catalog.domain.Genre> genreService,
            final Converter converter,
            final CatalogValidator<Genre> genreValidator) {
        super(genreService, converter, genreValidator);
    }

    @Override
    protected Class<Genre> getEntityClass() {
        return Genre.class;
    }

    @Override
    protected Class<cz.vhromada.catalog.domain.Genre> getDomainClass() {
        return cz.vhromada.catalog.domain.Genre.class;
    }

}
