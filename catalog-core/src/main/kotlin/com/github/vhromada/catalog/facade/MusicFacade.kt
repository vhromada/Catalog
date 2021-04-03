package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.entity.Music
import com.github.vhromada.common.entity.Time
import com.github.vhromada.common.facade.ParentFacade
import com.github.vhromada.common.result.Result

/**
 * An interface represents facade for music.
 *
 * @author Vladimir Hromada
 */
interface MusicFacade : ParentFacade<Music> {

    /**
     * Adds music. Sets new ID and position.
     * <br></br>
     * Validation errors:
     *
     *  * ID isn't null
     *  * Position isn't null
     *  * Name is null
     *  * Name is empty string
     *  * URL to english Wikipedia page about music is null
     *  * URL to czech Wikipedia page about music is null
     *  * Count of media is null
     *  * Count of media isn't positive number
     *  * Note is null
     *
     * @param data music
     * @return result with validation errors
     */
    override fun add(data: Music): Result<Unit>

    /**
     * Updates music.
     * <br></br>
     * Validation errors:
     *
     *  * ID is null
     *  * Position is null
     *  * Name is null
     *  * Name is empty string
     *  * URL to english Wikipedia page about music is null
     *  * URL to czech Wikipedia page about music is null
     *  * Count of media is null
     *  * Count of media isn't positive number
     *  * Note is null
     *  * Music doesn't exist in data storage
     *
     * @param data new value of music
     * @return result with validation errors
     */
    override fun update(data: Music): Result<Unit>

    /**
     * Returns total count of media.
     *
     * @return result with total count of media
     */
    fun getTotalMediaCount(): Result<Int>

    /**
     * Returns total length of all songs.
     *
     * @return result with total length of all songs
     */
    fun getTotalLength(): Result<Time>

    /**
     * Returns count of songs from all music.
     *
     * @return result with count of songs from all music
     */
    fun getSongsCount(): Result<Int>

}
