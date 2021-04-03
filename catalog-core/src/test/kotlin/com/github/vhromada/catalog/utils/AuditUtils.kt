package com.github.vhromada.catalog.utils

import com.github.vhromada.common.domain.Audit
import org.assertj.core.api.SoftAssertions
import java.time.LocalDateTime

/**
 * Fill audit.
 *
 * @param audit audit
 * @return object with filled audit
 */
fun <T : Audit> T.fillAudit(audit: Audit?): T {
    createdUser = audit?.createdUser
    createdTime = audit?.createdTime
    updatedUser = audit?.updatedUser
    updatedTime = audit?.updatedTime
    return this
}

/**
 * A class represents utility class for audit.
 *
 * @author Vladimir Hromada
 */
object AuditUtils {

    /**
     * Returns audit.
     *
     * @return audit
     */
    fun newAudit(): Audit {
        val audit = object : Audit() {
        }
        audit.createdUser = TestConstants.ACCOUNT.uuid!!
        audit.createdTime = TestConstants.TIME
        audit.updatedUser = TestConstants.ACCOUNT.uuid!!
        audit.updatedTime = TestConstants.TIME
        return audit
    }

    /**
     * Returns audit.
     *
     * @return audit
     */
    fun getAudit(): Audit {
        val audit = object : Audit() {
        }
        audit.createdUser = TestConstants.ACCOUNT.uuid!!
        audit.createdTime = LocalDateTime.of(2020, 1, 1, 0, 0, 0)
        audit.updatedUser = "0998ab47-0d27-4538-b551-ee7a471cfcf1"
        audit.updatedTime = LocalDateTime.of(2020, 1, 2, 0, 0, 0)
        return audit
    }

    /**
     * Returns update audit.
     *
     * @return update audit
     */
    fun updatedAudit(): Audit {
        val audit = object : Audit() {
        }
        audit.createdUser = TestConstants.ACCOUNT.uuid!!
        audit.createdTime = LocalDateTime.of(2020, 1, 1, 0, 0, 0)
        audit.updatedUser = TestConstants.ACCOUNT.uuid!!
        audit.updatedTime = TestConstants.TIME
        return audit
    }

    /**
     * Asserts audit deep equals.
     *
     * @param softly   soft assertions
     * @param expected expected audit
     * @param actual   actual audit
     */
    fun assertAuditDeepEquals(softly: SoftAssertions, expected: Audit, actual: Audit) {
        softly.assertThat(actual.createdUser).isEqualTo(expected.createdUser)
        assertTimeDeepEquals(softly = softly, expected = expected.createdTime, actual = actual.createdTime)
        softly.assertThat(actual.updatedUser).isEqualTo(expected.updatedUser)
        assertTimeDeepEquals(softly = softly, expected = expected.updatedTime, actual = actual.updatedTime)
    }

    /**
     * Asserts time deep equals.
     *
     * @param softly   soft assertions
     * @param expected expected audit
     * @param actual   actual audit
     */
    private fun assertTimeDeepEquals(softly: SoftAssertions, expected: LocalDateTime?, actual: LocalDateTime?) {
        if (expected == null) {
            softly.assertThat(actual).isNull()
        } else {
            softly.assertThat(actual).isEqualToIgnoringNanos(expected)
        }
    }

}
