package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.web.CatalogMapperTestConfiguration
import com.github.vhromada.catalog.web.fo.RoleFO
import com.github.vhromada.common.account.entity.UpdateRoles
import com.github.vhromada.common.mapper.Mapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * A class represents test for mapper between [RoleFO] and [UpdateRoles].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogMapperTestConfiguration::class])
class RoleMapperIntegrationTest {

    /**
     * Instance of [RoleMapper]
     */
    @Autowired
    private lateinit var mapper: Mapper<RoleFO, UpdateRoles>

    /**
     * Test method for [RoleMapper.map].
     */
    @Test
    fun map() {
        val role = RoleFO(roles = listOf("ROLE"))

        val updateRoles = mapper.map(role)

        assertThat(updateRoles).isNotNull
        assertThat(updateRoles.roles).isEqualTo(role.roles)
    }

}
