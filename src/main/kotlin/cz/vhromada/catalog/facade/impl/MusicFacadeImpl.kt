package cz.vhromada.catalog.facade.impl

import cz.vhromada.catalog.entity.Music
import cz.vhromada.catalog.facade.MusicFacade
import cz.vhromada.common.entity.Time
import cz.vhromada.common.facade.AbstractMovableParentFacade
import cz.vhromada.common.mapper.Mapper
import cz.vhromada.common.provider.AccountProvider
import cz.vhromada.common.provider.TimeProvider
import cz.vhromada.common.result.Result
import cz.vhromada.common.service.MovableService
import cz.vhromada.common.validator.MovableValidator
import org.springframework.stereotype.Component

/**
 * A class represents implementation of service for music.
 *
 * @author Vladimir Hromada
 */
@Component("musicFacade")
class MusicFacadeImpl(
        musicService: MovableService<cz.vhromada.catalog.domain.Music>,
        accountProvider: AccountProvider,
        timeProvider: TimeProvider,
        mapper: Mapper<Music, cz.vhromada.catalog.domain.Music>,
        musicValidator: MovableValidator<Music>
) : AbstractMovableParentFacade<Music, cz.vhromada.catalog.domain.Music>(musicService, accountProvider, timeProvider, mapper, musicValidator), MusicFacade {

    override fun getTotalMediaCount(): Result<Int> {
        return Result.of(service.getAll().sumBy { it.mediaCount })
    }

    override fun getTotalLength(): Result<Time> {
        return Result.of(Time(service.getAll().sumBy { it.songs.sumBy { s -> s.length } }))
    }

    override fun getSongsCount(): Result<Int> {
        return Result.of(service.getAll().sumBy { it.songs.size })
    }

    override fun getDataForUpdate(data: Music): cz.vhromada.catalog.domain.Music {
        return super.getDataForUpdate(data).copy(songs = service.get(data.id!!)!!.songs)
    }

}
