package cz.vhromada.catalog.facade;

import cz.vhromada.catalog.entity.Music;
import cz.vhromada.catalog.entity.Song;
import cz.vhromada.result.Result;

/**
 * An interface represents facade for songs.
 *
 * @author Vladimir Hromada
 */
public interface SongFacade extends CatalogChildFacade<Song, Music> {

    /**
     * Adds song. Sets new ID and position.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Music is null</li>
     * <li>Music ID is null</li>
     * <li>Music doesn't exist in data storage</li>
     * <li>Song is null</li>
     * <li>Song ID isn't null</li>
     * <li>Name is null</li>
     * <li>Name is empty string</li>
     * <li>Length of song is negative value</li>
     * <li>Note is null</li>
     * </ul>
     *
     * @param parent music
     * @param data  song
     * @return result with validation errors
     */
    @Override
    Result<Void> add(Music parent, Song data);

    /**
     * Updates song.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Song is null</li>
     * <li>ID isn't null</li>
     * <li>Name is null</li>
     * <li>Name is empty string</li>
     * <li>Length of song is negative value</li>
     * <li>Note is null</li>
     * <li>Song doesn't exist in data storage</li>
     * </ul>
     *
     * @param data new value of song
     * @return result with validation errors
     */
    @Override
    Result<Void> update(Song data);

}
