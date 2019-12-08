package cz.vhromada.catalog.repository

import cz.vhromada.catalog.CatalogTestConfiguration
import cz.vhromada.catalog.utils.PictureUtils
import cz.vhromada.catalog.utils.updated
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

/**
 * A class represents integration test for class [PictureRepository].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
@Transactional
@Rollback
class PictureRepositoryIntegrationTest {

    /**
     * Instance of [EntityManager]
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [PictureRepository]
     */
    @Autowired
    private lateinit var pictureRepository: PictureRepository

    /**
     * Test method for get pictures.
     */
    @Test
    fun getPictures() {
        val pictures = pictureRepository.findAll()

        PictureUtils.assertPicturesDeepEquals(PictureUtils.getPictures(), pictures)

        assertThat(PictureUtils.getPicturesCount(entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
    }

    /**
     * Test method for get picture.
     */
    @Test
    fun getPicture() {
        for (i in 1..PictureUtils.PICTURES_COUNT) {
            val picture = pictureRepository.findById(i).orElse(null)

            PictureUtils.assertPictureDeepEquals(PictureUtils.getPicture(i), picture)
        }

        assertThat(pictureRepository.findById(Integer.MAX_VALUE).isPresent).isFalse()

        assertThat(PictureUtils.getPicturesCount(entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
    }

    /**
     * Test method for add picture.
     */
    @Test
    fun add() {
        val picture = PictureUtils.newPictureDomain(null)
                .copy(position = PictureUtils.PICTURES_COUNT)

        pictureRepository.save(picture)

        assertThat(picture.id).isEqualTo(PictureUtils.PICTURES_COUNT + 1)

        val addedPicture = PictureUtils.getPicture(entityManager, PictureUtils.PICTURES_COUNT + 1)!!
        val expectedAddPicture = PictureUtils.newPictureDomain(null)
                .copy(id = PictureUtils.PICTURES_COUNT + 1, position = PictureUtils.PICTURES_COUNT)
        PictureUtils.assertPictureDeepEquals(expectedAddPicture, addedPicture)

        assertThat(PictureUtils.getPicturesCount(entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT + 1)
    }

    /**
     * Test method for update picture.
     */
    @Test
    fun update() {
        val picture = PictureUtils.updatePicture(entityManager, 1)

        pictureRepository.save(picture)

        val updatedPicture = PictureUtils.getPicture(entityManager, 1)!!
        val expectedUpdatedPicture = PictureUtils.getPicture(1)
                .updated()
                .copy(position = PictureUtils.POSITION)
        PictureUtils.assertPictureDeepEquals(expectedUpdatedPicture, updatedPicture)

        assertThat(PictureUtils.getPicturesCount(entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
    }

    /**
     * Test method for remove picture.
     */
    @Test
    @DirtiesContext
    fun remove() {
        val picture = PictureUtils.newPictureDomain(null)
                .copy(position = PictureUtils.PICTURES_COUNT)
        entityManager.persist(picture)
        assertThat(PictureUtils.getPicturesCount(entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT + 1)

        pictureRepository.delete(picture)

        assertThat(PictureUtils.getPicture(entityManager, picture.id!!)).isNull()

        assertThat(PictureUtils.getPicturesCount(entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
    }

    /**
     * Test method for remove all pictures.
     */
    @Test
    fun removeAll() {
        entityManager.createNativeQuery("UPDATE movies SET picture = NULL").executeUpdate()
        entityManager.createNativeQuery("UPDATE tv_shows SET picture = NULL").executeUpdate()

        pictureRepository.deleteAll()

        assertThat(PictureUtils.getPicturesCount(entityManager)).isEqualTo(0)
    }

}
