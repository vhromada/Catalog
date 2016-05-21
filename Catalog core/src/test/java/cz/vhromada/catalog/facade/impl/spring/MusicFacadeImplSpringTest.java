//package cz.vhromada.catalog.facade.impl.spring;
//
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.assertTrue;
//
//import javax.persistence.EntityManager;
//
//import cz.vhromada.catalog.commons.EntitiesUtils;
//import cz.vhromada.catalog.commons.SpringToUtils;
//import cz.vhromada.catalog.commons.SpringUtils;
//import cz.vhromada.catalog.commons.Time;
//import cz.vhromada.catalog.dao.entities.Music;
//import cz.vhromada.catalog.dao.entities.Song;
//import cz.vhromada.catalog.facade.MusicFacade;
//import cz.vhromada.catalog.facade.to.MusicTO;
//import cz.vhromada.generator.ObjectGenerator;
//import cz.vhromada.test.DeepAsserts;
//import cz.vhromada.validators.exceptions.RecordNotFoundException;
//import cz.vhromada.validators.exceptions.ValidationException;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.transaction.PlatformTransactionManager;
//
///**
// * A class represents test for class {@link cz.vhromada.catalog.facade.impl.MusicFacadeImpl}.
// *
// * @author Vladimir Hromada
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:testFacadeContext.xml")
//public class MusicFacadeImplSpringTest {
//
//    /**
//     * Instance of {@link EntityManager}
//     */
//    @Autowired
//    private EntityManager entityManager;
//
//    /**
//     * Instance of {@link PlatformTransactionManager}
//     */
//    @Autowired
//    private PlatformTransactionManager transactionManager;
//
//    /**
//     * Instance of {@link MusicFacade}
//     */
//    @Autowired
//    private MusicFacade musicFacade;
//
//    /**
//     * Instance of {@link ObjectGenerator}
//     */
//    @Autowired
//    private ObjectGenerator objectGenerator;
//
//    /**
//     * Initializes database.
//     */
//    @Before
//    public void setUp() {
//        SpringUtils.remove(transactionManager, entityManager, Song.class);
//        SpringUtils.remove(transactionManager, entityManager, Music.class);
//        SpringUtils.updateSequence(transactionManager, entityManager, "music_sq");
//        SpringUtils.updateSequence(transactionManager, entityManager, "songs_sq");
//        for (final Music music : EntitiesUtils.getMusic()) {
//            music.setId(null);
//            SpringUtils.persist(transactionManager, entityManager, music);
//        }
//        for (int i = 1; i <= SpringUtils.MUSIC_COUNT; i++) {
//            for (final Song song : EntitiesUtils.getSongs(i)) {
//                song.setId(null);
//                SpringUtils.persist(transactionManager, entityManager, song);
//            }
//        }
//    }
//
//    /**
//     * Test method for {@link MusicFacade#newData()}.
//     */
//    @Test
//    public void testNewData() {
//        musicFacade.newData();
//
//        DeepAsserts.assertEquals(0, SpringUtils.getMusicCount(entityManager));
//    }
//
//    /**
//     * Test method for {@link MusicFacade#getMusic()}.
//     */
//    @Test
//    public void testGetMusic() {
//        DeepAsserts.assertEquals(SpringToUtils.getMusic(), musicFacade.getMusic());
//        DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
//    }
//
//    /**
//     * Test method for {@link MusicFacade#getMusic(Integer)}.
//     */
//    @Test
//    public void testGetMusicById() {
//        for (int i = 1; i <= SpringUtils.MUSIC_COUNT; i++) {
//            DeepAsserts.assertEquals(SpringToUtils.getMusic(i), musicFacade.getMusic(i));
//        }
//
//        assertNull(musicFacade.getMusic(Integer.MAX_VALUE));
//
//        DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
//    }
//
//    /**
//     * Test method for {@link MusicFacade#getMusic(Integer)} with null argument.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testGetMusicByIdWithNullArgument() {
//        musicFacade.getMusic(null);
//    }
//
//    /**
//     * Test method for {@link MusicFacade#add(MusicTO)}.
//     */
//    @Test
//    public void testAdd() {
//        final MusicTO music = SpringToUtils.newMusic(objectGenerator);
//
//        musicFacade.add(music);
//
//        DeepAsserts.assertNotNull(music.getId());
//        DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT + 1, music.getId());
//        final Music addedMusic = SpringUtils.getMusic(entityManager, SpringUtils.MUSIC_COUNT + 1);
//        DeepAsserts.assertEquals(music, addedMusic, "songsCount", "totalLength");
//        DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT + 1, SpringUtils.getMusicCount(entityManager));
//    }
//
//    /**
//     * Test method for {@link MusicFacade#add(MusicTO)} with null argument.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testAddWithNullArgument() {
//        musicFacade.add(null);
//    }
//
//    /**
//     * Test method for {@link MusicFacade#add(MusicTO)} with music with not null ID.
//     */
//    @Test(expected = ValidationException.class)
//    public void testAddWithMusicWithNotNullId() {
//        musicFacade.add(SpringToUtils.newMusicWithId(objectGenerator));
//    }
//
//    /**
//     * Test method for {@link MusicFacade#add(MusicTO)} with music with null name.
//     */
//    @Test(expected = ValidationException.class)
//    public void testAddWithMusicWithNullName() {
//        final MusicTO music = SpringToUtils.newMusic(objectGenerator);
//        music.setName(null);
//
//        musicFacade.add(music);
//    }
//
//    /**
//     * Test method for {@link MusicFacade#add(MusicTO)} with music with empty string as name.
//     */
//    @Test(expected = ValidationException.class)
//    public void testAddWithMusicWithEmptyName() {
//        final MusicTO music = SpringToUtils.newMusic(objectGenerator);
//        music.setName("");
//
//        musicFacade.add(music);
//    }
//
//    /**
//     * Test method for {@link MusicFacade#add(MusicTO)} with music with null URL to english Wikipedia about music.
//     */
//    @Test(expected = ValidationException.class)
//    public void testAddWithMusicWithNullWikiEn() {
//        final MusicTO music = SpringToUtils.newMusic(objectGenerator);
//        music.setWikiEn(null);
//
//        musicFacade.add(music);
//    }
//
//    /**
//     * Test method for {@link MusicFacade#add(MusicTO)} with music with null URL to czech Wikipedia about music.
//     */
//    @Test(expected = ValidationException.class)
//    public void testAddWithMusicWithNullWikiCz() {
//        final MusicTO music = SpringToUtils.newMusic(objectGenerator);
//        music.setWikiCz(null);
//
//        musicFacade.add(music);
//    }
//
//    /**
//     * Test method for {@link MusicFacade#add(MusicTO)} with music with not positive count of media.
//     */
//    @Test(expected = ValidationException.class)
//    public void testAddWithMusicWithNotPositiveMediaCount() {
//        final MusicTO music = SpringToUtils.newMusic(objectGenerator);
//        music.setMediaCount(0);
//
//        musicFacade.add(music);
//    }
//
//    /**
//     * Test method for {@link MusicFacade#add(MusicTO)} with music with null note.
//     */
//    @Test(expected = ValidationException.class)
//    public void testAddWithMusicWithNullNote() {
//        final MusicTO music = SpringToUtils.newMusic(objectGenerator);
//        music.setNote(null);
//
//        musicFacade.add(music);
//    }
//
//    /**
//     * Test method for {@link MusicFacade#update(MusicTO)}.
//     */
//    @Test
//    public void testUpdate() {
//        final MusicTO music = SpringToUtils.newMusic(objectGenerator, 1);
//
//        musicFacade.update(music);
//
//        final Music updatedMusic = SpringUtils.getMusic(entityManager, 1);
//        DeepAsserts.assertEquals(music, updatedMusic, "songsCount", "totalLength");
//        DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
//    }
//
//    /**
//     * Test method for {@link MusicFacade#update(MusicTO)} with null argument.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testUpdateWithNullArgument() {
//        musicFacade.update(null);
//    }
//
//    /**
//     * Test method for {@link MusicFacade#update(MusicTO)} with music with null ID.
//     */
//    @Test(expected = ValidationException.class)
//    public void testUpdateWithMusicWithNullId() {
//        musicFacade.update(SpringToUtils.newMusic(objectGenerator));
//    }
//
//    /**
//     * Test method for {@link MusicFacade#update(MusicTO)} with music with null name.
//     */
//    @Test(expected = ValidationException.class)
//    public void testUpdateWithMusicWithNullName() {
//        final MusicTO music = SpringToUtils.newMusicWithId(objectGenerator);
//        music.setName(null);
//
//        musicFacade.update(music);
//    }
//
//    /**
//     * Test method for {@link MusicFacade#update(MusicTO)} with music with empty string as name.
//     */
//    @Test(expected = ValidationException.class)
//    public void testUpdateWithMusicWithEmptyName() {
//        final MusicTO music = SpringToUtils.newMusicWithId(objectGenerator);
//        music.setName(null);
//
//        musicFacade.update(music);
//    }
//
//    /**
//     * Test method for {@link MusicFacade#update(MusicTO)} with music with null URL to english Wikipedia about music.
//     */
//    @Test(expected = ValidationException.class)
//    public void testUpdateWithMusicWithNullWikiEn() {
//        final MusicTO music = SpringToUtils.newMusicWithId(objectGenerator);
//        music.setWikiEn(null);
//
//        musicFacade.update(music);
//    }
//
//    /**
//     * Test method for {@link MusicFacade#update(MusicTO)} with music with null URL to czech Wikipedia about music.
//     */
//    @Test(expected = ValidationException.class)
//    public void testUpdateWithMusicWithNullWikiCz() {
//        final MusicTO music = SpringToUtils.newMusicWithId(objectGenerator);
//        music.setWikiCz(null);
//
//        musicFacade.update(music);
//    }
//
//    /**
//     * Test method for {@link MusicFacade#update(MusicTO)} with music with not positive count of media.
//     */
//    @Test(expected = ValidationException.class)
//    public void testUpdateWithMusicWithNotPositiveMediaCount() {
//        final MusicTO music = SpringToUtils.newMusicWithId(objectGenerator);
//        music.setMediaCount(0);
//
//        musicFacade.update(music);
//    }
//
//    /**
//     * Test method for {@link MusicFacade#update(MusicTO)} with music with null note.
//     */
//    @Test(expected = ValidationException.class)
//    public void testUpdateWithMusicWithNullNote() {
//        final MusicTO music = SpringToUtils.newMusicWithId(objectGenerator);
//        music.setNote(null);
//
//        musicFacade.update(music);
//    }
//
//    /**
//     * Test method for {@link MusicFacade#update(MusicTO)} with music with bad ID.
//     */
//    @Test(expected = RecordNotFoundException.class)
//    public void testUpdateWithMusicWithBadId() {
//        musicFacade.update(SpringToUtils.newMusic(objectGenerator, Integer.MAX_VALUE));
//    }
//
//    /**
//     * Test method for {@link MusicFacade#remove(MusicTO)}.
//     */
//    @Test
//    public void testRemove() {
//        musicFacade.remove(SpringToUtils.newMusic(objectGenerator, 1));
//
//        assertNull(SpringUtils.getMusic(entityManager, 1));
//        DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT - 1, SpringUtils.getMusicCount(entityManager));
//    }
//
//    /**
//     * Test method for {@link MusicFacade#remove(MusicTO)} with null argument.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testRemoveWithNullArgument() {
//        musicFacade.remove(null);
//    }
//
//    /**
//     * Test method for {@link MusicFacade#remove(MusicTO)} with music with null ID.
//     */
//    @Test(expected = ValidationException.class)
//    public void testRemoveWithMusicWithNullId() {
//        musicFacade.remove(SpringToUtils.newMusic(objectGenerator));
//    }
//
//    /**
//     * Test method for {@link MusicFacade#remove(MusicTO)} with music with bad ID.
//     */
//    @Test(expected = RecordNotFoundException.class)
//    public void testRemoveWithMusicWithBadId() {
//        musicFacade.remove(SpringToUtils.newMusic(objectGenerator, Integer.MAX_VALUE));
//    }
//
//    /**
//     * Test method for {@link MusicFacade#duplicate(MusicTO)}.
//     */
//    @Test
//    public void testDuplicate() {
//        final Music music = EntitiesUtils.getMusic(SpringUtils.MUSIC_COUNT);
//        music.setId(SpringUtils.MUSIC_COUNT + 1);
//
//        musicFacade.duplicate(SpringToUtils.newMusic(objectGenerator, SpringUtils.MUSIC_COUNT));
//
//        final Music duplicatedMusic = SpringUtils.getMusic(entityManager, SpringUtils.MUSIC_COUNT + 1);
//        DeepAsserts.assertEquals(music, duplicatedMusic);
//        DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT + 1, SpringUtils.getMusicCount(entityManager));
//    }
//
//    /**
//     * Test method for {@link MusicFacade#duplicate(MusicTO)} with null argument.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testDuplicateWithNullArgument() {
//        musicFacade.duplicate(null);
//    }
//
//    /**
//     * Test method for {@link MusicFacade#duplicate(MusicTO)} with music with null ID.
//     */
//    @Test(expected = ValidationException.class)
//    public void testDuplicateWithMusicWithNullId() {
//        musicFacade.duplicate(SpringToUtils.newMusic(objectGenerator));
//    }
//
//    /**
//     * Test method for {@link MusicFacade#duplicate(MusicTO)} with music with bad ID.
//     */
//    @Test(expected = RecordNotFoundException.class)
//    public void testDuplicateWithMusicWithBadId() {
//        musicFacade.duplicate(SpringToUtils.newMusic(objectGenerator, Integer.MAX_VALUE));
//    }
//
//    /**
//     * Test method for {@link MusicFacade#moveUp(MusicTO)}.
//     */
//    @Test
//    public void testMoveUp() {
//        final Music music1 = EntitiesUtils.getMusic(1);
//        music1.setPosition(1);
//        final Music music2 = EntitiesUtils.getMusic(2);
//        music2.setPosition(0);
//
//        musicFacade.moveUp(SpringToUtils.newMusic(objectGenerator, 2));
//        DeepAsserts.assertEquals(music1, SpringUtils.getMusic(entityManager, 1));
//        DeepAsserts.assertEquals(music2, SpringUtils.getMusic(entityManager, 2));
//        for (int i = 3; i <= SpringUtils.MUSIC_COUNT; i++) {
//            DeepAsserts.assertEquals(EntitiesUtils.getMusic(i), SpringUtils.getMusic(entityManager, i));
//        }
//        DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
//    }
//
//    /**
//     * Test method for {@link MusicFacade#moveUp(MusicTO)} with null argument.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testMoveUpWithNullArgument() {
//        musicFacade.moveUp(null);
//    }
//
//    /**
//     * Test method for {@link MusicFacade#moveUp(MusicTO)} with music with null ID.
//     */
//    @Test(expected = ValidationException.class)
//    public void testMoveUpWithMusicWithNullId() {
//        musicFacade.moveUp(SpringToUtils.newMusic(objectGenerator));
//    }
//
//    /**
//     * Test method for {@link MusicFacade#moveUp(MusicTO)} with not movable argument.
//     */
//    @Test(expected = ValidationException.class)
//    public void testMoveUpWithNotMovableArgument() {
//        musicFacade.moveUp(SpringToUtils.newMusic(objectGenerator, 1));
//    }
//
//    /**
//     * Test method for {@link MusicFacade#moveUp(MusicTO)} with bad ID.
//     */
//    @Test(expected = RecordNotFoundException.class)
//    public void testMoveUpWithBadId() {
//        musicFacade.moveUp(SpringToUtils.newMusic(objectGenerator, Integer.MAX_VALUE));
//    }
//
//    /**
//     * Test method for {@link MusicFacade#moveDown(MusicTO)}.
//     */
//    @Test
//    public void testMoveDown() {
//        final Music music1 = EntitiesUtils.getMusic(1);
//        music1.setPosition(1);
//        final Music music2 = EntitiesUtils.getMusic(2);
//        music2.setPosition(0);
//
//        musicFacade.moveDown(SpringToUtils.newMusic(objectGenerator, 1));
//        DeepAsserts.assertEquals(music1, SpringUtils.getMusic(entityManager, 1));
//        DeepAsserts.assertEquals(music2, SpringUtils.getMusic(entityManager, 2));
//        for (int i = 3; i <= SpringUtils.MUSIC_COUNT; i++) {
//            DeepAsserts.assertEquals(EntitiesUtils.getMusic(i), SpringUtils.getMusic(entityManager, i));
//        }
//        DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
//    }
//
//    /**
//     * Test method for {@link MusicFacade#moveDown(MusicTO)} with null argument.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testMoveDownWithNullArgument() {
//        musicFacade.moveDown(null);
//    }
//
//    /**
//     * Test method for {@link MusicFacade#moveDown(MusicTO)} with music with null ID.
//     */
//    @Test(expected = ValidationException.class)
//    public void testMoveDownWithMusicWithNullId() {
//        musicFacade.moveDown(SpringToUtils.newMusic(objectGenerator));
//    }
//
//    /**
//     * Test method for {@link MusicFacade#moveDown(MusicTO)} with not movable argument.
//     */
//    @Test(expected = ValidationException.class)
//    public void testMoveDownWithNotMovableArgument() {
//        musicFacade.moveDown(SpringToUtils.newMusic(objectGenerator, SpringUtils.MUSIC_COUNT));
//    }
//
//    /**
//     * Test method for {@link MusicFacade#moveDown(MusicTO)} with bad ID.
//     */
//    @Test(expected = RecordNotFoundException.class)
//    public void testMoveDownWithBadId() {
//        musicFacade.moveDown(SpringToUtils.newMusic(objectGenerator, Integer.MAX_VALUE));
//    }
//
//    /**
//     * Test method for {@link MusicFacade#exists(MusicTO)} with existing music.
//     */
//    @Test
//    public void testExists() {
//        for (int i = 1; i <= SpringUtils.MUSIC_COUNT; i++) {
//            assertTrue(musicFacade.exists(SpringToUtils.newMusic(objectGenerator, i)));
//        }
//
//        assertFalse(musicFacade.exists(SpringToUtils.newMusic(objectGenerator, Integer.MAX_VALUE)));
//
//        DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
//    }
//
//    /**
//     * Test method for {@link MusicFacade#exists(MusicTO)} with null argument.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testExistsWithNullArgument() {
//        musicFacade.exists(null);
//    }
//
//    /**
//     * Test method for {@link MusicFacade#exists(MusicTO)} with music with null ID.
//     */
//    @Test(expected = ValidationException.class)
//    public void testExistsWithMusicWithNullId() {
//        musicFacade.exists(SpringToUtils.newMusic(objectGenerator));
//    }
//
//    /**
//     * Test method for {@link MusicFacade#updatePositions()}.
//     */
//    @Test
//    public void testUpdatePositions() {
//        musicFacade.updatePositions();
//
//        for (int i = 1; i <= SpringUtils.MUSIC_COUNT; i++) {
//            DeepAsserts.assertEquals(EntitiesUtils.getMusic(i), SpringUtils.getMusic(entityManager, i));
//        }
//        DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
//    }
//
//    /**
//     * Test method for {@link MusicFacade#getTotalMediaCount()}.
//     */
//    @Test
//    public void testGetTotalMediaCount() {
//        final int count = 60;
//
//        DeepAsserts.assertEquals(count, musicFacade.getTotalMediaCount());
//        DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
//    }
//
//    /**
//     * Test method for {@link MusicFacade#getTotalLength()}.
//     */
//    @Test
//    public void testGetTotalLength() {
//        final Time length = new Time(666);
//
//        DeepAsserts.assertEquals(length, musicFacade.getTotalLength());
//        DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
//    }
//
//    /**
//     * Test method for {@link MusicFacade#getSongsCount()}.
//     */
//    @Test
//    public void testGetSongsCount() {
//        DeepAsserts.assertEquals(SpringUtils.SONGS_COUNT, musicFacade.getSongsCount());
//        DeepAsserts.assertEquals(SpringUtils.MUSIC_COUNT, SpringUtils.getMusicCount(entityManager));
//    }
//
//}
