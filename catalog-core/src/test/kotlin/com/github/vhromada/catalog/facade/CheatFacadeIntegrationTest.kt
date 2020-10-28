package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.entity.Cheat
import com.github.vhromada.catalog.entity.Game
import com.github.vhromada.catalog.utils.CheatDataUtils
import com.github.vhromada.catalog.utils.CheatUtils
import com.github.vhromada.catalog.utils.GameUtils
import com.github.vhromada.common.facade.MovableChildFacade
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.result.Status
import com.github.vhromada.common.test.facade.MovableChildFacadeIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import javax.persistence.EntityManager

/**
 * A class represents integration test for class [CheatFacade].
 *
 * @author Vladimir Hromada
 */
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
class CheatFacadeIntegrationTest : MovableChildFacadeIntegrationTest<Cheat, com.github.vhromada.catalog.domain.Cheat, Game>() {

    /**
     * Instance of [EntityManager]
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [CheatFacade]
     */
    @Autowired
    private lateinit var facade: CheatFacade

    @Test
    @DirtiesContext
    override fun addNotNullPosition() {
        val expectedData = newDomainData(getDefaultChildDataCount() + 1)
        expectedData.position = Int.MAX_VALUE

        val result = getFacade().add(newParentData(1), newChildData(null, 0))

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        assertDataDomainDeepEquals(expectedData, getRepositoryData(getDefaultChildDataCount() + 1)!!)
        assertAddRepositoryData()
    }

    /**
     * Test method for [CheatFacade.add] with cheat with null setting for game.
     */
    @Test
    fun addNullGameSetting() {
        val cheat = newChildData(null)
                .copy(gameSetting = null)

        val result = facade.add(newParentData(1), cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_GAME_SETTING_NULL", "Setting for game mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [CheatFacade.add] with cheat with null setting for cheat.
     */
    @Test
    fun addNullCheatSetting() {
        val cheat = newChildData(null)
                .copy(cheatSetting = null)

        val result = facade.add(newParentData(1), cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_CHEAT_SETTING_NULL", "Setting for cheat mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [CheatFacade.add] with cheat with null cheat's data.
     */
    @Test
    fun addNullCheatData() {
        val cheat = newChildData(null)
                .copy(data = null)

        val result = facade.add(newParentData(1), cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_DATA_NULL", "Cheat's data mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [CheatFacade.add] with cheat with cheat's data with null value.
     */
    @Test
    fun addBadCheatData() {
        val cheat = newChildData(null)
                .copy(data = listOf(CheatDataUtils.newCheatData(1), null))

        val result = facade.add(newParentData(1), cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_DATA_CONTAIN_NULL", "Cheat's data mustn't contain null value.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [CheatFacade.add] with cheat with cheat's data with null action.
     */
    @Test
    fun addCheatDataWithNullAction() {
        val badCheatData = CheatDataUtils.newCheatData(2)
                .copy(action = null)
        val cheat = newChildData(null)
                .copy(data = listOf(CheatDataUtils.newCheatData(1), badCheatData))

        val result = facade.add(newParentData(1), cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_DATA_ACTION_NULL", "Cheat's data action mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [CheatFacade.add] with cheat with cheat's data with empty action.
     */
    @Test
    fun addCheatDataWithEmptyAction() {
        val badCheatData = CheatDataUtils.newCheatData(2)
                .copy(action = "")
        val cheat = newChildData(null)
                .copy(data = listOf(CheatDataUtils.newCheatData(1), badCheatData))

        val result = facade.add(newParentData(1), cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_DATA_ACTION_EMPTY", "Cheat's data action mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [CheatFacade.add] with cheat with cheat's data with null description.
     */
    @Test
    fun addCheatDataWithNullDescription() {
        val badCheatData = CheatDataUtils.newCheatData(2)
                .copy(description = null)
        val cheat = newChildData(null)
                .copy(data = listOf(CheatDataUtils.newCheatData(1), badCheatData))

        val result = facade.add(newParentData(1), cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_DATA_DESCRIPTION_NULL", "Cheat's data description mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [CheatFacade.add] with cheat with cheat's data with empty description.
     */
    @Test
    fun addCheatDataWithEmptyDescription() {
        val badCheatData = CheatDataUtils.newCheatData(2)
                .copy(description = "")
        val cheat = newChildData(null)
                .copy(data = listOf(CheatDataUtils.newCheatData(1), badCheatData))

        val result = facade.add(newParentData(1), cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_DATA_DESCRIPTION_EMPTY", "Cheat's data description mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [CheatFacade.add] with game with cheat.
     */
    @Test
    fun addGameWithCheat() {
        val cheat = newChildData(null)

        val result = facade.add(newParentData(2), cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getParentPrefix()}_CHEAT_EXIST", "${getParentName()} already has cheat.")))
        }

        assertDefaultRepositoryData()
    }

    @Test
    @DirtiesContext
    override fun updateNullPosition() {
        val data = newChildData(1, null)

        val result = getFacade().update(data)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        assertDataDeepEquals(data, getRepositoryData(1)!!)
        assertUpdateRepositoryData()
    }

    /**
     * Test method for [CheatFacade.update] with cheat with null setting for game.
     */
    @Test
    fun updateNullGameSetting() {
        val cheat = newChildData(1)
                .copy(gameSetting = null)

        val result = facade.update(cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_GAME_SETTING_NULL", "Setting for game mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [CheatFacade.update] with cheat with null setting for cheat.
     */
    @Test
    fun updateNullCheatSetting() {
        val cheat = newChildData(1)
                .copy(cheatSetting = null)

        val result = facade.update(cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_CHEAT_SETTING_NULL", "Setting for cheat mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [CheatFacade.update] with cheat with null cheat's data.
     */
    @Test
    fun updateNullCheatData() {
        val cheat = newChildData(1)
                .copy(data = null)

        val result = facade.update(cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_DATA_NULL", "Cheat's data mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [CheatFacade.update] with cheat with cheat's data with null value.
     */
    @Test
    fun updateBadCheatData() {
        val cheat = newChildData(1)
                .copy(data = listOf(CheatDataUtils.newCheatData(1), null))

        val result = facade.update(cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_DATA_CONTAIN_NULL", "Cheat's data mustn't contain null value.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [CheatFacade.update] with cheat with cheat's data with null action.
     */
    @Test
    fun updateCheatDataWithNullAction() {
        val badCheatData = CheatDataUtils.newCheatData(2)
                .copy(action = null)
        val cheat = newChildData(1)
                .copy(data = listOf(CheatDataUtils.newCheatData(1), badCheatData))

        val result = facade.update(cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_DATA_ACTION_NULL", "Cheat's data action mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [CheatFacade.update] with cheat with cheat's data with empty action.
     */
    @Test
    fun updateCheatDataWithEmptyAction() {
        val badCheatData = CheatDataUtils.newCheatData(2)
                .copy(action = "")
        val cheat = newChildData(1)
                .copy(data = listOf(CheatDataUtils.newCheatData(1), badCheatData))

        val result = facade.update(cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_DATA_ACTION_EMPTY", "Cheat's data action mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [CheatFacade.update] with cheat with cheat's data with null description.
     */
    @Test
    fun updateCheatDataWithNullDescription() {
        val badCheatData = CheatDataUtils.newCheatData(2)
                .copy(description = null)
        val cheat = newChildData(1)
                .copy(data = listOf(CheatDataUtils.newCheatData(1), badCheatData))

        val result = facade.update(cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_DATA_DESCRIPTION_NULL", "Cheat's data description mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [CheatFacade.update] with cheat with cheat's data with empty description.
     */
    @Test
    fun updateCheatDataWithEmptyDescription() {
        val badCheatData = CheatDataUtils.newCheatData(2)
                .copy(description = "")
        val cheat = newChildData(1)
                .copy(data = listOf(CheatDataUtils.newCheatData(1), badCheatData))

        val result = facade.update(cheat)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_DATA_DESCRIPTION_EMPTY", "Cheat's data description mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    @Test
    override fun duplicate() {
        val result = getFacade().duplicate(newChildData(Int.MAX_VALUE))

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_NOT_DUPLICABLE", "${getChildName()} can't be duplicated.")))
        }

        assertDefaultRepositoryData()
    }

    @Test
    override fun duplicateNullId() {
        // no test
    }

    @Test
    override fun duplicateBadId() {
        // no test
    }

    @Test
    override fun moveUp() {
        val result = getFacade().moveUp(newChildData(Int.MAX_VALUE))

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_NOT_MOVABLE", "${getChildName()} can't be moved up.")))
        }

        assertDefaultRepositoryData()
    }

    @Test
    override fun moveUpNullId() {
        // no test
    }

    @Test
    override fun moveUpNotMovableData() {
        // no test
    }

    @Test
    override fun moveUpBadId() {
        // no test
    }

    @Test
    override fun moveDown() {
        val result = getFacade().moveDown(newChildData(Int.MAX_VALUE))

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_NOT_MOVABLE", "${getChildName()} can't be moved down.")))
        }

        assertDefaultRepositoryData()
    }

    @Test
    override fun moveDownNullId() {
        // no test
    }

    @Test
    override fun moveDownNotMovableData() {
        // no test
    }

    @Test
    override fun moveDownBadId() {
        // no test
    }

    @Test
    override fun find() {
        for (i in 1..getDefaultParentDataCount()) {
            val result = getFacade().find(newParentData(i))

            assertSoftly {
                it.assertThat(result.status).isEqualTo(Status.OK)
                it.assertThat(result.events()).isEmpty()
                if (i == 1) {
                    it.assertThat(result.data).isEmpty()
                } else {
                    assertDataListDeepEquals(result.data!!, getDataList(i))
                }
            }
        }

        assertDefaultRepositoryData()
    }

    override fun getFacade(): MovableChildFacade<Cheat, Game> {
        return facade
    }

    override fun getDefaultParentDataCount(): Int {
        return GameUtils.GAMES_COUNT
    }

    override fun getDefaultChildDataCount(): Int {
        return CheatUtils.CHEATS_COUNT
    }

    override fun getRepositoryParentDataCount(): Int {
        return GameUtils.getGamesCount(entityManager)
    }

    override fun getRepositoryChildDataCount(): Int {
        return CheatUtils.getCheatsCount(entityManager)
    }

    override fun getDataList(parentId: Int): List<com.github.vhromada.catalog.domain.Cheat> {
        return listOf(CheatUtils.getCheat(parentId - 1))
    }

    override fun getDomainData(index: Int): com.github.vhromada.catalog.domain.Cheat {
        return CheatUtils.getCheat(index)
    }

    override fun newParentData(id: Int?): Game {
        return GameUtils.newGame(id)
    }

    override fun newChildData(id: Int?): Cheat {
        return CheatUtils.newCheat(id)
    }

    override fun newDomainData(id: Int): com.github.vhromada.catalog.domain.Cheat {
        return CheatUtils.newCheatDomain(id)
    }

    override fun getRepositoryData(id: Int): com.github.vhromada.catalog.domain.Cheat? {
        return CheatUtils.getCheat(entityManager, id)
    }

    override fun getParentName(): String {
        return "Game"
    }

    override fun getChildName(): String {
        return "Cheat"
    }

    override fun assertDataListDeepEquals(expected: List<Cheat>, actual: List<com.github.vhromada.catalog.domain.Cheat>) {
        CheatUtils.assertCheatListDeepEquals(expected, actual)
    }

    override fun assertDataDeepEquals(expected: Cheat, actual: com.github.vhromada.catalog.domain.Cheat) {
        CheatUtils.assertCheatDeepEquals(expected, actual)
    }

    override fun assertDataDomainDeepEquals(expected: com.github.vhromada.catalog.domain.Cheat, actual: com.github.vhromada.catalog.domain.Cheat) {
        CheatUtils.assertCheatDeepEquals(expected, actual)
    }

    override fun assertUpdateRepositoryData() {
        assertSoftly {
            it.assertThat(getRepositoryChildDataCount()).isEqualTo(getDefaultChildDataCount())
            it.assertThat(getRepositoryParentDataCount()).isEqualTo(getDefaultParentDataCount())
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT - CheatDataUtils.CHEAT_DATA_CHEAT_COUNT)
        }
    }

    override fun assertRemoveRepositoryData() {
        assertSoftly {
            it.assertThat(getRepositoryChildDataCount()).isEqualTo(getDefaultChildDataCount() - 1)
            it.assertThat(getRepositoryParentDataCount()).isEqualTo(getDefaultParentDataCount())
            it.assertThat(CheatDataUtils.getCheatDataCount(entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT - CheatDataUtils.CHEAT_DATA_CHEAT_COUNT)
        }
    }

    override fun assertReferences() {
        super.assertReferences()

        assertThat(CheatDataUtils.getCheatDataCount(entityManager)).isEqualTo(CheatDataUtils.CHEAT_DATA_COUNT)
    }

}
