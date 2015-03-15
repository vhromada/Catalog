package cz.vhromada.catalog.service.impl;

import java.util.List;

import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.dao.entities.Song;
import cz.vhromada.validators.Validators;

import org.springframework.cache.Cache;

/**
 * An abstract class represents service with music cache.
 *
 * @author Vladimir Hromada
 */
public abstract class AbstractMusicService extends AbstractInnerService<Music, Song> {

    /**
     * Cache for music argument
     */
    private static final String MUSIC_CACHE_ARGUMENT = "Cache for music";

    /**
     * Cache key for list of music
     */
    private static final String MUSIC_LIST_CACHE_KEY = "music";

    /**
     * Cache key for music
     */
    private static final String MUSIC_CACHE_KEY = "musicItem";

    /**
     * Cache key for list of songs
     */
    private static final String SONGS_CACHE_KEY = "songs";

    /**
     * Cache key for song
     */
    private static final String SONG_CACHE_KEY = "song";

    /**
     * Cache for music
     */
    private Cache musicCache;

    /**
     * Creates a new instance of AbstractMusicService.
     *
     * @param musicCache cache for music
     * @throws IllegalArgumentException if cache for music is null
     */
    public AbstractMusicService(final Cache musicCache) {
        Validators.validateArgumentNotNull(musicCache, MUSIC_CACHE_ARGUMENT);

        this.musicCache = musicCache;
    }

    /**
     * Remove all mappings from the cache for music.
     */
    protected void clearCache() {
        musicCache.clear();
    }

    /**
     * Returns list of music.
     *
     * @param cached true if returned data from DAO should be cached
     * @return list of categories
     */
    protected List<Music> getCachedMusic(final boolean cached) {
        return getCachedObjects(musicCache, MUSIC_LIST_CACHE_KEY, cached);
    }

    /**
     * Returns list of songs for specified music.
     *
     * @param music  music
     * @param cached true if returned data from DAO should be cached
     * @return list of songs for specified music
     */
    protected List<Song> getCachedSongs(final Music music, final boolean cached) {
        return getCachedInnerObjects(musicCache, SONGS_CACHE_KEY + music.getId(), cached, music);
    }

    /**
     * Returns music with ID or null if there isn't such song.
     *
     * @param id ID
     * @return music with ID or null if there isn't such song
     */
    protected Music getCachedMusic(final Integer id) {
        return getCachedObject(musicCache, MUSIC_CACHE_KEY, id, true);
    }

    /**
     * Returns song with ID or null if there isn't such song.
     *
     * @param id ID
     * @return song with ID or null if there isn't such song
     */
    protected Song getCachedSong(final Integer id) {
        return getCachedInnerObject(musicCache, SONG_CACHE_KEY, id);
    }

    /**
     * Adds music to cache.
     *
     * @param music music
     */
    protected void addMusicToCache(final Music music) {
        addObjectToListCache(musicCache, MUSIC_LIST_CACHE_KEY, music);
        addObjectToCache(musicCache, MUSIC_CACHE_KEY + music.getId(), music);
    }

    /**
     * Adds song to cache.
     *
     * @param song song
     */
    protected void addSongToCache(final Song song) {
        addInnerObjectToListCache(musicCache, SONGS_CACHE_KEY + song.getMusic().getId(), song);
        addInnerObjectToCache(musicCache, SONG_CACHE_KEY + song.getId(), song);
    }

    /**
     * Removes song from cache.
     *
     * @param song song
     */
    protected void removeSongFromCache(final Song song) {
        removeInnerObjectFromCache(musicCache, SONGS_CACHE_KEY + song.getMusic().getId(), song);
        musicCache.evict(SONG_CACHE_KEY + song.getId());
    }

    @Override
    protected List<Music> getData() {
        return getDAOMusic();
    }

    @Override
    protected Music getData(final Integer id) {
        return getDAOMusic(id);
    }

    @Override
    protected List<Song> getInnerData(final Music parent) {
        return getDAOSongs(parent);
    }

    @Override
    protected Song getInnerData(final Integer id) {
        return getDAOSong(id);
    }

    /**
     * Returns list of music from DAO tier.
     *
     * @return list of music from DAO tier
     */
    protected abstract List<Music> getDAOMusic();

    /**
     * Returns list of songs for specified music from DAO tier.
     *
     * @param music music
     * @return list of songs for specified music from DAO tier
     */
    protected abstract List<Song> getDAOSongs(final Music music);

    /**
     * Returns music with ID from DAO tier.
     *
     * @param id ID
     * @return music with ID from DAO tier
     */
    protected abstract Music getDAOMusic(final Integer id);

    /**
     * Returns song with ID from DAO tier.
     *
     * @param id ID
     * @return song with ID from DAO tier
     */
    protected abstract Song getDAOSong(final Integer id);

}
