package cz.vhromada.catalog.gui.programs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

import cz.vhromada.catalog.commons.CatalogSwingConstant2;
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

	/** Horizontal label size */
	private static final int HORIZONTAL_LABEL_SIZE = 150;

	/** Horizontal data size */
	private static final int HORIZONTAL_DATA_SIZE = 600;

	/** Horizontal button size */
	private static final int HORIZONTAL_BUTTON_SIZE = 120;

	/** Horizontal gap size */
	private static final int HORIZONTAL_GAP_SIZE = 10;

	/** Vertical gap size */
	private static final int VERTICAL_GAP_SIZE = 10;

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

	/** Button for showing program's czech Wikipedia page */
	private JButton wikiCzButton = new JButton("Czech Wikipedia");

	/** Button for showing program's english Wikipedia page */
	private JButton wikiEnButton = new JButton("English Wikipedia");

	/** URL to czech Wikipedia page about program */
	private String wikiCz;

	/** URL to english Wikipedia page about program */
	private String wikiEn;

	/**
	 * Creates a new instance of ProgramDataPanel.
	 *
	 * @param program TO for program
	 * @throws IllegalArgumentException if TO for program is null
	 */
	public ProgramDataPanel(final ProgramTO program) {
		Validators.validateArgumentNotNull(program, "TO for program");

		initData(nameLabel, nameData, program.getName());
		initData(dataLabel, dataData, program.getAdditionalData());
		initData(mediaCountLabel, mediaCountData, Integer.toString(program.getMediaCount()));
		initData(noteLabel, noteData, program.getNote());

		wikiCz = program.getWikiCz();
		wikiCzButton.setEnabled(!wikiCz.isEmpty());
		wikiCzButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final String url = "http://cs.wikipedia.org/wiki/" + wikiCz;
				try {
					Runtime.getRuntime().exec("rundll32.exe url.dll,FileProtocolHandler " + url);
				} catch (final IOException ex) {
					logger.error("Error in showing czech Wikipedia page.", ex);
				}
			}

		});

		wikiEn = program.getWikiEn();
		wikiEnButton.setEnabled(!wikiEn.isEmpty());
		wikiEnButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final String url = "http://en.wikipedia.org/wiki/" + wikiCz;
				try {
					Runtime.getRuntime().exec("rundll32.exe url.dll,FileProtocolHandler " + url);
				} catch (final IOException ex) {
					logger.error("Error in showing english Wikipedia page.", ex);
				}
			}

		});

		final GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setHorizontalGroup(createHorizontalLayout(layout));
		layout.setVerticalGroup(createVerticalLayout(layout));
	}

	/**
	 * Updates TO for program.
	 *
	 * @param program TO for program
	 * @throws IllegalArgumentException if TO for program is null
	 */
	public void updateProgramTO(final ProgramTO program) {
		Validators.validateArgumentNotNull(program, "TO for program");

		nameData.setText(program.getName());
		dataData.setText(program.getAdditionalData());
		mediaCountData.setText(Integer.toString(program.getMediaCount()));
		noteData.setText(program.getNote());
		wikiCz = program.getWikiCz();
		wikiCzButton.setEnabled(!wikiCz.isEmpty());
		wikiEn = program.getWikiEn();
		wikiEnButton.setEnabled(!wikiEn.isEmpty());
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
	 * Returns horizontal layout of components.
	 *
	 * @param layout layout
	 * @return horizontal layout of components
	 */
	public GroupLayout.Group createHorizontalLayout(final GroupLayout layout) {
		final GroupLayout.Group buttons = layout.createSequentialGroup()
				.addComponent(wikiCzButton, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE)
				.addGap(HORIZONTAL_GAP_SIZE)
				.addComponent(wikiEnButton, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE);

		final GroupLayout.Group components = layout.createParallelGroup()
				.addGroup(createHorizontalDataComponents(layout, nameLabel, nameData))
				.addGroup(createHorizontalDataComponents(layout, dataLabel, dataData))
				.addGroup(createHorizontalDataComponents(layout, mediaCountLabel, mediaCountData))
				.addGroup(createHorizontalDataComponents(layout, noteLabel, noteData))
				.addGroup(buttons);

		return layout.createSequentialGroup()
				.addGap(HORIZONTAL_GAP_SIZE)
				.addGroup(components)
				.addGap(HORIZONTAL_GAP_SIZE);

	}

	/**
	 * Returns horizontal layout for label component with data component.
	 *
	 * @param layout layout
	 * @param label  label
	 * @param data   data
	 * @return horizontal layout for label component with data component
	 */
	private GroupLayout.Group createHorizontalDataComponents(final GroupLayout layout, final JLabel label, final JLabel data) {
		return layout.createSequentialGroup()
				.addComponent(label, HORIZONTAL_LABEL_SIZE, HORIZONTAL_LABEL_SIZE, HORIZONTAL_LABEL_SIZE)
				.addGap(HORIZONTAL_GAP_SIZE)
				.addComponent(data, HORIZONTAL_DATA_SIZE, HORIZONTAL_DATA_SIZE, HORIZONTAL_DATA_SIZE);
	}

	/**
	 * Returns vertical layout of components.
	 *
	 * @param layout layout
	 * @return vertical layout of components
	 */
	public GroupLayout.Group createVerticalLayout(final GroupLayout layout) {
		final GroupLayout.Group buttons = layout.createParallelGroup()
				.addComponent(wikiCzButton, CatalogSwingConstant2.VERTICAL_BUTTON_SIZE, CatalogSwingConstant2.VERTICAL_BUTTON_SIZE,
						CatalogSwingConstant2.VERTICAL_BUTTON_SIZE)
				.addGap(VERTICAL_GAP_SIZE)
				.addComponent(wikiEnButton, CatalogSwingConstant2.VERTICAL_BUTTON_SIZE, CatalogSwingConstant2.VERTICAL_BUTTON_SIZE,
						CatalogSwingConstant2.VERTICAL_BUTTON_SIZE);

		return layout.createSequentialGroup()
				.addGap(5)
				.addGroup(createVerticalComponents(layout, nameLabel, nameData))
				.addGap(VERTICAL_GAP_SIZE)
				.addGroup(createVerticalComponents(layout, dataLabel, dataData))
				.addGap(VERTICAL_GAP_SIZE)
				.addGroup(createVerticalComponents(layout, mediaCountLabel, mediaCountData))
				.addGap(VERTICAL_GAP_SIZE)
				.addGroup(createVerticalComponents(layout, noteLabel, noteData))
				.addGap(VERTICAL_GAP_SIZE)
				.addGroup(buttons)
				.addGap(5);
	}

	/**
	 * Returns vertical layout for label component with data component.
	 *
	 * @param layout layout
	 * @param label  label component
	 * @param data   data component
	 * @return vertical layout for label component with data component
	 */
	private GroupLayout.Group createVerticalComponents(final GroupLayout layout, final JComponent label, final JComponent data) {
		return layout.createParallelGroup()
				.addComponent(label, CatalogSwingConstant2.VERTICAL_COMPONENT_SIZE, CatalogSwingConstant2.VERTICAL_COMPONENT_SIZE,
						CatalogSwingConstant2.VERTICAL_COMPONENT_SIZE)
				.addGap(VERTICAL_GAP_SIZE)
				.addComponent(data, CatalogSwingConstant2.VERTICAL_COMPONENT_SIZE, CatalogSwingConstant2.VERTICAL_COMPONENT_SIZE,
						CatalogSwingConstant2.VERTICAL_COMPONENT_SIZE);
	}

}
