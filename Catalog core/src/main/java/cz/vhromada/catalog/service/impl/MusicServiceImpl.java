package cz.vhromada.catalog.service.impl;

import java.util.List;

import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.MusicDAO;
import cz.vhromada.catalog.dao.SongDAO;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.dao.entities.Song;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.catalog.service.MusicService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of service for music.
 *
 * @author Vladimir Hromada
 */
@Component("musicService")
public class MusicServiceImpl extends AbstractMusicService implements MusicService {

    /** DAO for music field */
    private static final String MUSIC_DAO_ARGUMENT = "DAO for music";

    /** DAO for songs field */
    private static final String SONG_DAO_ARGUMENT = "DAO for songs";

    /** Music argument */
    private static final String MUSIC_ARGUMENT = "Music";

    /** ID argument */
    private static final String ID_ARGUMENT = "ID";

    /** Message for {@link ServiceOperationException} */
    private static final String SERVICE_OPERATION_EXCEPTION_MESSAGE = "Error in working with DAO tier.";

    /** DAO for music */
    private MusicDAO musicDAO;

    /** DAO for songs */
    private SongDAO songDAO;

    /**
     * Creates a new instance of MusicServiceImpl.
     *
     * @param musicDAO DAO for music
     * @param songDAO DAO for songs
     * @param musicCache cache for music
     * @throws IllegalArgumentException if DAO for music is null
     *                                  or DAO for songs is null
     *                                  or cache for music is null
     */
    @Autowired
    public MusicServiceImpl(final MusicDAO musicDAO,
            final SongDAO songDAO,
            @Value("#{cacheManager.getCache('musicCache')}") final Cache musicCache) {
        super(musicCache);

        Validators.validateArgumentNotNull(musicDAO, MUSIC_DAO_ARGUMENT);
        Validators.validateArgumentNotNull(songDAO, SONG_DAO_ARGUMENT);

        this.musicDAO = musicDAO;
        this.songDAO = songDAO;
    }

    /**
     * {@inheritDoc}
     *
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void newData() {
        try {
            for (final Music music : getCachedMusic(false)) {
                removeMusic(music);
            }
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public List<Music> getMusic() {
        try {
            return getCachedMusic(true);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public Music getMusic(final Integer id) {
        Validators.validateArgumentNotNull(id, ID_ARGUMENT);

        try {
            return getCachedMusic(id);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void add(final Music music) {
        Validators.validateArgumentNotNull(music, MUSIC_ARGUMENT);

        try {
            musicDAO.add(music);
            addMusicToCache(music);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void update(final Music music) {
        Validators.validateArgumentNotNull(music, MUSIC_ARGUMENT);

        try {
            musicDAO.update(music);
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void remove(final Music music) {
        Validators.validateArgumentNotNull(music, MUSIC_ARGUMENT);

        try {
            removeMusic(music);
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void duplicate(final Music music) {
        Validators.validateArgumentNotNull(music, MUSIC_ARGUMENT);

        try {
            final Music newMusic = new Music();
            newMusic.setName(music.getName());
            newMusic.setWikiEn(music.getWikiEn());
            newMusic.setWikiCz(music.getWikiCz());
            newMusic.setMediaCount(music.getMediaCount());
            newMusic.setNote(music.getNote());
            musicDAO.add(newMusic);
            newMusic.setPosition(music.getPosition());
            musicDAO.update(newMusic);

            for (final Song song : getCachedSongs(music, false)) {
                final Song newSong = new Song();
                newSong.setName(song.getName());
                newSong.setLength(song.getLength());
                newSong.setNote(song.getNote());
                newSong.setMusic(newMusic);
                songDAO.add(newSong);
                newSong.setPosition(song.getPosition());
                songDAO.update(newSong);
            }
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void moveUp(final Music music) {
        Validators.validateArgumentNotNull(music, MUSIC_ARGUMENT);

        try {
            final List<Music> musicList = getCachedMusic(false);
            final Music otherMusic = musicList.get(musicList.indexOf(music) - 1);
            switchPosition(music, otherMusic);
            musicDAO.update(music);
            musicDAO.update(otherMusic);
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void moveDown(final Music music) {
        Validators.validateArgumentNotNull(music, MUSIC_ARGUMENT);

        try {
            final List<Music> musicList = getCachedMusic(false);
            final Music otherMusic = musicList.get(musicList.indexOf(music) + 1);
            switchPosition(music, otherMusic);
            musicDAO.update(music);
            musicDAO.update(otherMusic);
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public boolean exists(final Music music) {
        Validators.validateArgumentNotNull(music, MUSIC_ARGUMENT);

        try {
            return getCachedMusic(music.getId()) != null;
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void updatePositions() {
        try {
            final List<Music> musicList = getCachedMusic(false);
            for (int i = 0; i < musicList.size(); i++) {
                final Music music = musicList.get(i);
                music.setPosition(i);
                musicDAO.update(music);
                final List<Song> songs = getCachedSongs(music, false);
                for (int j = 0; j < songs.size(); j++) {
                    final Song song = songs.get(j);
                    song.setPosition(j);
                    songDAO.update(song);
                }
            }
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public int getTotalMediaCount() {
        try {
            int sum = 0;
            for (final Music music : getCachedMusic(true)) {
                sum += music.getMediaCount();
            }
            return sum;
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public Time getTotalLength() {
        try {
            int sum = 0;
            for (final Music music : getCachedMusic(true)) {
                for (final Song song : getCachedSongs(music, true)) {
                    sum += song.getLength();
                }
            }
            return new Time(sum);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public int getSongsCount() {
        try {
            int sum = 0;
            for (final Music music : getCachedMusic(true)) {
                sum += getCachedSongs(music, true).size();
            }
            return sum;
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    @Override
    protected List<Music> getDAOMusic() {
        return musicDAO.getMusic();
    }

    @Override
    protected List<Song> getDAOSongs(final Music music) {
        return songDAO.findSongsByMusic(music);
    }

    @Override
    protected Music getDAOMusic(final Integer id) {
        return musicDAO.getMusic(id);
    }

    @Override
    protected Song getDAOSong(final Integer id) {
        return songDAO.getSong(id);
    }

    /**
     * Removes music.
     *
     * @param music music
     */
    private void removeMusic(final Music music) {
        for (final Song song : getCachedSongs(music, false)) {
            songDAO.remove(song);
        }
        musicDAO.remove(music);
    }

    /**
     * Switch position of music.
     *
     * @param music1 1st music
     * @param music2 2nd music
     */
    private static void switchPosition(final Music music1, final Music music2) {
        final int position = music1.getPosition();
        music1.setPosition(music2.getPosition());
        music2.setPosition(position);
    }

}
