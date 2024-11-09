package ex.couponissue.coupon.domain;

import com.github.ksuid.KsuidGenerator;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private Integer maxQuantity;
    private Integer nowQuantity;

    public static Coupon create(String name, Integer maxQuantity, Integer nowQuantity) {
        Coupon coupon = new Coupon();

        coupon.id = KsuidGenerator.generate();
        coupon.name = name;
        coupon.maxQuantity = maxQuantity;
        coupon.nowQuantity = nowQuantity;

        return coupon;
    }

    public void issue() {
        if (nowQuantity >= maxQuantity) {
            throw new IllegalArgumentException("발급할 수 있는 쿠폰이 없습니다.");
        }

        nowQuantity++;
    }
}