package ex.couponissue.config;

import ex.couponissue.coupon.domain.Coupon;
import ex.couponissue.coupon.infra.CouponRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class AppConfig {

    private static final String COUPON_ID = "2oeJ7WOeFLztfouBhspbJn3rsFd";

    @Bean
    @Transactional
    public ApplicationRunner databaseInitializer(CouponRepository couponRepository) {
        return args -> {
            couponRepository.deleteById(COUPON_ID);
            Coupon coupon = Coupon.create(COUPON_ID, "쿠폰-1", 0, 100_000);
            couponRepository.save(coupon);
        };
    }
}