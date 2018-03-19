package cz.vhromada.catalog.facade.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cz.vhromada.catalog.entity.Picture;
import cz.vhromada.catalog.facade.CatalogParentFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.PictureUtils;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.converter.Converter;

import org.junit.jupiter.api.Test;

/**
 * A class represents test for class {@link PictureFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
class PictureFacadeImplTest extends AbstractParentFacadeTest<Picture, cz.vhromada.catalog.domain.Picture> {

    /**
     * Test method for {@link PictureFacadeImpl#PictureFacadeImpl(CatalogService, Converter, CatalogValidator)} with null service for pictures.
     */
    @Test
    void constructor_NullPictureService() {
        assertThatThrownBy(() -> new PictureFacadeImpl(null, getConverter(), getCatalogValidator())).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link PictureFacadeImpl#PictureFacadeImpl(CatalogService, Converter, CatalogValidator)} with null converter.
     */
    @Test
    void constructor_NullConverter() {
        assertThatThrownBy(() -> new PictureFacadeImpl(getCatalogService(), null, getCatalogValidator())).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link PictureFacadeImpl#PictureFacadeImpl(CatalogService, Converter, CatalogValidator)} with null validator for picture.
     */
    @Test
    void constructor_NullPictureValidator() {
        assertThatThrownBy(() -> new PictureFacadeImpl(getCatalogService(), getConverter(), null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Override
    protected CatalogParentFacade<Picture> getCatalogParentFacade() {
        return new PictureFacadeImpl(getCatalogService(), getConverter(), getCatalogValidator());
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
