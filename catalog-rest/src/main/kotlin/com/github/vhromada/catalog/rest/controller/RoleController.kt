package com.github.vhromada.catalog.rest.controller

import com.github.vhromada.common.account.entity.UpdateRoles
import com.github.vhromada.common.account.facade.RoleFacade
import com.github.vhromada.common.entity.Account
import com.github.vhromada.common.web.controller.AbstractController
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

/**
 * A class represents controller for roles.
 *
 * @author Vladimir Hromada
 */
@RestController("roleController")
@RequestMapping("/catalog")
class RoleController(
    private val roleFacade: RoleFacade
) : AbstractController() {

    /**
     * Returns list of roles.
     *
     * @return list of roles
     */
    @GetMapping("/roles")
    fun getRoles(): List<String> {
        return processResult(result = roleFacade.getAll())!!
    }

    /**
     * Updates roles.
     * <br></br>
     * Validation errors:
     *
     *  * Roles are null
     *  * Roles contains null
     *  * Role doesn't exist in data storage
     *
     * @param accountId account ID
     * @param roles     new value of roles
     */
    @PostMapping("/accounts/{accountId}/roles")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateRoles(
        @PathVariable("accountId") accountId: Int,
        @RequestBody roles: UpdateRoles
    ) {
        val account = Account(id = accountId, uuid = null, username = null, password = null, roles = null)
        processResult(result = roleFacade.updateRoles(account = account, roles = roles))
    }

}
