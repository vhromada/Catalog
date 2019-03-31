package cz.vhromada.catalog.facade.impl;

import cz.vhromada.catalog.entity.Picture;
import cz.vhromada.catalog.facade.PictureFacade;
import cz.vhromada.common.converter.MovableConverter;
import cz.vhromada.common.facade.AbstractMovableParentFacade;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.validator.MovableValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of facade for pictures.
 *
 * @author Vladimir Hromada
 */
@Component("pictureFacade")
public class PictureFacadeImpl extends AbstractMovableParentFacade<Picture, cz.vhromada.catalog.domain.Picture> implements PictureFacade {

    /**
     * Creates a new instance of PictureFacadeImpl.
     *
     * @param pictureService   service for pictures
     * @param converter        converter for pictures
     * @param pictureValidator validator for picture
     * @throws IllegalArgumentException if service for pictures is null
     *                                  or converter for pictures is null
     *                                  or validator for picture is null
     */
    @Autowired
    public PictureFacadeImpl(final MovableService<cz.vhromada.catalog.domain.Picture> pictureService,
        final MovableConverter<Picture, cz.vhromada.catalog.domain.Picture> converter, final MovableValidator<Picture> pictureValidator) {
        super(pictureService, converter, pictureValidator);
    }

}
