package cz.vhromada.catalog.gui.game;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import cz.vhromada.catalog.facade.GameFacade;
import cz.vhromada.catalog.facade.to.GameTO;
import cz.vhromada.catalog.gui.commons.AbstractInfoDialog;
import cz.vhromada.catalog.gui.commons.AbstractOverviewDataPanel;

/**
 * A class represents panel with games' data.
 *
 * @author Vladimir Hromada
 */
public class GamesPanel extends AbstractOverviewDataPanel<GameTO> {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Facade for games
     */
    private GameFacade gameFacade;

    /**
     * Creates a new instance of GamesPanel.
     *
     * @param gameFacade facade for games
     * @throws IllegalArgumentException if facade for games is null
     */
    public GamesPanel(final GameFacade gameFacade) {
        super(getGameListDataModel(gameFacade), getGamesStatsTableDataModel(gameFacade));

        this.gameFacade = gameFacade;
    }

    @Override
    protected AbstractInfoDialog<GameTO> getInfoDialog(final boolean add, final GameTO data) {
        return add ? new GameInfoDialog() : new GameInfoDialog(data);
    }

    @Override
    protected void deleteData() {
        gameFacade.newData();
    }

    @Override
    protected void addData(final GameTO data) {
        gameFacade.add(data);
    }

    @Override
    protected void updateData(final GameTO data) {
        gameFacade.update(data);
    }

    @Override
    protected void removeData(final GameTO data) {
        gameFacade.remove(data);
    }

    @Override
    protected void duplicatesData(final GameTO data) {
        gameFacade.duplicate(data);
    }

    @Override
    protected void moveUpData(final GameTO data) {
        gameFacade.moveUp(data);
    }

    @Override
    protected void moveDownData(final GameTO data) {
        gameFacade.moveDown(data);
    }

    @Override
    protected JPanel getDataPanel(final GameTO data) {
        return new GameDataPanel(data);
    }

    @Override
    protected void updateDataOnChange(final JTabbedPane dataPanel, final GameTO data) {
    }

    /**
     * Returns data model for list with games.
     *
     * @param facade facade for games
     * @return data model for list with games
     * @throws IllegalArgumentException if facade for games is null
     */
    private static GamesListDataModel getGameListDataModel(final GameFacade facade) {
        return new GamesListDataModel(facade);
    }

    /**
     * Returns data model for table with stats for games.
     *
     * @param facade facade for games
     * @return data model for table with stats for games
     * @throws IllegalArgumentException if facade for games is null
     */
    private static GamesStatsTableDataModel getGamesStatsTableDataModel(final GameFacade facade) {
        return new GamesStatsTableDataModel(facade);
    }

}
