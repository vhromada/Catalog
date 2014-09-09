package cz.vhromada.catalog.dao;

import java.util.List;

import cz.vhromada.catalog.dao.entities.Program;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;

/**
 * An interface represents DAO for programs.
 *
 * @author Vladimir Hromada
 */
public interface ProgramDAO {

	/**
	 * Returns list of programs.
	 *
	 * @return list of programs
	 * @throws DataStorageException if there was error with working with data storage
	 */
	List<Program> getPrograms();

	/**
	 * Returns program with ID or null if there isn't such program.
	 *
	 * @param id ID
	 * @return program with ID or null if there isn't such program
	 * @throws IllegalArgumentException if ID is null
	 * @throws DataStorageException     if there was error with working with data storage
	 */
	Program getProgram(Integer id);

	/**
	 * Adds program. Sets new ID and position.
	 *
	 * @param program program
	 * @throws IllegalArgumentException if program is null
	 * @throws DataStorageException     if there was error with working with data storage
	 */
	void add(Program program);

	/**
	 * Updates program.
	 *
	 * @param program program
	 * @throws IllegalArgumentException if program is null
	 * @throws DataStorageException     if there was error with working with data storage
	 */
	void update(Program program);

	/**
	 * Removes program.
	 *
	 * @param program program
	 * @throws IllegalArgumentException if program is null
	 * @throws DataStorageException     if there was error with working with data storage
	 */
	void remove(Program program);

}
