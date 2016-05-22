package cz.vhromada.catalog.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
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
import cz.vhromada.catalog.dao.MusicDAO;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

/**
 * A class represents test for class {@link MusicDAOImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class MusicDAOImplTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Mock
    private EntityManager entityManager;

    /**
     * Query for music
     */
    @Mock
    private TypedQuery<Music> musicQuery;

    /**
     * Instance of {@link MusicDAO}
     */
    private MusicDAO musicDAO;

    /**
     * Initializes DAO for music.
     */
    @Before
    public void setUp() {
        musicDAO = new MusicDAOImpl(entityManager);
    }

    /**
     * Test method for {@link MusicDAOImpl#MusicDAOImpl(EntityManager)} with null entity manager.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullEntityManager() {
        new MusicDAOImpl(null);
    }

    /**
     * Test method for {@link MusicDAO#getMusic()}.
     */
    @Test
    public void testGetMusic() {
        when(entityManager.createNamedQuery(anyString(), eq(Music.class))).thenReturn(musicQuery);
        when(musicQuery.getResultList()).thenReturn(CollectionUtils.newList(MusicUtils.newMusic(MusicUtils.ID), MusicUtils.newMusic(2)));

        final List<Music> music = musicDAO.getMusic();

        MusicUtils.assertMusicDeepEquals(CollectionUtils.newList(MusicUtils.newMusic(MusicUtils.ID), MusicUtils.newMusic(2)), music);

        verify(entityManager).createNamedQuery(Music.SELECT_MUSIC, Music.class);
        verify(musicQuery).getResultList();
        verifyNoMoreInteractions(entityManager, musicQuery);
    }

    /**
     * Test method for {@link MusicDAO#getMusic()} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testGetMusic_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).createNamedQuery(anyString(), eq(Music.class));

        musicDAO.getMusic();
    }

    /**
     * Test method for {@link MusicDAO#getMusic(Integer)} with existing music.
     */
    @Test
    public void testGetMusicItem_ExistingMusic() {
        when(entityManager.find(eq(Music.class), anyInt())).thenReturn(MusicUtils.newMusic(MusicUtils.ID));

        final Music music = musicDAO.getMusic(MusicUtils.ID);

        MusicUtils.assertMusicDeepEquals(MusicUtils.newMusic(MusicUtils.ID), music);

        verify(entityManager).find(Music.class, MusicUtils.ID);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link MusicDAO#getMusic(Integer)} with not existing music.
     */
    @Test
    public void testGetMusicItem_NotExistingMusic() {
        when(entityManager.find(eq(Music.class), anyInt())).thenReturn(null);

        final Music music = musicDAO.getMusic(Integer.MAX_VALUE);

        assertNull(music);

        verify(entityManager).find(Music.class, Integer.MAX_VALUE);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link MusicDAO#getMusic(Integer)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetMusicItem_NullArgument() {
        musicDAO.getMusic(null);
    }

    /**
     * Test method for {@link MusicDAO#getMusic(Integer)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testGetMusicItem_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).find(eq(Music.class), anyInt());

        musicDAO.getMusic(Integer.MAX_VALUE);
    }

    /**
     * Test method for {@link MusicDAO#add(Music)}.
     */
    @Test
    public void testAdd() {
        final Music music = MusicUtils.newMusic(MusicUtils.ID);
        doAnswer(setId(MusicUtils.ID)).when(entityManager).persist(any(Music.class));

        musicDAO.add(music);

        assertEquals(MusicUtils.ID, music.getId());
        assertEquals(MusicUtils.ID - 1, music.getPosition());

        verify(entityManager).persist(music);
        verify(entityManager).merge(music);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link MusicDAO#add(Music)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullArgument() {
        musicDAO.add(null);
    }

    /**
     * Test method for {@link MusicDAO#add(Music)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testAdd_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).persist(any(Music.class));

        musicDAO.add(MusicUtils.newMusic(MusicUtils.ID));
    }

    /**
     * Test method for {@link MusicDAO#update(Music)}.
     */
    @Test
    public void testUpdate() {
        final Music music = MusicUtils.newMusic(MusicUtils.ID);

        musicDAO.update(music);

        verify(entityManager).merge(music);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link MusicDAO#update(Music)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        musicDAO.update(null);
    }

    /**
     * Test method for {@link MusicDAO#update(Music)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testUpdate_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).merge(any(Music.class));

        musicDAO.update(MusicUtils.newMusic(MusicUtils.ID));
    }

    /**
     * Test method for {@link MusicDAO#remove(Music)} with managed music.
     */
    @Test
    public void testRemove_ManagedMusic() {
        final Music music = MusicUtils.newMusic(MusicUtils.ID);
        when(entityManager.contains(any(Music.class))).thenReturn(true);

        musicDAO.remove(music);

        verify(entityManager).contains(music);
        verify(entityManager).remove(music);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link MusicDAO#remove(Music)} with not managed music.
     */
    @Test
    public void testRemove_NotManagedMusic() {
        final Music music = MusicUtils.newMusic(MusicUtils.ID);
        when(entityManager.contains(any(Music.class))).thenReturn(false);
        when(entityManager.getReference(eq(Music.class), anyInt())).thenReturn(music);

        musicDAO.remove(music);

        verify(entityManager).contains(music);
        verify(entityManager).getReference(Music.class, music.getId());
        verify(entityManager).remove(music);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link MusicDAO#remove(Music)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        musicDAO.remove(null);
    }

    /**
     * Test method for {@link MusicDAO#remove(Music)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testRemove_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).contains(any(Music.class));

        musicDAO.remove(MusicUtils.newMusic(MusicUtils.ID));
    }

    /**
     * Sets ID.
     *
     * @param id ID
     * @return mocked answer
     */
    private static Answer<Void> setId(final Integer id) {
        return invocation -> {
            ((Music) invocation.getArguments()[0]).setId(id);
            return null;
        };
    }

}
