package ex.couponissue.coupon.infra;

import ex.couponissue.coupon.domain.CouponIssue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponIssueRepository extends JpaRepository<CouponIssue, String> {

}
