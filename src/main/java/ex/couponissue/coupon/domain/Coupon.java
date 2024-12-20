package ex.couponissue.coupon.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "coupons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

    @Id
    private String id;

    private String name;
    private Integer nowQuantity;
    private Integer maxQuantity;

    @OneToMany(mappedBy = "coupon", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private Set<CouponIssue> couponIssues;

    public static Coupon create(String id, String name, Integer nowQuantity, Integer maxQuantity) {
        Coupon coupon = new Coupon();

        coupon.id = id;
        coupon.name = name;
        coupon.nowQuantity = nowQuantity;
        coupon.maxQuantity = maxQuantity;

        return coupon;
    }

    public void issue(CouponIssue couponIssue) {
        if (nowQuantity >= maxQuantity) {
            throw new IllegalArgumentException("발급할 수 있는 쿠폰이 없습니다.");
        }

        couponIssues.add(couponIssue);
        nowQuantity++;
    }
}
