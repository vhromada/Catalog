package com.github.vhromada.catalog.web.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

/**
 * A class represents controller for login.
 *
 * @author Vladimir Hromada
 */
@Controller("loginController")
@RequestMapping("/login")
class LoginController {

    /**
     * Shows page for login.
     *
     * @param model model
     * @return view for page for login
     */
    @GetMapping
    fun login(model: Model): String {
        return createLoginView(model)
    }

    /**
     * Shows page for login with error.
     *
     * @param model model
     * @return view for page for login with error.
     */
    @GetMapping("/error")
    fun loginError(model: Model): String {
        model.addAttribute("loginError", true)

        return createLoginView(model)
    }

    /**
     * Returns page's view for login.
     *
     * @param model  model
     * @return page's view for login
     */
    private fun createLoginView(model: Model): String {
        model.addAttribute("title", "Login")

        return "login/login"
    }

}
