package com.github.vhromada.catalog.rest.controller

import com.github.vhromada.catalog.entity.Picture
import com.github.vhromada.catalog.facade.PictureFacade
import com.github.vhromada.common.web.controller.AbstractController
import com.github.vhromada.common.web.exception.InputException
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

/**
 * A class represents controller for pictures.
 *
 * @author Vladimir Hromada
 */
@RestController("pictureController")
@RequestMapping("/catalog/pictures")
class PictureController(private val pictureFacade: PictureFacade) : AbstractController() {

    /**
     * Returns list of pictures.
     *
     * @return list of pictures
     */
    @GetMapping
    fun getPictures(): List<Int> {
        return processResult(pictureFacade.getAll())!!.map { it.id!! }
    }

    /**
     * Creates new data.
     *
     * @return result
     */
    @PostMapping("/new")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun newData() {
        processResult(pictureFacade.newData())
    }

    /**
     * Returns picture with ID.
     *
     * @param id ID
     * @return picture
     */
    @GetMapping("/{id}")
    fun getPicture(@PathVariable("id") id: Int): ResponseEntity<Resource> {
        val picture = processResult(pictureFacade.get(id)) ?: throw InputException("PICTURE_NOT_EXIST", "Picture doesn't exist.", HttpStatus.NOT_FOUND)
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"picture.jpg\"")
                .header(HttpHeaders.CONTENT_TYPE, "image/jpg")
                .body(ByteArrayResource(picture.content!!))
    }

    /**
     * Adds picture.
     * <br></br>
     * Validation errors:
     *
     *  * File is empty
     *
     * @param file picture
     * @throws IOException if getting picture content fails
     */
    @PutMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    @Throws(IOException::class)
    fun add(@RequestParam("file") file: MultipartFile) {
        if (file.isEmpty) {
            throw InputException("FILE_EMPTY", "File mustn't be empty.")
        }
        processResult(pictureFacade.add(Picture(id = null, content = file.bytes, position = null)))
    }

    /**
     * Removes picture.
     * <br></br>
     * Validation errors:
     *
     *  * Picture doesn't exist in data storage
     *
     * @param id ID
     */
    @DeleteMapping("/remove/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun remove(@PathVariable("id") id: Int) {
        processResult(pictureFacade.remove(Picture(id = id, content = null, position = null)))
    }

    /**
     * Moves picture in list one position up.
     * <br></br>
     * Validation errors:
     *
     *  * Picture can't be moved up
     *  * Picture doesn't exist in data storage
     *
     * @param id ID
     */
    @PostMapping("/moveUp/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun moveUp(@PathVariable("id") id: Int) {
        processResult(pictureFacade.moveUp(Picture(id = id, content = null, position = null)))
    }

    /**
     * Moves picture in list one position down.
     * <br></br>
     * Validation errors:
     *
     *  * Picture can't be moved down
     *  * Picture doesn't exist in data storage
     *
     * @param id ID
     */
    @PostMapping("/moveDown/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun moveDown(@PathVariable("id") id: Int) {
        processResult(pictureFacade.moveDown(Picture(id = id, content = null, position = null)))
    }

    /**
     * Updates positions.
     */
    @PostMapping("/updatePositions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updatePositions() {
        processResult(pictureFacade.updatePositions())
    }

}

