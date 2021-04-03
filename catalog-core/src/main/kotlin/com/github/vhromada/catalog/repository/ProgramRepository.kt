package com.github.vhromada.catalog.repository

import com.github.vhromada.catalog.domain.Program
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

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
    fun findByCreatedUser(user: String): List<Program>

    /**
     * Returns program with ID created by user.
     *
     * @param id   ID
     * @param user user's UUID
     * @return program with ID created by user
     */
    fun findByIdAndCreatedUser(id: Int, user: String): Optional<Program>

}
