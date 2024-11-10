package ex.couponissue.coupon.service;

import com.github.ksuid.KsuidGenerator;
import ex.couponissue.coupon.domain.Coupon;
import ex.couponissue.coupon.domain.CouponIssue;
import ex.couponissue.coupon.infra.CouponIssueRepository;
import ex.couponissue.coupon.infra.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j(topic = "CouponIssueService")
@Service
@RequiredArgsConstructor
public class CouponIssueService {

    private final CouponRepository couponRepository;
    private final CouponIssueRepository couponIssueRepository;

    /**
     * 쿠폰 발급 - 비관적 락
     */
    @Transactional
    public void issueWithPessimisticLock(String couponId, String userId) {
        // 쿠폰 조회
        Coupon coupon = couponRepository.findByIdWithPessimisticLock(couponId)
            .orElseThrow(() -> new HttpClientErrorException(HttpStatusCode.valueOf(404), "존재하지 않는 쿠폰입니다."));

        // 쿠폰 발급
        coupon.issue();
        CouponIssue couponIssue = CouponIssue.create(KsuidGenerator.generate(), userId, coupon);
        couponIssueRepository.save(couponIssue);
    }

    /**
     * 쿠폰 발급 - 낙관적 락
     */
    @Transactional
    public void issueWithOptimisticLock(String couponId, String userId) {
        // 쿠폰 조회
        Coupon coupon = couponRepository.findByIdWithOptimisticLock(couponId)
            .orElseThrow(() -> new HttpClientErrorException(HttpStatusCode.valueOf(404), "존재하지 않는 쿠폰입니다."));

        // 쿠폰 발급
        coupon.issue();
        CouponIssue couponIssue = CouponIssue.create(KsuidGenerator.generate(), userId, coupon);
        couponIssueRepository.save(couponIssue);
    }

    /**
     * 쿠폰 발급 - 분산 락
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, timeout = 15)
    public void issueWithDistributedLock(String couponId, String userId) {
        // 쿠폰 조회
        Coupon coupon = couponRepository.findById(couponId)
            .orElseThrow(() -> new HttpClientErrorException(HttpStatusCode.valueOf(404), "존재하지 않는 쿠폰입니다."));

        // 쿠폰 발급
        coupon.issue();
        CouponIssue couponIssue = CouponIssue.create(KsuidGenerator.generate(), userId, coupon);
        couponIssueRepository.save(couponIssue);
    }
}
