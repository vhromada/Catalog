package com.github.vhromada.catalog.web.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

/**
 * A class represents controller for index.
 *
 * @author Vladimir Hromada
 */
@Controller("catalogController")
@RequestMapping
class CatalogController {

    /**
     * Show index page.
     *
     * @param model model
     * @return view for index page
     */
    @GetMapping
    fun showIndex(model: Model): String {
        model.addAttribute("title", "Catalog")
        model.addAttribute("inner", false)

        return "index"
    }

}
