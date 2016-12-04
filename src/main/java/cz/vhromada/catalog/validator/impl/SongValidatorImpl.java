package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.entity.Song;
import cz.vhromada.catalog.validator.SongValidator;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * A class represents implementation of validator for song.
 *
 * @author Vladimir Hromada
 */
@Component("songValidator")
public class SongValidatorImpl implements SongValidator {

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void validateNewSong(final Song song) {
        validateSong(song);
        Assert.isNull(song.getId(), "ID must be null.");
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void validateExistingSong(final Song song) {
        validateSong(song);
        Assert.notNull(song.getId(), "ID mustn't be null.");
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void validateSongWithId(final Song song) {
        Assert.notNull(song, "Song mustn't be null.");
        Assert.notNull(song.getId(), "ID mustn't be null.");
    }

    /**
     * Validates song.
     *
     * @param song validating song
     * @throws IllegalArgumentException if song is null
     *                                  or number of song isn't positive number
     *                                  or name is null
     *                                  or name is empty string
     *                                  or length of song is negative value
     *                                  or note is null
     */
    private static void validateSong(final Song song) {
        Assert.notNull(song, "Song mustn't be null.");
        Assert.notNull(song.getName(), "Name mustn't be null");
        Assert.isTrue(!StringUtils.isEmpty(song.getName()) && !StringUtils.isEmpty(song.getName().trim()), "Name mustn't be empty string.");
        Assert.isTrue(song.getLength() >= 0, "Length mustn't be negative number.");
        Assert.notNull(song.getNote(), "Note mustn't be null.");
    }

}
