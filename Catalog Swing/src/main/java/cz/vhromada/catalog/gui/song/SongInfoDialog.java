package cz.vhromada.catalog.gui.song;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.facade.to.SongTO;
import cz.vhromada.catalog.gui.commons.AbstractInfoDialog;
import cz.vhromada.catalog.gui.commons.CatalogSwingConstants;

/**
 * A class represents dialog for song.
 *
 * @author Vladimir Hromada
 */
public class SongInfoDialog extends AbstractInfoDialog<SongTO> {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Horizontal time size
     */
    private static final int HORIZONTAL_TIME_SIZE = 60;

    /**
     * Maximum hours
     */
    private static final int MAX_HOURS = 23;

    /**
     * Maximum minutes
     */
    private static final int MAX_MINUTES = 59;

    /**
     * Maximum seconds
     */
    private static final int MAX_SECONDS = 59;

    /**
     * Label for name
     */
    private JLabel nameLabel = new JLabel("Name");

    /**
     * Text field for name
     */
    private JTextField nameData = new JTextField();

    /**
     * Label for length
     */
    private JLabel lengthLabel = new JLabel("Length");

    /**
     * Spinner for length - hours
     */
    private JSpinner lengthHoursData = new JSpinner(new SpinnerNumberModel(0, 0, MAX_HOURS, 1));

    /**
     * Spinner for length - minutes
     */
    private JSpinner lengthMinutesData = new JSpinner(new SpinnerNumberModel(0, 0, MAX_MINUTES, 1));

    /**
     * Spinner for length - seconds
     */
    private JSpinner lengthSecondsData = new JSpinner(new SpinnerNumberModel(0, 0, MAX_SECONDS, 1));

    /**
     * Label for note
     */
    private JLabel noteLabel = new JLabel("Note");

    /**
     * Text field for note
     */
    private JTextField noteData = new JTextField();

    /**
     * Creates a new instance of SongInfoDialog.
     */
    public SongInfoDialog() {
        super();

        initComponents();
        nameData.requestFocusInWindow();
        createLayout();
    }

    /**
     * Creates a new instance of SongInfoDialog.
     *
     * @param song TO for song
     * @throws IllegalArgumentException if TO for song is null
     */
    public SongInfoDialog(final SongTO song) {
        super(song);

        initComponents();
        this.nameData.setText(song.getName());
        final Time length = new Time(song.getLength());
        this.lengthHoursData.setValue(length.getData(Time.TimeData.HOUR));
        this.lengthMinutesData.setValue(length.getData(Time.TimeData.MINUTE));
        this.lengthSecondsData.setValue(length.getData(Time.TimeData.SECOND));
        this.noteData.setText(song.getNote());
        createLayout();
    }

    @Override
    protected SongTO processData(final SongTO objectData) {
        final SongTO song = objectData == null ? new SongTO() : objectData;
        song.setName(nameData.getText());
        final int hours = (Integer) lengthHoursData.getValue();
        final int minutes = (Integer) lengthMinutesData.getValue();
        final int seconds = (Integer) lengthSecondsData.getValue();
        song.setLength(new Time(hours, minutes, seconds).getLength());
        song.setNote(noteData.getText());

        return song;
    }

    /**
     * Returns true if input is valid: name isn't empty string.
     *
     * @return true if input is valid: name isn't empty string
     */
    @Override
    protected boolean isInputValid() {
        return !nameData.getText().isEmpty();
    }

    @Override
    protected GroupLayout.Group getHorizontalLayoutWithComponents(final GroupLayout layout, final GroupLayout.Group group) {
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

        return group
                .addGroup(createHorizontalComponents(layout, nameLabel, nameData))
                .addGroup(length)
                .addGroup(createHorizontalComponents(layout, noteLabel, noteData));
    }

    @Override
    protected GroupLayout.Group getVerticalLayoutWithComponents(final GroupLayout layout, final GroupLayout.Group group) {
        final GroupLayout.Group length = layout.createParallelGroup()
                .addComponent(lengthLabel, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
                .addComponent(lengthHoursData, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
                .addComponent(lengthMinutesData, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
                .addComponent(lengthSecondsData, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE);

        return group
                .addGroup(createVerticalComponents(layout, nameLabel, nameData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(length)
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, noteLabel, noteData));
    }

    /**
     * Initializes components.
     */
    private void initComponents() {
        initLabelComponent(nameLabel, nameData);
        initLabelComponent(noteLabel, noteData);

        addInputValidator(nameData);
    }

}
