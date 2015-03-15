package cz.vhromada.catalog.gui.episode;

import java.util.List;

import javax.swing.AbstractListModel;

import cz.vhromada.catalog.facade.EpisodeFacade;
import cz.vhromada.catalog.facade.to.EpisodeTO;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.validators.Validators;

/**
 * A class represents data model for list with episodes.
 *
 * @author Vladimir Hromada
 */
public class EpisodesListDataModel extends AbstractListModel<String> {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Facade for episodes
     */
    private EpisodeFacade episodeFacade;

    /**
     * TO for season
     */
    private SeasonTO season;

    /**
     * List of TO for episode
     */
    private List<EpisodeTO> episodes;

    /**
     * Creates a new instance of EpisodesListDataModel.
     *
     * @param episodeFacade facade for episodes
     * @param season        TO for season
     * @throws IllegalArgumentException if facade for episodes is null
     *                                  or TO for season is null
     */
    public EpisodesListDataModel(final EpisodeFacade episodeFacade, final SeasonTO season) {
        Validators.validateArgumentNotNull(episodeFacade, "Facade for episodes");
        Validators.validateArgumentNotNull(season, "TO for season");

        this.episodeFacade = episodeFacade;
        this.season = season;
        update();
    }

    /**
     * Returns the length of the list.
     *
     * @return the length of the list
     */
    @Override
    public int getSize() {
        return episodes.size();
    }

    /**
     * Returns the value at the specified index.
     *
     * @param index the requested index
     * @return the value at index
     */
    @Override
    public String getElementAt(final int index) {
        return getEpisodeAt(index).getName();
    }

    /**
     * Returns TO for episode at the specified index.
     *
     * @param index the requested index
     * @return TO for episode at index
     */
    public EpisodeTO getEpisodeAt(final int index) {
        return episodes.get(index);
    }

    /**
     * Updates model.
     */
    public final void update() {
        episodes = episodeFacade.findEpisodesBySeason(season);
    }

}
