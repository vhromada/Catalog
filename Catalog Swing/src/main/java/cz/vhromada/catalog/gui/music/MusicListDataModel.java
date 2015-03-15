package cz.vhromada.catalog.gui.music;

import java.util.List;

import javax.swing.*;

import cz.vhromada.catalog.facade.MusicFacade;
import cz.vhromada.catalog.facade.to.MusicTO;
import cz.vhromada.validators.Validators;

/**
 * A class represents data model for list with music.
 *
 * @author Vladimir Hromada
 */
public class MusicListDataModel extends AbstractListModel<String> {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Facade for music
     */
    private MusicFacade musicFacade;

    /**
     * List of TO for music
     */
    private List<MusicTO> musicList;

    /**
     * Creates a new instance of MusicListDataModel.
     *
     * @param musicFacade facade for music
     * @throws IllegalArgumentException if facade for music is null
     */
    public MusicListDataModel(final MusicFacade musicFacade) {
        Validators.validateArgumentNotNull(musicFacade, "Facade for music");

        this.musicFacade = musicFacade;
        update();
    }

    /**
     * Returns the length of the list.
     *
     * @return the length of the list
     */
    @Override
    public int getSize() {
        return musicList.size();
    }

    /**
     * Returns the value at the specified index.
     *
     * @param index the requested index
     * @return the value at index
     */
    @Override
    public String getElementAt(final int index) {
        return getMusicAt(index).getName();
    }

    /**
     * Returns TO for music at the specified index.
     *
     * @param index the requested index
     * @return TO for music at index
     */
    public MusicTO getMusicAt(final int index) {
        return musicList.get(index);
    }

    /**
     * Updates model.
     */
    public final void update() {
        musicList = musicFacade.getMusic();
    }

}
