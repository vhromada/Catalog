package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.web.CatalogMapperTestConfiguration
import com.github.vhromada.catalog.web.fo.AccountFO
import com.github.vhromada.catalog.web.utils.AccountUtils
import com.github.vhromada.common.account.entity.Credentials
import com.github.vhromada.common.mapper.Mapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * A class represents test for mapper between [AccountFO] and [Credentials].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogMapperTestConfiguration::class])
class AccountMapperIntegrationTest {

    /**
     * Instance of [AccountMapper]
     */
    @Autowired
    private lateinit var mapper: Mapper<AccountFO, Credentials>

    /**
     * Test method for [AccountMapper.map].
     */
    @Test
    fun map() {
        val account = AccountUtils.getAccount()

        val credentials = mapper.map(source = account)

        AccountUtils.assertAccountDeepEquals(expected = account, actual = credentials)
    }

}
