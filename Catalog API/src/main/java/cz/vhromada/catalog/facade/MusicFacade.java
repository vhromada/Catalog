package cz.vhromada.catalog.facade;

import java.util.List;

import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.facade.to.MusicTO;

/**
 * An interface represents facade for music.
 *
 * @author Vladimir Hromada
 */
public interface MusicFacade {

    /**
     * Creates new data.
     *
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    void newData();

    /**
     * Returns list of TO for music.
     *
     * @return list of TO for music
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    List<MusicTO> getMusic();

    /**
     * Returns TO for music with ID or null if there isn't such TO for music.
     *
     * @param id ID
     * @return TO for music with ID or null if there isn't such TO for music
     * @throws IllegalArgumentException                                       if ID is null
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    MusicTO getMusic(Integer id);

    /**
     * Adds TO for music. Sets new ID and position.
     *
     * @param music TO for music
     * @throws IllegalArgumentException                                       if TO for music is null
     * @throws cz.vhromada.validators.exceptions.ValidationException          if ID isn't null
     *                                                                        or name is null
     *                                                                        or name is empty string
     *                                                                        or URL to english Wikipedia page about music is null
     *                                                                        or URL to czech Wikipedia page about music is null
     *                                                                        or count of media isn't positive number
     *                                                                        or note is null
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    void add(MusicTO music);

    /**
     * Updates TO for music.
     *
     * @param music new value of TO for music
     * @throws IllegalArgumentException                                       if TO for music is null
     * @throws cz.vhromada.validators.exceptions.ValidationException          if ID is null
     *                                                                        or name is null
     *                                                                        or name is empty string
     *                                                                        or URL to english Wikipedia page about music is null
     *                                                                        or URL to czech Wikipedia page about music is null
     *                                                                        or count of media isn't positive number
     *                                                                        or note is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException      if TO for music doesn't exist in data storage
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    void update(MusicTO music);

    /**
     * Removes TO for music.
     *
     * @param music TO for music
     * @throws IllegalArgumentException                                       if TO for music is null
     * @throws cz.vhromada.validators.exceptions.ValidationException          if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException      if TO for music doesn't exist in data storage
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    void remove(MusicTO music);

    /**
     * Duplicates TO for music.
     *
     * @param music TO for music
     * @throws IllegalArgumentException                                       if TO for music is null
     * @throws cz.vhromada.validators.exceptions.ValidationException          if ID is null
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException      if TO for music doesn't exist in data storage
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    void duplicate(MusicTO music);

    /**
     * Moves TO for music in list one position up.
     *
     * @param music TO for music
     * @throws IllegalArgumentException                                       if TO for music is null
     * @throws cz.vhromada.validators.exceptions.ValidationException          if ID is null
     *                                                                        or TO for music can't be moved up
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException      if TO for music doesn't exist in data storage
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    void moveUp(MusicTO music);

    /**
     * Moves TO for music in list one position down.
     *
     * @param music TO for music
     * @throws IllegalArgumentException                                       if TO for music is null
     * @throws cz.vhromada.validators.exceptions.ValidationException          if ID is null
     *                                                                        or TO for music can't be moved down
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException      if TO for music doesn't exist in data storage
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    void moveDown(MusicTO music);

    /**
     * Returns true if TO for music exists.
     *
     * @param music TO for music
     * @return true if TO for music exists
     * @throws IllegalArgumentException                                       if TO for music is null
     * @throws cz.vhromada.validators.exceptions.ValidationException          if ID is null
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    boolean exists(MusicTO music);

    /**
     * Updates positions.
     *
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    void updatePositions();

    /**
     * Returns total count of media.
     *
     * @return total count of media
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    int getTotalMediaCount();

    /**
     * Returns total length of all songs.
     *
     * @return total length of all songs
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    Time getTotalLength();

    /**
     * Returns count of songs from all music.
     *
     * @return count of songs from all music
     * @throws cz.vhromada.catalog.facade.exceptions.FacadeOperationException if there was error in working with service tier
     */
    int getSongsCount();

}
