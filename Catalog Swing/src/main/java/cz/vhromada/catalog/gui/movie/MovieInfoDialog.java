package cz.vhromada.catalog.gui.movie;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentListener;

import cz.vhromada.catalog.commons.Constants;
import cz.vhromada.catalog.commons.Language;
import cz.vhromada.catalog.commons.SwingUtils;
import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.facade.to.MovieTO;
import cz.vhromada.catalog.gui.DialogResult;
import cz.vhromada.catalog.gui.InputValidator;
import cz.vhromada.catalog.gui.Picture;
import cz.vhromada.validators.Validators;

/**
 * A class represents dialog for movie.
 *
 * @author Vladimir Hromada
 */
public class MovieInfoDialog extends JDialog {

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

    /** TO for movie */
    private MovieTO movie;

    /** Label for czech name */
    private JLabel czechNameLabel = new JLabel("Czech name");

    /** Text field for czech name */
    private JTextField czechNameData = new JTextField();

    /** Label for original name */
    private JLabel originalNameLabel = new JLabel("Original name");

    /** Text field for original name */
    private JTextField originalNameData = new JTextField();

    /** Label for genre */
    private JLabel genreLabel = new JLabel("Genre");

    /** Text field for genre */
    private JTextField genreData = new JTextField();

    /** Label for year */
    private JLabel yearLabel = new JLabel("Year");

    /** Spinner for year */
    private JSpinner yearData = new JSpinner(new SpinnerNumberModel(Constants.CURRENT_YEAR, Constants.MIN_YEAR, Constants.CURRENT_YEAR, 1));

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

    /** Label for 1st medium */
    private JLabel medium1Label = new JLabel("Medium 1");

    /** Spinner for length of 1st medium - hours */
    private JSpinner medium1HoursData = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));

    /** Spinner for length of 1st medium - minutes */
    private JSpinner medium1MinutesData = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));

    /** Spinner for length of 1st medium - seconds */
    private JSpinner medium1SecondsData = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));

    /** Check box for 2nd medium */
    private JCheckBox medium2Label = new JCheckBox("Medium 2");

    /** Spinner for length of 2nd medium - hours */
    private JSpinner medium2HoursData = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));

    /** Spinner for length of 2nd medium - minutes */
    private JSpinner medium2MinutesData = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));

    /** Spinner for length of 2nd medium - seconds */
    private JSpinner medium2SecondsData = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));

    /** Label for ČSFD */
    private JLabel csfdLabel = new JLabel("\u010cSFD");

    /** Text field for ČSFD */
    private JTextField csfdData = new JTextField();

    /** Check box for IMDB code */
    private JCheckBox imdbCodeLabel = new JCheckBox("IMDB code");

    /** Spinner for IMDB code */
    private JSpinner imdbCodeData = new JSpinner(new SpinnerNumberModel(1, 1, Constants.MAX_IMDB_CODE, 1));

    /** Label for czech Wikipedia */
    private JLabel wikiCzLabel = new JLabel("Czech Wikipedia");

    /** Text field for czech Wikipedia */
    private JTextField wikiCzData = new JTextField();

    /** Label for english Wikipedia */
    private JLabel wikiEnLabel = new JLabel("English Wikipedia");

    /** Text field for english Wikipedia */
    private JTextField wikiEnData = new JTextField();

    /** Label for picture */
    private JLabel pictureLabel = new JLabel("Picture");

    /** Text field for picture */
    private JTextField pictureData = new JTextField();

    /** Label for note */
    private JLabel noteLabel = new JLabel("Note");

    /** Text field for note */
    private JTextField noteData = new JTextField();

    /** Button OK */
    private JButton okButton = new JButton("OK", Picture.OK.getIcon());

    /** Button Cancel */
    private JButton cancelButton = new JButton("Cancel", Picture.CANCEL.getIcon());

    /** Creates a new instance of MovieInfoDialog. */
    public MovieInfoDialog() {
        this("Add", Picture.ADD);

        czechNameData.requestFocusInWindow();
    }

    /**
     * Creates a new instance of MovieInfoDialog.
     *
     * @param movie TO for movie
     * @throws IllegalArgumentException if TO for movie is null
     */
    public MovieInfoDialog(final MovieTO movie) {
        this("Update", Picture.UPDATE);

        Validators.validateArgumentNotNull(movie, "TO for movie");

        this.movie = movie;
        this.czechNameData.setText(movie.getCzechName());
        this.originalNameData.setText(movie.getOriginalName());
        this.genreData.setText(movie.getGenres().toString());
        this.yearData.setValue(movie.getYear());
        switch (movie.getLanguage()) {
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
        //TODO vhromada 08.03.2015: subtitles
        initSubtitles(null);
        initTime(0, this.medium1HoursData, this.medium1MinutesData, this.medium1SecondsData);
        if (movie.getMedia() != null) {
            this.medium2Label.setSelected(true);
            initTime(0, this.medium2HoursData, this.medium2MinutesData, this.medium2SecondsData);
        }
        this.csfdData.setText(movie.getCsfd());
        final int imdbCode = movie.getImdbCode();
        if (imdbCode > 0) {
            this.imdbCodeLabel.setSelected(true);
            this.imdbCodeData.setValue(movie.getImdbCode());
        } else {
            this.imdbCodeLabel.setSelected(false);
        }
        this.wikiCzData.setText(movie.getWikiCz());
        this.wikiEnData.setText(movie.getWikiEn());
        this.pictureData.setText(movie.getPicture());
        this.noteData.setText(movie.getNote());
        this.okButton.requestFocusInWindow();
    }

    /**
     * Creates a new instance of MovieInfoDialog.
     *
     * @param name    name
     * @param picture picture
     */
    private MovieInfoDialog(final String name, final Picture picture) {
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
     * Returns TO for movie.
     *
     * @return TO for movie
     * @throws IllegalStateException if TO for movie hasn't been set
     */
    public MovieTO getMovie() {
        Validators.validateFieldNotNull(movie, "TO for movie");

        return movie;
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

    /**
     * Initializes time.
     *
     * @param length  length in seconds
     * @param hours   hours
     * @param minutes minutes
     * @param seconds seconds
     */
    private void initTime(final int length, final JSpinner hours, final JSpinner minutes, final JSpinner seconds) {
        final Time time = new Time(length);
        hours.setValue(time.getData(Time.TimeData.HOUR));
        minutes.setValue(time.getData(Time.TimeData.MINUTE));
        seconds.setValue(time.getData(Time.TimeData.SECOND));
    }

    /** Initializes components. */
    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        final DocumentListener inputValidator = new InputValidator(okButton) {

            @Override
            public boolean isInputValid() {
                return MovieInfoDialog.this.isInputValid();
            }

        };
        initLabelTextFieldWithValidator(czechNameLabel, czechNameData, inputValidator);
        initLabelTextFieldWithValidator(originalNameLabel, originalNameData, inputValidator);
        initLabelTextFieldWithValidator(genreLabel, genreData, inputValidator);
        initLabelComponent(yearLabel, yearData);
        initButtonGroup(czechLanguageData, englishLanguageData, frenchLanguageData, japaneseLanguageData);
        initLabelComponent(csfdLabel, csfdData);
        initLabelComponent(wikiCzLabel, wikiCzData);
        initLabelComponent(wikiEnLabel, wikiEnData);
        initLabelComponent(pictureLabel, pictureData);
        initLabelComponent(noteLabel, noteData);
        initComponentsFocusable(languageLabel, subtitlesLabel, medium1Label);
        initComponentsEnabled(medium2HoursData, medium2MinutesData, medium2SecondsData, imdbCodeData, okButton);

        medium2Label.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(final ChangeEvent e) {
                medium2StateValueChangedAction();
            }

        });
        imdbCodeLabel.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(final ChangeEvent e) {
                imdbCodeData.setEnabled(imdbCodeLabel.isSelected());
            }

        });

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
     * Initializes label with text field and input validator.
     *
     * @param label          label
     * @param textField      text field
     * @param inputValidator input validator
     */
    private void initLabelTextFieldWithValidator(final JLabel label, final JTextField textField, final DocumentListener inputValidator) {
        initLabelComponent(label, textField);
        textField.getDocument().addDocumentListener(inputValidator);
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
     * Initializes components.
     *
     * @param components components
     */
    private void initComponentsFocusable(final JComponent... components) {
        for (JComponent component : components) {
            component.setFocusable(false);
        }
    }

    /**
     * Initializes components.
     *
     * @param components component components
     */
    private void initComponentsEnabled(final JComponent... components) {
        for (JComponent component : components) {
            component.setEnabled(false);
        }
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

    /** Performs action for change of check box for 2nd medium state. */
    private void medium2StateValueChangedAction() {
        final boolean selected = medium2Label.isSelected();
        medium2HoursData.setEnabled(selected);
        medium2MinutesData.setEnabled(selected);
        medium2SecondsData.setEnabled(selected);
        okButton.setEnabled(isInputValid());
    }

    /** Performs action for button OK. */
    private void okAction() {
        returnStatus = DialogResult.OK;
        if (movie == null) {
            movie = new MovieTO();
        }
        movie.setCzechName(czechNameData.getText());
        movie.setOriginalName(originalNameData.getText());
        //TODO vhromada 08.03.2015: genres
        movie.setGenres(null);
        movie.setYear((Integer) yearData.getValue());
        final Language language;
        final ButtonModel model = languagesButtonGroup.getSelection();
        if (model.equals(czechLanguageData.getModel())) {
            language = Language.CZ;
        } else if (model.equals(englishLanguageData.getModel())) {
            language = Language.EN;
        } else {
            language = model.equals(frenchLanguageData.getModel()) ? Language.FR : Language.JP;
        }
        movie.setLanguage(language);
        movie.setSubtitles(getSelectedSubtitles());
        //TODO vhromada 08.03.2015: media
        processMedia();
        movie.setCsfd(csfdData.getText());
        movie.setImdbCode(imdbCodeLabel.isSelected() ? (Integer) imdbCodeData.getValue() : -1);
        movie.setWikiCz(wikiCzData.getText());
        movie.setWikiEn(wikiEnData.getText());
        movie.setPicture(pictureData.getText());
        movie.setNote(noteData.getText());
        close();
    }

    /**
     * Returns selected subtitles.
     *
     * @return selected subtitles
     */
    private List<Language> getSelectedSubtitles() {
        final List<Language> subtitles = new ArrayList<>(2);
        if (czechSubtitlesData.isSelected()) {
            subtitles.add(Language.CZ);
        }
        if (englishSubtitlesData.isSelected()) {
            subtitles.add(Language.EN);
        }

        return subtitles;
    }

    /** Processes media. */
    private void processMedia() {
    }

    /** Performs action for button Cancel. */
    private void cancelAction() {
        returnStatus = DialogResult.CANCEL;
        movie = null;
        close();
    }

    //TODO vhromada 08.03.2015: layout

    /**
     * Returns horizontal layout of components.
     *
     * @param layout layout
     * @return horizontal layout of components
     */
    private GroupLayout.Group createHorizontalLayout(final GroupLayout layout) {
        return SwingUtils.createHorizontalDialogLayout(layout,
                SwingUtils.createHorizontalComponents(layout, czechNameLabel, czechNameData),
                SwingUtils.createHorizontalComponents(layout, originalNameLabel, originalNameData),
                SwingUtils.createHorizontalComponents(layout, genreLabel, genreData),
                SwingUtils.createHorizontalComponents(layout, yearLabel, yearData),
                SwingUtils.createHorizontalComponents(layout, languageLabel, czechLanguageData),
                SwingUtils.createHorizontalSelectableComponents(layout, englishLanguageData, frenchLanguageData, japaneseLanguageData),
                SwingUtils.createHorizontalComponents(layout, subtitlesLabel, czechSubtitlesData),
                SwingUtils.createHorizontalSelectableComponents(layout, englishSubtitlesData),
                SwingUtils.createHorizontalLengthComponents(layout, medium1Label, medium1HoursData, medium1MinutesData, medium1SecondsData),
                SwingUtils.createHorizontalLengthComponents(layout, medium2Label, medium2HoursData, medium2MinutesData, medium2SecondsData),
                SwingUtils.createHorizontalComponents(layout, csfdLabel, csfdData),
                SwingUtils.createHorizontalComponents(layout, imdbCodeLabel, imdbCodeData),
                SwingUtils.createHorizontalComponents(layout, wikiCzLabel, wikiCzData),
                SwingUtils.createHorizontalComponents(layout, wikiEnLabel, wikiEnData),
                SwingUtils.createHorizontalComponents(layout, pictureLabel, pictureData),
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
                SwingUtils.createVerticalComponents(layout, czechNameLabel, czechNameData),
                SwingUtils.createVerticalComponents(layout, originalNameLabel, originalNameData),
                SwingUtils.createVerticalComponents(layout, genreLabel, genreData),
                SwingUtils.createVerticalComponents(layout, yearLabel, yearData),
                SwingUtils.createVerticalComponents(layout, languageLabel, czechLanguageData),
                SwingUtils.createVerticalSelectableComponents(layout, englishLanguageData, frenchLanguageData, japaneseLanguageData),
                SwingUtils.createVerticalComponents(layout, subtitlesLabel, czechSubtitlesData),
                SwingUtils.createVerticalSelectableComponents(layout, englishSubtitlesData),
                SwingUtils.createVerticalLengthComponents(layout, medium1Label, medium1HoursData, medium1MinutesData, medium1SecondsData),
                SwingUtils.createVerticalLengthComponents(layout, medium2Label, medium2HoursData, medium2MinutesData, medium2SecondsData),
                SwingUtils.createVerticalComponents(layout, csfdLabel, csfdData),
                SwingUtils.createVerticalComponents(layout, imdbCodeLabel, imdbCodeData),
                SwingUtils.createVerticalComponents(layout, wikiCzLabel, wikiCzData),
                SwingUtils.createVerticalComponents(layout, wikiEnLabel, wikiEnData),
                SwingUtils.createVerticalComponents(layout, pictureLabel, pictureData),
                SwingUtils.createVerticalComponents(layout, noteLabel, noteData));
    }

    /**
     * Returns true if input is valid: czech name isn't empty string, original name isn't empty string, genre isn't empty string.
     *
     * @return true if input is valid: czech name isn't empty string, original name isn't empty string, genre isn't empty string
     */
    private boolean isInputValid() {
        return !czechNameData.getText().isEmpty() && !originalNameData.getText().isEmpty() && !genreData.getText().isEmpty();
    }

    /** Closes dialog. */
    private void close() {
        setVisible(false);
        dispose();
    }

}
