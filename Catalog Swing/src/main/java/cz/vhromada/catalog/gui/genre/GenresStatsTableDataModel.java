package cz.vhromada.catalog.gui.genre;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import cz.vhromada.catalog.facade.GenreFacade;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.validators.Validators;

/**
 * A class represents data model for table with stats for genres.
 *
 * @author Vladimir Hromada
 */
public class GenresStatsTableDataModel extends AbstractTableModel {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Facade for genres
     */
    private GenreFacade genreFacade;

    /**
     * List of TO for genre
     */
    private List<GenreTO> genres;

    /**
     * Creates a new instance of GenresStatsTableDataModel.
     *
     * @param genreFacade facade for genres
     * @throws IllegalArgumentException if facade for genres is null
     */
    public GenresStatsTableDataModel(final GenreFacade genreFacade) {
        Validators.validateArgumentNotNull(genreFacade, "Facade for genres");

        this.genreFacade = genreFacade;
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
                return genres.size();
            default:
                throw new IndexOutOfBoundsException("Bad column");
        }
    }

    /**
     * Returns the number of columns in the model.
     *
     * @return the number of columns in the model
     */
    @Override
    public int getColumnCount() {
        return 1;
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
        return Integer.class;
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
                return "Count of genres";
            default:
                throw new IndexOutOfBoundsException("Bad column");
        }
    }

    /**
     * Updates model.
     */
    public final void update() {
        genres = genreFacade.getGenres();
    }

}
