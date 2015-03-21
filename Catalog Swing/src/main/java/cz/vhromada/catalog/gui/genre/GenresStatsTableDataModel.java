package cz.vhromada.catalog.gui.genre;

import java.util.List;

import cz.vhromada.catalog.facade.GenreFacade;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.gui.commons.AbstractStatsTableDataModel;
import cz.vhromada.validators.Validators;

/**
 * A class represents data model for table with stats for genres.
 *
 * @author Vladimir Hromada
 */
public class GenresStatsTableDataModel extends AbstractStatsTableDataModel {

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

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        switch (columnIndex) {
            case 0:
                return genres.size();
            default:
                throw new IndexOutOfBoundsException("Bad column");
        }
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public Class<?> getColumnClass(final int columnIndex) {
        return Integer.class;
    }

    @Override
    public String getColumnName(final int column) {
        switch (column) {
            case 0:
                return "Count of genres";
            default:
                throw new IndexOutOfBoundsException("Bad column");
        }
    }

    @Override
    public final void update() {
        genres = genreFacade.getGenres();
    }

}
