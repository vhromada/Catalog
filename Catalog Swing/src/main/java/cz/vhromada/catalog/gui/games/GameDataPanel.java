package cz.vhromada.catalog.gui.games;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.*;

import cz.vhromada.catalog.commons.SwingUtils;
import cz.vhromada.catalog.facade.to.GameTO;
import cz.vhromada.validators.Validators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class represents panel with game's data.
 *
 * @author Vladimir Hromada
 */
public class GameDataPanel extends JPanel {

	/** Logger */
	private static final Logger logger = LoggerFactory.getLogger(GameDataPanel.class);

	/** SerialVersionUID */
	private static final long serialVersionUID = 1L;

	/** Label for name */
	private JLabel nameLabel = new JLabel("Name");

	/** Label with name */
	private JLabel nameData = new JLabel();

	/** Label for additional data */
	private JLabel dataLabel = new JLabel("Additional data");

	/** Label with additional data */
	private JLabel dataData = new JLabel();

	/** Label for count of media */
	private JLabel mediaCountLabel = new JLabel("Count of media");

	/** Label with count of media */
	private JLabel mediaCountData = new JLabel();

	/** Label for note */
	private JLabel noteLabel = new JLabel("Note");

	/** Label with note */
	private JLabel noteData = new JLabel();

	/** Button for showing game's Wikipedia page */
	private JButton wikiButton = new JButton("Wikipedia");

	/** URL to Wikipedia page about game */
	private String wiki;

	/**
	 * Creates a new instance of GameDataPanel.
	 *
	 * @param gameTO TO for game
	 * @throws IllegalArgumentException if TO for game is null
	 */
	public GameDataPanel(final GameTO gameTO) {
		Validators.validateArgumentNotNull(gameTO, "TO for game");

		initData(nameLabel, nameData, gameTO.getName());
		initData(dataLabel, dataData, gameTO.getAdditionalData());
		initData(mediaCountLabel, mediaCountData, Integer.toString(gameTO.getMediaCount()));
		initData(noteLabel, noteData, gameTO.getNote());

		wiki = gameTO.getWikiCz();
		wikiButton.setEnabled(!wiki.isEmpty());
		wikiButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				final String url = "http://en.wikipedia.org/wiki/" + wiki;
				try {
					Runtime.getRuntime().exec("rundll32.exe url.dll,FileProtocolHandler " + url);
				} catch (final IOException ex) {
					logger.error("Error in showing Wikipedia page.", ex);
				}

			}

		});

		final GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setHorizontalGroup(SwingUtils.createHorizontalLayout(layout, createComponentsMap(), wikiButton));
		layout.setVerticalGroup(SwingUtils.createVerticalLayout(layout, createComponentsMap(), wikiButton));
	}

	/**
	 * Updates TO for game.
	 *
	 * @param gameTO TO for game
	 * @throws IllegalArgumentException if TO for game is null
	 */
	public void updateGameTO(final GameTO gameTO) {
		Validators.validateArgumentNotNull(gameTO, "TO for game");

		nameData.setText(gameTO.getName());
		dataData.setText(gameTO.getAdditionalData());
		mediaCountData.setText(Integer.toString(gameTO.getMediaCount()));
		noteData.setText(gameTO.getNote());
		wiki = gameTO.getWikiCz();
		wikiButton.setEnabled(!wiki.isEmpty());
	}

	/**
	 * Initializes data.
	 *
	 * @param label label
	 * @param data  data
	 * @param text  text for data
	 */
	private void initData(final JLabel label, final JLabel data, final String text) {
		label.setFocusable(false);
		label.setLabelFor(data);
		data.setText(text);
		data.setFocusable(false);
	}

	/**
	 * Returns components map.
	 *
	 * @return components map
	 */
	private Map<JLabel, JLabel> createComponentsMap() {
		final Map<JLabel, JLabel> components = new LinkedHashMap<>(4);
		components.put(nameLabel, nameData);
		components.put(dataLabel, dataData);
		components.put(mediaCountLabel, mediaCountData);
		components.put(noteLabel, noteData);
		return components;
	}

}
