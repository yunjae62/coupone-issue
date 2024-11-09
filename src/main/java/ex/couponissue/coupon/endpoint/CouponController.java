package ex.couponissue.coupon.endpoint;

import ex.couponissue.coupon.dto.request.CouponCreateReq;
import ex.couponissue.coupon.dto.response.CouponGetRes;
import ex.couponissue.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "CouponController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService couponService;

    /**
     * 쿠폰 단건 조회
     */
    @GetMapping("/{couponId}")
    public ResponseEntity<CouponGetRes> getCoupon(@PathVariable String couponId) {
        CouponGetRes response = couponService.getCoupon(couponId);
        return ResponseEntity.ok(response);
    }

    /**
     * 쿠폰 생성
     */
    @PostMapping
    public ResponseEntity<CouponGetRes> createCoupon(@RequestBody CouponCreateReq request) {
        CouponGetRes response = couponService.createCoupon(request);
        return ResponseEntity.ok(response);
    }
}
