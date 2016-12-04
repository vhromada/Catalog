package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.entity.EpisodeTO;
import cz.vhromada.catalog.validator.EpisodeValidator;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * A class represents implementation of validator for episode.
 *
 * @author Vladimir Hromada
 */
@Component("episodeValidator")
public class EpisodeValidatorImpl implements EpisodeValidator {

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void validateNewEpisodeTO(final EpisodeTO episode) {
        validateEpisodeTO(episode);
        Assert.isNull(episode.getId(), "ID must be null.");
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void validateExistingEpisodeTO(final EpisodeTO episode) {
        validateEpisodeTO(episode);
        Assert.notNull(episode.getId(), "ID mustn't be null.");
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void validateEpisodeTOWithId(final EpisodeTO episode) {
        Assert.notNull(episode, "Episode mustn't be null.");
        Assert.notNull(episode.getId(), "ID mustn't be null.");
    }

    /**
     * Validates episode.
     *
     * @param episode validating episode
     * @throws IllegalArgumentException if episode is null
     *                                  or number of episode isn't positive number
     *                                  or name is null
     *                                  or name is empty string
     *                                  or length of episode is negative value
     *                                  or note is null
     */
    private static void validateEpisodeTO(final EpisodeTO episode) {
        Assert.notNull(episode, "Episode mustn't be null.");
        Assert.isTrue(episode.getNumber() > 0, "Number of episode must be positive number.");
        Assert.notNull(episode.getName(), "Name mustn't be null");
        Assert.isTrue(!StringUtils.isEmpty(episode.getName()) && !StringUtils.isEmpty(episode.getName().trim()), "Name mustn't be empty string.");
        Assert.isTrue(episode.getLength() >= 0, "Length mustn't be negative number.");
        Assert.notNull(episode.getNote(), "Note mustn't be null.");
    }

}
