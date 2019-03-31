package cz.vhromada.catalog.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cz.vhromada.catalog.domain.Music;
import cz.vhromada.catalog.domain.Song;
import cz.vhromada.catalog.repository.MusicRepository;
import cz.vhromada.catalog.utils.CatalogUtils;
import cz.vhromada.common.service.AbstractMovableService;
import cz.vhromada.common.utils.CollectionUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

/**
 * A class represents service for music.
 *
 * @author Vladimir Hromada
 */
@Component("musicService")
public class MusicService extends AbstractMovableService<Music> {

    /**
     * Creates a new instance of MusicService.
     *
     * @param musicRepository repository for music
     * @param cache           cache
     * @throws IllegalArgumentException if repository for music is null
     *                                  or cache is null
     */
    @Autowired
    public MusicService(final MusicRepository musicRepository, @Value("#{cacheManager.getCache('catalogCache')}") final Cache cache) {
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
        newMusic.setSongs(data.getSongs().stream().map(CatalogUtils::duplicateSong).collect(Collectors.toList()));

        return newMusic;
    }

    @Override
    protected void updatePositions(final List<Music> data) {
        for (int i = 0; i < data.size(); i++) {
            final Music music = data.get(i);
            music.setPosition(i);
            if (music.getSongs() == null) {
                music.setSongs(new ArrayList<>());
            }
            final List<Song> songs = CollectionUtils.getSortedData(music.getSongs());
            for (int j = 0; j < songs.size(); j++) {
                final Song song = songs.get(j);
                song.setPosition(j);
            }
        }
    }

}