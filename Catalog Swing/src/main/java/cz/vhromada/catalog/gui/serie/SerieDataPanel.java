package cz.vhromada.catalog.gui.serie;

import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.facade.EpisodeFacade;
import cz.vhromada.catalog.facade.SeasonFacade;
import cz.vhromada.catalog.facade.to.EpisodeTO;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.catalog.facade.to.SerieTO;
import cz.vhromada.catalog.gui.commons.AbstractDataPanel;
import cz.vhromada.catalog.gui.commons.WebPageButtonType;
import cz.vhromada.validators.Validators;

/**
 * A class represents panel with serie's data.
 *
 * @author Vladimir Hromada
 */
public class SerieDataPanel extends AbstractDataPanel<SerieTO> {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Facade for seasons
     */
    private SeasonFacade seasonFacade;

    /**
     * Facade for episodes
     */
    private EpisodeFacade episodeFacade;

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
     * Label for count of seasons
     */
    private JLabel seasonsCountLabel = new JLabel("Count of seasons");

    /**
     * Label with count of seasons
     */
    private JLabel seasonsCountData = new JLabel();

    /**
     * Label for count of episodes
     */
    private JLabel episodesCountLabel = new JLabel("Count of episodes");

    /**
     * Label with count of episodes
     */
    private JLabel episodesCountData = new JLabel();

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
     * Button for showing serie's ČSFD page
     */
    private JButton csfdButton = new JButton("ČSFD");

    /**
     * Button for showing serie's IMDB page
     */
    private JButton imdbButton = new JButton("IMDB");

    /**
     * Button for showing serie's czech Wikipedia page
     */
    private JButton wikiCzButton = new JButton("Czech Wikipedia");

    /**
     * Button for showing serie's english Wikipedia page
     */
    private JButton wikiEnButton = new JButton("English Wikipedia");

    /**
     * URL to ČSFD page about serie
     */
    private String csfd;

    /**
     * IMDB code
     */
    private int imdb;

    /**
     * URL to czech Wikipedia page about serie
     */
    private String wikiCz;

    /**
     * URL to english Wikipedia page about serie
     */
    private String wikiEn;

    /**
     * Creates a new instance of SerieDataPanel.
     *
     * @param serie         TO for serie
     * @param seasonFacade  facade for seasons
     * @param episodeFacade facade for episodes
     * @throws IllegalArgumentException if serie is null
     *                                  or facade for seasons is null
     *                                  or facade for episodes is null
     */
    public SerieDataPanel(final SerieTO serie, final SeasonFacade seasonFacade, final EpisodeFacade episodeFacade) {
        Validators.validateArgumentNotNull(seasonFacade, "Facade for seasons");
        Validators.validateArgumentNotNull(episodeFacade, "Facade for episodes");

        this.seasonFacade = seasonFacade;
        this.episodeFacade = episodeFacade;

        updateData(serie);

        pictureData.setFocusable(false);

        initData(czechNameLabel, czechNameData);
        initData(originalNameLabel, originalNameData);
        initData(genreLabel, genreData);
        initData(seasonsCountLabel, seasonsCountData);
        initData(episodesCountLabel, episodesCountData);
        initData(totalLengthLabel, totalLengthData);
        initData(noteLabel, noteData);

        initButton(csfdButton, WebPageButtonType.CSFD);
        initButton(imdbButton, WebPageButtonType.IMDB);
        initButton(wikiCzButton, WebPageButtonType.WIKI_CZ);
        initButton(wikiEnButton, WebPageButtonType.WIKI_EN);

        createLayout();
    }

    @Override
    protected void updateComponentData(final SerieTO data) {
        final String picture = data.getPicture();
        if (picture.isEmpty()) {
            pictureData.setIcon(null);
        } else {
            pictureData.setIcon(new ImageIcon("posters/" + picture));
        }

        czechNameData.setText(data.getCzechName());
        originalNameData.setText(data.getOriginalName());
        genreData.setText(getGenres(data));
        seasonsCountData.setText(getSeasonsCount(data));
        episodesCountData.setText(getEpisodesCount(data));
        totalLengthData.setText(getSerieLength(data));
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
        return group
                .addComponent(pictureData, HORIZONTAL_PICTURE_SIZE, HORIZONTAL_PICTURE_SIZE, HORIZONTAL_PICTURE_SIZE)
                .addGroup(createHorizontalDataComponents(layout, czechNameLabel, czechNameData))
                .addGroup(createHorizontalDataComponents(layout, originalNameLabel, originalNameData))
                .addGroup(createHorizontalDataComponents(layout, genreLabel, genreData))
                .addGroup(createHorizontalDataComponents(layout, seasonsCountLabel, seasonsCountData))
                .addGroup(createHorizontalDataComponents(layout, episodesCountLabel, episodesCountData))
                .addGroup(createHorizontalDataComponents(layout, totalLengthLabel, totalLengthData))
                .addGroup(createHorizontalDataComponents(layout, noteLabel, noteData))
                .addGroup(createHorizontalButtons(layout, csfdButton, imdbButton, wikiCzButton, wikiEnButton));
    }

    @Override
    protected GroupLayout.Group getVerticalLayoutWithComponents(final GroupLayout layout, final GroupLayout.Group group) {
        return group
                .addComponent(pictureData, VERTICAL_PICTURE_SIZE, VERTICAL_PICTURE_SIZE, VERTICAL_PICTURE_SIZE)
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, czechNameLabel, czechNameData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, originalNameLabel, originalNameData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, genreLabel, genreData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, seasonsCountLabel, seasonsCountData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, episodesCountLabel, episodesCountData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, totalLengthLabel, totalLengthData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, noteLabel, noteData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalButtons(layout, csfdButton, imdbButton, wikiCzButton, wikiEnButton));
    }

    /**
     * Returns serie's genres.
     *
     * @param serie TO for serie
     * @return serie's genres
     */
    private static String getGenres(final SerieTO serie) {
        final List<GenreTO> genres = serie.getGenres();

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
     * Returns count of serie's seasons.
     *
     * @param serie TO for serie
     * @return count of serie's seasons
     */
    private String getSeasonsCount(final SerieTO serie) {
        final List<SeasonTO> seasons = seasonFacade.findSeasonsBySerie(serie);
        return Integer.toString(seasons.size());
    }

    /**
     * Returns count of serie's episodes.
     *
     * @param serie TO for serie
     * @return count of serie's episodes
     */
    private String getEpisodesCount(final SerieTO serie) {
        final List<SeasonTO> seasons = seasonFacade.findSeasonsBySerie(serie);
        int totalCount = 0;
        for (final SeasonTO season : seasons) {
            final List<EpisodeTO> episodes = episodeFacade.findEpisodesBySeason(season);
            totalCount += episodes.size();
        }
        return Integer.toString(totalCount);
    }

    /**
     * Returns total length of all serie's seasons.
     *
     * @param serie TO for serie
     * @return total length of all serie's seasons
     */
    private String getSerieLength(final SerieTO serie) {
        final List<SeasonTO> seasons = seasonFacade.findSeasonsBySerie(serie);
        int totalLength = 0;
        for (final SeasonTO season : seasons) {
            final List<EpisodeTO> episodes = episodeFacade.findEpisodesBySeason(season);
            for (final EpisodeTO episode : episodes) {
                totalLength += episode.getLength();
            }
        }
        return new Time(totalLength).toString();
    }

}
