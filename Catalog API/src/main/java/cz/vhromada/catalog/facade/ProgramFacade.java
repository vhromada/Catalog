package cz.vhromada.catalog.facade;

import java.util.List;

import cz.vhromada.catalog.facade.to.ProgramTO;

/**
 * An interface represents facade for programs.
 *
 * @author Vladimir Hromada
 */
public interface ProgramFacade {

    /**
     * Creates new data.
     *
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    void newData();

    /**
     * Returns list of TO for program.
     *
     * @return list of TO for program
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    List<ProgramTO> getPrograms();

    /**
     * Returns TO for program with ID or null if there isn't such TO for program.
     *
     * @param id ID
     * @return TO for program with ID or null if there isn't such TO for program
     * @throws IllegalArgumentException                                       if ID is null
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    ProgramTO getProgram(Integer id);

    /**
     * Adds TO for program. Sets new ID and position.
     *
     * @param program TO for program
     * @throws IllegalArgumentException                                       if TO for program is null
     * @throws cz.vhromada.validators.exceptions.ValidationException          if ID isn't null
     *                                                                        or name is null
     *                                                                        or name is empty string
     *                                                                        or URL to english Wikipedia page about program is null
     *                                                                        or URL to czech Wikipedia page about program is null
     *                                                                        or count of media isn't positive number
     *                                                                        or other data is null
     *                                                                        or note is null
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    void add(ProgramTO program);

    /**
     * Updates TO for program.
     *
     * @param program new value of TO for program
     * @throws IllegalArgumentException                                       if TO for program is null
     * @throws cz.vhromada.validators.exceptions.ValidationException          if ID is null
     *                                                                        or name is null
     *                                                                        or name is empty string
     *                                                                        or URL to english Wikipedia page about program is null
     *                                                                        or URL to czech Wikipedia page about program is null
     *                                                                        or count of media isn't positive number
     *                                                                        or other data is null
     *                                                                        or note is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException      if TO for program doesn't exist in data storage
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    void update(ProgramTO program);

    /**
     * Removes TO for program.
     *
     * @param program TO for program
     * @throws IllegalArgumentException                                       if TO for program is null
     * @throws cz.vhromada.validators.exceptions.ValidationException          if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException      if TO for program doesn't exist in data storage
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    void remove(ProgramTO program);

    /**
     * Duplicates TO for program.
     *
     * @param program TO for program
     * @throws IllegalArgumentException                                       if TO for program is null
     * @throws cz.vhromada.validators.exceptions.ValidationException          if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException      if TO for program doesn't exist in data storage
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    void duplicate(ProgramTO program);

    /**
     * Moves TO for program in list one position up.
     *
     * @param program TO for program
     * @throws IllegalArgumentException                                       if TO for program is null
     * @throws cz.vhromada.validators.exceptions.ValidationException          if ID is null
     *                                                                        or TO for program can't be moved up
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException      if TO for program doesn't exist in data storage
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    void moveUp(ProgramTO program);

    /**
     * Moves TO for program in list one position down.
     *
     * @param program TO for program
     * @throws IllegalArgumentException                                       if TO for program is null
     * @throws cz.vhromada.validators.exceptions.ValidationException          if ID is null
     *                                                                        or TO for program can't be moved down
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException      if TO for program doesn't exist in data storage
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    void moveDown(ProgramTO program);

    /**
     * Returns true if TO for program exists.
     *
     * @param program TO for program
     * @return true if TO for program exists
     * @throws IllegalArgumentException                                       if TO for program is null
     * @throws cz.vhromada.validators.exceptions.ValidationException          if ID is null
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    boolean exists(ProgramTO program);

    /**
     * Updates positions.
     *
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    void updatePositions();

    /**
     * Returns total count of media.
     *
     * @return total count of media
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    int getTotalMediaCount();

}
