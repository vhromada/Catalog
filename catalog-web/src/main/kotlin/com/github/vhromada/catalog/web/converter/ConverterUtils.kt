package com.github.vhromada.catalog.web.converter

import com.github.vhromada.catalog.entity.Game
import com.github.vhromada.catalog.entity.Genre
import com.github.vhromada.catalog.entity.Medium
import com.github.vhromada.catalog.entity.Program
import com.github.vhromada.catalog.entity.Season
import com.github.vhromada.common.entity.Language
import com.github.vhromada.common.entity.Time

/**
 * A class represents utility class for converters.
 *
 * @author Vladimir Hromada
 */
object ConverterUtils {

    /**
     * Converts length.
     *
     * @param length length
     * @return converted length
     */
    @JvmStatic
    fun convertLength(length: Int): String {
        return Time(length = length).toString()
    }

    /**
     * Converts languages.
     *
     * @param languages languages
     * @return converted languages
     */
    @JvmStatic
    @Suppress("DuplicatedCode")
    fun convertLanguages(languages: List<Language>): String {
        if (languages.isEmpty()) {
            return ""
        }

        val result = StringBuilder()
        for (language in languages) {
            result.append(language)
            result.append(", ")
        }

        return result.substring(0, result.length - 2)
    }

    /**
     * Converts movie's media.
     *
     * @param media media
     * @return converted movie's media
     */
    @JvmStatic
    fun convertMedia(media: List<Medium>): String {
        val result = StringBuilder()
        for (medium in media) {
            result.append(Time(medium.length!!))
            result.append(", ")
        }

        return result.substring(0, result.length - 2)
    }

    /**
     * Converts movie's total length.
     *
     * @param media media
     * @return converted movie's total length
     */
    @JvmStatic
    fun convertMovieTotalLength(media: List<Medium>): String {
        var totalLength = 0
        for (medium in media) {
            totalLength += medium.length!!
        }

        return Time(length = totalLength).toString()
    }

    /**
     * Converts game's additional data.
     *
     * @param game game
     * @return converted game's additional data
     */
    @JvmStatic
    fun convertGameAdditionalData(game: Game): String {
        val result = StringBuilder()
        if (game.crack!!) {
            result.append("Crack")
        }
        addToResult(result = result, value = game.serialKey!!, data = "serial key")
        addToResult(result = result, value = game.patch!!, data = "patch")
        addToResult(result = result, value = game.trainer!!, data = "trainer")
        addToResult(result = result, value = game.trainerData!!, data = "data for trainer")
        addToResult(result = result, value = game.editor!!, data = "editor")
        addToResult(result = result, value = game.saves!!, data = "saves")
        if (!game.otherData.isNullOrBlank()) {
            if (result.isNotEmpty()) {
                result.append(", ")
            }
            result.append(game.otherData)
        }

        return result.toString()
    }

    /**
     * Converts content of game's additional data.
     *
     * @param game game
     * @return converted content of game's additional data
     */
    @JvmStatic
    fun convertGameAdditionalDataContent(game: Game): Boolean {
        return convertGameAdditionalData(game = game).isNotEmpty()
    }

    /**
     * Converts season's years.
     *
     * @param season season
     * @return converted season's years
     */
    @JvmStatic
    fun convertSeasonYears(season: Season): String {
        return if (season.startYear === season.endYear) season.startYear.toString() else "${season.startYear} - ${season.endYear}"
    }

    /**
     * Converts program's additional data.
     *
     * @param program program
     * @return converted program's additional data
     */
    @JvmStatic
    fun convertProgramAdditionalData(program: Program): String {
        val result = StringBuilder()
        if (program.crack!!) {
            result.append("Crack")
        }
        addToResult(result = result, value = program.serialKey!!, "serial key")
        if (!program.otherData.isNullOrBlank()) {
            if (result.isNotEmpty()) {
                result.append(", ")
            }
            result.append(program.otherData)
        }

        return result.toString()
    }

    /**
     * Converts content of program's additional data.
     *
     * @param program program
     * @return converted content of program's additional data
     */
    @JvmStatic
    fun convertProgramAdditionalDataContent(program: Program): Boolean {
        return convertProgramAdditionalData(program = program).isNotEmpty()
    }

    /**
     * Converts genres.
     *
     * @param genres genres
     * @return converted genres
     */
    @JvmStatic
    fun convertGenres(genres: List<Genre>): String {
        val result = StringBuilder()
        for (genre in genres) {
            result.append(genre.name)
            result.append(", ")
        }

        return result.substring(0, result.length - 2)
    }

    /**
     * Converts IMDB code.
     *
     * @param imdbCode imdb code
     * @return converted IMDB code
     */
    @JvmStatic
    fun convertImdbCode(imdbCode: Int): String {
        return imdbCode.toString().padStart(7, '0')
    }

    /**
     * Converts roles.
     *
     * @param roles roles
     * @return converted roles
     */
    @JvmStatic
    @Suppress("DuplicatedCode")
    fun convertRoles(roles: List<String>): String {
        if (roles.isEmpty()) {
            return ""
        }

        val result = StringBuilder()
        for (role in roles) {
            result.append(role)
            result.append(", ")
        }

        return result.substring(0, result.length - 2)
    }

    /**
     * Adds data to result.
     *
     * @param result result
     * @param value  value
     * @param data   data
     */
    private fun addToResult(result: StringBuilder, value: Boolean, data: String) {
        if (value) {
            if (result.isEmpty()) {
                result.append(data.substring(0, 1).toUpperCase())
                result.append(data.substring(1))
            } else {
                result.append(", ")
                result.append(data)
            }
        }
    }

}
