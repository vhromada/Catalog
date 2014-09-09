package cz.vhromada.catalog.facade.converters;

import cz.vhromada.catalog.dao.entities.Program;
import cz.vhromada.catalog.facade.to.ProgramTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A class represents converter from TO for program to entity program.
 *
 * @author Vladimir Hromada
 */
@Component("programTOToProgramConverter")
public class ProgramTOToProgramConverter implements Converter<ProgramTO, Program> {

	@Override
	public Program convert(final ProgramTO source) {
		if (source == null) {
			return null;
		}

		final Program program = new Program();
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
