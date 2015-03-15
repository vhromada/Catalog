package cz.vhromada.catalog.gui.program;

import javax.swing.GroupLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import cz.vhromada.catalog.facade.to.ProgramTO;
import cz.vhromada.catalog.gui.commons.AbstractInfoDialog;
import cz.vhromada.catalog.gui.commons.CatalogSwingConstants;

/**
 * A class represents dialog for program.
 *
 * @author Vladimir Hromada
 */
public class ProgramInfoDialog extends AbstractInfoDialog<ProgramTO> {

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
     * Label for czech Wikipedia
     */
    private JLabel wikiCzLabel = new JLabel("Czech Wikipedia");

    /**
     * Text field for czech Wikipedia
     */
    private JTextField wikiCzData = new JTextField();

    /**
     * Label for english Wikipedia
     */
    private JLabel wikiEnLabel = new JLabel("English Wikipedia");

    /**
     * Text field for english Wikipedia
     */
    private JTextField wikiEnData = new JTextField();

    /**
     * Label for count of media
     */
    private JLabel mediaCountLabel = new JLabel("Count of media");

    /**
     * Spinner for count of media
     */
    private JSpinner mediaCountData = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));

    /**
     * Check box for crack
     */
    private JCheckBox crackData = new JCheckBox("Crack");

    /**
     * Check box for serial key
     */
    private JCheckBox serialData = new JCheckBox("Serial key");

    /**
     * Label for other data
     */
    private JLabel otherDataLabel = new JLabel("Other data");

    /**
     * Text field for other data
     */
    private JTextField otherDataData = new JTextField();

    /**
     * Label for note
     */
    private JLabel noteLabel = new JLabel("Note");

    /**
     * Text field for note
     */
    private JTextField noteData = new JTextField();

    /**
     * Creates a new instance of ProgramInfoDialog.
     */
    public ProgramInfoDialog() {
        init();
    }

    /**
     * Creates a new instance of ProgramInfoDialog.
     *
     * @param program TO for program
     * @throws IllegalArgumentException if TO for program is null
     */
    public ProgramInfoDialog(final ProgramTO program) {
        super(program);

        init();
        this.nameData.setText(program.getName());
        this.wikiCzData.setText(program.getWikiCz());
        this.wikiEnData.setText(program.getWikiEn());
        this.mediaCountData.setValue(program.getMediaCount());
        this.crackData.setSelected(program.hasCrack());
        this.serialData.setSelected(program.hasSerialKey());
        this.otherDataData.setText(program.getOtherData());
        this.noteData.setText(program.getNote());
    }

    @Override
    protected void initComponents() {
        initLabelComponent(nameLabel, nameData);
        initLabelComponent(wikiCzLabel, wikiCzData);
        initLabelComponent(wikiEnLabel, wikiEnData);
        initLabelComponent(mediaCountLabel, mediaCountData);
        initLabelComponent(otherDataLabel, otherDataData);
        initLabelComponent(noteLabel, noteData);

        addInputValidator(nameData);
    }

    @Override
    protected ProgramTO processData(final ProgramTO objectData) {
        final ProgramTO program = objectData == null ? new ProgramTO() : objectData;
        program.setName(nameData.getText());
        program.setWikiCz(wikiCzData.getText());
        program.setWikiEn(wikiEnData.getText());
        program.setMediaCount((Integer) mediaCountData.getValue());
        program.setCrack(crackData.isSelected());
        program.setSerialKey(serialData.isSelected());
        program.setOtherData(otherDataData.getText());
        program.setNote(noteData.getText());

        return program;
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
                .addGroup(createHorizontalComponents(layout, wikiCzLabel, wikiCzData))
                .addGroup(createHorizontalComponents(layout, wikiEnLabel, wikiEnData))
                .addGroup(createHorizontalComponents(layout, mediaCountLabel, mediaCountData))
                .addComponent(crackData, HORIZONTAL_CHECK_BOX_SIZE, HORIZONTAL_CHECK_BOX_SIZE, HORIZONTAL_CHECK_BOX_SIZE)
                .addComponent(serialData, HORIZONTAL_CHECK_BOX_SIZE, HORIZONTAL_CHECK_BOX_SIZE, HORIZONTAL_CHECK_BOX_SIZE)
                .addGroup(createHorizontalComponents(layout, otherDataLabel, otherDataData))
                .addGroup(createHorizontalComponents(layout, noteLabel, noteData));
    }

    @Override
    protected GroupLayout.Group getVerticalLayoutWithComponents(final GroupLayout layout, final GroupLayout.Group group) {
        return group
                .addGroup(createVerticalComponents(layout, nameLabel, nameData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, wikiCzLabel, wikiCzData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, wikiEnLabel, wikiEnData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, mediaCountLabel, mediaCountData))
                .addGap(VERTICAL_GAP_SIZE)
                .addComponent(crackData, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
                .addGap(VERTICAL_GAP_SIZE)
                .addComponent(serialData, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, otherDataLabel, otherDataData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, noteLabel, noteData));
    }

}
