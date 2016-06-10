package cz.vhromada.catalog.service.impl;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.entities.Music;
import cz.vhromada.catalog.entities.Song;
import cz.vhromada.catalog.repository.MusicRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of service for music.
 *
 * @author Vladimir Hromada
 */
@Component("musicService")
public class MusicServiceImpl extends AbstractCatalogService<Music> {

    /**
     * Creates a new instance of MusicServiceImpl.
     *
     * @param musicRepository repository for music
     * @param cache           cache
     * @throws IllegalArgumentException if repository for music is null
     *                                  or cache is null
     */
    @Autowired
    public MusicServiceImpl(final MusicRepository musicRepository,
            @Value("#{cacheManager.getCache('catalogCache')}") final Cache cache) {
        super(musicRepository, cache, "music");
    }

    @Override
    protected Music getCopy(final Music data) {
        final Music newMusic = new Music();
        newMusic.setName(data.getName());
        newMusic.setWikiEn(data.getWikiEn());
        newMusic.setWikiCz(data.getWikiCz());
        newMusic.setMediaCount(data.getMediaCount());
        newMusic.setNote(data.getNote());
        newMusic.setPosition(data.getPosition());
        final List<Song> newSongs = new ArrayList<>();
        for (final Song song : data.getSongs()) {
            final Song newSong = new Song();
            newSong.setName(song.getName());
            newSong.setLength(song.getLength());
            newSong.setNote(song.getNote());
            newSong.setPosition(song.getPosition());
            newSongs.add(newSong);
        }
        newMusic.setSongs(newSongs);

        return newMusic;
    }

    @Override
    protected void updatePositions(final List<Music> data) {
        for (int i = 0; i < data.size(); i++) {
            final Music music = data.get(i);
            music.setPosition(i);
            final List<Song> songs = music.getSongs();
            for (int j = 0; j < songs.size(); j++) {
                final Song song = songs.get(j);
                song.setPosition(j);
            }
        }
    }

}
