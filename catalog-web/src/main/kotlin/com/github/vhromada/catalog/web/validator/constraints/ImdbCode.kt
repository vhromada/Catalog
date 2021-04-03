package com.github.vhromada.catalog.web.validator.constraints

import com.github.vhromada.catalog.web.validator.ImdbCodeValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * An annotation represents IMDB code constraint.
 *
 * @author Vladimir Hromada
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ImdbCodeValidator::class])
@MustBeDocumented
annotation class ImdbCode(
    /**
     * Returns error message template.
     *
     * @return error message template
     */
    val message: String = "{com.github.vhromada.catalog.validator.ImdbCode.message}",

    /**
     * Returns groups constraint belongs to.
     *
     * @return groups constraint belongs to
     */
    val groups: Array<KClass<*>> = [],

    /**
     * Returns payload associated to constraint.
     *
     * @return payload associated to constraint
     */
    @Suppress("unused")
    val payload: Array<KClass<out Payload>> = []
)
