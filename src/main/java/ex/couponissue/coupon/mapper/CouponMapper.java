package ex.couponissue.coupon.mapper;

import ex.couponissue.coupon.domain.Coupon;
import ex.couponissue.coupon.dto.response.CouponGetRes;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CouponMapper {

    CouponGetRes toCouponGetRes(Coupon coupon);
}
