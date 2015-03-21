package cz.vhromada.catalog.gui.serie;

import java.util.List;

import cz.vhromada.catalog.facade.SerieFacade;
import cz.vhromada.catalog.facade.to.SerieTO;
import cz.vhromada.catalog.gui.commons.AbstractListDataModel;
import cz.vhromada.validators.Validators;

/**
 * A class represents data model for list with series.
 *
 * @author Vladimir Hromada
 */
public class SeriesListDataModel extends AbstractListDataModel<SerieTO> {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Facade for series
     */
    private SerieFacade serieFacade;

    /**
     * Creates a new instance of SeriesListDataModel.
     *
     * @param serieFacade facade for series
     * @throws IllegalArgumentException if service is null
     */
    public SeriesListDataModel(final SerieFacade serieFacade) {
        Validators.validateArgumentNotNull(serieFacade, "Facade for series");

        this.serieFacade = serieFacade;
        update();
    }

    @Override
    protected List<SerieTO> getData() {
        return serieFacade.getSeries();
    }

    @Override
    protected String getDisplayValue(final SerieTO dataObject) {
        return dataObject.getCzechName();
    }

}
