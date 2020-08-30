package com.github.vhromada.catalog.web.controller

import com.github.vhromada.catalog.entity.Music
import com.github.vhromada.catalog.entity.Song
import com.github.vhromada.catalog.facade.MusicFacade
import com.github.vhromada.catalog.facade.SongFacade
import com.github.vhromada.catalog.web.exception.IllegalRequestException
import com.github.vhromada.catalog.web.fo.SongFO
import com.github.vhromada.catalog.web.mapper.SongMapper
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.validation.Valid

/**
 * A class represents controller for songs.
 *
 * @author Vladimir Hromada
 */
@Controller("songController")
@RequestMapping("/music/{musicId}/songs")
class SongController(
        private val musicFacade: MusicFacade,
        private val songFacade: SongFacade,
        private val songMapper: SongMapper) : AbstractResultController() {

    /**
     * Shows page with list of songs.
     *
     * @param model   model
     * @param musicId music ID
     * @return view for page with list of songs
     * @throws IllegalRequestException  if music doesn't exist
     */
    @GetMapping("", "/list")
    fun showList(model: Model, @PathVariable("musicId") musicId: Int): String {
        val music = getMusic(musicId)

        val result = songFacade.find(music)
        processResults(result)

        model.addAttribute("songs", result.data)
        model.addAttribute("music", musicId)
        model.addAttribute("title", "Songs")

        return "song/index"
    }

    /**
     * Shows page with detail of song.
     *
     * @param model   model
     * @param musicId music ID
     * @param id      ID of editing song
     * @return view for page with detail of song
     * @throws IllegalRequestException  if music doesn't exist
     * or song doesn't exist
     */
    @GetMapping("/{id}/detail")
    fun showDetail(model: Model, @PathVariable("musicId") musicId: Int, @PathVariable("id") id: Int): String {
        getMusic(musicId)

        val result = songFacade.get(id)
        processResults(result)

        val song = result.data
        if (song != null) {
            model.addAttribute("song", song)
            model.addAttribute("music", musicId)
            model.addAttribute("title", "Song detail")

            return "song/detail"
        } else {
            throw IllegalRequestException(ILLEGAL_REQUEST_MESSAGE)
        }
    }

    /**
     * Shows page for adding song.
     *
     * @param model   model
     * @param musicId music ID
     * @return view for page for adding song
     * @throws IllegalRequestException  if music doesn't exist
     */
    @GetMapping("/add")
    fun showAdd(model: Model, @PathVariable("musicId") musicId: Int): String {
        getMusic(musicId)

        val song = SongFO(id = null,
                name = null,
                length = null,
                note = null,
                position = null)
        return createFormView(model, song, musicId, "Add song", "add")
    }

    /**
     * Process adding song.
     *
     * @param model   model
     * @param musicId music ID
     * @param song    FO for song
     * @param errors  errors
     * @return view for redirect to page with list of songs (no errors) or view for page for adding song (errors)
     * @throws IllegalArgumentException if ID isn't null
     * @throws IllegalRequestException  if music doesn't exist
     */
    @PostMapping(value = ["/add"], params = ["create"])
    fun processAdd(model: Model, @PathVariable("musicId") musicId: Int, @ModelAttribute("song") @Valid song: SongFO, errors: Errors): String {
        require(song.id == null) { "ID must be null." }

        val music = getMusic(musicId)

        if (errors.hasErrors()) {
            return createFormView(model, song, musicId, "Add song", "add")
        }
        processResults(songFacade.add(music, songMapper.mapBack(song)))

        return getListRedirectUrl(musicId)
    }

    /**
     * Cancel adding song.
     *
     * @param musicId music ID
     * @return view for redirect to page with list of songs
     * @throws IllegalRequestException  if music doesn't exist
     */
    @PostMapping(value = ["/add"], params = ["cancel"])
    fun cancelAdd(@PathVariable("musicId") musicId: Int): String {
        return cancel(musicId)
    }

    /**
     * Shows page for editing song.
     *
     * @param model   model
     * @param musicId music ID
     * @param id      ID of editing song
     * @return view for page for editing song
     * @throws IllegalRequestException  if music doesn't exist
     * or song doesn't exist
     */
    @GetMapping("/edit/{id}")
    fun showEdit(model: Model, @PathVariable("musicId") musicId: Int, @PathVariable("id") id: Int): String {
        getMusic(musicId)

        val result = songFacade.get(id)
        processResults(result)

        val song = result.data
        return if (song != null) {
            createFormView(model, songMapper.map(song), musicId, "Edit song", "edit")
        } else {
            throw IllegalRequestException(ILLEGAL_REQUEST_MESSAGE)
        }
    }

    /**
     * Process editing song.
     *
     * @param model   model
     * @param musicId music ID
     * @param song    FO for song
     * @param errors  errors
     * @return view for redirect to page with list of songs (no errors) or view for page for editing song (errors)
     * @throws IllegalArgumentException if ID is null
     * @throws IllegalRequestException  if music doesn't exist
     * or song doesn't exist
     */
    @PostMapping(value = ["/edit"], params = ["update"])
    fun processEdit(model: Model, @PathVariable("musicId") musicId: Int, @ModelAttribute("song") @Valid song: SongFO, errors: Errors): String {
        require(song.id != null) { "ID mustn't be null." }
        getMusic(musicId)

        if (errors.hasErrors()) {
            return createFormView(model, song, musicId, "Edit song", "edit")
        }
        processResults(songFacade.update(processSong(songMapper.mapBack(song))))

        return getListRedirectUrl(musicId)
    }

    /**
     * Cancel editing song.
     *
     * @param musicId music ID
     * @return view for redirect to page with list of songs
     * @throws IllegalRequestException  if music doesn't exist
     */
    @PostMapping("/edit")
    fun cancelEdit(@PathVariable("musicId") musicId: Int): String {
        return cancel(musicId)
    }

    /**
     * Process duplicating song.
     *
     * @param musicId music ID
     * @param id      ID of duplicating song
     * @return view for redirect to page with list of songs
     * @throws IllegalRequestException  if music doesn't exist
     * or song doesn't exist
     */
    @GetMapping("/duplicate/{id}")
    fun processDuplicate(@PathVariable("musicId") musicId: Int, @PathVariable("id") id: Int): String {
        processResults(songFacade.duplicate(getSong(musicId, id)))

        return getListRedirectUrl(musicId)
    }

    /**
     * Process removing song.
     *
     * @param musicId music ID
     * @param id      ID of removing song
     * @return view for redirect to page with list of songs
     * @throws IllegalRequestException  if music doesn't exist
     * or song doesn't exist
     */
    @GetMapping("/remove/{id}")
    fun processRemove(@PathVariable("musicId") musicId: Int, @PathVariable("id") id: Int): String {
        processResults(songFacade.remove(getSong(musicId, id)))

        return getListRedirectUrl(musicId)
    }

    /**
     * Process moving song up.
     *
     * @param musicId music ID
     * @param id      ID of moving song
     * @return view for redirect to page with list of songs
     * @throws IllegalRequestException  if music doesn't exist
     * or song doesn't exist
     */
    @GetMapping("/moveUp/{id}")
    fun processMoveUp(@PathVariable("musicId") musicId: Int, @PathVariable("id") id: Int): String {
        processResults(songFacade.moveUp(getSong(musicId, id)))

        return getListRedirectUrl(musicId)
    }

    /**
     * Process moving song down.
     *
     * @param musicId music ID
     * @param id      ID of moving song
     * @return view for redirect to page with list of songs
     * @throws IllegalRequestException  if music doesn't exist
     * or song doesn't exist
     */
    @GetMapping("/moveDown/{id}")
    fun processMoveDown(@PathVariable("musicId") musicId: Int, @PathVariable("id") id: Int): String {
        processResults(songFacade.moveDown(getSong(musicId, id)))

        return getListRedirectUrl(musicId)
    }

    /**
     * Cancel processing song.
     *
     * @param musicId music ID
     * @return view for redirect to page with list of songs
     * @throws IllegalRequestException  if music doesn't exist
     */
    private fun cancel(musicId: Int): String {
        getMusic(musicId)

        return getListRedirectUrl(musicId)
    }

    /**
     * Returns music.
     *
     * @param id music ID
     * @return music
     * @throws IllegalRequestException if music doesn't exist
     */
    private fun getMusic(id: Int): Music {
        val musicResult = musicFacade.get(id)
        processResults(musicResult)

        return musicResult.data ?: throw IllegalRequestException("Music doesn't exist.")
    }

    /**
     * Returns song with ID.
     *
     * @param musicId music ID
     * @param id      ID
     * @return song with ID
     * @throws IllegalRequestException  if music doesn't exist
     * or song doesn't exist
     */
    private fun getSong(musicId: Int, id: Int): Song {
        getMusic(musicId)

        val song = Song(id = id,
                name = null,
                length = null,
                note = null,
                position = null)

        return processSong(song)
    }

    /**
     * Returns processed song.
     *
     * @param song for processing
     * @return processed song
     * @throws IllegalRequestException if song doesn't exist
     */
    private fun processSong(song: Song): Song {
        val songResult = songFacade.get(song.id!!)
        processResults(songResult)

        if (songResult.data != null) {
            return song
        }

        throw IllegalRequestException(ILLEGAL_REQUEST_MESSAGE)
    }


    /**
     * Returns page's view with form.
     *
     * @param model   model
     * @param song    FO for song
     * @param musicId music ID
     * @param title   page's title
     * @param action  action
     * @return page's view with form
     */
    private fun createFormView(model: Model, song: SongFO, musicId: Int, title: String, action: String): String {
        model.addAttribute("song", song)
        model.addAttribute("music", musicId)
        model.addAttribute("title", title)
        model.addAttribute("action", action)

        return "song/form"
    }

    /**
     * Returns redirect URL to list.
     *
     * @param musicId music ID
     * @return redirect URL to list
     */
    private fun getListRedirectUrl(musicId: Int): String {
        return "redirect:/music/$musicId/songs/list"
    }

    companion object {

        /**
         * Message for illegal request
         */
        private const val ILLEGAL_REQUEST_MESSAGE = "song doesn't exist."

    }

}
