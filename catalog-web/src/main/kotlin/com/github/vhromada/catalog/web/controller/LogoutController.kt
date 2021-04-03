package com.github.vhromada.catalog.web.controller

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * A class represents controller for logout.
 *
 * @author Vladimir Hromada
 */
@Controller("logoutController")
@RequestMapping("logout")
class LogoutController {

    /**
     * Process logout.
     *
     * @param request  HTTP request
     * @param response HTTP response
     * @return view for redirect to page for login
     */
    @PostMapping
    fun logout(request: HttpServletRequest, response: HttpServletResponse): String {
        val auth = SecurityContextHolder.getContext().authentication
        if (auth != null) {
            SecurityContextLogoutHandler().logout(request, response, auth)
        }

        return "redirect:/login"
    }

}
