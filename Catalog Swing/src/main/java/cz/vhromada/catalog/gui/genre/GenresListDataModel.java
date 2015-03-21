package cz.vhromada.catalog.gui.genre;

import java.util.List;

import cz.vhromada.catalog.facade.GenreFacade;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.gui.commons.AbstractListDataModel;
import cz.vhromada.validators.Validators;

/**
 * A class represents data model for list with genres.
 *
 * @author Vladimir Hromada
 */
public class GenresListDataModel extends AbstractListDataModel<GenreTO> {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Facade for genres
     */
    private GenreFacade genreFacade;

    /**
     * Creates a new instance of GenresListDataModel.
     *
     * @param genreFacade facade for genres
     * @throws IllegalArgumentException if facade for genres is null
     */
    public GenresListDataModel(final GenreFacade genreFacade) {
        Validators.validateArgumentNotNull(genreFacade, "Facade for genres");

        this.genreFacade = genreFacade;
        update();
    }

    @Override
    protected List<GenreTO> getData() {
        return genreFacade.getGenres();
    }

    @Override
    protected String getDisplayValue(final GenreTO dataObject) {
        return dataObject.getName();
    }

}
