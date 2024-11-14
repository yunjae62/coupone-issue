package ex.couponissue.coupon.service.optimistic;

import com.github.ksuid.KsuidGenerator;
import ex.couponissue.aop.optimistic.OptimisticLock;
import ex.couponissue.coupon.domain.Coupon;
import ex.couponissue.coupon.domain.CouponIssue;
import ex.couponissue.coupon.infra.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j(topic = "CouponIssueService")
@Service
@RequiredArgsConstructor
public class CouponIssueOptimisticService {

    private final CouponRepository couponRepository;

    /**
     * 쿠폰 발급 - 낙관적 락
     * <p>
     * ****** Coupon 엔티티에 `@Version` 주석 해제 해야 함!!!!!!!!! ******
     */
//    @Transactional
    @OptimisticLock
    public void issue(String couponId, String userId) {
        // 쿠폰 조회
        Coupon coupon = couponRepository.findByIdWithOptimisticLock(couponId)
            .orElseThrow(() -> new HttpClientErrorException(HttpStatusCode.valueOf(404), "존재하지 않는 쿠폰입니다."));

        // 쿠폰 발급
        CouponIssue couponIssue = CouponIssue.create(KsuidGenerator.generate(), userId, coupon);
        coupon.issue(couponIssue);
        couponRepository.saveAndFlush(coupon); // CascadeType.MERGE 로 인해 couponIssue 도 같이 저장됨
    }
}
