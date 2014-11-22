package cz.vhromada.catalog.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import cz.vhromada.catalog.dao.ProgramDAO;
import cz.vhromada.catalog.dao.entities.Program;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of DAO for programs.
 *
 * @author Vladimir Hromada
 */
@Component("programDAO")
public class ProgramDAOImpl implements ProgramDAO {

    /** Entity manager field */
    private static final String ENTITY_MANAGER_FIELD = "Entity manager";

    /** Program argument */
    private static final String PROGRAM_ARGUMENT = "Program";

    /** ID argument */
    private static final String ID_ARGUMENT = "ID";

    /** Message for {@link DataStorageException} */
    private static final String DATA_STORAGE_EXCEPTION_MESSAGE = "Error in working with ORM.";

    /** Entity manager */
    @Autowired
    private EntityManager entityManager;

    /**
     * Returns entity manager.
     *
     * @return entity manager
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Sets a new value to entity manager.
     *
     * @param entityManager new value
     */
    public void setEntityManager(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException if entity manager isn't set
     * @throws DataStorageException  {@inheritDoc}
     */
    @Override
    public List<Program> getPrograms() {
        Validators.validateFieldNotNull(entityManager, ENTITY_MANAGER_FIELD);

        try {
            return new ArrayList<>(entityManager.createNamedQuery(Program.SELECT_PROGRAMS, Program.class).getResultList());
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if entity manager isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public Program getProgram(final Integer id) {
        Validators.validateFieldNotNull(entityManager, ENTITY_MANAGER_FIELD);
        Validators.validateArgumentNotNull(id, ID_ARGUMENT);

        try {
            return entityManager.find(Program.class, id);
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if entity manager isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public void add(final Program program) {
        Validators.validateFieldNotNull(entityManager, ENTITY_MANAGER_FIELD);
        Validators.validateArgumentNotNull(program, PROGRAM_ARGUMENT);

        try {
            entityManager.persist(program);
            program.setPosition(program.getId() - 1);
            entityManager.merge(program);
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if entity manager isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public void update(final Program program) {
        Validators.validateFieldNotNull(entityManager, ENTITY_MANAGER_FIELD);
        Validators.validateArgumentNotNull(program, PROGRAM_ARGUMENT);

        try {
            entityManager.merge(program);
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if entity manager isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws DataStorageException     {@inheritDoc}
     */
    @Override
    public void remove(final Program program) {
        Validators.validateFieldNotNull(entityManager, ENTITY_MANAGER_FIELD);
        Validators.validateArgumentNotNull(program, PROGRAM_ARGUMENT);

        try {
            if (entityManager.contains(program)) {
                entityManager.remove(program);
            } else {
                entityManager.remove(entityManager.getReference(Program.class, program.getId()));
            }
        } catch (final PersistenceException ex) {
            throw new DataStorageException(DATA_STORAGE_EXCEPTION_MESSAGE, ex);
        }
    }

}
