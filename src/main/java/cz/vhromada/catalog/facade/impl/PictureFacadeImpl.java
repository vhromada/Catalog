package cz.vhromada.catalog.facade.impl;

import cz.vhromada.catalog.entity.Picture;
import cz.vhromada.catalog.facade.PictureFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.converter.Converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of facade for pictures.
 *
 * @author Vladimir Hromada
 */
@Component("pictureFacade")
public class PictureFacadeImpl extends AbstractCatalogParentFacade<Picture, cz.vhromada.catalog.domain.Picture> implements PictureFacade {

    /**
     * Creates a new instance of PictureFacadeImpl.
     *
     * @param pictureService   service for pictures
     * @param converter        converter
     * @param pictureValidator validator for picture
     * @throws IllegalArgumentException if service for pictures is null
     *                                  or converter is null
     *                                  or validator for picture is null
     */
    @Autowired
    public PictureFacadeImpl(final CatalogService<cz.vhromada.catalog.domain.Picture> pictureService,
        final Converter converter,
        final CatalogValidator<Picture> pictureValidator) {
        super(pictureService, converter, pictureValidator);
    }

    @Override
    protected Class<Picture> getEntityClass() {
        return Picture.class;
    }

    @Override
    protected Class<cz.vhromada.catalog.domain.Picture> getDomainClass() {
        return cz.vhromada.catalog.domain.Picture.class;
    }

}
