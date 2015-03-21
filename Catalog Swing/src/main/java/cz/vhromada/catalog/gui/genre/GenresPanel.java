package cz.vhromada.catalog.gui.genre;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import cz.vhromada.catalog.facade.GenreFacade;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.gui.commons.AbstractInfoDialog;
import cz.vhromada.catalog.gui.commons.AbstractOverviewDataPanel;

/**
 * A class represents panel with genres' data.
 *
 * @author Vladimir Hromada
 */
public class GenresPanel extends AbstractOverviewDataPanel<GenreTO> {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Facade for genres
     */
    private GenreFacade genreFacade;

    /**
     * Creates a new instance of GenresPanel.
     *
     * @param genreFacade facade for genres
     * @throws IllegalArgumentException if facade for genres is null
     */
    public GenresPanel(final GenreFacade genreFacade) {
        super(getGenresListDataModel(genreFacade), getGenresStatsTableDataModel(genreFacade), false);

        this.genreFacade = genreFacade;
    }

    @Override
    protected AbstractInfoDialog<GenreTO> getInfoDialog(final boolean add, final GenreTO data) {
        return add ? new GenreInfoDialog() : new GenreInfoDialog(data);
    }

    @Override
    protected void deleteData() {
        genreFacade.newData();
    }

    @Override
    protected void addData(final GenreTO data) {
        genreFacade.add(data);
    }

    @Override
    protected void updateData(final GenreTO data) {
        genreFacade.update(data);
    }

    @Override
    protected void removeData(final GenreTO data) {
        genreFacade.remove(data);
    }

    @Override
    protected void duplicatesData(final GenreTO data) {
        genreFacade.duplicate(data);
    }

    @Override
    protected void moveUpData(final GenreTO data) {
        throw new UnsupportedOperationException("Data can't be move up is not allowed for genres.");
    }

    @Override
    protected void moveDownData(final GenreTO data) {
        throw new UnsupportedOperationException("Data can't be move down is not allowed for genres.");
    }

    @Override
    protected JPanel getDataPanel(final GenreTO data) {
        return new GenreDataPanel(data);
    }

    @Override
    protected void updateDataOnChange(final JTabbedPane dataPanel, final GenreTO data) {
    }

    /**
     * Returns data model for list with genres.
     *
     * @param facade facade for genres
     * @return data model for list with genres
     * @throws IllegalArgumentException if facade for genres is null
     */
    private static GenresListDataModel getGenresListDataModel(final GenreFacade facade) {
        return new GenresListDataModel(facade);
    }

    /**
     * Returns data model for table with stats for genres.
     *
     * @param facade facade for genres
     * @return data model for table with stats for genres
     * @throws IllegalArgumentException if facade for genres is null
     */
    private static GenresStatsTableDataModel getGenresStatsTableDataModel(final GenreFacade facade) {
        return new GenresStatsTableDataModel(facade);
    }

}
