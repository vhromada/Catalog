package com.github.vhromada.catalog.repository

import com.github.vhromada.catalog.domain.Picture
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

/**
 * An interface represents repository for pictures.
 *
 * @author Vladimir Hromada
 */
interface PictureRepository : JpaRepository<Picture, Int> {

    /**
     * Returns all pictures created by user.
     *
     * @param user user's UUID
     * @return all pictures created by user
     */
    fun findByCreatedUser(user: String): List<Picture>

    /**
     * Returns picture with ID created by user.
     *
     * @param id   ID
     * @param user user's UUID
     * @return picture with ID created by user
     */
    fun findByIdAndCreatedUser(id: Int, user: String): Optional<Picture>

}
