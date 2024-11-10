package ex.couponissue.coupon.service;

import ex.couponissue.coupon.domain.Coupon;
import ex.couponissue.coupon.domain.CouponIssueRequestCode;
import ex.couponissue.coupon.infra.CouponRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j(topic = "RedisService")
@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisScript<String> script;
    private final CouponRepository couponRepository;
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 쿠폰의 최대 발급량만 캐싱하여 저장 - 1시간 TTL
     */
    @Cacheable(value = "coupon-max-quantity", key = "#couponId")
    public int getMaxQuantity(String couponId) {
        return couponRepository.findById(couponId)
            .map(Coupon::getMaxQuantity)
            .orElseThrow(() -> new HttpClientErrorException(HttpStatusCode.valueOf(404), "존재하지 않는 쿠폰입니다."));
    }

    /**
     * 쿠폰 발급 요청을 Redis에 저장
     * 1. 유저가 이미 발급받았는지 검증
     * 2. 최대 발급량을 초과하지 않는지 검증
     */
    public void issueInRedis(String couponId, String userId, int maxQuantity) {
        String key = "couponIssueRequest::" + couponId;
        String code = stringRedisTemplate.execute(script, List.of(key), userId, String.valueOf(maxQuantity));

        CouponIssueRequestCode couponIssueRequestCode = CouponIssueRequestCode.from(code);
        couponIssueRequestCode.validate();
    }
}
