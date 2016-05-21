package cz.vhromada.catalog.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.dao.ProgramDAO;
import cz.vhromada.catalog.dao.entities.Program;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of DAO for programs.
 *
 * @author Vladimir Hromada
 */
@Component("programDAO")
public class ProgramDAOImpl extends AbstractDAO<Program> implements ProgramDAO {

    /**
     * Creates a new instance of ProgramDAOImpl.
     *
     * @param entityManager entity manager
     * @throws IllegalArgumentException if entity manager is null
     */
    @Autowired
    public ProgramDAOImpl(final EntityManager entityManager) {
        super(entityManager, Program.class, "Program");
    }

    /**
     * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException {@inheritDoc}
     */
    @Override
    public List<Program> getPrograms() {
        return getData(Program.SELECT_PROGRAMS);
    }

    /**
     * @throws IllegalArgumentException                                {@inheritDoc}
     * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException {@inheritDoc}
     */
    @Override
    public Program getProgram(final Integer id) {
        return getItem(id);
    }

    /**
     * @throws IllegalArgumentException                                {@inheritDoc}
     * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException {@inheritDoc}
     */
    @Override
    public void add(final Program program) {
        addItem(program);
    }

    /**
     * @throws IllegalArgumentException                                {@inheritDoc}
     * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException {@inheritDoc}
     */
    @Override
    public void update(final Program program) {
        updateItem(program);
    }

    /**
     * @throws IllegalArgumentException                                {@inheritDoc}
     * @throws cz.vhromada.catalog.dao.exceptions.DataStorageException {@inheritDoc}
     */
    @Override
    public void remove(final Program program) {
        removeItem(program);
    }

}
