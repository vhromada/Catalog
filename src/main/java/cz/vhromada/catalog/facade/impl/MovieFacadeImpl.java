package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.common.Time;
import cz.vhromada.catalog.entity.Medium;
import cz.vhromada.catalog.entity.Movie;
import cz.vhromada.catalog.facade.MovieFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.converter.Converter;
import cz.vhromada.result.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of facade for movies.
 *
 * @author Vladimir Hromada
 */
@Component("movieFacade")
public class MovieFacadeImpl extends AbstractCatalogParentFacade<Movie, cz.vhromada.catalog.domain.Movie> implements MovieFacade {

    /**
     * Creates a new instance of MovieFacadeImpl.
     *
     * @param movieService   service for movies
     * @param converter      converter
     * @param movieValidator validator for movie
     * @throws IllegalArgumentException if service for movies is null
     *                                  or converter is null
     *                                  or validator for movie is null
     */
    @Autowired
    public MovieFacadeImpl(final CatalogService<cz.vhromada.catalog.domain.Movie> movieService,
            final Converter converter,
            final CatalogValidator<Movie> movieValidator) {
        super(movieService, converter, movieValidator);
    }

    @Override
    public Result<Integer> getTotalMediaCount() {
        int totalMediaCount = 0;
        for (final cz.vhromada.catalog.domain.Movie movie : getCatalogService().getAll()) {
            totalMediaCount += movie.getMedia().size();
        }

        return Result.of(totalMediaCount);
    }

    @Override
    public Result<Time> getTotalLength() {
        int totalLength = 0;
        for (final cz.vhromada.catalog.domain.Movie movie : getCatalogService().getAll()) {
            for (final cz.vhromada.catalog.domain.Medium medium : movie.getMedia()) {
                totalLength += medium.getLength();
            }
        }

        return Result.of(new Time(totalLength));
    }

    @Override
    protected cz.vhromada.catalog.domain.Movie getDataForUpdate(final Movie data) {
        final cz.vhromada.catalog.domain.Movie movie = super.getDataForUpdate(data);
        movie.setMedia(getUpdatedMedia(getCatalogService().get(data.getId()).getMedia(), data.getMedia()));

        return movie;
    }

    @Override
    protected Class<Movie> getEntityClass() {
        return Movie.class;
    }

    @Override
    protected Class<cz.vhromada.catalog.domain.Movie> getDomainClass() {
        return cz.vhromada.catalog.domain.Movie.class;
    }

    /**
     * Updates media.
     *
     * @param originalMedia original media
     * @param updatedMedia  updated media
     * @return updated media
     */
    private static List<cz.vhromada.catalog.domain.Medium> getUpdatedMedia(final List<cz.vhromada.catalog.domain.Medium> originalMedia,
            final List<Medium> updatedMedia) {
        final List<cz.vhromada.catalog.domain.Medium> result = new ArrayList<>();

        int index = 0;
        final int max = Math.min(originalMedia.size(), updatedMedia.size());
        while (index < max) {
            final cz.vhromada.catalog.domain.Medium medium = new cz.vhromada.catalog.domain.Medium();
            medium.setId(originalMedia.get(index).getId());
            medium.setNumber(index + 1);
            medium.setLength(updatedMedia.get(index).getLength());
            result.add(medium);
            index++;
        }
        while (index < updatedMedia.size()) {
            final cz.vhromada.catalog.domain.Medium medium = new cz.vhromada.catalog.domain.Medium();
            medium.setNumber(index + 1);
            medium.setLength(updatedMedia.get(index).getLength());
            result.add(medium);
            index++;
        }

        return result;
    }

}
