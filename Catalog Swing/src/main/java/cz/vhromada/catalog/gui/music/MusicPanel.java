package cz.vhromada.catalog.gui.music;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import cz.vhromada.catalog.facade.MusicFacade;
import cz.vhromada.catalog.facade.SongFacade;
import cz.vhromada.catalog.facade.to.MusicTO;
import cz.vhromada.catalog.gui.commons.AbstractDataPanel;
import cz.vhromada.catalog.gui.commons.AbstractInfoDialog;
import cz.vhromada.catalog.gui.song.SongsPanel;
import cz.vhromada.validators.Validators;

/**
 * A class represents panel with music data.
 *
 * @author Vladimir Hromada
 */
public class MusicPanel extends AbstractDataPanel<MusicTO> {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Facade for music
     */
    private MusicFacade musicFacade;

    /**
     * Facade for songs
     */
    private SongFacade songFacade;

    /**
     * Creates a new instance of MusicPanel.
     *
     * @param musicFacade facade for music
     * @param songFacade  facade for songs
     * @throws IllegalArgumentException if facade for music is null
     *                                  or facade for songs is null
     */
    public MusicPanel(final MusicFacade musicFacade, final SongFacade songFacade) {
        super(getMusicListDataModel(musicFacade), getMusicStatsTableDataModel(musicFacade));

        Validators.validateArgumentNotNull(songFacade, "Facade for songs");

        this.musicFacade = musicFacade;
        this.songFacade = songFacade;
    }

    @Override
    protected AbstractInfoDialog<MusicTO> getInfoDialog(final boolean add, final MusicTO data) {
        return add ? new MusicInfoDialog() : new MusicInfoDialog(data);
    }

    @Override
    protected void deleteData() {
        musicFacade.newData();
    }

    @Override
    protected void addData(final MusicTO data) {
        musicFacade.add(data);
    }

    @Override
    protected void updateData(final MusicTO data) {
        musicFacade.update(data);
    }

    @Override
    protected void removeData(final MusicTO data) {
        musicFacade.remove(data);
    }

    @Override
    protected void duplicatesData(final MusicTO data) {
        musicFacade.duplicate(data);
    }

    @Override
    protected void moveUpData(final MusicTO data) {
        musicFacade.moveUp(data);
    }

    @Override
    protected void moveDownData(final MusicTO data) {
        musicFacade.moveDown(data);
    }

    @Override
    protected void updateDataPanel(final Component dataPanel, final MusicTO data) {
        ((MusicDataPanel) dataPanel).updateMusic(data);
    }

    @Override
    protected JPanel getDataPanel(final MusicTO data) {
        return new MusicDataPanel(data, songFacade);
    }

    @Override
    protected void updateDataOnChange(final JTabbedPane dataPanel, final MusicTO data) {
        super.updateDataOnChange(dataPanel, data);

        final SongsPanel songsPanel = new SongsPanel(songFacade, data);
        songsPanel.addPropertyChangeListener("update", new PropertyChangeListener() {

            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                if (Boolean.TRUE.equals(evt.getNewValue())) {
                    updateModel(data);
                    songsPanel.setMusic(data);
                }
            }

        });
        dataPanel.add("Songs", songsPanel);
    }

    /**
     * Returns data model for list with music.
     *
     * @param facade facade for music
     * @return data model for list with music
     * @throws IllegalArgumentException if facade for music is null
     */
    private static MusicListDataModel getMusicListDataModel(final MusicFacade facade) {
        return new MusicListDataModel(facade);
    }

    /**
     * Returns data model for table with stats for music.
     *
     * @param facade facade for music
     * @return data model for table with stats for music
     * @throws IllegalArgumentException if facade for music is null
     */
    private static MusicStatsTableDataModel getMusicStatsTableDataModel(final MusicFacade facade) {
        return new MusicStatsTableDataModel(facade);
    }

}
