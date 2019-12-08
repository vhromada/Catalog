package cz.vhromada.catalog.facade.impl

import cz.vhromada.catalog.CatalogTestConfiguration
import cz.vhromada.catalog.entity.Game
import cz.vhromada.catalog.facade.GameFacade
import cz.vhromada.catalog.utils.GameUtils
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
import javax.persistence.EntityManager

/**
 * A class represents integration test for class [GameFacadeImpl].
 *
 * @author Vladimir Hromada
 */
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
class GameFacadeImplIntegrationTest : MovableParentFacadeIntegrationTest<Game, cz.vhromada.catalog.domain.Game>() {

    /**
     * Instance of [EntityManager]
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [GameFacade]
     */
    @Autowired
    private lateinit var facade: GameFacade

    /**
     * Test method for [GameFacade.add] with game with null name.
     */
    @Test
    fun add_NullName() {
        val game = newData(null)
                .copy(name = null)

        val result = facade.add(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GAME_NAME_NULL", "Name mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GameFacade.add] with game with empty string as name.
     */
    @Test
    fun add_EmptyName() {
        val game = newData(null)
                .copy(name = "")

        val result = facade.add(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GAME_NAME_EMPTY", "Name mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GameFacade.add] with game with null URL to english Wikipedia about game.
     */
    @Test
    fun add_NullWikiEn() {
        val game = newData(null)
                .copy(wikiEn = null)

        val result = facade.add(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GAME_WIKI_EN_NULL", "URL to english Wikipedia page about game mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GameFacade.add] with game with null URL to czech Wikipedia about game.
     */
    @Test
    fun add_NullWikiCz() {
        val game = newData(null)
                .copy(wikiCz = null)

        val result = facade.add(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GAME_WIKI_CZ_NULL", "URL to czech Wikipedia page about game mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GameFacade.add] with game with null count of media.
     */
    @Test
    fun add_NullMediaCount() {
        val game = newData(null)
                .copy(mediaCount = null)

        val result = facade.add(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GAME_MEDIA_COUNT_NULL", "Count of media mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GameFacade.add] with game with not positive count of media.
     */
    @Test
    fun add_NotPositiveMediaCount() {
        val game = newData(null)
                .copy(mediaCount = 0)

        val result = facade.add(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GAME_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GameFacade.add] with game with null other data.
     */
    @Test
    fun add_NullOtherData() {
        val game = newData(null)
                .copy(otherData = null)

        val result = facade.add(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GAME_OTHER_DATA_NULL", "Other data mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GameFacade.add] with game with null note.
     */
    @Test
    fun add_NullNote() {
        val game = newData(null)
                .copy(note = null)

        val result = facade.add(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GAME_NOTE_NULL", "Note mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GameFacade.update] with game with null name.
     */
    @Test
    fun update_NullName() {
        val game = newData(1)
                .copy(name = null)

        val result = facade.update(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GAME_NAME_NULL", "Name mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GameFacade.update] with game with empty string as name.
     */
    @Test
    fun update_EmptyName() {
        val game = newData(1)
                .copy(name = "")

        val result = facade.update(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GAME_NAME_EMPTY", "Name mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GameFacade.update] with game with null URL to english Wikipedia about game.
     */
    @Test
    fun update_NullWikiEn() {
        val game = newData(1)
                .copy(wikiEn = null)

        val result = facade.update(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GAME_WIKI_EN_NULL", "URL to english Wikipedia page about game mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GameFacade.update] with game with null URL to czech Wikipedia about game.
     */
    @Test
    fun update_NullWikiCz() {
        val game = newData(1)
                .copy(wikiCz = null)

        val result = facade.update(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GAME_WIKI_CZ_NULL", "URL to czech Wikipedia page about game mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GameFacade.update] with game with null count of media.
     */
    @Test
    fun update_NullMediaCount() {
        val game = newData(1)
                .copy(mediaCount = null)

        val result = facade.update(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GAME_MEDIA_COUNT_NULL", "Count of media mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GameFacade.update] with game with not positive count of media.
     */
    @Test
    fun update_NotPositiveMediaCount() {
        val game = newData(1)
                .copy(mediaCount = 0)

        val result = facade.update(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GAME_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GameFacade.update] with game with null other data.
     */
    @Test
    fun update_NullOtherData() {
        val game = newData(1)
                .copy(otherData = null)

        val result = facade.update(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GAME_OTHER_DATA_NULL", "Other data mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GameFacade.update] with game with null note.
     */
    @Test
    fun update_NullNote() {
        val game = newData(1)
                .copy(note = null)

        val result = facade.update(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GAME_NOTE_NULL", "Note mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GameFacade.getTotalMediaCount].
     */
    @Test
    fun getTotalMediaCount() {
        val result = facade.getTotalMediaCount()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(6)
            it.assertThat(result.events()).isEmpty()
        }

        assertDefaultRepositoryData()
    }

    override fun getFacade(): MovableParentFacade<Game> {
        return facade
    }

    override fun getDefaultDataCount(): Int {
        return GameUtils.GAMES_COUNT
    }

    override fun getRepositoryDataCount(): Int {
        return GameUtils.getGamesCount(entityManager)
    }

    override fun getDataList(): List<cz.vhromada.catalog.domain.Game> {
        return GameUtils.getGames()
    }

    override fun getDomainData(index: Int): cz.vhromada.catalog.domain.Game {
        return GameUtils.getGame(index)
    }

    override fun newData(id: Int?): Game {
        return GameUtils.newGame(id)
    }

    override fun newDomainData(id: Int): cz.vhromada.catalog.domain.Game {
        return GameUtils.newGameDomain(id)
    }

    override fun getRepositoryData(id: Int): cz.vhromada.catalog.domain.Game? {
        return GameUtils.getGame(entityManager, id)
    }

    override fun getName(): String {
        return "Game"
    }

    override fun clearReferencedData() {}

    override fun assertDataListDeepEquals(expected: List<Game>, actual: List<cz.vhromada.catalog.domain.Game>) {
        GameUtils.assertGameListDeepEquals(expected, actual)
    }

    override fun assertDataDeepEquals(expected: Game, actual: cz.vhromada.catalog.domain.Game) {
        GameUtils.assertGameDeepEquals(expected, actual)
    }

    override fun assertDataDomainDeepEquals(expected: cz.vhromada.catalog.domain.Game, actual: cz.vhromada.catalog.domain.Game) {
        GameUtils.assertGameDeepEquals(expected, actual)
    }

}
