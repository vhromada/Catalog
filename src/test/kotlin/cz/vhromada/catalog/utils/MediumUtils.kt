package cz.vhromada.catalog.utils

import cz.vhromada.catalog.entity.Medium
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import javax.persistence.EntityManager

/**
 * A class represents utility class for media.
 *
 * @author Vladimir Hromada
 */
object MediumUtils {

    /**
     * Count of media
     */
    const val MEDIA_COUNT = 4

    /**
     * Returns media.
     *
     * @return media
     */
    fun getMedia(): List<cz.vhromada.catalog.domain.Medium> {
        val media = mutableListOf<cz.vhromada.catalog.domain.Medium>()
        for (i in 0 until MEDIA_COUNT) {
            media.add(getMedium(i + 1))
        }

        return media
    }

    /**
     * Returns medium.
     *
     * @param id ID
     * @return medium
     */
    fun newMediumDomain(id: Int?): cz.vhromada.catalog.domain.Medium {
        return cz.vhromada.catalog.domain.Medium(id = id, number = 1, length = 10)
    }

    /**
     * Returns medium.
     *
     * @param id ID
     * @return medium
     */
    fun newMedium(id: Int?): Medium {
        return Medium(id = id, number = 1, length = 10)
    }

    /**
     * Returns medium for index.
     *
     * @param index index
     * @return medium for index
     */
    fun getMedium(index: Int): cz.vhromada.catalog.domain.Medium {
        val lengthMultiplier = 100

        return cz.vhromada.catalog.domain.Medium(id = index, number = if (index < 4) 1 else 2, length = index * lengthMultiplier)
    }

    /**
     * Returns count of media.
     *
     * @param entityManager entity manager
     * @return count of media
     */
    @Suppress("CheckStyle")
    fun getMediaCount(entityManager: EntityManager): Int {
        return entityManager.createQuery("SELECT COUNT(m.id) FROM Medium m", java.lang.Long::class.java).singleResult.toInt()
    }

    /**
     * Asserts media deep equals.
     *
     * @param expected expected media
     * @param actual   actual media
     */
    fun assertMediaDeepEquals(expected: List<cz.vhromada.catalog.domain.Medium?>?, actual: List<cz.vhromada.catalog.domain.Medium?>?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertThat(expected!!.size).isEqualTo(actual!!.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertMediumDeepEquals(expected[i], actual[i])
            }
        }
    }

    /**
     * Asserts medium deep equals.
     *
     * @param expected expected medium
     * @param actual   actual medium
     */
    private fun assertMediumDeepEquals(expected: cz.vhromada.catalog.domain.Medium?, actual: cz.vhromada.catalog.domain.Medium?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(expected!!.id).isEqualTo(actual!!.id)
            it.assertThat(expected.number).isEqualTo(actual.number)
            it.assertThat(expected.length).isEqualTo(actual.length)
        }
    }

    /**
     * Asserts media deep equals.
     *
     * @param expected expected list of medium
     * @param actual   actual media
     */
    fun assertMediumListDeepEquals(expected: List<Medium?>?, actual: List<cz.vhromada.catalog.domain.Medium?>?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertThat(expected!!.size).isEqualTo(actual!!.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertMediumDeepEquals(expected[i], actual[i])
            }
        }
    }

    /**
     * Asserts medium deep equals.
     *
     * @param expected expected medium
     * @param actual   actual medium
     */
    private fun assertMediumDeepEquals(expected: Medium?, actual: cz.vhromada.catalog.domain.Medium?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(actual!!.id).isEqualTo(expected!!.id)
            it.assertThat(actual.number).isEqualTo(expected.number)
            it.assertThat(actual.length).isEqualTo(expected.length)
        }
    }

}