package cz.vhromada.catalog.gui.season;

import java.util.List;

import javax.swing.*;

import cz.vhromada.catalog.commons.CatalogSwingConstants;
import cz.vhromada.catalog.commons.Language;
import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.facade.EpisodeFacade;
import cz.vhromada.catalog.facade.to.EpisodeTO;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.validators.Validators;

/**
 * A class represents panel with season's data.
 *
 * @author Vladimir Hromada
 */
public class SeasonDataPanel extends JPanel {

    /** SerialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Horizontal label size */
    private static final int HORIZONTAL_LABEL_SIZE = 150;

    /** Horizontal data size */
    private static final int HORIZONTAL_DATA_SIZE = 600;

    /** Horizontal gap size */
    private static final int HORIZONTAL_GAP_SIZE = 10;

    /** Vertical gap size */
    private static final int VERTICAL_GAP_SIZE = 10;

    /** Facade for episodes */
    private EpisodeFacade episodeFacade;

    /** Label for number */
    private JLabel numberLabel = new JLabel("Number of season");

    /** Label with number */
    private JLabel numberData = new JLabel();

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

    /**
     * Creates a new instance of SeasonDataPanel.
     *
     * @param season TO for season
     * @param episodeFacade facade for episodes
     * @throws IllegalArgumentException if TO for season is null
     *                                  or facade for episodes is null
     */
    public SeasonDataPanel(final SeasonTO season, final EpisodeFacade episodeFacade) {
        Validators.validateArgumentNotNull(season, "TO for season");
        Validators.validateArgumentNotNull(episodeFacade, "Facade for episodes");

        this.episodeFacade = episodeFacade;

        initData(numberLabel, numberData, Integer.toString(season.getNumber()));
        initData(yearLabel, yearData, getYear(season));
        initData(languageLabel, languageData, season.getLanguage().toString());
        initData(subtitlesLabel, subtitlesData, getSubtitles(season));
        initData(episodesCountLabel, episodesCountData, getEpisodesCount(season));
        initData(totalLengthLabel, totalLengthData, getSeasonLength(season));
        initData(noteLabel, noteData, season.getNote());

        final GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(createHorizontalLayout(layout));
        layout.setVerticalGroup(createVerticalLayout(layout));
    }

    /**
     * Updates TO for season.
     *
     * @param season TO for season
     * @throws IllegalArgumentException if TO for season is null
     */
    public void updateSeason(final SeasonTO season) {
        Validators.validateArgumentNotNull(season, "TO for season");

        numberData.setText(Integer.toString(season.getNumber()));
        yearData.setText(getYear(season));
        languageData.setText(season.getLanguage().toString());
        subtitlesData.setText(getSubtitles(season));
        episodesCountData.setText(getEpisodesCount(season));
        totalLengthData.setText(getSeasonLength(season));
        noteData.setText(season.getNote());
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
     * Returns season's year.
     *
     * @param season TO for season
     * @return season's year
     */
    private static String getYear(final SeasonTO season) {
        final int startYear = season.getStartYear();
        final int endYear = season.getEndYear();

        return startYear == endYear ? Integer.toString(startYear) : startYear + " - " + endYear;
    }

    /**
     * Returns season's subtitles.
     *
     * @param season TO for season
     * @return season's subtitles
     */
    private static String getSubtitles(final SeasonTO season) {
        final List<Language> subtitles = season.getSubtitles();

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
     * Returns count of season's episodes.
     *
     * @param season TO for season
     * @return count of season's episodes
     */
    private String getEpisodesCount(final SeasonTO season) {
        final List<EpisodeTO> episodes = episodeFacade.findEpisodesBySeason(season);
        return Integer.toString(episodes.size());
    }

    /**
     * Returns total length of all season's episodes.
     *
     * @param season TO for season
     * @return total length of all season's episodes
     */
    private String getSeasonLength(final SeasonTO season) {
        final List<EpisodeTO> episodes = episodeFacade.findEpisodesBySeason(season);
        int totalLength = 0;
        for (EpisodeTO episode : episodes) {
            totalLength += episode.getLength();
        }
        return new Time(totalLength).toString();
    }

    /**
     * Returns horizontal layout for components.
     *
     * @param layout layout
     * @return horizontal layout for components
     */
    private GroupLayout.Group createHorizontalLayout(final GroupLayout layout) {
        final GroupLayout.Group components = layout.createParallelGroup()
                .addGroup(createHorizontalDataComponents(layout, numberLabel, numberData))
                .addGroup(createHorizontalDataComponents(layout, yearLabel, yearData))
                .addGroup(createHorizontalDataComponents(layout, languageLabel, languageData))
                .addGroup(createHorizontalDataComponents(layout, subtitlesLabel, subtitlesData))
                .addGroup(createHorizontalDataComponents(layout, episodesCountLabel, episodesCountData))
                .addGroup(createHorizontalDataComponents(layout, totalLengthLabel, totalLengthData))
                .addGroup(createHorizontalDataComponents(layout, noteLabel, noteData));

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
        return layout.createSequentialGroup()
                .addGap(5)
                .addGroup(createVerticalComponents(layout, numberLabel, numberData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, yearLabel, yearData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, languageLabel, languageData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, subtitlesLabel, subtitlesData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, episodesCountLabel, episodesCountData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, totalLengthLabel, totalLengthData))
                .addGap(VERTICAL_GAP_SIZE)
                .addGroup(createVerticalComponents(layout, noteLabel, noteData))
                .addGap(VERTICAL_GAP_SIZE);
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
