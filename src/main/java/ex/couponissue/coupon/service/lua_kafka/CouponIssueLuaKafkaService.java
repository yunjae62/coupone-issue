package ex.couponissue.coupon.service.lua_kafka;

import com.github.ksuid.KsuidGenerator;
import ex.couponissue.coupon.domain.Coupon;
import ex.couponissue.coupon.domain.CouponIssue;
import ex.couponissue.coupon.dto.request.CouponIssueReq;
import ex.couponissue.coupon.infra.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j(topic = "CouponIssueService")
@Service
@RequiredArgsConstructor
public class CouponIssueLuaKafkaService {

    private final RedisService redisService;
    private final CouponRepository couponRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * 쿠폰 발급 - LuaScript + Kafka
     */
    public void issue(String couponId, String userId) {
        // Redis+LuaScript를 이용한 쿠폰 발급 줄세우기
        int maxQuantity = redisService.getMaxQuantity(couponId); // 캐싱된 쿠폰의 최대 발급량 조회
        redisService.issueInRedis(couponId, userId, maxQuantity); // 레디스에 쿠폰 발급 요청 저장

        // Kafka를 시용한 실제 쿠폰 발급하기
        CouponIssueReq couponIssueReq = new CouponIssueReq(couponId, userId);
        kafkaTemplate.send("coupon.issue", couponIssueReq);
    }

    /**
     * 쿠폰 발급 - 비관적 락
     */
    @Transactional
    public void issueWithPessimisticLock(String couponId, String userId) {
        // 쿠폰 조회
        Coupon coupon = couponRepository.findByIdWithPessimisticLock(couponId)
            .orElseThrow(() -> new HttpClientErrorException(HttpStatusCode.valueOf(404), "존재하지 않는 쿠폰입니다."));

        // 쿠폰 발급
        CouponIssue couponIssue = CouponIssue.create(KsuidGenerator.generate(), userId, coupon);
        coupon.issue(couponIssue);
        couponRepository.saveAndFlush(coupon); // CascadeType.MERGE 로 인해 couponIssue 도 같이 저장됨
    }
}
