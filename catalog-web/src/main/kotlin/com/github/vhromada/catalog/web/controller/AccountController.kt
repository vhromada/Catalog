package com.github.vhromada.catalog.web.controller

import com.github.vhromada.catalog.web.fo.AccountFO
import com.github.vhromada.common.account.entity.Credentials
import com.github.vhromada.common.account.facade.AccountFacade
import com.github.vhromada.common.mapper.Mapper
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.validation.Valid

/**
 * A class represents controller for accounts.
 *
 * @author Vladimir Hromada
 */
@Controller("accountController")
@RequestMapping("/accounts")
class AccountController(
    private val accountFacade: AccountFacade,
    private val accountMapper: Mapper<AccountFO, Credentials>
) : AbstractResultController() {

    /**
     * Shows page with list of accounts.
     *
     * @param model model
     * @return view for page with list of accounts
     */
    @GetMapping("", "/list")
    fun showList(model: Model): String {
        val accountsResult = accountFacade.getAll()
        processResults(accountsResult)

        model.addAttribute("accounts", accountsResult.data)
        model.addAttribute("title", "Accounts")

        return "account/index"
    }

    /**
     * Shows page for editing account.
     *
     * @param model model
     * @return view for page for editing account
     */
    @GetMapping("/edit")
    fun showEdit(model: Model): String {
        return createFormView(model = model, account = AccountFO(username = null, password = null, copyPassword = null))
    }

    /**
     * Process editing account.
     *
     * @param model   model
     * @param account FO for account
     * @param errors  errors
     * @return view for redirect to page with list of accounts (no errors) or view for page for editing account (errors)
     */
    @PostMapping(value = ["/edit"], params = ["update"])
    fun processEdit(model: Model, @ModelAttribute("account") @Valid account: AccountFO, errors: Errors): String {
        if (errors.hasErrors()) {
            return createFormView(model = model, account = account)
        }
        processResults(accountFacade.update(credentials = accountMapper.map(source = account)))

        return HOME_PAGE_REDIRECT_URL
    }

    /**
     * Cancel editing account.
     *
     * @return view for redirect to page with list of accounts
     */
    @PostMapping(value = ["/edit"], params = ["cancel"])
    fun processEdit(): String {
        return HOME_PAGE_REDIRECT_URL
    }

    /**
     * Returns page's view with form.
     *
     * @param model   model
     * @param account FO for account
     * @return page's view with form
     */
    private fun createFormView(model: Model, account: AccountFO): String {
        model.addAttribute("account", account)
        model.addAttribute("title", "Edit account")

        return "account/form"
    }

    companion object {

        /**
         * Redirect URL to home page
         */
        private const val HOME_PAGE_REDIRECT_URL = "redirect:/"

    }

}
