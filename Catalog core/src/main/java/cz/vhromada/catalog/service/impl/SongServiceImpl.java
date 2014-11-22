package cz.vhromada.catalog.service.impl;

import java.util.List;

import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.SongDAO;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.dao.entities.Song;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.catalog.service.SongService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of service for songs.
 *
 * @author Vladimir Hromada
 */
@Component("songService")
public class SongServiceImpl extends AbstractMusicService implements SongService {

    /** DAO for songs field */
    private static final String SONG_DAO_FIELD = "DAO for songs";

    /** Music argument */
    private static final String MUSIC_ARGUMENT = "Music";

    /** Song argument */
    private static final String SONG_ARGUMENT = "Song";

    /** ID argument */
    private static final String ID_ARGUMENT = "ID";

    /** Message for {@link ServiceOperationException} */
    private static final String SERVICE_OPERATION_EXCEPTION_MESSAGE = "Error in working with DAO tier.";

    /** DAO for songs */
    @Autowired
    private SongDAO songDAO;

    /**
     * Returns DAO for songs.
     *
     * @return DAO for songs
     */
    public SongDAO getSongDAO() {
        return songDAO;
    }

    /**
     * Sets a new value to DAO for songs.
     *
     * @param songDAO new value
     */
    public void setSongDAO(final SongDAO songDAO) {
        this.songDAO = songDAO;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for songs isn't set
     *                                   or cache for music isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public Song getSong(final Integer id) {
        Validators.validateFieldNotNull(songDAO, SONG_DAO_FIELD);
        validateMusicCacheNotNull();
        Validators.validateArgumentNotNull(id, ID_ARGUMENT);

        try {
            return getCachedSong(id);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for songs isn't set
     *                                   or cache for music isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void add(final Song song) {
        Validators.validateFieldNotNull(songDAO, SONG_DAO_FIELD);
        validateMusicCacheNotNull();
        Validators.validateArgumentNotNull(song, SONG_ARGUMENT);

        try {
            songDAO.add(song);
            addSongToCache(song);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for songs isn't set
     *                                   or cache for music isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void update(final Song song) {
        Validators.validateFieldNotNull(songDAO, SONG_DAO_FIELD);
        validateMusicCacheNotNull();
        Validators.validateArgumentNotNull(song, SONG_ARGUMENT);

        try {
            songDAO.update(song);
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for songs isn't set
     *                                   or cache for music isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void remove(final Song song) {
        Validators.validateFieldNotNull(songDAO, SONG_DAO_FIELD);
        validateMusicCacheNotNull();
        Validators.validateArgumentNotNull(song, SONG_ARGUMENT);

        try {
            songDAO.remove(song);
            removeSongFromCache(song);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for songs isn't set
     *                                   or cache for music isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void duplicate(final Song song) {
        Validators.validateFieldNotNull(songDAO, SONG_DAO_FIELD);
        validateMusicCacheNotNull();
        Validators.validateArgumentNotNull(song, SONG_ARGUMENT);

        try {
            final Song newSong = new Song();
            newSong.setName(song.getName());
            newSong.setLength(song.getLength());
            newSong.setNote(song.getNote());
            newSong.setMusic(song.getMusic());
            songDAO.add(newSong);
            newSong.setPosition(song.getPosition());
            songDAO.update(newSong);
            addSongToCache(newSong);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for songs isn't set
     *                                   or cache for music isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void moveUp(final Song song) {
        Validators.validateFieldNotNull(songDAO, SONG_DAO_FIELD);
        validateMusicCacheNotNull();
        Validators.validateArgumentNotNull(song, SONG_ARGUMENT);

        try {
            final List<Song> songs = getCachedSongs(song.getMusic(), false);
            final Song otherSong = songs.get(songs.indexOf(song) - 1);
            switchPosition(song, otherSong);
            songDAO.update(song);
            songDAO.update(otherSong);
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for songs isn't set
     *                                   or cache for music isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void moveDown(final Song song) {
        Validators.validateFieldNotNull(songDAO, SONG_DAO_FIELD);
        validateMusicCacheNotNull();
        Validators.validateArgumentNotNull(song, SONG_ARGUMENT);

        try {
            final List<Song> songs = getCachedSongs(song.getMusic(), false);
            final Song otherSong = songs.get(songs.indexOf(song) + 1);
            switchPosition(song, otherSong);
            songDAO.update(song);
            songDAO.update(otherSong);
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }


    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for songs isn't set
     *                                   or cache for music isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public boolean exists(final Song song) {
        Validators.validateFieldNotNull(songDAO, SONG_DAO_FIELD);
        validateMusicCacheNotNull();
        Validators.validateArgumentNotNull(song, SONG_ARGUMENT);

        try {
            return getCachedSong(song.getId()) != null;
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for songs isn't set
     *                                   or cache for music isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public List<Song> findSongsByMusic(final Music music) {
        Validators.validateFieldNotNull(songDAO, SONG_DAO_FIELD);
        validateMusicCacheNotNull();
        Validators.validateArgumentNotNull(music, MUSIC_ARGUMENT);

        try {
            return getCachedSongs(music, true);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for songs isn't set
     *                                   or cache for music isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public Time getTotalLengthByMusic(final Music music) {
        Validators.validateFieldNotNull(songDAO, SONG_DAO_FIELD);
        validateMusicCacheNotNull();
        Validators.validateArgumentNotNull(music, MUSIC_ARGUMENT);

        try {
            int sum = 0;
            for (final Song song : getCachedSongs(music, true)) {
                sum += song.getLength();
            }
            return new Time(sum);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    @Override
    protected List<Music> getDAOMusic() {
        return null;
    }

    @Override
    protected List<Song> getDAOSongs(final Music music) {
        return songDAO.findSongsByMusic(music);
    }

    @Override
    protected Music getDAOMusic(final Integer id) {
        return null;
    }

    @Override
    protected Song getDAOSong(final Integer id) {
        return songDAO.getSong(id);
    }

    /**
     * Switch position of songs.
     *
     * @param song1 1st song
     * @param song2 2nd song
     */
    private static void switchPosition(final Song song1, final Song song2) {
        final int position = song1.getPosition();
        song1.setPosition(song2.getPosition());
        song2.setPosition(position);
    }

}

