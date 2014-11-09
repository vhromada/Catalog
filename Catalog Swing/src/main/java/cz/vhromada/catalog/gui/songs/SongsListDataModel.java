package cz.vhromada.catalog.gui.songs;

import java.util.List;

import javax.swing.*;

import cz.vhromada.catalog.facade.SongFacade;
import cz.vhromada.catalog.facade.to.MusicTO;
import cz.vhromada.catalog.facade.to.SongTO;
import cz.vhromada.validators.Validators;

/**
 * A class represents data model for list with songs.
 *
 * @author Vladimir Hromada
 */
public class SongsListDataModel extends AbstractListModel<String> {

	/** SerialVersionUID */
	private static final long serialVersionUID = 1L;

	/** Facade for songs */
	private SongFacade songFacade;

	/** TO for music */
	private MusicTO music;

	/** List of TO for song */
	private List<SongTO> songs;

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

	/**
	 * Returns the length of the list.
	 *
	 * @return the length of the list
	 */
	@Override
	public int getSize() {
		return songs.size();
	}

	/**
	 * Returns the value at the specified index.
	 *
	 * @param index the requested index
	 * @return the value at index
	 */
	@Override
	public String getElementAt(final int index) {
		return getSongAt(index).getName();
	}

	/**
	 * Returns TO for song at the specified index.
	 *
	 * @param index the requested index
	 * @return TO for song at index
	 */
	public SongTO getSongAt(final int index) {
		return songs.get(index);
	}

	/** Updates model. */
	public final void update() {
		songs = songFacade.findSongsByMusic(music);
	}

}
