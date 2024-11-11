package ex.couponissue.coupon.service.distribute;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Slf4j(topic = "CouponIssueDistributeLockFacade")
@Service
@RequiredArgsConstructor
public class CouponIssueDistributeLockFacade {

    private final RedissonClient redissonClient;
    private final CouponIssueDistributeService couponIssueDistributeService;

    public void issue(String couponId, String userId) {
        String lockName = "lock_" + couponId;
        RLock lock = redissonClient.getLock(lockName);

        try {
            boolean available = lock.tryLock(10_000, 10_000, TimeUnit.MILLISECONDS);

            if (!available) {
                throw new IllegalArgumentException("[" + lockName + "] lock 획득 실패");
            }

            couponIssueDistributeService.issueWithDistributedLock(couponId, userId);

        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
