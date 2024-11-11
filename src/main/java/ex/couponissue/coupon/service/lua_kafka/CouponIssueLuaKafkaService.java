package ex.couponissue.coupon.service.lua_kafka;

import ex.couponissue.coupon.dto.request.CouponIssueReq;
import ex.couponissue.coupon.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j(topic = "CouponIssueService")
@Service
@RequiredArgsConstructor
public class CouponIssueLuaKafkaService {

    private final RedisService redisService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * 쿠폰 발급 - LuaScript + Kafka
     */
    public void issueWithLuaScriptAndKafka(String couponId, String userId) {
        // Redis+LuaScript를 이용한 쿠폰 발급 줄세우기
        int maxQuantity = redisService.getMaxQuantity(couponId); // 캐싱된 쿠폰의 최대 발급량 조회
        redisService.issueInRedis(couponId, userId, maxQuantity); // 레디스에 쿠폰 발급 요청 저장

        // Kafka를 시용한 실제 쿠폰 발급하기
        CouponIssueReq couponIssueReq = new CouponIssueReq(couponId, userId);
        kafkaTemplate.send("coupon.issue", couponIssueReq);
    }
}
