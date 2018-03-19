package cz.vhromada.catalog.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cz.vhromada.catalog.domain.Picture;
import cz.vhromada.catalog.repository.PictureRepository;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.PictureUtils;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.cache.Cache;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A class represents test for class {@link PictureServiceImpl}.
 *
 * @author Vladimir Hromada
 */
class PictureServiceImplTest extends AbstractServiceTest<Picture> {

    /**
     * Instance of {@link PictureRepository}
     */
    @Mock
    private PictureRepository pictureRepository;

    /**
     * Test method for {@link PictureServiceImpl#PictureServiceImpl(PictureRepository, Cache)} with null repository for pictures.
     */
    @Test
    void constructor_NullPictureRepository() {
        assertThatThrownBy(() -> new PictureServiceImpl(null, getCache())).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link PictureServiceImpl#PictureServiceImpl(PictureRepository, Cache)} with null cache.
     */
    @Test
    void constructor_NullCache() {
        assertThatThrownBy(() -> new PictureServiceImpl(pictureRepository, null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Override
    protected JpaRepository<Picture, Integer> getRepository() {
        return pictureRepository;
    }

    @Override
    protected CatalogService<Picture> getCatalogService() {
        return new PictureServiceImpl(pictureRepository, getCache());
    }

    @Override
    protected String getCacheKey() {
        return "pictures";
    }

    @Override
    protected Picture getItem1() {
        return PictureUtils.newPictureDomain(1);
    }

    @Override
    protected Picture getItem2() {
        return PictureUtils.newPictureDomain(2);
    }

    @Override
    protected Picture getAddItem() {
        return PictureUtils.newPictureDomain(null);
    }

    @Override
    protected Picture getCopyItem() {
        final Picture picture = PictureUtils.newPictureDomain(null);
        picture.setPosition(0);

        return picture;
    }

    @Override
    protected Class<Picture> getItemClass() {
        return Picture.class;
    }

    @Override
    protected void assertDataDeepEquals(final Picture expected, final Picture actual) {
        PictureUtils.assertPictureDeepEquals(expected, actual);
    }

}
