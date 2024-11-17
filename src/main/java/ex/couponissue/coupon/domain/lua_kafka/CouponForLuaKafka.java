package ex.couponissue.coupon.domain.lua_kafka;

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
@Table(name = "coupon_for_lua_kafka")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponForLuaKafka {

    @Id
    private String id;

    private String name;
    private Integer maxQuantity;

    @OneToMany(mappedBy = "coupon", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private Set<CouponIssueForLuaKafka> couponIssues;

    public static CouponForLuaKafka create(String id, String name, Integer nowQuantity, Integer maxQuantity) {
        CouponForLuaKafka coupon = new CouponForLuaKafka();

        coupon.id = id;
        coupon.name = name;
        coupon.maxQuantity = maxQuantity;

        return coupon;
    }
}
