package cz.vhromada.catalog.gui.movie;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.*;

import cz.vhromada.catalog.commons.Language;
import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.to.MovieTO;
import cz.vhromada.catalog.gui.commons.CatalogSwingConstants;
import cz.vhromada.validators.Validators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class represents panel with movie's data.
 *
 * @author Vladimir Hromada
 */
public class MovieDataPanel extends JPanel {

    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(MovieDataPanel.class);

    /** SerialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Horizontal label size */
    private static final int HORIZONTAL_LABEL_SIZE = 150;

    /** Horizontal data size */
    private static final int HORIZONTAL_DATA_SIZE = 600;

    /** Horizontal button size */
    private static final int HORIZONTAL_BUTTON_SIZE = 90;

    /** Horizontal picture size */
    private static final int HORIZONTAL_PICTURE_SIZE = 200;

    /** Horizontal gap size */
    private static final int HORIZONTAL_GAP_SIZE = 10;

    /** Vertical picture size */
    private static final int VERTICAL_PICTURE_SIZE = 180;

    /** Vertical gap size */
    private static final int VERTICAL_GAP_SIZE = 10;

    /** Label for picture */
    private JLabel pictureData = new JLabel();

    /** Label for czech name */
    private JLabel czechNameLabel = new JLabel("Czech name");

    /** Label with czech name */
    private JLabel czechNameData = new JLabel();

    /** Label for original name */
    private JLabel originalNameLabel = new JLabel("Original name");

    /** Label with original name */
    private JLabel originalNameData = new JLabel();

    /** Label for genre */
    private JLabel genreLabel = new JLabel("Genre");

    /** Label with genre */
    private JLabel genreData = new JLabel();

    /** Label for year */
    private JLabel yearLabel = new JLabel("Year");

    /** Label with year */
    private JLabel yearData = new JLabel();

    /** Label for language */
    private JLabel languageLabel = new JLabel("Language");

    /** Label with language */
    private JLabel languageData = new JLabel();

    /** Label for subtitles */
    private JLabel subtitlesLabel = new JLabel("Subtitles");

    /** Label with subtitles */
    private JLabel subtitlesData = new JLabel();

    /** Label for media */
    private JLabel mediaLabel = new JLabel("Length of media");

    /** Label with media */
    private JLabel mediaData = new JLabel();

    /** Label for total length */
    private JLabel totalLengthLabel = new JLabel("Total length");

    /** Label with total length */
    private JLabel totalLengthData = new JLabel();

    /** Label for note */
    private JLabel noteLabel = new JLabel("Note");

    /** Label with note */
    private JLabel noteData = new JLabel();

    /** Button for showing movie's ČSFD page */
    private JButton csfdButton = new JButton("\u010cSFD");

    /** Button for showing movie's IMDB page */
    private JButton imdbButton = new JButton("IMDB");

    /** Button for showing movie's czech Wikipedia page */
    private JButton wikiCzButton = new JButton("Czech Wikipedia");

    /** Button for showing movie's english Wikipedia page */
    private JButton wikiEnButton = new JButton("English Wikipedia");

    /** URL to ČSFD page about movie */
    private String csfd;

    /** IMDB code */
    private int imdb;

    /** URL to czech Wikipedia page about movie */
    private String wikiCz;

    /** URL to english Wikipedia page about movie */
    private String wikiEn;

    /**
     * Creates a new instance of MovieDataPanel.
     *
     * @param movie TO for movie
     * @throws IllegalArgumentException if movie is null
     */
    public MovieDataPanel(final MovieTO movie) {
        Validators.validateArgumentNotNull(movie, "TO for movie");

        final String picture = movie.getPicture();
        if (!picture.isEmpty()) {
            pictureData.setIcon(new ImageIcon("posters/" + picture));
        }
        pictureData.setFocusable(false);

        initData(czechNameLabel, czechNameData, movie.getCzechName());
        initData(originalNameLabel, originalNameData, movie.getOriginalName());
        initData(genreLabel, genreData, getGenres(movie));
        initData(yearLabel, yearData, Integer.toString(movie.getYear()));
        initData(languageLabel, languageData, movie.getLanguage().toString());
        initData(subtitlesLabel, subtitlesData, getSubtitles(movie));
        initData(mediaLabel, mediaData, getMedia(movie));
        initData(totalLengthLabel, totalLengthData, getMovieLength(movie));
        initData(noteLabel, noteData, movie.getNote());

        csfd = movie.getCsfd();
        csfdButton.setEnabled(!csfd.isEmpty());
        csfdButton.addActionListener(createActionListener("http://www.csfd.cz/film/" + csfd, "\u010cSFD"));

        imdb = movie.getImdbCode();
        imdbButton.setEnabled(imdb > 0);
        imdbButton.addActionListener(createActionListener("http://www.imdb.com/title/tt" + imdb, "IMDB"));

        wikiCz = movie.getWikiCz();
        wikiCzButton.setEnabled(!wikiCz.isEmpty());
        wikiCzButton.addActionListener(createActionListener("http://cs.wikipedia.org/wiki/" + wikiCz, "czech Wikipedia"));

        wikiEn = movie.getWikiEn();
        wikiEnButton.setEnabled(!wikiEn.isEmpty());
        wikiEnButton.addActionListener(createActionListener("http://en.wikipedia.org/wiki/" + wikiEn, "english Wikipedia"));

        final GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(createHorizontalLayout(layout));
        layout.setVerticalGroup(createVerticalLayout(layout));
    }

    /**
     * Updates TO for movie.
     *
     * @param movie TO for movie
     * @throws IllegalArgumentException if TO for movie is null
     */
    public void updateMovie(final MovieTO movie) {
        Validators.validateArgumentNotNull(movie, "TO for movie");

        final String picture = movie.getPicture();
        if (!picture.isEmpty()) {
            pictureData.setIcon(new ImageIcon("posters/" + picture));
        } else {
            pictureData.setIcon(null);
        }
        czechNameData.setText(movie.getCzechName());
        originalNameData.setText(movie.getOriginalName());
        genreData.setText(getGenres(movie));
        yearData.setText(Integer.toString(movie.getYear()));
        languageData.setText(movie.getLanguage().toString());
        subtitlesData.setText(getSubtitles(movie));
        mediaData.setText(getMedia(movie));
        totalLengthData.setText(getMovieLength(movie));
        noteData.setText(movie.getNote());
        csfd = movie.getCsfd();
        csfdButton.setEnabled(!csfd.isEmpty());
        imdb = movie.getImdbCode();
        imdbButton.setEnabled(imdb > 0);
        wikiCz = movie.getWikiCz();
        wikiCzButton.setEnabled(!wikiCz.isEmpty());
        wikiEn = movie.getWikiEn();
        wikiEnButton.setEnabled(!wikiEn.isEmpty());
    }

    /**
     * Initializes data.
     *
     * @param label label
     * @param data  data
     * @param text  text for data
     */
    private static void initData(final JLabel label, final JLabel data, final String text) {
        label.setFocusable(false);
        label.setLabelFor(data);
        data.setText(text);
        data.setFocusable(false);
    }

    /**
     * Returns movie's genres.
     *
     * @param movie TO for movie
     * @return movie's genres
     */
    private static String getGenres(final MovieTO movie) {
        final List<GenreTO> genres = movie.getGenres();

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
     * Returns movie's subtitles.
     *
     * @param movie TO for movie
     * @return movie's subtitles
     */
    private static String getSubtitles(final MovieTO movie) {
        final List<Language> subtitles = movie.getSubtitles();

        if (subtitles == null || subtitles.isEmpty()) {
            return "";
        }

        final StringBuilder result = new StringBuilder();
        for (final Language subtitle : subtitles) {
            result.append(subtitle);
            result.append(" / ");
        }

        return result.substring(0, result.length() - 3);
    }

    /**
     * Returns movie's media.
     *
     * @param movie TO for movie
     * @return movie's media
     */
    private static String getMedia(final MovieTO movie) {
        final List<Integer> media = movie.getMedia();

        if (media == null || media.isEmpty()) {
            return "";
        }

        final StringBuilder subtitlesString = new StringBuilder();
        for (final Integer medium : media) {
            subtitlesString.append(new Time(medium).toString());
            subtitlesString.append(", ");
        }

        return subtitlesString.substring(0, subtitlesString.length() - 2);
    }

    /**
     * Returns total length of movie.
     *
     * @param movie TO for movie
     * @return total length of amovie
     */
    private static String getMovieLength(final MovieTO movie) {
        final List<Integer> media = movie.getMedia();

        if (media == null || media.isEmpty()) {
            return new Time(0).toString();
        }

        int totalLength = 0;
        for (Integer medium : media) {
            totalLength += medium;
        }
        return new Time(totalLength).toString();
    }

    /**
     * Returns action listener.
     *
     * @param url  URL to web page
     * @param name name of web page
     * @return action listener
     */
    private ActionListener createActionListener(final String url, final String name) {
        return new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    Runtime.getRuntime().exec("rundll32.exe url.dll,FileProtocolHandler " + url);
                } catch (final IOException ex) {
                    logger.error("Error in showing {} page.", name, ex);
                }

            }

        };
    }

    /**
     * Returns horizontal layout of components.
     *
     * @param layout layout
     * @return horizontal layout of components
     */
    private GroupLayout.Group createHorizontalLayout(final GroupLayout layout) {
        final GroupLayout.Group buttons = layout.createSequentialGroup()
                .addComponent(csfdButton, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE)
                .addGap(HORIZONTAL_GAP_SIZE)
                .addComponent(imdbButton, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE)
                .addGap(HORIZONTAL_GAP_SIZE)
                .addComponent(wikiCzButton, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE)
                .addGap(HORIZONTAL_GAP_SIZE)
                .addComponent(wikiEnButton, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE, HORIZONTAL_BUTTON_SIZE);

        final GroupLayout.Group components = layout.createParallelGroup()
                .addComponent(pictureData, HORIZONTAL_PICTURE_SIZE, HORIZONTAL_PICTURE_SIZE, HORIZONTAL_PICTURE_SIZE)
                .addGroup(createHorizontalDataComponents(layout, czechNameLabel, czechNameData))
                .addGroup(createHorizontalDataComponents(layout, originalNameLabel, originalNameData))
                .addGroup(createHorizontalDataComponents(layout, genreLabel, genreData))
                .addGroup(createHorizontalDataComponents(layout, yearLabel, yearData))
                .addGroup(createHorizontalDataComponents(layout, languageLabel, languageData))
                .addGroup(createHorizontalDataComponents(layout, subtitlesLabel, subtitlesData))
                .addGroup(createHorizontalDataComponents(layout, mediaLabel, mediaData))
                .addGroup(createHorizontalDataComponents(layout, totalLengthLabel, totalLengthData))
                .addGroup(createHorizontalDataComponents(layout, noteLabel, noteData))
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
     * @param label  label
     * @param data   data
     * @return horizontal layout for label component with data component
     */
    private GroupLayout.Group createHorizontalDataComponents(final GroupLayout layout, final JLabel label, final JLabel data) {
        return layout.createSequentialGroup()
                .addComponent(label, HORIZONTAL_LABEL_SIZE, HORIZONTAL_LABEL_SIZE, HORIZONTAL_LABEL_SIZE)
                .addGap(HORIZONTAL_GAP_SIZE)
                .addComponent(data, HORIZONTAL_DATA_SIZE, HORIZONTAL_DATA_SIZE, HORIZONTAL_DATA_SIZE);
    }

    /**
     * Returns vertical layout of components.
     *
     * @param layout layout
     * @return vertical layout of components
     */
    private GroupLayout.Group createVerticalLayout(final GroupLayout layout) {
        final GroupLayout.Group buttons = layout.createParallelGroup()
                .addComponent(csfdButton, CatalogSwingConstants.VERTICAL_BUTTON_SIZE, CatalogSwingConstants.VERTICAL_BUTTON_SIZE,
                        CatalogSwingConstants.VERTICAL_BUTTON_SIZE)
                .addComponent(imdbButton, CatalogSwingConstants.VERTICAL_BUTTON_SIZE, CatalogSwingConstants.VERTICAL_BUTTON_SIZE,
                        CatalogSwingConstants.VERTICAL_BUTTON_SIZE)
                .addComponent(wikiCzButton, CatalogSwingConstants.VERTICAL_BUTTON_SIZE, CatalogSwingConstants.VERTICAL_BUTTON_SIZE,
                        CatalogSwingConstants.VERTICAL_BUTTON_SIZE)
                .addComponent(wikiEnButton, CatalogSwingConstants.VERTICAL_BUTTON_SIZE, CatalogSwingConstants.VERTICAL_BUTTON_SIZE,
                        CatalogSwingConstants.VERTICAL_BUTTON_SIZE);

        return layout.createSequentialGroup()
                .addGap(5)
                .addComponent(pictureData, VERTICAL_PICTURE_SIZE, VERTICAL_PICTURE_SIZE, VERTICAL_PICTURE_SIZE)
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, czechNameLabel, czechNameData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, originalNameLabel, originalNameData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, genreLabel, genreData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, yearLabel, yearData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, languageLabel, languageData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, subtitlesLabel, subtitlesData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, mediaLabel, mediaData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, totalLengthLabel, totalLengthData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, noteLabel, noteData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(buttons)
                .addGap(5);
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

}
