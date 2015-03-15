package cz.vhromada.catalog.dao.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.dao.SongDAO;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.dao.entities.Song;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.test.DeepAsserts;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

/**
 * A class represents test for class {@link SongDAOImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class SongDAOImplTest extends ObjectGeneratorTest {

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
    public void testConstructorWithNullEntityManager() {
        new SongDAOImpl(null);
    }

    /**
     * Test method for {@link SongDAO#getSong(Integer)} with existing song.
     */
    @Test
    public void testGetSongWithExistingSong() {
        final int id = generate(Integer.class);
        final Song song = mock(Song.class);
        when(entityManager.find(eq(Song.class), anyInt())).thenReturn(song);

        DeepAsserts.assertEquals(song, songDAO.getSong(id));

        verify(entityManager).find(Song.class, id);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link SongDAO#getSong(Integer)} with not existing song.
     */
    @Test
    public void testGetSongWithNotExistingSong() {
        when(entityManager.find(eq(Song.class), anyInt())).thenReturn(null);

        assertNull(songDAO.getSong(Integer.MAX_VALUE));

        verify(entityManager).find(Song.class, Integer.MAX_VALUE);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link SongDAO#getSong(Integer)} with null argument.
     */
    @Test
    public void testGetSongWithNullArgument() {
        try {
            songDAO.getSong(null);
            fail("Can't get song with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(entityManager);
    }

    /**
     * Test method for {@link SongDAO#getSong(Integer)} with exception in persistence.
     */
    @Test
    public void testGetSongWithPersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).find(eq(Song.class), anyInt());

        try {
            songDAO.getSong(Integer.MAX_VALUE);
            fail("Can't get song with not thrown DataStorageException for exception in persistence.");
        } catch (final DataStorageException ex) {
            // OK
        }

        verify(entityManager).find(Song.class, Integer.MAX_VALUE);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link SongDAO#add(Song)}.
     */
    @Test
    public void testAdd() {
        final Song song = generate(Song.class);
        final int id = generate(Integer.class);
        doAnswer(setId(id)).when(entityManager).persist(any(Song.class));

        songDAO.add(song);
        DeepAsserts.assertEquals(id, song.getId());
        DeepAsserts.assertEquals(id - 1, song.getPosition());

        verify(entityManager).persist(song);
        verify(entityManager).merge(song);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link SongDAO#add(Song)} with null argument.
     */
    @Test
    public void testAddWithNullArgument() {
        try {
            songDAO.add(null);
            fail("Can't add song with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(entityManager);
    }

    /**
     * Test method for {@link SongDAO#add(Song)} with exception in persistence.
     */
    @Test
    public void testAddWithPersistenceException() {
        final Song song = generate(Song.class);
        doThrow(PersistenceException.class).when(entityManager).persist(any(Song.class));

        try {
            songDAO.add(song);
            fail("Can't add song with not thrown DataStorageException for exception in persistence.");
        } catch (final DataStorageException ex) {
            // OK
        }

        verify(entityManager).persist(song);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link SongDAO#update(Song)}.
     */
    @Test
    public void testUpdate() {
        final Song song = generate(Song.class);

        songDAO.update(song);

        verify(entityManager).merge(song);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link SongDAO#update(Song)} with null argument.
     */
    @Test
    public void testUpdateWithNullArgument() {
        try {
            songDAO.update(null);
            fail("Can't update song with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(entityManager);
    }

    /**
     * Test method for {@link SongDAO#update(Song)} with exception in persistence.
     */
    @Test
    public void testUpdateWithPersistenceException() {
        final Song song = generate(Song.class);
        doThrow(PersistenceException.class).when(entityManager).merge(any(Song.class));

        try {
            songDAO.update(song);
            fail("Can't update song with not thrown DataStorageException for exception in persistence.");
        } catch (final DataStorageException ex) {
            // OK
        }

        verify(entityManager).merge(song);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link SongDAO#remove(Song)} with managed song.
     */
    @Test
    public void testRemoveWithManagedSong() {
        final Song song = generate(Song.class);
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
    public void testRemoveWithNotManagedSong() {
        final Song song = generate(Song.class);
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
    @Test
    public void testRemoveWithNullArgument() {
        try {
            songDAO.remove(null);
            fail("Can't remove song with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(entityManager);
    }

    /**
     * Test method for {@link SongDAO#remove(Song)} with exception in persistence.
     */
    @Test
    public void testRemoveWithPersistenceException() {
        final Song song = generate(Song.class);
        doThrow(PersistenceException.class).when(entityManager).contains(any(Song.class));

        try {
            songDAO.remove(song);
            fail("Can't remove song with not thrown DataStorageException for exception in persistence.");
        } catch (final DataStorageException ex) {
            // OK
        }

        verify(entityManager).contains(song);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link SongDAO#findSongsByMusic(Music)}.
     */
    @Test
    public void testFindSongsByMusic() {
        final Music music = generate(Music.class);
        final List<Song> songs = CollectionUtils.newList(generate(Song.class), generate(Song.class));
        when(entityManager.createNamedQuery(anyString(), eq(Song.class))).thenReturn(songsQuery);
        when(songsQuery.getResultList()).thenReturn(songs);

        DeepAsserts.assertEquals(songs, songDAO.findSongsByMusic(music));

        verify(entityManager).createNamedQuery(Song.FIND_BY_MUSIC, Song.class);
        verify(songsQuery).getResultList();
        verify(songsQuery).setParameter("music", music.getId());
        verifyNoMoreInteractions(entityManager, songsQuery);
    }

    /**
     * Test method for {@link SongDAO#findSongsByMusic(Music)} with null argument.
     */
    @Test
    public void testFindSongsByMusicWithNullArgument() {
        try {
            songDAO.findSongsByMusic(null);
            fail("Can't find songs by music with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(entityManager, songsQuery);
    }

    /**
     * Test method for {@link SongDAO#findSongsByMusic(Music)} with exception in persistence.
     */
    @Test
    public void testFindSongsByMusicWithPersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).createNamedQuery(anyString(), eq(Song.class));

        try {
            songDAO.findSongsByMusic(generate(Music.class));
            fail("Can't find songs by music with not thrown DataStorageException for exception in persistence.");
        } catch (final DataStorageException ex) {
            // OK
        }

        verify(entityManager).createNamedQuery(Song.FIND_BY_MUSIC, Song.class);
        verifyNoMoreInteractions(entityManager);
        verifyZeroInteractions(songsQuery);
    }

    /**
     * Sets ID.
     *
     * @param id ID
     * @return mocked answer
     */
    private static Answer<Void> setId(final Integer id) {
        return new Answer<Void>() {

            @Override
            public Void answer(final InvocationOnMock invocation) {
                ((Song) invocation.getArguments()[0]).setId(id);
                return null;
            }

        };
    }

}
