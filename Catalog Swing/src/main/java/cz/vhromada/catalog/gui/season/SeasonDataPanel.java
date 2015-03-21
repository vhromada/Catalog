package cz.vhromada.catalog.gui.season;

import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JLabel;

import cz.vhromada.catalog.commons.Language;
import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.facade.EpisodeFacade;
import cz.vhromada.catalog.facade.to.EpisodeTO;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.catalog.gui.commons.AbstractDataPanel;
import cz.vhromada.validators.Validators;

/**
 * A class represents panel with season's data.
 *
 * @author Vladimir Hromada
 */
public class SeasonDataPanel extends AbstractDataPanel<SeasonTO> {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Facade for episodes
     */
    private EpisodeFacade episodeFacade;

    /**
     * Label for number
     */
    private JLabel numberLabel = new JLabel("Number of season");

    /**
     * Label with number
     */
    private JLabel numberData = new JLabel();

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
     * Creates a new instance of SeasonDataPanel.
     *
     * @param season        TO for season
     * @param episodeFacade facade for episodes
     * @throws IllegalArgumentException if TO for season is null
     *                                  or facade for episodes is null
     */
    public SeasonDataPanel(final SeasonTO season, final EpisodeFacade episodeFacade) {
        Validators.validateArgumentNotNull(episodeFacade, "Facade for episodes");

        this.episodeFacade = episodeFacade;

        updateData(season);

        initData(numberLabel, numberData);
        initData(yearLabel, yearData);
        initData(languageLabel, languageData);
        initData(subtitlesLabel, subtitlesData);
        initData(episodesCountLabel, episodesCountData);
        initData(totalLengthLabel, totalLengthData);
        initData(noteLabel, noteData);

        createLayout();
    }

    @Override
    protected void updateComponentData(final SeasonTO data) {
        numberData.setText(Integer.toString(data.getNumber()));
        yearData.setText(getYear(data));
        languageData.setText(data.getLanguage().toString());
        subtitlesData.setText(getSubtitles(data));
        episodesCountData.setText(getEpisodesCount(data));
        totalLengthData.setText(getSeasonLength(data));
        noteData.setText(data.getNote());
    }

    @Override
    protected String getCzWikiUrl() {
        throw new IllegalStateException("Getting URL to czech Wikipedia page is not allowed for seasons.");
    }

    @Override
    protected String getEnWikiUrl() {
        throw new IllegalStateException("Getting URL to english Wikipedia page is not allowed for seasons.");
    }

    @Override
    protected String getCsfdUrl() {
        throw new IllegalStateException("Getting URL to ČSFD page is not allowed for seasons.");
    }

    @Override
    protected int getImdbUrl() {
        throw new IllegalStateException("Getting URL to IMDB page is not allowed for seasons.");
    }

    @Override
    protected GroupLayout.Group getHorizontalLayoutWithComponents(final GroupLayout layout, final GroupLayout.Group group) {
        return group.addGroup(createHorizontalDataComponents(layout, numberLabel, numberData))
                .addGroup(createHorizontalDataComponents(layout, yearLabel, yearData))
                .addGroup(createHorizontalDataComponents(layout, languageLabel, languageData))
                .addGroup(createHorizontalDataComponents(layout, subtitlesLabel, subtitlesData))
                .addGroup(createHorizontalDataComponents(layout, episodesCountLabel, episodesCountData))
                .addGroup(createHorizontalDataComponents(layout, totalLengthLabel, totalLengthData))
                .addGroup(createHorizontalDataComponents(layout, noteLabel, noteData));
    }

    @Override
    protected GroupLayout.Group getVerticalLayoutWithComponents(final GroupLayout layout, final GroupLayout.Group group) {
        return group.addGroup(createVerticalComponents(layout, numberLabel, numberData))
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
        for (final EpisodeTO episode : episodes) {
            totalLength += episode.getLength();
        }
        return new Time(totalLength).toString();
    }

}
