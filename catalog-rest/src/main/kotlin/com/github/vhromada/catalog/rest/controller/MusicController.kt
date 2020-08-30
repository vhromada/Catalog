package com.github.vhromada.catalog.rest.controller

import com.github.vhromada.catalog.entity.Music
import com.github.vhromada.catalog.facade.MusicFacade
import com.github.vhromada.common.web.controller.AbstractController
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

/**
 * A class represents controller for music.
 *
 * @author Vladimir Hromada
 */
@RestController("musicController")
@RequestMapping("/catalog/music")
class MusicController(private val musicFacade: MusicFacade) : AbstractController() {

    /**
     * Returns list of music.
     *
     * @return list of music
     */
    @GetMapping
    fun getMusic(): List<Music> {
        return processResult(musicFacade.getAll())!!
    }

    /**
     * Creates new data.
     */
    @PostMapping("/new")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun newData() {
        processResult(musicFacade.newData())
    }

    /**
     * Returns music with ID or null if there isn't such music.
     *
     * @param id ID
     * @return music with ID or null if there isn't such music
     */
    @GetMapping("/{id}")
    fun getMusic(@PathVariable("id") id: Int): Music {
        return processResult(musicFacade.get(id))!!
    }

    /**
     * Adds music. Sets new ID and position.
     * <br></br>
     * Validation errors:
     *
     *  * ID isn't null
     *  * Position isn't null
     *  * Name is null
     *  * Name is empty string
     *  * URL to english Wikipedia page about music is null
     *  * URL to czech Wikipedia page about music is null
     *  * Count of media isn't positive number
     *  * Other data is null
     *  * Note is null
     *
     * @param music music
     */
    @PutMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    fun add(@RequestBody music: Music) {
        processResult(musicFacade.add(music))
    }

    /**
     * Updates music.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Position is null
     *  * Name is null
     *  * Name is empty string
     *  * URL to english Wikipedia page about music is null
     *  * URL to czech Wikipedia page about music is null
     *  * Count of media isn't positive number
     *  * Other data is null
     *  * Note is null
     *  * Music doesn't exist in data storage
     *
     * @param music new value of music
     */
    @PostMapping("/update")
    fun update(@RequestBody music: Music) {
        processResult(musicFacade.update(music))
    }

    /**
     * Removes music.
     * <br></br>
     * Validation errors:
     *
     *  * Music doesn't exist in data storage
     *
     * @param id ID
     */
    @DeleteMapping("/remove/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun remove(@PathVariable("id") id: Int) {
        val music = Music(id = id, name = null, wikiEn = null, wikiCz = null, mediaCount = null, note = null, position = null)
        processResult(musicFacade.remove(music))
    }

    /**
     * Duplicates music.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Music doesn't exist in data storage
     *
     * @param music music
     */
    @PostMapping("/duplicate")
    @ResponseStatus(HttpStatus.CREATED)
    fun duplicate(@RequestBody music: Music) {
        processResult(musicFacade.duplicate(music))
    }

    /**
     * Moves music in list one position up.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Music can't be moved up
     *  * Music doesn't exist in data storage
     *
     * @param music music
     */
    @PostMapping("/moveUp")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun moveUp(@RequestBody music: Music) {
        processResult(musicFacade.moveUp(music))
    }

    /**
     * Moves music in list one position down.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Music can't be moved down
     *  * Music doesn't exist in data storage
     *
     * @param music music
     */
    @PostMapping("/moveDown")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun moveDown(@RequestBody music: Music) {
        processResult(musicFacade.moveDown(music))
    }

    /**
     * Updates positions.
     */
    @PostMapping("/updatePositions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updatePositions() {
        processResult(musicFacade.updatePositions())
    }

    /**
     * Returns total count of media.
     *
     * @return total count of media
     */
    @GetMapping("/totalMedia")
    fun getTotalMediaCount(): Int {
        return processResult(musicFacade.getTotalMediaCount())!!
    }

    /**
     * Returns total length of all music.
     *
     * @return total length of all music
     */
    @GetMapping("/totalLength")
    fun getTotalLength(): Int {
        return processResult(musicFacade.getTotalLength())!!.length
    }

    /**
     * Returns count of songs from all music.
     *
     * @return count of songs from all music
     */
    @GetMapping("/songsCount")
    fun geSongsCount(): Int {
        return processResult(musicFacade.getSongsCount())!!
    }

}
