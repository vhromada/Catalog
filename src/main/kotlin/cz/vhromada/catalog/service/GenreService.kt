package cz.vhromada.catalog.service

import cz.vhromada.catalog.domain.Genre
import cz.vhromada.catalog.repository.GenreRepository
import cz.vhromada.common.service.AbstractMovableService
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.Cache
import org.springframework.stereotype.Component

/**
 * A class represents service for genres.
 *
 * @author Vladimir Hromada
 */
@Component("genreService")
class GenreService(
        genreRepository: GenreRepository,
        @Value("#{cacheManager.getCache('catalogCache')}") cache: Cache) : AbstractMovableService<Genre>(genreRepository, cache, "genres") {

    override fun getCopy(data: Genre): Genre {
        return data.copy(id = null)
    }

}
