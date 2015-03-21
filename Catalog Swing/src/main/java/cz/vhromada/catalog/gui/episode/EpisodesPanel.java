package cz.vhromada.catalog.gui.episode;

import java.awt.Component;

import javax.swing.JPanel;

import cz.vhromada.catalog.facade.EpisodeFacade;
import cz.vhromada.catalog.facade.to.EpisodeTO;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.catalog.gui.commons.AbstractDataPanel;
import cz.vhromada.catalog.gui.commons.AbstractInfoDialog;
import cz.vhromada.validators.Validators;

/**
 * A class represents panel with episodes' data.
 *
 * @author Vladimir Hromada
 */
public class EpisodesPanel extends AbstractDataPanel<EpisodeTO> {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Facade for episodes
     */
    private EpisodeFacade episodeFacade;

    /**
     * TO for season
     */
    private SeasonTO season;

    /**
     * Creates a new instance of EpisodesPanel.
     *
     * @param episodeFacade facade for episodes
     * @param season        TO for season
     * @throws IllegalArgumentException if facade for episodes is null
     *                                  or TO for season is null
     */
    public EpisodesPanel(final EpisodeFacade episodeFacade, final SeasonTO season) {
        super(getEpisodesListDataModel(episodeFacade, season));

        this.episodeFacade = episodeFacade;
        this.season = season;
    }

    /**
     * Sets a new value to TO for season.
     *
     * @param season new value
     * @throws IllegalArgumentException if TO for season is null
     */
    public void setSeason(final SeasonTO season) {
        Validators.validateArgumentNotNull(season, "TO for season");

        this.season = season;
    }

    @Override
    public void newData() {
        throw new UnsupportedOperationException("Calling newData is not allowed for episodes.");
    }

    @Override
    public void clearSelection() {
        throw new UnsupportedOperationException("Calling clearSelection is not allowed for episodes.");
    }

    @Override
    public void save() {
        throw new UnsupportedOperationException("Calling save is not allowed for episodes.");
    }

    @Override
    public boolean isSaved() {
        throw new UnsupportedOperationException("Calling isSaved is not allowed for episodes.");
    }

    @Override
    protected AbstractInfoDialog<EpisodeTO> getInfoDialog(final boolean add, final EpisodeTO data) {
        return add ? new EpisodeInfoDialog() : new EpisodeInfoDialog(data);
    }

    @Override
    protected void addData(final EpisodeTO data) {
        data.setSeason(season);
        episodeFacade.add(data);
    }

    @Override
    protected void deleteData() {
        throw new UnsupportedOperationException("Calling deleteData is not allowed for episodes.");
    }

    @Override
    protected void updateData(final EpisodeTO data) {
        episodeFacade.update(data);
    }

    @Override
    protected void removeData(final EpisodeTO data) {
        episodeFacade.remove(data);
    }

    @Override
    protected void duplicatesData(final EpisodeTO data) {
        episodeFacade.duplicate(data);
    }

    @Override
    protected void moveUpData(final EpisodeTO data) {
        episodeFacade.moveUp(data);
    }

    @Override
    protected void moveDownData(final EpisodeTO data) {
        episodeFacade.moveDown(data);
    }

    @Override
    protected void updateDataPanel(final Component dataPanel, final EpisodeTO data) {
        ((EpisodeDataPanel) dataPanel).updateEpisode(data);
    }

    @Override
    protected JPanel getDataPanel(final EpisodeTO data) {
        return new EpisodeDataPanel(data);
    }

    /**
     * Returns data model for list with episodes.
     *
     * @param facade       facade for episodes
     * @param seasonObject TO for season
     * @return data model for list with episodes
     * @throws IllegalArgumentException if facade for episodes is null
     *                                  or TO for season is null
     */
    private static EpisodesListDataModel getEpisodesListDataModel(final EpisodeFacade facade, final SeasonTO seasonObject) {
        return new EpisodesListDataModel(facade, seasonObject);
    }

}
