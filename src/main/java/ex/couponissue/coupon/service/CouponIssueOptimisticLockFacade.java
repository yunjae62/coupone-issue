package ex.couponissue.coupon.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j(topic = "CouponIssueOptimisticLockFacade")
@Service
@RequiredArgsConstructor
public class CouponIssueOptimisticLockFacade {

    private final CouponIssueService couponIssueService;

    public void issue(String couponId, String userId) {
        while (true) {
            try {
                couponIssueService.issueWithOptimisticLock(couponId, userId);
                break;
            } catch (Exception e) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }
}
