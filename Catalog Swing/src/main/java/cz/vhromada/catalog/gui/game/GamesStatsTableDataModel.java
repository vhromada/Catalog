package cz.vhromada.catalog.gui.game;

import java.util.List;

import cz.vhromada.catalog.facade.GameFacade;
import cz.vhromada.catalog.facade.to.GameTO;
import cz.vhromada.catalog.gui.commons.AbstractStatsTableDataModel;
import cz.vhromada.validators.Validators;

/**
 * A class represents data model for table with stats for games.
 *
 * @author Vladimir Hromada
 */
public class GamesStatsTableDataModel extends AbstractStatsTableDataModel<GameTO> {

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

    @Override
    public int getColumnCount() {
        return 2;
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
                return "Count of games";
            case 1:
                return "Count of media";
            default:
                throw new IndexOutOfBoundsException("Bad column");
        }
    }

    @Override
    public final void update() {
        games = gameFacade.getGames();
        totalMediaCount = gameFacade.getTotalMediaCount();
    }

}
