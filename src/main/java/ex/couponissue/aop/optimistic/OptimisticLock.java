package ex.couponissue.aop.optimistic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OptimisticLock {

    /**
     * 최대 재시도 횟수 (default - 50)
     */
    int maxRetry() default 50;

    /**
     * retry 사이 간격 (default - 50ms)
     */
    long retryInterval() default 50L;
}
