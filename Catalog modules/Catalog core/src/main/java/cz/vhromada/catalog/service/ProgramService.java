package cz.vhromada.catalog.service;

import java.util.List;

import cz.vhromada.catalog.dao.entities.Program;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;

/**
 * An interface represents service for programs.
 *
 * @author Vladimir Hromada
 */
public interface ProgramService {

	/**
	 * Creates new data.
	 *
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	void newData();

	/**
	 * Returns list of programs.
	 *
	 * @return list of programs
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	List<Program> getPrograms();

	/**
	 * Returns program with ID or null if there isn't such program.
	 *
	 * @param id ID
	 * @return program with ID or null if there isn't such program
	 * @throws IllegalArgumentException  if ID is null
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	Program getProgram(Integer id);

	/**
	 * Adds program. Sets new ID and position.
	 *
	 * @param program program
	 * @throws IllegalArgumentException  if program is null
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	void add(Program program);

	/**
	 * Updates program.
	 *
	 * @param program new value of program
	 * @throws IllegalArgumentException  if program is null
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	void update(Program program);

	/**
	 * Removes program.
	 *
	 * @param program program
	 * @throws IllegalArgumentException  if program is null
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	void remove(Program program);

	/**
	 * Duplicates program.
	 *
	 * @param program program
	 * @throws IllegalArgumentException  if program is null
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	void duplicate(Program program);

	/**
	 * Moves program in list one position up.
	 *
	 * @param program program
	 * @throws IllegalArgumentException  if program is null
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	void moveUp(Program program);

	/**
	 * Moves program in list one position down.
	 *
	 * @param program program
	 * @throws IllegalArgumentException  if program is null
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	void moveDown(Program program);

	/**
	 * Returns true if program exists.
	 *
	 * @param program program
	 * @return true if program exists
	 * @throws IllegalArgumentException  if program is null
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	boolean exists(Program program);

	/**
	 * Updates positions.
	 *
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	void updatePositions();

	/**
	 * Returns total count of media.
	 *
	 * @return total count of media
	 * @throws ServiceOperationException if there was error in working with DAO tier
	 */
	int getTotalMediaCount();

}
