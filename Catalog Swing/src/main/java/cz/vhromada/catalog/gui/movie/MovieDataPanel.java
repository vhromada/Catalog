package cz.vhromada.catalog.gui.movie;

import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import cz.vhromada.catalog.commons.Language;
import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.to.MovieTO;
import cz.vhromada.catalog.gui.commons.AbstractDataPanel;
import cz.vhromada.catalog.gui.commons.WebPageButtonType;
import cz.vhromada.validators.Validators;

/**
 * A class represents panel with movie's data.
 *
 * @author Vladimir Hromada
 */
public class MovieDataPanel extends AbstractDataPanel<MovieTO> {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Label for picture
     */
    private JLabel pictureData = new JLabel();

    /**
     * Label for czech name
     */
    private JLabel czechNameLabel = new JLabel("Czech name");

    /**
     * Label with czech name
     */
    private JLabel czechNameData = new JLabel();

    /**
     * Label for original name
     */
    private JLabel originalNameLabel = new JLabel("Original name");

    /**
     * Label with original name
     */
    private JLabel originalNameData = new JLabel();

    /**
     * Label for genre
     */
    private JLabel genreLabel = new JLabel("Genre");

    /**
     * Label with genre
     */
    private JLabel genreData = new JLabel();

    /**
     * Label for year
     */
    private JLabel yearLabel = new JLabel("Year");

    /**
     * Label with year
     */
    private JLabel yearData = new JLabel();

    /**
     * Label for language
     */
    private JLabel languageLabel = new JLabel("Language");

    /**
     * Label with language
     */
    private JLabel languageData = new JLabel();

    /**
     * Label for subtitles
     */
    private JLabel subtitlesLabel = new JLabel("Subtitles");

    /**
     * Label with subtitles
     */
    private JLabel subtitlesData = new JLabel();

    /**
     * Label for media
     */
    private JLabel mediaLabel = new JLabel("Length of media");

    /**
     * Label with media
     */
    private JLabel mediaData = new JLabel();

    /**
     * Label for total length
     */
    private JLabel totalLengthLabel = new JLabel("Total length");

    /**
     * Label with total length
     */
    private JLabel totalLengthData = new JLabel();

    /**
     * Label for note
     */
    private JLabel noteLabel = new JLabel("Note");

    /**
     * Label with note
     */
    private JLabel noteData = new JLabel();

    /**
     * Button for showing movie's ČSFD page
     */
    private JButton csfdButton = new JButton("ČSFD");

    /**
     * Button for showing movie's IMDB page
     */
    private JButton imdbButton = new JButton("IMDB");

    /**
     * Button for showing movie's czech Wikipedia page
     */
    private JButton wikiCzButton = new JButton("Czech Wikipedia");

    /**
     * Button for showing movie's english Wikipedia page
     */
    private JButton wikiEnButton = new JButton("English Wikipedia");

    /**
     * URL to ČSFD page about movie
     */
    private String csfd;

    /**
     * IMDB code
     */
    private int imdb;

    /**
     * URL to czech Wikipedia page about movie
     */
    private String wikiCz;

    /**
     * URL to english Wikipedia page about movie
     */
    private String wikiEn;

    /**
     * Creates a new instance of MovieDataPanel.
     *
     * @param movie TO for movie
     * @throws IllegalArgumentException if movie is null
     */
    public MovieDataPanel(final MovieTO movie) {
        updateData(movie);

        Validators.validateArgumentNotNull(movie, "TO for movie");

        pictureData.setFocusable(false);

        initData(czechNameLabel, czechNameData);
        initData(originalNameLabel, originalNameData);
        initData(genreLabel, genreData);
        initData(yearLabel, yearData);
        initData(languageLabel, languageData);
        initData(subtitlesLabel, subtitlesData);
        initData(mediaLabel, mediaData);
        initData(totalLengthLabel, totalLengthData);
        initData(noteLabel, noteData);

        initButton(csfdButton, WebPageButtonType.CSFD);
        initButton(imdbButton, WebPageButtonType.IMDB);
        initButton(wikiCzButton, WebPageButtonType.WIKI_CZ);
        initButton(wikiEnButton, WebPageButtonType.WIKI_EN);

        createLayout();
    }

    @Override
    protected void updateComponentData(final MovieTO data) {
        Validators.validateArgumentNotNull(data, "TO for movie");

        final String picture = data.getPicture();
        if (picture.isEmpty()) {
            pictureData.setIcon(null);
        } else {
            pictureData.setIcon(new ImageIcon("posters/" + picture));
        }
        czechNameData.setText(data.getCzechName());
        originalNameData.setText(data.getOriginalName());
        genreData.setText(getGenres(data));
        yearData.setText(Integer.toString(data.getYear()));
        languageData.setText(data.getLanguage().toString());
        subtitlesData.setText(getSubtitles(data));
        mediaData.setText(getMedia(data));
        totalLengthData.setText(getMovieLength(data));
        noteData.setText(data.getNote());

        csfd = data.getCsfd();
        imdb = data.getImdbCode();
        wikiCz = data.getWikiCz();
        wikiEn = data.getWikiEn();

        csfdButton.setEnabled(!csfd.isEmpty());
        imdbButton.setEnabled(imdb > 0);
        wikiCzButton.setEnabled(!wikiCz.isEmpty());
        wikiEnButton.setEnabled(!wikiEn.isEmpty());
    }

    @Override
    protected String getCzWikiUrl() {
        return wikiCz;
    }

    @Override
    protected String getEnWikiUrl() {
        return wikiEn;
    }

    @Override
    protected String getCsfdUrl() {
        return csfd;
    }

    @Override
    protected int getImdbUrl() {
        return imdb;
    }

    @Override
    protected GroupLayout.Group getHorizontalLayoutWithComponents(final GroupLayout layout, final GroupLayout.Group group) {
        return group.addComponent(pictureData, HORIZONTAL_PICTURE_SIZE, HORIZONTAL_PICTURE_SIZE, HORIZONTAL_PICTURE_SIZE)
                .addGroup(createHorizontalDataComponents(layout, czechNameLabel, czechNameData))
                .addGroup(createHorizontalDataComponents(layout, originalNameLabel, originalNameData))
                .addGroup(createHorizontalDataComponents(layout, genreLabel, genreData))
                .addGroup(createHorizontalDataComponents(layout, yearLabel, yearData))
                .addGroup(createHorizontalDataComponents(layout, languageLabel, languageData))
                .addGroup(createHorizontalDataComponents(layout, subtitlesLabel, subtitlesData))
                .addGroup(createHorizontalDataComponents(layout, mediaLabel, mediaData))
                .addGroup(createHorizontalDataComponents(layout, totalLengthLabel, totalLengthData))
                .addGroup(createHorizontalDataComponents(layout, noteLabel, noteData))
                .addGroup(createHorizontalButtons(layout, csfdButton, imdbButton, wikiCzButton, wikiEnButton));
    }

    @Override
    protected GroupLayout.Group getVerticalLayoutWithComponents(final GroupLayout layout, final GroupLayout.Group group) {
        return group.addComponent(pictureData, VERTICAL_PICTURE_SIZE, VERTICAL_PICTURE_SIZE, VERTICAL_PICTURE_SIZE)
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
                .addGroup(createVerticalButtons(layout, csfdButton, imdbButton, wikiCzButton, wikiEnButton));
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
            subtitlesString.append(new Time(medium));
            subtitlesString.append(", ");
        }

        return subtitlesString.substring(0, subtitlesString.length() - 2);
    }

    /**
     * Returns total length of movie.
     *
     * @param movie TO for movie
     * @return total length of movie
     */
    private static String getMovieLength(final MovieTO movie) {
        final List<Integer> media = movie.getMedia();

        if (media == null || media.isEmpty()) {
            return new Time(0).toString();
        }

        int totalLength = 0;
        for (final Integer medium : media) {
            totalLength += medium;
        }
        return new Time(totalLength).toString();
    }

}
