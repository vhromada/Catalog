package cz.vhromada.catalog.gui.movie;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.facade.MovieFacade;
import cz.vhromada.catalog.facade.to.MovieTO;
import cz.vhromada.validators.Validators;

/**
 * A class represents data model for table with stats for movies.
 *
 * @author Vladimir Hromada
 */
public class MoviesStatsTableDataModel extends AbstractTableModel {

    /** SerialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Error message for bad column */
    private static final String BAD_COLUMN_ERROR_MESSAGE = "Bad column";

    /** Facade for movies */
    private MovieFacade movieFacade;

    /** List of TO for movie */
    private List<MovieTO> movies;

    /** Total length of all movies */
    private Time totalLength;

    /** Total count of media */
    private int totalMediaCount;

    /**
     * Creates a new instance of MoviesStatsTableDataModel.
     *
     * @param movieFacade facade for movies
     * @throws IllegalArgumentException if facade for movies is null
     */
    public MoviesStatsTableDataModel(final MovieFacade movieFacade) {
        Validators.validateArgumentNotNull(movieFacade, "Facade for movies");

        this.movieFacade = movieFacade;
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
                return movies.size();
            case 1:
                return totalLength.toString();
            case 2:
                return totalMediaCount;
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
        return 3;
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
                return String.class;
            case 2:
                return Integer.class;
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
                return "Count of movies";
            case 1:
                return "Total length";
            case 2:
                return "Count of media";
            default:
                throw new IndexOutOfBoundsException(BAD_COLUMN_ERROR_MESSAGE);
        }
    }

    /** Updates model. */
    public final void update() {
        movies = movieFacade.getMovies();
        totalLength = movieFacade.getTotalLength();
        totalMediaCount = movieFacade.getTotalMediaCount();
    }

}
