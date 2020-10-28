package com.github.vhromada.catalog.web.controller

import com.github.vhromada.catalog.web.exception.IllegalRequestException
import com.github.vhromada.catalog.web.fo.RoleFO
import com.github.vhromada.common.account.entity.UpdateRoles
import com.github.vhromada.common.account.facade.AccountFacade
import com.github.vhromada.common.account.facade.RoleFacade
import com.github.vhromada.common.entity.Account
import com.github.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.validation.Valid

/**
 * A class represents controller for roles.
 *
 * @author Vladimir Hromada
 */
@Controller("roleController")
@RequestMapping("/accounts/{accountId}/roles")
class RoleController(
        private val accountFacade: AccountFacade,
        private val roleFacade: RoleFacade,
        private val roleMapper: Mapper<RoleFO, UpdateRoles>) : AbstractResultController() {

    /**
     * Shows page for editing roles.
     *
     * @param model     model
     * @param accountId account ID
     * @return view for page for editing account
     * @throws IllegalRequestException if account doesn't exist
     */
    @GetMapping("/edit")
    fun showEdit(model: Model, @PathVariable("accountId") accountId: Int): String {
        val account = getAccount(accountId)

        return createFormView(model, RoleFO(roles = account.roles), accountId)
    }

    /**
     * Process editing roles.
     *
     * @param model     model
     * @param accountId account ID
     * @param role      FO for role
     * @param errors    errors
     * @return view for redirect to page with list of accounts (no errors) or view for page for editing account (errors)
     * @throws IllegalRequestException if account doesn't exist
     */
    @PostMapping(value = ["/edit"], params = ["update"])
    fun processEdit(model: Model, @PathVariable("accountId") accountId: Int, @ModelAttribute("role") @Valid role: RoleFO, errors: Errors): String {
        val account = getAccount(accountId)

        if (errors.hasErrors()) {
            return createFormView(model, role, accountId)
        }
        processResults(roleFacade.updateRoles(account, roleMapper.map(role)))

        return ACCOUNTS_REDIRECT_URL
    }

    /**
     * Cancel editing roles.
     *
     * @return view for redirect to page with list of accounts
     */
    @PostMapping(value = ["/edit"], params = ["cancel"])
    fun processEdit(): String {
        return ACCOUNTS_REDIRECT_URL
    }

    /**
     * Returns page's view with form.
     *
     * @param model     model
     * @param role      FO for role
     * @param accountId account ID
     * @return page's view with form
     */
    private fun createFormView(model: Model, role: RoleFO, accountId: Int): String {
        val roles = roleFacade.getAll()
        processResults(roles)

        model.addAttribute("role", role)
        model.addAttribute("account", accountId)
        model.addAttribute("roles", roles.data)
        model.addAttribute("title", "Edit roles")

        return "roles/form"
    }

    /**
     * Returns account with ID.
     *
     * @param id account ID
     * @return account with ID
     * @throws IllegalRequestException if account doesn't exist
     */
    private fun getAccount(id: Int): Account {
        val result = accountFacade.get(id)
        processResults(result)

        if (result.data != null) {
            return result.data!!
        }

        throw IllegalRequestException(ILLEGAL_REQUEST_MESSAGE)
    }

    companion object {

        /**
         * Message for illegal request
         */
        private const val ILLEGAL_REQUEST_MESSAGE = "Account doesn't exist."

        /**
         * Redirect URL to accounts
         */
        private const val ACCOUNTS_REDIRECT_URL = "redirect:/accounts/list"

    }

}