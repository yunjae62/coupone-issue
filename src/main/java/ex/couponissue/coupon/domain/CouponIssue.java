package ex.couponissue.coupon.domain;

import com.github.ksuid.KsuidGenerator;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "coupon_issues")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponIssue {

    @Id
    private String id;

    private String userId;

    @Enumerated(EnumType.STRING)
    private CouponStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    public static CouponIssue create(String userId, Coupon coupon) {
        CouponIssue couponIssue = new CouponIssue();

        couponIssue.id = KsuidGenerator.generate();
        couponIssue.userId = userId;
        couponIssue.status = CouponStatus.ISSUED;
        couponIssue.coupon = coupon;

        return couponIssue;
    }

    public void use(String userId) {
        if (!this.userId.equals(userId)) {
            throw new IllegalArgumentException("사용자가 일치하지 않습니다.");
        }

        if (status != CouponStatus.ISSUED) {
            throw new IllegalArgumentException("사용할 수 없는 쿠폰입니다.");
        }

        status = CouponStatus.USED;
    }
}
