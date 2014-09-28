package cz.vhromada.catalog.service.impl.spring;

import static cz.vhromada.catalog.commons.SpringUtils.PROGRAMS_COUNT;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.EntityGenerator;
import cz.vhromada.catalog.commons.SpringEntitiesUtils;
import cz.vhromada.catalog.commons.SpringUtils;
import cz.vhromada.catalog.dao.entities.Program;
import cz.vhromada.catalog.service.ProgramService;
import cz.vhromada.catalog.service.impl.ProgramServiceImpl;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents test for class {@link ProgramServiceImpl} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testServiceContext.xml")
@Transactional
public class ProgramServiceImplSpringTest {

	/** Instance of {@link EntityManager} */
	@Autowired
	private EntityManager entityManager;

	/** Instance of {@link Cache} */
	@Value("#{cacheManager.getCache('programCache')}")
	private Cache programCache;

	/** Instance of {@link ProgramService} */
	@Autowired
	private ProgramService programService;

	/** Clears cache and restarts sequence. */
	@Before
	public void setUp() {
		programCache.clear();
		entityManager.createNativeQuery("ALTER SEQUENCE programs_sq RESTART WITH 4").executeUpdate();
	}

	/** Test method for {@link ProgramService#newData()}. */
	@Test
	public void testNewData() {
		programService.newData();

		DeepAsserts.assertEquals(0, SpringUtils.getProgramsCount(entityManager));
		assertTrue(SpringUtils.getCacheKeys(programCache).isEmpty());
	}

	/** Test method for {@link ProgramService#getPrograms()}. */
	@Test
	public void testGetPrograms() {
		final List<Program> programs = SpringEntitiesUtils.getPrograms();
		final String key = "programs";

		DeepAsserts.assertEquals(programs, programService.getPrograms());
		DeepAsserts.assertEquals(PROGRAMS_COUNT, SpringUtils.getProgramsCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(programCache));
		SpringUtils.assertCacheValue(programCache, key, programs);
	}

	/** Test method for {@link ProgramService#getProgram(Integer)} with existing program. */
	@Test
	public void testGetProgramWithExistingProgram() {
		final List<String> keys = new ArrayList<>();
		for (int i = 1; i <= PROGRAMS_COUNT; i++) {
			keys.add("program" + i);
		}

		for (int i = 1; i <= PROGRAMS_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getProgram(i), programService.getProgram(i));
		}
		DeepAsserts.assertEquals(PROGRAMS_COUNT, SpringUtils.getProgramsCount(entityManager));
		DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(programCache));
		for (int i = 1; i <= PROGRAMS_COUNT; i++) {
			SpringUtils.assertCacheValue(programCache, keys.get(i - 1), SpringEntitiesUtils.getProgram(i));
		}
	}

	/** Test method for {@link ProgramService#getProgram(Integer)} with not existing program. */
	@Test
	public void testGetProgramWithNotExistingProgram() {
		final String key = "program" + Integer.MAX_VALUE;

		assertNull(programService.getProgram(Integer.MAX_VALUE));
		DeepAsserts.assertEquals(PROGRAMS_COUNT, SpringUtils.getProgramsCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(programCache));
		SpringUtils.assertCacheValue(programCache, key, null);
	}

	/** Test method for {@link ProgramService#add(Program)} with empty cache. */
	@Test
	public void testAddWithEmptyCache() {
		final Program program = EntityGenerator.createProgram();
		final Program expectedProgram = EntityGenerator.createProgram(PROGRAMS_COUNT + 1);
		expectedProgram.setPosition(PROGRAMS_COUNT);

		programService.add(program);

		DeepAsserts.assertNotNull(program.getId());
		DeepAsserts.assertEquals(PROGRAMS_COUNT + 1, program.getId());
		final Program addedProgram = SpringUtils.getProgram(entityManager, PROGRAMS_COUNT + 1);
		DeepAsserts.assertEquals(program, addedProgram);
		DeepAsserts.assertEquals(expectedProgram, addedProgram);
		DeepAsserts.assertEquals(PROGRAMS_COUNT + 1, SpringUtils.getProgramsCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(programCache).size());
	}

	/** Test method for {@link ProgramService#add(Program)} with not empty cache. */
	@Test
	public void testAddWithNotEmptyCache() {
		final Program program = EntityGenerator.createProgram();
		final Program expectedProgram = EntityGenerator.createProgram(PROGRAMS_COUNT + 1);
		expectedProgram.setPosition(PROGRAMS_COUNT);
		final String keyList = "programs";
		final String keyItem = "program" + (PROGRAMS_COUNT + 1);
		programCache.put(keyList, new ArrayList<>());
		programCache.put(keyItem, null);

		programService.add(program);

		DeepAsserts.assertNotNull(program.getId());
		DeepAsserts.assertEquals(PROGRAMS_COUNT + 1, program.getId());
		final Program addedProgram = SpringUtils.getProgram(entityManager, PROGRAMS_COUNT + 1);
		DeepAsserts.assertEquals(program, addedProgram);
		DeepAsserts.assertEquals(expectedProgram, addedProgram);
		DeepAsserts.assertEquals(PROGRAMS_COUNT + 1, SpringUtils.getProgramsCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(keyList, keyItem), SpringUtils.getCacheKeys(programCache));
		SpringUtils.assertCacheValue(programCache, keyList, CollectionUtils.newList(program));
		SpringUtils.assertCacheValue(programCache, keyItem, program);
	}

	/** Test method for {@link ProgramService#update(Program)}. */
	@Test
	public void testUpdate() {
		final Program program = SpringEntitiesUtils.updateProgram(SpringUtils.getProgram(entityManager, 1));

		programService.update(program);

		final Program updatedProgram = SpringUtils.getProgram(entityManager, 1);
		DeepAsserts.assertEquals(program, updatedProgram);
		DeepAsserts.assertEquals(EntityGenerator.createProgram(1), updatedProgram);
		DeepAsserts.assertEquals(PROGRAMS_COUNT, SpringUtils.getProgramsCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(programCache).size());
	}

	/** Test method for {@link ProgramService#remove(Program)} with empty cache. */
	@Test
	public void testRemoveWithEmptyCache() {
		final Program program = EntityGenerator.createProgram();
		entityManager.persist(program);
		DeepAsserts.assertEquals(PROGRAMS_COUNT + 1, SpringUtils.getProgramsCount(entityManager));

		programService.remove(program);

		assertNull(SpringUtils.getProgram(entityManager, program.getId()));
		DeepAsserts.assertEquals(PROGRAMS_COUNT, SpringUtils.getProgramsCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(programCache).size());
	}

	/** Test method for {@link ProgramService#remove(Program)} with not empty cache. */
	@Test
	public void testRemoveWithNotEmptyCache() {
		final Program program = EntityGenerator.createProgram();
		entityManager.persist(program);
		DeepAsserts.assertEquals(PROGRAMS_COUNT + 1, SpringUtils.getProgramsCount(entityManager));
		final String key = "programs";
		final List<Program> cachePrograms = new ArrayList<>();
		cachePrograms.add(program);
		programCache.put(key, cachePrograms);

		programService.remove(program);

		assertNull(SpringUtils.getProgram(entityManager, program.getId()));
		DeepAsserts.assertEquals(PROGRAMS_COUNT, SpringUtils.getProgramsCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(programCache));
		SpringUtils.assertCacheValue(programCache, key, new ArrayList<>());
	}

	/** Test method for {@link ProgramService#duplicate(Program)}. */
	@Test
	public void testDuplicate() {
		final Program program = SpringUtils.getProgram(entityManager, 3);
		final Program expectedProgram = SpringEntitiesUtils.getProgram(3);
		expectedProgram.setId(PROGRAMS_COUNT + 1);

		programService.duplicate(program);

		DeepAsserts.assertEquals(expectedProgram, SpringUtils.getProgram(entityManager, PROGRAMS_COUNT + 1));
		for (int i = 1; i <= PROGRAMS_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getProgram(i), SpringUtils.getProgram(entityManager, i));
		}
		DeepAsserts.assertEquals(PROGRAMS_COUNT + 1, SpringUtils.getProgramsCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(programCache).size());
	}

	/** Test method for {@link ProgramService#moveUp(Program)}. */
	@Test
	public void testMoveUp() {
		final Program program = SpringUtils.getProgram(entityManager, 2);
		final Program expectedProgram1 = SpringEntitiesUtils.getProgram(1);
		expectedProgram1.setPosition(1);
		final Program expectedProgram2 = SpringEntitiesUtils.getProgram(2);
		expectedProgram2.setPosition(0);

		programService.moveUp(program);

		DeepAsserts.assertEquals(expectedProgram1, SpringUtils.getProgram(entityManager, 1));
		DeepAsserts.assertEquals(expectedProgram2, SpringUtils.getProgram(entityManager, 2));
		for (int i = 3; i <= PROGRAMS_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getProgram(i), SpringUtils.getProgram(entityManager, i));
		}
		DeepAsserts.assertEquals(PROGRAMS_COUNT, SpringUtils.getProgramsCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(programCache).size());
	}

	/** Test method for {@link ProgramService#moveDown(Program)}. */
	@Test
	public void testMoveDown() {
		final Program program = SpringUtils.getProgram(entityManager, 1);
		final Program expectedProgram1 = SpringEntitiesUtils.getProgram(1);
		expectedProgram1.setPosition(1);
		final Program expectedProgram2 = SpringEntitiesUtils.getProgram(2);
		expectedProgram2.setPosition(0);

		programService.moveDown(program);

		DeepAsserts.assertEquals(expectedProgram1, SpringUtils.getProgram(entityManager, 1));
		DeepAsserts.assertEquals(expectedProgram2, SpringUtils.getProgram(entityManager, 2));
		for (int i = 3; i <= PROGRAMS_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getProgram(i), SpringUtils.getProgram(entityManager, i));
		}
		DeepAsserts.assertEquals(PROGRAMS_COUNT, SpringUtils.getProgramsCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(programCache).size());
	}


	/** Test method for {@link ProgramService#exists(Program)} with existing program. */
	@Test
	public void testExistsWithExistingProgram() {
		final List<String> keys = new ArrayList<>();
		for (int i = 1; i <= PROGRAMS_COUNT; i++) {
			keys.add("program" + i);
		}

		for (int i = 1; i <= PROGRAMS_COUNT; i++) {
			assertTrue(programService.exists(SpringEntitiesUtils.getProgram(i)));
		}
		DeepAsserts.assertEquals(PROGRAMS_COUNT, SpringUtils.getProgramsCount(entityManager));
		DeepAsserts.assertEquals(keys, SpringUtils.getCacheKeys(programCache));
		for (int i = 1; i <= PROGRAMS_COUNT; i++) {
			SpringUtils.assertCacheValue(programCache, keys.get(i - 1), SpringEntitiesUtils.getProgram(i));
		}
	}

	/** Test method for {@link ProgramService#exists(Program)} with not existing program. */
	@Test
	public void testExistsWithNotExistingProgram() {
		final String key = "program" + Integer.MAX_VALUE;

		assertFalse(programService.exists(EntityGenerator.createProgram(Integer.MAX_VALUE)));
		DeepAsserts.assertEquals(PROGRAMS_COUNT, SpringUtils.getProgramsCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(programCache));
		SpringUtils.assertCacheValue(programCache, key, null);
	}

	/** Test method for {@link ProgramService#updatePositions()}. */
	@Test
	public void testUpdatePositions() {
		final Program program = SpringUtils.getProgram(entityManager, PROGRAMS_COUNT);
		program.setPosition(5000);
		entityManager.merge(program);

		programService.updatePositions();

		for (int i = 1; i <= PROGRAMS_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getProgram(i), SpringUtils.getProgram(entityManager, i));
		}
		DeepAsserts.assertEquals(PROGRAMS_COUNT, SpringUtils.getProgramsCount(entityManager));
		DeepAsserts.assertEquals(0, SpringUtils.getCacheKeys(programCache).size());
	}

	/** Test method for {@link ProgramService#getTotalMediaCount()}. */
	@Test
	public void testGetTotalMediaCount() {
		final String key = "programs";

		DeepAsserts.assertEquals(600, programService.getTotalMediaCount());
		DeepAsserts.assertEquals(PROGRAMS_COUNT, SpringUtils.getProgramsCount(entityManager));
		DeepAsserts.assertEquals(CollectionUtils.newList(key), SpringUtils.getCacheKeys(programCache));
		SpringUtils.assertCacheValue(programCache, key, SpringEntitiesUtils.getPrograms());
	}

}
