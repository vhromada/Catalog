package com.github.vhromada.catalog.rest.controller

import com.github.vhromada.catalog.entity.Music
import com.github.vhromada.catalog.entity.Song
import com.github.vhromada.catalog.facade.SongFacade
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
 * A class represents controller for songs.
 *
 * @author Vladimir Hromada
 */
@RestController("songController")
@RequestMapping("/catalog/music/{musicId}/songs")
class SongController(private val songFacade: SongFacade) : AbstractController() {

    /**
     * Returns song with ID or null if there isn't such song.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *
     * @param musicId music ID
     * @param songId  song ID
     * @return song with ID or null if there isn't such song
     */
    @GetMapping("/{songId}")
    fun getSong(@PathVariable("musicId") musicId: Int,
                @PathVariable("songId") songId: Int): Song? {
        return processResult(songFacade.get(songId))
    }

    /**
     * Adds song. Sets new ID and position.
     * <br></br>
     * Validation errors:
     *
     *  * Music ID is null
     *  * Music doesn't exist in data storage
     *  * Song ID isn't null
     *  * Song position isn't null
     *  * Name is null
     *  * Name is empty string
     *  * Length of song is negative value
     *  * Note is null
     *
     * @param musicId music ID
     * @param song    song
     */
    @PutMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    fun add(@PathVariable("musicId") musicId: Int,
            @RequestBody song: Song) {
        val music = Music(id = musicId, name = null, wikiEn = null, wikiCz = null, mediaCount = null, note = null, position = null)
        processResult(songFacade.add(music, song))
    }

    /**
     * Updates song.
     * <br></br>
     * Validation errors:
     *
     *  * ID isn't null
     *  * Position is null
     *  * Name is null
     *  * Name is empty string
     *  * Length of song is negative value
     *  * Note is null
     *  * Song doesn't exist in data storage
     *
     * @param musicId music ID
     * @param song    new value of song
     */
    @PostMapping("/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@PathVariable("musicId") musicId: Int,
               @RequestBody song: Song) {
        processResult(songFacade.update(song))
    }

    /**
     * Removes song.
     * <br></br>
     * Validation errors:
     *
     *  * Song doesn't exist in data storage
     *
     * @param musicId music ID
     * @param id      ID
     */
    @DeleteMapping("/remove/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun remove(@PathVariable("musicId") musicId: Int,
               @PathVariable("id") id: Int) {
        val song = Song(id = id, name = null, length = null, note = null, position = null)
        processResult(songFacade.remove(song))
    }

    /**
     * Duplicates song.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Song doesn't exist in data storage
     *
     * @param musicId music ID
     * @param song    song
     */
    @PostMapping("/duplicate")
    @ResponseStatus(HttpStatus.CREATED)
    fun duplicate(@PathVariable("musicId") musicId: Int,
                  @RequestBody song: Song) {
        processResult(songFacade.duplicate(song))
    }

    /**
     * Moves song in list one position up.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Song can't be moved up
     *  * Song doesn't exist in data storage
     *
     * @param musicId music ID
     * @param song    song
     */
    @PostMapping("/moveUp")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun moveUp(@PathVariable("musicId") musicId: Int,
               @RequestBody song: Song) {
        processResult(songFacade.moveUp(song))
    }

    /**
     * Moves song in list one position down.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Song can't be moved down
     *  * Song doesn't exist in data storage
     *
     * @param musicId music ID
     * @param song    song
     */
    @PostMapping("/moveDown")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun moveDown(@PathVariable("musicId") musicId: Int,
                 @RequestBody song: Song) {
        processResult(songFacade.moveDown(song))
    }

    /**
     * Returns list of songs for specified music.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Music doesn't exist in data storage
     *
     * @param musicId music ID
     * @return list of songs for specified music
     */
    @GetMapping
    fun findSongsByMusic(@PathVariable("musicId") musicId: Int): List<Song> {
        val music = Music(id = musicId, name = null, wikiEn = null, wikiCz = null, mediaCount = null, note = null, position = null)
        return processResult(songFacade.find(music))!!
    }

}
