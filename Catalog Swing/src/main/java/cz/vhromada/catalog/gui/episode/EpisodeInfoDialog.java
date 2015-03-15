package cz.vhromada.catalog.gui.episode;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.facade.to.EpisodeTO;
import cz.vhromada.catalog.gui.commons.AbstractInfoDialog;
import cz.vhromada.catalog.gui.commons.TimeDataPanel;

/**
 * A class represents dialog for episode.
 *
 * @author Vladimir Hromada
 */
public class EpisodeInfoDialog extends AbstractInfoDialog<EpisodeTO> {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Label for episode's number
     */
    private JLabel numberLabel = new JLabel("Number of episode");

    /**
     * Spinner for episode's number
     */
    private JSpinner numberData = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));

    /**
     * Label for name
     */
    private JLabel nameLabel = new JLabel("Name");

    /**
     * Text field for name
     */
    private JTextField nameData = new JTextField();

    /**
     * Panel for length
     */
    private TimeDataPanel lengthPanel = new TimeDataPanel("Length");

    /**
     * Label for note
     */
    private JLabel noteLabel = new JLabel("Note");

    /**
     * Text field for note
     */
    private JTextField noteData = new JTextField();

    /**
     * Creates a new instance of EpisodeInfoDialog.
     */
    public EpisodeInfoDialog() {
        init();
    }

    /**
     * Creates a new instance of EpisodeInfoDialog.
     *
     * @param episode TO for episode
     * @throws IllegalArgumentException if TO for episode is null
     */
    public EpisodeInfoDialog(final EpisodeTO episode) {
        super(episode);

        init();
        this.numberData.setValue(episode.getNumber());
        this.nameData.setText(episode.getName());
        this.lengthPanel.setLength(new Time(episode.getLength()));
        this.noteData.setText(episode.getNote());
    }

    @Override
    protected void initComponents() {
        initLabelComponent(numberLabel, numberData);
        initLabelComponent(nameLabel, nameData);
        initLabelComponent(noteLabel, noteData);

        addInputValidator(nameData);
    }

    @Override
    protected EpisodeTO processData(final EpisodeTO objectData) {
        final EpisodeTO episode = objectData == null ? new EpisodeTO() : objectData;
        episode.setNumber((Integer) numberData.getValue());
        episode.setName(nameData.getText());
        episode.setLength(lengthPanel.getLength().getLength());
        episode.setNote(noteData.getText());

        return episode;
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
        return group
                .addGroup(createHorizontalComponents(layout, numberLabel, numberData))
                .addGroup(createHorizontalComponents(layout, nameLabel, nameData))
                .addComponent(lengthPanel)
                .addGroup(createHorizontalComponents(layout, noteLabel, noteData));
    }

    @Override
    protected GroupLayout.Group getVerticalLayoutWithComponents(final GroupLayout layout, final GroupLayout.Group group) {
        return group
                .addGroup(createVerticalComponents(layout, numberLabel, numberData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, nameLabel, nameData))
                .addGap(VERTICAL_GAP_SIZE)
                .addComponent(lengthPanel)
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, noteLabel, noteData));
    }

}
