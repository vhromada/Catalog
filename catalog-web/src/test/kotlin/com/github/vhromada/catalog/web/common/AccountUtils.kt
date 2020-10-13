package com.github.vhromada.catalog.web.common

import com.github.vhromada.catalog.web.fo.AccountFO
import com.github.vhromada.common.account.entity.Credentials
import org.assertj.core.api.SoftAssertions.assertSoftly

/**
 * A class represents utility class for accounts.
 *
 * @author Vladimir Hromada
 */
object AccountUtils {

    /**
     * Returns FO for account.
     *
     * @return FO for account
     */
    fun getAccount(): AccountFO {
        return AccountFO(username = "username", password = "password", copyPassword = "copyPassword")
    }

    /**
     * Asserts account deep equals.
     *
     * @param expected expected FO for account
     * @param actual   actual credentials
     */
    fun assertAccountDeepEquals(expected: AccountFO?, actual: Credentials?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(actual!!.username).isEqualTo(expected!!.username)
            it.assertThat(actual.password).isEqualTo(expected.password)
        }
    }

}
