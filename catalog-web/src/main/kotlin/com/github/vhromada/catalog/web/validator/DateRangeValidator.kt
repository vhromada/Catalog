package com.github.vhromada.catalog.web.validator

import com.github.vhromada.catalog.web.validator.constraints.DateRange
import com.github.vhromada.common.utils.Constants
import java.util.regex.Pattern
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * A class represents validator for date range constraint.
 *
 * @author Vladimir Hromada
 */
class DateRangeValidator : ConstraintValidator<DateRange, String> {

    /**
     * Minimal date
     */
    private var minDate: Int = 0

    override fun initialize(dateRange: DateRange) {
        minDate = dateRange.value
    }

    override fun isValid(date: String?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        if (date == null || !PATTERN.matcher(date).matches()) {
            return false
        }

        return date.toInt() in minDate..Constants.CURRENT_YEAR
    }

    @Suppress("CheckStyle")
    companion object {

        /**
         * Date pattern
         */
        private val PATTERN = Pattern.compile("\\d{4}")

    }

}
