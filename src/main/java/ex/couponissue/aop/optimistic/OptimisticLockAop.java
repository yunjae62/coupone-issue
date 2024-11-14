package ex.couponissue.aop.optimistic;

import ex.couponissue.aop.AopForTransaction;
import jakarta.persistence.OptimisticLockException;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

@Slf4j(topic = "OptimisticLockAop")
@Aspect
@Component
@RequiredArgsConstructor
public class OptimisticLockAop {

    private final AopForTransaction aopForTransaction;

    @Around("@annotation(ex.couponissue.aop.optimistic.OptimisticLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        OptimisticLock optimisticLock = method.getAnnotation(OptimisticLock.class);

        int maxRetry = optimisticLock.maxRetry();
        long retryInterval = optimisticLock.retryInterval();

        int count = 0;

        while (true) {
            count++;

            if (count > maxRetry) {
                log.error("쿠폰 발급에 실패했습니다.");
                throw new RuntimeException("쿠폰 발급에 실패했습니다.");
            }

            try {
                Object proceed = aopForTransaction.proceed(joinPoint);
                log.info("쿠폰 발급 완료 (시도 횟수: {})", count);
                return proceed;
            } catch (ObjectOptimisticLockingFailureException | OptimisticLockException e) {
                try {
                    Thread.sleep(retryInterval);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }
}