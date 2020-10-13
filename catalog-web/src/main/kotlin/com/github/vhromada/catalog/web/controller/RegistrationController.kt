package com.github.vhromada.catalog.web.controller

import com.github.vhromada.catalog.web.fo.AccountFO
import com.github.vhromada.catalog.web.mapper.AccountMapper
import com.github.vhromada.common.account.facade.AccountFacade
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.validation.Valid

/**
 * A class represents controller for registration.
 *
 * @author Vladimir Hromada
 */
@Controller("registrationController")
@RequestMapping("/registration")
class RegistrationController(
        private val accountFacade: AccountFacade,
        private val accountMapper: AccountMapper) : AbstractResultController() {

    /**
     * Shows page for registration.
     *
     * @param model model
     * @return view for page for registration
     */
    @GetMapping
    fun login(model: Model): String {
        return createFormView(model, AccountFO(username = null, password = null, copyPassword = null))
    }

    /**
     * Process creating account.
     *
     * @param model   model
     * @param account FO for account
     * @param errors  errors
     * @return view for redirect to page with login (no errors) or view for page for creating user (errors)
     */
    @PostMapping(params = ["create"])
    fun processAdd(model: Model, @ModelAttribute("account") @Valid account: AccountFO, errors: Errors): String {
        if (errors.hasErrors()) {
            return createFormView(model, account)
        }
        processResults(accountFacade.add(accountMapper.map(account)))

        return "redirect:/login"
    }

    /**
     * Cancel creating account.
     *
     * @return view for redirect to page with login
     */
    @PostMapping(params = ["cancel"])
    fun cancelAdd(): String {
        return "redirect:/login"
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
        model.addAttribute("title", "Registration")

        return "registration/registration"
    }

}
