package cz.vhromada.catalog.repository;

import cz.vhromada.catalog.domain.Program;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * An interface represents repository for programs.
 *
 * @author Vladimir Hromada
 */
public interface ProgramRepository extends JpaRepository<Program, Integer> {
}
