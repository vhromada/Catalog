package cz.vhromada.catalog.dao.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
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
import cz.vhromada.catalog.dao.MusicDAO;
import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

/**
 * A class represents test for class {@link MusicDAOImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class MusicDAOImplTest extends ObjectGeneratorTest {

	/** Instance of {@link EntityManager} */
	@Mock
	private EntityManager entityManager;

	/** Query for music */
	@Mock
	private TypedQuery<Music> musicQuery;

	/** Instance of {@link MusicDAO} */
	@InjectMocks
	private MusicDAO musicDAO = new MusicDAOImpl();

	/** Test method for {@link MusicDAO#getMusic()}. */
	@Test
	public void testGetMusic() {
		final List<Music> music = CollectionUtils.newList(generate(Music.class), generate(Music.class));
		when(entityManager.createNamedQuery(anyString(), eq(Music.class))).thenReturn(musicQuery);
		when(musicQuery.getResultList()).thenReturn(music);

		DeepAsserts.assertEquals(music, musicDAO.getMusic());

		verify(entityManager).createNamedQuery(Music.SELECT_MUSIC, Music.class);
		verify(musicQuery).getResultList();
		verifyNoMoreInteractions(entityManager, musicQuery);
	}

	/** Test method for {@link MusicDAOImpl#getMusic()} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testGetMusicWithNotSetEntityManager() {
		((MusicDAOImpl) musicDAO).setEntityManager(null);
		musicDAO.getMusic();
	}

	/** Test method for {@link MusicDAOImpl#getMusic()} with exception in persistence. */
	@Test
	public void testGetMusicWithPersistenceException() {
		doThrow(PersistenceException.class).when(entityManager).createNamedQuery(anyString(), eq(Music.class));

		try {
			musicDAO.getMusic();
			fail("Can't get music with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).createNamedQuery(Music.SELECT_MUSIC, Music.class);
		verifyNoMoreInteractions(entityManager);
		verifyZeroInteractions(musicQuery);
	}

	/** Test method for {@link MusicDAO#getMusic(Integer)} with existing music. */
	@Test
	public void testGetMusicByIdWithExistingMusic() {
		final int id = generate(Integer.class);
		final Music music = mock(Music.class);
		when(entityManager.find(eq(Music.class), anyInt())).thenReturn(music);

		DeepAsserts.assertEquals(music, musicDAO.getMusic(id));

		verify(entityManager).find(Music.class, id);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link MusicDAO#getMusic(Integer)} with not existing music. */
	@Test
	public void testGetMusicByIdWithNotExistingMusic() {
		when(entityManager.find(eq(Music.class), anyInt())).thenReturn(null);

		assertNull(musicDAO.getMusic(Integer.MAX_VALUE));

		verify(entityManager).find(Music.class, Integer.MAX_VALUE);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link MusicDAOImpl#getMusic(Integer)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testGetMusicByIdWithNotSetEntityManager() {
		((MusicDAOImpl) musicDAO).setEntityManager(null);
		musicDAO.getMusic(Integer.MAX_VALUE);
	}

	/** Test method for {@link MusicDAO#getMusic(Integer)} with null argument. */
	@Test
	public void testGetMusicByIdWithNullArgument() {
		try {
			musicDAO.getMusic(null);
			fail("Can't get music with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager);
	}

	/** Test method for {@link MusicDAOImpl#getMusic(Integer)} with exception in persistence. */
	@Test
	public void testGetMusicByIdWithPersistenceException() {
		doThrow(PersistenceException.class).when(entityManager).find(eq(Music.class), anyInt());

		try {
			musicDAO.getMusic(Integer.MAX_VALUE);
			fail("Can't get music with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).find(Music.class, Integer.MAX_VALUE);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link MusicDAO#add(Music)} . */
	@Test
	public void testAdd() {
		final Music music = generate(Music.class);
		final int id = generate(Integer.class);
		doAnswer(setId(id)).when(entityManager).persist(any(Music.class));

		musicDAO.add(music);
		DeepAsserts.assertEquals(id, music.getId());
		DeepAsserts.assertEquals(id - 1, music.getPosition());

		verify(entityManager).persist(music);
		verify(entityManager).merge(music);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link MusicDAOImpl#add(Music)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetEntityManager() {
		((MusicDAOImpl) musicDAO).setEntityManager(null);
		musicDAO.add(mock(Music.class));
	}

	/** Test method for {@link MusicDAO#add(Music)} with null argument. */
	@Test
	public void testAddWithNullArgument() {
		try {
			musicDAO.add(null);
			fail("Can't add music with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager);
	}

	/** Test method for {@link MusicDAOImpl#add(Music)} with exception in persistence. */
	@Test
	public void testAddWithPersistenceException() {
		final Music music = generate(Music.class);
		doThrow(PersistenceException.class).when(entityManager).persist(any(Music.class));

		try {
			musicDAO.add(music);
			fail("Can't add music with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).persist(music);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link MusicDAO#update(Music)}. */
	@Test
	public void testUpdate() {
		final Music music = generate(Music.class);

		musicDAO.update(music);

		verify(entityManager).merge(music);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link MusicDAOImpl#update(Music)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetEntityManager() {
		((MusicDAOImpl) musicDAO).setEntityManager(null);
		musicDAO.update(mock(Music.class));
	}

	/** Test method for {@link MusicDAO#update(Music)} with null argument. */
	@Test
	public void testUpdateWithNullArgument() {
		try {
			musicDAO.update(null);
			fail("Can't update music with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager);
	}

	/** Test method for {@link MusicDAOImpl#update(Music)} with exception in persistence. */
	@Test
	public void testUpdateWithPersistenceException() {
		final Music music = generate(Music.class);
		doThrow(PersistenceException.class).when(entityManager).merge(any(Music.class));

		try {
			musicDAO.update(music);
			fail("Can't update music with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).merge(music);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link MusicDAO#remove(Music)} with managed music. */
	@Test
	public void testRemoveWithManagedMusic() {
		final Music music = generate(Music.class);
		when(entityManager.contains(any(Music.class))).thenReturn(true);

		musicDAO.remove(music);

		verify(entityManager).contains(music);
		verify(entityManager).remove(music);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link MusicDAO#remove(Music)} with not managed music. */
	@Test
	public void testRemoveWithNotManagedMusic() {
		final Music music = generate(Music.class);
		when(entityManager.contains(any(Music.class))).thenReturn(false);
		when(entityManager.getReference(eq(Music.class), anyInt())).thenReturn(music);

		musicDAO.remove(music);

		verify(entityManager).contains(music);
		verify(entityManager).getReference(Music.class, music.getId());
		verify(entityManager).remove(music);
		verifyNoMoreInteractions(entityManager);
	}

	/** Test method for {@link MusicDAOImpl#remove(Music)} with not set entity manager. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetEntityManager() {
		((MusicDAOImpl) musicDAO).setEntityManager(null);
		musicDAO.remove(mock(Music.class));
	}

	/** Test method for {@link MusicDAO#remove(Music)} with null argument. */
	@Test
	public void testRemoveWithNullArgument() {
		try {
			musicDAO.remove(null);
			fail("Can't remove music with null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(entityManager);
	}

	/** Test method for {@link MusicDAOImpl#remove(Music)} with exception in persistence. */
	@Test
	public void testRemoveWithPersistenceException() {
		final Music music = generate(Music.class);
		doThrow(PersistenceException.class).when(entityManager).contains(any(Music.class));

		try {
			musicDAO.remove(music);
			fail("Can't remove music with not thrown DataStorageException for exception in persistence.");
		} catch (final DataStorageException ex) {
			// OK
		}

		verify(entityManager).contains(music);
		verifyNoMoreInteractions(entityManager);
	}

	/**
	 * Sets ID.
	 *
	 * @param id ID
	 * @return mocked answer
	 */
	private Answer<Void> setId(final Integer id) {
		return new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				((Music) invocation.getArguments()[0]).setId(id);
				return null;
			}

		};
	}

}
