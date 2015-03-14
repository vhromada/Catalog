package cz.vhromada.catalog.gui.genre;

import java.util.List;

import javax.swing.*;

import cz.vhromada.catalog.facade.GenreFacade;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.validators.Validators;

/**
 * A class represents data model for list with genres.
 *
 * @author Vladimir Hromada
 */
public class GenresListDataModel extends AbstractListModel<String> {

    /** SerialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Facade for genres */
    private GenreFacade genreFacade;

    /** List of TO for genre */
    private List<GenreTO> genres;

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

    /**
     * Returns the length of the list.
     *
     * @return the length of the list
     */
    @Override
    public int getSize() {
        return genres.size();
    }

    /**
     * Returns the value at the specified index.
     *
     * @param index the requested index
     * @return the value at index
     */
    @Override
    public String getElementAt(final int index) {
        return getGenreAt(index).getName();
    }

    /**
     * Returns TO for genre at the specified index.
     *
     * @param index the requested index
     * @return TO for genre at index
     */
    public GenreTO getGenreAt(final int index) {
        return genres.get(index);
    }

    /** Updates model. */
    public final void update() {
        genres = genreFacade.getGenres();
    }

}
