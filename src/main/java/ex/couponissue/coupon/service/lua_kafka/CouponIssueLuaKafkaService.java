package ex.couponissue.coupon.service.lua_kafka;

import com.github.ksuid.KsuidGenerator;
import ex.couponissue.coupon.domain.lua_kafka.CouponForLuaKafka;
import ex.couponissue.coupon.domain.lua_kafka.CouponIssueForLuaKafka;
import ex.couponissue.coupon.dto.request.CouponIssueReq;
import ex.couponissue.coupon.infra.CouponIssueForLuaKafkaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "CouponIssueService")
@Service
@RequiredArgsConstructor
public class CouponIssueLuaKafkaService {

    private final RedisService redisService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final CouponIssueForLuaKafkaRepository couponIssueForLuaKafkaRepository;

    /**
     * 쿠폰 발급 - LuaScript + Kafka
     */
    public void issue(String couponId, String userId) {
        // Redis+LuaScript를 이용한 쿠폰 발급 줄세우기
        CouponForLuaKafka coupon = redisService.getCoupon(couponId);// 캐싱된 쿠폰의 최대 발급량 조회
        redisService.issueInRedis(couponId, userId, coupon.getMaxQuantity()); // 레디스에 쿠폰 발급 요청 저장

        // Kafka를 시용한 실제 쿠폰 발급하기
        CouponIssueReq couponIssueReq = new CouponIssueReq(couponId, userId);
        kafkaTemplate.send("coupon.issue", couponIssueReq);
    }

    /**
     * 쿠폰 발급
     */
    @Transactional
    public void issueWithNoLock(String couponId, String userId) {
        CouponForLuaKafka coupon = redisService.getCoupon(couponId);// 캐싱된 쿠폰의 최대 발급량 조회
        CouponIssueForLuaKafka couponIssue = CouponIssueForLuaKafka.create(KsuidGenerator.generate(), userId, coupon);
        couponIssueForLuaKafkaRepository.saveAndFlush(couponIssue);
    }
}
