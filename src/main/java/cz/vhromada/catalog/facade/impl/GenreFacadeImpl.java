package cz.vhromada.catalog.facade.impl;

import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.facade.GenreFacade;
import cz.vhromada.common.converter.MovableConverter;
import cz.vhromada.common.facade.AbstractMovableParentFacade;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.validator.MovableValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of facade for genres.
 *
 * @author Vladimir Hromada
 */
@Component("genreFacade")
public class GenreFacadeImpl extends AbstractMovableParentFacade<Genre, cz.vhromada.catalog.domain.Genre> implements GenreFacade {

    /**
     * Creates a new instance of GenreFacadeImpl.
     *
     * @param genreService   service for genres
     * @param converter      converter for genres
     * @param genreValidator validator for genre
     * @throws IllegalArgumentException if service for genres is null
     *                                  or converter for genres is null
     *                                  or validator for genre is null
     */
    @Autowired
    public GenreFacadeImpl(final MovableService<cz.vhromada.catalog.domain.Genre> genreService,
        final MovableConverter<Genre, cz.vhromada.catalog.domain.Genre> converter, final MovableValidator<Genre> genreValidator) {
        super(genreService, converter, genreValidator);
    }

}
