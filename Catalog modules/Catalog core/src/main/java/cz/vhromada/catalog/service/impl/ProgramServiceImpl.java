package cz.vhromada.catalog.service.impl;

import java.util.List;

import cz.vhromada.catalog.dao.ProgramDAO;
import cz.vhromada.catalog.dao.entities.Program;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.catalog.service.ProgramService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of service for programs.
 *
 * @author Vladimir Hromada
 */
@Component("programService")
public class ProgramServiceImpl extends AbstractService<Program> implements ProgramService {

	/** DAO for programs */
	@Autowired
	private ProgramDAO programDAO;

	/** Cache for programs */
	@Value("#{cacheManager.getCache('programCache')}")
	private Cache programCache;

	/**
	 * Returns DAO for programs.
	 *
	 * @return DAO for programs
	 */
	public ProgramDAO getProgramDAO() {
		return programDAO;
	}

	/**
	 * Sets a new value to DAO for programs.
	 *
	 * @param programDAO new value
	 */
	public void setProgramDAO(final ProgramDAO programDAO) {
		this.programDAO = programDAO;
	}

	/**
	 * Returns cache for programs.
	 *
	 * @return cache for programs
	 */
	public Cache getProgramCache() {
		return programCache;
	}

	/**
	 * Sets a new value to cache for programs.
	 *
	 * @param programCache new value
	 */
	public void setProgramCache(final Cache programCache) {
		this.programCache = programCache;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException     if DAO for programs isn't set
	 *                                   or cache for programs isn't set
	 * @throws ServiceOperationException {@inheritDoc}
	 */
	@Override
	public void newData() {
		Validators.validateFieldNotNull(programDAO, "DAO for programs");
		Validators.validateFieldNotNull(programCache, "Cache for programs");

		try {
			for (Program program : getCachedPrograms(false)) {
				programDAO.remove(program);
			}
			programCache.clear();
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException("Error in working with DAO tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException     if DAO for programs isn't set
	 *                                   or cache for programs isn't set
	 * @throws ServiceOperationException {@inheritDoc}
	 */
	@Override
	public List<Program> getPrograms() {
		Validators.validateFieldNotNull(programDAO, "DAO for programs");
		Validators.validateFieldNotNull(programCache, "Cache for programs");

		try {
			return getCachedPrograms(true);
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException("Error in working with DAO tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException     if DAO for programs isn't set
	 *                                   or cache for programs isn't set
	 * @throws IllegalArgumentException  {@inheritDoc}
	 * @throws ServiceOperationException {@inheritDoc}
	 */
	@Override
	public Program getProgram(final Integer id) {
		Validators.validateFieldNotNull(programDAO, "DAO for programs");
		Validators.validateFieldNotNull(programCache, "Cache for programs");
		Validators.validateArgumentNotNull(id, "ID");

		try {
			return getCachedProgram(id);
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException("Error in working with DAO tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException     if DAO for programs isn't set
	 *                                   or cache for programs isn't set
	 * @throws IllegalArgumentException  {@inheritDoc}
	 * @throws ServiceOperationException {@inheritDoc}
	 */
	@Override
	public void add(final Program program) {
		Validators.validateFieldNotNull(programDAO, "DAO for programs");
		Validators.validateFieldNotNull(programCache, "Cache for programs");
		Validators.validateArgumentNotNull(program, "Program");

		try {
			programDAO.add(program);
			addObjectToListCache(programCache, "programs", program);
			addObjectToCache(programCache, "program" + program.getId(), program);
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException("Error in working with DAO tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException     if DAO for programs isn't set
	 *                                   or cache for programs isn't set
	 * @throws IllegalArgumentException  {@inheritDoc}
	 * @throws ServiceOperationException {@inheritDoc}
	 */
	@Override
	public void update(final Program program) {
		Validators.validateFieldNotNull(programDAO, "DAO for programs");
		Validators.validateFieldNotNull(programCache, "Cache for programs");
		Validators.validateArgumentNotNull(program, "Program");

		try {
			programDAO.update(program);
			programCache.clear();
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException("Error in working with DAO tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException     if DAO for programs isn't set
	 *                                   or cache for programs isn't set
	 * @throws IllegalArgumentException  {@inheritDoc}
	 * @throws ServiceOperationException {@inheritDoc}
	 */
	@Override
	public void remove(final Program program) {
		Validators.validateFieldNotNull(programDAO, "DAO for programs");
		Validators.validateFieldNotNull(programCache, "Cache for programs");
		Validators.validateArgumentNotNull(program, "Program");

		try {
			programDAO.remove(program);
			removeObjectFromCache(programCache, "programs", program);
			programCache.evict(program.getId());
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException("Error in working with DAO tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException     if DAO for programs isn't set
	 *                                   or cache for programs isn't set
	 * @throws IllegalArgumentException  {@inheritDoc}
	 * @throws ServiceOperationException {@inheritDoc}
	 */
	@Override
	public void duplicate(final Program program) {
		Validators.validateFieldNotNull(programDAO, "DAO for programs");
		Validators.validateFieldNotNull(programCache, "Cache for programs");
		Validators.validateArgumentNotNull(program, "Program");

		try {
			final Program newProgram = new Program();
			newProgram.setName(program.getName());
			newProgram.setWikiEn(program.getWikiEn());
			newProgram.setWikiCz(program.getWikiCz());
			newProgram.setMediaCount(program.getMediaCount());
			newProgram.setCrack(program.hasCrack());
			newProgram.setSerialKey(program.hasSerialKey());
			newProgram.setOtherData(program.getOtherData());
			newProgram.setNote(program.getNote());
			programDAO.add(newProgram);
			newProgram.setPosition(program.getPosition());
			programDAO.update(newProgram);
			programCache.clear();
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException("Error in working with DAO tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException     if DAO for programs isn't set
	 *                                   or cache for programs isn't set
	 * @throws IllegalArgumentException  {@inheritDoc}
	 * @throws ServiceOperationException {@inheritDoc}
	 */
	@Override
	public void moveUp(final Program program) {
		Validators.validateFieldNotNull(programDAO, "DAO for programs");
		Validators.validateFieldNotNull(programCache, "Cache for programs");
		Validators.validateArgumentNotNull(program, "Program");

		try {
			final List<Program> programs = getCachedPrograms(false);
			final Program otherProgram = programs.get(programs.indexOf(program) - 1);
			switchPosition(program, otherProgram);
			programDAO.update(program);
			programDAO.update(otherProgram);
			programCache.clear();
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException("Error in working with DAO tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException     if DAO for programs isn't set
	 *                                   or cache for programs isn't set
	 * @throws IllegalArgumentException  {@inheritDoc}
	 * @throws ServiceOperationException {@inheritDoc}
	 */
	@Override
	public void moveDown(final Program program) {
		Validators.validateFieldNotNull(programDAO, "DAO for programs");
		Validators.validateFieldNotNull(programCache, "Cache for programs");
		Validators.validateArgumentNotNull(program, "Program");

		try {
			final List<Program> programs = getCachedPrograms(false);
			final Program otherProgram = programs.get(programs.indexOf(program) + 1);
			switchPosition(program, otherProgram);
			programDAO.update(program);
			programDAO.update(otherProgram);
			programCache.clear();
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException("Error in working with DAO tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException     if DAO for programs isn't set
	 *                                   or cache for programs isn't set
	 * @throws IllegalArgumentException  {@inheritDoc}
	 * @throws ServiceOperationException {@inheritDoc}
	 */
	@Override
	public boolean exists(final Program program) {
		Validators.validateFieldNotNull(programDAO, "DAO for programs");
		Validators.validateFieldNotNull(programCache, "Cache for programs");
		Validators.validateArgumentNotNull(program, "Program");

		try {
			return getCachedProgram(program.getId()) != null;
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException("Error in working with DAO tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException     if DAO for programs isn't set
	 *                                   or cache for programs isn't set
	 * @throws IllegalArgumentException  {@inheritDoc}
	 * @throws ServiceOperationException {@inheritDoc}
	 */
	@Override
	public void updatePositions() {
		Validators.validateFieldNotNull(programDAO, "DAO for programs");
		Validators.validateFieldNotNull(programCache, "Cache for programs");

		try {
			final List<Program> programs = getCachedPrograms(false);
			for (int i = 0; i < programs.size(); i++) {
				final Program program = programs.get(i);
				program.setPosition(i);
				programDAO.update(program);
			}
			programCache.clear();
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException("Error in working with DAO tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException     if DAO for programs isn't set
	 *                                   or cache for programs isn't set
	 * @throws IllegalArgumentException  {@inheritDoc}
	 * @throws ServiceOperationException {@inheritDoc}
	 */
	@Override
	public int getTotalMediaCount() {
		Validators.validateFieldNotNull(programDAO, "DAO for programs");
		Validators.validateFieldNotNull(programCache, "Cache for programs");

		try {
			int sum = 0;
			for (Program program : getCachedPrograms(true)) {
				sum += program.getMediaCount();
			}
			return sum;
		} catch (final DataStorageException ex) {
			throw new ServiceOperationException("Error in working with DAO tier.", ex);
		}
	}

	@Override
	protected List<Program> getData() {
		return programDAO.getPrograms();
	}

	@Override
	protected Program getData(final Integer id) {
		return programDAO.getProgram(id);
	}

	/**
	 * Returns list of programs.
	 *
	 * @param cached true if returned data from DAO should be cached
	 * @return list of programs
	 */
	private List<Program> getCachedPrograms(final boolean cached) {
		return getCachedObjects(programCache, "programs", cached);
	}

	/**
	 * Returns program with ID or null if there isn't such program.
	 *
	 * @param id ID
	 * @return program with ID or null if there isn't such program
	 */
	private Program getCachedProgram(final Integer id) {
		return getCachedObject(programCache, "program", id, true);
	}

	/**
	 * Switch position of programs.
	 *
	 * @param program1 1st program
	 * @param program2 2nd program
	 */
	private void switchPosition(final Program program1, final Program program2) {
		final int position = program1.getPosition();
		program1.setPosition(program2.getPosition());
		program2.setPosition(position);
	}

}
