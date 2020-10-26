package com.github.vhromada.catalog.web.validator

import com.github.vhromada.catalog.web.validator.constraints.ImdbCode
import com.github.vhromada.common.utils.Constants
import java.util.regex.Pattern
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * A class represents validator for IMDB code constraint.
 *
 * @author Vladimir Hromada
 */
class ImdbCodeValidator : ConstraintValidator<ImdbCode, String> {

    override fun isValid(imdbCode: String?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        if (imdbCode == null || imdbCode.isEmpty()) {
            return true
        }
        if (!PATTERN.matcher(imdbCode).matches()) {
            return false
        }

        return imdbCode.toInt() in 1..Constants.MAX_IMDB_CODE
    }

    companion object {

        /**
         * IMDB code pattern
         */
        private val PATTERN = Pattern.compile("\\d{1,7}")

    }

}
