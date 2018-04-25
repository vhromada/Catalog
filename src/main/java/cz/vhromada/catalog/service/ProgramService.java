package cz.vhromada.catalog.service;

import cz.vhromada.catalog.domain.Program;
import cz.vhromada.catalog.repository.ProgramRepository;
import cz.vhromada.common.service.AbstractMovableService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

/**
 * A class represents service for programs.
 *
 * @author Vladimir Hromada
 */
@Component("programService")
public class ProgramService extends AbstractMovableService<Program> {

    /**
     * Creates a new instance of ProgramService.
     *
     * @param programRepository repository for programs
     * @param cache             cache
     * @throws IllegalArgumentException if repository programs is null
     *                                  or cache is null
     */
    @Autowired
    public ProgramService(final ProgramRepository programRepository, @Value("#{cacheManager.getCache('catalogCache')}") final Cache cache) {
        super(programRepository, cache, "programs");
    }

    @Override
    protected Program getCopy(final Program data) {
        final Program newProgram = new Program();
        newProgram.setName(data.getName());
        newProgram.setWikiEn(data.getWikiEn());
        newProgram.setWikiCz(data.getWikiCz());
        newProgram.setMediaCount(data.getMediaCount());
        newProgram.setCrack(data.getCrack());
        newProgram.setSerialKey(data.getSerialKey());
        newProgram.setOtherData(data.getOtherData());
        newProgram.setNote(data.getNote());
        newProgram.setPosition(data.getPosition());

        return newProgram;
    }

}
