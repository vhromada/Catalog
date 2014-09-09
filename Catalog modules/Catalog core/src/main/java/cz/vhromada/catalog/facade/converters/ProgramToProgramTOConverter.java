package cz.vhromada.catalog.facade.converters;

import cz.vhromada.catalog.dao.entities.Program;
import cz.vhromada.catalog.facade.to.ProgramTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A class represents converter from entity program to TO for program.
 *
 * @author Vladimir Hromada
 */
@Component("programToProgramTOConverter")
public class ProgramToProgramTOConverter implements Converter<Program, ProgramTO> {

	@Override
	public ProgramTO convert(final Program source) {
		if (source == null) {
			return null;
		}

		final ProgramTO program = new ProgramTO();
		program.setId(source.getId());
		program.setName(source.getName());
		program.setWikiEn(source.getWikiEn());
		program.setWikiCz(source.getWikiCz());
		program.setMediaCount(source.getMediaCount());
		program.setCrack(source.hasCrack());
		program.setSerialKey(source.hasSerialKey());
		program.setOtherData(source.getOtherData());
		program.setNote(source.getNote());
		program.setPosition(source.getPosition());
		return program;
	}

}
