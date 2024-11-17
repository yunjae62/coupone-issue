package ex.couponissue.coupon.infra;

import ex.couponissue.coupon.domain.lua_kafka.CouponForLuaKafka;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponForLuaKafkaRepository extends JpaRepository<CouponForLuaKafka, String> {

}
