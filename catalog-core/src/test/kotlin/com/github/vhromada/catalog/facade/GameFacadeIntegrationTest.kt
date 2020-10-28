package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.entity.Game
import com.github.vhromada.catalog.utils.CheatDataUtils
import com.github.vhromada.catalog.utils.CheatUtils
import com.github.vhromada.catalog.utils.GameUtils
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
import javax.persistence.EntityManager

/**
 * A class represents integration test for class [GameFacade].
 *
 * @author Vladimir Hromada
 */
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
class GameFacadeIntegrationTest : MovableParentFacadeIntegrationTest<Game, com.github.vhromada.catalog.domain.Game>() {

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
    fun addNullName() {
        val game = newData(null)
                .copy(name = null)

        val result = facade.add(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_NAME_NULL", "Name mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GameFacade.add] with game with empty string as name.
     */
    @Test
    fun addEmptyName() {
        val game = newData(null)
                .copy(name = "")

        val result = facade.add(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_NAME_EMPTY", "Name mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GameFacade.add] with game with null URL to english Wikipedia about game.
     */
    @Test
    fun addNullWikiEn() {
        val game = newData(null)
                .copy(wikiEn = null)

        val result = facade.add(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_WIKI_EN_NULL", "URL to english Wikipedia page about game mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GameFacade.add] with game with null URL to czech Wikipedia about game.
     */
    @Test
    fun addNullWikiCz() {
        val game = newData(null)
                .copy(wikiCz = null)

        val result = facade.add(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_WIKI_CZ_NULL", "URL to czech Wikipedia page about game mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GameFacade.add] with game with null count of media.
     */
    @Test
    fun addNullMediaCount() {
        val game = newData(null)
                .copy(mediaCount = null)

        val result = facade.add(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_MEDIA_COUNT_NULL", "Count of media mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GameFacade.add] with game with not positive count of media.
     */
    @Test
    fun addNotPositiveMediaCount() {
        val game = newData(null)
                .copy(mediaCount = 0)

        val result = facade.add(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GameFacade.add] with game with null format.
     */
    @Test
    fun addNullFormat() {
        val game = newData(null)
                .copy(format = null)

        val result = facade.add(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_FORMAT_NULL", "Format mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GameFacade.add] with game with null other data.
     */
    @Test
    fun addNullOtherData() {
        val game = newData(null)
                .copy(otherData = null)

        val result = facade.add(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_OTHER_DATA_NULL", "Other data mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GameFacade.add] with game with null note.
     */
    @Test
    fun addNullNote() {
        val game = newData(null)
                .copy(note = null)

        val result = facade.add(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_NOTE_NULL", "Note mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GameFacade.update] with game with null name.
     */
    @Test
    fun updateNullName() {
        val game = newData(1)
                .copy(name = null)

        val result = facade.update(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_NAME_NULL", "Name mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GameFacade.update] with game with empty string as name.
     */
    @Test
    fun updateEmptyName() {
        val game = newData(1)
                .copy(name = "")

        val result = facade.update(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_NAME_EMPTY", "Name mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GameFacade.update] with game with null URL to english Wikipedia about game.
     */
    @Test
    fun updateNullWikiEn() {
        val game = newData(1)
                .copy(wikiEn = null)

        val result = facade.update(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_WIKI_EN_NULL", "URL to english Wikipedia page about game mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GameFacade.update] with game with null URL to czech Wikipedia about game.
     */
    @Test
    fun updateNullWikiCz() {
        val game = newData(1)
                .copy(wikiCz = null)

        val result = facade.update(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_WIKI_CZ_NULL", "URL to czech Wikipedia page about game mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GameFacade.update] with game with null count of media.
     */
    @Test
    fun updateNullMediaCount() {
        val game = newData(1)
                .copy(mediaCount = null)

        val result = facade.update(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_MEDIA_COUNT_NULL", "Count of media mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GameFacade.update] with game with not positive count of media.
     */
    @Test
    fun updateNotPositiveMediaCount() {
        val game = newData(1)
                .copy(mediaCount = 0)

        val result = facade.update(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GameFacade.update] with game with null format.
     */
    @Test
    fun updateNullFormat() {
        val game = newData(1)
                .copy(format = null)

        val result = facade.update(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_FORMAT_NULL", "Format mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GameFacade.update] with game with null other data.
     */
    @Test
    fun updateNullOtherData() {
        val game = newData(1)
                .copy(otherData = null)

        val result = facade.update(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_OTHER_DATA_NULL", "Other data mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [GameFacade.update] with game with null note.
     */
    @Test
    fun updateNullNote() {
        val game = newData(1)
                .copy(note = null)

        val result = facade.update(game)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_NOTE_NULL", "Note mustn't be null.")))
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

    override fun getDataList(): List<com.github.vhromada.catalog.domain.Game> {
        return GameUtils.getGames()
    }

    override fun getDomainData(index: Int): com.github.vhromada.catalog.domain.Game {
        return GameUtils.getGame(index)
    }

    override fun newData(id: Int?): Game {
        return GameUtils.newGame(id)
    }

    override fun newDomainData(id: Int): com.github.vhromada.catalog.domain.Game {
        return GameUtils.newGameDomain(id)
    }

    override fun getRepositoryData(id: Int): com.github.vhromada.catalog.domain.Game? {
        return GameUtils.getGame(entityManager, id)
    }

    override fun getName(): String {
        return "Game"
    }

    override fun clearReferencedData() {}

    override fun assertDataListDeepEquals(expected: List<Game>, actual: List<com.github.vhromada.catalog.domain.Game>) {
        GameUtils.assertGameListDeepEquals(expected, actual)
    }

    override fun assertDataDeepEquals(expected: Game, actual: com.github.vhromada.catalog.domain.Game) {
        GameUtils.assertGameDeepEquals(expected, actual)
    }

    override fun assertDataDomainDeepEquals(expected: com.github.vhromada.catalog.domain.Game, actual: com.github.vhromada.catalog.domain.Game) {
        GameUtils.assertGameDeepEquals(expected, actual)
    }

    override fun assertDefaultRepositoryData() {
        super.assertDefaultRepositoryData()

        assertReferences()
    }

    override fun assertNewRepositoryData() {
        super.assertNewRepositoryData()

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager)).isEqualTo(0)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager)).isEqualTo(0)
        }
    }

    override fun assertAddRepositoryData() {
        super.assertAddRepositoryData()

        assertReferences()
    }

    override fun assertUpdateRepositoryData() {
        super.assertUpdateRepositoryData()

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

    override fun assertRemoveRepositoryData() {
        super.assertRemoveRepositoryData()

        assertSoftly {
            it.assertThat(CheatUtils.getCheatsCount(entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
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
            it.assertThat(CheatUtils.getCheatsCount(entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
        }
    }

}
