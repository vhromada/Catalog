package cz.vhromada.catalog.gui.episode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import cz.vhromada.catalog.commons.CatalogSwingConstant2;
import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.facade.to.EpisodeTO;
import cz.vhromada.catalog.gui.DialogResult;
import cz.vhromada.catalog.gui.InputValidator;
import cz.vhromada.catalog.gui.Pictures;
import cz.vhromada.validators.Validators;

/**
 * A class represents dialog for episode.
 *
 * @author Vladimir Hromada
 */
public class EpisodeInfoDialog extends JDialog {

    /** SerialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Horizontal label size in dialog */
    private static final int HORIZONTAL_LABEL_DIALOG_SIZE = 100;

    /** Horizontal data size in dialog */
    private static final int HORIZONTAL_DATA_DIALOG_SIZE = 200;

    /** Horizontal time size */
    private static final int HORIZONTAL_TIME_SIZE = 60;

    /** Horizontal button size */
    private static final int HORIZONTAL_BUTTON_SIZE = 96;

    /** Horizontal button gap size */
    private static final int HORIZONTAL_BUTTON_GAP_SIZE = 32;

    /** Horizontal gap size */
    private static final int HORIZONTAL_GAP_SIZE = 20;

    /** Vertical gap size */
    private static final int VERTICAL_GAP_SIZE = 10;

    /** Vertical long gap size */
    private static final int VERTICAL_LONG_GAP_SIZE = 20;

    /** Maximum hours */
    private static final int MAX_HOURS = 23;

    /** Maximum minutes */
    private static final int MAX_MINUTES = 59;

    /** Maximum seconds */
    private static final int MAX_SECONDS = 59;

    /** Return status */
    private DialogResult returnStatus = DialogResult.CANCEL;

    /** TO for episode */
    private EpisodeTO episode;

    /** Label for episode's number */
    private JLabel numberLabel = new JLabel("Number of episode");

    /** Spinner for episode's number */
    private JSpinner numberData = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));

    /** Label for name */
    private JLabel nameLabel = new JLabel("Name");

    /** Text field for name */
    private JTextField nameData = new JTextField();

    /** Label for length */
    private JLabel lengthLabel = new JLabel("Length");

    /** Spinner for length - hours */
    private JSpinner lengthHoursData = new JSpinner(new SpinnerNumberModel(0, 0, MAX_HOURS, 1));

    /** Spinner for length - minutes */
    private JSpinner lengthMinutesData = new JSpinner(new SpinnerNumberModel(0, 0, MAX_MINUTES, 1));

    /** Spinner for length - seconds */
    private JSpinner lengthSecondsData = new JSpinner(new SpinnerNumberModel(0, 0, MAX_SECONDS, 1));

    /** Label for note */
    private JLabel noteLabel = new JLabel("Note");

    /** Text field for note */
    private JTextField noteData = new JTextField();

    /** Button OK */
    private JButton okButton = new JButton("OK", Pictures.getPicture("ok"));

    /** Button Cancel */
    private JButton cancelButton = new JButton("Cancel", Pictures.getPicture("cancel"));

    /** Creates a new instance of EpisodeInfoDialog. */
    public EpisodeInfoDialog() {
        this("Add", "add");

        numberData.requestFocusInWindow();
    }

    /**
     * Creates a new instance of EpisodeInfoDialog.
     *
     * @param episode TO for episode
     * @throws IllegalArgumentException if TO for episode is null
     */
    public EpisodeInfoDialog(final EpisodeTO episode) {
        this("Update", "update");

        Validators.validateArgumentNotNull(episode, "TO for episode");

        this.episode = episode;
        this.numberData.setValue(episode.getNumber());
        this.nameData.setText(episode.getName());
        final Time length = new Time(episode.getLength());
        this.lengthHoursData.setValue(length.getData(Time.TimeData.HOUR));
        this.lengthMinutesData.setValue(length.getData(Time.TimeData.MINUTE));
        this.lengthSecondsData.setValue(length.getData(Time.TimeData.SECOND));
        this.noteData.setText(episode.getNote());
        this.okButton.requestFocusInWindow();
    }

    /**
     * Creates a new instance of EpisodeInfoDialog.
     *
     * @param name    name
     * @param picture picture
     */
    private EpisodeInfoDialog(final String name, final String picture) {
        super(new JFrame(), name, true);

        initComponents();
        setIconImage(Pictures.getPicture(picture).getImage());
    }

    /**
     * Returns return status.
     *
     * @return return status
     */
    public DialogResult getReturnStatus() {
        return returnStatus;
    }

    /**
     * Returns TO for episode.
     *
     * @return TO for episode
     * @throws IllegalStateException if TO for hasn't been set
     */
    public EpisodeTO getEpisode() {
        Validators.validateFieldNotNull(episode, "TO for episode");

        return episode;
    }

    /** Initializes components. */
    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        initLabelComponent(numberLabel, numberData);
        initLabelComponent(nameLabel, nameData);
        initLabelComponent(noteLabel, noteData);

        nameData.getDocument().addDocumentListener(new InputValidator(okButton) {

            @Override
            public boolean isInputValid() {
                return EpisodeInfoDialog.this.isInputValid();
            }

        });
        lengthLabel.setFocusable(false);

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
        returnStatus = DialogResult.OK;
        if (episode == null) {
            episode = new EpisodeTO();
        }
        episode.setNumber((Integer) numberData.getValue());
        episode.setName(nameData.getText());
        final int hours = (Integer) lengthHoursData.getValue();
        final int minutes = (Integer) lengthMinutesData.getValue();
        final int seconds = (Integer) lengthSecondsData.getValue();
        episode.setLength(new Time(hours, minutes, seconds).getLength());
        episode.setNote(noteData.getText());
        close();
    }

    /** Performs action for button Cancel. */
    private void cancelAction() {
        returnStatus = DialogResult.CANCEL;
        episode = null;
        close();
    }

    /**
     * Returns horizontal layout of components.
     *
     * @param layout layout
     * @return horizontal layout of components
     */
    private GroupLayout.Group createHorizontalLayout(final GroupLayout layout) {
        final GroupLayout.Group lengthData = layout.createSequentialGroup()
                .addComponent(lengthHoursData, HORIZONTAL_TIME_SIZE, HORIZONTAL_TIME_SIZE, HORIZONTAL_TIME_SIZE)
                .addGap(10)
                .addComponent(lengthMinutesData, HORIZONTAL_TIME_SIZE, HORIZONTAL_TIME_SIZE, HORIZONTAL_TIME_SIZE)
                .addGap(10)
                .addComponent(lengthSecondsData, HORIZONTAL_TIME_SIZE, HORIZONTAL_TIME_SIZE, HORIZONTAL_TIME_SIZE);
        final GroupLayout.Group length = layout.createSequentialGroup()
                .addComponent(lengthLabel, HORIZONTAL_LABEL_DIALOG_SIZE, HORIZONTAL_LABEL_DIALOG_SIZE, HORIZONTAL_LABEL_DIALOG_SIZE)
                .addGap(10)
                .addGroup(lengthData);

        final GroupLayout.Group buttons = layout.createSequentialGroup()
                .addGap(HORIZONTAL_BUTTON_GAP_SIZE)
                .addComponent(okButton, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE)
                .addGap(54)
                .addComponent(cancelButton, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE)
                .addGap(HORIZONTAL_BUTTON_GAP_SIZE);


        final GroupLayout.Group components = layout.createParallelGroup()
                .addGroup(createHorizontalComponents(layout, numberLabel, numberData))
                .addGroup(length)
                .addGroup(createHorizontalComponents(layout, nameLabel, nameData))
                .addGroup(length)
                .addGroup(createHorizontalComponents(layout, noteLabel, noteData))
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
     * @param label  label component
     * @param data   data component
     * @return horizontal layout for label component with data component
     */
    private static GroupLayout.Group createHorizontalComponents(final GroupLayout layout, final JComponent label, final JComponent data) {
        return layout.createSequentialGroup()
                .addComponent(label, HORIZONTAL_LABEL_DIALOG_SIZE, HORIZONTAL_LABEL_DIALOG_SIZE, HORIZONTAL_LABEL_DIALOG_SIZE)
                .addGap(10)
                .addComponent(data, HORIZONTAL_DATA_DIALOG_SIZE, HORIZONTAL_DATA_DIALOG_SIZE, HORIZONTAL_DATA_DIALOG_SIZE);
    }

    /**
     * Returns vertical layout of components.
     *
     * @param layout layout
     * @return vertical layout of components
     */
    private GroupLayout.Group createVerticalLayout(final GroupLayout layout) {
        final GroupLayout.Group length = layout.createParallelGroup()
                .addComponent(lengthLabel, CatalogSwingConstant2.VERTICAL_COMPONENT_SIZE, CatalogSwingConstant2.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstant2.VERTICAL_COMPONENT_SIZE)
                .addComponent(lengthHoursData, CatalogSwingConstant2.VERTICAL_COMPONENT_SIZE, CatalogSwingConstant2.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstant2.VERTICAL_COMPONENT_SIZE)
                .addComponent(lengthMinutesData, CatalogSwingConstant2.VERTICAL_COMPONENT_SIZE, CatalogSwingConstant2.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstant2.VERTICAL_COMPONENT_SIZE)
                .addComponent(lengthSecondsData, CatalogSwingConstant2.VERTICAL_COMPONENT_SIZE, CatalogSwingConstant2.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstant2.VERTICAL_COMPONENT_SIZE);

        final GroupLayout.Group buttons = layout.createParallelGroup()
                .addComponent(okButton, CatalogSwingConstant2.VERTICAL_BUTTON_SIZE, CatalogSwingConstant2.VERTICAL_BUTTON_SIZE,
                        CatalogSwingConstant2.VERTICAL_BUTTON_SIZE)
                .addComponent(cancelButton, CatalogSwingConstant2.VERTICAL_BUTTON_SIZE, CatalogSwingConstant2.VERTICAL_BUTTON_SIZE,
                        CatalogSwingConstant2.VERTICAL_BUTTON_SIZE);

        return layout.createSequentialGroup()
                .addGap(VERTICAL_LONG_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, numberLabel, numberData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, nameLabel, nameData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(length)
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, noteLabel, noteData))
                .addGap(VERTICAL_LONG_GAP_SIZE)
                .addGroup(buttons)
                .addGap(VERTICAL_LONG_GAP_SIZE);
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
