package cz.vhromada.catalog.gui.episode;

import java.util.List;

import cz.vhromada.catalog.facade.EpisodeFacade;
import cz.vhromada.catalog.facade.to.EpisodeTO;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.catalog.gui.commons.AbstractListDataModel;
import cz.vhromada.validators.Validators;

/**
 * A class represents data model for list with episodes.
 *
 * @author Vladimir Hromada
 */
public class EpisodesListDataModel extends AbstractListDataModel<EpisodeTO> {

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

    @Override
    public String getElementAt(final int index) {
        return getObjectAt(index).getName();
    }

    @Override
    protected List<EpisodeTO> getData() {
        return episodeFacade.findEpisodesBySeason(season);
    }

}
