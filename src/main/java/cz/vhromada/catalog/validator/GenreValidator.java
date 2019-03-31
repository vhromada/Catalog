package cz.vhromada.catalog.validator;

import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.validator.AbstractMovableValidator;
import cz.vhromada.validation.result.Event;
import cz.vhromada.validation.result.Result;
import cz.vhromada.validation.result.Severity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * A class represents validator for genre.
 *
 * @author Vladimir Hromada
 */
@Component("genreValidator")
public class GenreValidator extends AbstractMovableValidator<Genre, cz.vhromada.catalog.domain.Genre> {

    /**
     * Creates a new instance of GenreValidator.
     *
     * @param genreService service for genres
     * @throws IllegalArgumentException if service for genres is null
     */
    @Autowired
    public GenreValidator(final MovableService<cz.vhromada.catalog.domain.Genre> genreService) {
        super("Genre", genreService);
    }

    /**
     * Validates genre deeply.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Name is null</li>
     * <li>Name is empty string</li>
     * </ul>
     *
     * @param data   validating genre
     * @param result result with validation errors
     */
    @Override
    protected void validateDataDeep(final Genre data, final Result<Void> result) {
        if (data.getName() == null) {
            result.addEvent(new Event(Severity.ERROR, "GENRE_NAME_NULL", "Name mustn't be null."));
        } else if (!StringUtils.hasText(data.getName())) {
            result.addEvent(new Event(Severity.ERROR, "GENRE_NAME_EMPTY", "Name mustn't be empty string."));
        }
    }

}
