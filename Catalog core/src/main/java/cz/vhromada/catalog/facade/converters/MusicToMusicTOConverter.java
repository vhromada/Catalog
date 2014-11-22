package cz.vhromada.catalog.facade.converters;

import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.facade.to.MusicTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A class represents converter from entity music to TO for music.
 *
 * @author Vladimir Hromada
 */
@Component("musicToMusicTOConverter")
public class MusicToMusicTOConverter implements Converter<Music, MusicTO> {

    @Override
    public MusicTO convert(final Music source) {
        if (source == null) {
            return null;
        }

        final MusicTO music = new MusicTO();
        music.setId(source.getId());
        music.setName(source.getName());
        music.setWikiEn(source.getWikiEn());
        music.setWikiCz(source.getWikiCz());
        music.setMediaCount(source.getMediaCount());
        music.setNote(source.getNote());
        music.setPosition(source.getPosition());
        return music;
    }

}
