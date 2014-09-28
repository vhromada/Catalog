package cz.vhromada.catalog.service.impl;

import static cz.vhromada.catalog.commons.TestConstants.INNER_ID;
import static cz.vhromada.catalog.commons.TestConstants.MOVE_POSITION;
import static cz.vhromada.catalog.commons.TestConstants.POSITION;
import static cz.vhromada.catalog.commons.TestConstants.PRIMARY_ID;
import static cz.vhromada.catalog.commons.TestConstants.SECONDARY_ID;
import static cz.vhromada.catalog.commons.TestConstants.SECONDARY_INNER_ID;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.EntityGenerator;
import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.MusicDAO;
import cz.vhromada.catalog.dao.SongDAO;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.dao.entities.Song;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.catalog.service.MusicService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

/**
 * A class represents test for class {@link MusicServiceImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class MusicServiceImplTest {

	/** Instance of {@link MusicDAO} */
	@Mock
	private MusicDAO musicDAO;

	/** Instance of {@link SongDAO} */
	@Mock
	private SongDAO songDAO;

	/** Instance of {@link Cache} */
	@Mock
	private Cache musicCache;

	/** Instance of {@link MusicService} */
	@InjectMocks
	private MusicService musicService = new MusicServiceImpl();

	/** Test method for {@link MusicServiceImpl#getMusicDAO()} and {@link MusicServiceImpl#setMusicDAO(MusicDAO)}. */
	@Test
	public void testMusicDAO() {
		final MusicServiceImpl musicService = new MusicServiceImpl();
		musicService.setMusicDAO(musicDAO);
		DeepAsserts.assertEquals(musicDAO, musicService.getMusicDAO());
	}

	/** Test method for {@link MusicServiceImpl#getSongDAO()} and {@link MusicServiceImpl#setSongDAO(SongDAO)}. */
	@Test
	public void testSongDAO() {
		final MusicServiceImpl musicService = new MusicServiceImpl();
		musicService.setSongDAO(songDAO);
		DeepAsserts.assertEquals(songDAO, musicService.getSongDAO());
	}

	/** Test method for {@link MusicServiceImpl#getMusicCache()} and {@link MusicServiceImpl#setMusicCache(Cache)}. */
	@Test
	public void testMusicCache() {
		final MusicServiceImpl musicService = new MusicServiceImpl();
		musicService.setMusicCache(musicCache);
		DeepAsserts.assertEquals(musicCache, musicService.getMusicCache());
	}

	/** Test method for {@link MusicService#newData()} with cached data. */
	@Test
	public void testNewDataWithCachedData() {
		final List<Music> musicList = CollectionUtils.newList(EntityGenerator.createMusic(PRIMARY_ID), EntityGenerator.createMusic(SECONDARY_ID));
		final List<Song> songs = CollectionUtils.newList(mock(Song.class), mock(Song.class));
		when(musicCache.get("music")).thenReturn(new SimpleValueWrapper(musicList));
		when(musicCache.get("songs" + PRIMARY_ID)).thenReturn(new SimpleValueWrapper(songs));
		when(musicCache.get("songs" + SECONDARY_ID)).thenReturn(new SimpleValueWrapper(songs));

		musicService.newData();

		for (Music music : musicList) {
			verify(musicDAO).remove(music);
			verify(musicCache).get("songs" + music.getId());
		}
		for (Song song : songs) {
			verify(songDAO, times(musicList.size())).remove(song);
		}
		verify(musicCache).get("music");
		verify(musicCache).clear();
		verifyNoMoreInteractions(musicDAO, songDAO, musicCache);
	}

	/** Test method for {@link MusicService#newData()} with not cached data. */
	@Test
	public void testNewDataWithNotCachedData() {
		final List<Music> musicList = CollectionUtils.newList(EntityGenerator.createMusic(PRIMARY_ID), EntityGenerator.createMusic(SECONDARY_ID));
		final List<Song> songs = CollectionUtils.newList(mock(Song.class), mock(Song.class));
		when(musicDAO.getMusic()).thenReturn(musicList);
		when(songDAO.findSongsByMusic(any(Music.class))).thenReturn(songs);
		when(musicCache.get(anyString())).thenReturn(null);

		musicService.newData();

		verify(musicDAO).getMusic();
		for (Music music : musicList) {
			verify(musicDAO).remove(music);
			verify(songDAO).findSongsByMusic(music);
			verify(musicCache).get("songs" + music.getId());
		}
		for (Song song : songs) {
			verify(songDAO, times(musicList.size())).remove(song);
		}
		verify(musicCache).get("music");
		verify(musicCache).clear();
		verifyNoMoreInteractions(musicDAO, songDAO, musicCache);
	}

	/** Test method for {@link MusicService#newData()} with not set DAO for music. */
	@Test(expected = IllegalStateException.class)
	public void testNewDataWithNotSetMusicDAO() {
		((MusicServiceImpl) musicService).setMusicDAO(null);
		musicService.newData();
	}

	/** Test method for {@link MusicService#newData()} with not set DAO for songs. */
	@Test(expected = IllegalStateException.class)
	public void testNewDataWithNotSetSongDAO() {
		((MusicServiceImpl) musicService).setSongDAO(null);
		musicService.newData();
	}

	/** Test method for {@link MusicService#newData()} with not set cache for music. */
	@Test(expected = IllegalStateException.class)
	public void testNewDataWithNotSetMusicCache() {
		((MusicServiceImpl) musicService).setMusicCache(null);
		musicService.newData();
	}

	/** Test method for {@link MusicService#newData()} with exception in DAO tier. */
	@Test
	public void testNewDataWithDAOTierException() {
		doThrow(DataStorageException.class).when(musicDAO).getMusic();
		when(musicCache.get(anyString())).thenReturn(null);

		try {
			musicService.newData();
			fail("Can't create new data with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(musicDAO).getMusic();
		verify(musicCache).get("music");
		verifyNoMoreInteractions(musicDAO, musicCache);
		verifyZeroInteractions(songDAO);
	}

	/** Test method for {@link MusicService#getMusic()} with cached music. */
	@Test
	public void testGetMusicWithCachedMusic() {
		final List<Music> music = CollectionUtils.newList(mock(Music.class), mock(Music.class));
		when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(music));

		DeepAsserts.assertEquals(music, musicService.getMusic());

		verify(musicCache).get("music");
		verifyNoMoreInteractions(musicCache);
		verifyZeroInteractions(musicDAO);
	}

	/** Test method for {@link MusicService#getMusic()} with not cached music. */
	@Test
	public void testGetMusicWithNotCachedMusic() {
		final List<Music> music = CollectionUtils.newList(mock(Music.class), mock(Music.class));
		when(musicDAO.getMusic()).thenReturn(music);
		when(musicCache.get(anyString())).thenReturn(null);

		DeepAsserts.assertEquals(music, musicService.getMusic());

		verify(musicDAO).getMusic();
		verify(musicCache).get("music");
		verify(musicCache).put("music", music);
		verifyNoMoreInteractions(musicDAO, musicCache);
	}

	/** Test method for {@link MusicService#getMusic()} with not set DAO for music. */
	@Test(expected = IllegalStateException.class)
	public void testGetMusicWithNotSetMusicDAO() {
		((MusicServiceImpl) musicService).setMusicDAO(null);
		musicService.getMusic();
	}

	/** Test method for {@link MusicService#getMusic()} with not set cache for music. */
	@Test(expected = IllegalStateException.class)
	public void testGetMusicWithNotSetMusicCache() {
		((MusicServiceImpl) musicService).setMusicCache(null);
		musicService.getMusic();
	}

	/** Test method for {@link MusicService#getMusic()} with exception in DAO tier. */
	@Test
	public void testGetMusicWithDAOTierException() {
		doThrow(DataStorageException.class).when(musicDAO).getMusic();
		when(musicCache.get(anyString())).thenReturn(null);

		try {
			musicService.getMusic();
			fail("Can't get music with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(musicDAO).getMusic();
		verify(musicCache).get("music");
		verifyNoMoreInteractions(musicDAO, musicCache);
	}

	/** Test method for {@link MusicService#getMusic(Integer)} with cached existing music. */
	@Test
	public void testGetMusicByIdWithCachedExistingMusic() {
		final Music music = EntityGenerator.createMusic(PRIMARY_ID);
		when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(music));

		DeepAsserts.assertEquals(music, musicService.getMusic(PRIMARY_ID));

		verify(musicCache).get("music" + PRIMARY_ID);
		verifyNoMoreInteractions(musicCache);
		verifyZeroInteractions(musicDAO);
	}

	/** Test method for {@link MusicService#getMusic(Integer)} with cached not existing music. */
	@Test
	public void testGetMusicByIdWithCachedNotExistingMusic() {
		when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

		assertNull(musicService.getMusic(PRIMARY_ID));

		verify(musicCache).get("music" + PRIMARY_ID);
		verifyNoMoreInteractions(musicCache);
		verifyZeroInteractions(musicDAO);
	}

	/** Test method for {@link MusicService#getMusic(Integer)} with not cached existing music. */
	@Test
	public void testGetMusicByIdWithNotCachedExistingMusic() {
		final Music music = EntityGenerator.createMusic(PRIMARY_ID);
		when(musicDAO.getMusic(anyInt())).thenReturn(music);
		when(musicCache.get(anyString())).thenReturn(null);

		DeepAsserts.assertEquals(music, musicService.getMusic(PRIMARY_ID));

		verify(musicDAO).getMusic(PRIMARY_ID);
		verify(musicCache).get("music" + PRIMARY_ID);
		verify(musicCache).put("music" + PRIMARY_ID, music);
		verifyNoMoreInteractions(musicDAO, musicCache);
	}

	/** Test method for {@link MusicService#getMusic(Integer)} with not cached not existing music. */
	@Test
	public void testGetMusicByIdWithNotCachedNotExistingMusic() {
		when(musicDAO.getMusic(anyInt())).thenReturn(null);
		when(musicCache.get(anyString())).thenReturn(null);

		assertNull(musicService.getMusic(PRIMARY_ID));

		verify(musicDAO).getMusic(PRIMARY_ID);
		verify(musicCache).get("music" + PRIMARY_ID);
		verify(musicCache).put("music" + PRIMARY_ID, null);
		verifyNoMoreInteractions(musicDAO, musicCache);
	}

	/** Test method for {@link MusicService#getMusic(Integer)} with not set DAO for music. */
	@Test(expected = IllegalStateException.class)
	public void testGetMusicByIdWithNotSetMusicDAO() {
		((MusicServiceImpl) musicService).setMusicDAO(null);
		musicService.getMusic(Integer.MAX_VALUE);
	}

	/** Test method for {@link MusicService#getMusic(Integer)} with not set cache for music. */
	@Test(expected = IllegalStateException.class)
	public void testGetMusicByIdWithNotSetMusicCache() {
		((MusicServiceImpl) musicService).setMusicCache(null);
		musicService.getMusic(Integer.MAX_VALUE);
	}

	/** Test method for {@link MusicService#getMusic(Integer)} with null argument. */
	@Test
	public void testGetMusicByIdWithNullArgument() {
		try {
			musicService.getMusic(null);
			fail("Can't get music with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(musicDAO, musicCache);
	}

	/** Test method for {@link MusicService#getMusic(Integer)} with exception in DAO tier. */
	@Test
	public void testGetMusicByIdWithDAOTierException() {
		doThrow(DataStorageException.class).when(musicDAO).getMusic(anyInt());
		when(musicCache.get(anyString())).thenReturn(null);

		try {
			musicService.getMusic(Integer.MAX_VALUE);
			fail("Can't get music with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(musicDAO).getMusic(Integer.MAX_VALUE);
		verify(musicCache).get("music" + Integer.MAX_VALUE);
		verifyNoMoreInteractions(musicDAO, musicCache);
	}

	/** Test method for {@link MusicService#add(Music)} with cached music. */
	@Test
	public void testAddWithCachedMusic() {
		final Music music = EntityGenerator.createMusic();
		final List<Music> musicList = CollectionUtils.newList(mock(Music.class), mock(Music.class));
		final List<Music> addedMusicList = new ArrayList<>(musicList);
		addedMusicList.add(music);
		when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(musicList));

		musicService.add(music);

		verify(musicDAO).add(music);
		verify(musicCache).get("music");
		verify(musicCache).get("music" + null);
		verify(musicCache).put("music", addedMusicList);
		verify(musicCache).put("music" + null, music);
		verifyNoMoreInteractions(musicDAO, musicCache);
	}

	/** Test method for {@link MusicService#add(Music)} with not cached music. */
	@Test
	public void testAddWithNotCachedMusic() {
		final Music music = EntityGenerator.createMusic();
		when(musicCache.get(anyString())).thenReturn(null);

		musicService.add(music);

		verify(musicDAO).add(music);
		verify(musicCache).get("music" + null);
		verify(musicCache).get("music");
		verifyNoMoreInteractions(musicDAO, musicCache);
	}

	/** Test method for {@link MusicService#add(Music)} with not set DAO for music. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetMusicDAO() {
		((MusicServiceImpl) musicService).setMusicDAO(null);
		musicService.add(mock(Music.class));
	}

	/** Test method for {@link MusicService#add(Music)} with not set cache for music. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetMusicCache() {
		((MusicServiceImpl) musicService).setMusicCache(null);
		musicService.add(mock(Music.class));
	}

	/** Test method for {@link MusicService#add(Music)} with null argument. */
	@Test
	public void testAddWithNullArgument() {
		try {
			musicService.add(null);
			fail("Can't add music with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(musicDAO, musicCache);
	}

	/** Test method for {@link MusicService#add(Music)} with exception in DAO tier. */
	@Test
	public void testAddWithDAOTierException() {
		final Music music = EntityGenerator.createMusic();
		doThrow(DataStorageException.class).when(musicDAO).add(any(Music.class));

		try {
			musicService.add(music);
			fail("Can't add music with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(musicDAO).add(music);
		verifyNoMoreInteractions(musicDAO);
		verifyZeroInteractions(musicCache);
	}

	/** Test method for {@link MusicService#update(Music)}. */
	@Test
	public void testUpdate() {
		final Music music = EntityGenerator.createMusic(PRIMARY_ID);

		musicService.update(music);

		verify(musicDAO).update(music);
		verify(musicCache).clear();
		verifyNoMoreInteractions(musicDAO, musicCache);
	}

	/** Test method for {@link MusicService#update(Music)} with not set DAO for music. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetMusicDAO() {
		((MusicServiceImpl) musicService).setMusicDAO(null);
		musicService.update(mock(Music.class));
	}

	/** Test method for {@link MusicService#update(Music)} with not set cache for music. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetMusicCache() {
		((MusicServiceImpl) musicService).setMusicCache(null);
		musicService.update(mock(Music.class));
	}

	/** Test method for {@link MusicService#update(Music)} with null argument. */
	@Test
	public void testUpdateWithNullArgument() {
		try {
			musicService.update(null);
			fail("Can't update music with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(musicDAO, musicCache);
	}

	/** Test method for {@link MusicService#update(Music)} with exception in DAO tier. */
	@Test
	public void testUpdateWithDAOTierException() {
		final Music music = EntityGenerator.createMusic(Integer.MAX_VALUE);
		doThrow(DataStorageException.class).when(musicDAO).update(any(Music.class));

		try {
			musicService.update(music);
			fail("Can't update music with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(musicDAO).update(music);
		verifyNoMoreInteractions(musicDAO);
		verifyZeroInteractions(musicCache);
	}

	/** Test method for {@link MusicService#remove(Music)} with cached songs. */
	@Test
	public void testRemoveWithCachedSongs() {
		final Music music = EntityGenerator.createMusic(PRIMARY_ID);
		final List<Song> songs = CollectionUtils.newList(mock(Song.class), mock(Song.class));
		when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(songs));

		musicService.remove(music);

		verify(musicDAO).remove(music);
		for (Song song : songs) {
			verify(songDAO).remove(song);
		}
		verify(musicCache).get("songs" + PRIMARY_ID);
		verify(musicCache).clear();
		verifyNoMoreInteractions(musicDAO, songDAO, musicCache);
	}

	/** Test method for {@link MusicService#remove(Music)} with not cached songs. */
	@Test
	public void testRemoveWithNotCachedSongs() {
		final Music music = EntityGenerator.createMusic(PRIMARY_ID);
		final List<Song> songs = CollectionUtils.newList(mock(Song.class), mock(Song.class));
		when(songDAO.findSongsByMusic(any(Music.class))).thenReturn(songs);
		when(musicCache.get(anyString())).thenReturn(null);

		musicService.remove(music);

		verify(musicDAO).remove(music);
		verify(songDAO).findSongsByMusic(music);
		for (Song song : songs) {
			verify(songDAO).remove(song);
		}
		verify(musicCache).get("songs" + PRIMARY_ID);
		verify(musicCache).clear();
		verifyNoMoreInteractions(musicDAO, songDAO, musicCache);
	}

	/** Test method for {@link MusicService#remove(Music)} with not set DAO for music. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetMusicDAO() {
		((MusicServiceImpl) musicService).setMusicDAO(null);
		musicService.remove(mock(Music.class));
	}

	/** Test method for {@link MusicService#remove(Music)} with not set DAO for songs. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetSongDAO() {
		((MusicServiceImpl) musicService).setSongDAO(null);
		musicService.remove(mock(Music.class));
	}

	/** Test method for {@link MusicService#remove(Music)} with not set cache for music. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetMusicCache() {
		((MusicServiceImpl) musicService).setMusicCache(null);
		musicService.remove(mock(Music.class));
	}

	/** Test method for {@link MusicService#remove(Music)} with null argument. */
	@Test
	public void testRemoveWithNullArgument() {
		try {
			musicService.remove(null);
			fail("Can't remove music with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(musicDAO, songDAO, musicCache);
	}

	/** Test method for {@link MusicService#remove(Music)} with exception in DAO tier. */
	@Test
	public void testRemoveWithDAOTierException() {
		final Music music = EntityGenerator.createMusic(Integer.MAX_VALUE);
		doThrow(DataStorageException.class).when(songDAO).findSongsByMusic(any(Music.class));
		when(musicCache.get(anyString())).thenReturn(null);

		try {
			musicService.remove(music);
			fail("Can't remove music with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(songDAO).findSongsByMusic(music);
		verify(musicCache).get("songs" + Integer.MAX_VALUE);
		verifyNoMoreInteractions(songDAO, musicCache);
		verifyZeroInteractions(musicDAO);
	}

	/** Test method for {@link MusicService#duplicate(Music)} with cached songs. */
	@Test
	public void testDuplicateWithCachedSongs() {
		final List<Song> songs = CollectionUtils.newList(mock(Song.class), mock(Song.class));
		when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(songs));

		musicService.duplicate(EntityGenerator.createMusic(PRIMARY_ID));

		verify(musicDAO).add(any(Music.class));
		verify(musicDAO).update(any(Music.class));
		verify(songDAO, times(songs.size())).add(any(Song.class));
		verify(songDAO, times(songs.size())).update(any(Song.class));
		verify(musicCache).get("songs" + PRIMARY_ID);
		verify(musicCache).clear();
		verifyNoMoreInteractions(musicDAO, songDAO, musicCache);
	}

	/** Test method for {@link MusicService#duplicate(Music)} with not cached songs. */
	@Test
	public void testDuplicateWithNotCachedSongs() {
		final Music music = EntityGenerator.createMusic(PRIMARY_ID);
		final List<Song> songs = CollectionUtils.newList(mock(Song.class), mock(Song.class));
		when(songDAO.findSongsByMusic(any(Music.class))).thenReturn(songs);
		when(musicCache.get(anyString())).thenReturn(null);

		musicService.duplicate(music);

		verify(musicDAO).add(any(Music.class));
		verify(musicDAO).update(any(Music.class));
		verify(songDAO).findSongsByMusic(music);
		verify(songDAO, times(songs.size())).add(any(Song.class));
		verify(songDAO, times(songs.size())).update(any(Song.class));
		verify(musicCache).get("songs" + PRIMARY_ID);
		verify(musicCache).clear();
		verifyNoMoreInteractions(musicDAO, songDAO, musicCache);
	}

	/** Test method for {@link MusicService#duplicate(Music)} with not set DAO for music. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetMusicDAO() {
		((MusicServiceImpl) musicService).setMusicDAO(null);
		musicService.duplicate(mock(Music.class));
	}

	/** Test method for {@link MusicService#duplicate(Music)} with not set DAO for songs. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetSongDAO() {
		((MusicServiceImpl) musicService).setSongDAO(null);
		musicService.duplicate(mock(Music.class));
	}

	/** Test method for {@link MusicService#duplicate(Music)} with not set cache for music. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetMusicCache() {
		((MusicServiceImpl) musicService).setMusicCache(null);
		musicService.duplicate(mock(Music.class));
	}

	/** Test method for {@link MusicService#duplicate(Music)} with null argument. */
	@Test
	public void testDuplicateWithNullArgument() {
		try {
			musicService.duplicate(null);
			fail("Can't duplicate music with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(musicDAO, songDAO, musicCache);
	}

	/** Test method for {@link MusicService#duplicate(Music)} with exception in DAO tier. */
	@Test
	public void testDuplicateWithDAOTierException() {
		doThrow(DataStorageException.class).when(musicDAO).add(any(Music.class));

		try {
			musicService.duplicate(EntityGenerator.createMusic(Integer.MAX_VALUE));
			fail("Can't duplicate music with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(musicDAO).add(any(Music.class));
		verifyNoMoreInteractions(musicDAO);
		verifyZeroInteractions(songDAO, musicCache);
	}

	/** Test method for {@link MusicService#moveUp(Music)} with cached music. */
	@Test
	public void testMoveUpWithCachedMusic() {
		final Music music1 = EntityGenerator.createMusic(PRIMARY_ID);
		music1.setPosition(MOVE_POSITION);
		final Music music2 = EntityGenerator.createMusic(SECONDARY_ID);
		final List<Music> music = CollectionUtils.newList(music1, music2);
		when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(music));

		musicService.moveUp(music2);
		DeepAsserts.assertEquals(POSITION, music1.getPosition());
		DeepAsserts.assertEquals(MOVE_POSITION, music2.getPosition());

		verify(musicDAO).update(music1);
		verify(musicDAO).update(music2);
		verify(musicCache).get("music");
		verify(musicCache).clear();
		verifyNoMoreInteractions(musicDAO, musicCache);
	}

	/** Test method for {@link MusicService#moveUp(Music)} with not cached music. */
	@Test
	public void testMoveUpWithNotCachedMusic() {
		final Music music1 = EntityGenerator.createMusic(PRIMARY_ID);
		music1.setPosition(MOVE_POSITION);
		final Music music2 = EntityGenerator.createMusic(SECONDARY_ID);
		final List<Music> music = CollectionUtils.newList(music1, music2);
		when(musicDAO.getMusic()).thenReturn(music);
		when(musicCache.get(anyString())).thenReturn(null);

		musicService.moveUp(music2);
		DeepAsserts.assertEquals(POSITION, music1.getPosition());
		DeepAsserts.assertEquals(MOVE_POSITION, music2.getPosition());

		verify(musicDAO).update(music1);
		verify(musicDAO).update(music2);
		verify(musicDAO).getMusic();
		verify(musicCache).get("music");
		verify(musicCache).clear();
		verifyNoMoreInteractions(musicDAO, musicCache);
	}

	/** Test method for {@link MusicService#moveUp(Music)} with not set DAO for music. */
	@Test(expected = IllegalStateException.class)
	public void testMoveUpWithNotSetMusicDAO() {
		((MusicServiceImpl) musicService).setMusicDAO(null);
		musicService.moveUp(mock(Music.class));
	}

	/** Test method for {@link MusicService#moveUp(Music)} with not set cache for music. */
	@Test(expected = IllegalStateException.class)
	public void testMoveUpWithNotSetSetMusicCache() {
		((MusicServiceImpl) musicService).setMusicCache(null);
		musicService.moveUp(mock(Music.class));
	}

	/** Test method for {@link MusicService#moveUp(Music)} with null argument. */
	@Test
	public void testMoveUpWithNullArgument() {
		try {
			musicService.moveUp(null);
			fail("Can't move up music with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(musicDAO, musicCache);
	}

	/** Test method for {@link MusicService#moveUp(Music)} with exception in DAO tier. */
	@Test
	public void testMoveUpWithDAOTierException() {
		doThrow(DataStorageException.class).when(musicDAO).getMusic();
		when(musicCache.get(anyString())).thenReturn(null);

		try {
			musicService.moveUp(EntityGenerator.createMusic(Integer.MAX_VALUE));
			fail("Can't move up music with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(musicDAO).getMusic();
		verify(musicCache).get("music");
		verifyNoMoreInteractions(musicDAO, musicCache);
	}

	/** Test method for {@link MusicService#moveDown(Music)} with cached music. */
	@Test
	public void testMoveDownWithCachedMusic() {
		final Music music1 = EntityGenerator.createMusic(PRIMARY_ID);
		final Music music2 = EntityGenerator.createMusic(SECONDARY_ID);
		music2.setPosition(MOVE_POSITION);
		final List<Music> music = CollectionUtils.newList(music1, music2);
		when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(music));

		musicService.moveDown(music1);
		DeepAsserts.assertEquals(MOVE_POSITION, music1.getPosition());
		DeepAsserts.assertEquals(POSITION, music2.getPosition());

		verify(musicDAO).update(music1);
		verify(musicDAO).update(music2);
		verify(musicCache).get("music");
		verify(musicCache).clear();
		verifyNoMoreInteractions(musicDAO, musicCache);
	}

	/** Test method for {@link MusicService#moveDown(Music)} with not cached music. */
	@Test
	public void testMoveDownWithNotCachedMusic() {
		final Music music1 = EntityGenerator.createMusic(PRIMARY_ID);
		final Music music2 = EntityGenerator.createMusic(SECONDARY_ID);
		music2.setPosition(MOVE_POSITION);
		final List<Music> music = CollectionUtils.newList(music1, music2);
		when(musicDAO.getMusic()).thenReturn(music);
		when(musicCache.get(anyString())).thenReturn(null);

		musicService.moveDown(music1);
		DeepAsserts.assertEquals(MOVE_POSITION, music1.getPosition());
		DeepAsserts.assertEquals(POSITION, music2.getPosition());

		verify(musicDAO).update(music1);
		verify(musicDAO).update(music2);
		verify(musicDAO).getMusic();
		verify(musicCache).get("music");
		verify(musicCache).clear();
		verifyNoMoreInteractions(musicDAO, musicCache);
	}

	/** Test method for {@link MusicService#moveDown(Music)} with not set DAO for music. */
	@Test(expected = IllegalStateException.class)
	public void testMoveDownWithNotSetMusicDAO() {
		((MusicServiceImpl) musicService).setMusicDAO(null);
		musicService.moveDown(mock(Music.class));
	}

	/** Test method for {@link MusicService#moveDown(Music)} with not set cache for music. */
	@Test(expected = IllegalStateException.class)
	public void testMoveDownWithNotSetSetMusicCache() {
		((MusicServiceImpl) musicService).setMusicCache(null);
		musicService.moveDown(mock(Music.class));
	}

	/** Test method for {@link MusicService#moveDown(Music)} with null argument. */
	@Test
	public void testMoveDownWithNullArgument() {
		try {
			musicService.moveDown(null);
			fail("Can't move down music with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(musicDAO, musicCache);
	}

	/** Test method for {@link MusicService#moveDown(Music)} with exception in DAO tier. */
	@Test
	public void testMoveDownWithDAOTierException() {
		doThrow(DataStorageException.class).when(musicDAO).getMusic();
		when(musicCache.get(anyString())).thenReturn(null);

		try {
			musicService.moveDown(EntityGenerator.createMusic(Integer.MAX_VALUE));
			fail("Can't move down music with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(musicDAO).getMusic();
		verify(musicCache).get("music");
		verifyNoMoreInteractions(musicDAO, musicCache);
	}

	/** Test method for {@link MusicService#exists(Music)} with cached existing music. */
	@Test
	public void testExistsWithCachedExistingMusic() {
		final Music music = EntityGenerator.createMusic(PRIMARY_ID);
		when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(music));

		assertTrue(musicService.exists(music));

		verify(musicCache).get("music" + PRIMARY_ID);
		verifyNoMoreInteractions(musicCache);
		verifyZeroInteractions(musicDAO);
	}

	/** Test method for {@link MusicService#exists(Music)} with cached not existing music. */
	@Test
	public void testExistsWithCachedNotExistingMusic() {
		final Music music = EntityGenerator.createMusic(Integer.MAX_VALUE);
		when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

		assertFalse(musicService.exists(music));

		verify(musicCache).get("music" + Integer.MAX_VALUE);
		verifyNoMoreInteractions(musicCache);
		verifyZeroInteractions(musicDAO);
	}

	/** Test method for {@link MusicService#exists(Music)} with not cached existing music. */
	@Test
	public void testExistsWithNotCachedExistingMusic() {
		final Music music = EntityGenerator.createMusic(PRIMARY_ID);
		when(musicDAO.getMusic(anyInt())).thenReturn(music);
		when(musicCache.get(anyString())).thenReturn(null);

		assertTrue(musicService.exists(music));

		verify(musicDAO).getMusic(PRIMARY_ID);
		verify(musicCache).get("music" + PRIMARY_ID);
		verify(musicCache).put("music" + PRIMARY_ID, music);
		verifyNoMoreInteractions(musicDAO, musicCache);
	}

	/** Test method for {@link MusicService#exists(Music)} with not cached not existing music. */
	@Test
	public void testExistsWithNotCachedNotExistingMusic() {
		when(musicDAO.getMusic(anyInt())).thenReturn(null);
		when(musicCache.get(anyString())).thenReturn(null);

		assertFalse(musicService.exists(EntityGenerator.createMusic(Integer.MAX_VALUE)));

		verify(musicDAO).getMusic(Integer.MAX_VALUE);
		verify(musicCache).get("music" + Integer.MAX_VALUE);
		verify(musicCache).put("music" + Integer.MAX_VALUE, null);
		verifyNoMoreInteractions(musicDAO, musicCache);
	}

	/** Test method for {@link MusicService#exists(Music)} with not set DAO for music. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetMusicDAO() {
		((MusicServiceImpl) musicService).setMusicDAO(null);
		musicService.exists(mock(Music.class));
	}

	/** Test method for {@link MusicService#exists(Music)} with not set cache for music. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetMusicCache() {
		((MusicServiceImpl) musicService).setMusicCache(null);
		musicService.exists(mock(Music.class));
	}

	/** Test method for {@link MusicService#exists(Music)} with null argument. */
	@Test
	public void testExistsWithNullArgument() {
		try {
			musicService.exists(null);
			fail("Can't exists music with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(musicDAO, musicCache);
	}

	/** Test method for {@link MusicService#exists(Music)} with exception in DAO tier. */
	@Test
	public void testExistsWithDAOTierException() {
		doThrow(DataStorageException.class).when(musicDAO).getMusic(anyInt());
		when(musicCache.get(anyString())).thenReturn(null);

		try {
			musicService.exists(EntityGenerator.createMusic(Integer.MAX_VALUE));
			fail("Can't exists music with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(musicDAO).getMusic(Integer.MAX_VALUE);
		verify(musicCache).get("music" + Integer.MAX_VALUE);
		verifyNoMoreInteractions(musicDAO, musicCache);
	}

	/** Test method for {@link MusicService#updatePositions()} with cached data. */
	@Test
	public void testUpdatePositionsWithCachedData() {
		final List<Music> musicList = CollectionUtils.newList(EntityGenerator.createMusic(PRIMARY_ID), EntityGenerator.createMusic(SECONDARY_ID));
		final List<Song> songs = CollectionUtils.newList(EntityGenerator.createSong(INNER_ID), EntityGenerator.createSong(SECONDARY_INNER_ID));
		when(musicCache.get("music")).thenReturn(new SimpleValueWrapper(musicList));
		when(musicCache.get("songs" + PRIMARY_ID)).thenReturn(new SimpleValueWrapper(songs));
		when(musicCache.get("songs" + SECONDARY_ID)).thenReturn(new SimpleValueWrapper(songs));

		musicService.updatePositions();

		for (int i = 0; i < musicList.size(); i++) {
			final Music music = musicList.get(i);
			DeepAsserts.assertEquals(i, music.getPosition());
			verify(musicDAO).update(music);
			verify(musicCache).get("songs" + music.getId());
		}
		for (int i = 0; i < songs.size(); i++) {
			final Song song = songs.get(i);
			DeepAsserts.assertEquals(i, song.getPosition());
			verify(songDAO, times(musicList.size())).update(song);
		}
		verify(musicCache).get("music");
		verify(musicCache).clear();
		verifyNoMoreInteractions(musicDAO, songDAO, musicCache);
	}

	/** Test method for {@link MusicService#updatePositions()} with not cached data. */
	@Test
	public void testUpdatePositionsWithNotCachedData() {
		final List<Music> musicList = CollectionUtils.newList(EntityGenerator.createMusic(PRIMARY_ID), EntityGenerator.createMusic(SECONDARY_ID));
		final List<Song> songs = CollectionUtils.newList(EntityGenerator.createSong(INNER_ID), EntityGenerator.createSong(SECONDARY_INNER_ID));
		when(musicDAO.getMusic()).thenReturn(musicList);
		when(songDAO.findSongsByMusic(any(Music.class))).thenReturn(songs);
		when(musicCache.get(anyString())).thenReturn(null);

		musicService.updatePositions();

		verify(musicDAO).getMusic();
		for (int i = 0; i < musicList.size(); i++) {
			final Music music = musicList.get(i);
			DeepAsserts.assertEquals(i, music.getPosition());
			verify(musicDAO).update(music);
			verify(songDAO).findSongsByMusic(music);
			verify(musicCache).get("songs" + music.getId());
		}
		for (int i = 0; i < songs.size(); i++) {
			final Song song = songs.get(i);
			DeepAsserts.assertEquals(i, song.getPosition());
			verify(songDAO, times(musicList.size())).update(song);
		}
		verify(musicCache).get("music");
		verify(musicCache).clear();
		verifyNoMoreInteractions(musicDAO, songDAO, musicCache);
	}

	/** Test method for {@link MusicService#updatePositions()} with not set DAO for music. */
	@Test(expected = IllegalStateException.class)
	public void testUpdatePositionsWithNotSetMusicDAO() {
		((MusicServiceImpl) musicService).setMusicDAO(null);
		musicService.updatePositions();
	}

	/** Test method for {@link MusicService#updatePositions()} with not set DAO for songs. */
	@Test(expected = IllegalStateException.class)
	public void testUpdatePositionsWithNotSetSongDAO() {
		((MusicServiceImpl) musicService).setSongDAO(null);
		musicService.updatePositions();
	}

	/** Test method for {@link MusicService#updatePositions()} with not set cache for music. */
	@Test(expected = IllegalStateException.class)
	public void testUpdatePositionsWithNotSetMusicCache() {
		((MusicServiceImpl) musicService).setMusicCache(null);
		musicService.updatePositions();
	}

	/** Test method for {@link MusicService#updatePositions()} with exception in DAO tier. */
	@Test
	public void testUpdatePositionsWithDAOTierException() {
		doThrow(DataStorageException.class).when(musicDAO).getMusic();
		when(musicCache.get(anyString())).thenReturn(null);

		try {
			musicService.updatePositions();
			fail("Can't update positions with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(musicDAO).getMusic();
		verify(musicCache).get("music");
		verifyNoMoreInteractions(musicDAO, musicCache);
		verifyZeroInteractions(songDAO);
	}

	/** Test method for {@link MusicService#getTotalMediaCount()} with cached music. */
	@Test
	public void testGetTotalMediaCountWithCachedMusic() {
		final Music music1 = mock(Music.class);
		final Music music2 = mock(Music.class);
		final Music music3 = mock(Music.class);
		final List<Music> musicList = CollectionUtils.newList(music1, music2, music3);
		when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(musicList));
		when(music1.getMediaCount()).thenReturn(1);
		when(music2.getMediaCount()).thenReturn(2);
		when(music3.getMediaCount()).thenReturn(3);

		DeepAsserts.assertEquals(6, musicService.getTotalMediaCount());

		verify(musicCache).get("music");
		verify(music1).getMediaCount();
		verify(music2).getMediaCount();
		verify(music3).getMediaCount();
		verifyNoMoreInteractions(musicCache, music1, music2, music3);
		verifyZeroInteractions(musicDAO);
	}

	/** Test method for {@link MusicService#getTotalMediaCount()} with not cached music. */
	@Test
	public void testGetTotalMediaCountWithNotCachedMusic() {
		final Music music1 = mock(Music.class);
		final Music music2 = mock(Music.class);
		final Music music3 = mock(Music.class);
		final List<Music> musicList = CollectionUtils.newList(music1, music2, music3);
		when(musicDAO.getMusic()).thenReturn(musicList);
		when(musicCache.get(anyString())).thenReturn(null);
		when(music1.getMediaCount()).thenReturn(1);
		when(music2.getMediaCount()).thenReturn(2);
		when(music3.getMediaCount()).thenReturn(3);

		DeepAsserts.assertEquals(6, musicService.getTotalMediaCount());

		verify(musicDAO).getMusic();
		verify(musicCache).get("music");
		verify(musicCache).put("music", musicList);
		verify(music1).getMediaCount();
		verify(music2).getMediaCount();
		verify(music3).getMediaCount();
		verifyNoMoreInteractions(musicDAO, musicCache, music1, music2, music3);
	}

	/** Test method for {@link MusicService#getTotalMediaCount()} with not set DAO for music. */
	@Test(expected = IllegalStateException.class)
	public void testGetTotalMediaCountWithNotSetMusicDAO() {
		((MusicServiceImpl) musicService).setMusicDAO(null);
		musicService.getTotalMediaCount();
	}

	/** Test method for {@link MusicService#getTotalMediaCount()} with not set cache for music. */
	@Test(expected = IllegalStateException.class)
	public void testGetTotalMediaCountWithNotSetMusicCache() {
		((MusicServiceImpl) musicService).setMusicCache(null);
		musicService.getTotalMediaCount();
	}

	/** Test method for {@link MusicService#getTotalMediaCount()} with exception in DAO tier. */
	@Test
	public void testGetTotalMediaCountWithDAOTierException() {
		doThrow(DataStorageException.class).when(musicDAO).getMusic();
		when(musicCache.get(anyString())).thenReturn(null);

		try {
			musicService.getTotalMediaCount();
			fail("Can't get total media count with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(musicDAO).getMusic();
		verify(musicCache).get("music");
		verifyNoMoreInteractions(musicDAO, musicCache);
	}

	/** Test method for {@link MusicService#getTotalLength()} with cached data. */
	@Test
	public void testGetTotalLengthWithCachedData() {
		final Music music1 = EntityGenerator.createMusic(PRIMARY_ID);
		final Music music2 = EntityGenerator.createMusic(SECONDARY_ID);
		final List<Music> music = CollectionUtils.newList(music1, music2);
		final Song song1 = mock(Song.class);
		final Song song2 = mock(Song.class);
		final Song song3 = mock(Song.class);
		final List<Song> songs1 = CollectionUtils.newList(song1);
		final List<Song> songs2 = CollectionUtils.newList(song2, song3);
		when(musicCache.get("music")).thenReturn(new SimpleValueWrapper(music));
		when(musicCache.get("songs" + PRIMARY_ID)).thenReturn(new SimpleValueWrapper(songs1));
		when(musicCache.get("songs" + SECONDARY_ID)).thenReturn(new SimpleValueWrapper(songs2));
		when(song1.getLength()).thenReturn(100);
		when(song2.getLength()).thenReturn(200);
		when(song3.getLength()).thenReturn(300);

		DeepAsserts.assertEquals(new Time(600), musicService.getTotalLength());

		verify(musicCache).get("music");
		verify(musicCache).get("songs" + PRIMARY_ID);
		verify(musicCache).get("songs" + SECONDARY_ID);
		verify(song1).getLength();
		verify(song2).getLength();
		verify(song3).getLength();
		verifyNoMoreInteractions(musicCache, song1, song2, song3);
		verifyZeroInteractions(musicDAO, songDAO);
	}

	/** Test method for {@link MusicService#getTotalLength()} with not cached data. */
	@Test
	public void testGetTotalLengthWithNotCachedMusic() {
		final Music music1 = EntityGenerator.createMusic(PRIMARY_ID);
		final Music music2 = EntityGenerator.createMusic(SECONDARY_ID);
		final List<Music> music = CollectionUtils.newList(music1, music2);
		final Song song1 = mock(Song.class);
		final Song song2 = mock(Song.class);
		final Song song3 = mock(Song.class);
		final List<Song> songs1 = CollectionUtils.newList(song1);
		final List<Song> songs2 = CollectionUtils.newList(song2, song3);
		when(musicDAO.getMusic()).thenReturn(music);
		when(songDAO.findSongsByMusic(music1)).thenReturn(songs1);
		when(songDAO.findSongsByMusic(music2)).thenReturn(songs2);
		when(musicCache.get(anyString())).thenReturn(null);
		when(song1.getLength()).thenReturn(100);
		when(song2.getLength()).thenReturn(200);
		when(song3.getLength()).thenReturn(300);

		DeepAsserts.assertEquals(new Time(600), musicService.getTotalLength());

		verify(musicDAO).getMusic();
		verify(songDAO).findSongsByMusic(music1);
		verify(songDAO).findSongsByMusic(music2);
		verify(musicCache).get("music");
		verify(musicCache).put("music", music);
		verify(musicCache).get("songs" + PRIMARY_ID);
		verify(musicCache).put("songs" + PRIMARY_ID, songs1);
		verify(musicCache).get("songs" + SECONDARY_ID);
		verify(musicCache).put("songs" + SECONDARY_ID, songs2);
		verify(song1).getLength();
		verify(song2).getLength();
		verify(song3).getLength();
		verifyNoMoreInteractions(musicDAO, songDAO, musicCache, song1, song2, song3);
	}

	/** Test method for {@link MusicService#getTotalLength()} with not set DAO for music. */
	@Test(expected = IllegalStateException.class)
	public void testGetTotalLengthWithNotSetMusicDAO() {
		((MusicServiceImpl) musicService).setMusicDAO(null);
		musicService.getTotalLength();
	}

	/** Test method for {@link MusicService#getTotalLength()} with not set DAO for songs. */
	@Test(expected = IllegalStateException.class)
	public void testGetTotalLengthWithNotSetSongDAO() {
		((MusicServiceImpl) musicService).setSongDAO(null);
		musicService.getTotalLength();
	}

	/** Test method for {@link MusicService#getTotalLength()} with not set cache for music. */
	@Test(expected = IllegalStateException.class)
	public void testGetTotalLengthWithNotSetMusicCache() {
		((MusicServiceImpl) musicService).setMusicCache(null);
		musicService.getTotalLength();
	}

	/** Test method for {@link MusicService#getTotalLength()} with exception in DAO tier. */
	@Test
	public void testGetTotalLengthWithDAOTierException() {
		doThrow(DataStorageException.class).when(musicDAO).getMusic();
		when(musicCache.get(anyString())).thenReturn(null);

		try {
			musicService.getTotalLength();
			fail("Can't get total length with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(musicDAO).getMusic();
		verify(musicCache).get("music");
		verifyNoMoreInteractions(musicDAO, musicCache);
		verifyZeroInteractions(songDAO);
	}

	/** Test method for {@link MusicService#getSongsCount()} with cached data. */
	@Test
	public void testGetSongsCountWithCachedData() {
		final Music music1 = EntityGenerator.createMusic(PRIMARY_ID);
		final Music music2 = EntityGenerator.createMusic(SECONDARY_ID);
		final List<Music> music = CollectionUtils.newList(music1, music2);
		final List<Song> songs1 = CollectionUtils.newList(mock(Song.class));
		final List<Song> songs2 = CollectionUtils.newList(mock(Song.class), mock(Song.class));
		when(musicCache.get("music")).thenReturn(new SimpleValueWrapper(music));
		when(musicCache.get("songs" + PRIMARY_ID)).thenReturn(new SimpleValueWrapper(songs1));
		when(musicCache.get("songs" + SECONDARY_ID)).thenReturn(new SimpleValueWrapper(songs2));

		DeepAsserts.assertEquals(songs1.size() + songs2.size(), musicService.getSongsCount());

		verify(musicCache).get("music");
		verify(musicCache).get("songs" + PRIMARY_ID);
		verify(musicCache).get("songs" + SECONDARY_ID);
		verifyNoMoreInteractions(musicCache);
		verifyZeroInteractions(musicDAO, songDAO);
	}

	/** Test method for {@link MusicService#getSongsCount()} with not cached data. */
	@Test
	public void testGetSongsCountWithNotCachedMusic() {
		final Music music1 = EntityGenerator.createMusic(PRIMARY_ID);
		final Music music2 = EntityGenerator.createMusic(SECONDARY_ID);
		final List<Music> music = CollectionUtils.newList(music1, music2);
		final List<Song> songs1 = CollectionUtils.newList(mock(Song.class));
		final List<Song> songs2 = CollectionUtils.newList(mock(Song.class), mock(Song.class));
		when(musicDAO.getMusic()).thenReturn(music);
		when(songDAO.findSongsByMusic(music1)).thenReturn(songs1);
		when(songDAO.findSongsByMusic(music2)).thenReturn(songs2);
		when(musicCache.get(anyString())).thenReturn(null);

		DeepAsserts.assertEquals(songs1.size() + songs2.size(), musicService.getSongsCount());

		verify(musicDAO).getMusic();
		verify(songDAO).findSongsByMusic(music1);
		verify(songDAO).findSongsByMusic(music2);
		verify(musicCache).get("music");
		verify(musicCache).put("music", music);
		verify(musicCache).get("songs" + PRIMARY_ID);
		verify(musicCache).put("songs" + PRIMARY_ID, songs1);
		verify(musicCache).get("songs" + SECONDARY_ID);
		verify(musicCache).put("songs" + SECONDARY_ID, songs2);
		verifyNoMoreInteractions(musicDAO, songDAO, musicCache);
	}

	/** Test method for {@link MusicService#getSongsCount()} with not set DAO for music. */
	@Test(expected = IllegalStateException.class)
	public void testGetSongsCountWithNotSetMusicDAO() {
		((MusicServiceImpl) musicService).setMusicDAO(null);
		musicService.getSongsCount();
	}

	/** Test method for {@link MusicService#getSongsCount()} with not set DAO for songs. */
	@Test(expected = IllegalStateException.class)
	public void testGetSongsCountWithNotSetSongDAO() {
		((MusicServiceImpl) musicService).setSongDAO(null);
		musicService.getSongsCount();
	}

	/** Test method for {@link MusicService#getSongsCount()} with not set cache for music. */
	@Test(expected = IllegalStateException.class)
	public void testGetSongsCountWithNotSetMusicCache() {
		((MusicServiceImpl) musicService).setMusicCache(null);
		musicService.getSongsCount();
	}

	/** Test method for {@link MusicService#getSongsCount()} with exception in DAO tier. */
	@Test
	public void testGetSongsCountWithDAOTierException() {
		doThrow(DataStorageException.class).when(musicDAO).getMusic();
		when(musicCache.get(anyString())).thenReturn(null);

		try {
			musicService.getSongsCount();
			fail("Can't get songs count with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(musicDAO).getMusic();
		verify(musicCache).get("music");
		verifyNoMoreInteractions(musicDAO, musicCache);
		verifyZeroInteractions(songDAO);
	}

}
