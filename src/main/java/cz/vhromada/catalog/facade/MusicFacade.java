package cz.vhromada.catalog.facade;

import cz.vhromada.catalog.entity.Music;
import cz.vhromada.common.Time;
import cz.vhromada.common.facade.MovableParentFacade;
import cz.vhromada.validation.result.Result;

/**
 * An interface represents facade for music.
 *
 * @author Vladimir Hromada
 */
public interface MusicFacade extends MovableParentFacade<Music> {

    /**
     * Adds music. Sets new ID and position.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Music is null</li>
     * <li>ID isn't null</li>
     * <li>Position isn't null</li>
     * <li>Name is null</li>
     * <li>Name is empty string</li>
     * <li>URL to english Wikipedia page about music is null</li>
     * <li>URL to czech Wikipedia page about music is null</li>
     * <li>Count of media isn't positive number</li>
     * <li>Other data is null</li>
     * <li>Note is null</li>
     * </ul>
     *
     * @param data music
     * @return result with validation errors
     */
    @Override
    Result<Void> add(Music data);

    /**
     * Updates music.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Music is null</li>
     * <li>ID is null</li>
     * <li>Position is null</li>
     * <li>Name is null</li>
     * <li>Name is empty string</li>
     * <li>URL to english Wikipedia page about music is null</li>
     * <li>URL to czech Wikipedia page about music is null</li>
     * <li>Count of media isn't positive number</li>
     * <li>Note is null</li>
     * <li>Music doesn't exist in data storage</li>
     * </ul>
     *
     * @param data new value of music
     * @return result with validation errors
     */
    @Override
    Result<Void> update(Music data);

    /**
     * Returns total count of media.
     *
     * @return result with total count of media
     */
    Result<Integer> getTotalMediaCount();

    /**
     * Returns total length of all songs.
     *
     * @return result with total length of all songs
     */
    Result<Time> getTotalLength();

    /**
     * Returns count of songs from all music.
     *
     * @return result with count of songs from all music
     */
    Result<Integer> getSongsCount();

}
