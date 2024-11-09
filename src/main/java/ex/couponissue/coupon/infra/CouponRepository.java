package ex.couponissue.coupon.infra;

import ex.couponissue.coupon.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, String> {

}
