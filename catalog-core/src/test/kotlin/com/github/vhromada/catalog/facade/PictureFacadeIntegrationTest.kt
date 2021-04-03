package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.utils.AuditUtils
import com.github.vhromada.catalog.utils.PictureUtils
import com.github.vhromada.catalog.utils.fillAudit
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.result.Status
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * A class represents integration test for class [PictureFacade].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
@Transactional
@Rollback
class PictureFacadeIntegrationTest {

    /**
     * Instance of [EntityManager]
     */
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [PictureFacade]
     */
    @Autowired
    private lateinit var facade: PictureFacade

    /**
     * Test method for [PictureFacade.get].
     */
    @Test
    fun get() {
        for (i in 1..PictureUtils.PICTURES_COUNT) {
            val result = facade.get(id = i)

            assertSoftly {
                it.assertThat(result.status).isEqualTo(Status.OK)
                it.assertThat(result.data).isNotNull
                it.assertThat(result.events()).isEmpty()
            }
            PictureUtils.assertPictureDeepEquals(expected = PictureUtils.getPictureDomain(index = i), actual = result.data!!)
        }

        assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
    }

    /**
     * Test method for [PictureFacade.get] with bad ID.
     */
    @Test
    fun getBadId() {
        val result = facade.get(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(PICTURE_NOT_EXIST_EVENT))
        }

        assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
    }

    /**
     * Test method for [PictureFacade.update].
     */
    @Test
    fun update() {
        val picture = PictureUtils.newPicture(id = 1)
        val expectedPicture = PictureUtils.newPictureDomain(id = 1)
            .fillAudit(audit = AuditUtils.updatedAudit())

        val result = facade.update(data = picture)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        PictureUtils.assertPictureDeepEquals(expected = expectedPicture, actual = PictureUtils.getPicture(entityManager = entityManager, id = 1)!!)

        assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
    }

    /**
     * Test method for [PictureFacade.update] with picture with null ID.
     */
    @Test
    fun updateNullId() {
        val picture = PictureUtils.newPicture(id = 1)
            .copy(id = null)

        val result = facade.update(data = picture)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PICTURE_ID_NULL", message = "ID mustn't be null.")))
        }

        assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
    }

    /**
     * Test method for [PictureFacade.update] with picture with null position.
     */
    @Test
    fun updateNullPosition() {
        val picture = PictureUtils.newPicture(id = 1)
            .copy(position = null)

        val result = facade.update(data = picture)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PICTURE_POSITION_NULL", message = "Position mustn't be null.")))
        }

        assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
    }

    /**
     * Test method for [PictureFacade.update] with picture with null content.
     */
    @Test
    fun updateNullContent() {
        val picture = PictureUtils.newPicture(id = 1)
            .copy(content = null)

        val result = facade.update(data = picture)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PICTURE_CONTENT_NULL", message = "Content mustn't be null.")))
        }

        assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
    }

    /**
     * Test method for [PictureFacade.update] with picture with bad ID.
     */
    @Test
    fun updateBadId() {
        val result = facade.update(data = PictureUtils.newPicture(id = Int.MAX_VALUE))

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(PICTURE_NOT_EXIST_EVENT))
        }

        assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
    }

    /**
     * Test method for [PictureFacade.remove].
     */
    @Test
    fun remove() {
        clearReferencedData()

        val result = facade.remove(id = 1)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        assertThat(PictureUtils.getPicture(entityManager = entityManager, id = 1)).isNull()

        assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT - 1)
    }

    /**
     * Test method for [PictureFacade.remove] with picture with bad ID.
     */
    @Test
    fun removeBadId() {
        val result = facade.remove(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(PICTURE_NOT_EXIST_EVENT))
        }

        assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
    }

    /**
     * Test method for [PictureFacade.duplicate].
     */
    @Test
    @DirtiesContext
    fun duplicate() {
        val expectedPicture = PictureUtils.getPictureDomain(index = PictureUtils.PICTURES_COUNT)
            .copy(id = PictureUtils.PICTURES_COUNT + 1)
            .fillAudit(audit = AuditUtils.newAudit())

        val result = facade.duplicate(id = PictureUtils.PICTURES_COUNT)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        PictureUtils.assertPictureDeepEquals(expected = expectedPicture, actual = PictureUtils.getPicture(entityManager = entityManager, id = PictureUtils.PICTURES_COUNT + 1)!!)

        assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT + 1)
    }

    /**
     * Test method for [PictureFacade.duplicate] with picture with bad ID.
     */
    @Test
    fun duplicateBadId() {
        val result = facade.duplicate(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(PICTURE_NOT_EXIST_EVENT))
        }

        assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
    }

    /**
     * Test method for [PictureFacade.moveUp].
     */
    @Test
    fun moveUp() {
        val result = facade.moveUp(id = 2)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        val picture1 = PictureUtils.getPictureDomain(index = 1)
            .copy(position = 11)
            .fillAudit(audit = AuditUtils.updatedAudit())
        val picture2 = PictureUtils.getPictureDomain(index = 2)
            .copy(position = 10)
            .fillAudit(audit = AuditUtils.updatedAudit())
        PictureUtils.assertPictureDeepEquals(expected = picture1, actual = PictureUtils.getPicture(entityManager = entityManager, id = 1)!!)
        PictureUtils.assertPictureDeepEquals(expected = picture2, actual = PictureUtils.getPicture(entityManager = entityManager, id = 2)!!)
        for (i in 3..PictureUtils.PICTURES_COUNT) {
            PictureUtils.assertPictureDeepEquals(expected = PictureUtils.getPictureDomain(i), actual = PictureUtils.getPicture(entityManager = entityManager, id = i)!!)
        }

        assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
    }

    /**
     * Test method for [PictureFacade.moveUp] with not movable picture.
     */
    @Test
    fun moveUpNotMovable() {
        val result = facade.moveUp(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PICTURE_NOT_MOVABLE", message = "Picture can't be moved up.")))
        }

        assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
    }

    /**
     * Test method for [PictureFacade.moveUp] with picture with bad ID.
     */
    @Test
    fun moveUpBadId() {
        val result = facade.moveUp(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(PICTURE_NOT_EXIST_EVENT))
        }

        assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
    }

    /**
     * Test method for [PictureFacade.moveDown].
     */
    @Test
    fun moveDown() {
        val result = facade.moveDown(id = 1)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        val picture1 = PictureUtils.getPictureDomain(index = 1)
            .copy(position = 11)
            .fillAudit(audit = AuditUtils.updatedAudit())
        val picture2 = PictureUtils.getPictureDomain(index = 2)
            .copy(position = 10)
            .fillAudit(audit = AuditUtils.updatedAudit())
        PictureUtils.assertPictureDeepEquals(expected = picture1, actual = PictureUtils.getPicture(entityManager = entityManager, id = 1)!!)
        PictureUtils.assertPictureDeepEquals(expected = picture2, actual = PictureUtils.getPicture(entityManager = entityManager, id = 2)!!)
        for (i in 3..PictureUtils.PICTURES_COUNT) {
            PictureUtils.assertPictureDeepEquals(expected = PictureUtils.getPictureDomain(i), actual = PictureUtils.getPicture(entityManager = entityManager, id = i)!!)
        }

        assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
    }

    /**
     * Test method for [PictureFacade.moveDown] with not movable picture.
     */
    @Test
    fun moveDownNotMovable() {
        val result = facade.moveDown(id = PictureUtils.PICTURES_COUNT)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PICTURE_NOT_MOVABLE", message = "Picture can't be moved down.")))
        }

        assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
    }

    /**
     * Test method for [PictureFacade.moveDown] with picture with bad ID.
     */
    @Test
    fun moveDownBadId() {
        val result = facade.moveDown(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(PICTURE_NOT_EXIST_EVENT))
        }

        assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
    }

    /**
     * Test method for [PictureFacade.newData].
     */
    @Test
    fun newData() {
        clearReferencedData()

        val result = facade.newData()
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(0)
    }

    /**
     * Test method for [PictureFacade.getAll].
     */
    @Test
    fun getAll() {
        val result = facade.getAll()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isNotNull
            it.assertThat(result.events()).isEmpty()
        }
        PictureUtils.assertPictureListDeepEquals(expected = PictureUtils.getPictures(), actual = result.data!!)

        assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
    }

    /**
     * Test method for [PictureFacade.add].
     */
    @Test
    @DirtiesContext
    fun add() {
        val expectedPicture = PictureUtils.newPictureDomain(id = PictureUtils.PICTURES_COUNT + 1)
            .fillAudit(audit = AuditUtils.newAudit())

        val result = facade.add(PictureUtils.newPicture(id = null))
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        PictureUtils.assertPictureDeepEquals(expected = expectedPicture, actual = PictureUtils.getPicture(entityManager = entityManager, id = PictureUtils.PICTURES_COUNT + 1)!!)

        assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT + 1)
    }

    /**
     * Test method for [PictureFacade.add] with picture with not null ID.
     */
    @Test
    fun addNotNullId() {
        val picture = PictureUtils.newPicture(id = null)
            .copy(id = Int.MAX_VALUE)

        val result = facade.add(data = picture)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PICTURE_ID_NOT_NULL", message = "ID must be null.")))
        }

        assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
    }

    /**
     * Test method for [PictureFacade.add] with picture with not null position.
     */
    @Test
    fun addNotNullPosition() {
        val picture = PictureUtils.newPicture(id = null)
            .copy(position = Int.MAX_VALUE)

        val result = facade.add(data = picture)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PICTURE_POSITION_NOT_NULL", message = "Position must be null.")))
        }

        assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
    }

    /**
     * Test method for [PictureFacade.add] with picture with null content.
     */
    @Test
    fun addNullContent() {
        val picture = PictureUtils.newPicture(id = null)
            .copy(content = null)

        val result = facade.add(data = picture)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PICTURE_CONTENT_NULL", message = "Content mustn't be null.")))
        }

        assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
    }

    /**
     * Test method for [PictureFacade.updatePositions].
     */
    @Test
    fun updatePositions() {
        val result = facade.updatePositions()
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        for (i in 1..PictureUtils.PICTURES_COUNT) {
            val expectedPicture = PictureUtils.getPictureDomain(index = i)
                .copy(position = i - 1)
                .fillAudit(audit = AuditUtils.updatedAudit())
            PictureUtils.assertPictureDeepEquals(expected = expectedPicture, actual = PictureUtils.getPicture(entityManager = entityManager, id = i)!!)
        }

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

    companion object {

        /**
         * Event for not existing picture
         */
        private val PICTURE_NOT_EXIST_EVENT = Event(severity = Severity.ERROR, key = "PICTURE_NOT_EXIST", message = "Picture doesn't exist.")

    }

}
