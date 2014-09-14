package cz.vhromada.catalog.facade.to;

import static cz.vhromada.catalog.common.TestConstants.OTHER_DATA;

import cz.vhromada.test.DeepAsserts;
import org.junit.Test;

/**
 * A class represents test for class {@link GameTO}.
 *
 * @author Vladimir Hromada
 */
public class GameTOTest {

	/** Test method for {@link GameTO#getAdditionalData()}. */
	@Test
	public void testGetAdditionalData() {
		final GameTO game = new GameTO();
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
		game.setOtherData(OTHER_DATA);
		DeepAsserts.assertEquals("Crack, serial key, patch, trainer, data for trainer, editor, saves, " + OTHER_DATA, game.getAdditionalData());
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
		game.setOtherData(OTHER_DATA);
		DeepAsserts.assertEquals(OTHER_DATA, game.getAdditionalData());
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
