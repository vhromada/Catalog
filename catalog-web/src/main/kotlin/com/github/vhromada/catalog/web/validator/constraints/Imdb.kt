package com.github.vhromada.catalog.web.validator.constraints

import com.github.vhromada.catalog.web.validator.ImdbMovieValidator
import com.github.vhromada.catalog.web.validator.ImdbShowValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * An annotation represents IMDB code constraint.
 *
 * @author Vladimir Hromada
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ImdbMovieValidator::class, ImdbShowValidator::class])
@MustBeDocumented
annotation class Imdb(
    /**
     * Returns error message template.
     *
     * @return error message template
     */
    val message: String = "{com.github.vhromada.catalog.validator.Imdb.message}",

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
