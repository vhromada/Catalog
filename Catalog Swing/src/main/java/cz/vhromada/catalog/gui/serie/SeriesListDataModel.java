package cz.vhromada.catalog.gui.serie;

import java.util.List;

import javax.swing.*;

import cz.vhromada.catalog.facade.SerieFacade;
import cz.vhromada.catalog.facade.to.SerieTO;
import cz.vhromada.validators.Validators;

/**
 * A class represents data model for list with series.
 *
 * @author Vladimir Hromada
 */
public class SeriesListDataModel extends AbstractListModel<String> {

    /** SerialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Facade for series */
    private SerieFacade serieFacade;

    /** List of TO for serie */
    private List<SerieTO> series;

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

    /**
     * Returns the length of the list.
     *
     * @return the length of the list
     */
    @Override
    public int getSize() {
        return series.size();

    }

    /**
     * Returns the value at the specified index.
     *
     * @param index the requested index
     * @return the value at index
     */
    @Override
    public String getElementAt(final int index) {
        return getSerieAt(index).getCzechName();
    }

    /**
     * Returns TO for serie at the specified index.
     *
     * @param index the requested index
     * @return TO for serie at index
     */
    public SerieTO getSerieAt(final int index) {
        return series.get(index);
    }

    /** Updates model. */
    public final void update() {
        series = serieFacade.getSeries();
    }

}
