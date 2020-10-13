package com.github.vhromada.catalog.web.controller

import com.github.vhromada.catalog.entity.Picture
import com.github.vhromada.catalog.facade.PictureFacade
import com.github.vhromada.catalog.web.exception.IllegalRequestException
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

/**
 * A class represents controller for pictures.
 *
 * @author Vladimir Hromada
 */
@Controller("pictureController")
@RequestMapping("/pictures")
class PictureController(private val pictureFacade: PictureFacade) : AbstractResultController() {

    /**
     * Process new data.
     *
     * @return view for redirect to page with list of pictures
     */
    @GetMapping("/new")
    fun processNew(): String {
        processResults(pictureFacade.newData())

        return LIST_REDIRECT_URL
    }

    /**
     * Shows page with list of pictures.
     *
     * @param model model
     * @return view for page with list of pictures
     */
    @GetMapping("", "/list")
    fun showList(model: Model): String {
        val picturesResult = pictureFacade.getAll()
        processResults(picturesResult)

        model.addAttribute("pictures", picturesResult.data!!.map { it.id })
        model.addAttribute("title", "Pictures")

        return "picture/index"
    }

    /**
     * Get picture content.
     *
     * @param id ID of picture
     * @return picture content
     */
    @GetMapping("/{id}")
    operator fun get(@PathVariable("id") id: Int): ResponseEntity<Resource> {
        val picture = pictureFacade.get(id)
        processResults(picture)

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"picture.jpg\"")
                .header(HttpHeaders.CONTENT_TYPE, "image/jpg")
                .body(ByteArrayResource(picture.data!!.content!!))
    }

    /**
     * Shows page for adding picture.
     *
     * @param model model
     * @return view for page for adding picture
     */
    @GetMapping("/add")
    fun showAdd(model: Model): String {
        model.addAttribute("title", "Add picture")

        return "picture/form"
    }

    /**
     * Process adding picture.
     *
     * @param file picture
     * @return view for redirect to home page
     * @throws IOException if getting picture content fails
     */
    @PostMapping(value = ["/add"], params = ["create"])
    @Throws(IOException::class)
    fun processAdd(@RequestParam("file") file: MultipartFile): String {
        if (!file.isEmpty) {
            val picture = Picture(id = null,
                    content = file.bytes,
                    position = null)
            processResults(pictureFacade.add(picture))
        }

        return LIST_REDIRECT_URL
    }

    /**
     * Cancel adding picture.
     *
     * @return view for redirect to home page
     */
    @PostMapping(value = ["/add"], params = ["cancel"])
    fun cancelAdd(): String {
        return LIST_REDIRECT_URL
    }

    /**
     * Process removing picture.
     *
     * @param id ID of removing picture
     * @return view for redirect to page with list of pictures
     * @throws IllegalRequestException if picture doesn't exist
     */
    @GetMapping("/remove/{id}")
    fun processRemove(@PathVariable("id") id: Int): String {
        processResults(pictureFacade.remove(getPicture(id)))

        return LIST_REDIRECT_URL
    }

    /**
     * Process moving picture up.
     *
     * @param id ID of moving picture
     * @return view for redirect to page with list of pictures
     * @throws IllegalRequestException if picture doesn't exist
     */
    @GetMapping("/moveUp/{id}")
    fun processMoveUp(@PathVariable("id") id: Int): String {
        processResults(pictureFacade.moveUp(getPicture(id)))

        return LIST_REDIRECT_URL
    }

    /**
     * Process moving picture down.
     *
     * @param id ID of moving picture
     * @return view for redirect to page with list of pictures
     * @throws IllegalRequestException if picture doesn't exist
     */
    @GetMapping("/moveDown/{id}")
    fun processMoveDown(@PathVariable("id") id: Int): String {
        processResults(pictureFacade.moveDown(getPicture(id)))

        return LIST_REDIRECT_URL
    }

    /**
     * Process updating positions.
     *
     * @return view for redirect to page with list of pictures
     */
    @GetMapping("/update")
    fun processUpdatePositions(): String {
        processResults(pictureFacade.updatePositions())

        return LIST_REDIRECT_URL
    }

    /**
     * Returns picture with ID.
     *
     * @param id ID
     * @return picture with ID
     * @throws IllegalRequestException if picture doesn't exist
     */
    private fun getPicture(id: Int): Picture {
        val picture = Picture(id = id,
                content = null,
                position = null)

        val pictureResult = pictureFacade.get(picture.id!!)
        processResults(pictureResult)

        if (pictureResult.data != null) {
            return picture
        }

        throw IllegalRequestException(ILLEGAL_REQUEST_MESSAGE)
    }

    companion object {

        /**
         * Message for illegal request
         */
        private const val ILLEGAL_REQUEST_MESSAGE = "Picture doesn't exist."

        /**
         * Redirect URL to list
         */
        private const val LIST_REDIRECT_URL = "redirect:/pictures/list"

    }

}
