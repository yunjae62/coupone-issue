package ex.couponissue.coupon.endpoint;

import ex.couponissue.coupon.dto.request.CouponCreateReq;
import ex.couponissue.coupon.dto.response.CouponGetRes;
import ex.couponissue.coupon.service.CouponIssueService;
import ex.couponissue.coupon.service.CouponService;
import ex.couponissue.coupon.service.distribute.CouponIssueDistributeLockFacade;
import ex.couponissue.coupon.service.optimistic.CouponIssueOptimisticLockFacade;
import ex.couponissue.coupon.service.pessimistic.CouponIssuePessimisticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "CouponController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService couponService;
    private final CouponIssuePessimisticService couponIssuePessimisticService;
    private final CouponIssueService couponIssueService;
    private final CouponIssueOptimisticLockFacade couponIssueOptimisticLockFacade;
    private final CouponIssueDistributeLockFacade couponIssueDistributeLockFacade;

    /**
     * 쿠폰 단건 조회
     */
    @GetMapping("/{couponId}")
    public ResponseEntity<CouponGetRes> getCoupon(@PathVariable String couponId) {
        CouponGetRes response = couponService.getCoupon(couponId);
        return ResponseEntity.ok(response);
    }

    /**
     * 쿠폰 생성
     */
    @PostMapping
    public ResponseEntity<CouponGetRes> createCoupon(@RequestBody CouponCreateReq request) {
        CouponGetRes response = couponService.createCoupon(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 쿠폰 발급
     */
    @PostMapping("/{couponId}/issue")
    public ResponseEntity<Void> issueCoupon(@PathVariable String couponId, @RequestParam String userId) {
//        couponIssueService.issueWithPessimisticLock(couponId, userId); // 비관락
//        couponIssueOptimisticLockFacade.issue(couponId, userId); // 낙관락
//        couponIssueDistributeLockFacade.issue(couponId, userId); // 분산락
        couponIssueService.issueWithLuaScriptAndKafka(couponId, userId); // Redis(Lua Script) + Kafka
        return ResponseEntity.ok().build();
    }

    /**
     * 쿠폰 발급 - 비관락
     */
    @PostMapping("/{couponId}/issue/pessimistic")
    public ResponseEntity<Void> issueCouponWithPessimistic(@PathVariable String couponId, @RequestParam String userId) {
        couponIssuePessimisticService.issue(couponId, userId);
        return ResponseEntity.ok().build();
    }

    /**
     * 쿠폰 발급 - 낙관락
     */
    @PostMapping("/{couponId}/issue/optimistic")
    public ResponseEntity<Void> issueCouponWithOptimistic(@PathVariable String couponId, @RequestParam String userId) {
        couponIssueOptimisticLockFacade.issue(couponId, userId);
        return ResponseEntity.ok().build();
    }

    /**
     * 쿠폰 발급 - 분산락
     */
    @PostMapping("/{couponId}/issue/distribute")
    public ResponseEntity<Void> issueCouponWithDistribute(@PathVariable String couponId, @RequestParam String userId) {
        couponIssueDistributeLockFacade.issue(couponId, userId);
        return ResponseEntity.ok().build();
    }
}
