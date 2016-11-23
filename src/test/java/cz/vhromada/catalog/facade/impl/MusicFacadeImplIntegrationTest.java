package cz.vhromada.catalog.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.common.MusicUtils;
import cz.vhromada.catalog.common.SongUtils;
import cz.vhromada.catalog.common.Time;
import cz.vhromada.catalog.domain.Music;
import cz.vhromada.catalog.domain.Song;
import cz.vhromada.catalog.entity.MusicTO;
import cz.vhromada.catalog.facade.MusicFacade;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents integration test for class {@link MusicFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeContext.xml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class MusicFacadeImplIntegrationTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private EntityManager entityManager;

    /**
     * Instance of {@link MusicFacade}
     */
    @Autowired
    private MusicFacade musicFacade;

    /**
     * Test method for {@link MusicFacade#newData()}.
     */
    @Test
    @DirtiesContext
    public void testNewData() {
        musicFacade.newData();

        assertEquals(0, MusicUtils.getMusicCount(entityManager));
        assertEquals(0, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for {@link MusicFacade#getMusic()}.
     */
    @Test
    public void testGetMusic() {
        MusicUtils.assertMusicListDeepEquals(musicFacade.getMusic(), MusicUtils.getMusic());

        assertEquals(MusicUtils.MUSIC_COUNT, MusicUtils.getMusicCount(entityManager));
        assertEquals(SongUtils.SONGS_COUNT, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for {@link MusicFacade#getMusic(Integer)}.
     */
    @Test
    public void testGetMusicById() {
        for (int i = 1; i <= MusicUtils.MUSIC_COUNT; i++) {
            MusicUtils.assertMusicDeepEquals(musicFacade.getMusic(i), MusicUtils.getMusic(i));
        }

        assertNull(musicFacade.getMusic(Integer.MAX_VALUE));

        assertEquals(MusicUtils.MUSIC_COUNT, MusicUtils.getMusicCount(entityManager));
        assertEquals(SongUtils.SONGS_COUNT, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for {@link MusicFacade#getMusic(Integer)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetMusicById_NullArgument() {
        musicFacade.getMusic(null);
    }

    /**
     * Test method for {@link MusicFacade#add(MusicTO)}.
     */
    @Test
    @DirtiesContext
    public void testAdd() {
        musicFacade.add(MusicUtils.newMusicTO(null));

        final Music addedMusic = MusicUtils.getMusic(entityManager, MusicUtils.MUSIC_COUNT + 1);
        MusicUtils.assertMusicDeepEquals(MusicUtils.newMusic(MusicUtils.MUSIC_COUNT + 1), addedMusic);

        assertEquals(MusicUtils.MUSIC_COUNT + 1, MusicUtils.getMusicCount(entityManager));
        assertEquals(SongUtils.SONGS_COUNT, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for {@link MusicFacade#add(MusicTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullArgument() {
        musicFacade.add(null);
    }

    /**
     * Test method for {@link MusicFacade#add(MusicTO)} with music with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NotNullId() {
        musicFacade.add(MusicUtils.newMusicTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MusicFacade#add(MusicTO)} with music with null name.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullName() {
        final MusicTO music = MusicUtils.newMusicTO(null);
        music.setName(null);

        musicFacade.add(music);
    }

    /**
     * Test method for {@link MusicFacade#add(MusicTO)} with music with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_EmptyName() {
        final MusicTO music = MusicUtils.newMusicTO(null);
        music.setName("");

        musicFacade.add(music);
    }

    /**
     * Test method for {@link MusicFacade#add(MusicTO)} with music with null URL to english Wikipedia about music.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullWikiEn() {
        final MusicTO music = MusicUtils.newMusicTO(null);
        music.setWikiEn(null);

        musicFacade.add(music);
    }

    /**
     * Test method for {@link MusicFacade#add(MusicTO)} with music with null URL to czech Wikipedia about music.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullWikiCz() {
        final MusicTO music = MusicUtils.newMusicTO(null);
        music.setWikiCz(null);

        musicFacade.add(music);
    }

    /**
     * Test method for {@link MusicFacade#add(MusicTO)} with music with not positive count of media.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NotPositiveMediaCount() {
        final MusicTO music = MusicUtils.newMusicTO(null);
        music.setMediaCount(0);

        musicFacade.add(music);
    }

    /**
     * Test method for {@link MusicFacade#add(MusicTO)} with music with null note.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullNote() {
        final MusicTO music = MusicUtils.newMusicTO(null);
        music.setNote(null);

        musicFacade.add(music);
    }

    /**
     * Test method for {@link MusicFacade#update(MusicTO)}.
     */
    @Test
    @DirtiesContext
    public void testUpdate() {
        final MusicTO music = MusicUtils.newMusicTO(1);

        musicFacade.update(music);

        final Music updatedMusic = MusicUtils.getMusic(entityManager, 1);
        MusicUtils.assertMusicDeepEquals(music, updatedMusic);

        assertEquals(MusicUtils.MUSIC_COUNT, MusicUtils.getMusicCount(entityManager));
        assertEquals(SongUtils.SONGS_COUNT, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for {@link MusicFacade#update(MusicTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        musicFacade.update(null);
    }

    /**
     * Test method for {@link MusicFacade#update(MusicTO)} with music with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullId() {
        musicFacade.update(MusicUtils.newMusicTO(null));
    }

    /**
     * Test method for {@link MusicFacade#update(MusicTO)} with music with null name.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullName() {
        final MusicTO music = MusicUtils.newMusicTO(1);
        music.setName(null);

        musicFacade.update(music);
    }

    /**
     * Test method for {@link MusicFacade#update(MusicTO)} with music with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_EmptyName() {
        final MusicTO music = MusicUtils.newMusicTO(1);
        music.setName(null);

        musicFacade.update(music);
    }

    /**
     * Test method for {@link MusicFacade#update(MusicTO)} with music with null URL to english Wikipedia about music.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullWikiEn() {
        final MusicTO music = MusicUtils.newMusicTO(1);
        music.setWikiEn(null);

        musicFacade.update(music);
    }

    /**
     * Test method for {@link MusicFacade#update(MusicTO)} with music with null URL to czech Wikipedia about music.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullWikiCz() {
        final MusicTO music = MusicUtils.newMusicTO(1);
        music.setWikiCz(null);

        musicFacade.update(music);
    }

    /**
     * Test method for {@link MusicFacade#update(MusicTO)} with music with not positive count of media.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NotPositiveMediaCount() {
        final MusicTO music = MusicUtils.newMusicTO(1);
        music.setMediaCount(0);

        musicFacade.update(music);
    }

    /**
     * Test method for {@link MusicFacade#update(MusicTO)} with music with null note.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullNote() {
        final MusicTO music = MusicUtils.newMusicTO(1);
        music.setNote(null);

        musicFacade.update(music);
    }

    /**
     * Test method for {@link MusicFacade#update(MusicTO)} with music with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testUpdate_BadId() {
        musicFacade.update(MusicUtils.newMusicTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MusicFacade#remove(MusicTO)}.
     */
    @Test
    @DirtiesContext
    public void testRemove() {
        musicFacade.remove(MusicUtils.newMusicTO(1));

        assertNull(MusicUtils.getMusic(entityManager, 1));

        assertEquals(MusicUtils.MUSIC_COUNT - 1, MusicUtils.getMusicCount(entityManager));
        assertEquals(SongUtils.SONGS_COUNT - SongUtils.SONGS_PER_MUSIC_COUNT, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for {@link MusicFacade#remove(MusicTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        musicFacade.remove(null);
    }

    /**
     * Test method for {@link MusicFacade#remove(MusicTO)} with music with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testRemove_NullId() {
        musicFacade.remove(MusicUtils.newMusicTO(null));
    }

    /**
     * Test method for {@link MusicFacade#remove(MusicTO)} with music with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testRemove_BadId() {
        musicFacade.remove(MusicUtils.newMusicTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MusicFacade#duplicate(MusicTO)}.
     */
    @Test
    @DirtiesContext
    public void testDuplicate() {
        final Music music = MusicUtils.getMusic(MusicUtils.MUSIC_COUNT);
        music.setId(MusicUtils.MUSIC_COUNT + 1);
        for (final Song song : music.getSongs()) {
            song.setId(SongUtils.SONGS_COUNT + music.getSongs().indexOf(song) + 1);
        }

        musicFacade.duplicate(MusicUtils.newMusicTO(MusicUtils.MUSIC_COUNT));

        final Music duplicatedMusic = MusicUtils.getMusic(entityManager, MusicUtils.MUSIC_COUNT + 1);
        MusicUtils.assertMusicDeepEquals(music, duplicatedMusic);

        assertEquals(MusicUtils.MUSIC_COUNT + 1, MusicUtils.getMusicCount(entityManager));
        assertEquals(SongUtils.SONGS_COUNT + SongUtils.SONGS_PER_MUSIC_COUNT, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for {@link MusicFacade#duplicate(MusicTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        musicFacade.duplicate(null);
    }

    /**
     * Test method for {@link MusicFacade#duplicate(MusicTO)} with music with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testDuplicate_NullId() {
        musicFacade.duplicate(MusicUtils.newMusicTO(null));
    }

    /**
     * Test method for {@link MusicFacade#duplicate(MusicTO)} with music with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testDuplicate_BadId() {
        musicFacade.duplicate(MusicUtils.newMusicTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MusicFacade#moveUp(MusicTO)}.
     */
    @Test
    @DirtiesContext
    public void testMoveUp() {
        final Music music1 = MusicUtils.getMusic(1);
        music1.setPosition(1);
        final Music music2 = MusicUtils.getMusic(2);
        music2.setPosition(0);

        musicFacade.moveUp(MusicUtils.newMusicTO(2));

        MusicUtils.assertMusicDeepEquals(music1, MusicUtils.getMusic(entityManager, 1));
        MusicUtils.assertMusicDeepEquals(music2, MusicUtils.getMusic(entityManager, 2));
        for (int i = 3; i <= MusicUtils.MUSIC_COUNT; i++) {
            MusicUtils.assertMusicDeepEquals(MusicUtils.getMusic(i), MusicUtils.getMusic(entityManager, i));
        }

        assertEquals(MusicUtils.MUSIC_COUNT, MusicUtils.getMusicCount(entityManager));
        assertEquals(SongUtils.SONGS_COUNT, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for {@link MusicFacade#moveUp(MusicTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullArgument() {
        musicFacade.moveUp(null);
    }

    /**
     * Test method for {@link MusicFacade#moveUp(MusicTO)} with music with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_NullId() {
        musicFacade.moveUp(MusicUtils.newMusicTO(null));
    }

    /**
     * Test method for {@link MusicFacade#moveUp(MusicTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_NotMovableArgument() {
        musicFacade.moveUp(MusicUtils.newMusicTO(1));
    }

    /**
     * Test method for {@link MusicFacade#moveUp(MusicTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveUp_BadId() {
        musicFacade.moveUp(MusicUtils.newMusicTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MusicFacade#moveDown(MusicTO)}.
     */
    @Test
    @DirtiesContext
    public void testMoveDown() {
        final Music music1 = MusicUtils.getMusic(1);
        music1.setPosition(1);
        final Music music2 = MusicUtils.getMusic(2);
        music2.setPosition(0);

        musicFacade.moveDown(MusicUtils.newMusicTO(1));

        MusicUtils.assertMusicDeepEquals(music1, MusicUtils.getMusic(entityManager, 1));
        MusicUtils.assertMusicDeepEquals(music2, MusicUtils.getMusic(entityManager, 2));
        for (int i = 3; i <= MusicUtils.MUSIC_COUNT; i++) {
            MusicUtils.assertMusicDeepEquals(MusicUtils.getMusic(i), MusicUtils.getMusic(entityManager, i));
        }

        assertEquals(MusicUtils.MUSIC_COUNT, MusicUtils.getMusicCount(entityManager));
        assertEquals(SongUtils.SONGS_COUNT, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for {@link MusicFacade#moveDown(MusicTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullArgument() {
        musicFacade.moveDown(null);
    }

    /**
     * Test method for {@link MusicFacade#moveDown(MusicTO)} with music with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_NullId() {
        musicFacade.moveDown(MusicUtils.newMusicTO(null));
    }

    /**
     * Test method for {@link MusicFacade#moveDown(MusicTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_NotMovableArgument() {
        musicFacade.moveDown(MusicUtils.newMusicTO(MusicUtils.MUSIC_COUNT));
    }

    /**
     * Test method for {@link MusicFacade#moveDown(MusicTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveDown_BadId() {
        musicFacade.moveDown(MusicUtils.newMusicTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MusicFacade#updatePositions()}.
     */
    @Test
    @DirtiesContext
    public void testUpdatePositions() {
        musicFacade.updatePositions();

        for (int i = 1; i <= MusicUtils.MUSIC_COUNT; i++) {
            MusicUtils.assertMusicDeepEquals(MusicUtils.getMusic(i), MusicUtils.getMusic(entityManager, i));
        }

        assertEquals(MusicUtils.MUSIC_COUNT, MusicUtils.getMusicCount(entityManager));
        assertEquals(SongUtils.SONGS_COUNT, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for {@link MusicFacade#getTotalMediaCount()}.
     */
    @Test
    public void testGetTotalMediaCount() {
        final int count = 60;

        assertEquals(count, musicFacade.getTotalMediaCount());

        assertEquals(MusicUtils.MUSIC_COUNT, MusicUtils.getMusicCount(entityManager));
        assertEquals(SongUtils.SONGS_COUNT, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for {@link MusicFacade#getTotalLength()}.
     */
    @Test
    public void testGetTotalLength() {
        final Time length = new Time(666);

        assertEquals(length, musicFacade.getTotalLength());

        assertEquals(MusicUtils.MUSIC_COUNT, MusicUtils.getMusicCount(entityManager));
        assertEquals(SongUtils.SONGS_COUNT, SongUtils.getSongsCount(entityManager));
    }

    /**
     * Test method for {@link MusicFacade#getSongsCount()}.
     */
    @Test
    public void testGetSongsCount() {
        assertEquals(SongUtils.SONGS_COUNT, musicFacade.getSongsCount());

        assertEquals(MusicUtils.MUSIC_COUNT, MusicUtils.getMusicCount(entityManager));
        assertEquals(SongUtils.SONGS_COUNT, SongUtils.getSongsCount(entityManager));
    }

}
