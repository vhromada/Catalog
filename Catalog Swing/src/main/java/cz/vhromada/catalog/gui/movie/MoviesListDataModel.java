package cz.vhromada.catalog.gui.movie;

import java.util.List;

import cz.vhromada.catalog.facade.MovieFacade;
import cz.vhromada.catalog.facade.to.MovieTO;
import cz.vhromada.catalog.gui.commons.AbstractListDataModel;
import cz.vhromada.validators.Validators;

/**
 * A class represents data model for list with movies.
 *
 * @author Vladimir Hromada
 */
public class MoviesListDataModel extends AbstractListDataModel<MovieTO> {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Facade for movies
     */
    private MovieFacade movieFacade;

    /**
     * Creates a new instance of MoviesListDataModel.
     *
     * @param movieFacade facade for movies
     * @throws IllegalArgumentException if facade for movies is null
     */
    public MoviesListDataModel(final MovieFacade movieFacade) {
        Validators.validateArgumentNotNull(movieFacade, "Facade for movies");

        this.movieFacade = movieFacade;
        update();
    }

    @Override
    protected List<MovieTO> getData() {
        return movieFacade.getMovies();
    }

    @Override
    protected String getDisplayValue(final MovieTO dataObject) {
        return dataObject.getCzechName();
    }

}
