package ex.couponissue.coupon.service;

import static org.assertj.core.api.Assertions.assertThat;

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

    private static final Integer maxQuantity = 100;
    private String couponId;

    @Autowired
    private CouponIssueService couponIssueService;

    @Autowired
    private CouponIssueOptimisticLockFacade couponIssueOptimisticLockFacade;

    @Autowired
    private CouponRepository couponRepository;

    @BeforeEach
    void setUp() {
        Coupon coupon = Coupon.create(KsuidGenerator.generate(), "쿠폰-1", 0, maxQuantity);
        Coupon savedCoupon = couponRepository.save(coupon);
        couponId = savedCoupon.getId();
    }

    @AfterEach
    void tearDown() {
        couponRepository.deleteById(couponId);
    }

    @Test
    public void 동시_쿠폰_발급_비관적락() throws InterruptedException {
        int threadCount = maxQuantity;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    couponIssueService.issueWithPessimisticLock(couponId, KsuidGenerator.generate());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Coupon coupon = couponRepository.findById(couponId).orElseThrow();
        assertThat(coupon.getNowQuantity()).isEqualTo(maxQuantity);
    }

    @Test
    public void 동시_쿠폰_발급_낙관적락() throws InterruptedException {
        int threadCount = maxQuantity;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    couponIssueOptimisticLockFacade.issue(couponId, KsuidGenerator.generate());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Coupon coupon = couponRepository.findById(couponId).orElseThrow();
        assertThat(coupon.getNowQuantity()).isEqualTo(maxQuantity);
    }
}