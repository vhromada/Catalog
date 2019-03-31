package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.entity.Medium;
import cz.vhromada.catalog.entity.Movie;
import cz.vhromada.catalog.facade.MovieFacade;
import cz.vhromada.common.Time;
import cz.vhromada.common.converter.MovableConverter;
import cz.vhromada.common.facade.AbstractMovableParentFacade;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.validator.MovableValidator;
import cz.vhromada.validation.result.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of facade for movies.
 *
 * @author Vladimir Hromada
 */
@Component("movieFacade")
public class MovieFacadeImpl extends AbstractMovableParentFacade<Movie, cz.vhromada.catalog.domain.Movie> implements MovieFacade {

    /**
     * Creates a new instance of MovieFacadeImpl.
     *
     * @param movieService   service for movies
     * @param converter      converter for movies
     * @param movieValidator validator for movie
     * @throws IllegalArgumentException if service for movies is null
     *                                  or converter for movies is null
     *                                  or validator for movie is null
     */
    @Autowired
    public MovieFacadeImpl(final MovableService<cz.vhromada.catalog.domain.Movie> movieService,
        final MovableConverter<Movie, cz.vhromada.catalog.domain.Movie> converter, final MovableValidator<Movie> movieValidator) {
        super(movieService, converter, movieValidator);
    }

    @Override
    public Result<Integer> getTotalMediaCount() {
        int totalMediaCount = 0;
        for (final cz.vhromada.catalog.domain.Movie movie : getService().getAll()) {
            totalMediaCount += movie.getMedia().size();
        }

        return Result.of(totalMediaCount);
    }

    @Override
    public Result<Time> getTotalLength() {
        int totalLength = 0;
        for (final cz.vhromada.catalog.domain.Movie movie : getService().getAll()) {
            for (final cz.vhromada.catalog.domain.Medium medium : movie.getMedia()) {
                totalLength += medium.getLength();
            }
        }

        return Result.of(new Time(totalLength));
    }

    @Override
    protected cz.vhromada.catalog.domain.Movie getDataForUpdate(final Movie data) {
        final cz.vhromada.catalog.domain.Movie movie = super.getDataForUpdate(data);
        movie.setMedia(getUpdatedMedia(getService().get(data.getId()).getMedia(), data.getMedia()));

        return movie;
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
            final cz.vhromada.catalog.domain.Medium medium = getUpdatedMedium(updatedMedia, index);
            medium.setId(originalMedia.get(index).getId());
            result.add(medium);
            index++;
        }
        while (index < updatedMedia.size()) {
            result.add(getUpdatedMedium(updatedMedia, index));
            index++;
        }

        return result;
    }

    /**
     * Returns updated medium.
     *
     * @param updatedMedia list of updated media
     * @param index        index
     * @return updated medium
     */
    private static cz.vhromada.catalog.domain.Medium getUpdatedMedium(final List<Medium> updatedMedia, final int index) {
        final cz.vhromada.catalog.domain.Medium medium = new cz.vhromada.catalog.domain.Medium();
        medium.setNumber(index + 1);
        medium.setLength(updatedMedia.get(index).getLength());
        return medium;
    }

}
