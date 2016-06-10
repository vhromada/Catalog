package cz.vhromada.catalog.facade;

import java.util.List;

import cz.vhromada.catalog.facade.to.MusicTO;
import cz.vhromada.catalog.facade.to.SongTO;

/**
 * An interface represents facade for songs.
 *
 * @author Vladimir Hromada
 */
public interface SongFacade {

    /**
     * Returns TO for song with ID or null if there isn't such TO for song.
     *
     * @param id ID
     * @return TO for song with ID or null if there isn't such TO for song
     * @throws IllegalArgumentException                                       if ID is null
     */
    SongTO getSong(Integer id);

    /**
     * Adds TO for song. Sets new ID and position.
     *
     * @param music TO for music
     * @param song TO for song
     * @throws IllegalArgumentException                                       if TO for music is null
     *                                                                        or TO for song is null
     * @throws cz.vhromada.validators.exceptions.ValidationException          if ID of music is null
     *                                                                        or ID of song isn't null
     *                                                                        or name is null
     *                                                                        or name is empty string
     *                                                                        or length of song is negative value
     *                                                                        or note is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException      if TO for music doesn't exist in data storage
     */
    void add(MusicTO music, SongTO song);

    /**
     * Updates TO for song.
     *
     * @param song new value of TO for song
     * @throws IllegalArgumentException                                       if TO for song is null
     * @throws cz.vhromada.validators.exceptions.ValidationException          if ID is null
     *                                                                        or name is null
     *                                                                        or name is empty string
     *                                                                        or length of song is negative value
     *                                                                        or note is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException      if TO for song doesn't exist in data storage
     */
    void update(SongTO song);

    /**
     * Removes TO for song.
     *
     * @param song TO for song
     * @throws IllegalArgumentException                                       if TO for song is null
     * @throws cz.vhromada.validators.exceptions.ValidationException          if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException      if TO for song doesn't exist in data storage
     */
    void remove(SongTO song);

    /**
     * Duplicates TO for song.
     *
     * @param song TO for song
     * @throws IllegalArgumentException                                       if TO for song is null
     * @throws cz.vhromada.validators.exceptions.ValidationException          if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException      if TO for song doesn't exist in data storage
     */
    void duplicate(SongTO song);

    /**
     * Moves TO for song in list one position up.
     *
     * @param song TO for song
     * @throws IllegalArgumentException                                       if TO for song is null
     * @throws cz.vhromada.validators.exceptions.ValidationException          if ID is null
     *                                                                        or TO for song can't be moved up
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException      if TO for song doesn't exist in data storage
     */
    void moveUp(SongTO song);

    /**
     * Moves TO for song in list one position down.
     *
     * @param song TO for song
     * @throws IllegalArgumentException                                       if TO for song is null
     * @throws cz.vhromada.validators.exceptions.ValidationException          if ID is null
     *                                                                        or TO for song can't be moved down
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException      if TO for song doesn't exist in data storage
     */
    void moveDown(SongTO song);

    /**
     * Returns list of TO for songs for specified TO for music.
     *
     * @param music TO for music
     * @return list of TO for songs for specified TO for music
     * @throws IllegalArgumentException                                       if TO for music is null
     * @throws cz.vhromada.validators.exceptions.ValidationException          if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException      if TO for music doesn't exist in data storage
     */
    List<SongTO> findSongsByMusic(MusicTO music);

}
