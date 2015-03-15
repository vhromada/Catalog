package cz.vhromada.catalog.gui.season;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import cz.vhromada.catalog.facade.EpisodeFacade;
import cz.vhromada.catalog.facade.SeasonFacade;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.catalog.facade.to.SerieTO;
import cz.vhromada.catalog.gui.commons.AbstractInfoDialog;
import cz.vhromada.catalog.gui.commons.AbstractInnerDataPanel;
import cz.vhromada.catalog.gui.episode.EpisodesPanel;
import cz.vhromada.validators.Validators;

/**
 * A class represents panel with seasons' data.
 *
 * @author Vladimir Hromada
 */
public class SeasonsPanel extends AbstractInnerDataPanel<SeasonTO> {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Facade for seasons
     */
    private SeasonFacade seasonFacade;

    /**
     * Facade for episodes
     */
    private EpisodeFacade episodeFacade;

    /**
     * TO for serie
     */
    private SerieTO serie;

    /**
     * Creates a new instance of SeasonsPanel.
     *
     * @param seasonFacade  facade for seasons
     * @param episodeFacade facade for episodes
     * @param serie         TO for serie
     * @throws IllegalArgumentException if facade for seasons is null
     *                                  or facade for episodes is null
     *                                  or TO for serie is null
     */
    public SeasonsPanel(final SeasonFacade seasonFacade, final EpisodeFacade episodeFacade, final SerieTO serie) {
        super(getSeasonsListDataModel(seasonFacade, serie));

        Validators.validateArgumentNotNull(episodeFacade, "Facade for episodes");

        this.seasonFacade = seasonFacade;
        this.episodeFacade = episodeFacade;
        this.serie = serie;
    }

    /**
     * Sets a new value to TO for serie.
     *
     * @param serie new value
     * @throws IllegalArgumentException if TO for serie is null
     */
    public void setSerie(final SerieTO serie) {
        Validators.validateArgumentNotNull(serie, "TO for serie");

        this.serie = serie;
    }

    @Override
    protected AbstractInfoDialog<SeasonTO> getInfoDialog(final boolean add, final SeasonTO data) {
        return add ? new SeasonInfoDialog() : new SeasonInfoDialog(data);
    }

    @Override
    protected void addData(final SeasonTO data) {
        data.setSerie(serie);
        seasonFacade.add(data);
    }

    @Override
    protected void updateData(final SeasonTO data) {
        seasonFacade.update(data);
    }

    @Override
    protected void removeData(final SeasonTO data) {
        seasonFacade.remove(data);
    }

    @Override
    protected void duplicatesData(final SeasonTO data) {
        seasonFacade.duplicate(data);
    }

    @Override
    protected void moveUpData(final SeasonTO data) {
        seasonFacade.moveUp(data);
    }

    @Override
    protected void moveDownData(final SeasonTO data) {
        seasonFacade.moveDown(data);
    }

    @Override
    protected void updateDataPanel(final Component dataPanel, final SeasonTO data) {
        ((SeasonDataPanel) dataPanel).updateSeason(data);
    }

    @Override
    protected JPanel getDataPanel(final SeasonTO data) {
        return new SeasonDataPanel(data, episodeFacade);
    }

    @Override
    protected void updateDataOnChange(final JTabbedPane dataPanel, final SeasonTO data) {
        super.updateDataOnChange(dataPanel, data);

        final EpisodesPanel episodesPanel = new EpisodesPanel(episodeFacade, data);
        episodesPanel.addPropertyChangeListener("update", new PropertyChangeListener() {

            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                if (Boolean.TRUE.equals(evt.getNewValue())) {
                    updateModel(data);
                    firePropertyChange("update", false, true);
                }
            }

        });
        dataPanel.add("Episodes", episodesPanel);
    }

    /**
     * Returns data model for list with seasons.
     *
     * @param facade      facade for seasons
     * @param serieObject TO for serie
     * @return data model for list with seasons
     * @throws IllegalArgumentException if facade for seasons is null
     *                                  or TO for serie is null
     */
    private static SeasonsListDataModel getSeasonsListDataModel(final SeasonFacade facade, final SerieTO serieObject) {
        return new SeasonsListDataModel(facade, serieObject);
    }

}
