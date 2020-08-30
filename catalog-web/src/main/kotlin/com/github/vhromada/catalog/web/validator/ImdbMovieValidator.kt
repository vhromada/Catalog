package com.github.vhromada.catalog.web.validator

import com.github.vhromada.catalog.web.fo.MovieFO
import com.github.vhromada.catalog.web.validator.constraints.Imdb
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * A class represents show validator for IMDB code constraint.
 *
 * @author Vladimir Hromada
 */
class ImdbMovieValidator : ConstraintValidator<Imdb, MovieFO> {

    override fun isValid(movie: MovieFO?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        if (movie == null) {
            return false
        }
        return if (!movie.imdb) {
            true
        } else !movie.imdbCode.isNullOrBlank()
    }

}
