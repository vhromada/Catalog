package com.github.vhromada.catalog.rest.controller

import com.github.vhromada.catalog.common.Format
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * A class represents controller for format.
 *
 * @author Vladimir Hromada
 */
@RestController("formatController")
@RequestMapping("/catalog/formats")
class FormatController {

    /**
     * Returns list of formats.
     *
     * @return list of formats
     */
    @GetMapping
    fun getFormats(): List<Format> {
        return Format.values().asList()
    }

}
