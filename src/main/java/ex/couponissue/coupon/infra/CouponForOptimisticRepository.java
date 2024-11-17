package ex.couponissue.coupon.infra;

import ex.couponissue.coupon.domain.optimistic.CouponForOptimistic;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface CouponForOptimisticRepository extends JpaRepository<CouponForOptimistic, String> {

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT c FROM CouponForOptimistic c WHERE c.id = :id")
    Optional<CouponForOptimistic> findByIdWithOptimisticLock(String id);
}
