package cz.vhromada.catalog.gui.serie;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.facade.SerieFacade;
import cz.vhromada.catalog.facade.to.SerieTO;
import cz.vhromada.validators.Validators;

/**
 * A class represents data model for table with stats for series.
 *
 * @author Vladimir Hromada
 */
public class SeriesStatsTableDataModel extends AbstractTableModel {

    /** SerialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Error message for bad column */
    private static final String BAD_COLUMN_ERROR_MESSAGE = "Bad column";

    /** Facade for series */
    private SerieFacade serieFacade;

    /** List of TO for serie */
    private List<SerieTO> series;

    /** Count of seasons from all series */
    private int seasonsCount;

    /** Count of episodes from all series */
    private int episodesCount;

    /** Total length of all series */
    private Time totalLength;

    /**
     * Creates a new instance of SeriesStatsTableDataModel.
     *
     * @param serieFacade facade for series
     * @throws IllegalArgumentException if service is null
     */
    public SeriesStatsTableDataModel(final SerieFacade serieFacade) {
        Validators.validateArgumentNotNull(serieFacade, "Facade for series");

        this.serieFacade = serieFacade;
        update();
    }

    /**
     * Returns the value for the cell at columnIndex and rowIndex.
     *
     * @param rowIndex    the row whose value is to be queried
     * @param columnIndex the column whose value is to be queried
     * @return the value at the specified cell
     */
    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        switch (columnIndex) {
            case 0:
                return series.size();
            case 1:
                return seasonsCount;
            case 2:
                return episodesCount;
            case 3:
                return totalLength.toString();
            default:
                throw new IndexOutOfBoundsException(BAD_COLUMN_ERROR_MESSAGE);
        }
    }

    /**
     * Returns the number of columns in the model.
     *
     * @return the number of columns in the model
     */
    @Override
    public int getColumnCount() {
        return 4;
    }

    /**
     * Returns the number of rows in the model.
     *
     * @return the number of rows in the model
     */
    @Override
    public int getRowCount() {
        return 1;
    }

    /**
     * Returns class of data regardless of columnIndex.
     *
     * @param columnIndex the column being queried
     * @return class of data
     */
    @Override
    public Class<?> getColumnClass(final int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Integer.class;
            case 1:
                return Integer.class;
            case 2:
                return Integer.class;
            case 3:
                return String.class;
            default:
                throw new IndexOutOfBoundsException(BAD_COLUMN_ERROR_MESSAGE);
        }
    }

    /**
     * Returns a default name for the column.
     *
     * @param column the column being queried
     * @return a string containing the default name of column
     */
    @Override
    public String getColumnName(final int column) {
        switch (column) {
            case 0:
                return "Count of series";
            case 1:
                return "Count of seasons";
            case 2:
                return "Count of episodes";
            case 3:
                return "Total length";
            default:
                throw new IndexOutOfBoundsException(BAD_COLUMN_ERROR_MESSAGE);
        }
    }

    /** Updates model. */
    public final void update() {
        series = serieFacade.getSeries();
        seasonsCount = serieFacade.getSeasonsCount();
        episodesCount = serieFacade.getEpisodesCount();
        totalLength = serieFacade.getTotalLength();
    }

}
