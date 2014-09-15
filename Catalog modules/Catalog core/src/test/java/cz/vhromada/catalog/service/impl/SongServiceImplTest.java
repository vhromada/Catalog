package cz.vhromada.catalog.service.impl;

import static cz.vhromada.catalog.common.TestConstants.INNER_ID;
import static cz.vhromada.catalog.common.TestConstants.MOVE_POSITION;
import static cz.vhromada.catalog.common.TestConstants.POSITION;
import static cz.vhromada.catalog.common.TestConstants.PRIMARY_ID;
import static cz.vhromada.catalog.common.TestConstants.SECONDARY_ID;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.common.EntityGenerator;
import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.SongDAO;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.dao.entities.Song;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.catalog.service.SongService;
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
 * A class represents test for class {@link SongServiceImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class SongServiceImplTest {

	/** Instance of {@link SongDAO} */
	@Mock
	private SongDAO songDAO;

	/** Instance of {@link Cache} */
	@Mock
	private Cache musicCache;

	/** Instance of {@link SongService} */
	@InjectMocks
	private SongService songService = new SongServiceImpl();

	/** Test method for {@link SongServiceImpl#getSongDAO()} and {@link SongServiceImpl#setSongDAO(SongDAO)}. */
	@Test
	public void testSongDAO() {
		final SongServiceImpl songService = new SongServiceImpl();
		songService.setSongDAO(songDAO);
		DeepAsserts.assertEquals(songDAO, songService.getSongDAO());
	}

	/** Test method for {@link SongServiceImpl#getMusicCache()} and {@link SongServiceImpl#setMusicCache(Cache)}. */
	@Test
	public void testMusicCache() {
		final SongServiceImpl songService = new SongServiceImpl();
		songService.setMusicCache(musicCache);
		DeepAsserts.assertEquals(musicCache, songService.getMusicCache());
	}

	/** Test method for {@link SongService#getSong(Integer)} with cached existing song. */
	@Test
	public void testGetSongWithCachedExistingSong() {
		final Song song = EntityGenerator.createSong(PRIMARY_ID, EntityGenerator.createMusic(INNER_ID));
		when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(song));

		DeepAsserts.assertEquals(song, songService.getSong(PRIMARY_ID));

		verify(musicCache).get("song" + PRIMARY_ID);
		verifyNoMoreInteractions(musicCache);
		verifyZeroInteractions(songDAO);
	}

	/** Test method for {@link SongService#getSong(Integer)} with cached not existing song. */
	@Test
	public void testGetSongWithCachedNotExistingSong() {
		when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

		assertNull(songService.getSong(PRIMARY_ID));

		verify(musicCache).get("song" + PRIMARY_ID);
		verifyNoMoreInteractions(musicCache);
		verifyZeroInteractions(songDAO);
	}

	/** Test method for {@link SongService#getSong(Integer)} with not cached existing song. */
	@Test
	public void testGetSongWithNotCachedExistingSong() {
		final Song song = EntityGenerator.createSong(PRIMARY_ID, EntityGenerator.createMusic(INNER_ID));
		when(songDAO.getSong(anyInt())).thenReturn(song);
		when(musicCache.get(anyString())).thenReturn(null);

		DeepAsserts.assertEquals(song, songService.getSong(PRIMARY_ID));

		verify(songDAO).getSong(PRIMARY_ID);
		verify(musicCache).get("song" + PRIMARY_ID);
		verify(musicCache).put("song" + PRIMARY_ID, song);
		verifyNoMoreInteractions(songDAO, musicCache);
	}

	/** Test method for {@link SongService#getSong(Integer)} with not cached not existing song. */
	@Test
	public void testGetSongWithNotCachedNotExistingSong() {
		when(songDAO.getSong(anyInt())).thenReturn(null);
		when(musicCache.get(anyString())).thenReturn(null);

		assertNull(songService.getSong(PRIMARY_ID));

		verify(songDAO).getSong(PRIMARY_ID);
		verify(musicCache).get("song" + PRIMARY_ID);
		verify(musicCache).put("song" + PRIMARY_ID, null);
		verifyNoMoreInteractions(songDAO, musicCache);
	}

	/** Test method for {@link SongService#getSong(Integer)} with not set DAO for songs. */
	@Test(expected = IllegalStateException.class)
	public void testGetSongWithNotSetSongDAO() {
		((SongServiceImpl) songService).setSongDAO(null);
		songService.getSong(Integer.MAX_VALUE);
	}

	/** Test method for {@link SongService#getSong(Integer)} with not set cache for music. */
	@Test(expected = IllegalStateException.class)
	public void testGetSongWithNotSetMusicCache() {
		((SongServiceImpl) songService).setMusicCache(null);
		songService.getSong(Integer.MAX_VALUE);
	}

	/** Test method for {@link SongService#getSong(Integer)} with null argument. */
	@Test
	public void testGetSongWithNullArgument() {
		try {
			songService.getSong(null);
			fail("Can't get song with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(songDAO, musicCache);
	}

	/** Test method for {@link SongService#getSong(Integer)} with exception in DAO tier. */
	@Test
	public void testGetSongWithDAOTierException() {
		doThrow(DataStorageException.class).when(songDAO).getSong(anyInt());
		when(musicCache.get(anyString())).thenReturn(null);

		try {
			songService.getSong(Integer.MAX_VALUE);
			fail("Can't get song with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(songDAO).getSong(Integer.MAX_VALUE);
		verify(musicCache).get("song" + Integer.MAX_VALUE);
		verifyNoMoreInteractions(songDAO, musicCache);
	}

	/** Test method for {@link SongService#add(Song)} with cached songs. */
	@Test
	public void testAddWithCachedSongs() {
		final Song song = EntityGenerator.createSong(EntityGenerator.createMusic(INNER_ID));
		final List<Song> songs = CollectionUtils.newList(mock(Song.class), mock(Song.class));
		final List<Song> songsList = new ArrayList<>(songs);
		songsList.add(song);
		when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(songs));

		songService.add(song);

		verify(songDAO).add(song);
		verify(musicCache).get("songs" + INNER_ID);
		verify(musicCache).get("song" + null);
		verify(musicCache).put("songs" + INNER_ID, songsList);
		verify(musicCache).put("song" + null, song);
		verifyNoMoreInteractions(songDAO, musicCache);
	}

	/** Test method for {@link SongService#add(Song)} with not cached songs. */
	@Test
	public void testAddWithNotCachedSongs() {
		final Song song = EntityGenerator.createSong(EntityGenerator.createMusic(INNER_ID));
		when(musicCache.get(anyString())).thenReturn(null);

		songService.add(song);

		verify(songDAO).add(song);
		verify(musicCache).get("songs" + INNER_ID);
		verify(musicCache).get("song" + null);
		verifyNoMoreInteractions(songDAO, musicCache);
	}

	/** Test method for {@link SongService#add(Song)} with not set DAO for songs. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetSongDAO() {
		((SongServiceImpl) songService).setSongDAO(null);
		songService.add(mock(Song.class));
	}

	/** Test method for {@link SongService#add(Song)} with not set cache for music. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetMusicCache() {
		((SongServiceImpl) songService).setMusicCache(null);
		songService.add(mock(Song.class));
	}

	/** Test method for {@link SongService#add(Song)} with null argument. */
	@Test
	public void testAddWithNullArgument() {
		try {
			songService.add(null);
			fail("Can't add song with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(songDAO, musicCache);
	}

	/** Test method for {@link SongService#add(Song)} with exception in DAO tier. */
	@Test
	public void testAddWithDAOTierException() {
		final Song song = EntityGenerator.createSong(EntityGenerator.createMusic(INNER_ID));
		doThrow(DataStorageException.class).when(songDAO).add(any(Song.class));

		try {
			songService.add(song);
			fail("Can't add song with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(songDAO).add(song);
		verifyNoMoreInteractions(songDAO);
		verifyZeroInteractions(musicCache);
	}

	/** Test method for {@link SongService#update(Song)}. */
	@Test
	public void testUpdate() {
		final Song song = EntityGenerator.createSong(PRIMARY_ID, EntityGenerator.createMusic(INNER_ID));

		songService.update(song);

		verify(songDAO).update(song);
		verify(musicCache).clear();
		verifyNoMoreInteractions(songDAO, musicCache);
	}

	/** Test method for {@link SongService#update(Song)} with not set DAO for songs. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetSongDAO() {
		((SongServiceImpl) songService).setSongDAO(null);
		songService.update(mock(Song.class));
	}

	/** Test method for {@link SongService#update(Song)} with not set cache for music. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetMusicCache() {
		((SongServiceImpl) songService).setMusicCache(null);
		songService.update(mock(Song.class));
	}

	/** Test method for {@link SongService#update(Song)} with null argument. */
	@Test
	public void testUpdateWithNullArgument() {
		try {
			songService.update(null);
			fail("Can't update song with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(songDAO, musicCache);
	}

	/** Test method for {@link SongService#update(Song)} with exception in DAO tier. */
	@Test
	public void testUpdateWithDAOTierException() {
		final Song song = EntityGenerator.createSong(Integer.MAX_VALUE, EntityGenerator.createMusic(INNER_ID));
		doThrow(DataStorageException.class).when(songDAO).update(any(Song.class));

		try {
			songService.update(song);
			fail("Can't update song with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(songDAO).update(song);
		verifyNoMoreInteractions(songDAO);
		verifyZeroInteractions(musicCache);
	}

	/** Test method for {@link SongService#remove(Song)} with cached songs. */
	@Test
	public void testRemoveWithCachedSongs() {
		final Song song = EntityGenerator.createSong(PRIMARY_ID, EntityGenerator.createMusic(INNER_ID));
		final List<Song> songs = CollectionUtils.newList(mock(Song.class), mock(Song.class));
		final List<Song> songsList = new ArrayList<>(songs);
		songsList.add(song);
		when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(songsList));

		songService.remove(song);

		verify(songDAO).remove(song);
		verify(musicCache).get("songs" + INNER_ID);
		verify(musicCache).put("songs" + INNER_ID, songs);
		verify(musicCache).evict("song" + PRIMARY_ID);
		verifyNoMoreInteractions(songDAO, musicCache);
	}

	/** Test method for {@link SongService#remove(Song)} with not cached songs. */
	@Test
	public void testRemoveWithNotCachedSongs() {
		final Song song = EntityGenerator.createSong(PRIMARY_ID, EntityGenerator.createMusic(INNER_ID));
		when(musicCache.get(anyString())).thenReturn(null);

		songService.remove(song);

		verify(songDAO).remove(song);
		verify(musicCache).get("songs" + INNER_ID);
		verify(musicCache).evict("song" + PRIMARY_ID);
		verifyNoMoreInteractions(songDAO, musicCache);
	}

	/** Test method for {@link SongService#remove(Song)} with not set DAO for songs. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetSongDAO() {
		((SongServiceImpl) songService).setSongDAO(null);
		songService.remove(mock(Song.class));
	}

	/** Test method for {@link SongService#remove(Song)} with not set cache for music. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetMusicCache() {
		((SongServiceImpl) songService).setMusicCache(null);
		songService.remove(mock(Song.class));
	}

	/** Test method for {@link SongService#remove(Song)} with null argument. */
	@Test
	public void testRemoveWithNullArgument() {
		try {
			songService.remove(null);
			fail("Can't remove song with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(songDAO, musicCache);
	}

	/** Test method for {@link SongService#remove(Song)} with exception in DAO tier. */
	@Test
	public void testRemoveWithDAOTierException() {
		final Song song = EntityGenerator.createSong(Integer.MAX_VALUE, EntityGenerator.createMusic(INNER_ID));
		doThrow(DataStorageException.class).when(songDAO).remove(any(Song.class));

		try {
			songService.remove(song);
			fail("Can't remove song with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(songDAO).remove(song);
		verifyNoMoreInteractions(songDAO);
		verifyZeroInteractions(musicCache);
	}

	/** Test method for {@link SongService#duplicate(Song)} with cached songs. */
	@Test
	public void testDuplicateWithCachedSongs() {
		when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(CollectionUtils.newList(mock(Song.class), mock(Song.class))));

		songService.duplicate(EntityGenerator.createSong(PRIMARY_ID, EntityGenerator.createMusic(INNER_ID)));

		verify(songDAO).add(any(Song.class));
		verify(songDAO).update(any(Song.class));
		verify(musicCache).get("songs" + INNER_ID);
		verify(musicCache).get("song" + null);
		verify(musicCache).put(eq("songs" + INNER_ID), anyListOf(Song.class));
		verify(musicCache).put(eq("song" + null), any(Song.class));
		verifyNoMoreInteractions(songDAO, musicCache);
	}

	/** Test method for {@link SongService#duplicate(Song)} with not cached songs. */
	@Test
	public void testDuplicateWithNotCachedSongs() {
		when(musicCache.get(anyString())).thenReturn(null);

		songService.duplicate(EntityGenerator.createSong(PRIMARY_ID, EntityGenerator.createMusic(INNER_ID)));

		verify(songDAO).add(any(Song.class));
		verify(songDAO).update(any(Song.class));
		verify(musicCache).get("songs" + INNER_ID);
		verify(musicCache).get("song" + null);
		verifyNoMoreInteractions(songDAO, musicCache);
	}

	/** Test method for {@link SongService#duplicate(Song)} with not set DAO for songs. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetSongDAO() {
		((SongServiceImpl) songService).setSongDAO(null);
		songService.duplicate(mock(Song.class));
	}

	/** Test method for {@link SongService#duplicate(Song)} with not set cache for music. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetMusicCache() {
		((SongServiceImpl) songService).setMusicCache(null);
		songService.duplicate(mock(Song.class));
	}

	/** Test method for {@link SongService#duplicate(Song)} with null argument. */
	@Test
	public void testDuplicateWithNullArgument() {
		try {
			songService.duplicate(null);
			fail("Can't duplicate song with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(songDAO, musicCache);
	}

	/** Test method for {@link SongService#duplicate(Song)} with exception in DAO tier. */
	@Test
	public void testDuplicateWithDAOTierException() {
		doThrow(DataStorageException.class).when(songDAO).add(any(Song.class));

		try {
			songService.duplicate(EntityGenerator.createSong(Integer.MAX_VALUE, EntityGenerator.createMusic(INNER_ID)));
			fail("Can't duplicate song with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(songDAO).add(any(Song.class));
		verifyNoMoreInteractions(songDAO);
		verifyZeroInteractions(musicCache);
	}

	/** Test method for {@link SongService#moveUp(Song)} with cached songs. */
	@Test
	public void testMoveUpWithCachedSongs() {
		final Music music = EntityGenerator.createMusic(INNER_ID);
		final Song song1 = EntityGenerator.createSong(PRIMARY_ID, music);
		song1.setPosition(MOVE_POSITION);
		final Song song2 = EntityGenerator.createSong(SECONDARY_ID, music);
		final List<Song> songs = CollectionUtils.newList(song1, song2);
		when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(songs));

		songService.moveUp(song2);
		DeepAsserts.assertEquals(POSITION, song1.getPosition());
		DeepAsserts.assertEquals(MOVE_POSITION, song2.getPosition());

		verify(songDAO).update(song1);
		verify(songDAO).update(song2);
		verify(musicCache).get("songs" + INNER_ID);
		verify(musicCache).clear();
		verifyNoMoreInteractions(songDAO, musicCache);
	}

	/** Test method for {@link SongService#moveUp(Song)} with not cached songs. */
	@Test
	public void testMoveUpWithNotCachedSongs() {
		final Music music = EntityGenerator.createMusic(INNER_ID);
		final Song song1 = EntityGenerator.createSong(PRIMARY_ID, music);
		song1.setPosition(MOVE_POSITION);
		final Song song2 = EntityGenerator.createSong(SECONDARY_ID, music);
		final List<Song> songs = CollectionUtils.newList(song1, song2);
		when(songDAO.findSongsByMusic(any(Music.class))).thenReturn(songs);
		when(musicCache.get(anyString())).thenReturn(null);

		songService.moveUp(song2);
		DeepAsserts.assertEquals(POSITION, song1.getPosition());
		DeepAsserts.assertEquals(MOVE_POSITION, song2.getPosition());

		verify(songDAO).update(song1);
		verify(songDAO).update(song2);
		verify(songDAO).findSongsByMusic(music);
		verify(musicCache).get("songs" + INNER_ID);
		verify(musicCache).clear();
		verifyNoMoreInteractions(songDAO, musicCache);
	}

	/** Test method for {@link SongService#moveUp(Song)} with not set DAO for songs. */
	@Test(expected = IllegalStateException.class)
	public void testMoveUpWithNotSetSongDAO() {
		((SongServiceImpl) songService).setSongDAO(null);
		songService.moveUp(mock(Song.class));
	}

	/** Test method for {@link SongService#moveUp(Song)} with not set cache for music. */
	@Test(expected = IllegalStateException.class)
	public void testMoveUpWithNotSetSetMusicCache() {
		((SongServiceImpl) songService).setMusicCache(null);
		songService.moveUp(mock(Song.class));
	}

	/** Test method for {@link SongService#moveUp(Song)} with null argument. */
	@Test
	public void testMoveUpWithNullArgument() {
		try {
			songService.moveUp(null);
			fail("Can't move up song with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(songDAO, musicCache);
	}

	/** Test method for {@link SongService#moveUp(Song)} with exception in DAO tier. */
	@Test
	public void testMoveUpWithDAOTierException() {
		final Music music = EntityGenerator.createMusic(INNER_ID);
		doThrow(DataStorageException.class).when(songDAO).findSongsByMusic(any(Music.class));
		when(musicCache.get(anyString())).thenReturn(null);

		try {
			songService.moveUp(EntityGenerator.createSong(Integer.MAX_VALUE, music));
			fail("Can't move up song with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(songDAO).findSongsByMusic(music);
		verify(musicCache).get("songs" + INNER_ID);
		verifyNoMoreInteractions(songDAO, musicCache);
	}

	/** Test method for {@link SongService#moveDown(Song)} with cached songs. */
	@Test
	public void testMoveDownWithCachedSongs() {
		final Music music = EntityGenerator.createMusic(INNER_ID);
		final Song song1 = EntityGenerator.createSong(PRIMARY_ID, music);
		final Song song2 = EntityGenerator.createSong(SECONDARY_ID, music);
		song2.setPosition(MOVE_POSITION);
		final List<Song> songs = CollectionUtils.newList(song1, song2);
		when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(songs));

		songService.moveDown(song1);
		DeepAsserts.assertEquals(MOVE_POSITION, song1.getPosition());
		DeepAsserts.assertEquals(POSITION, song2.getPosition());

		verify(songDAO).update(song1);
		verify(songDAO).update(song2);
		verify(musicCache).get("songs" + INNER_ID);
		verify(musicCache).clear();
		verifyNoMoreInteractions(songDAO, musicCache);
	}

	/** Test method for {@link SongService#moveDown(Song)} with not cached songs. */
	@Test
	public void testMoveDownWithNotCachedSongs() {
		final Music music = EntityGenerator.createMusic(INNER_ID);
		final Song song1 = EntityGenerator.createSong(PRIMARY_ID, music);
		final Song song2 = EntityGenerator.createSong(SECONDARY_ID, music);
		song2.setPosition(MOVE_POSITION);
		final List<Song> songs = CollectionUtils.newList(song1, song2);
		when(songDAO.findSongsByMusic(any(Music.class))).thenReturn(songs);
		when(musicCache.get(anyString())).thenReturn(null);

		songService.moveDown(song1);
		DeepAsserts.assertEquals(MOVE_POSITION, song1.getPosition());
		DeepAsserts.assertEquals(POSITION, song2.getPosition());

		verify(songDAO).update(song1);
		verify(songDAO).update(song2);
		verify(songDAO).findSongsByMusic(music);
		verify(musicCache).get("songs" + INNER_ID);
		verify(musicCache).clear();
		verifyNoMoreInteractions(songDAO, musicCache);
	}

	/** Test method for {@link SongService#moveDown(Song)} with not set DAO for songs. */
	@Test(expected = IllegalStateException.class)
	public void testMoveDownWithNotSetSongDAO() {
		((SongServiceImpl) songService).setSongDAO(null);
		songService.moveDown(mock(Song.class));
	}

	/** Test method for {@link SongService#moveDown(Song)} with not set cache for music. */
	@Test(expected = IllegalStateException.class)
	public void testMoveDownWithNotSetSetMusicCache() {
		((SongServiceImpl) songService).setMusicCache(null);
		songService.moveDown(mock(Song.class));
	}

	/** Test method for {@link SongService#moveDown(Song)} with null argument. */
	@Test
	public void testMoveDownWithNullArgument() {
		try {
			songService.moveDown(null);
			fail("Can't move down song with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(songDAO, musicCache);
	}

	/** Test method for {@link SongService#moveDown(Song)} with exception in DAO tier. */
	@Test
	public void testMoveDownWithDAOTierException() {
		final Music music = EntityGenerator.createMusic(INNER_ID);
		doThrow(DataStorageException.class).when(songDAO).findSongsByMusic(any(Music.class));
		when(musicCache.get(anyString())).thenReturn(null);

		try {
			songService.moveDown(EntityGenerator.createSong(Integer.MAX_VALUE, music));
			fail("Can't move down song with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(songDAO).findSongsByMusic(music);
		verify(musicCache).get("songs" + INNER_ID);
		verifyNoMoreInteractions(songDAO, musicCache);
	}

	/** Test method for {@link SongService#exists(Song)} with cached existing song. */
	@Test
	public void testExistsWithCachedExistingSong() {
		final Song song = EntityGenerator.createSong(PRIMARY_ID, EntityGenerator.createMusic(INNER_ID));
		when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(song));

		assertTrue(songService.exists(song));

		verify(musicCache).get("song" + PRIMARY_ID);
		verifyNoMoreInteractions(musicCache);
		verifyZeroInteractions(songDAO);
	}

	/** Test method for {@link SongService#exists(Song)} with cached not existing song. */
	@Test
	public void testExistsWithCachedNotExistingSong() {
		final Song song = EntityGenerator.createSong(Integer.MAX_VALUE, EntityGenerator.createMusic(INNER_ID));
		when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

		assertFalse(songService.exists(song));

		verify(musicCache).get("song" + Integer.MAX_VALUE);
		verifyNoMoreInteractions(musicCache);
		verifyZeroInteractions(songDAO);
	}

	/** Test method for {@link SongService#exists(Song)} with not cached existing song. */
	@Test
	public void testExistsWithNotCachedExistingSong() {
		final Song song = EntityGenerator.createSong(PRIMARY_ID, EntityGenerator.createMusic(INNER_ID));
		when(songDAO.getSong(anyInt())).thenReturn(song);
		when(musicCache.get(anyString())).thenReturn(null);

		assertTrue(songService.exists(song));

		verify(songDAO).getSong(PRIMARY_ID);
		verify(musicCache).get("song" + PRIMARY_ID);
		verify(musicCache).put("song" + PRIMARY_ID, song);
		verifyNoMoreInteractions(songDAO, musicCache);
	}

	/** Test method for {@link SongService#exists(Song)} with not cached not existing song. */
	@Test
	public void testExistsWithNotCachedNotExistingSong() {
		when(songDAO.getSong(anyInt())).thenReturn(null);
		when(musicCache.get(anyString())).thenReturn(null);

		assertFalse(songService.exists(EntityGenerator.createSong(Integer.MAX_VALUE, EntityGenerator.createMusic(INNER_ID))));

		verify(songDAO).getSong(Integer.MAX_VALUE);
		verify(musicCache).get("song" + Integer.MAX_VALUE);
		verify(musicCache).put("song" + Integer.MAX_VALUE, null);
		verifyNoMoreInteractions(songDAO, musicCache);
	}

	/** Test method for {@link SongService#exists(Song)} with not set DAO for songs. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetSongDAO() {
		((SongServiceImpl) songService).setSongDAO(null);
		songService.exists(mock(Song.class));
	}

	/** Test method for {@link SongService#exists(Song)} with not set cache for music. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetMusicCache() {
		((SongServiceImpl) songService).setMusicCache(null);
		songService.exists(mock(Song.class));
	}

	/** Test method for {@link SongService#exists(Song)} with null argument. */
	@Test
	public void testExistsWithNullArgument() {
		try {
			songService.exists(null);
			fail("Can't exists song with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(songDAO, musicCache);
	}

	/** Test method for {@link SongService#exists(Song)} with exception in DAO tier. */
	@Test
	public void testExistsWithDAOTierException() {
		doThrow(DataStorageException.class).when(songDAO).getSong(anyInt());
		when(musicCache.get(anyString())).thenReturn(null);

		try {
			songService.exists(EntityGenerator.createSong(Integer.MAX_VALUE, EntityGenerator.createMusic(INNER_ID)));
			fail("Can't exists song with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(songDAO).getSong(Integer.MAX_VALUE);
		verify(musicCache).get("song" + Integer.MAX_VALUE);
		verifyNoMoreInteractions(songDAO, musicCache);
	}

	/** Test method for {@link SongService#findSongsByMusic(Music)} with cached songs. */
	@Test
	public void testFindSongsByMusicWithCachedSongs() {
		final Music music = EntityGenerator.createMusic(PRIMARY_ID);
		final List<Song> songs = CollectionUtils.newList(mock(Song.class), mock(Song.class));
		when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(songs));

		DeepAsserts.assertEquals(songs, songService.findSongsByMusic(music));

		verify(musicCache).get("songs" + PRIMARY_ID);
		verifyNoMoreInteractions(musicCache);
		verifyZeroInteractions(songDAO);
	}

	/** Test method for {@link SongService#findSongsByMusic(Music)} with not cached songs. */
	@Test
	public void testFindSongsByMusicWithNotCachedSongs() {
		final Music music = EntityGenerator.createMusic(PRIMARY_ID);
		final List<Song> songs = CollectionUtils.newList(mock(Song.class), mock(Song.class));
		when(songDAO.findSongsByMusic(any(Music.class))).thenReturn(songs);
		when(musicCache.get(anyString())).thenReturn(null);

		DeepAsserts.assertEquals(songs, songService.findSongsByMusic(music));

		verify(songDAO).findSongsByMusic(music);
		verify(musicCache).get("songs" + PRIMARY_ID);
		verify(musicCache).put("songs" + PRIMARY_ID, songs);
		verifyNoMoreInteractions(songDAO, musicCache);
	}

	/** Test method for {@link SongService#findSongsByMusic(Music)} with not set DAO for songs. */
	@Test(expected = IllegalStateException.class)
	public void testFindSongsByMusicWithNotSetSongDAO() {
		((SongServiceImpl) songService).setSongDAO(null);
		songService.findSongsByMusic(mock(Music.class));
	}

	/** Test method for {@link SongService#findSongsByMusic(Music)} with not set cache for music. */
	@Test(expected = IllegalStateException.class)
	public void testFindSongsByMusicWithNotSetMusicCache() {
		((SongServiceImpl) songService).setMusicCache(null);
		songService.findSongsByMusic(mock(Music.class));
	}

	/** Test method for {@link SongService#findSongsByMusic(Music)} with null argument. */
	@Test
	public void testFindSongsByMusicWithNullArgument() {
		try {
			songService.findSongsByMusic(null);
			fail("Can't find songs by music with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(songDAO, musicCache);
	}

	/** Test method for {@link SongService#findSongsByMusic(Music)} with exception in DAO tier. */
	@Test
	public void testFindSongsByMusicWithDAOTierException() {
		final Music music = EntityGenerator.createMusic(Integer.MAX_VALUE);
		doThrow(DataStorageException.class).when(songDAO).findSongsByMusic(any(Music.class));
		when(musicCache.get(anyString())).thenReturn(null);

		try {
			songService.findSongsByMusic(music);
			fail("Can't find songs by music with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(songDAO).findSongsByMusic(music);
		verify(musicCache).get("songs" + Integer.MAX_VALUE);
		verifyNoMoreInteractions(songDAO, musicCache);
	}

	/** Test method for {@link SongService#getTotalLengthByMusic(Music)} with cached songs. */
	@Test
	public void testGetTotalLengthByMusicWithCachedSongs() {
		final Music music = EntityGenerator.createMusic(PRIMARY_ID);
		final Song song1 = mock(Song.class);
		final Song song2 = mock(Song.class);
		final Song song3 = mock(Song.class);
		final List<Song> songs = CollectionUtils.newList(song1, song2, song3);
		when(musicCache.get(anyString())).thenReturn(new SimpleValueWrapper(songs));
		when(song1.getLength()).thenReturn(100);
		when(song2.getLength()).thenReturn(200);
		when(song3.getLength()).thenReturn(300);

		DeepAsserts.assertEquals(new Time(600), songService.getTotalLengthByMusic(music));

		verify(musicCache).get("songs" + PRIMARY_ID);
		verify(song1).getLength();
		verify(song2).getLength();
		verify(song3).getLength();
		verifyNoMoreInteractions(musicCache, song1, song2, song3);
		verifyZeroInteractions(songDAO);
	}

	/** Test method for {@link SongService#getTotalLengthByMusic(Music)} with not cached songs. */
	@Test
	public void testGetTotalLengthByMusicWithNotCachedSongs() {
		final Music music = EntityGenerator.createMusic(PRIMARY_ID);
		final Song song1 = mock(Song.class);
		final Song song2 = mock(Song.class);
		final Song song3 = mock(Song.class);
		final List<Song> songs = CollectionUtils.newList(song1, song2, song3);
		when(songDAO.findSongsByMusic(any(Music.class))).thenReturn(songs);
		when(musicCache.get(anyString())).thenReturn(null);
		when(song1.getLength()).thenReturn(100);
		when(song2.getLength()).thenReturn(200);
		when(song3.getLength()).thenReturn(300);

		DeepAsserts.assertEquals(new Time(600), songService.getTotalLengthByMusic(music));

		verify(songDAO).findSongsByMusic(music);
		verify(musicCache).get("songs" + PRIMARY_ID);
		verify(musicCache).put("songs" + PRIMARY_ID, songs);
		verify(song1).getLength();
		verify(song2).getLength();
		verify(song3).getLength();
		verifyNoMoreInteractions(songDAO, musicCache, song1, song2, song3);
	}

	/** Test method for {@link SongService#getTotalLengthByMusic(Music)} with not set DAO for songs. */
	@Test(expected = IllegalStateException.class)
	public void testGetTotalLengthByMusicWithNotSetSongDAO() {
		((SongServiceImpl) songService).setSongDAO(null);
		songService.getTotalLengthByMusic(mock(Music.class));
	}

	/** Test method for {@link SongService#getTotalLengthByMusic(Music)} with not set cache for music. */
	@Test(expected = IllegalStateException.class)
	public void testGetTotalLengthByMusicWithNotSetMusicCache() {
		((SongServiceImpl) songService).setMusicCache(null);
		songService.getTotalLengthByMusic(mock(Music.class));
	}

	/** Test method for {@link SongService#getTotalLengthByMusic(Music)} with null argument. */
	@Test
	public void testGetTotalLengthByMusicWithNullArgument() {
		try {
			songService.getTotalLengthByMusic(null);
			fail("Can't get total length by music with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(songDAO, musicCache);
	}

	/** Test method for {@link SongService#getTotalLengthByMusic(Music)} with exception in DAO tier. */
	@Test
	public void testGetTotalLengthByMusicWithDAOTierException() {
		final Music music = EntityGenerator.createMusic(Integer.MAX_VALUE);
		doThrow(DataStorageException.class).when(songDAO).findSongsByMusic(any(Music.class));
		when(musicCache.get(anyString())).thenReturn(null);

		try {
			songService.getTotalLengthByMusic(music);
			fail("Can't get total length by music with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(songDAO).findSongsByMusic(music);
		verify(musicCache).get("songs" + Integer.MAX_VALUE);
		verifyNoMoreInteractions(songDAO, musicCache);
	}

}
