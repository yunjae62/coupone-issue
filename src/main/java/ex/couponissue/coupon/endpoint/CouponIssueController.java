package ex.couponissue.coupon.endpoint;

import ex.couponissue.coupon.service.CouponIssueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "CouponIssueController")
@RestController
@RequiredArgsConstructor
public class CouponIssueController {

    private final CouponIssueService couponIssueService;

    /**
     * 쿠폰 발급
     */
    @PostMapping("/coupons/{couponId}/issue")
    public ResponseEntity<?> issueCoupon(@PathVariable String couponId, @RequestParam String userId) {
        couponIssueService.issueWithPessimisticLock(couponId, userId);
        return ResponseEntity.ok().build();
    }
}
