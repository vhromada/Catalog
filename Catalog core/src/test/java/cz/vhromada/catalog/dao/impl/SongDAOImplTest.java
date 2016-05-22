package cz.vhromada.catalog.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.MusicUtils;
import cz.vhromada.catalog.commons.SongUtils;
import cz.vhromada.catalog.dao.SongDAO;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.dao.entities.Song;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

/**
 * A class represents test for class {@link SongDAOImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class SongDAOImplTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Mock
    private EntityManager entityManager;

    /**
     * Query for songs
     */
    @Mock
    private TypedQuery<Song> songsQuery;

    /**
     * Instance of {@link SongDAO}
     */
    private SongDAO songDAO;

    /**
     * Initializes DAO for songs.
     */
    @Before
    public void setUp() {
        songDAO = new SongDAOImpl(entityManager);
    }

    /**
     * Test method for {@link SongDAOImpl#SongDAOImpl(EntityManager)} with null entity manager.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullEntityManager() {
        new SongDAOImpl(null);
    }

    /**
     * Test method for {@link SongDAO#getSong(Integer)} with existing song.
     */
    @Test
    public void testGetSong_ExistingSong() {
        when(entityManager.find(eq(Song.class), anyInt())).thenReturn(SongUtils.newSong(SongUtils.ID));

        final Song song = songDAO.getSong(SongUtils.ID);

        SongUtils.assertSongDeepEquals(SongUtils.newSong(SongUtils.ID), song);

        verify(entityManager).find(Song.class, SongUtils.ID);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link SongDAO#getSong(Integer)} with not existing song.
     */
    @Test
    public void testGetSong_NotExistingSong() {
        when(entityManager.find(eq(Song.class), anyInt())).thenReturn(null);

        final Song song = songDAO.getSong(Integer.MAX_VALUE);

        assertNull(song);

        verify(entityManager).find(Song.class, Integer.MAX_VALUE);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link SongDAO#getSong(Integer)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetSong_NullArgument() {
        songDAO.getSong(null);
    }

    /**
     * Test method for {@link SongDAO#getSong(Integer)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testGetSong_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).find(eq(Song.class), anyInt());

        songDAO.getSong(Integer.MAX_VALUE);
    }

    /**
     * Test method for {@link SongDAO#add(Song)}.
     */
    @Test
    public void testAdd() {
        final Song song = SongUtils.newSong(SongUtils.ID);
        doAnswer(setId(SongUtils.ID)).when(entityManager).persist(any(Song.class));

        songDAO.add(song);

        assertEquals(SongUtils.ID, song.getId());
        assertEquals(SongUtils.ID - 1, song.getPosition());

        verify(entityManager).persist(song);
        verify(entityManager).merge(song);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link SongDAO#add(Song)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullArgument() {
        songDAO.add(null);
    }

    /**
     * Test method for {@link SongDAO#add(Song)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testAdd_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).persist(any(Song.class));

        songDAO.add(SongUtils.newSong(SongUtils.ID));
    }

    /**
     * Test method for {@link SongDAO#update(Song)}.
     */
    @Test
    public void testUpdate() {
        final Song song = SongUtils.newSong(SongUtils.ID);

        songDAO.update(song);

        verify(entityManager).merge(song);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link SongDAO#update(Song)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        songDAO.update(null);
    }

    /**
     * Test method for {@link SongDAO#update(Song)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testUpdate_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).merge(any(Song.class));

        songDAO.update(SongUtils.newSong(SongUtils.ID));
    }

    /**
     * Test method for {@link SongDAO#remove(Song)} with managed song.
     */
    @Test
    public void testRemove_ManagedSong() {
        final Song song = SongUtils.newSong(SongUtils.ID);
        when(entityManager.contains(any(Song.class))).thenReturn(true);

        songDAO.remove(song);

        verify(entityManager).contains(song);
        verify(entityManager).remove(song);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link SongDAO#remove(Song)} with not managed song.
     */
    @Test
    public void testRemove_NotManagedSong() {
        final Song song = SongUtils.newSong(SongUtils.ID);
        when(entityManager.contains(any(Song.class))).thenReturn(false);
        when(entityManager.getReference(eq(Song.class), anyInt())).thenReturn(song);

        songDAO.remove(song);

        verify(entityManager).contains(song);
        verify(entityManager).getReference(Song.class, song.getId());
        verify(entityManager).remove(song);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link SongDAO#remove(Song)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        songDAO.remove(null);
    }

    /**
     * Test method for {@link SongDAO#remove(Song)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testRemove_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).contains(any(Song.class));

        songDAO.remove(SongUtils.newSong(SongUtils.ID));
    }

    /**
     * Test method for {@link SongDAO#findSongsByMusic(Music)}.
     */
    @Test
    public void testFindSongsByMusic() {
        final Music music = MusicUtils.newMusic(MusicUtils.ID);
        when(entityManager.createNamedQuery(anyString(), eq(Song.class))).thenReturn(songsQuery);
        when(songsQuery.getResultList()).thenReturn(CollectionUtils.newList(SongUtils.newSong(SongUtils.ID), SongUtils.newSong(2)));

        final List<Song> songs = songDAO.findSongsByMusic(music);

        SongUtils.assertSongsDeepEquals(CollectionUtils.newList(SongUtils.newSong(SongUtils.ID), SongUtils.newSong(2)), songs);

        verify(entityManager).createNamedQuery(Song.FIND_BY_MUSIC, Song.class);
        verify(songsQuery).setParameter("music", MusicUtils.ID);
        verify(songsQuery).getResultList();
        verifyNoMoreInteractions(entityManager, songsQuery);
    }

    /**
     * Test method for {@link SongDAO#findSongsByMusic(Music)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFindSongsByMusic_NullArgument() {
        songDAO.findSongsByMusic(null);
    }

    /**
     * Test method for {@link SongDAO#findSongsByMusic(Music)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testFindSongsByMusic_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).createNamedQuery(anyString(), eq(Song.class));

        songDAO.findSongsByMusic(MusicUtils.newMusic(Integer.MAX_VALUE));
    }

    /**
     * Sets ID.
     *
     * @param id ID
     * @return mocked answer
     */
    private static Answer<Void> setId(final Integer id) {
        return invocation -> {
            ((Song) invocation.getArguments()[0]).setId(id);
            return null;
        };
    }

}
