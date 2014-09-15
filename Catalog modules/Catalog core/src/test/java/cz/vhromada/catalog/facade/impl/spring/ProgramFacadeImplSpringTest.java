package cz.vhromada.catalog.facade.impl.spring;

import static cz.vhromada.catalog.common.SpringUtils.PROGRAMS_COUNT;
import static cz.vhromada.catalog.common.TestConstants.PRIMARY_ID;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.common.EntityGenerator;
import cz.vhromada.catalog.common.SpringEntitiesUtils;
import cz.vhromada.catalog.common.SpringToUtils;
import cz.vhromada.catalog.common.SpringUtils;
import cz.vhromada.catalog.common.ToGenerator;
import cz.vhromada.catalog.dao.entities.Program;
import cz.vhromada.catalog.facade.ProgramFacade;
import cz.vhromada.catalog.facade.impl.ProgramFacadeImpl;
import cz.vhromada.catalog.facade.to.ProgramTO;
import cz.vhromada.test.DeepAsserts;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * A class represents test for class {@link ProgramFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeContext.xml")
public class ProgramFacadeImplSpringTest {

	/** Instance of {@link EntityManager} */
	@Autowired
	private EntityManager entityManager;

	/** Instance of {@link PlatformTransactionManager} */
	@Autowired
	private PlatformTransactionManager transactionManager;

	/** Instance of {@link ProgramFacade} */
	@Autowired
	private ProgramFacade programFacade;

	/** Initializes database. */
	@Before
	public void setUp() {
		SpringUtils.remove(transactionManager, entityManager, Program.class);
		SpringUtils.updateSequence(transactionManager, entityManager, "programs_sq");
		for (Program program : SpringEntitiesUtils.getPrograms()) {
			program.setId(null);
			SpringUtils.persist(transactionManager, entityManager, program);
		}
	}

	/** Test method for {@link ProgramFacade#newData()}. */
	@Test
	public void testNewData() {
		programFacade.newData();

		DeepAsserts.assertEquals(0, SpringUtils.getProgramsCount(entityManager));
	}

	/** Test method for {@link ProgramFacade#getPrograms()}. */
	@Test
	public void testGetPrograms() {
		DeepAsserts.assertEquals(SpringToUtils.getPrograms(), programFacade.getPrograms());
		DeepAsserts.assertEquals(PROGRAMS_COUNT, SpringUtils.getProgramsCount(entityManager));
	}

	/** Test method for {@link ProgramFacade#getProgram(Integer)}. */
	@Test
	public void testGetProgram() {
		for (int i = 1; i <= PROGRAMS_COUNT; i++) {
			DeepAsserts.assertEquals(SpringToUtils.getProgram(i), programFacade.getProgram(i));
		}

		assertNull(programFacade.getProgram(Integer.MAX_VALUE));

		DeepAsserts.assertEquals(PROGRAMS_COUNT, SpringUtils.getProgramsCount(entityManager));
	}

	/** Test method for {@link ProgramFacade#getProgram(Integer)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testGetProgramWithNullArgument() {
		programFacade.getProgram(null);
	}

	/** Test method for {@link ProgramFacade#add(ProgramTO)}. */
	@Test
	public void testAdd() {
		final ProgramTO program = ToGenerator.createProgram();
		final Program expectedProgram = EntityGenerator.createProgram(PROGRAMS_COUNT + 1);
		expectedProgram.setPosition(PROGRAMS_COUNT);

		programFacade.add(program);

		DeepAsserts.assertNotNull(program.getId());
		DeepAsserts.assertEquals(PROGRAMS_COUNT + 1, program.getId());
		final Program addedProgram = SpringUtils.getProgram(entityManager, PROGRAMS_COUNT + 1);
		DeepAsserts.assertEquals(program, addedProgram, "additionalData");
		DeepAsserts.assertEquals(expectedProgram, addedProgram);
		DeepAsserts.assertEquals(PROGRAMS_COUNT + 1, SpringUtils.getProgramsCount(entityManager));
	}

	/** Test method for {@link ProgramFacade#add(ProgramTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testAddWithNullArgument() {
		programFacade.add(null);
	}

	/** Test method for {@link ProgramFacade#add(ProgramTO)} with program with not null ID. */
	@Test(expected = ValidationException.class)
	public void testAddWithProgramWithNotNullId() {
		programFacade.add(ToGenerator.createProgram(Integer.MAX_VALUE));
	}

	/** Test method for {@link ProgramFacade#add(ProgramTO)} with program with null name. */
	@Test(expected = ValidationException.class)
	public void testAddWithProgramWithNullName() {
		final ProgramTO program = ToGenerator.createProgram();
		program.setName(null);

		programFacade.add(program);
	}

	/** Test method for {@link ProgramFacade#add(ProgramTO)} with program with empty string as name. */
	@Test(expected = ValidationException.class)
	public void testAddWithProgramWithEmptyName() {
		final ProgramTO program = ToGenerator.createProgram();
		program.setName("");

		programFacade.add(program);
	}

	/** Test method for {@link ProgramFacade#add(ProgramTO)} with program with null URL to english Wikipedia about program. */
	@Test(expected = ValidationException.class)
	public void testAddWithProgramWithNullWikiEn() {
		final ProgramTO program = ToGenerator.createProgram();
		program.setWikiEn(null);

		programFacade.add(program);
	}

	/** Test method for {@link ProgramFacade#add(ProgramTO)} with program with null URL to czech Wikipedia about program. */
	@Test(expected = ValidationException.class)
	public void testAddWithProgramWithNullWikiCz() {
		final ProgramTO program = ToGenerator.createProgram();
		program.setWikiCz(null);

		programFacade.add(program);
	}

	/** Test method for {@link ProgramFacade#add(ProgramTO)} with program with not positive count of media. */
	@Test(expected = ValidationException.class)
	public void testAddWithProgramWithNotPositiveMediaCount() {
		final ProgramTO program = ToGenerator.createProgram();
		program.setMediaCount(0);

		programFacade.add(program);
	}

	/** Test method for {@link ProgramFacade#add(ProgramTO)} with program with null other data. */
	@Test(expected = ValidationException.class)
	public void testAddWithProgramWithNullOtherData() {
		final ProgramTO program = ToGenerator.createProgram();
		program.setOtherData(null);

		programFacade.add(program);
	}

	/** Test method for {@link ProgramFacade#add(ProgramTO)} with program with null note. */
	@Test(expected = ValidationException.class)
	public void testAddWithProgramWithNullNote() {
		final ProgramTO program = ToGenerator.createProgram();
		program.setNote(null);

		programFacade.add(program);
	}

	/** Test method for {@link ProgramFacade#update(ProgramTO)}. */
	@Test
	public void testUpdate() {
		final ProgramTO program = ToGenerator.createProgram(1);
		final Program expectedProgram = EntityGenerator.createProgram(1);

		programFacade.update(program);

		final Program updatedProgram = SpringUtils.getProgram(entityManager, 1);
		DeepAsserts.assertEquals(program, updatedProgram, "additionalData");
		DeepAsserts.assertEquals(expectedProgram, updatedProgram);
		DeepAsserts.assertEquals(PROGRAMS_COUNT, SpringUtils.getProgramsCount(entityManager));
	}

	/** Test method for {@link ProgramFacade#update(ProgramTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateWithNullArgument() {
		programFacade.update(null);
	}

	/** Test method for {@link ProgramFacade#update(ProgramTO)} with program with null ID. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithProgramWithNullId() {
		programFacade.update(ToGenerator.createProgram());
	}

	/** Test method for {@link ProgramFacade#update(ProgramTO)} with program with null name. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithProgramWithNullName() {
		final ProgramTO program = ToGenerator.createProgram(PRIMARY_ID);
		program.setName(null);

		programFacade.update(program);
	}

	/** Test method for {@link ProgramFacade#update(ProgramTO)} with program with empty string as name. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithProgramWithEmptyName() {
		final ProgramTO program = ToGenerator.createProgram(PRIMARY_ID);
		program.setName(null);

		programFacade.update(program);
	}

	/** Test method for {@link ProgramFacade#update(ProgramTO)} with program with null URL to english Wikipedia about program. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithProgramWithNullWikiEn() {
		final ProgramTO program = ToGenerator.createProgram(PRIMARY_ID);
		program.setWikiEn(null);

		programFacade.update(program);
	}

	/** Test method for {@link ProgramFacade#update(ProgramTO)} with program with null URL to czech Wikipedia about program. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithProgramWithNullWikiCz() {
		final ProgramTO program = ToGenerator.createProgram(PRIMARY_ID);
		program.setWikiCz(null);

		programFacade.update(program);
	}

	/** Test method for {@link ProgramFacade#update(ProgramTO)} with program with not positive count of media. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithProgramWithNotPositiveMediaCount() {
		final ProgramTO program = ToGenerator.createProgram(PRIMARY_ID);
		program.setMediaCount(0);

		programFacade.update(program);
	}

	/** Test method for {@link ProgramFacade#update(ProgramTO)} with program with null other data. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithProgramWithNullOtherData() {
		final ProgramTO program = ToGenerator.createProgram(PRIMARY_ID);
		program.setOtherData(null);

		programFacade.update(program);
	}

	/** Test method for {@link ProgramFacade#update(ProgramTO)} with program with null note. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithProgramWithNullNote() {
		final ProgramTO program = ToGenerator.createProgram(PRIMARY_ID);
		program.setNote(null);

		programFacade.update(program);
	}

	/** Test method for {@link ProgramFacade#update(ProgramTO)} with program with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testUpdateWithProgramWithBadId() {
		programFacade.update(ToGenerator.createProgram(Integer.MAX_VALUE));
	}

	/** Test method for {@link ProgramFacade#remove(ProgramTO)}. */
	@Test
	public void testRemove() {
		programFacade.remove(ToGenerator.createProgram(1));

		assertNull(SpringUtils.getProgram(entityManager, 1));
		DeepAsserts.assertEquals(PROGRAMS_COUNT - 1, SpringUtils.getProgramsCount(entityManager));
	}

	/** Test method for {@link ProgramFacade#remove(ProgramTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testRemoveWithNullArgument() {
		programFacade.remove(null);
	}

	/** Test method for {@link ProgramFacade#remove(ProgramTO)} with program with null ID. */
	@Test(expected = ValidationException.class)
	public void testRemoveWithProgramWithNullId() {
		programFacade.remove(ToGenerator.createProgram());
	}

	/** Test method for {@link ProgramFacade#remove(ProgramTO)} with program with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testRemoveWithProgramWithBadId() {
		programFacade.remove(ToGenerator.createProgram(Integer.MAX_VALUE));
	}

	/** Test method for {@link ProgramFacade#duplicate(ProgramTO)}. */
	@Test
	public void testDuplicate() {
		final Program program = SpringEntitiesUtils.getProgram(PROGRAMS_COUNT);
		program.setId(PROGRAMS_COUNT + 1);

		programFacade.duplicate(ToGenerator.createProgram(PROGRAMS_COUNT));

		final Program duplicatedProgram = SpringUtils.getProgram(entityManager, PROGRAMS_COUNT + 1);
		DeepAsserts.assertEquals(program, duplicatedProgram);
		DeepAsserts.assertEquals(PROGRAMS_COUNT + 1, SpringUtils.getProgramsCount(entityManager));
	}

	/** Test method for {@link ProgramFacade#duplicate(ProgramTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testDuplicateWithNullArgument() {
		programFacade.duplicate(null);
	}

	/** Test method for {@link ProgramFacade#duplicate(ProgramTO)} with program with null ID. */
	@Test(expected = ValidationException.class)
	public void testDuplicateWithProgramWithNullId() {
		programFacade.duplicate(ToGenerator.createProgram());
	}

	/** Test method for {@link ProgramFacade#duplicate(ProgramTO)} with program with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testDuplicateWithProgramWithBadId() {
		programFacade.duplicate(ToGenerator.createProgram(Integer.MAX_VALUE));
	}

	/** Test method for {@link ProgramFacade#moveUp(ProgramTO)}. */
	@Test
	public void testMoveUp() {
		final Program program1 = SpringEntitiesUtils.getProgram(1);
		program1.setPosition(1);
		final Program program2 = SpringEntitiesUtils.getProgram(2);
		program2.setPosition(0);

		programFacade.moveUp(ToGenerator.createProgram(2));
		DeepAsserts.assertEquals(program1, SpringUtils.getProgram(entityManager, 1));
		DeepAsserts.assertEquals(program2, SpringUtils.getProgram(entityManager, 2));
		for (int i = 3; i <= PROGRAMS_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getProgram(i), SpringUtils.getProgram(entityManager, i));
		}
		DeepAsserts.assertEquals(PROGRAMS_COUNT, SpringUtils.getProgramsCount(entityManager));
	}

	/** Test method for {@link ProgramFacade#moveUp(ProgramTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testMoveUpWithNullArgument() {
		programFacade.moveUp(null);
	}

	/** Test method for {@link ProgramFacade#moveUp(ProgramTO)} with program with null ID. */
	@Test(expected = ValidationException.class)
	public void testMoveUpWithProgramWithNullId() {
		programFacade.moveUp(ToGenerator.createProgram());
	}

	/** Test method for {@link ProgramFacade#moveUp(ProgramTO)} with not moveable argument. */
	@Test(expected = ValidationException.class)
	public void testMoveUpWithNotMoveableArgument() {
		programFacade.moveUp(ToGenerator.createProgram(1));
	}

	/** Test method for {@link ProgramFacade#moveUp(ProgramTO)} with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testMoveUpWithBadId() {
		programFacade.moveUp(ToGenerator.createProgram(Integer.MAX_VALUE));
	}

	/** Test method for {@link ProgramFacade#moveDown(ProgramTO)}. */
	@Test
	public void testMoveDown() {
		final ProgramTO program = ToGenerator.createProgram(1);
		final Program program1 = SpringEntitiesUtils.getProgram(1);
		program1.setPosition(1);
		final Program program2 = SpringEntitiesUtils.getProgram(2);
		program2.setPosition(0);

		programFacade.moveDown(program);
		DeepAsserts.assertEquals(program1, SpringUtils.getProgram(entityManager, 1));
		DeepAsserts.assertEquals(program2, SpringUtils.getProgram(entityManager, 2));
		for (int i = 3; i <= PROGRAMS_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getProgram(i), SpringUtils.getProgram(entityManager, i));
		}
		DeepAsserts.assertEquals(PROGRAMS_COUNT, SpringUtils.getProgramsCount(entityManager));
	}

	/** Test method for {@link ProgramFacade#moveDown(ProgramTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testMoveDownWithNullArgument() {
		programFacade.moveDown(null);
	}

	/** Test method for {@link ProgramFacade#moveDown(ProgramTO)} with program with null ID. */
	@Test(expected = ValidationException.class)
	public void testMoveDownWithProgramWithNullId() {
		programFacade.moveDown(ToGenerator.createProgram());
	}

	/** Test method for {@link ProgramFacade#moveDown(ProgramTO)} with not moveable argument. */
	@Test(expected = ValidationException.class)
	public void testMoveDownWithNotMoveableArgument() {
		programFacade.moveDown(ToGenerator.createProgram(PROGRAMS_COUNT));
	}

	/** Test method for {@link ProgramFacade#moveDown(ProgramTO)} with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testMoveDownWithBadId() {
		programFacade.moveDown(ToGenerator.createProgram(Integer.MAX_VALUE));
	}

	/** Test method for {@link ProgramFacade#exists(ProgramTO)} with existing program. */
	@Test
	public void testExists() {
		for (int i = 1; i <= PROGRAMS_COUNT; i++) {
			assertTrue(programFacade.exists(ToGenerator.createProgram(i)));
		}

		assertFalse(programFacade.exists(ToGenerator.createProgram(Integer.MAX_VALUE)));

		DeepAsserts.assertEquals(PROGRAMS_COUNT, SpringUtils.getProgramsCount(entityManager));
	}

	/** Test method for {@link ProgramFacade#exists(ProgramTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testExistsWithNullArgument() {
		programFacade.exists(null);
	}

	/** Test method for {@link ProgramFacade#exists(ProgramTO)} with program with null ID. */
	@Test(expected = ValidationException.class)
	public void testExistsWithProgramWithNullId() {
		programFacade.exists(ToGenerator.createProgram());
	}

	/** Test method for {@link ProgramFacade#updatePositions()}. */
	@Test
	public void testUpdatePositions() {
		programFacade.updatePositions();

		for (int i = 1; i <= PROGRAMS_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getProgram(i), SpringUtils.getProgram(entityManager, i));
		}
		DeepAsserts.assertEquals(PROGRAMS_COUNT, SpringUtils.getProgramsCount(entityManager));
	}

	/** Test method for {@link ProgramFacade#getTotalMediaCount()}. */
	@Test
	public void testGetTotalMediaCount() {
		DeepAsserts.assertEquals(600, programFacade.getTotalMediaCount());
		DeepAsserts.assertEquals(PROGRAMS_COUNT, SpringUtils.getProgramsCount(entityManager));
	}

}
