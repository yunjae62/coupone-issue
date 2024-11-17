package ex.couponissue.coupon.domain.optimistic;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "coupon_for_opstimistic")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponForOptimistic {

    @Id
    private String id;

    private String name;
    private Integer nowQuantity;
    private Integer maxQuantity;

    @Version // 낙관적 락에 필요
    private Long version = 0L;

    @OneToMany(mappedBy = "coupon", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private Set<CouponIssueForOptimistic> couponIssues;

    public static CouponForOptimistic create(String id, String name, Integer nowQuantity, Integer maxQuantity) {
        CouponForOptimistic coupon = new CouponForOptimistic();

        coupon.id = id;
        coupon.name = name;
        coupon.nowQuantity = nowQuantity;
        coupon.maxQuantity = maxQuantity;

        return coupon;
    }

    public void issue() {
        if (nowQuantity >= maxQuantity) {
            throw new IllegalArgumentException("발급할 수 있는 쿠폰이 없습니다.");
        }

        nowQuantity++;
    }

    public void issue(CouponIssueForOptimistic couponIssue) {
        if (nowQuantity >= maxQuantity) {
            throw new IllegalArgumentException("발급할 수 있는 쿠폰이 없습니다.");
        }

        couponIssues.add(couponIssue);
        nowQuantity++;
    }
}
