package cz.vhromada.catalog.gui.game;

import java.util.List;

import cz.vhromada.catalog.facade.GameFacade;
import cz.vhromada.catalog.facade.to.GameTO;
import cz.vhromada.catalog.gui.commons.AbstractListDataModel;
import cz.vhromada.validators.Validators;

/**
 * A class represents data model for list with games.
 *
 * @author Vladimir Hromada
 */
public class GamesListDataModel extends AbstractListDataModel<GameTO> {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Facade for games
     */
    private GameFacade gameFacade;

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

    @Override
    protected List<GameTO> getData() {
        return gameFacade.getGames();
    }

    @Override
    protected String getDisplayValue(final GameTO dataObject) {
        return dataObject.getName();
    }

}
