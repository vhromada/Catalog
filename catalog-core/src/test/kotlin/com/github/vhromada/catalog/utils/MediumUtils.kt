package com.github.vhromada.catalog.utils

import com.github.vhromada.catalog.entity.Medium
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
     * Returns medium.
     *
     * @param id ID
     * @return medium
     */
    fun newMediumDomain(id: Int?): com.github.vhromada.catalog.domain.Medium {
        return com.github.vhromada.catalog.domain.Medium(id = id, number = 1, length = 10)
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
    fun getMediumDomain(index: Int): com.github.vhromada.catalog.domain.Medium {
        val lengthMultiplier = 100

        return com.github.vhromada.catalog.domain.Medium(id = index, number = if (index < 4) 1 else 2, length = index * lengthMultiplier)
            .fillAudit(AuditUtils.getAudit())
    }

    /**
     * Returns count of media.
     *
     * @param entityManager entity manager
     * @return count of media
     */
    @Suppress("JpaQlInspection")
    fun getMediaCount(entityManager: EntityManager): Int {
        return entityManager.createQuery("SELECT COUNT(m.id) FROM Medium m", java.lang.Long::class.java).singleResult.toInt()
    }

    /**
     * Asserts media deep equals.
     *
     * @param expected expected list of media
     * @param actual   actual list of media
     */
    fun assertDomainMediaDeepEquals(expected: List<com.github.vhromada.catalog.domain.Medium>, actual: List<com.github.vhromada.catalog.domain.Medium>) {
        assertThat(expected.size).isEqualTo(actual.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertMediumDeepEquals(expected = expected[i], actual = actual[i])
            }
        }
    }

    /**
     * Asserts medium deep equals.
     *
     * @param expected expected medium
     * @param actual   actual medium
     */
    private fun assertMediumDeepEquals(expected: com.github.vhromada.catalog.domain.Medium, actual: com.github.vhromada.catalog.domain.Medium) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.number).isEqualTo(expected.number)
            it.assertThat(actual.length).isEqualTo(expected.length)
            AuditUtils.assertAuditDeepEquals(softly = it, expected = expected, actual = actual)
        }
    }

    /**
     * Asserts media deep equals.
     *
     * @param expected expected list of media
     * @param actual   actual list of media
     */
    fun assertMediaDeepEquals(expected: List<Medium>, actual: List<com.github.vhromada.catalog.domain.Medium>) {
        assertThat(expected.size).isEqualTo(actual.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertMediumDeepEquals(expected = expected[i], actual = actual[i])
            }
        }
    }

    /**
     * Asserts medium deep equals.
     *
     * @param expected expected medium
     * @param actual   actual medium
     */
    private fun assertMediumDeepEquals(expected: Medium, actual: com.github.vhromada.catalog.domain.Medium) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.number).isEqualTo(expected.number)
            it.assertThat(actual.length).isEqualTo(expected.length)
            it.assertThat(actual.createdUser).isNull()
            it.assertThat(actual.createdTime).isNull()
            it.assertThat(actual.updatedUser).isNull()
            it.assertThat(actual.updatedTime).isNull()
        }
    }

    /**
     * Asserts media deep equals.
     *
     * @param expected expected list of media
     * @param actual   actual list of media
     */
    fun assertMediumListDeepEquals(expected: List<com.github.vhromada.catalog.domain.Medium>, actual: List<Medium>) {
        assertThat(expected.size).isEqualTo(actual.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertMediumDeepEquals(expected = expected[i], actual = actual[i])
            }
        }
    }

    /**
     * Asserts medium deep equals.
     *
     * @param expected expected medium
     * @param actual   actual medium
     */
    private fun assertMediumDeepEquals(expected: com.github.vhromada.catalog.domain.Medium, actual: Medium) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.number).isEqualTo(expected.number)
            it.assertThat(actual.length).isEqualTo(expected.length)
        }
    }

}
