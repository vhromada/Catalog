package cz.vhromada.catalog.gui.game;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import cz.vhromada.catalog.facade.GameFacade;
import cz.vhromada.catalog.facade.to.GameTO;
import cz.vhromada.validators.Validators;

/**
 * A class represents data model for table with stats for games.
 *
 * @author Vladimir Hromada
 */
public class GamesStatsTableDataModel extends AbstractTableModel {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Facade for games
     */
    private GameFacade gameFacade;

    /**
     * List of TO for game
     */
    private List<GameTO> games;

    /**
     * Total count of media
     */
    private int totalMediaCount;

    /**
     * Creates a new instance of GamesStatsTableDataModel.
     *
     * @param gameFacade facade for games
     * @throws IllegalArgumentException if facade for games is null
     */
    public GamesStatsTableDataModel(final GameFacade gameFacade) {
        Validators.validateArgumentNotNull(gameFacade, "Facade for games");

        this.gameFacade = gameFacade;
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
                return games.size();
            case 1:
                return totalMediaCount;
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
        return 2;
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
                return "Count of games";
            case 1:
                return "Count of media";
            default:
                throw new IndexOutOfBoundsException("Bad column");
        }
    }

    /**
     * Updates model.
     */
    public final void update() {
        games = gameFacade.getGames();
        totalMediaCount = gameFacade.getTotalMediaCount();
    }

}
