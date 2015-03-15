package cz.vhromada.catalog.gui.movie;

import java.util.List;

import javax.swing.AbstractListModel;

import cz.vhromada.catalog.facade.MovieFacade;
import cz.vhromada.catalog.facade.to.MovieTO;
import cz.vhromada.validators.Validators;

/**
 * A class represents data model for list with movies.
 *
 * @author Vladimir Hromada
 */
public class MoviesListDataModel extends AbstractListModel<String> {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Facade for movies
     */
    private MovieFacade movieFacade;

    /**
     * List of TO for movie
     */
    private List<MovieTO> movies;

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

    /**
     * Returns the length of the list.
     *
     * @return the length of the list
     */
    @Override
    public int getSize() {
        return movies.size();
    }

    /**
     * Returns the value at the specified index.
     *
     * @param index the requested index
     * @return the value at index
     */
    @Override
    public String getElementAt(final int index) {
        return getMovieAt(index).getCzechName();
    }

    /**
     * Returns TO for movie at the specified index.
     *
     * @param index the requested index
     * @return TO for movie at index
     */
    public MovieTO getMovieAt(final int index) {
        return movies.get(index);
    }

    /**
     * Updates model.
     */
    public final void update() {
        movies = movieFacade.getMovies();
    }

}
