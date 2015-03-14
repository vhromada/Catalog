package cz.vhromada.catalog.gui.season;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cz.vhromada.catalog.commons.CatalogSwingConstants;
import cz.vhromada.catalog.commons.Constants;
import cz.vhromada.catalog.commons.Language;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.catalog.gui.DialogResult;
import cz.vhromada.catalog.gui.Picture;
import cz.vhromada.validators.Validators;

/**
 * A class represents dialog for season.
 *
 * @author Vladimir Hromada
 */
public class SeasonInfoDialog extends JDialog {

    /** SerialVersionUID */
    private static final long serialVersionUID = 1L;

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

    /** TO for season */
    private SeasonTO season;

    /** Label for season's number */
    private JLabel numberLabel = new JLabel("Number of season");

    /** Spinner for season's number */
    private JSpinner numberData = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));

    /** Label for starting year */
    private JLabel startYearLabel = new JLabel("Starting year");

    /** Spinner for starting year */
    private JSpinner startYearData = new JSpinner(new SpinnerNumberModel(Constants.CURRENT_YEAR, Constants.MIN_YEAR, Constants.CURRENT_YEAR, 1));

    /** Label for ending year */
    private JLabel endYearLabel = new JLabel("Ending year");

    /** Spinner for ending year */
    private JSpinner endYearData = new JSpinner(new SpinnerNumberModel(Constants.CURRENT_YEAR, Constants.MIN_YEAR, Constants.CURRENT_YEAR, 1));

    /** Label for language */
    private JLabel languageLabel = new JLabel("Language");

    /** Button group for languages */
    private ButtonGroup languagesButtonGroup = new ButtonGroup();

    /** Radio button for czech language */
    private JRadioButton czechLanguageData = new JRadioButton("Czech", true);

    /** Radio button for english language */
    private JRadioButton englishLanguageData = new JRadioButton("English");

    /** Radio button for french language */
    private JRadioButton frenchLanguageData = new JRadioButton("French");

    /** Radio button for japanese language */
    private JRadioButton japaneseLanguageData = new JRadioButton("Japanese");

    /** Label for subtitles */
    private JLabel subtitlesLabel = new JLabel("Subtitles");

    /** Check box for czech subtitles */
    private JCheckBox czechSubtitlesData = new JCheckBox("Czech");

    /** Check box for english subtitles */
    private JCheckBox englishSubtitlesData = new JCheckBox("English");

    /** Label for note */
    private JLabel noteLabel = new JLabel("Note");

    /** Text field for note */
    private JTextField noteData = new JTextField();

    /** Button OK */
    private JButton okButton = new JButton("OK", Picture.OK.getIcon());

    /** Button Cancel */
    private JButton cancelButton = new JButton("Cancel", Picture.CANCEL.getIcon());

    /** Creates a new instance of SeasonInfoDialog. */
    public SeasonInfoDialog() {
        this("Add", Picture.ADD);

        numberData.requestFocusInWindow();
    }

    /**
     * Creates a new instance of SeasonInfoDialog.
     *
     * @param season TO for season
     * @throws IllegalArgumentException if TO for season is null
     */
    public SeasonInfoDialog(final SeasonTO season) {
        this("Update", Picture.UPDATE);

        Validators.validateArgumentNotNull(season, "TO for season");

        this.season = season;
        this.numberData.setValue(season.getNumber());
        this.startYearData.setValue(season.getStartYear());
        this.endYearData.setValue(season.getEndYear());
        switch (season.getLanguage()) {
            case CZ:
                this.czechLanguageData.setSelected(true);
                break;
            case EN:
                this.englishLanguageData.setSelected(true);
                break;
            case FR:
                this.frenchLanguageData.setSelected(true);
                break;
            case JP:
                this.japaneseLanguageData.setSelected(true);
                break;
            default:
                throw new IndexOutOfBoundsException("Bad language");
        }
        for (Language subtitles : season.getSubtitles()) {
            initSubtitles(subtitles);
        }
        this.noteData.setText(season.getNote());
        this.okButton.requestFocusInWindow();
    }

    /**
     * Creates a new instance of SeasonInfoDialog.
     *
     * @param name    name
     * @param picture picture
     */
    private SeasonInfoDialog(final String name, final Picture picture) {
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
     * Returns TO for season.
     *
     * @return TO for season
     * @throws IllegalStateException if TO for season hasn't been set
     */
    public SeasonTO getSeason() {
        Validators.validateFieldNotNull(season, "TO for season");

        return season;
    }

    /**
     * Returns horizontal layout for selectable components.
     *
     * @param layout     layout
     * @param components components
     * @return horizontal layout for selectable components
     */
    public static GroupLayout.Group createHorizontalSelectableComponents(final GroupLayout layout, final JComponent... components) {
        final GroupLayout.Group result = layout.createParallelGroup();
        for (JComponent component : components) {
            final GroupLayout.Group group = layout.createSequentialGroup()
                    .addGap(110)
                    .addComponent(component, HORIZONTAL_DATA_DIALOG_SIZE, HORIZONTAL_DATA_DIALOG_SIZE, HORIZONTAL_DATA_DIALOG_SIZE);
            result.addGroup(group);
        }
        return result;
    }

    /**
     * Returns vertical layout for selectable components.
     *
     * @param layout     layout
     * @param components components
     * @return vertical layout for selectable components
     */
    public static GroupLayout.Group createVerticalSelectableComponents(final GroupLayout layout, final JComponent... components) {
        final GroupLayout.Group result = layout.createSequentialGroup();
        for (JComponent component : components) {
            result.addComponent(component, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                    CatalogSwingConstants.VERTICAL_COMPONENT_SIZE);
            if (!component.equals(components[components.length - 1])) {
                result.addGap(VERTICAL_GAP_SIZE);
            }
        }
        return result;
    }

    /**
     * Initializes subtitles.
     *
     * @param subtitles subtitles
     */
    private void initSubtitles(final Language subtitles) {
        if (subtitles != null) {
            switch (subtitles) {
                case CZ:
                    this.czechSubtitlesData.setSelected(true);
                    break;
                case EN:
                    this.englishSubtitlesData.setSelected(true);
                    break;
                default:
                    throw new IndexOutOfBoundsException("Bad subtitles");
            }
        }
    }

    /** Initializes components. */
    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        initLabelComponent(numberLabel, numberData);
        initLabelSpinnerWithListener(startYearLabel, startYearData);
        initLabelSpinnerWithListener(endYearLabel, endYearData);
        initButtonGroup(czechLanguageData, englishLanguageData, frenchLanguageData, japaneseLanguageData);
        subtitlesLabel.setFocusable(false);
        initLabelComponent(noteLabel, noteData);

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

    /**
     * Initializes label with spinner and listener.
     *
     * @param label   label
     * @param spinner component
     */
    private void initLabelSpinnerWithListener(final JLabel label, final JSpinner spinner) {
        label.setLabelFor(spinner);
        label.setFocusable(false);
        spinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(final ChangeEvent e) {
                okButton.setEnabled(isInputValid());
            }

        });
    }

    /**
     * Initializes button group.
     *
     * @param buttons buttons in button group
     */
    private void initButtonGroup(final JRadioButton... buttons) {
        for (JRadioButton button : buttons) {
            languagesButtonGroup.add(button);
        }
    }

    /** Performs action for button OK. */
    private void okAction() {
        returnStatus = DialogResult.OK;
        if (season == null) {
            season = new SeasonTO();
        }
        season.setNumber((Integer) numberData.getValue());
        season.setStartYear((Integer) startYearData.getValue());
        season.setEndYear((Integer) endYearData.getValue());
        final Language language;
        final ButtonModel model = languagesButtonGroup.getSelection();
        if (model.equals(czechLanguageData.getModel())) {
            language = Language.CZ;
        } else if (model.equals(englishLanguageData.getModel())) {
            language = Language.EN;
        } else {
            language = model.equals(frenchLanguageData.getModel()) ? Language.FR : Language.JP;
        }
        season.setLanguage(language);
        season.setSubtitles(getSelectedSubtitles());
        season.setNote(noteData.getText());
        close();
    }

    /**
     * Returns selected subtitles.
     *
     * @return selected subtitles
     */
    private List<Language> getSelectedSubtitles() {
        final List<Language> subtitles = new ArrayList<>();
        if (czechSubtitlesData.isSelected()) {
            subtitles.add(Language.CZ);
        }
        if (englishSubtitlesData.isSelected()) {
            subtitles.add(Language.EN);
        }

        return subtitles;
    }

    /** Performs action for button Cancel. */
    private void cancelAction() {
        returnStatus = DialogResult.CANCEL;
        season = null;
        close();
    }

    /**
     * Returns horizontal layout of components.
     *
     * @param layout layout
     * @return horizontal layout of components
     */
    private GroupLayout.Group createHorizontalLayout(final GroupLayout layout) {
        final GroupLayout.Group buttons = layout.createSequentialGroup()
                .addGap(HORIZONTAL_BUTTON_GAP_SIZE)
                .addComponent(okButton, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE)
                .addGap(54)
                .addComponent(cancelButton, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE)
                .addGap(HORIZONTAL_BUTTON_GAP_SIZE);


        final GroupLayout.Group components = layout.createParallelGroup()
                .addGroup(createHorizontalComponents(layout, numberLabel, numberData))
                .addGroup(createHorizontalComponents(layout, startYearLabel, startYearData))
                .addGroup(createHorizontalComponents(layout, endYearLabel, endYearData))
                .addGroup(createHorizontalComponents(layout, languageLabel, czechLanguageData))
                .addGroup(createHorizontalSelectableComponents(layout, englishLanguageData, frenchLanguageData, japaneseLanguageData))
                .addGroup(createHorizontalComponents(layout, subtitlesLabel, czechSubtitlesData))
                .addGroup(createHorizontalSelectableComponents(layout, englishSubtitlesData))
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
        final GroupLayout.Group buttons = layout.createParallelGroup()
                .addComponent(okButton, CatalogSwingConstants.VERTICAL_BUTTON_SIZE, CatalogSwingConstants.VERTICAL_BUTTON_SIZE,
                        CatalogSwingConstants.VERTICAL_BUTTON_SIZE)
                .addComponent(cancelButton, CatalogSwingConstants.VERTICAL_BUTTON_SIZE, CatalogSwingConstants.VERTICAL_BUTTON_SIZE,
                        CatalogSwingConstants.VERTICAL_BUTTON_SIZE);

        return layout.createSequentialGroup()
                .addGap(VERTICAL_LONG_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, numberLabel, numberData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, startYearLabel, startYearData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, endYearLabel, endYearData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, languageLabel, czechLanguageData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalSelectableComponents(layout, englishLanguageData, frenchLanguageData, japaneseLanguageData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, subtitlesLabel, czechSubtitlesData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalSelectableComponents(layout, englishSubtitlesData))
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
     * Returns true if input is valid: starting year isn't greater than ending year.
     *
     * @return true if input is valid: starting year isn't greater than ending year
     */
    private boolean isInputValid() {
        return (Integer) startYearData.getValue() <= (Integer) endYearData.getValue();
    }

    /** Closes dialog. */
    private void close() {
        setVisible(false);
        dispose();
    }

}
