package cz.vhromada.catalog.gui.movie;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import cz.vhromada.catalog.facade.GenreFacade;
import cz.vhromada.catalog.facade.MovieFacade;
import cz.vhromada.catalog.facade.to.MovieTO;
import cz.vhromada.catalog.gui.commons.AbstractInfoDialog;
import cz.vhromada.catalog.gui.commons.AbstractOverviewDataPanel;
import cz.vhromada.validators.Validators;

/**
 * A class represents panel with movies' data.
 *
 * @author Vladimir Hromada
 */
public class MoviesPanel extends AbstractOverviewDataPanel<MovieTO> {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Facade for movies
     */
    private MovieFacade movieFacade;

    /**
     * Facade for genres
     */
    private GenreFacade genreFacade;

    /**
     * Creates a new instance of MoviesPanel.
     *
     * @param movieFacade facade for movies
     * @param genreFacade facade for genres
     * @throws IllegalArgumentException if facade for movies is null
     *                                  or facade for genres is null
     */
    public MoviesPanel(final MovieFacade movieFacade, final GenreFacade genreFacade) {
        super(getMoviesListDataModel(movieFacade), getMoviesStatsTableDataModel(movieFacade));

        Validators.validateArgumentNotNull(genreFacade, "Facade for genres");

        this.movieFacade = movieFacade;
        this.genreFacade = genreFacade;
    }

    @Override
    protected AbstractInfoDialog<MovieTO> getInfoDialog(final boolean add, final MovieTO data) {
        return add ? new MovieInfoDialog(genreFacade) : new MovieInfoDialog(genreFacade, data);
    }

    @Override
    protected void deleteData() {
        movieFacade.newData();
    }

    @Override
    protected void addData(final MovieTO data) {
        movieFacade.add(data);
    }

    @Override
    protected void updateData(final MovieTO data) {
        movieFacade.update(data);
    }

    @Override
    protected void removeData(final MovieTO data) {
        movieFacade.remove(data);
    }

    @Override
    protected void duplicatesData(final MovieTO data) {
        movieFacade.duplicate(data);
    }

    @Override
    protected void moveUpData(final MovieTO data) {
        movieFacade.moveUp(data);
    }

    @Override
    protected void moveDownData(final MovieTO data) {
        movieFacade.moveDown(data);
    }

    @Override
    protected JPanel getDataPanel(final MovieTO data) {
        return new MovieDataPanel(data);
    }

    @Override
    protected void updateDataOnChange(final JTabbedPane dataPanel, final MovieTO data) {
    }

    /**
     * Returns data model for list with movies.
     *
     * @param facade facade for movies
     * @return data model for list with movies
     * @throws IllegalArgumentException if facade for movies is null
     */
    private static MoviesListDataModel getMoviesListDataModel(final MovieFacade facade) {
        return new MoviesListDataModel(facade);
    }

    /**
     * Returns data model for table with stats for movies.
     *
     * @param facade facade for movies
     * @return data model for table with stats for movies
     * @throws IllegalArgumentException if facade for movies is null
     */
    private static MoviesStatsTableDataModel getMoviesStatsTableDataModel(final MovieFacade facade) {
        return new MoviesStatsTableDataModel(facade);
    }

}
