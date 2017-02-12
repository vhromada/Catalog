package cz.vhromada.catalog;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * A class represents aspect for logging method.
 *
 * @author Vladimir Hromada
 */
@Aspect
@Component
public class LogMethodAspect {

    /**
     * Logs method.
     *
     * @param proceedingJoinPoint proceeding join point
     * @return result of calling method
     * @throws Throwable if calling method fails
     */
    @Around("facadePointcut() || servicePointcut()")
    public Object log(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        final Object result;

        final long start = System.currentTimeMillis();

        final StringBuilder message = new StringBuilder();
        message.append("Method [").append(proceedingJoinPoint.getSignature().getName()).append(']');

        try {
            result = proceedingJoinPoint.proceed();
        } catch (final Exception exception) {
            finishLogMessage(proceedingJoinPoint, message, start);
            throw exception;
        }

        finishLogMessage(proceedingJoinPoint, message, start);

        return result;
    }

    /**
     * Pointcut for facade layer
     */
    @Pointcut("execution(public * cz.vhromada.catalog..*FacadeImpl.*(..))")
    public void facadePointcut() {
    }

    /**
     * Pointcut for service layer
     */
    @Pointcut("execution(public * cz.vhromada.catalog..*Service.*(..))")
    public void servicePointcut() {
    }

    /**
     * Finishes method logging.
     *
     * @param proceedingJoinPoint proceeding join point
     * @param message             result message
     * @param start               start time of calling method
     */
    private static void finishLogMessage(final ProceedingJoinPoint proceedingJoinPoint, final StringBuilder message, final long start) {
        message.append(" spent ").append(System.currentTimeMillis() - start).append("ms");

        LoggerFactory.getLogger(proceedingJoinPoint.getTarget().getClass()).debug(message.toString());
    }

}
