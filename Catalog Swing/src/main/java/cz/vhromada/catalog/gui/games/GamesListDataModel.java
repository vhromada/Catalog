package cz.vhromada.catalog.gui.games;

import java.util.List;

import javax.swing.*;

import cz.vhromada.catalog.facade.GameFacade;
import cz.vhromada.catalog.facade.to.GameTO;
import cz.vhromada.validators.Validators;

/**
 * A class represents data model for list with games.
 *
 * @author Vladimir Hromada
 */
public class GamesListDataModel extends AbstractListModel<String> {

    /** SerialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Facade for games */
    private GameFacade gameFacade;

    /** List of TO for game */
    private List<GameTO> games;

    /**
     * Creates a new instance of GamesListDataModel.
     *
     * @param gameFacade facade for games
     * @throws IllegalArgumentException if facade for games is null
     */
    public GamesListDataModel(final GameFacade gameFacade) {
        Validators.validateArgumentNotNull(gameFacade, "Facade for games");

        this.gameFacade = gameFacade;
        update();
    }

    /**
     * Returns the length of the list.
     *
     * @return the length of the list
     */
    @Override
    public int getSize() {
        return games.size();
    }

    /**
     * Returns the value at the specified index.
     *
     * @param index the requested index
     * @return the value at index
     */
    @Override
    public String getElementAt(final int index) {
        return getGameAt(index).getName();
    }

    /**
     * Returns TO for game at the specified index.
     *
     * @param index the requested index
     * @return TO for game at index
     */
    public GameTO getGameAt(final int index) {
        return games.get(index);
    }

    /** Updates model. */
    public final void update() {
        games = gameFacade.getGames();
    }

}
