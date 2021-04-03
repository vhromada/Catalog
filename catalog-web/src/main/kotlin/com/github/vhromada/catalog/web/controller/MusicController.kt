package com.github.vhromada.catalog.web.controller

import com.github.vhromada.catalog.entity.Music
import com.github.vhromada.catalog.facade.MusicFacade
import com.github.vhromada.catalog.facade.SongFacade
import com.github.vhromada.catalog.web.domain.MusicData
import com.github.vhromada.catalog.web.fo.MusicFO
import com.github.vhromada.common.entity.Time
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
 * A class represents controller for music.
 *
 * @author Vladimir Hromada
 */
@Controller("musicController")
@RequestMapping("/music")
class MusicController(
    private val musicFacade: MusicFacade,
    private val songFacade: SongFacade,
    private val musicMapper: Mapper<Music, MusicFO>
) : AbstractResultController() {

    /**
     * Process new data.
     *
     * @return view for redirect to page with list of music
     */
    @GetMapping("/new")
    fun processNew(): String {
        processResults(musicFacade.newData())

        return LIST_REDIRECT_URL
    }

    /**
     * Shows page with list of music.
     *
     * @param model model
     * @return view for page with list of music
     */
    @GetMapping("", "/list")
    fun showList(model: Model): String {
        val musicResult = musicFacade.getAll()
        val mediaCountResult = musicFacade.getTotalMediaCount()
        val songsCountResult = musicFacade.getSongsCount()
        val totalLengthResult = musicFacade.getTotalLength()
        processResults(musicResult, mediaCountResult, songsCountResult, totalLengthResult)

        model.addAttribute("music", musicResult.data)
        model.addAttribute("mediaCount", mediaCountResult.data)
        model.addAttribute("songsCount", songsCountResult.data)
        model.addAttribute("totalLength", totalLengthResult.data)
        model.addAttribute("title", "Music")

        return "music/index"
    }

    /**
     * Shows page with detail of music.
     *
     * @param model model
     * @param id    ID of showing music
     * @return view for page with detail of music
     */
    @GetMapping("/{id}/detail")
    fun showDetail(model: Model, @PathVariable("id") id: Int): String {
        val musicResult = musicFacade.get(id = id)
        val songsResult = songFacade.find(parent = id)
        processResults(musicResult, songsResult)

        val length = songsResult.data!!.sumBy { it.length!! }
        model.addAttribute("music", MusicData(music = musicResult.data!!, songsCount = songsResult.data!!.size, totalLength = Time(length = length)))
        model.addAttribute("title", "Music detail")

        return "music/detail"
    }

    /**
     * Shows page for adding music.
     *
     * @param model model
     * @return view for page for adding music
     */
    @GetMapping("/add")
    fun showAdd(model: Model): String {
        val music = MusicFO(
            id = null,
            name = null,
            wikiEn = null,
            wikiCz = null,
            mediaCount = null,
            note = null,
            position = null
        )
        return createFormView(model = model, music = music, title = "Add music", action = "add")
    }

    /**
     * Process adding music.
     *
     * @param model  model
     * @param music  FO for music
     * @param errors errors
     * @return view for redirect to page with list of music (no errors) or view for page for adding music (errors)
     * @throws IllegalArgumentException if ID isn't null
     */
    @PostMapping(value = ["/add"], params = ["create"])
    fun processAdd(model: Model, @ModelAttribute("music") @Valid music: MusicFO, errors: Errors): String {
        require(music.id == null) { "ID must be null." }

        if (errors.hasErrors()) {
            return createFormView(model = model, music = music, title = "Add music", action = "add")
        }
        processResults(musicFacade.add(data = musicMapper.mapBack(source = music)))

        return LIST_REDIRECT_URL
    }

    /**
     * Cancel adding music.
     *
     * @return view for redirect to page with list of music
     */
    @PostMapping(value = ["/add"], params = ["cancel"])
    fun cancelAdd(): String {
        return LIST_REDIRECT_URL
    }

    /**
     * Shows page for editing music.
     *
     * @param model model
     * @param id    ID of editing music
     * @return view for page for editing music
     */
    @GetMapping("/edit/{id}")
    fun showEdit(model: Model, @PathVariable("id") id: Int): String {
        val result = musicFacade.get(id)
        processResults(result)

        return createFormView(model = model, music = musicMapper.map(source = result.data!!), title = "Edit music", action = "edit")
    }

    /**
     * Process editing music.
     *
     * @param model  model
     * @param music  FO for music
     * @param errors errors
     * @return view for redirect to page with list of music (no errors) or view for page for editing music (errors)
     * @throws IllegalArgumentException if ID is null
     */
    @PostMapping(value = ["/edit"], params = ["update"])
    fun processEdit(model: Model, @ModelAttribute("music") @Valid music: MusicFO, errors: Errors): String {
        require(music.id != null) { "ID mustn't be null." }

        if (errors.hasErrors()) {
            return createFormView(model = model, music = music, title = "Edit music", action = "edit")
        }
        processResults(musicFacade.update(data = musicMapper.mapBack(source = music)))

        return LIST_REDIRECT_URL
    }

    /**
     * Process editing music.
     *
     * @return view for redirect to page with list of music
     */
    @PostMapping(value = ["/edit"], params = ["cancel"])
    fun cancelEdit(): String {
        return LIST_REDIRECT_URL
    }

    /**
     * Process duplicating music.
     *
     * @param id ID of duplicating music
     * @return view for redirect to page with list of music
     */
    @GetMapping("/duplicate/{id}")
    fun processDuplicate(@PathVariable("id") id: Int): String {
        processResults(musicFacade.duplicate(id = id))

        return LIST_REDIRECT_URL
    }

    /**
     * Process removing music.
     *
     * @param id ID of removing music
     * @return view for redirect to page with list of music
     */
    @GetMapping("/remove/{id}")
    fun processRemove(@PathVariable("id") id: Int): String {
        processResults(musicFacade.remove(id = id))

        return LIST_REDIRECT_URL
    }

    /**
     * Process moving music up.
     *
     * @param id ID of moving music
     * @return view for redirect to page with list of music
     */
    @GetMapping("/moveUp/{id}")
    fun processMoveUp(@PathVariable("id") id: Int): String {
        processResults(musicFacade.moveUp(id = id))

        return LIST_REDIRECT_URL
    }

    /**
     * Process moving music down.
     *
     * @param id ID of moving music
     * @return view for redirect to page with list of music
     */
    @GetMapping("/moveDown/{id}")
    fun processMoveDown(@PathVariable("id") id: Int): String {
        processResults(musicFacade.moveDown(id = id))

        return LIST_REDIRECT_URL
    }

    /**
     * Process updating positions.
     *
     * @return view for redirect to page with list of music
     */
    @GetMapping("/update")
    fun processUpdatePositions(): String {
        processResults(musicFacade.updatePositions())

        return LIST_REDIRECT_URL
    }

    /**
     * Returns page's view with form.
     *
     * @param model  model
     * @param music  FO for music
     * @param title  page's title
     * @param action action
     * @return page's view with form
     */
    private fun createFormView(model: Model, music: MusicFO, title: String, action: String): String {
        model.addAttribute("music", music)
        model.addAttribute("title", title)
        model.addAttribute("action", action)

        return "music/form"
    }

    companion object {

        /**
         * Redirect URL to list
         */
        private const val LIST_REDIRECT_URL = "redirect:/music/list"

    }

}
