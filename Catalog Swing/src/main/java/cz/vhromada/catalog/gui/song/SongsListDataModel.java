package cz.vhromada.catalog.gui.song;

import java.util.List;

import cz.vhromada.catalog.facade.SongFacade;
import cz.vhromada.catalog.facade.to.MusicTO;
import cz.vhromada.catalog.facade.to.SongTO;
import cz.vhromada.catalog.gui.commons.AbstractListDataModel;
import cz.vhromada.validators.Validators;

/**
 * A class represents data model for list with songs.
 *
 * @author Vladimir Hromada
 */
public class SongsListDataModel extends AbstractListDataModel<SongTO> {

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
     * Creates a new instance of SongsListDataModel.
     *
     * @param songFacade facade for songs
     * @param music      TO for music
     * @throws IllegalArgumentException if facade for songs is null
     *                                  or TO for music is null
     */
    public SongsListDataModel(final SongFacade songFacade, final MusicTO music) {
        Validators.validateArgumentNotNull(songFacade, "Facade for songs");
        Validators.validateArgumentNotNull(music, "TO for music");

        this.songFacade = songFacade;
        this.music = music;
        update();
    }

    @Override
    public String getElementAt(final int index) {
        return getObjectAt(index).getName();
    }

    @Override
    protected List<SongTO> getData() {
        return songFacade.findSongsByMusic(music);
    }

}
