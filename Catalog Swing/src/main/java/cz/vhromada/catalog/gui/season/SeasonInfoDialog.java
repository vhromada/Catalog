package cz.vhromada.catalog.gui.season;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.GroupLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import cz.vhromada.catalog.commons.Constants;
import cz.vhromada.catalog.commons.Language;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.catalog.gui.commons.AbstractInfoDialog;
import cz.vhromada.catalog.gui.commons.CatalogSwingConstants;

/**
 * A class represents dialog for season.
 *
 * @author Vladimir Hromada
 */
public class SeasonInfoDialog extends AbstractInfoDialog<SeasonTO> {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Label for season's number
     */
    private JLabel numberLabel = new JLabel("Number of season");

    /**
     * Spinner for season's number
     */
    private JSpinner numberData = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));

    /**
     * Label for starting year
     */
    private JLabel startYearLabel = new JLabel("Starting year");

    /**
     * Spinner for starting year
     */
    private JSpinner startYearData = new JSpinner(new SpinnerNumberModel(Constants.CURRENT_YEAR, Constants.MIN_YEAR, Constants.CURRENT_YEAR, 1));

    /**
     * Label for ending year
     */
    private JLabel endYearLabel = new JLabel("Ending year");

    /**
     * Spinner for ending year
     */
    private JSpinner endYearData = new JSpinner(new SpinnerNumberModel(Constants.CURRENT_YEAR, Constants.MIN_YEAR, Constants.CURRENT_YEAR, 1));

    /**
     * Label for language
     */
    private JLabel languageLabel = new JLabel("Language");

    /**
     * Button group for languages
     */
    private ButtonGroup languagesButtonGroup = new ButtonGroup();

    /**
     * Radio button for czech language
     */
    private JRadioButton czechLanguageData = new JRadioButton("Czech", true);

    /**
     * Radio button for english language
     */
    private JRadioButton englishLanguageData = new JRadioButton("English");

    /**
     * Radio button for french language
     */
    private JRadioButton frenchLanguageData = new JRadioButton("French");

    /**
     * Radio button for japanese language
     */
    private JRadioButton japaneseLanguageData = new JRadioButton("Japanese");

    /**
     * Label for subtitles
     */
    private JLabel subtitlesLabel = new JLabel("Subtitles");

    /**
     * Check box for czech subtitles
     */
    private JCheckBox czechSubtitlesData = new JCheckBox("Czech");

    /**
     * Check box for english subtitles
     */
    private JCheckBox englishSubtitlesData = new JCheckBox("English");

    /**
     * Label for note
     */
    private JLabel noteLabel = new JLabel("Note");

    /**
     * Text field for note
     */
    private JTextField noteData = new JTextField();

    /**
     * Creates a new instance of SeasonInfoDialog.
     */
    public SeasonInfoDialog() {
        init();

        setOkButtonEnabled(true);
    }

    /**
     * Creates a new instance of SeasonInfoDialog.
     *
     * @param season TO for season
     * @throws IllegalArgumentException if TO for season is null
     */
    public SeasonInfoDialog(final SeasonTO season) {
        super(season);

        init();
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
        for (final Language subtitles : season.getSubtitles()) {
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
        this.noteData.setText(season.getNote());
    }

    @Override
    protected void initComponents() {
        initLabelComponent(numberLabel, numberData);
        initLabelComponent(startYearLabel, startYearData);
        initLabelComponent(endYearLabel, endYearData);
        initLabelComponent(noteLabel, noteData);

        languagesButtonGroup.add(czechLanguageData);
        languagesButtonGroup.add(englishLanguageData);
        languagesButtonGroup.add(frenchLanguageData);
        languagesButtonGroup.add(japaneseLanguageData);

        subtitlesLabel.setFocusable(false);

        addSpinnerValidator(startYearData);
        addSpinnerValidator(endYearData);
    }

    @Override
    protected SeasonTO processData(final SeasonTO objectData) {
        final SeasonTO season = objectData == null ? new SeasonTO() : objectData;
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

        return season;
    }

    /**
     * Returns true if input is valid: starting year isn't greater than ending year.
     *
     * @return true if input is valid: starting year isn't greater than ending year
     */
    @Override
    protected boolean isInputValid() {
        return (Integer) startYearData.getValue() <= (Integer) endYearData.getValue();
    }

    @Override
    protected GroupLayout.Group getHorizontalLayoutWithComponents(final GroupLayout layout, final GroupLayout.Group group) {
        return group
                .addGroup(createHorizontalComponents(layout, numberLabel, numberData))
                .addGroup(createHorizontalComponents(layout, startYearLabel, startYearData))
                .addGroup(createHorizontalComponents(layout, endYearLabel, endYearData))
                .addGroup(createHorizontalComponents(layout, languageLabel, czechLanguageData))
                .addGroup(createHorizontalSelectableComponent(layout, englishLanguageData))
                .addGroup(createHorizontalSelectableComponent(layout, frenchLanguageData))
                .addGroup(createHorizontalSelectableComponent(layout, japaneseLanguageData))
                .addGroup(createHorizontalComponents(layout, subtitlesLabel, czechSubtitlesData))
                .addGroup(createHorizontalSelectableComponent(layout, englishSubtitlesData))
                .addGroup(createHorizontalComponents(layout, noteLabel, noteData));
    }

    @Override
    protected GroupLayout.Group getVerticalLayoutWithComponents(final GroupLayout layout, final GroupLayout.Group group) {
        return group
                .addGroup(createVerticalComponents(layout, numberLabel, numberData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, startYearLabel, startYearData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, endYearLabel, endYearData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, languageLabel, czechLanguageData))
                .addGap(VERTICAL_GAP_SIZE)
                .addComponent(englishLanguageData, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
                .addGap(VERTICAL_GAP_SIZE)
                .addComponent(frenchLanguageData, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
                .addGap(VERTICAL_GAP_SIZE)
                .addComponent(japaneseLanguageData, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, subtitlesLabel, czechSubtitlesData))
                .addGap(VERTICAL_GAP_SIZE)
                .addComponent(englishSubtitlesData, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, noteLabel, noteData));
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

}
