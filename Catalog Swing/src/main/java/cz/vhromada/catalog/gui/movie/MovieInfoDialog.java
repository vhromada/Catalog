package cz.vhromada.catalog.gui.movie;

import java.awt.*;
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
import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.facade.GenreFacade;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.to.MovieTO;
import cz.vhromada.catalog.gui.commons.CatalogSwingConstants;
import cz.vhromada.catalog.gui.commons.DialogResult;
import cz.vhromada.catalog.gui.commons.InputValidator;
import cz.vhromada.catalog.gui.commons.Picture;
import cz.vhromada.catalog.gui.genre.GenreChooseDialog;
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

    /** Horizontal time size */
    private static final int HORIZONTAL_TIME_SIZE = 60;

    /** Horizontal genres button size */
    private static final int HORIZONTAL_GENRES_BUTTON_SIZE = 310;

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

    /** Facade for genres */
    private GenreFacade genreFacade;

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
    private JSpinner medium1HoursData = new JSpinner(new SpinnerNumberModel(0, 0, MAX_HOURS, 1));

    /** Spinner for length of 1st medium - minutes */
    private JSpinner medium1MinutesData = new JSpinner(new SpinnerNumberModel(0, 0, MAX_MINUTES, 1));

    /** Spinner for length of 1st medium - seconds */
    private JSpinner medium1SecondsData = new JSpinner(new SpinnerNumberModel(0, 0, MAX_SECONDS, 1));

    /** Check box for 2nd medium */
    private JCheckBox medium2Label = new JCheckBox("Medium 2");

    /** Spinner for length of 2nd medium - hours */
    private JSpinner medium2HoursData = new JSpinner(new SpinnerNumberModel(0, 0, MAX_HOURS, 1));

    /** Spinner for length of 2nd medium - minutes */
    private JSpinner medium2MinutesData = new JSpinner(new SpinnerNumberModel(0, 0, MAX_MINUTES, 1));

    /** Spinner for length of 2nd medium - seconds */
    private JSpinner medium2SecondsData = new JSpinner(new SpinnerNumberModel(0, 0, MAX_SECONDS, 1));

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

    /** Button for genres */
    private JButton genresButton = new JButton("Genres", Picture.CHOOSE.getIcon());

    /** Button OK */
    private JButton okButton = new JButton("OK", Picture.OK.getIcon());

    /** Button Cancel */
    private JButton cancelButton = new JButton("Cancel", Picture.CANCEL.getIcon());

    /** List of TO for genre */
    private List<GenreTO> genres = new ArrayList<>();

    /**
     * Creates a new instance of MovieInfoDialog.
     *
     * @param genreFacade facade for genres
     * @throws IllegalArgumentException if facade for genres is null
     */
    public MovieInfoDialog(final GenreFacade genreFacade) {
        this("Add", Picture.ADD, genreFacade);

        imdbCodeData.setEnabled(false);
        medium2HoursData.setEnabled(false);
        medium2MinutesData.setEnabled(false);
        medium2SecondsData.setEnabled(false);
        okButton.setEnabled(false);
        czechNameData.requestFocusInWindow();
    }

    /**
     * Creates a new instance of MovieInfoDialog.
     *
     * @param genreFacade facade for genres
     * @param movie TO for movie
     * @throws IllegalArgumentException if facade for genres is null
     * or TO for serie is null
     */
    public MovieInfoDialog(final GenreFacade genreFacade, final MovieTO movie) {
        this("Update", Picture.UPDATE, genreFacade);

        Validators.validateArgumentNotNull(movie, "TO for movie");

        this.movie = movie;
        this.czechNameData.setText(movie.getCzechName());
        this.originalNameData.setText(movie.getOriginalName());
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
        for (Language subtitles : movie.getSubtitles()) {
            initSubtitles(subtitles);
        }
        initTime(movie.getMedia().get(0), this.medium1HoursData, this.medium1MinutesData, this.medium1SecondsData);
        if (movie.getMedia().size() > 1) {
            this.medium2Label.setSelected(true);
            initTime(movie.getMedia().get(1), this.medium2HoursData, this.medium2MinutesData, this.medium2SecondsData);
        }
        this.csfdData.setText(movie.getCsfd());
        final int imdbCode = movie.getImdbCode();
        if (imdbCode > 0) {
            this.imdbCodeLabel.setSelected(true);
            this.imdbCodeData.setValue(movie.getImdbCode());
        } else {
            this.imdbCodeLabel.setSelected(false);
            this.imdbCodeData.setEnabled(false);
        }
        this.wikiCzData.setText(movie.getWikiCz());
        this.wikiEnData.setText(movie.getWikiEn());
        this.pictureData.setText(movie.getPicture());
        this.noteData.setText(movie.getNote());
        this.genres = movie.getGenres();
        this.okButton.setEnabled(true);
        this.okButton.requestFocusInWindow();
    }

    /**
     * Creates a new instance of MovieInfoDialog.
     *
     * @param name    name
     * @param picture picture
     * @param genreFacade facade for genres
     * @throws IllegalArgumentException if facade for genres is null
     */
    private MovieInfoDialog(final String name, final Picture picture, final GenreFacade genreFacade) {
        super(new JFrame(), name, true);

        Validators.validateArgumentNotNull(genreFacade, "Facade for genres");

        this.genreFacade = genreFacade;
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
        initLabelComponent(yearLabel, yearData);
        initButtonGroup(czechLanguageData, englishLanguageData, frenchLanguageData, japaneseLanguageData);
        initLabelComponent(csfdLabel, csfdData);
        initLabelComponent(wikiCzLabel, wikiCzData);
        initLabelComponent(wikiEnLabel, wikiEnData);
        initLabelComponent(pictureLabel, pictureData);
        initLabelComponent(noteLabel, noteData);
        initComponentsFocusable(languageLabel, subtitlesLabel, medium1Label);

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

        genresButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                genresAction();
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
        movie.setMedia(getMedia());
        movie.setCsfd(csfdData.getText());
        movie.setImdbCode(imdbCodeLabel.isSelected() ? (Integer) imdbCodeData.getValue() : -1);
        movie.setWikiCz(wikiCzData.getText());
        movie.setWikiEn(wikiEnData.getText());
        movie.setPicture(pictureData.getText());
        movie.setNote(noteData.getText());
        movie.setGenres(genres);
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

    /**
     * Returns media.
     *
     * @return media
     */
    private List<Integer> getMedia() {
        final List<Integer> media = new ArrayList<>();
        media.add(getMedium(medium1HoursData, medium1MinutesData, medium1SecondsData));
        if (medium2Label.isSelected()) {
            media.add(getMedium(medium2HoursData, medium2MinutesData, medium2SecondsData));
        }

        return media;
    }

    /**
     * Returns medium.
     *
     * @param hoursData data for hours
     * @param minutesData data for minutes
     * @param secondsData data for seconds
     * @return medium
     */
    private int getMedium(final JSpinner hoursData, final JSpinner minutesData, final JSpinner secondsData) {
        final int hours = (Integer) hoursData.getValue();
        final int minutes = (Integer) minutesData.getValue();
        final int seconds = (Integer) secondsData.getValue();

        return new Time(hours, minutes, seconds).getLength();
    }

    /** Performs action for button Cancel. */
    private void cancelAction() {
        returnStatus = DialogResult.CANCEL;
        movie = null;
        close();
    }

    /** Performs action for button Genres. */
    private void genresAction() {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                final GenreChooseDialog dialog = new GenreChooseDialog(genreFacade, new ArrayList<>(genres));
                dialog.setVisible(true);
                if (dialog.getReturnStatus() == DialogResult.OK) {
                    genres.clear();
                    genres.addAll(dialog.getGenres());
                    okButton.setEnabled(isInputValid());
                }
            }

        });
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

        final GroupLayout.Group componentsGroup = layout.createParallelGroup()
                .addGroup(createHorizontalComponents(layout, czechNameLabel, czechNameData))
                .addGroup(createHorizontalComponents(layout, originalNameLabel, originalNameData))
                .addGroup(createHorizontalComponents(layout, yearLabel, yearData))
                .addGroup(createHorizontalComponents(layout, languageLabel, czechLanguageData))
                .addGroup(createHorizontalSelectableComponents(layout, englishLanguageData, frenchLanguageData, japaneseLanguageData))
                .addGroup(createHorizontalComponents(layout, subtitlesLabel, czechSubtitlesData))
                .addGroup(createHorizontalSelectableComponents(layout, englishSubtitlesData))
                .addGroup(createHorizontalLengthComponents(layout, medium1Label, medium1HoursData, medium1MinutesData, medium1SecondsData))
                .addGroup(createHorizontalLengthComponents(layout, medium2Label, medium2HoursData, medium2MinutesData, medium2SecondsData))
                .addGroup(createHorizontalComponents(layout, csfdLabel, csfdData))
                .addGroup(createHorizontalComponents(layout, imdbCodeLabel, imdbCodeData))
                .addGroup(createHorizontalComponents(layout, wikiCzLabel, wikiCzData))
                .addGroup(createHorizontalComponents(layout, wikiEnLabel, wikiEnData))
                .addGroup(createHorizontalComponents(layout, pictureLabel, pictureData))
                .addGroup(createHorizontalComponents(layout, noteLabel, noteData))
                .addComponent(genresButton, HORIZONTAL_GENRES_BUTTON_SIZE, HORIZONTAL_GENRES_BUTTON_SIZE, HORIZONTAL_GENRES_BUTTON_SIZE)
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
     * Returns horizontal layout for selectable components.
     *
     * @param layout     layout
     * @param components components
     * @return horizontal layout for selectable components
     */
    private GroupLayout.Group createHorizontalSelectableComponents(final GroupLayout layout, final JComponent... components) {
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
     * Returns horizontal layout for length components.
     *
     * @param layout  layout
     * @param label   label
     * @param hours   data - hours
     * @param minutes data - minutes
     * @param seconds data - seconds
     * @return horizontal layout for length components
     */
    private GroupLayout.Group createHorizontalLengthComponents(final GroupLayout layout, final JComponent label, final JSpinner hours,
            final JSpinner minutes, final JSpinner seconds) {
        final GroupLayout.SequentialGroup lengthData = layout.createSequentialGroup()
                .addComponent(hours, HORIZONTAL_TIME_SIZE, HORIZONTAL_TIME_SIZE, HORIZONTAL_TIME_SIZE)
                .addGap(HORIZONTAL_GAP_SIZE)
                .addComponent(minutes, HORIZONTAL_TIME_SIZE, HORIZONTAL_TIME_SIZE, HORIZONTAL_TIME_SIZE)
                .addGap(HORIZONTAL_GAP_SIZE)
                .addComponent(seconds, HORIZONTAL_TIME_SIZE, HORIZONTAL_TIME_SIZE, HORIZONTAL_TIME_SIZE);

        return layout.createSequentialGroup()
                .addComponent(label, HORIZONTAL_LABEL_DIALOG_SIZE, HORIZONTAL_LABEL_DIALOG_SIZE, HORIZONTAL_LABEL_DIALOG_SIZE)
                .addGap(HORIZONTAL_GAP_SIZE)
                .addGroup(lengthData);
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
                .addGroup(createVerticalComponents(layout, czechNameLabel, czechNameData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, originalNameLabel, originalNameData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, yearLabel, yearData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, languageLabel, czechLanguageData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalSelectableComponents(layout, englishLanguageData, frenchLanguageData, japaneseLanguageData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, subtitlesLabel, czechSubtitlesData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalSelectableComponents(layout, englishSubtitlesData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalLengthComponents(layout, medium1Label, medium1HoursData, medium1MinutesData, medium1SecondsData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalLengthComponents(layout, medium2Label, medium2HoursData, medium2MinutesData, medium2SecondsData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, csfdLabel, csfdData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, imdbCodeLabel, imdbCodeData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, wikiCzLabel, wikiCzData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, wikiEnLabel, wikiEnData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, pictureLabel, pictureData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, noteLabel, noteData))
                .addGap(VERTICAL_GAP_SIZE)
                .addComponent(genresButton, CatalogSwingConstants.VERTICAL_BUTTON_SIZE, CatalogSwingConstants.VERTICAL_BUTTON_SIZE,
                        CatalogSwingConstants.VERTICAL_BUTTON_SIZE)
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
     * Returns vertical layout for selectable components.
     *
     * @param layout     layout
     * @param components components
     * @return vertical layout for selectable components
     */
    private GroupLayout.Group createVerticalSelectableComponents(final GroupLayout layout, final JComponent... components) {
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
     * Returns vertical layout for length components.
     *
     * @param layout  layout
     * @param label   label
     * @param hours   data - hours
     * @param minutes data - minutes
     * @param seconds data - seconds
     * @return vertical layout for length components
     */
    private GroupLayout.Group createVerticalLengthComponents(final GroupLayout layout, final JComponent label, final JSpinner hours, final JSpinner minutes,
            final JSpinner seconds) {
        final GroupLayout.Group lengthComponents = layout.createParallelGroup()
                .addComponent(label, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
                .addComponent(hours, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
                .addComponent(minutes, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
                .addComponent(seconds, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE);

        return layout.createParallelGroup()
                .addComponent(label, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE, CatalogSwingConstants.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstants.VERTICAL_COMPONENT_SIZE)
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(lengthComponents);
    }


    /**
     * Returns true if input is valid: czech name isn't empty string, original name isn't empty string, genres isn't empty collection.
     *
     * @return true if input is valid: czech name isn't empty string, original name isn't empty string, genres isn't empty collection
     */
    private boolean isInputValid() {
        return !czechNameData.getText().isEmpty() && !originalNameData.getText().isEmpty() && !genres.isEmpty();
    }

    /** Closes dialog. */
    private void close() {
        setVisible(false);
        dispose();
    }

}
