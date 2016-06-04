package cz.vhromada.catalog.service.impl;

import cz.vhromada.catalog.dao.entities.Program;
import cz.vhromada.catalog.repository.ProgramRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of service for programs.
 *
 * @author Vladimir Hromada
 */
@Component("programService")
public class ProgramServiceImpl extends AbstractCatalogService<Program> {

    /**
     * Creates a new instance of ProgramServiceImpl.
     *
     * @param programRepository repository for programs
     * @param programCache      cache for programs
     * @throws IllegalArgumentException if repository programs is null
     *                                  or cache for programs is null
     */
    @Autowired
    public ProgramServiceImpl(final ProgramRepository programRepository,
            @Value("#{cacheManager.getCache('programCache')}") final Cache programCache) {
        super(programRepository, programCache, "programs");
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
