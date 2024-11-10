package ex.couponissue.coupon.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponIssueRequestCode {

    SUCCESS("success"),
    DUPLICATED_COUPON_ISSUE("duplicated"),
    INVALID_COUPON_ISSUE_QUANTITY("invalid");

    private final String code;

    public static CouponIssueRequestCode from(String code) {
        return switch (code) {
            case "success" -> SUCCESS;
            case "duplicated" -> DUPLICATED_COUPON_ISSUE;
            case "invalid" -> INVALID_COUPON_ISSUE_QUANTITY;
            default -> throw new IllegalArgumentException("잘못된 code 입니다.");
        };
    }

    public void validate() {
        if (this.code.equals(INVALID_COUPON_ISSUE_QUANTITY.code)) {
            throw new IllegalArgumentException("쿠폰 발급 수량이 초과되었습니다.");
        }

        if (this.code.equals(DUPLICATED_COUPON_ISSUE.code)) {
            throw new IllegalArgumentException("이미 발급된 쿠폰입니다.");
        }
    }
}
