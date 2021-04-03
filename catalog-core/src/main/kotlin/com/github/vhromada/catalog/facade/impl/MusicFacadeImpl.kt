package com.github.vhromada.catalog.facade.impl

import com.github.vhromada.catalog.entity.Music
import com.github.vhromada.catalog.facade.MusicFacade
import com.github.vhromada.common.entity.Time
import com.github.vhromada.common.facade.AbstractParentFacade
import com.github.vhromada.common.mapper.Mapper
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.service.ParentService
import com.github.vhromada.common.validator.Validator
import org.springframework.stereotype.Component

/**
 * A class represents implementation of service for music.
 *
 * @author Vladimir Hromada
 */
@Component("musicFacade")
class MusicFacadeImpl(
    private val musicService: ParentService<com.github.vhromada.catalog.domain.Music>,
    mapper: Mapper<Music, com.github.vhromada.catalog.domain.Music>,
    musicValidator: Validator<Music, com.github.vhromada.catalog.domain.Music>
) : AbstractParentFacade<Music, com.github.vhromada.catalog.domain.Music>(parentService = musicService, mapper = mapper, validator = musicValidator), MusicFacade {

    override fun updateData(data: Music): Result<Unit> {
        val storedMusic = musicService.get(id = data.id!!)
        val validationResult = validator.validateExists(data = storedMusic)
        if (validationResult.isOk()) {
            val music = mapper.map(source = data)
                .copy(songs = storedMusic.get().songs)
            music.createdUser = storedMusic.get().createdUser
            music.createdTime = storedMusic.get().createdTime
            musicService.update(data = music)
        }
        return validationResult
    }

    override fun addData(data: Music): Result<Unit> {
        musicService.add(data = mapper.map(source = data))
        return Result()
    }

    override fun getTotalMediaCount(): Result<Int> {
        return Result.of(data = musicService.getAll().sumBy { it.mediaCount })
    }

    override fun getTotalLength(): Result<Time> {
        return Result.of(data = Time(length = musicService.getAll().sumBy { it.songs.sumBy { s -> s.length } }))
    }

    override fun getSongsCount(): Result<Int> {
        return Result.of(data = musicService.getAll().sumBy { it.songs.size })
    }

}
