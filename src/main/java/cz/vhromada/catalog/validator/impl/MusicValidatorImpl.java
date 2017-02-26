package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.entity.Music;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * A class represents implementation of validator for music.
 *
 * @author Vladimir Hromada
 */
@Component("musicValidator")
public class MusicValidatorImpl extends AbstractCatalogValidator<Music, cz.vhromada.catalog.domain.Music> {

    /**
     * Creates a new instance of MusicValidatorImpl.
     *
     * @param musicService service for music
     * @throws IllegalArgumentException if service for music is null
     */
    @Autowired
    public MusicValidatorImpl(final CatalogService<cz.vhromada.catalog.domain.Music> musicService) {
        super("Music", musicService);
    }

    /**
     * Validates music deeply.
     * <br/>
     * Validation errors:
     * <ul>
     * <li>Name is null</li>
     * <li>Name is empty string</li>
     * <li>URL to english Wikipedia page about music is null</li>
     * <li>URL to czech Wikipedia page about music is null</li>
     * <li>Count of media isn't positive number</li>
     * <li>Other data is null</li>
     * <li>Note is null</li>
     * </ul>
     *
     * @param data   validating music
     * @param result result with validation errors
     */
    @Override
    protected void validateDataDeep(final Music data, final Result<Void> result) {
        if (data.getName() == null) {
            result.addEvent(new Event(Severity.ERROR, "MUSIC_NAME_NULL", "Name mustn't be null."));
        } else if (StringUtils.isEmpty(data.getName()) || StringUtils.isEmpty(data.getName().trim())) {
            result.addEvent(new Event(Severity.ERROR, "MUSIC_NAME_EMPTY", "Name mustn't be empty string."));
        }
        validateUrls(data, result);
        if (data.getMediaCount() <= 0) {
            result.addEvent(new Event(Severity.ERROR, "MUSIC_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number."));
        }
        if (data.getNote() == null) {
            result.addEvent(new Event(Severity.ERROR, "MUSIC_NOTE_NULL", "Note mustn't be null."));
        }
    }

    /**
     * Validates URLs.
     * <br/>
     * Validation errors:
     * <ul>
     * <li>URL to english Wikipedia page about music is null</li>
     * <li>URL to czech Wikipedia page about music is null</li>
     * </ul>
     *
     * @param data   validating show
     * @param result result with validation errors
     */
    private static void validateUrls(final Music data, final Result<Void> result) {
        if (data.getWikiEn() == null) {
            result.addEvent(new Event(Severity.ERROR, "MUSIC_WIKI_EN_NULL", "URL to english Wikipedia page about music mustn't be null."));
        }
        if (data.getWikiCz() == null) {
            result.addEvent(new Event(Severity.ERROR, "MUSIC_WIKI_CZ_NULL", "URL to czech Wikipedia page about music mustn't be null."));
        }
    }

}
