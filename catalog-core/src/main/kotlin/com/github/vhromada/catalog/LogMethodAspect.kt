package com.github.vhromada.catalog

import mu.KotlinLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component

/**
 * A class represents aspect for logging method.
 *
 * @author Vladimir Hromada
 */
@Aspect
@Component
class LogMethodAspect {

    /**
     * Logs method.
     *
     * @param proceedingJoinPoint proceeding join point
     * @return result of calling method
     * @throws Throwable if calling method fails
     */
    @Around("catalogFacadePointcut() || commonFacadePointcut()")
    @Throws(Throwable::class)
    fun log(proceedingJoinPoint: ProceedingJoinPoint): Any {
        val result: Any

        val start = System.currentTimeMillis()

        val message = StringBuilder()
        message.append("Method [").append(proceedingJoinPoint.signature.name).append(']')

        try {
            result = proceedingJoinPoint.proceed()
        } catch (exception: Exception) {
            finishLogMessage(proceedingJoinPoint = proceedingJoinPoint, message = message, start = start)
            throw exception
        }

        finishLogMessage(proceedingJoinPoint = proceedingJoinPoint, message = message, start = start)

        return result
    }

    /**
     * Pointcut for facade layer
     */
    @Pointcut("execution(public * com.github.vhromada.catalog..*FacadeImpl.*(..))")
    @Suppress("unused")
    fun catalogFacadePointcut() {
        // pointcut
    }

    /**
     * Pointcut for service layer
     */
    @Pointcut("execution(public * com.github.vhromada.common..*Facade.*(..))")
    @Suppress("unused")
    fun commonFacadePointcut() {
        // pointcut
    }

    /**
     * Finishes method logging.
     *
     * @param proceedingJoinPoint proceeding join point
     * @param message             result message
     * @param start               start time of calling method
     */
    private fun finishLogMessage(proceedingJoinPoint: ProceedingJoinPoint, message: StringBuilder, start: Long) {
        message.append(" spent ").append(System.currentTimeMillis() - start).append("ms")

        KotlinLogging.logger(proceedingJoinPoint.target.javaClass.name).debug { message.toString() }
    }

}
