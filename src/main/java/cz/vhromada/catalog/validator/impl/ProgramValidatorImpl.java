package cz.vhromada.catalog.validator.impl;

import java.util.List;

import cz.vhromada.catalog.entity.Program;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * A class represents implementation of validator for program.
 *
 * @author Vladimir Hromada
 */
@Component("programValidator")
public class ProgramValidatorImpl extends AbstractCatalogValidator<Program, cz.vhromada.catalog.domain.Program> {

    /**
     * Service for programs
     */
    private CatalogService<cz.vhromada.catalog.domain.Program> programService;

    /**
     * Creates a new instance of ProgramValidatorImpl.
     *
     * @param programService service for programs
     * @throws IllegalArgumentException if service for programs is null
     */
    @Autowired
    public ProgramValidatorImpl(final CatalogService<cz.vhromada.catalog.domain.Program> programService) {
        super("Program");

        Assert.notNull(programService, "Service for programs mustn't be null.");

        this.programService = programService;
    }

    @Override
    protected cz.vhromada.catalog.domain.Program getData(final Program data) {
        return programService.get(data.getId());
    }

    @Override
    protected List<cz.vhromada.catalog.domain.Program> getList(final Program data) {
        return programService.getAll();
    }

    /**
     * Validates program deeply.
     * <br/>
     * Validation errors:
     * <ul>
     * <li>Name is null</li>
     * <li>Name is empty string</li>
     * <li>URL to english Wikipedia page about program is null</li>
     * <li>URL to czech Wikipedia page about program is null</li>
     * <li>Count of media isn't positive number</li>
     * <li>Other data is null</li>
     * <li>Note is null</li>
     * </ul>
     *
     * @param data   validating program
     * @param result result with validation errors
     */
    @Override
    protected void validateDataDeep(final Program data, final Result<Void> result) {
        if (data.getName() == null) {
            result.addEvent(new Event(Severity.ERROR, "PROGRAM_NAME_NULL", "Name mustn't be null."));
        } else if (StringUtils.isEmpty(data.getName()) || StringUtils.isEmpty(data.getName().trim())) {
            result.addEvent(new Event(Severity.ERROR, "PROGRAM_NAME_EMPTY", "Name mustn't be empty string."));
        }
        if (data.getWikiEn() == null) {
            result.addEvent(new Event(Severity.ERROR, "PROGRAM_WIKI_EN_NULL", "URL to english Wikipedia page about program mustn't be null."));
        }
        if (data.getWikiCz() == null) {
            result.addEvent(new Event(Severity.ERROR, "PROGRAM_WIKI_CZ_NULL", "URL to czech Wikipedia page about program mustn't be null."));
        }
        if (data.getMediaCount() <= 0) {
            result.addEvent(new Event(Severity.ERROR, "PROGRAM_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number."));
        }
        if (data.getOtherData() == null) {
            result.addEvent(new Event(Severity.ERROR, "PROGRAM_OTHER_DATA_NULL", "Other data mustn't be null."));
        }
        if (data.getNote() == null) {
            result.addEvent(new Event(Severity.ERROR, "PROGRAM_NOTE_NULL", "Note mustn't be null."));
        }
    }

}
