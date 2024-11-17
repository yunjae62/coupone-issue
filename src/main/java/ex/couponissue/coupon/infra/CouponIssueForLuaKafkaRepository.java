package ex.couponissue.coupon.infra;

import ex.couponissue.coupon.domain.lua_kafka.CouponIssueForLuaKafka;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponIssueForLuaKafkaRepository extends JpaRepository<CouponIssueForLuaKafka, String> {

}
