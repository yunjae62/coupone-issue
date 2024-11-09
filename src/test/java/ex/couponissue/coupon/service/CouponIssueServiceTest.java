package ex.couponissue.coupon.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.ksuid.KsuidGenerator;
import ex.couponissue.coupon.domain.Coupon;
import ex.couponissue.coupon.infra.CouponRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CouponIssueServiceTest {

    private String couponId;

    @Autowired
    private CouponIssueService couponIssueService;

    @Autowired
    private CouponRepository couponRepository;

    @BeforeEach
    void setUp() {
        Coupon coupon = Coupon.create(KsuidGenerator.generate(), "쿠폰-1", 0, 1000000);
        Coupon savedCoupon = couponRepository.save(coupon);
        couponId = savedCoupon.getId();
    }

    @AfterEach
    void tearDown() {
        couponRepository.deleteById(couponId);
    }

    @Test
    public void 동시에_100명이_주문() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    couponIssueService.issue(couponId, KsuidGenerator.generate());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Coupon coupon = couponRepository.findById(couponId).orElseThrow();

        // 100 - (100 * 1) = 0
        assertEquals(0, coupon.getNowQuantity());
    }
}