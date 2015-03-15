package cz.vhromada.catalog.gui.song;

import java.awt.Component;

import javax.swing.JPanel;

import cz.vhromada.catalog.facade.SongFacade;
import cz.vhromada.catalog.facade.to.MusicTO;
import cz.vhromada.catalog.facade.to.SongTO;
import cz.vhromada.catalog.gui.commons.AbstractInfoDialog;
import cz.vhromada.catalog.gui.commons.AbstractInnerDataPanel;
import cz.vhromada.validators.Validators;

/**
 * A class represents panel with songs' data.
 *
 * @author Vladimir Hromada
 */
public class SongsPanel extends AbstractInnerDataPanel<SongTO> {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Facade for songs
     */
    private SongFacade songFacade;

    /**
     * TO for music
     */
    private MusicTO music;

    /**
     * Creates a new instance of SongsPanel.
     *
     * @param songFacade facade for songs
     * @param music      TO for music
     * @throws IllegalArgumentException if facade for songs is null
     *                                  or TO for music is null
     */
    public SongsPanel(final SongFacade songFacade, final MusicTO music) {
        super(getSongsListDataModel(songFacade, music));

        this.songFacade = songFacade;
        this.music = music;
    }

    /**
     * Sets a new value to TO for music.
     *
     * @param music new value
     * @throws IllegalArgumentException if TO for music is null
     */
    public void setMusic(final MusicTO music) {
        Validators.validateArgumentNotNull(music, "TO for music");

        this.music = music;
    }

    @Override
    protected AbstractInfoDialog<SongTO> getInfoDialog(final boolean add, final SongTO data) {
        return add ? new SongInfoDialog() : new SongInfoDialog(data);
    }

    @Override
    protected void addData(final SongTO data) {
        data.setMusic(music);
        songFacade.add(data);
    }

    @Override
    protected void updateData(final SongTO data) {
        songFacade.update(data);
    }

    @Override
    protected void removeData(final SongTO data) {
        songFacade.remove(data);
    }

    @Override
    protected void duplicatesData(final SongTO data) {
        songFacade.duplicate(data);
    }

    @Override
    protected void moveUpData(final SongTO data) {
        songFacade.moveUp(data);
    }

    @Override
    protected void moveDownData(final SongTO data) {
        songFacade.moveDown(data);
    }

    @Override
    protected void updateDataPanel(final Component dataPanel, final SongTO data) {
        ((SongDataPanel) dataPanel).updateSong(data);
    }

    @Override
    protected JPanel getDataPanel(final SongTO data) {
        return new SongDataPanel(data);
    }

    /**
     * Returns data model for list with songs.
     *
     * @param facade      facade for songs
     * @param musicObject TO for music
     * @return data model for list with songs
     * @throws IllegalArgumentException if facade for songs is null
     *                                  or TO for music is null
     */
    private static SongsListDataModel getSongsListDataModel(final SongFacade facade, final MusicTO musicObject) {
        return new SongsListDataModel(facade, musicObject);
    }

}
