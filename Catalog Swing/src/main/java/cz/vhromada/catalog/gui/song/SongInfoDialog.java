package cz.vhromada.catalog.gui.song;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.facade.to.SongTO;
import cz.vhromada.catalog.gui.commons.AbstractInfoDialog;
import cz.vhromada.catalog.gui.commons.TimeDataPanel;

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
     * Label for name
     */
    private JLabel nameLabel = new JLabel("Name");

    /**
     * Text field for name
     */
    private JTextField nameData = new JTextField();

    /**
     * Length panel
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
     * Creates a new instance of SongInfoDialog.
     */
    public SongInfoDialog() {
        init();
    }

    /**
     * Creates a new instance of SongInfoDialog.
     *
     * @param song TO for song
     * @throws IllegalArgumentException if TO for song is null
     */
    public SongInfoDialog(final SongTO song) {
        super(song);

        init();
        this.nameData.setText(song.getName());
        this.lengthPanel.setLength(new Time(song.getLength()));
        this.noteData.setText(song.getNote());
    }

    @Override
    protected void initComponents() {
        initLabelComponent(nameLabel, nameData);
        initLabelComponent(noteLabel, noteData);

        addInputValidator(nameData);
    }

    @Override
    protected SongTO processData(final SongTO objectData) {
        final SongTO song = objectData == null ? new SongTO() : objectData;
        song.setName(nameData.getText());
        song.setLength(lengthPanel.getLength().getLength());
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
        return group
                .addGroup(createHorizontalComponents(layout, nameLabel, nameData))
                .addComponent(lengthPanel)
                .addGroup(createHorizontalComponents(layout, noteLabel, noteData));
    }

    @Override
    protected GroupLayout.Group getVerticalLayoutWithComponents(final GroupLayout layout, final GroupLayout.Group group) {
        return group
                .addGroup(createVerticalComponents(layout, nameLabel, nameData))
                .addGap(VERTICAL_GAP_SIZE)
                .addComponent(lengthPanel)
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, noteLabel, noteData));
    }

}
