package com.github.vhromada.catalog.repository

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.utils.AuditUtils
import com.github.vhromada.catalog.utils.PictureUtils
import com.github.vhromada.catalog.utils.TestConstants
import com.github.vhromada.catalog.utils.fillAudit
import com.github.vhromada.catalog.utils.updated
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

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
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [PictureRepository]
     */
    @Autowired
    private lateinit var repository: PictureRepository

    /**
     * Test method for get pictures.
     */
    @Test
    fun getPictures() {
        val pictures = repository.findAll()

        PictureUtils.assertDomainPicturesDeepEquals(expected = PictureUtils.getPictures(), actual = pictures)

        assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
    }

    /**
     * Test method for get picture.
     */
    @Test
    fun getPicture() {
        for (i in 1..PictureUtils.PICTURES_COUNT) {
            val picture = repository.findById(i).orElse(null)

            PictureUtils.assertPictureDeepEquals(expected = PictureUtils.getPictureDomain(index = i), actual = picture)
        }

        assertThat(repository.findById(Int.MAX_VALUE)).isNotPresent

        assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
    }

    /**
     * Test method for add picture.
     */
    @Test
    fun add() {
        val picture = PictureUtils.newPictureDomain(id = null)
            .copy(position = PictureUtils.PICTURES_COUNT)
        val expectedPicture = PictureUtils.newPictureDomain(id = PictureUtils.PICTURES_COUNT + 1)
            .fillAudit(audit = AuditUtils.newAudit())

        repository.save(picture)

        assertSoftly {
            it.assertThat(picture.id).isEqualTo(PictureUtils.PICTURES_COUNT + 1)
            it.assertThat(picture.createdUser).isEqualTo(TestConstants.ACCOUNT.uuid!!)
            it.assertThat(picture.createdTime).isEqualTo(TestConstants.TIME)
            it.assertThat(picture.updatedUser).isEqualTo(TestConstants.ACCOUNT.uuid!!)
            it.assertThat(picture.updatedTime).isEqualTo(TestConstants.TIME)
        }

        val addedPicture = PictureUtils.getPicture(entityManager, PictureUtils.PICTURES_COUNT + 1)!!
        assertThat(addedPicture).isNotNull
        PictureUtils.assertPictureDeepEquals(expected = expectedPicture, actual = addedPicture)

        assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT + 1)
    }

    /**
     * Test method for update picture.
     */
    @Test
    fun update() {
        val picture = PictureUtils.updatePicture(entityManager = entityManager, id = 1)
        val expectedPicture = PictureUtils.getPictureDomain(index = 1)
            .updated()
            .copy(position = PictureUtils.POSITION)
            .fillAudit(audit = AuditUtils.updatedAudit())

        repository.saveAndFlush(picture)

        val updatedPicture = PictureUtils.getPicture(entityManager = entityManager, id = 1)
        assertThat(updatedPicture).isNotNull
        PictureUtils.assertPictureDeepEquals(expected = expectedPicture, actual = updatedPicture!!)

        assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
    }

    /**
     * Test method for remove picture.
     */
    @Test
    fun remove() {
        clearReferencedData()

        repository.delete(PictureUtils.getPicture(entityManager = entityManager, id = 1)!!)

        assertThat(PictureUtils.getPicture(entityManager = entityManager, id = 1)).isNull()

        assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT - 1)
    }

    /**
     * Test method for remove all pictures.
     */
    @Test
    fun removeAll() {
        clearReferencedData()

        repository.deleteAll()

        assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(0)
    }

    /**
     * Test method for get pictures for user.
     */
    @Test
    fun findByCreatedUser() {
        val pictures = repository.findByCreatedUser(user = AuditUtils.getAudit().createdUser!!)

        PictureUtils.assertDomainPicturesDeepEquals(expected = PictureUtils.getPictures(), actual = pictures)

        assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
    }

    /**
     * Test method for get picture by id for user.
     */
    @Test
    fun findByIdAndCreatedUser() {
        val user = AuditUtils.getAudit().createdUser!!
        for (i in 1..PictureUtils.PICTURES_COUNT) {
            val author = repository.findByIdAndCreatedUser(id = i, user = user).orElse(null)

            PictureUtils.assertPictureDeepEquals(expected = PictureUtils.getPictureDomain(index = i), actual = author)
        }

        assertThat(repository.findByIdAndCreatedUser(id = Int.MAX_VALUE, user = user)).isNotPresent

        assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
    }

    /**
     * Clears referenced data.
     */
    @Suppress("SqlDialectInspection", "SqlNoDataSourceInspection")
    private fun clearReferencedData() {
        entityManager.createNativeQuery("UPDATE movies SET picture = NULL").executeUpdate()
        entityManager.createNativeQuery("UPDATE tv_shows SET picture = NULL").executeUpdate()
        entityManager.flush()
    }

}
