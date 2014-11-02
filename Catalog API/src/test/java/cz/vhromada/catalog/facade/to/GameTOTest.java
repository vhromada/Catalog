package cz.vhromada.catalog.facade.to;

import cz.vhromada.generator.ObjectGenerator;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for class {@link GameTO}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testGeneratorContext.xml")
public class GameTOTest {

	/** Instance of {@link ObjectGenerator} */
	@Autowired
	private ObjectGenerator objectGenerator;

	/** Test method for {@link GameTO#getAdditionalData()}. */
	@Test
	public void testGetAdditionalData() {
		final GameTO game = new GameTO();
		final String otherData = objectGenerator.generate(String.class);

		game.setCrack(true);
		DeepAsserts.assertEquals("Crack", game.getAdditionalData());

		game.setSerialKey(true);
		DeepAsserts.assertEquals("Crack, serial key", game.getAdditionalData());

		game.setPatch(true);
		DeepAsserts.assertEquals("Crack, serial key, patch", game.getAdditionalData());

		game.setTrainer(true);
		DeepAsserts.assertEquals("Crack, serial key, patch, trainer", game.getAdditionalData());

		game.setTrainerData(true);
		DeepAsserts.assertEquals("Crack, serial key, patch, trainer, data for trainer", game.getAdditionalData());

		game.setEditor(true);
		DeepAsserts.assertEquals("Crack, serial key, patch, trainer, data for trainer, editor", game.getAdditionalData());

		game.setSaves(true);
		DeepAsserts.assertEquals("Crack, serial key, patch, trainer, data for trainer, editor, saves", game.getAdditionalData());

		game.setOtherData(otherData);
		DeepAsserts.assertEquals("Crack, serial key, patch, trainer, data for trainer, editor, saves, " + otherData, game.getAdditionalData());

		clearAdditionalData(game);
		game.setSerialKey(true);
		DeepAsserts.assertEquals("Serial key", game.getAdditionalData());

		clearAdditionalData(game);
		game.setPatch(true);
		DeepAsserts.assertEquals("Patch", game.getAdditionalData());

		clearAdditionalData(game);
		game.setTrainer(true);
		DeepAsserts.assertEquals("Trainer", game.getAdditionalData());

		clearAdditionalData(game);
		game.setTrainerData(true);
		DeepAsserts.assertEquals("Data for trainer", game.getAdditionalData());

		clearAdditionalData(game);
		game.setEditor(true);
		DeepAsserts.assertEquals("Editor", game.getAdditionalData());

		clearAdditionalData(game);
		game.setSaves(true);
		DeepAsserts.assertEquals("Saves", game.getAdditionalData());

		clearAdditionalData(game);
		game.setOtherData(otherData);
		DeepAsserts.assertEquals(otherData, game.getAdditionalData());
	}

	/**
	 * Clear additional data.
	 *
	 * @param game TO for game
	 */
	private void clearAdditionalData(final GameTO game) {
		game.setCrack(false);
		game.setSerialKey(false);
		game.setPatch(false);
		game.setTrainer(false);
		game.setTrainerData(false);
		game.setEditor(false);
		game.setSaves(false);
		game.setOtherData(null);
	}

}
