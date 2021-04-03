package com.github.vhromada.catalog.facade.impl

import com.github.vhromada.catalog.entity.Music
import com.github.vhromada.catalog.entity.Song
import com.github.vhromada.catalog.facade.SongFacade
import com.github.vhromada.common.facade.AbstractChildFacade
import com.github.vhromada.common.mapper.Mapper
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.service.ChildService
import com.github.vhromada.common.service.ParentService
import com.github.vhromada.common.validator.Validator
import org.springframework.stereotype.Component

/**
 * A class represents implementation of service for songs.
 *
 * @author Vladimir Hromada
 */
@Component("songFacade")
class SongFacadeImpl(
    songService: ChildService<com.github.vhromada.catalog.domain.Song>,
    musicService: ParentService<com.github.vhromada.catalog.domain.Music>,
    mapper: Mapper<Song, com.github.vhromada.catalog.domain.Song>,
    songValidator: Validator<Song, com.github.vhromada.catalog.domain.Song>,
    musicValidator: Validator<Music, com.github.vhromada.catalog.domain.Music>
) : AbstractChildFacade<Song, com.github.vhromada.catalog.domain.Song, Music, com.github.vhromada.catalog.domain.Music>(
    childService = songService,
    parentService = musicService,
    mapper = mapper,
    childValidator = songValidator,
    parentValidator = musicValidator
), SongFacade {

    @Suppress("DuplicatedCode")
    override fun updateData(data: Song): Result<Unit> {
        val storedSong = service.get(data.id!!)
        val validationResult = validator.validateExists(storedSong)
        if (validationResult.isOk()) {
            val song = mapper.map(data)
            song.createdUser = storedSong.get().createdUser
            song.createdTime = storedSong.get().createdTime
            song.music = storedSong.get().music
            service.update(song)
        }
        return validationResult
    }

    override fun addData(parent: com.github.vhromada.catalog.domain.Music, data: Song): Result<Unit> {
        val song = mapper.map(data)
        song.music = parent
        service.add(song)
        return Result()
    }

    override fun getParent(data: com.github.vhromada.catalog.domain.Song): com.github.vhromada.catalog.domain.Music {
        return data.music!!
    }

}
