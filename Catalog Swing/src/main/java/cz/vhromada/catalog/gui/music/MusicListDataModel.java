package cz.vhromada.catalog.gui.music;

import java.util.List;

import cz.vhromada.catalog.facade.MusicFacade;
import cz.vhromada.catalog.facade.to.MusicTO;
import cz.vhromada.catalog.gui.commons.AbstractListDataModel;
import cz.vhromada.validators.Validators;

/**
 * A class represents data model for list with music.
 *
 * @author Vladimir Hromada
 */
public class MusicListDataModel extends AbstractListDataModel<MusicTO> {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Facade for music
     */
    private MusicFacade musicFacade;

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

    @Override
    public String getElementAt(final int index) {
        return getObjectAt(index).getName();
    }

    @Override
    protected List<MusicTO> getData() {
        return musicFacade.getMusic();
    }

}
