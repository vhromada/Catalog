package cz.vhromada.catalog.gui.programs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import cz.vhromada.catalog.commons.CatalogSwingConstants;
import cz.vhromada.catalog.commons.SwingUtils;
import cz.vhromada.catalog.facade.to.ProgramTO;
import cz.vhromada.catalog.gui.InputValidator;
import cz.vhromada.catalog.gui.Pictures;
import cz.vhromada.validators.Validators;

/**
 * A class represents dialog for program.
 *
 * @author Vladimir Hromada
 */
public class ProgramInfoDialog extends JDialog {

	/** SerialVersionUID */
	private static final long serialVersionUID = 1L;

	/** Return status */
	private int returnStatus;

	/** TO for program */
	private ProgramTO programTO;

	/** Label for name */
	private JLabel nameLabel = new JLabel("Name");

	/** Text field for name */
	private JTextField nameData = new JTextField();

	/** Label for Wikipedia */
	private JLabel wikiLabel = new JLabel("Wikipedia");

	/** Text field for Wikipedia */
	private JTextField wikiData = new JTextField();

	/** Label for count of media */
	private JLabel mediaCountLabel = new JLabel("Count of media");

	/** Spinner for count of media */
	private JSpinner mediaCountData = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));

	/** Check box for crack */
	private JCheckBox crackData = new JCheckBox("Crack");

	/** Check box for serial key */
	private JCheckBox serialData = new JCheckBox("Serial key");

	/** Label for other data */
	private JLabel otherDataLabel = new JLabel("Other data");

	/** Text field for other data */
	private JTextField otherDataData = new JTextField();

	/** Label for note */
	private JLabel noteLabel = new JLabel("Note");

	/** Text field for note */
	private JTextField noteData = new JTextField();

	/** Button OK */
	private JButton okButton = new JButton("OK", Pictures.getPicture("ok"));

	/** Button Cancel */
	private JButton cancelButton = new JButton("Cancel", Pictures.getPicture("cancel"));

	/** Creates a new instance of ProgramInfoDialog. */
	public ProgramInfoDialog() {
		this("Add", "add");

		nameData.requestFocusInWindow();
	}

	/**
	 * Creates a new instance of ProgramInfoDialog.
	 *
	 * @param programTO TO for program
	 * @throws IllegalArgumentException if TO for program is null
	 */
	public ProgramInfoDialog(final ProgramTO programTO) {
		this("Update", "update");

		Validators.validateArgumentNotNull(programTO, "TO for program");

		this.programTO = programTO;
		this.nameData.setText(programTO.getName());
		this.wikiData.setText(programTO.getWikiCz());
		this.mediaCountData.setValue(programTO.getMediaCount());
		this.crackData.setSelected(programTO.hasCrack());
		this.serialData.setSelected(programTO.hasSerialKey());
		this.otherDataData.setText(programTO.getOtherData());
		this.noteData.setText(programTO.getNote());
		this.okButton.requestFocusInWindow();
	}

	/**
	 * Creates a new instance of ProgramInfoDialog.
	 *
	 * @param name    name
	 * @param picture picture
	 */
	private ProgramInfoDialog(final String name, final String picture) {
		super(new JFrame(), name, true);

		initComponents();
		setIconImage(Pictures.getPicture(picture).getImage());
	}

	/**
	 * Returns return status.
	 *
	 * @return return status
	 */
	public int getReturnStatus() {
		return returnStatus;
	}

	/**
	 * Returns TO for program.
	 *
	 * @return TO for ame
	 * @throws IllegalStateException if TO for program hasn't been set
	 */
	public ProgramTO getProgramTO() {
		Validators.validateFieldNotNull(programTO, "TO for program");

		return programTO;
	}

	/** Initializes components. */
	private void initComponents() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);

		initLabelComponent(nameLabel, nameData);
		initLabelComponent(wikiLabel, wikiData);
		initLabelComponent(mediaCountLabel, mediaCountData);
		initLabelComponent(otherDataLabel, otherDataData);
		initLabelComponent(noteLabel, noteData);

		nameData.getDocument().addDocumentListener(new InputValidator(okButton) {

			@Override
			public boolean isInputValid() {
				return ProgramInfoDialog.this.isInputValid();
			}

		});

		okButton.setEnabled(false);
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				okAction();
			}

		});

		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				cancelAction();
			}

		});

		final GroupLayout layout = new GroupLayout(getRootPane());
		getRootPane().setLayout(layout);
		layout.setHorizontalGroup(createHorizontalLayout(layout));
		layout.setVerticalGroup(createVerticalLayout(layout));

		pack();
		setLocationRelativeTo(getRootPane());
	}

	/**
	 * Initializes label with component.
	 *
	 * @param label     label
	 * @param component component
	 */
	private void initLabelComponent(final JLabel label, final JComponent component) {
		label.setLabelFor(component);
		label.setFocusable(false);
	}

	/** Performs action for button OK. */
	private void okAction() {
		returnStatus = CatalogSwingConstants.RET_OK;
		if (programTO == null) {
			programTO = new ProgramTO();
		}
		programTO.setName(nameData.getText());
		programTO.setWikiEn(wikiData.getText());
		programTO.setMediaCount((Integer) mediaCountData.getValue());
		programTO.setCrack(crackData.isSelected());
		programTO.setSerialKey(serialData.isSelected());
		programTO.setOtherData(otherDataData.getText());
		programTO.setNote(noteData.getText());
		close();
	}

	/** Performs action for button Cancel. */
	private void cancelAction() {
		returnStatus = CatalogSwingConstants.RET_CANCEL;
		programTO = null;
		close();
	}

	/**
	 * Returns horizontal layout of components.
	 *
	 * @param layout layout
	 * @return horizontal layout of components
	 */
	private GroupLayout.Group createHorizontalLayout(final GroupLayout layout) {
		return SwingUtils.createHorizontalDialogLayout(layout,
				SwingUtils.createHorizontalComponents(layout, nameLabel, nameData),
				SwingUtils.createHorizontalComponents(layout, wikiLabel, wikiData),
				SwingUtils.createHorizontalComponents(layout, mediaCountLabel, mediaCountData),
				SwingUtils.createHorizontalCheckBoxesComponents(layout, crackData, serialData),
				SwingUtils.createHorizontalComponents(layout, otherDataLabel, otherDataData),
				SwingUtils.createHorizontalComponents(layout, noteLabel, noteData),
				SwingUtils.createHorizontalButtonComponents(layout, okButton, cancelButton));
	}

	/**
	 * Returns vertical layout of components.
	 *
	 * @param layout layout
	 * @return vertical layout of components
	 */
	private GroupLayout.Group createVerticalLayout(final GroupLayout layout) {
		return SwingUtils.createVerticalDialogLayout(layout, okButton, cancelButton,
				SwingUtils.createVerticalComponents(layout, nameLabel, nameData),
				SwingUtils.createVerticalComponents(layout, wikiLabel, wikiData),
				SwingUtils.createVerticalComponents(layout, mediaCountLabel, mediaCountData),
				SwingUtils.createVerticalSelectableComponents(layout, crackData, serialData),
				SwingUtils.createVerticalComponents(layout, otherDataLabel, otherDataData),
				SwingUtils.createVerticalComponents(layout, noteLabel, noteData));
	}

	/**
	 * Returns true if input is valid: name isn't empty string.
	 *
	 * @return true if input is valid: name isn't empty string
	 */
	private boolean isInputValid() {
		return !nameData.getText().isEmpty();
	}

	/** Closes dialog. */
	private void close() {
		setVisible(false);
		dispose();
	}

}
