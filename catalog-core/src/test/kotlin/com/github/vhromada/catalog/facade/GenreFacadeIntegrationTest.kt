package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.entity.Genre
import com.github.vhromada.catalog.utils.GenreUtils
import com.github.vhromada.catalog.utils.MovieUtils
import com.github.vhromada.catalog.utils.ShowUtils
import com.github.vhromada.common.facade.MovableParentFacade
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.result.Status
import com.github.vhromada.common.test.facade.MovableParentFacadeIntegrationTest
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.DefaultTransactionDefinition
import javax.persistence.EntityManager

/**
 * A class represents integration test for class [GenreFacade].
 *
 * @author Vladimir Hromada
 */
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
class GenreFacadeIntegrationTest : MovableParentFacadeIntegrationTest<Genre, com.github.vhromada.catalog.domain.Genre>() {

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
     * Instance of [GenreFacade]
     */
    @Autowired
    private lateinit var facade: GenreFacade

    /**
     * Test method for [GenreFacade.add] with genre with null name.
     */
    @Test
    fun addNullName() {
        val genre = newData(null)
                .copy(name = null)

        val result = facade.add(genre)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_NAME_NULL", "Name mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GenreFacade.add] with genre with empty string as name.
     */
    @Test
    fun addEmptyName() {
        val genre = newData(null)
                .copy(name = "")

        val result = facade.add(genre)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_NAME_EMPTY", "Name mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GenreFacade.update] with genre with null name.
     */
    @Test
    fun updateNullName() {
        val genre = newData(1)
                .copy(name = null)

        val result = facade.update(genre)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_NAME_NULL", "Name mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GenreFacade.update] with genre with empty string as name.
     */
    @Test
    fun updateEmptyName() {
        val genre = newData(1)
                .copy(name = "")

        val result = facade.update(genre)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_NAME_EMPTY", "Name mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    override fun getFacade(): MovableParentFacade<Genre> {
        return facade
    }

    override fun getDefaultDataCount(): Int {
        return GenreUtils.GENRES_COUNT
    }

    override fun getRepositoryDataCount(): Int {
        return GenreUtils.getGenresCount(entityManager)
    }

    override fun getDataList(): List<com.github.vhromada.catalog.domain.Genre> {
        return GenreUtils.getGenres()
    }

    override fun getDomainData(index: Int): com.github.vhromada.catalog.domain.Genre {
        return GenreUtils.getGenreDomain(index)
    }

    override fun newData(id: Int?): Genre {
        return GenreUtils.newGenre(id)
    }

    override fun newDomainData(id: Int): com.github.vhromada.catalog.domain.Genre {
        return GenreUtils.newGenreDomain(id)
    }

    override fun getRepositoryData(id: Int): com.github.vhromada.catalog.domain.Genre? {
        return GenreUtils.getGenre(entityManager, id)
    }

    override fun getName(): String {
        return "Genre"
    }

    override fun clearReferencedData() {
        val transactionStatus = transactionManager.getTransaction(DefaultTransactionDefinition())
        entityManager.createNativeQuery("DELETE FROM movie_genres").executeUpdate()
        entityManager.createNativeQuery("DELETE FROM tv_show_genres").executeUpdate()
        transactionManager.commit(transactionStatus)
    }

    override fun assertDataListDeepEquals(expected: List<Genre>, actual: List<com.github.vhromada.catalog.domain.Genre>) {
        GenreUtils.assertGenreListDeepEquals(expected, actual)
    }

    override fun assertDataDeepEquals(expected: Genre, actual: com.github.vhromada.catalog.domain.Genre) {
        GenreUtils.assertGenreDeepEquals(expected, actual)
    }

    override fun assertDataDomainDeepEquals(expected: com.github.vhromada.catalog.domain.Genre, actual: com.github.vhromada.catalog.domain.Genre) {
        GenreUtils.assertGenreDeepEquals(expected, actual)
    }

    override fun assertDefaultRepositoryData() {
        super.assertDefaultRepositoryData()

        assertReferences()
    }

    override fun assertNewRepositoryData() {
        super.assertNewRepositoryData()

        assertReferences()
    }

    override fun assertAddRepositoryData() {
        super.assertAddRepositoryData()

        assertReferences()
    }

    override fun assertUpdateRepositoryData() {
        super.assertUpdateRepositoryData()

        assertReferences()
    }

    override fun assertRemoveRepositoryData() {
        super.assertRemoveRepositoryData()

        assertReferences()
    }

    override fun assertDuplicateRepositoryData() {
        super.assertDuplicateRepositoryData()

        assertReferences()
    }

    /**
     * Asserts references.
     */
    private fun assertReferences() {
        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

}
