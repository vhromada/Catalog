package cz.vhromada.catalog.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.domain.Picture;
import cz.vhromada.catalog.utils.PictureUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents integration test for class {@link PictureRepository}.
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
@Transactional
@Rollback
class PictureRepositoryIntegrationTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private EntityManager entityManager;

    /**
     * Instance of {@link PictureRepository}
     */
    @Autowired
    private PictureRepository pictureRepository;

    /**
     * Test method for get pictures.
     */
    @Test
    void getPictures() {
        final List<Picture> pictures = pictureRepository.findAll();

        PictureUtils.assertPicturesDeepEquals(PictureUtils.getPictures(), pictures);

        assertThat(PictureUtils.getPicturesCount(entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT);
    }

    /**
     * Test method for get picture.
     */
    @Test
    void getPicture() {
        for (int i = 1; i <= PictureUtils.PICTURES_COUNT; i++) {
            final Picture picture = pictureRepository.findById(i).orElse(null);

            PictureUtils.assertPictureDeepEquals(PictureUtils.getPicture(i), picture);
        }

        assertThat(pictureRepository.findById(Integer.MAX_VALUE).isPresent()).isFalse();

        assertThat(PictureUtils.getPicturesCount(entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT);
    }

    /**
     * Test method for add picture.
     */
    @Test
    void add() {
        final Picture picture = PictureUtils.newPictureDomain(null);

        pictureRepository.save(picture);

        assertThat(picture.getId()).isEqualTo(PictureUtils.PICTURES_COUNT + 1);

        final Picture addedPicture = PictureUtils.getPicture(entityManager, PictureUtils.PICTURES_COUNT + 1);
        final Picture expectedAddPicture = PictureUtils.newPictureDomain(null);
        expectedAddPicture.setId(PictureUtils.PICTURES_COUNT + 1);
        PictureUtils.assertPictureDeepEquals(expectedAddPicture, addedPicture);

        assertThat(PictureUtils.getPicturesCount(entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT + 1);
    }

    /**
     * Test method for update picture.
     */
    @Test
    void update() {
        final Picture picture = PictureUtils.updatePicture(entityManager, 1);

        pictureRepository.save(picture);

        final Picture updatedPicture = PictureUtils.getPicture(entityManager, 1);
        final Picture expectedUpdatedPicture = PictureUtils.getPicture(1);
        PictureUtils.updatePicture(expectedUpdatedPicture);
        expectedUpdatedPicture.setPosition(PictureUtils.POSITION);
        PictureUtils.assertPictureDeepEquals(expectedUpdatedPicture, updatedPicture);

        assertThat(PictureUtils.getPicturesCount(entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT);
    }

    /**
     * Test method for remove picture.
     */
    @Test
    @DirtiesContext
    void remove() {
        final Picture picture = PictureUtils.newPictureDomain(null);
        entityManager.persist(picture);
        assertThat(PictureUtils.getPicturesCount(entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT + 1);

        pictureRepository.delete(picture);

        assertThat(PictureUtils.getPicture(entityManager, picture.getId())).isNull();

        assertThat(PictureUtils.getPicturesCount(entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT);
    }

    /**
     * Test method for remove all pictures.
     */
    @Test
    void removeAll() {
        entityManager.createNativeQuery("UPDATE movies SET picture = NULL").executeUpdate();
        entityManager.createNativeQuery("UPDATE tv_shows SET picture = NULL").executeUpdate();

        pictureRepository.deleteAll();

        assertThat(PictureUtils.getPicturesCount(entityManager)).isEqualTo(0);
    }

}
