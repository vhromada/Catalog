package cz.vhromada.catalog.repository

import cz.vhromada.catalog.domain.Program
import org.springframework.data.jpa.repository.JpaRepository

/**
 * An interface represents repository for programs.
 *
 * @author Vladimir Hromada
 */
interface ProgramRepository : JpaRepository<Program, Int>
