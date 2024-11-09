package ex.couponissue.coupon.service;

import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Slf4j(topic = "CouponIssueOptimisticLockFacade")
@Service
@RequiredArgsConstructor
public class CouponIssueOptimisticLockFacade {

    private final CouponIssueService couponIssueService;

    public void issue(String couponId, String userId) {
        int count = 0;
        while (true) {
            count++;
//            if (count > 10) {
//                log.error("쿠폰 발급에 실패했습니다.");
//                throw new RuntimeException("쿠폰 발급에 실패했습니다.");
//            }
            try {
                couponIssueService.issueWithOptimisticLock(couponId, userId);
                log.info("쿠폰 발급 완료 (시도 횟수: {})", count);
                break;
            } catch (ObjectOptimisticLockingFailureException | OptimisticLockException e) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }
}
