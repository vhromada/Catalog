package cz.vhromada.catalog.facade.impl

import cz.vhromada.catalog.CatalogTestConfiguration
import cz.vhromada.catalog.entity.Picture
import cz.vhromada.catalog.facade.PictureFacade
import cz.vhromada.catalog.utils.PictureUtils
import cz.vhromada.common.facade.MovableParentFacade
import cz.vhromada.common.test.facade.MovableParentFacadeIntegrationTest
import cz.vhromada.validation.result.Event
import cz.vhromada.validation.result.Severity
import cz.vhromada.validation.result.Status
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.DefaultTransactionDefinition
import javax.persistence.EntityManager

/**
 * A class represents integration test for class [PictureFacadeImpl].
 *
 * @author Vladimir Hromada
 */
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
class PictureFacadeImplIntegrationTest : MovableParentFacadeIntegrationTest<Picture, cz.vhromada.catalog.domain.Picture>() {

    /**
     * Instance of [EntityManager]
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [PlatformTransactionManager]
     */
    @Autowired
    private lateinit var transactionManager: PlatformTransactionManager

    /**
     * Instance of [PictureFacade]
     */
    @Autowired
    private lateinit var facade: PictureFacade

    /**
     * Test method for [PictureFacade.add] with picture with null content.
     */
    @Test
    fun add_NullContent() {
        val picture = newData(null)
                .copy(content = null)

        val result = facade.add(picture)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "PICTURE_CONTENT_NULL", "Content mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [PictureFacade.update] with picture with null content.
     */
    @Test
    fun update_NullContent() {
        val picture = newData(1)
                .copy(content = null)

        val result = facade.update(picture)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "PICTURE_CONTENT_NULL", "Content mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    override fun getFacade(): MovableParentFacade<Picture> {
        return facade
    }

    override fun getDefaultDataCount(): Int {
        return PictureUtils.PICTURES_COUNT
    }

    override fun getRepositoryDataCount(): Int {
        return PictureUtils.getPicturesCount(entityManager)
    }

    override fun getDataList(): List<cz.vhromada.catalog.domain.Picture> {
        return PictureUtils.getPictures()
    }

    override fun getDomainData(index: Int): cz.vhromada.catalog.domain.Picture {
        return PictureUtils.getPicture(index)
    }

    override fun newData(id: Int?): Picture {
        return PictureUtils.newPicture(id)
    }

    override fun newDomainData(id: Int): cz.vhromada.catalog.domain.Picture {
        return PictureUtils.newPictureDomain(id)
    }

    override fun getRepositoryData(id: Int): cz.vhromada.catalog.domain.Picture? {
        return PictureUtils.getPicture(entityManager, id)
    }

    override fun getName(): String {
        return "Picture"
    }

    override fun clearReferencedData() {
        val transactionStatus = transactionManager.getTransaction(DefaultTransactionDefinition())
        entityManager.createNativeQuery("UPDATE movies SET picture = NULL").executeUpdate()
        entityManager.createNativeQuery("UPDATE tv_shows SET picture = NULL").executeUpdate()
        transactionManager.commit(transactionStatus)
    }

    override fun assertDataListDeepEquals(expected: List<Picture>, actual: List<cz.vhromada.catalog.domain.Picture>) {
        PictureUtils.assertPictureListDeepEquals(expected, actual)
    }

    override fun assertDataDeepEquals(expected: Picture, actual: cz.vhromada.catalog.domain.Picture) {
        PictureUtils.assertPictureDeepEquals(expected, actual)
    }

    override fun assertDataDomainDeepEquals(expected: cz.vhromada.catalog.domain.Picture, actual: cz.vhromada.catalog.domain.Picture) {
        PictureUtils.assertPictureDeepEquals(expected, actual)
    }

}
