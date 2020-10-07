package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.web.fo.AccountFO
import com.github.vhromada.common.account.entity.Credentials

/**
 * An interface represents mapper for accounts.
 *
 * @author Vladimir Hromada
 */
interface AccountMapper {

    /**
     * Returns credentials.
     *
     * @param source FO for account
     * @return credentials
     */
    fun map(source: AccountFO): Credentials

}