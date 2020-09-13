package com.github.vhromada.catalog.rest.controller

import com.github.vhromada.common.account.entity.Credentials
import com.github.vhromada.common.account.entity.UpdateRoles
import com.github.vhromada.common.account.facade.AccountFacade
import com.github.vhromada.common.web.controller.AbstractController
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

/**
 * A class represents controller for accounts.
 *
 * @author Vladimir Hromada
 */
@RestController("accountController")
@RequestMapping("/catalog/accounts")
class AccountController(private val accountFacade: AccountFacade) : AbstractController() {

    /**
     * Adds account.
     * <br></br>
     * Validation errors:
     *
     *  * Username is null
     *  * Password is null
     *
     * @param credentials credentials
     */
    @PutMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    fun add(@RequestBody credentials: Credentials) {
        processResult(accountFacade.add(credentials))
    }

    /**
     * Updates credentials.
     * <br></br>
     * Validation errors:
     *
     *  * Username is null
     *  * Password is null
     *
     * @param credentials new value of credentials
     */
    @PostMapping("/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@RequestBody credentials: Credentials) {
        processResult(accountFacade.update(credentials))
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
     * @param roles new value of roles
     */
    @PostMapping("/roles")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateRoles(@RequestBody roles: UpdateRoles) {
        processResult(accountFacade.updateRoles(roles))
    }

}
