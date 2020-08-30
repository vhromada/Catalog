package com.github.vhromada.catalog.rest.controller

import com.github.vhromada.common.entity.Language
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * A class represents controller for language.
 *
 * @author Vladimir Hromada
 */
@RestController("languageController")
@RequestMapping("/catalog/languages")
class LanguageController {

    /**
     * Returns list of languages.
     *
     * @return list of languages
     */
    @GetMapping
    fun getLanguages(): List<Language> {
        return Language.values().asList()
    }

    /**
     * Returns list of subtitles.
     *
     * @return list of subtitles
     */
    @GetMapping("/subtitles")
    fun getSubtitles(): List<Language> {
        return listOf(Language.CZ, Language.EN)
    }

}
