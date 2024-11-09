package ex.couponissue.coupon.service;

import ex.couponissue.coupon.domain.Coupon;
import ex.couponissue.coupon.dto.request.CouponCreateReq;
import ex.couponissue.coupon.dto.response.CouponGetRes;
import ex.couponissue.coupon.infra.CouponRepository;
import ex.couponissue.coupon.mapper.CouponMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "CouponService")
@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponMapper couponMapper;
    private final CouponRepository couponRepository;

    /**
     * 쿠폰 생성
     */
    @Transactional
    public CouponGetRes createCoupon(CouponCreateReq request) {
        Coupon coupon = Coupon.create(request.name(), 0, request.maxQuantity());
        Coupon savedCoupon = couponRepository.save(coupon);
        return couponMapper.toCouponGetRes(savedCoupon);
    }
}
