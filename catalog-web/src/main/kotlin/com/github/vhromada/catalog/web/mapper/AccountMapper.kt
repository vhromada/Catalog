package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.web.fo.AccountFO
import com.github.vhromada.common.account.entity.Credentials
import com.github.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for accounts.
 *
 * @author Vladimir Hromada
 */
@Component("webAccountMapper")
class AccountMapper : Mapper<AccountFO, Credentials> {

    override fun map(source: AccountFO): Credentials {
        return Credentials(username = source.username, password = source.password)
    }

    override fun mapBack(source: Credentials): AccountFO {
        throw UnsupportedOperationException("Not supported")
    }

}
