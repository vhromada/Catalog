package cz.vhromada.catalog.gui.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import cz.vhromada.catalog.commons.CatalogSwingConstants;
import cz.vhromada.catalog.facade.to.GameTO;
import cz.vhromada.catalog.gui.DialogResult;
import cz.vhromada.catalog.gui.InputValidator;
import cz.vhromada.catalog.gui.Picture;
import cz.vhromada.validators.Validators;

/**
 * A class represents dialog for game.
 *
 * @author Vladimir Hromada
 */
public class GameInfoDialog extends JDialog {

    /** SerialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Horizontal component size */
    private static final int HORIZONTAL_COMPONENT_SIZE = 310;

    /** Horizontal label size in dialog */
    private static final int HORIZONTAL_LABEL_DIALOG_SIZE = 100;

    /** Horizontal data size in dialog */
    private static final int HORIZONTAL_DATA_DIALOG_SIZE = 200;

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

    /** Return status */
    private DialogResult returnStatus = DialogResult.CANCEL;

    /** TO for game */
    private GameTO game;

    /** Label for name */
    private JLabel nameLabel = new JLabel("Name");

    /** Text field for name */
    private JTextField nameData = new JTextField();

    /** Label for czech Wikipedia */
    private JLabel wikiCzLabel = new JLabel("Czech Wikipedia");

    /** Text field for czech Wikipedia */
    private JTextField wikiCzData = new JTextField();

    /** Label for english Wikipedia */
    private JLabel wikiEnLabel = new JLabel("English Wikipedia");

    /** Text field for english Wikipedia */
    private JTextField wikiEnData = new JTextField();

    /** Label for count of media */
    private JLabel mediaCountLabel = new JLabel("Count of media");

    /** Spinner for count of media */
    private JSpinner mediaCountData = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));

    /** Check box for crack */
    private JCheckBox crackData = new JCheckBox("Crack");

    /** Check box for serial key */
    private JCheckBox serialData = new JCheckBox("Serial key");

    /** Check box for patch */
    private JCheckBox patchData = new JCheckBox("Patch");

    /** Check box for trainer */
    private JCheckBox trainerData = new JCheckBox("Trainer");

    /** Check box for data for trainer */
    private JCheckBox trainerDataData = new JCheckBox("Data for trainer");

    /** Check box for editor */
    private JCheckBox editorData = new JCheckBox("Editor");

    /** Check box for saves */
    private JCheckBox savesData = new JCheckBox("Saves");

    /** Label for other data */
    private JLabel otherDataLabel = new JLabel("Other data");

    /** Text field for other data */
    private JTextField otherDataData = new JTextField();

    /** Label for note */
    private JLabel noteLabel = new JLabel("Note");

    /** Text field for note */
    private JTextField noteData = new JTextField();

    /** Button OK */
    private JButton okButton = new JButton("OK", Picture.OK.getIcon());

    /** Button Cancel */
    private JButton cancelButton = new JButton("Cancel", Picture.CANCEL.getIcon());

    /** Creates a new instance of GameInfoDialog. */
    public GameInfoDialog() {
        this("Add", Picture.ADD);

        nameData.requestFocusInWindow();
    }

    /**
     * Creates a new instance of GameInfoDialog.
     *
     * @param game TO for game
     * @throws IllegalArgumentException if TO for game is null
     */
    public GameInfoDialog(final GameTO game) {
        this("Update", Picture.UPDATE);

        Validators.validateArgumentNotNull(game, "TO for game");

        this.game = game;
        this.nameData.setText(game.getName());
        this.wikiCzData.setText(game.getWikiCz());
        this.wikiEnData.setText(game.getWikiEn());
        this.mediaCountData.setValue(game.getMediaCount());
        this.crackData.setSelected(game.hasCrack());
        this.serialData.setSelected(game.hasSerialKey());
        this.patchData.setSelected(game.hasPatch());
        this.trainerData.setSelected(game.hasTrainer());
        this.trainerDataData.setSelected(game.hasTrainerData());
        this.editorData.setSelected(game.hasEditor());
        this.savesData.setSelected(game.haveSaves());
        this.otherDataData.setText(game.getOtherData());
        this.noteData.setText(game.getNote());
        this.okButton.requestFocusInWindow();
    }

    /**
     * Creates a new instance of GameInfoDialog.
     *
     * @param name    name
     * @param picture picture
     */
    private GameInfoDialog(final String name, final Picture picture) {
        super(new JFrame(), name, true);

        initComponents();
        setIconImage(picture.getIcon().getImage());
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
     * Returns TO for game.
     *
     * @return TO for ame
     * @throws IllegalStateException if TO for game hasn't been set
     */
    public GameTO getGame() {
        Validators.validateFieldNotNull(game, "TO for game");

        return game;
    }

    /** Initializes components. */
    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        initLabelComponent(nameLabel, nameData);
        initLabelComponent(wikiCzLabel, wikiCzData);
        initLabelComponent(wikiEnLabel, wikiEnData);
        initLabelComponent(mediaCountLabel, mediaCountData);
        initLabelComponent(otherDataLabel, otherDataData);
        initLabelComponent(noteLabel, noteData);

        nameData.getDocument().addDocumentListener(new InputValidator(okButton) {

            @Override
            public boolean isInputValid() {
                return GameInfoDialog.this.isInputValid();
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
    private static void initLabelComponent(final JLabel label, final JComponent component) {
        label.setLabelFor(component);
        label.setFocusable(false);
    }

    /** Performs action for button OK. */
    private void okAction() {
        returnStatus = DialogResult.OK;
        if (game == null) {
            game = new GameTO();
        }
        game.setName(nameData.getText());
        game.setWikiCz(wikiCzData.getText());
        game.setWikiEn(wikiEnData.getText());
        game.setMediaCount((Integer) mediaCountData.getValue());
        game.setCrack(crackData.isSelected());
        game.setSerialKey(serialData.isSelected());
        game.setPatch(patchData.isSelected());
        game.setTrainer(trainerData.isSelected());
        game.setTrainerData(trainerDataData.isSelected());
        game.setEditor(editorData.isSelected());
        game.setSaves(savesData.isSelected());
        game.setOtherData(otherDataData.getText());
        game.setNote(noteData.getText());
        close();
    }

    /** Performs action for button Cancel. */
    private void cancelAction() {
        returnStatus = DialogResult.CANCEL;
        game = null;
        close();
    }

    /**
     * Returns horizontal layout of components.
     *
     * @param layout layout
     * @return horizontal layout of components
     */
    private GroupLayout.Group createHorizontalLayout(final GroupLayout layout) {
        final GroupLayout.Group checkBoxes = layout.createParallelGroup()
                .addComponent(crackData, HORIZONTAL_COMPONENT_SIZE, HORIZONTAL_COMPONENT_SIZE, HORIZONTAL_COMPONENT_SIZE)
                .addComponent(serialData, HORIZONTAL_COMPONENT_SIZE, HORIZONTAL_COMPONENT_SIZE, HORIZONTAL_COMPONENT_SIZE)
                .addComponent(patchData, HORIZONTAL_COMPONENT_SIZE, HORIZONTAL_COMPONENT_SIZE, HORIZONTAL_COMPONENT_SIZE)
                .addComponent(trainerData, HORIZONTAL_COMPONENT_SIZE, HORIZONTAL_COMPONENT_SIZE, HORIZONTAL_COMPONENT_SIZE)
                .addComponent(trainerDataData, HORIZONTAL_COMPONENT_SIZE, HORIZONTAL_COMPONENT_SIZE, HORIZONTAL_COMPONENT_SIZE)
                .addComponent(editorData, HORIZONTAL_COMPONENT_SIZE, HORIZONTAL_COMPONENT_SIZE, HORIZONTAL_COMPONENT_SIZE)
                .addComponent(savesData, HORIZONTAL_COMPONENT_SIZE, HORIZONTAL_COMPONENT_SIZE, HORIZONTAL_COMPONENT_SIZE);

        final GroupLayout.Group buttons = layout.createSequentialGroup()
                .addGap(HORIZONTAL_BUTTON_GAP_SIZE)
                .addComponent(okButton, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE)
                .addGap(54)
                .addComponent(cancelButton, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE)
                .addGap(HORIZONTAL_BUTTON_GAP_SIZE);

        final GroupLayout.Group componentsGroup = layout.createParallelGroup()
                .addGroup(createHorizontalComponents(layout, nameLabel, nameData))
                .addGroup(createHorizontalComponents(layout, wikiCzLabel, wikiCzData))
                .addGroup(createHorizontalComponents(layout, wikiEnLabel, wikiEnData))
                .addGroup(createHorizontalComponents(layout, mediaCountLabel, mediaCountData))
                .addGroup(checkBoxes)
                .addGroup(createHorizontalComponents(layout, otherDataLabel, otherDataData))
                .addGroup(createHorizontalComponents(layout, noteLabel, noteData))
                .addGroup(buttons);

        return layout.createSequentialGroup()
                .addGap(HORIZONTAL_GAP_SIZE)
                .addGroup(componentsGroup)
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
    private GroupLayout.Group createHorizontalComponents(final GroupLayout layout, final JComponent label, final JComponent data) {
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
        final GroupLayout.Group checkBoxes = layout.createSequentialGroup()
                .addComponent(crackData, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
                .addGap(VERTICAL_GAP_SIZE)
                .addComponent(serialData, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
                .addGap(VERTICAL_GAP_SIZE)
                .addComponent(patchData, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
                .addGap(VERTICAL_GAP_SIZE)
                .addComponent(trainerData, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
                .addGap(VERTICAL_GAP_SIZE)
                .addComponent(trainerDataData, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
                .addGap(VERTICAL_GAP_SIZE)
                .addComponent(editorData, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
                .addGap(VERTICAL_GAP_SIZE)
                .addComponent(savesData, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE);

        final GroupLayout.Group buttons = layout.createParallelGroup()
                .addComponent(okButton, CatalogSwingConstants.VERTICAL_BUTTON_SIZE, CatalogSwingConstants.VERTICAL_BUTTON_SIZE,
                        CatalogSwingConstants.VERTICAL_BUTTON_SIZE)
                .addComponent(cancelButton, CatalogSwingConstants.VERTICAL_BUTTON_SIZE, CatalogSwingConstants.VERTICAL_BUTTON_SIZE,
                        CatalogSwingConstants.VERTICAL_BUTTON_SIZE);

        return layout.createSequentialGroup()
                .addGap(VERTICAL_LONG_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, nameLabel, nameData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, wikiCzLabel, wikiCzData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, wikiEnLabel, wikiEnData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, mediaCountLabel, mediaCountData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(checkBoxes)
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, otherDataLabel, otherDataData))
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
                .addComponent(label, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
                .addGap(VERTICAL_GAP_SIZE)
                .addComponent(data, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE);
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
