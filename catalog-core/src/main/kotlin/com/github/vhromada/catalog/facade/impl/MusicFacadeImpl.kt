package com.github.vhromada.catalog.facade.impl

import com.github.vhromada.catalog.entity.Music
import com.github.vhromada.catalog.facade.MusicFacade
import com.github.vhromada.common.entity.Time
import com.github.vhromada.common.facade.AbstractMovableParentFacade
import com.github.vhromada.common.mapper.Mapper
import com.github.vhromada.common.provider.AccountProvider
import com.github.vhromada.common.provider.TimeProvider
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.service.MovableService
import com.github.vhromada.common.validator.MovableValidator
import org.springframework.stereotype.Component

/**
 * A class represents implementation of service for music.
 *
 * @author Vladimir Hromada
 */
@Component("musicFacade")
class MusicFacadeImpl(
        musicService: MovableService<com.github.vhromada.catalog.domain.Music>,
        accountProvider: AccountProvider,
        timeProvider: TimeProvider,
        mapper: Mapper<Music, com.github.vhromada.catalog.domain.Music>,
        musicValidator: MovableValidator<Music>
) : AbstractMovableParentFacade<Music, com.github.vhromada.catalog.domain.Music>(musicService, accountProvider, timeProvider, mapper, musicValidator), MusicFacade {

    override fun getTotalMediaCount(): Result<Int> {
        return Result.of(service.getAll().sumBy { it.mediaCount })
    }

    override fun getTotalLength(): Result<Time> {
        return Result.of(Time(service.getAll().sumBy { it.songs.sumBy { s -> s.length } }))
    }

    override fun getSongsCount(): Result<Int> {
        return Result.of(service.getAll().sumBy { it.songs.size })
    }

    override fun getDataForUpdate(data: Music): com.github.vhromada.catalog.domain.Music {
        return super.getDataForUpdate(data).copy(songs = service.get(data.id!!).get().songs)
    }

}
