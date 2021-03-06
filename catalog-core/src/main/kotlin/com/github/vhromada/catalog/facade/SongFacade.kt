package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.entity.Music
import com.github.vhromada.catalog.entity.Song
import com.github.vhromada.common.facade.ChildFacade
import com.github.vhromada.common.result.Result

/**
 * An interface represents facade for songs.
 *
 * @author Vladimir Hromada
 */
interface SongFacade : ChildFacade<Song, Music> {

    /**
     * Adds song. Sets new ID and position.
     * <br></br>
     * Validation errors:
     *
     *  * Music doesn't exist in data storage
     *  * Song ID isn't null
     *  * Song position isn't null
     *  * Name is null
     *  * Name is empty string
     *  * Length of song is null
     *  * Length of song is negative value
     *  * Note is null
     *
     * @param parent music ID
     * @param data   song
     * @return result with validation errors
     */
    override fun add(parent: Int, data: Song): Result<Unit>

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
