package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.entity.Music;
import cz.vhromada.catalog.validator.MusicValidator;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * A class represents implementation of validator for music.
 *
 * @author Vladimir Hromada
 */
@Component("musicValidator")
public class MusicValidatorImpl implements MusicValidator {

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void validateNewMusic(final Music music) {
        validateMusic(music);
        Assert.isNull(music.getId(), "ID must be null.");
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void validateExistingMusic(final Music music) {
        validateMusic(music);
        Assert.notNull(music.getId(), "ID mustn't be null.");
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void validateMusicWithId(final Music music) {
        Assert.notNull(music, "Music mustn't be null.");
        Assert.notNull(music.getId(), "ID mustn't be null.");
    }

    /**
     * Validates music.
     *
     * @param music validating music
     * @throws IllegalArgumentException if music is null
     *                                  or name is null
     *                                  or name is empty string
     *                                  or URL to english Wikipedia page about music is null
     *                                  or URL to czech Wikipedia page about music is null
     *                                  or count of media isn't positive number
     *                                  or note is null
     */
    private static void validateMusic(final Music music) {
        Assert.notNull(music, "Music mustn't be null.");
        Assert.notNull(music.getName(), "Name mustn't be null");
        Assert.isTrue(!StringUtils.isEmpty(music.getName()) && !StringUtils.isEmpty(music.getName().trim()), "Name mustn't be empty string.");
        Assert.notNull(music.getWikiEn(), "URL to english Wikipedia page about music mustn't be null.");
        Assert.notNull(music.getWikiCz(), "URL to czech Wikipedia page about music mustn't be null.");
        Assert.isTrue(music.getMediaCount() > 0, "Count of media must be positive number.");
        Assert.notNull(music.getNote(), "Note mustn't be null.");
    }

}
