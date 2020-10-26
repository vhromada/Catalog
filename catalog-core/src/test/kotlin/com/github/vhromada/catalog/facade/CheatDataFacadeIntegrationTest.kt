package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.entity.Cheat
import com.github.vhromada.catalog.entity.CheatData
import com.github.vhromada.catalog.utils.CheatDataUtils
import com.github.vhromada.catalog.utils.CheatUtils
import com.github.vhromada.catalog.utils.GameUtils
import com.github.vhromada.common.facade.MovableChildFacade
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.result.Status
import com.github.vhromada.common.test.facade.MovableChildFacadeIntegrationTest
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.test.context.ContextConfiguration
import javax.persistence.EntityManager

/**
 * A class represents integration test for class [CheatDataFacade].
 *
 * @author Vladimir Hromada
 */
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
class CheatDataFacadeIntegrationTest : MovableChildFacadeIntegrationTest<CheatData, com.github.vhromada.catalog.domain.CheatData, Cheat>() {

    /**
     * Instance of [EntityManager]
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [CheatDataFacade]
     */
    @Autowired
    private lateinit var facade: CheatDataFacade

    /**
     * Test method for [CheatDataFacade.add] with cheat's data with null action.
     */
    @Test
    fun addNullAction() {
        val cheatData = newChildData(null)
                .copy(action = null)

        val result = facade.add(CheatUtils.newCheat(1), cheatData)

        SoftAssertions.assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_ACTION_NULL", "Action mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [CheatDataFacade.add] with cheat's data with empty string as action.
     */
    @Test
    fun addEmptyAction() {
        val cheatData = newChildData(null)
                .copy(action = "")

        val result = facade.add(CheatUtils.newCheat(1), cheatData)

        SoftAssertions.assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_ACTION_EMPTY", "Action mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [CheatDataFacade.add] with cheat's data with null description.
     */
    @Test
    fun addNullDescription() {
        val cheatData = newChildData(null)
                .copy(description = null)

        val result = facade.add(CheatUtils.newCheat(1), cheatData)

        SoftAssertions.assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_DESCRIPTION_NULL", "Description mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [CheatDataFacade.add] with cheat's data with empty string as description.
     */
    @Test
    fun addEmptyDescription() {
        val cheatData = newChildData(null)
                .copy(description = "")

        val result = facade.add(CheatUtils.newCheat(1), cheatData)

        SoftAssertions.assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_DESCRIPTION_EMPTY", "Description mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [CheatDataFacade.update] with cheat's data with null action.
     */
    @Test
    fun updateNullAction() {
        val cheatData = newChildData(1)
                .copy(action = null)

        val result = facade.update(cheatData)

        SoftAssertions.assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_ACTION_NULL", "Action mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [CheatDataFacade.update] with cheat's data with empty string as action.
     */
    @Test
    fun updateEmptyAction() {
        val cheatData = newChildData(1)
                .copy(action = "")

        val result = facade.update(cheatData)

        SoftAssertions.assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_ACTION_EMPTY", "Action mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [CheatDataFacade.update] with cheat's data with null description.
     */
    @Test
    fun updateNullDescription() {
        val cheatData = newChildData(1)
                .copy(description = null)

        val result = facade.update(cheatData)

        SoftAssertions.assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_DESCRIPTION_NULL", "Description mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [CheatDataFacade.update] with cheat's data with empty string as description.
     */
    @Test
    fun updateEmptyDescription() {
        val cheatData = newChildData(1)
                .copy(description = "")

        val result = facade.update(cheatData)

        SoftAssertions.assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_DESCRIPTION_EMPTY", "Description mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    override fun getFacade(): MovableChildFacade<CheatData, Cheat> {
        return facade
    }

    override fun getDefaultParentDataCount(): Int {
        return CheatUtils.CHEATS_COUNT
    }

    override fun getDefaultChildDataCount(): Int {
        return CheatDataUtils.CHEAT_DATA_COUNT
    }

    override fun getRepositoryParentDataCount(): Int {
        return CheatUtils.getCheatsCount(entityManager)
    }

    override fun getRepositoryChildDataCount(): Int {
        return CheatDataUtils.getCheatDataCount(entityManager)
    }

    override fun getDataList(parentId: Int): List<com.github.vhromada.catalog.domain.CheatData> {
        return CheatDataUtils.getCheatDataList(parentId)
    }

    override fun getDomainData(index: Int): com.github.vhromada.catalog.domain.CheatData {
        return CheatDataUtils.getCheatData(index)
    }

    override fun newParentData(id: Int?): Cheat {
        return CheatUtils.newCheat(id)
    }

    override fun newChildData(id: Int?): CheatData {
        return CheatDataUtils.newCheatData(id)
    }

    override fun newDomainData(id: Int): com.github.vhromada.catalog.domain.CheatData {
        return CheatDataUtils.newCheatDataDomain(id)
    }

    override fun getRepositoryData(id: Int): com.github.vhromada.catalog.domain.CheatData? {
        return CheatDataUtils.getCheatData(entityManager, id)
    }

    override fun getParentName(): String {
        return "Cheat"
    }

    override fun getChildName(): String {
        return "Cheat's data"
    }

    override fun getChildPrefix(): String {
        return "CHEAT_DATA"
    }

    override fun assertDataListDeepEquals(expected: List<CheatData>, actual: List<com.github.vhromada.catalog.domain.CheatData>) {
        CheatDataUtils.assertCheatDataListDeepEquals(expected, actual)
    }

    override fun assertDataDeepEquals(expected: CheatData, actual: com.github.vhromada.catalog.domain.CheatData) {
        CheatDataUtils.assertCheatDataDeepEquals(expected, actual)
    }

    override fun assertDataDomainDeepEquals(expected: com.github.vhromada.catalog.domain.CheatData, actual: com.github.vhromada.catalog.domain.CheatData) {
        CheatDataUtils.assertCheatDataDeepEquals(expected, actual)
    }

    override fun assertReferences() {
        super.assertReferences()

        SoftAssertions.assertSoftly {
            it.assertThat(GameUtils.getGamesCount(entityManager)).isEqualTo(GameUtils.GAMES_COUNT)
            it.assertThat(CheatUtils.getCheatsCount(entityManager)).isEqualTo(CheatUtils.CHEATS_COUNT)
        }
    }

}
