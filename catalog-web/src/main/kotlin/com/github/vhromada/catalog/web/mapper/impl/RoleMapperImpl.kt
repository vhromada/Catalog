package com.github.vhromada.catalog.web.mapper.impl

import com.github.vhromada.catalog.web.fo.RoleFO
import com.github.vhromada.catalog.web.mapper.RoleMapper
import com.github.vhromada.common.account.entity.UpdateRoles
import org.springframework.stereotype.Component

/**
 * A class represents implementation of mapper for roles.
 *
 * @author Vladimir Hromada
 */
@Component("webRoleMapper")
class RoleMapperImpl : RoleMapper {

    override fun map(source: RoleFO): UpdateRoles {
        return UpdateRoles(roles = source.roles)
    }

}