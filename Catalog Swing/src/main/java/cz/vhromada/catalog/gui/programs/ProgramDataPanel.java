package cz.vhromada.catalog.gui.programs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.*;

import cz.vhromada.catalog.commons.SwingUtils;
import cz.vhromada.catalog.facade.to.ProgramTO;
import cz.vhromada.validators.Validators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class represents panel with program's data.
 *
 * @author Vladimir Hromada
 */
public class ProgramDataPanel extends JPanel {

	/** Logger */
	private static final Logger logger = LoggerFactory.getLogger(ProgramDataPanel.class);

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

	/** Button for showing program's Wikipedia page */
	private JButton wikiButton = new JButton("Wikipedia");

	/** URL to Wikipedia page about program */
	private String wiki;

	/**
	 * Creates a new instance of ProgramDataPanel.
	 *
	 * @param programTO TO for program
	 * @throws IllegalArgumentException if TO for program is null
	 */
	public ProgramDataPanel(final ProgramTO programTO) {
		Validators.validateArgumentNotNull(programTO, "TO for program");

		initData(nameLabel, nameData, programTO.getName());
		initData(dataLabel, dataData, programTO.getAdditionalData());
		initData(mediaCountLabel, mediaCountData, Integer.toString(programTO.getMediaCount()));
		initData(noteLabel, noteData, programTO.getNote());

		wiki = programTO.getWikiCz();
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
	 * Updates TO for program.
	 *
	 * @param programTO TO for program
	 * @throws IllegalArgumentException if TO for program is null
	 */
	public void updateProgramTO(final ProgramTO programTO) {
		Validators.validateArgumentNotNull(programTO, "TO for program");

		nameData.setText(programTO.getName());
		dataData.setText(programTO.getAdditionalData());
		mediaCountData.setText(Integer.toString(programTO.getMediaCount()));
		noteData.setText(programTO.getNote());
		wiki = programTO.getWikiCz();
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
