package cz.vhromada.catalog.facade.impl.spring;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.SpringEntitiesUtils;
import cz.vhromada.catalog.commons.SpringToUtils;
import cz.vhromada.catalog.commons.SpringUtils;
import cz.vhromada.catalog.dao.entities.Program;
import cz.vhromada.catalog.facade.ProgramFacade;
import cz.vhromada.catalog.facade.to.ProgramTO;
import cz.vhromada.generator.ObjectGenerator;
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
 * A class represents test for class {@link cz.vhromada.catalog.facade.impl.ProgramFacadeImpl}.
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

	/** Instance of {@link ObjectGenerator} */
	@Autowired
	private ObjectGenerator objectGenerator;

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
		DeepAsserts.assertEquals(SpringUtils.PROGRAMS_COUNT, SpringUtils.getProgramsCount(entityManager));
	}

	/** Test method for {@link ProgramFacade#getProgram(Integer)}. */
	@Test
	public void testGetProgram() {
		for (int i = 1; i <= SpringUtils.PROGRAMS_COUNT; i++) {
			DeepAsserts.assertEquals(SpringToUtils.getProgram(i), programFacade.getProgram(i));
		}

		assertNull(programFacade.getProgram(Integer.MAX_VALUE));

		DeepAsserts.assertEquals(SpringUtils.PROGRAMS_COUNT, SpringUtils.getProgramsCount(entityManager));
	}

	/** Test method for {@link ProgramFacade#getProgram(Integer)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testGetProgramWithNullArgument() {
		programFacade.getProgram(null);
	}

	/** Test method for {@link ProgramFacade#add(ProgramTO)}. */
	@Test
	public void testAdd() {
		final ProgramTO program = SpringToUtils.newProgram(objectGenerator);

		programFacade.add(program);

		DeepAsserts.assertNotNull(program.getId());
		DeepAsserts.assertEquals(SpringUtils.PROGRAMS_COUNT + 1, program.getId());
		final Program addedProgram = SpringUtils.getProgram(entityManager, SpringUtils.PROGRAMS_COUNT + 1);
		DeepAsserts.assertEquals(program, addedProgram, "additionalData");
		DeepAsserts.assertEquals(SpringUtils.PROGRAMS_COUNT + 1, SpringUtils.getProgramsCount(entityManager));
	}

	/** Test method for {@link ProgramFacade#add(ProgramTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testAddWithNullArgument() {
		programFacade.add(null);
	}

	/** Test method for {@link ProgramFacade#add(ProgramTO)} with program with not null ID. */
	@Test(expected = ValidationException.class)
	public void testAddWithProgramWithNotNullId() {
		programFacade.add(SpringToUtils.newProgramWithId(objectGenerator));
	}

	/** Test method for {@link ProgramFacade#add(ProgramTO)} with program with null name. */
	@Test(expected = ValidationException.class)
	public void testAddWithProgramWithNullName() {
		final ProgramTO program = SpringToUtils.newProgram(objectGenerator);
		program.setName(null);

		programFacade.add(program);
	}

	/** Test method for {@link ProgramFacade#add(ProgramTO)} with program with empty string as name. */
	@Test(expected = ValidationException.class)
	public void testAddWithProgramWithEmptyName() {
		final ProgramTO program = SpringToUtils.newProgram(objectGenerator);
		program.setName("");

		programFacade.add(program);
	}

	/** Test method for {@link ProgramFacade#add(ProgramTO)} with program with null URL to english Wikipedia about program. */
	@Test(expected = ValidationException.class)
	public void testAddWithProgramWithNullWikiEn() {
		final ProgramTO program = SpringToUtils.newProgram(objectGenerator);
		program.setWikiEn(null);

		programFacade.add(program);
	}

	/** Test method for {@link ProgramFacade#add(ProgramTO)} with program with null URL to czech Wikipedia about program. */
	@Test(expected = ValidationException.class)
	public void testAddWithProgramWithNullWikiCz() {
		final ProgramTO program = SpringToUtils.newProgram(objectGenerator);
		program.setWikiCz(null);

		programFacade.add(program);
	}

	/** Test method for {@link ProgramFacade#add(ProgramTO)} with program with not positive count of media. */
	@Test(expected = ValidationException.class)
	public void testAddWithProgramWithNotPositiveMediaCount() {
		final ProgramTO program = SpringToUtils.newProgram(objectGenerator);
		program.setMediaCount(0);

		programFacade.add(program);
	}

	/** Test method for {@link ProgramFacade#add(ProgramTO)} with program with null other data. */
	@Test(expected = ValidationException.class)
	public void testAddWithProgramWithNullOtherData() {
		final ProgramTO program = SpringToUtils.newProgram(objectGenerator);
		program.setOtherData(null);

		programFacade.add(program);
	}

	/** Test method for {@link ProgramFacade#add(ProgramTO)} with program with null note. */
	@Test(expected = ValidationException.class)
	public void testAddWithProgramWithNullNote() {
		final ProgramTO program = SpringToUtils.newProgram(objectGenerator);
		program.setNote(null);

		programFacade.add(program);
	}

	/** Test method for {@link ProgramFacade#update(ProgramTO)}. */
	@Test
	public void testUpdate() {
		final ProgramTO program = SpringToUtils.newProgram(objectGenerator, 1);

		programFacade.update(program);

		final Program updatedProgram = SpringUtils.getProgram(entityManager, 1);
		DeepAsserts.assertEquals(program, updatedProgram, "additionalData");
		DeepAsserts.assertEquals(SpringUtils.PROGRAMS_COUNT, SpringUtils.getProgramsCount(entityManager));
	}

	/** Test method for {@link ProgramFacade#update(ProgramTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateWithNullArgument() {
		programFacade.update(null);
	}

	/** Test method for {@link ProgramFacade#update(ProgramTO)} with program with null ID. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithProgramWithNullId() {
		programFacade.update(SpringToUtils.newProgram(objectGenerator));
	}

	/** Test method for {@link ProgramFacade#update(ProgramTO)} with program with null name. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithProgramWithNullName() {
		final ProgramTO program = SpringToUtils.newProgramWithId(objectGenerator);
		program.setName(null);

		programFacade.update(program);
	}

	/** Test method for {@link ProgramFacade#update(ProgramTO)} with program with empty string as name. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithProgramWithEmptyName() {
		final ProgramTO program = SpringToUtils.newProgramWithId(objectGenerator);
		program.setName(null);

		programFacade.update(program);
	}

	/** Test method for {@link ProgramFacade#update(ProgramTO)} with program with null URL to english Wikipedia about program. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithProgramWithNullWikiEn() {
		final ProgramTO program = SpringToUtils.newProgramWithId(objectGenerator);
		program.setWikiEn(null);

		programFacade.update(program);
	}

	/** Test method for {@link ProgramFacade#update(ProgramTO)} with program with null URL to czech Wikipedia about program. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithProgramWithNullWikiCz() {
		final ProgramTO program = SpringToUtils.newProgramWithId(objectGenerator);
		program.setWikiCz(null);

		programFacade.update(program);
	}

	/** Test method for {@link ProgramFacade#update(ProgramTO)} with program with not positive count of media. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithProgramWithNotPositiveMediaCount() {
		final ProgramTO program = SpringToUtils.newProgramWithId(objectGenerator);
		program.setMediaCount(0);

		programFacade.update(program);
	}

	/** Test method for {@link ProgramFacade#update(ProgramTO)} with program with null other data. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithProgramWithNullOtherData() {
		final ProgramTO program = SpringToUtils.newProgramWithId(objectGenerator);
		program.setOtherData(null);

		programFacade.update(program);
	}

	/** Test method for {@link ProgramFacade#update(ProgramTO)} with program with null note. */
	@Test(expected = ValidationException.class)
	public void testUpdateWithProgramWithNullNote() {
		final ProgramTO program = SpringToUtils.newProgramWithId(objectGenerator);
		program.setNote(null);

		programFacade.update(program);
	}

	/** Test method for {@link ProgramFacade#update(ProgramTO)} with program with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testUpdateWithProgramWithBadId() {
		programFacade.update(SpringToUtils.newProgram(objectGenerator, Integer.MAX_VALUE));
	}

	/** Test method for {@link ProgramFacade#remove(ProgramTO)}. */
	@Test
	public void testRemove() {
		programFacade.remove(SpringToUtils.newProgram(objectGenerator, 1));

		assertNull(SpringUtils.getProgram(entityManager, 1));
		DeepAsserts.assertEquals(SpringUtils.PROGRAMS_COUNT - 1, SpringUtils.getProgramsCount(entityManager));
	}

	/** Test method for {@link ProgramFacade#remove(ProgramTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testRemoveWithNullArgument() {
		programFacade.remove(null);
	}

	/** Test method for {@link ProgramFacade#remove(ProgramTO)} with program with null ID. */
	@Test(expected = ValidationException.class)
	public void testRemoveWithProgramWithNullId() {
		programFacade.remove(SpringToUtils.newProgram(objectGenerator));
	}

	/** Test method for {@link ProgramFacade#remove(ProgramTO)} with program with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testRemoveWithProgramWithBadId() {
		programFacade.remove(SpringToUtils.newProgram(objectGenerator, Integer.MAX_VALUE));
	}

	/** Test method for {@link ProgramFacade#duplicate(ProgramTO)}. */
	@Test
	public void testDuplicate() {
		final Program program = SpringEntitiesUtils.getProgram(SpringUtils.PROGRAMS_COUNT);
		program.setId(SpringUtils.PROGRAMS_COUNT + 1);

		programFacade.duplicate(SpringToUtils.newProgram(objectGenerator, SpringUtils.PROGRAMS_COUNT));

		final Program duplicatedProgram = SpringUtils.getProgram(entityManager, SpringUtils.PROGRAMS_COUNT + 1);
		DeepAsserts.assertEquals(program, duplicatedProgram);
		DeepAsserts.assertEquals(SpringUtils.PROGRAMS_COUNT + 1, SpringUtils.getProgramsCount(entityManager));
	}

	/** Test method for {@link ProgramFacade#duplicate(ProgramTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testDuplicateWithNullArgument() {
		programFacade.duplicate(null);
	}

	/** Test method for {@link ProgramFacade#duplicate(ProgramTO)} with program with null ID. */
	@Test(expected = ValidationException.class)
	public void testDuplicateWithProgramWithNullId() {
		programFacade.duplicate(SpringToUtils.newProgram(objectGenerator));
	}

	/** Test method for {@link ProgramFacade#duplicate(ProgramTO)} with program with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testDuplicateWithProgramWithBadId() {
		programFacade.duplicate(SpringToUtils.newProgram(objectGenerator, Integer.MAX_VALUE));
	}

	/** Test method for {@link ProgramFacade#moveUp(ProgramTO)}. */
	@Test
	public void testMoveUp() {
		final Program program1 = SpringEntitiesUtils.getProgram(1);
		program1.setPosition(1);
		final Program program2 = SpringEntitiesUtils.getProgram(2);
		program2.setPosition(0);

		programFacade.moveUp(SpringToUtils.newProgram(objectGenerator, 2));
		DeepAsserts.assertEquals(program1, SpringUtils.getProgram(entityManager, 1));
		DeepAsserts.assertEquals(program2, SpringUtils.getProgram(entityManager, 2));
		for (int i = 3; i <= SpringUtils.PROGRAMS_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getProgram(i), SpringUtils.getProgram(entityManager, i));
		}
		DeepAsserts.assertEquals(SpringUtils.PROGRAMS_COUNT, SpringUtils.getProgramsCount(entityManager));
	}

	/** Test method for {@link ProgramFacade#moveUp(ProgramTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testMoveUpWithNullArgument() {
		programFacade.moveUp(null);
	}

	/** Test method for {@link ProgramFacade#moveUp(ProgramTO)} with program with null ID. */
	@Test(expected = ValidationException.class)
	public void testMoveUpWithProgramWithNullId() {
		programFacade.moveUp(SpringToUtils.newProgram(objectGenerator));
	}

	/** Test method for {@link ProgramFacade#moveUp(ProgramTO)} with not moveable argument. */
	@Test(expected = ValidationException.class)
	public void testMoveUpWithNotMoveableArgument() {
		programFacade.moveUp(SpringToUtils.newProgram(objectGenerator, 1));
	}

	/** Test method for {@link ProgramFacade#moveUp(ProgramTO)} with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testMoveUpWithBadId() {
		programFacade.moveUp(SpringToUtils.newProgram(objectGenerator, Integer.MAX_VALUE));
	}

	/** Test method for {@link ProgramFacade#moveDown(ProgramTO)}. */
	@Test
	public void testMoveDown() {
		final Program program1 = SpringEntitiesUtils.getProgram(1);
		program1.setPosition(1);
		final Program program2 = SpringEntitiesUtils.getProgram(2);
		program2.setPosition(0);

		programFacade.moveDown(SpringToUtils.newProgram(objectGenerator, 1));
		DeepAsserts.assertEquals(program1, SpringUtils.getProgram(entityManager, 1));
		DeepAsserts.assertEquals(program2, SpringUtils.getProgram(entityManager, 2));
		for (int i = 3; i <= SpringUtils.PROGRAMS_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getProgram(i), SpringUtils.getProgram(entityManager, i));
		}
		DeepAsserts.assertEquals(SpringUtils.PROGRAMS_COUNT, SpringUtils.getProgramsCount(entityManager));
	}

	/** Test method for {@link ProgramFacade#moveDown(ProgramTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testMoveDownWithNullArgument() {
		programFacade.moveDown(null);
	}

	/** Test method for {@link ProgramFacade#moveDown(ProgramTO)} with program with null ID. */
	@Test(expected = ValidationException.class)
	public void testMoveDownWithProgramWithNullId() {
		programFacade.moveDown(SpringToUtils.newProgram(objectGenerator));
	}

	/** Test method for {@link ProgramFacade#moveDown(ProgramTO)} with not moveable argument. */
	@Test(expected = ValidationException.class)
	public void testMoveDownWithNotMoveableArgument() {
		programFacade.moveDown(SpringToUtils.newProgram(objectGenerator, SpringUtils.PROGRAMS_COUNT));
	}

	/** Test method for {@link ProgramFacade#moveDown(ProgramTO)} with bad ID. */
	@Test(expected = RecordNotFoundException.class)
	public void testMoveDownWithBadId() {
		programFacade.moveDown(SpringToUtils.newProgram(objectGenerator, Integer.MAX_VALUE));
	}

	/** Test method for {@link ProgramFacade#exists(ProgramTO)} with existing program. */
	@Test
	public void testExists() {
		for (int i = 1; i <= SpringUtils.PROGRAMS_COUNT; i++) {
			assertTrue(programFacade.exists(SpringToUtils.newProgram(objectGenerator, i)));
		}

		assertFalse(programFacade.exists(SpringToUtils.newProgram(objectGenerator, Integer.MAX_VALUE)));

		DeepAsserts.assertEquals(SpringUtils.PROGRAMS_COUNT, SpringUtils.getProgramsCount(entityManager));
	}

	/** Test method for {@link ProgramFacade#exists(ProgramTO)} with null argument. */
	@Test(expected = IllegalArgumentException.class)
	public void testExistsWithNullArgument() {
		programFacade.exists(null);
	}

	/** Test method for {@link ProgramFacade#exists(ProgramTO)} with program with null ID. */
	@Test(expected = ValidationException.class)
	public void testExistsWithProgramWithNullId() {
		programFacade.exists(SpringToUtils.newProgram(objectGenerator));
	}

	/** Test method for {@link ProgramFacade#updatePositions()}. */
	@Test
	public void testUpdatePositions() {
		programFacade.updatePositions();

		for (int i = 1; i <= SpringUtils.PROGRAMS_COUNT; i++) {
			DeepAsserts.assertEquals(SpringEntitiesUtils.getProgram(i), SpringUtils.getProgram(entityManager, i));
		}
		DeepAsserts.assertEquals(SpringUtils.PROGRAMS_COUNT, SpringUtils.getProgramsCount(entityManager));
	}

	/** Test method for {@link ProgramFacade#getTotalMediaCount()}. */
	@Test
	public void testGetTotalMediaCount() {
		final int count = 600;

		DeepAsserts.assertEquals(count, programFacade.getTotalMediaCount());
		DeepAsserts.assertEquals(SpringUtils.PROGRAMS_COUNT, SpringUtils.getProgramsCount(entityManager));
	}

}
