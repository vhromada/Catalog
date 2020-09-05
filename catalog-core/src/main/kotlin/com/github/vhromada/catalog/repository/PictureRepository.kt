package com.github.vhromada.catalog.repository

import com.github.vhromada.catalog.domain.Picture
import org.springframework.data.jpa.repository.JpaRepository

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
    fun findByAuditCreatedUser(user: String): List<Picture>

}
