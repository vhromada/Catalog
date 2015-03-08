package cz.vhromada.catalog.gui.serie;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.*;

import cz.vhromada.catalog.commons.CatalogSwingConstant2;
import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.facade.EpisodeFacade;
import cz.vhromada.catalog.facade.SeasonFacade;
import cz.vhromada.catalog.facade.to.EpisodeTO;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.catalog.facade.to.SerieTO;
import cz.vhromada.validators.Validators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class represents panel with serie's data.
 *
 * @author Vladimir Hromada
 */
public class SerieDataPanel extends JPanel {

    /** Vertical picture size */
    public static final int VERTICAL_PICTURE_SIZE = 180;
    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(SerieDataPanel.class);
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
    /** Vertical gap size */
    private static final int VERTICAL_GAP_SIZE = 10;

    /** Facade for seasons */
    private SeasonFacade seasonFacade;

    /** Facade for episodes */
    private EpisodeFacade episodeFacade;

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

    /** Label for count of seasons */
    private JLabel seasonsCountLabel = new JLabel("Count of seasons");

    /** Label with count of seasons */
    private JLabel seasonsCountData = new JLabel();

    /** Label for count of episodes */
    private JLabel episodesCountLabel = new JLabel("Count of episodes");

    /** Label with count of episodes */
    private JLabel episodesCountData = new JLabel();

    /** Label for total length */
    private JLabel totalLengthLabel = new JLabel("Total length");

    /** Label with total length */
    private JLabel totalLengthData = new JLabel();

    /** Label for note */
    private JLabel noteLabel = new JLabel("Note");

    /** Label with note */
    private JLabel noteData = new JLabel();

    /** Button for showing serie's ČSFD page */
    private JButton csfdButton = new JButton("\u010cSFD");

    /** Button for showing serie's IMDB page */
    private JButton imdbButton = new JButton("IMDB");

    /** Button for showing serie's czech Wikipedia page */
    private JButton wikiCzButton = new JButton("Czech Wikipedia");

    /** Button for showing serie's english Wikipedia page */
    private JButton wikiEnButton = new JButton("English Wikipedia");

    /** URL to ČSFD page about serie */
    private String csfd;

    /** IMDB code */
    private int imdb;

    /** URL to czech Wikipedia page about serie */
    private String wikiCz;

    /** URL to english Wikipedia page about serie */
    private String wikiEn;

    /**
     * Creates a new instance of SerieDataPanel.
     *
     * @param serie TO for serie
     * @param seasonFacade facade for seasons
     * @param episodeFacade facade for episodes
     * @throws IllegalArgumentException if serie is null
     *                                  or facade for seasons is null
     *                                  or facade for episodes is null
     */
    public SerieDataPanel(final SerieTO serie, final SeasonFacade seasonFacade, final EpisodeFacade episodeFacade) {
        Validators.validateArgumentNotNull(serie, "TO for serie");
        Validators.validateArgumentNotNull(seasonFacade, "Facade for seasons");
        Validators.validateArgumentNotNull(episodeFacade, "Facade for episodes");

        this.seasonFacade = seasonFacade;
        this.episodeFacade = episodeFacade;

        final String picture = serie.getPicture();
        if (!picture.isEmpty()) {
            pictureData.setIcon(new ImageIcon("posters/" + picture));
        }
        pictureData.setFocusable(false);

        initData(czechNameLabel, czechNameData, serie.getCzechName());
        initData(originalNameLabel, originalNameData, serie.getOriginalName());
        initData(genreLabel, genreData, getGenres(serie));
        initData(seasonsCountLabel, seasonsCountData, getSeasonsCount(serie));
        initData(episodesCountLabel, episodesCountData, getEpisodesCount(serie));
        initData(totalLengthLabel, totalLengthData, getSerieLength(serie));
        initData(noteLabel, noteData, serie.getNote());

        csfd = serie.getCsfd();
        csfdButton.setEnabled(!csfd.isEmpty());
        csfdButton.addActionListener(createActionListener("http://www.csfd.cz/film/" + csfd, "\u010cSFD"));

        imdb = serie.getImdbCode();
        imdbButton.setEnabled(imdb > 0);
        imdbButton.addActionListener(createActionListener("http://www.imdb.com/title/tt" + imdb, "IMDB"));

        wikiCz = serie.getWikiCz();
        wikiCzButton.setEnabled(!wikiCz.isEmpty());
        wikiCzButton.addActionListener(createActionListener("http://cs.wikipedia.org/wiki/" + wikiCz, "czech Wikipedia"));

        wikiEn = serie.getWikiEn();
        wikiEnButton.setEnabled(!wikiEn.isEmpty());
        wikiEnButton.addActionListener(createActionListener("http://en.wikipedia.org/wiki/" + wikiEn, "english Wikipedia"));

        final GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(createHorizontalLayout(layout));
        layout.setVerticalGroup(createVerticalLayout(layout));
    }

    /**
     * Updates TO for serie.
     *
     * @param serie TO for serie
     * @throws IllegalArgumentException if TO for serie is null
     */
    public void updateSerie(final SerieTO serie) {
        Validators.validateArgumentNotNull(serie, "TO for serie");

        final String picture = serie.getPicture();
        if (!picture.isEmpty()) {
            pictureData.setIcon(new ImageIcon("posters/" + picture));
        } else {
            pictureData.setIcon(null);
        }
        czechNameData.setText(serie.getCzechName());
        originalNameData.setText(serie.getOriginalName());
        genreData.setText(getGenres(serie));
        seasonsCountData.setText(getSeasonsCount(serie));
        episodesCountData.setText(getEpisodesCount(serie));
        totalLengthData.setText(getSerieLength(serie));
        noteData.setText(serie.getNote());
        csfd = serie.getCsfd();
        csfdButton.setEnabled(!csfd.isEmpty());
        imdb = serie.getImdbCode();
        imdbButton.setEnabled(imdb > 0);
        wikiCz = serie.getWikiCz();
        wikiCzButton.setEnabled(!wikiCz.isEmpty());
        wikiEn = serie.getWikiEn();
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
        for (SeasonTO season : seasons) {
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
        for (SeasonTO season : seasons) {
            final List<EpisodeTO> episodes = episodeFacade.findEpisodesBySeason(season);
            for (EpisodeTO episode : episodes) {
                totalLength += episode.getLength();
            }
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
                .addGroup(createHorizontalDataComponents(layout, seasonsCountLabel, seasonsCountData))
                .addGroup(createHorizontalDataComponents(layout, episodesCountLabel, episodesCountData))
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
                .addComponent(csfdButton, CatalogSwingConstant2.VERTICAL_BUTTON_SIZE, CatalogSwingConstant2.VERTICAL_BUTTON_SIZE,
                        CatalogSwingConstant2.VERTICAL_BUTTON_SIZE)
                .addComponent(imdbButton, CatalogSwingConstant2.VERTICAL_BUTTON_SIZE, CatalogSwingConstant2.VERTICAL_BUTTON_SIZE,
                        CatalogSwingConstant2.VERTICAL_BUTTON_SIZE)
                .addComponent(wikiCzButton, CatalogSwingConstant2.VERTICAL_BUTTON_SIZE, CatalogSwingConstant2.VERTICAL_BUTTON_SIZE,
                        CatalogSwingConstant2.VERTICAL_BUTTON_SIZE)
                .addComponent(wikiEnButton, CatalogSwingConstant2.VERTICAL_BUTTON_SIZE, CatalogSwingConstant2.VERTICAL_BUTTON_SIZE,
                        CatalogSwingConstant2.VERTICAL_BUTTON_SIZE);

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
                .addGroup(createVerticalComponents(layout, seasonsCountLabel, seasonsCountData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, episodesCountLabel, episodesCountData))
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
                .addComponent(label, CatalogSwingConstant2.VERTICAL_COMPONENT_SIZE, CatalogSwingConstant2.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstant2.VERTICAL_COMPONENT_SIZE)
                .addGap(VERTICAL_GAP_SIZE)
                .addComponent(data, CatalogSwingConstant2.VERTICAL_COMPONENT_SIZE, CatalogSwingConstant2.VERTICAL_COMPONENT_SIZE,
                        CatalogSwingConstant2.VERTICAL_COMPONENT_SIZE);
    }

}
