package cz.vhromada.catalog.facade.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.domain.Song;
import cz.vhromada.catalog.entity.Music;
import cz.vhromada.catalog.facade.MusicFacade;
import cz.vhromada.catalog.utils.MusicUtils;
import cz.vhromada.catalog.utils.SongUtils;
import cz.vhromada.common.Time;
import cz.vhromada.common.facade.MovableParentFacade;
import cz.vhromada.common.test.facade.MovableParentFacadeIntegrationTest;
import cz.vhromada.validation.result.Event;
import cz.vhromada.validation.result.Result;
import cz.vhromada.validation.result.Severity;
import cz.vhromada.validation.result.Status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

/**
 * A class represents integration test for class {@link MusicFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@ContextConfiguration(classes = CatalogTestConfiguration.class)
class MusicFacadeImplIntegrationTest extends MovableParentFacadeIntegrationTest<Music, cz.vhromada.catalog.domain.Music> {

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
    private MusicFacade facade;

    /**
     * Test method for {@link MusicFacade#add(Music)} with music with null name.
     */
    @Test
    void add_NullName() {
        final Music music = newData(null);
        music.setName(null);

        final Result<Void> result = facade.add(music);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MUSIC_NAME_NULL", "Name mustn't be null.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MusicFacade#add(Music)} with music with empty string as name.
     */
    @Test
    void add_EmptyName() {
        final Music music = newData(null);
        music.setName("");

        final Result<Void> result = facade.add(music);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MUSIC_NAME_EMPTY", "Name mustn't be empty string.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MusicFacade#add(Music)} with music with null URL to english Wikipedia about music.
     */
    @Test
    void add_NullWikiEn() {
        final Music music = newData(null);
        music.setWikiEn(null);

        final Result<Void> result = facade.add(music);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MUSIC_WIKI_EN_NULL",
                "URL to english Wikipedia page about music mustn't be null.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MusicFacade#add(Music)} with music with null URL to czech Wikipedia about music.
     */
    @Test
    void add_NullWikiCz() {
        final Music music = newData(null);
        music.setWikiCz(null);

        final Result<Void> result = facade.add(music);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MUSIC_WIKI_CZ_NULL",
                "URL to czech Wikipedia page about music mustn't be null.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MusicFacade#add(Music)} with music with not positive count of media.
     */
    @Test
    void add_NotPositiveMediaCount() {
        final Music music = newData(null);
        music.setMediaCount(0);

        final Result<Void> result = facade.add(music);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MUSIC_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MusicFacade#add(Music)} with music with null note.
     */
    @Test
    void add_NullNote() {
        final Music music = newData(null);
        music.setNote(null);

        final Result<Void> result = facade.add(music);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MUSIC_NOTE_NULL", "Note mustn't be null.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MusicFacade#update(Music)} with music with null name.
     */
    @Test
    void update_NullName() {
        final Music music = newData(1);
        music.setName(null);

        final Result<Void> result = facade.update(music);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MUSIC_NAME_NULL", "Name mustn't be null.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MusicFacade#update(Music)} with music with empty string as name.
     */
    @Test
    void update_EmptyName() {
        final Music music = newData(1);
        music.setName("");

        final Result<Void> result = facade.update(music);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MUSIC_NAME_EMPTY", "Name mustn't be empty string.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MusicFacade#update(Music)} with music with null URL to english Wikipedia about music.
     */
    @Test
    void update_NullWikiEn() {
        final Music music = newData(1);
        music.setWikiEn(null);

        final Result<Void> result = facade.update(music);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MUSIC_WIKI_EN_NULL",
                "URL to english Wikipedia page about music mustn't be null.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MusicFacade#update(Music)} with music with null URL to czech Wikipedia about music.
     */
    @Test
    void update_NullWikiCz() {
        final Music music = newData(1);
        music.setWikiCz(null);

        final Result<Void> result = facade.update(music);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MUSIC_WIKI_CZ_NULL",
                "URL to czech Wikipedia page about music mustn't be null.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MusicFacade#update(Music)} with music with not positive count of media.
     */
    @Test
    void update_NotPositiveMediaCount() {
        final Music music = newData(1);
        music.setMediaCount(0);

        final Result<Void> result = facade.update(music);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MUSIC_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MusicFacade#update(Music)} with music with null note.
     */
    @Test
    void update_NullNote() {
        final Music music = newData(1);
        music.setNote(null);

        final Result<Void> result = facade.update(music);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "MUSIC_NOTE_NULL", "Note mustn't be null.")));
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MusicFacade#getTotalMediaCount()}.
     */
    @Test
    void getTotalMediaCount() {
        final int count = 60;

        final Result<Integer> result = facade.getTotalMediaCount();

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.OK);
            softly.assertThat(result.getData()).isEqualTo(count);
            softly.assertThat(result.getEvents()).isEmpty();
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MusicFacade#getTotalLength()}.
     */
    @Test
    void getTotalLength() {
        final Time length = new Time(666);

        final Result<Time> result = facade.getTotalLength();

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.OK);
            softly.assertThat(result.getData()).isEqualTo(length);
            softly.assertThat(result.getEvents()).isEmpty();
        });

        assertDefaultRepositoryData();
    }

    /**
     * Test method for {@link MusicFacade#getSongsCount()}.
     */
    @Test
    void getSongsCount() {
        final Result<Integer> result = facade.getSongsCount();

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.OK);
            softly.assertThat(result.getData()).isEqualTo(SongUtils.SONGS_COUNT);
            softly.assertThat(result.getEvents()).isEmpty();
        });

        assertDefaultRepositoryData();
    }

    @Override
    protected MovableParentFacade<Music> getFacade() {
        return facade;
    }

    @Override
    protected Integer getDefaultDataCount() {
        return MusicUtils.MUSIC_COUNT;
    }

    @Override
    protected Integer getRepositoryDataCount() {
        return MusicUtils.getMusicCount(entityManager);
    }

    @Override
    protected List<cz.vhromada.catalog.domain.Music> getDataList() {
        return MusicUtils.getMusic();
    }

    @Override
    protected cz.vhromada.catalog.domain.Music getDomainData(final Integer index) {
        return MusicUtils.getMusic(index);
    }

    @Override
    protected Music newData(final Integer id) {
        return MusicUtils.newMusic(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Music newDomainData(final Integer id) {
        return MusicUtils.newMusicDomain(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Music getRepositoryData(final Integer id) {
        return MusicUtils.getMusic(entityManager, id);
    }

    @Override
    protected String getName() {
        return "Music";
    }

    @Override
    protected void clearReferencedData() {
    }

    @Override
    protected void assertDataListDeepEquals(final List<Music> expected, final List<cz.vhromada.catalog.domain.Music> actual) {
        MusicUtils.assertMusicListDeepEquals(expected, actual);
    }

    @Override
    protected void assertDataDeepEquals(final Music expected, final cz.vhromada.catalog.domain.Music actual) {
        MusicUtils.assertMusicDeepEquals(expected, actual);
    }

    @Override
    protected void assertDataDomainDeepEquals(final cz.vhromada.catalog.domain.Music expected, final cz.vhromada.catalog.domain.Music actual) {
        MusicUtils.assertMusicDeepEquals(expected, actual);
    }

    @Override
    protected void assertDefaultRepositoryData() {
        super.assertDefaultRepositoryData();

        assertReferences();
    }

    @Override
    protected void assertNewRepositoryData() {
        super.assertNewRepositoryData();

        assertThat(SongUtils.getSongsCount(entityManager)).isEqualTo(0);
    }

    @Override
    protected void assertAddRepositoryData() {
        super.assertAddRepositoryData();

        assertReferences();
    }

    @Override
    protected void assertUpdateRepositoryData() {
        super.assertUpdateRepositoryData();

        assertReferences();
    }

    @Override
    protected void assertRemoveRepositoryData() {
        super.assertRemoveRepositoryData();

        assertThat(SongUtils.getSongsCount(entityManager)).isEqualTo(SongUtils.SONGS_COUNT - SongUtils.SONGS_PER_MUSIC_COUNT);
    }

    @Override
    protected void assertDuplicateRepositoryData() {
        super.assertDuplicateRepositoryData();

        assertThat(SongUtils.getSongsCount(entityManager)).isEqualTo(SongUtils.SONGS_COUNT + SongUtils.SONGS_PER_MUSIC_COUNT);
    }

    @Override
    protected cz.vhromada.catalog.domain.Music getExpectedDuplicatedData() {
        final cz.vhromada.catalog.domain.Music music = super.getExpectedDuplicatedData();
        for (final Song song : music.getSongs()) {
            song.setId(SongUtils.SONGS_COUNT + music.getSongs().indexOf(song) + 1);
        }

        return music;
    }

    /**
     * Asserts references.
     */
    private void assertReferences() {
        assertThat(SongUtils.getSongsCount(entityManager)).isEqualTo(SongUtils.SONGS_COUNT);
    }

}
