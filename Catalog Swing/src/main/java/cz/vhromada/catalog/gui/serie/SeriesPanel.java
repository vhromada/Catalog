package cz.vhromada.catalog.gui.serie;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import cz.vhromada.catalog.facade.EpisodeFacade;
import cz.vhromada.catalog.facade.GenreFacade;
import cz.vhromada.catalog.facade.SeasonFacade;
import cz.vhromada.catalog.facade.SerieFacade;
import cz.vhromada.catalog.facade.to.SerieTO;
import cz.vhromada.catalog.gui.commons.AbstractInfoDialog;
import cz.vhromada.catalog.gui.commons.AbstractOverviewDataPanel;
import cz.vhromada.catalog.gui.season.SeasonsPanel;
import cz.vhromada.validators.Validators;

/**
 * A class represents panel with series' data.
 *
 * @author Vladimir Hromada
 */
public class SeriesPanel extends AbstractOverviewDataPanel<SerieTO> {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Facade for series
     */
    private SerieFacade serieFacade;

    /**
     * Facade for seasons
     */
    private SeasonFacade seasonFacade;

    /**
     * Facade for episodes
     */
    private EpisodeFacade episodeFacade;

    /**
     * Facade for genres
     */
    private GenreFacade genreFacade;

    /**
     * Creates a new instance of SeriesPanel.
     *
     * @param serieFacade   facade for series
     * @param seasonFacade  facade for episodes
     * @param episodeFacade facade for episodes
     * @param genreFacade   facade for genres
     * @throws IllegalArgumentException if facade for series is null
     *                                  or facade for seasons is null
     *                                  or facade for episodes is null
     *                                  or facade for genres is null
     */
    public SeriesPanel(final SerieFacade serieFacade, final SeasonFacade seasonFacade, final EpisodeFacade episodeFacade, final GenreFacade genreFacade) {
        super(getSeriesListDataModel(serieFacade), getSeriesStatsTableDataModel(serieFacade));

        Validators.validateArgumentNotNull(seasonFacade, "Facade for seasons");
        Validators.validateArgumentNotNull(episodeFacade, "Facade for episodes");
        Validators.validateArgumentNotNull(genreFacade, "Facade for genres");

        this.serieFacade = serieFacade;
        this.seasonFacade = seasonFacade;
        this.episodeFacade = episodeFacade;
        this.genreFacade = genreFacade;
    }

    @Override
    protected AbstractInfoDialog<SerieTO> getInfoDialog(final boolean add, final SerieTO data) {
        return add ? new SerieInfoDialog(genreFacade) : new SerieInfoDialog(genreFacade, data);
    }

    @Override
    protected void deleteData() {
        serieFacade.newData();
    }

    @Override
    protected void addData(final SerieTO data) {
        serieFacade.add(data);
    }

    @Override
    protected void updateData(final SerieTO data) {
        serieFacade.update(data);
    }

    @Override
    protected void removeData(final SerieTO data) {
        serieFacade.remove(data);
    }

    @Override
    protected void duplicatesData(final SerieTO data) {
        serieFacade.duplicate(data);
    }

    @Override
    protected void moveUpData(final SerieTO data) {
        serieFacade.moveUp(data);
    }

    @Override
    protected void moveDownData(final SerieTO data) {
        serieFacade.moveDown(data);
    }

    @Override
    protected JPanel getDataPanel(final SerieTO data) {
        return new SerieDataPanel(data, seasonFacade, episodeFacade);
    }

    @Override
    protected void updateDataOnChange(final JTabbedPane dataPanel, final SerieTO data) {
        final SeasonsPanel seasonsPanel = new SeasonsPanel(seasonFacade, episodeFacade, data);
        seasonsPanel.addPropertyChangeListener("update", new PropertyChangeListener() {

            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                if (Boolean.TRUE.equals(evt.getNewValue())) {
                    updateModel(data);
                }
            }

        });
        dataPanel.add("Seasons", seasonsPanel);
    }

    /**
     * Returns data model for list with series.
     *
     * @param facade facade for series
     * @return data model for list with series
     * @throws IllegalArgumentException if facade for series is null
     */
    private static SeriesListDataModel getSeriesListDataModel(final SerieFacade facade) {
        return new SeriesListDataModel(facade);
    }

    /**
     * Returns data model for table with stats for series.
     *
     * @param facade facade for series
     * @return data model for table with stats for series
     * @throws IllegalArgumentException if facade for series is null
     */
    private static SeriesStatsTableDataModel getSeriesStatsTableDataModel(final SerieFacade facade) {
        return new SeriesStatsTableDataModel(facade);
    }

}
