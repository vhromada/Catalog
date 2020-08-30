package com.github.vhromada.catalog.web.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

/**
 * A class represents controller for access denied.
 *
 * @author Vladimir Hromada
 */
@Controller("accessDeniedController")
class AccessDeniedController {

    /**
     * Shows page for access denied.
     *
     * @param model model
     * @return view for page for access denied
     */
    @GetMapping("/access-denied")
    fun login(model: Model): String {
        model.addAttribute("errorMessage", "Access denied")
        model.addAttribute("title", "Error")
        model.addAttribute("inner", false)

        return "errors"
    }

}
