package com.github.vhromada.catalog.web.validator

import com.github.vhromada.catalog.web.fo.TimeFO
import com.github.vhromada.catalog.web.validator.constraints.Time
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * A class represents show validator for time constraint.
 *
 * @author Vladimir Hromada
 */
class TimeValidator : ConstraintValidator<Time, TimeFO> {

    override fun isValid(time: TimeFO?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        if (time == null) {
            return false
        }
        if (time.hours.isNullOrBlank() || time.minutes.isNullOrBlank() || time.seconds.isNullOrBlank()) {
            return true
        }

        val length = time.hours!!.toInt() + time.minutes!!.toInt() + time.seconds!!.toInt()
        return length > 0
    }

}
