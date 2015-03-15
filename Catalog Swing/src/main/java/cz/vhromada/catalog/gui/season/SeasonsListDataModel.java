package cz.vhromada.catalog.gui.season;

import java.util.List;

import javax.swing.AbstractListModel;

import cz.vhromada.catalog.facade.SeasonFacade;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.catalog.facade.to.SerieTO;
import cz.vhromada.validators.Validators;

/**
 * A class represents data model for list with seasons.
 *
 * @author Vladimir Hromada
 */
public class SeasonsListDataModel extends AbstractListModel<Integer> {

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
     * List of TO for season
     */
    private List<SeasonTO> seasons;

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

    /**
     * Returns the length of the list.
     *
     * @return the length of the list
     */
    @Override
    public int getSize() {
        return seasons.size();
    }

    /**
     * Returns the value at the specified index.
     *
     * @param index the requested index
     * @return the value at index
     */
    @Override
    public Integer getElementAt(final int index) {
        return getSeasonAt(index).getNumber();
    }

    /**
     * Returns TO for season at the specified index.
     *
     * @param index the requested index
     * @return TO for season at index
     */
    public SeasonTO getSeasonAt(final int index) {
        return seasons.get(index);
    }

    /**
     * Updates model.
     */
    public final void update() {
        seasons = seasonFacade.findSeasonsBySerie(serie);
    }

}
