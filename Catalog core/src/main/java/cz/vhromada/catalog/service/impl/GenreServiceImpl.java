package cz.vhromada.catalog.service.impl;

import cz.vhromada.catalog.domain.Genre;
import cz.vhromada.catalog.repository.GenreRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of service for genres.
 *
 * @author Vladimir Hromada
 */
@Component("genreService")
public class GenreServiceImpl extends AbstractCatalogService<Genre> {

    /**
     * Creates a new instance of GenreServiceImpl.
     *
     * @param genreRepository repository for genres
     * @param cache           cache
     * @throws IllegalArgumentException if repository genres is null
     *                                  or cache is null
     */
    @Autowired
    public GenreServiceImpl(final GenreRepository genreRepository,
            @Value("#{cacheManager.getCache('catalogCache')}") final Cache cache) {
        super(genreRepository, cache, "genres");
    }

    @Override
    protected Genre getCopy(final Genre data) {
        final Genre newGenre = new Genre();
        newGenre.setName(data.getName());
        newGenre.setPosition(data.getPosition());

        return newGenre;
    }

}
