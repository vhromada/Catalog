package com.github.vhromada.catalog.web.mapper

import com.github.vhromada.catalog.web.fo.RoleFO
import com.github.vhromada.common.account.entity.UpdateRoles
import com.github.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Component

/**
 * A class represents mapper for roles.
 *
 * @author Vladimir Hromada
 */
@Component("webRoleMapper")
class RoleMapper : Mapper<RoleFO, UpdateRoles> {

    override fun map(source: RoleFO): UpdateRoles {
        return UpdateRoles(roles = source.roles)
    }

    override fun mapBack(source: UpdateRoles): RoleFO {
        throw UnsupportedOperationException("Not supported")
    }

}
