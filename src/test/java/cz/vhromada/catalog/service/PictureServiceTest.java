package cz.vhromada.catalog.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cz.vhromada.catalog.domain.Picture;
import cz.vhromada.catalog.repository.PictureRepository;
import cz.vhromada.catalog.utils.PictureUtils;
import cz.vhromada.common.repository.MovableRepository;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.test.service.MovableServiceTest;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.cache.Cache;

/**
 * A class represents test for class {@link PictureService}.
 *
 * @author Vladimir Hromada
 */
class PictureServiceTest extends MovableServiceTest<Picture> {

    /**
     * Instance of {@link PictureRepository}
     */
    @Mock
    private PictureRepository repository;

    /**
     * Test method for {@link PictureService#PictureService(PictureRepository, Cache)} with null repository for pictures.
     */
    @Test
    void constructor_NullPictureRepository() {
        assertThatThrownBy(() -> new PictureService(null, getCache())).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link PictureService#PictureService(PictureRepository, Cache)} with null cache.
     */
    @Test
    void constructor_NullCache() {
        assertThatThrownBy(() -> new PictureService(repository, null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Override
    protected MovableRepository<Picture> getRepository() {
        return repository;
    }

    @Override
    protected MovableService<Picture> getService() {
        return new PictureService(repository, getCache());
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
