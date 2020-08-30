package com.github.vhromada.catalog.web.controller

import com.github.vhromada.catalog.web.exception.IllegalRequestException
import mu.KotlinLogging
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

/**
 * Logger
 */
private val logger = KotlinLogging.logger {}

/**
 * A class represents advice for errors.
 *
 * @author Vladimir Hromada
 */
@ControllerAdvice
class ErrorControllerAdvice {

    /**
     * Shows page for illegal argument exception.
     *
     * @param model     model
     * @param exception exception
     * @return view for page for known exception
     */
    @ExceptionHandler(IllegalArgumentException::class)
    fun processKnownException(model: Model, exception: Exception): String {
        return processException(model, exception, "There was error in working with data.")
    }

    /**
     * Shows page for exception in request.
     *
     * @param model     model
     * @param exception exception
     * @return view for exception in request
     */
    @ExceptionHandler(IllegalRequestException::class)
    fun processRequestException(model: Model, exception: Exception): String {
        return processException(model, exception, "There was illegal changes in pages or call on non existing data.")
    }

    /**
     * Shows page for unknown exception.
     *
     * @param model     model
     * @param exception exception
     * @return view for page for unknown exception
     */
    @ExceptionHandler(Exception::class)
    fun processUnknownException(model: Model, exception: Exception): String {
        return processException(model, exception, "There was unexpected error.")
    }

    /**
     * Process exception.
     *
     * @param model        model
     * @param exception    exception
     * @param errorMessage error message
     * @return view for page for exception
     */
    private fun processException(model: Model, exception: Exception, errorMessage: String): String {
        logger.error(exception) { "Web exception" }

        return process(model, errorMessage)
    }

    /**
     * Adds data to model and returns view for page for exception.
     *
     * @param model        model
     * @param errorMessage error message
     * @return view for page for exception
     */
    private fun process(model: Model, errorMessage: String): String {
        model.addAttribute("errorMessage", errorMessage)
        model.addAttribute("title", "Error")
        model.addAttribute("inner", false)

        return "errors"
    }

}
