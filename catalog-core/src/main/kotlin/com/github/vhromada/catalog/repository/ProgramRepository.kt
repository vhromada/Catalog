package com.github.vhromada.catalog.repository

import com.github.vhromada.catalog.domain.Program
import org.springframework.data.jpa.repository.JpaRepository

/**
 * An interface represents repository for programs.
 *
 * @author Vladimir Hromada
 */
interface ProgramRepository : JpaRepository<Program, Int> {

    /**
     * Returns all programs created by user.
     *
     * @param user user's UUID
     * @return all programs created by user
     */
    fun findByAuditCreatedUser(user: String): List<Program>

}
