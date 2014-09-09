package cz.vhromada.catalog.service.impl;

import java.util.List;

import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.dao.entities.Song;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;

/**
 * An abstract class represents service with music cache.
 *
 * @author Vladimir Hromada
 */
public abstract class AbstractMusicService extends AbstractInnerService<Music, Song> {

	/** Cache for music */
	@Value("#{cacheManager.getCache('musicCache')}")
	private Cache musicCache;

	/**
	 * Returns cache for music.
	 *
	 * @return cache for music
	 */
	public Cache getMusicCache() {
		return musicCache;
	}

	/**
	 * Sets a new value to cache for music.
	 *
	 * @param musicCache new value
	 */
	public void setMusicCache(final Cache musicCache) {
		this.musicCache = musicCache;
	}

	/**
	 * Validates if cache for music is set.
	 *
	 * @throws IllegalStateException if cache for music is null
	 */
	protected void validateMusicCacheNotNull() {
		Validators.validateFieldNotNull(musicCache, "Cache for music");
	}

	/** Remove all mappings from the cache for music. */
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
		return getCachedObjects(musicCache, "music", cached);
	}

	/**
	 * Returns list of songs for specified music.
	 *
	 * @param music  music
	 * @param cached true if returned data from DAO should be cached
	 * @return list of songs for specified music
	 */
	protected List<Song> getCachedSongs(final Music music, final boolean cached) {
		return getCachedInnerObjects(musicCache, "songs" + music.getId(), cached, music);
	}

	/**
	 * Returns music with ID or null if there isn't such song.
	 *
	 * @param id ID
	 * @return music with ID or null if there isn't such song
	 */
	protected Music getCachedMusic(final Integer id) {
		return getCachedObject(musicCache, "music", id, true);
	}

	/**
	 * Returns song with ID or null if there isn't such song.
	 *
	 * @param id ID
	 * @return song with ID or null if there isn't such song
	 */
	protected Song getCachedSong(final Integer id) {
		return getCachedInnerObject(musicCache, "song", id);
	}

	/**
	 * Adds music to cache.
	 *
	 * @param music music
	 */
	protected void addMusicToCache(final Music music) {
		addObjectToListCache(musicCache, "music", music);
		addObjectToCache(musicCache, "music" + music.getId(), music);
	}

	/**
	 * Adds song to cache.
	 *
	 * @param song song
	 */
	protected void addSongToCache(final Song song) {
		addInnerObjectToListCache(musicCache, "songs" + song.getMusic().getId(), song);
		addInnerObjectToCache(musicCache, "song" + song.getId(), song);
	}

	/**
	 * Removes song from cache.
	 *
	 * @param song song
	 */
	protected void removeSongFromCache(final Song song) {
		removeInnerObjectFromCache(musicCache, "songs" + song.getMusic().getId(), song);
		musicCache.evict("song" + song.getId());
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
