package cz.vhromada.catalog.gui.season;

import java.util.List;

import cz.vhromada.catalog.facade.SeasonFacade;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.catalog.facade.to.SerieTO;
import cz.vhromada.catalog.gui.commons.AbstractListDataModel;
import cz.vhromada.validators.Validators;

/**
 * A class represents data model for list with seasons.
 *
 * @author Vladimir Hromada
 */
public class SeasonsListDataModel extends AbstractListDataModel<SeasonTO> {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Facade for seasons
     */
    private SeasonFacade seasonFacade;

    /**
     * TO for serie
     */
    private SerieTO serie;

    /**
     * Creates a new instance of SeasonsListDataModel.
     *
     * @param seasonFacade facade for seasons
     * @param serie        TO for serie
     * @throws IllegalArgumentException if facade for seasons is null
     *                                  or TO for serie is null
     */
    public SeasonsListDataModel(final SeasonFacade seasonFacade, final SerieTO serie) {
        Validators.validateArgumentNotNull(seasonFacade, "Facade for seasons");
        Validators.validateArgumentNotNull(serie, "TO for serie");

        this.seasonFacade = seasonFacade;
        this.serie = serie;
        update();
    }

    @Override
    protected List<SeasonTO> getData() {
        return seasonFacade.findSeasonsBySerie(serie);
    }

    @Override
    protected String getDisplayValue(final SeasonTO dataObject) {
        return Integer.toString(dataObject.getNumber());
    }

}
