package cz.vhromada.catalog.facade

import cz.vhromada.catalog.entity.Music
import cz.vhromada.catalog.entity.Song
import cz.vhromada.common.facade.MovableChildFacade
import cz.vhromada.validation.result.Result

/**
 * An interface represents facade for songs.
 *
 * @author Vladimir Hromada
 */
interface SongFacade : MovableChildFacade<Song, Music> {

    /**
     * Adds song. Sets new ID and position.
     * <br></br>
     * Validation errors:
     *
     *  * Music ID is null
     *  * Music doesn't exist in data storage
     *  * Song ID isn't null
     *  * Song position isn't null
     *  * Name is null
     *  * Name is empty string
     *  * Length of song is null
     *  * Length of song is negative value
     *  * Note is null
     *
     * @param parent music
     * @param data   song
     * @return result with validation errors
     */
    override fun add(parent: Music, data: Song): Result<Unit>

    /**
     * Updates song.
     * <br></br>
     * Validation errors:
     *
     *  * ID isn't null
     *  * Position is null
     *  * Name is null
     *  * Name is empty string
     *  * Length of song is null
     *  * Length of song is negative value
     *  * Note is null
     *  * Song doesn't exist in data storage
     *
     * @param data new value of song
     * @return result with validation errors
     */
    override fun update(data: Song): Result<Unit>

}
