package cz.vhromada.catalog.facade.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cz.vhromada.catalog.entity.Picture;
import cz.vhromada.catalog.utils.PictureUtils;
import cz.vhromada.common.facade.MovableParentFacade;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.test.facade.MovableParentFacadeTest;
import cz.vhromada.common.validator.MovableValidator;
import cz.vhromada.converter.Converter;

import org.junit.jupiter.api.Test;

/**
 * A class represents test for class {@link PictureFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
class PictureFacadeImplTest extends MovableParentFacadeTest<Picture, cz.vhromada.catalog.domain.Picture> {

    /**
     * Test method for {@link PictureFacadeImpl#PictureFacadeImpl(MovableService, Converter, MovableValidator)} with null service for pictures.
     */
    @Test
    void constructor_NullPictureService() {
        assertThatThrownBy(() -> new PictureFacadeImpl(null, getConverter(), getMovableValidator())).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link PictureFacadeImpl#PictureFacadeImpl(MovableService, Converter, MovableValidator)} with null converter.
     */
    @Test
    void constructor_NullConverter() {
        assertThatThrownBy(() -> new PictureFacadeImpl(getMovableService(), null, getMovableValidator())).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link PictureFacadeImpl#PictureFacadeImpl(MovableService, Converter, MovableValidator)} with null validator for picture.
     */
    @Test
    void constructor_NullPictureValidator() {
        assertThatThrownBy(() -> new PictureFacadeImpl(getMovableService(), getConverter(), null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Override
    protected MovableParentFacade<Picture> getMovableParentFacade() {
        return new PictureFacadeImpl(getMovableService(), getConverter(), getMovableValidator());
    }

    @Override
    protected Picture newEntity(final Integer id) {
        return PictureUtils.newPicture(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Picture newDomain(final Integer id) {
        return PictureUtils.newPictureDomain(id);
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
