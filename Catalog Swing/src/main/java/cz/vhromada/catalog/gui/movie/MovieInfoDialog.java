package cz.vhromada.catalog.gui.movie;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cz.vhromada.catalog.commons.Constants;
import cz.vhromada.catalog.commons.Language;
import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.facade.GenreFacade;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.to.MovieTO;
import cz.vhromada.catalog.gui.commons.AbstractInfoDialog;
import cz.vhromada.catalog.gui.commons.CatalogSwingConstants;
import cz.vhromada.catalog.gui.commons.DialogResult;
import cz.vhromada.catalog.gui.commons.Picture;
import cz.vhromada.catalog.gui.genre.GenreChooseDialog;
import cz.vhromada.validators.Validators;

/**
 * A class represents dialog for movie.
 *
 * @author Vladimir Hromada
 */
public class MovieInfoDialog extends AbstractInfoDialog<MovieTO> {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Facade for genres
     */
    private GenreFacade genreFacade;

    /**
     * List of TO for genre
     */
    private List<GenreTO> genres = new ArrayList<>();

    /**
     * List of media
     */
    private List<Integer> media = new ArrayList<>();

    /**
     * Label for czech name
     */
    private JLabel czechNameLabel = new JLabel("Czech name");

    /**
     * Text field for czech name
     */
    private JTextField czechNameData = new JTextField();

    /**
     * Label for original name
     */
    private JLabel originalNameLabel = new JLabel("Original name");

    /**
     * Text field for original name
     */
    private JTextField originalNameData = new JTextField();

    /**
     * Label for year
     */
    private JLabel yearLabel = new JLabel("Year");

    /**
     * Spinner for year
     */
    private JSpinner yearData = new JSpinner(new SpinnerNumberModel(Constants.CURRENT_YEAR, Constants.MIN_YEAR, Constants.CURRENT_YEAR, 1));

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
     * Label for media
     */
    private JLabel mediaLabel = new JLabel("Media");

    /**
     * Data with media
     */
    private JLabel mediaData = new JLabel();

    /**
     * Button for changing media
     */
    private JButton mediaButton = new JButton("Change media", Picture.CHOOSE.getIcon());

    /**
     * Label for ČSFD
     */
    private JLabel csfdLabel = new JLabel("ČSFD");

    /**
     * Text field for ČSFD
     */
    private JTextField csfdData = new JTextField();

    /**
     * Check box for IMDB code
     */
    private JCheckBox imdbCodeLabel = new JCheckBox("IMDB code");

    /**
     * Spinner for IMDB code
     */
    private JSpinner imdbCodeData = new JSpinner(new SpinnerNumberModel(1, 1, Constants.MAX_IMDB_CODE, 1));

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
     * Label for picture
     */
    private JLabel pictureLabel = new JLabel("Picture");

    /**
     * Text field for picture
     */
    private JTextField pictureData = new JTextField();

    /**
     * Label for note
     */
    private JLabel noteLabel = new JLabel("Note");

    /**
     * Text field for note
     */
    private JTextField noteData = new JTextField();

    /**
     * Label for genres
     */
    private JLabel genreLabel = new JLabel("Genres");

    /**
     * Data with genres
     */
    private JLabel genreData = new JLabel();

    /**
     * Button for changing genres
     */
    private JButton genresButton = new JButton("Change genres", Picture.CHOOSE.getIcon());

    /**
     * Creates a new instance of MovieInfoDialog.
     *
     * @param genreFacade facade for genres
     * @throws IllegalArgumentException if facade for genres is null
     */
    public MovieInfoDialog(final GenreFacade genreFacade) {
        init();
        setGenreFacade(genreFacade);
        imdbCodeData.setEnabled(false);
    }

    /**
     * Creates a new instance of MovieInfoDialog.
     *
     * @param genreFacade facade for genres
     * @param movie       TO for movie
     * @throws IllegalArgumentException if facade for genres is null
     *                                  or TO for serie is null
     */
    public MovieInfoDialog(final GenreFacade genreFacade, final MovieTO movie) {
        super(movie);

        init();
        setGenreFacade(genreFacade);
        this.genres = movie.getGenres();
        this.media = movie.getMedia();
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
        for (final Language subtitles : movie.getSubtitles()) {
            initSubtitles(subtitles);
        }
        this.mediaData.setText(getMedia());
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
        this.genreData.setText(getGenres());
    }

    @Override
    protected void initComponents() {
        initLabelComponent(czechNameLabel, czechNameData);
        initLabelComponent(originalNameLabel, originalNameData);
        initLabelComponent(yearLabel, yearData);
        initLabelComponent(csfdLabel, csfdData);
        initLabelComponent(wikiCzLabel, wikiCzData);
        initLabelComponent(wikiEnLabel, wikiEnData);
        initLabelComponent(pictureLabel, pictureData);
        initLabelComponent(noteLabel, noteData);
        initLabelComponent(genreLabel, genreData);

        addInputValidator(czechNameData);
        addInputValidator(originalNameData);

        languagesButtonGroup.add(czechLanguageData);
        languagesButtonGroup.add(englishLanguageData);
        languagesButtonGroup.add(frenchLanguageData);
        languagesButtonGroup.add(japaneseLanguageData);

        languageLabel.setFocusable(false);
        subtitlesLabel.setFocusable(false);
        genreData.setFocusable(false);

        imdbCodeLabel.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(final ChangeEvent e) {
                imdbCodeData.setEnabled(imdbCodeLabel.isSelected());
            }

        });

        mediaButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                mediaAction();
            }

        });

        genresButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                genresAction();
            }

        });
    }

    @Override
    protected MovieTO processData(final MovieTO objectData) {
        final MovieTO movie = objectData == null ? new MovieTO() : objectData;
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
        movie.setMedia(media);
        movie.setCsfd(csfdData.getText());
        movie.setImdbCode(imdbCodeLabel.isSelected() ? (Integer) imdbCodeData.getValue() : -1);
        movie.setWikiCz(wikiCzData.getText());
        movie.setWikiEn(wikiEnData.getText());
        movie.setPicture(pictureData.getText());
        movie.setNote(noteData.getText());
        movie.setGenres(genres);

        return movie;
    }

    /**
     * Returns true if input is valid: czech name isn't empty string, original name isn't empty string, media isn't empty collection, genres isn't empty
     * collection
     *
     * @return true if input is valid: czech name isn't empty string, original name isn't empty string, media isn't empty collection, genres isn't empty
     * collection
     */
    @Override
    protected boolean isInputValid() {
        return !czechNameData.getText().isEmpty() && !originalNameData.getText().isEmpty() && !media.isEmpty() && !genres.isEmpty();
    }

    @Override
    protected GroupLayout.Group getHorizontalLayoutWithComponents(final GroupLayout layout, final GroupLayout.Group group) {
        return group
                .addGroup(createHorizontalComponents(layout, czechNameLabel, czechNameData))
                .addGroup(createHorizontalComponents(layout, originalNameLabel, originalNameData))
                .addGroup(createHorizontalComponents(layout, yearLabel, yearData))
                .addGroup(createHorizontalComponents(layout, languageLabel, czechLanguageData))
                .addGroup(createHorizontalSelectableComponent(layout, englishLanguageData))
                .addGroup(createHorizontalSelectableComponent(layout, frenchLanguageData))
                .addGroup(createHorizontalSelectableComponent(layout, japaneseLanguageData))
                .addGroup(createHorizontalComponents(layout, subtitlesLabel, czechSubtitlesData))
                .addGroup(createHorizontalSelectableComponent(layout, englishSubtitlesData))
                .addGroup(createHorizontalComponents(layout, mediaLabel, mediaData))
                .addComponent(mediaButton, HORIZONTAL_LONG_COMPONENT_SIZE, HORIZONTAL_LONG_COMPONENT_SIZE, HORIZONTAL_LONG_COMPONENT_SIZE)
                .addGroup(createHorizontalComponents(layout, csfdLabel, csfdData))
                .addGroup(createHorizontalComponents(layout, imdbCodeLabel, imdbCodeData))
                .addGroup(createHorizontalComponents(layout, wikiCzLabel, wikiCzData))
                .addGroup(createHorizontalComponents(layout, wikiEnLabel, wikiEnData))
                .addGroup(createHorizontalComponents(layout, pictureLabel, pictureData))
                .addGroup(createHorizontalComponents(layout, noteLabel, noteData))
                .addGroup(createHorizontalComponents(layout, genreLabel, genreData))
                .addComponent(genresButton, HORIZONTAL_LONG_COMPONENT_SIZE, HORIZONTAL_LONG_COMPONENT_SIZE, HORIZONTAL_LONG_COMPONENT_SIZE);
    }

    @Override
    protected GroupLayout.Group getVerticalLayoutWithComponents(final GroupLayout layout, final GroupLayout.Group group) {
        return group
                .addGroup(createVerticalComponents(layout, czechNameLabel, czechNameData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, originalNameLabel, originalNameData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, yearLabel, yearData))
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
                .addGroup(createVerticalComponents(layout, mediaLabel, mediaData))
                .addGap(VERTICAL_GAP_SIZE)
                .addComponent(mediaButton, CatalogSwingConstants.VERTICAL_BUTTON_SIZE, CatalogSwingConstants.VERTICAL_BUTTON_SIZE,
                        CatalogSwingConstants.VERTICAL_BUTTON_SIZE)
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
                .addGroup(createVerticalComponents(layout, genreLabel, genreData))
                .addGap(VERTICAL_GAP_SIZE)
                .addComponent(genresButton, CatalogSwingConstants.VERTICAL_BUTTON_SIZE, CatalogSwingConstants.VERTICAL_BUTTON_SIZE,
                        CatalogSwingConstants.VERTICAL_BUTTON_SIZE);
    }

    /**
     * Initializes facade for genres.
     *
     * @throws IllegalArgumentException if facade for genres is null
     */
    private void setGenreFacade(final GenreFacade genreFacade) {
        Validators.validateArgumentNotNull(genreFacade, "Facade for genres");

        this.genreFacade = genreFacade;
    }

    /**
     * Returns media.
     *
     * @return media
     */
    private String getMedia() {
        if (media == null || media.isEmpty()) {
            return "";
        }

        final StringBuilder subtitlesString = new StringBuilder();
        for (final Integer medium : media) {
            subtitlesString.append(new Time(medium));
            subtitlesString.append(", ");
        }

        return subtitlesString.substring(0, subtitlesString.length() - 2);
    }

    /**
     * Returns genres.
     *
     * @return genres
     */
    private String getGenres() {
        if (genres == null || genres.isEmpty()) {
            return "";
        }

        final StringBuilder subtitlesString = new StringBuilder();
        for (final GenreTO genre : genres) {
            subtitlesString.append(genre.getName());
            subtitlesString.append(", ");
        }

        return subtitlesString.substring(0, subtitlesString.length() - 2);
    }

    /**
     * Performs action for button Change media.
     */
    private void mediaAction() {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                final MediaChooseDialog dialog = new MediaChooseDialog(new ArrayList<>(media));
                dialog.setVisible(true);
                if (dialog.getReturnStatus() == DialogResult.OK) {
                    media.clear();
                    media.addAll(dialog.getMedia());
                    mediaData.setText(getMedia());
                    setOkButtonEnabled(isInputValid());
                }
            }

        });
    }

    /**
     * Performs action for button Change genres.
     */
    private void genresAction() {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                final GenreChooseDialog dialog = new GenreChooseDialog(genreFacade, new ArrayList<>(genres));
                dialog.setVisible(true);
                if (dialog.getReturnStatus() == DialogResult.OK) {
                    genres.clear();
                    genres.addAll(dialog.getGenres());
                    genreData.setText(getGenres());
                    setOkButtonEnabled(isInputValid());
                }
            }

        });
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
