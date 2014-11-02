package cz.vhromada.catalog.facade.converters;

import cz.vhromada.catalog.dao.entities.Music;
import cz.vhromada.catalog.facade.to.MusicTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A class represents converter from TO for music to entity music.
 *
 * @author Vladimir Hromada
 */
@Component("musicTOToMusicConverter")
public class MusicTOToMusicConverter implements Converter<MusicTO, Music> {

	@Override
	public Music convert(final MusicTO source) {
		if (source == null) {
			return null;
		}

		final Music music = new Music();
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
