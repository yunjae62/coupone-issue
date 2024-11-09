package ex.couponissue.coupon.dto.response;

public record CouponGetRes(
    String id,
    String name,
    Integer nowQuantity,
    Integer maxQuantity
) {

}
