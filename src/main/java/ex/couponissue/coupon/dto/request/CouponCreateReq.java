package ex.couponissue.coupon.dto.request;

public record CouponCreateReq(
    String name,
    Integer maxQuantity
) {

}
