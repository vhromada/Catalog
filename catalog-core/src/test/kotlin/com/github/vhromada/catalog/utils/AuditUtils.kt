package com.github.vhromada.catalog.utils

import com.github.vhromada.common.domain.Audit
import com.github.vhromada.common.test.utils.TestConstants
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import java.time.LocalDateTime

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
        return Audit(user = TestConstants.ACCOUNT_UUID, time = TestConstants.TIME)
    }

    /**
     * Returns audit.
     *
     * @return audit
     */
    fun getAudit(): Audit {
        return Audit(
                createdUser = TestConstants.ACCOUNT_UUID,
                createdTime = LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                updatedUser = "0998ab47-0d27-4538-b551-ee7a471cfcf1",
                updatedTime = LocalDateTime.of(2020, 1, 2, 0, 0, 0))
    }

    /**
     * Asserts audit deep equals.
     *
     * @param expected expected audit
     * @param actual   actual audit
     */
    fun assertAuditDeepEquals(expected: Audit?, actual: Audit?) {
        if (expected == null) {
            assertThat(actual).isNull()
        } else {
            assertThat(actual).isNotNull
            assertSoftly {
                it.assertThat(actual!!.createdUser).isEqualTo(expected.createdUser)
                it.assertThat(actual.createdTime).isEqualToIgnoringNanos(expected.createdTime)
                it.assertThat(actual.updatedUser).isEqualTo(expected.updatedUser)
                it.assertThat(actual.updatedTime).isEqualToIgnoringNanos(expected.updatedTime)
            }
        }
    }

}
