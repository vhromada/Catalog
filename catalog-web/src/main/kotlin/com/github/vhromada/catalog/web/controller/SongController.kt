package com.github.vhromada.catalog.web.controller

import com.github.vhromada.catalog.entity.Song
import com.github.vhromada.catalog.facade.MusicFacade
import com.github.vhromada.catalog.facade.SongFacade
import com.github.vhromada.catalog.web.fo.SongFO
import com.github.vhromada.common.mapper.Mapper
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
    private val songFacade: SongFacade,
    private val musicFacade: MusicFacade,
    private val songMapper: Mapper<Song, SongFO>
) : AbstractResultController() {

    /**
     * Shows page with list of songs.
     *
     * @param model   model
     * @param musicId music ID
     * @return view for page with list of songs
     */
    @GetMapping("", "/list")
    fun showList(model: Model, @PathVariable("musicId") musicId: Int): String {
        val result = songFacade.find(parent = musicId)
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
     * @param id      ID of showing song
     * @return view for page with detail of song
     */
    @GetMapping("/{id}/detail")
    fun showDetail(model: Model, @PathVariable("musicId") musicId: Int, @PathVariable("id") id: Int): String {
        val musicResult = musicFacade.get(id = musicId)
        val songResult = songFacade.get(id = id)
        processResults(musicResult, songResult)

        model.addAttribute("song", songResult.data)
        model.addAttribute("music", musicId)
        model.addAttribute("title", "Song detail")

        return "song/detail"
    }

    /**
     * Shows page for adding song.
     *
     * @param model   model
     * @param musicId music ID
     * @return view for page for adding song
     */
    @GetMapping("/add")
    fun showAdd(model: Model, @PathVariable("musicId") musicId: Int): String {
        processResults(musicFacade.get(id = musicId))

        val song = SongFO(
            id = null,
            name = null,
            length = null,
            note = null,
            position = null
        )
        return createFormView(model = model, song = song, musicId = musicId, title = "Add song", action = "add")
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
     */
    @PostMapping(value = ["/add"], params = ["create"])
    fun processAdd(model: Model, @PathVariable("musicId") musicId: Int, @ModelAttribute("song") @Valid song: SongFO, errors: Errors): String {
        require(song.id == null) { "ID must be null." }

        if (errors.hasErrors()) {
            return createFormView(model = model, song = song, musicId = musicId, title = "Add song", action = "add")
        }
        processResults(songFacade.add(parent = musicId, data = songMapper.mapBack(source = song)))

        return getListRedirectUrl(musicId = musicId)
    }

    /**
     * Cancel adding song.
     *
     * @param musicId music ID
     * @return view for redirect to page with list of songs
     */
    @PostMapping(value = ["/add"], params = ["cancel"])
    fun cancelAdd(@PathVariable("musicId") musicId: Int): String {
        return cancel(musicId = musicId)
    }

    /**
     * Shows page for editing song.
     *
     * @param model   model
     * @param musicId music ID
     * @param id      ID of editing song
     * @return view for page for editing song
     */
    @GetMapping("/edit/{id}")
    fun showEdit(model: Model, @PathVariable("musicId") musicId: Int, @PathVariable("id") id: Int): String {
        val musicResult = musicFacade.get(id = musicId)
        val songResult = songFacade.get(id = id)
        processResults(musicResult, songResult)

        return createFormView(model = model, song = songMapper.map(source = songResult.data!!), musicId = musicId, title = "Edit song", action = "edit")
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
     */
    @PostMapping(value = ["/edit"], params = ["update"])
    fun processEdit(model: Model, @PathVariable("musicId") musicId: Int, @ModelAttribute("song") @Valid song: SongFO, errors: Errors): String {
        require(song.id != null) { "ID mustn't be null." }

        if (errors.hasErrors()) {
            return createFormView(model = model, song = song, musicId = musicId, title = "Edit song", action = "edit")
        }
        processResults(musicFacade.get(id = musicId))
        processResults(songFacade.update(data = songMapper.mapBack(source = song)))

        return getListRedirectUrl(musicId = musicId)
    }

    /**
     * Cancel editing song.
     *
     * @param musicId music ID
     * @return view for redirect to page with list of songs
     */
    @PostMapping("/edit")
    fun cancelEdit(@PathVariable("musicId") musicId: Int): String {
        return cancel(musicId = musicId)
    }

    /**
     * Process duplicating song.
     *
     * @param musicId music ID
     * @param id      ID of duplicating song
     * @return view for redirect to page with list of songs
     */
    @GetMapping("/duplicate/{id}")
    fun processDuplicate(@PathVariable("musicId") musicId: Int, @PathVariable("id") id: Int): String {
        processResults(musicFacade.get(id = musicId))

        processResults(songFacade.duplicate(id = id))

        return getListRedirectUrl(musicId = musicId)
    }

    /**
     * Process removing song.
     *
     * @param musicId music ID
     * @param id      ID of removing song
     * @return view for redirect to page with list of songs
     */
    @GetMapping("/remove/{id}")
    fun processRemove(@PathVariable("musicId") musicId: Int, @PathVariable("id") id: Int): String {
        processResults(musicFacade.get(id = musicId))

        processResults(songFacade.remove(id = id))

        return getListRedirectUrl(musicId = musicId)
    }

    /**
     * Process moving song up.
     *
     * @param musicId music ID
     * @param id      ID of moving song
     * @return view for redirect to page with list of songs
     */
    @GetMapping("/moveUp/{id}")
    fun processMoveUp(@PathVariable("musicId") musicId: Int, @PathVariable("id") id: Int): String {
        processResults(musicFacade.get(id = musicId))

        processResults(songFacade.moveUp(id = id))

        return getListRedirectUrl(musicId = musicId)
    }

    /**
     * Process moving song down.
     *
     * @param musicId music ID
     * @param id      ID of moving song
     * @return view for redirect to page with list of songs
     */
    @GetMapping("/moveDown/{id}")
    fun processMoveDown(@PathVariable("musicId") musicId: Int, @PathVariable("id") id: Int): String {
        processResults(musicFacade.get(id = musicId))

        processResults(songFacade.moveDown(id = id))

        return getListRedirectUrl(musicId = musicId)
    }

    /**
     * Cancel processing song.
     *
     * @param musicId music ID
     * @return view for redirect to page with list of songs
     */
    private fun cancel(musicId: Int): String {
        processResults(musicFacade.get(id = musicId))

        return getListRedirectUrl(musicId = musicId)
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

}
