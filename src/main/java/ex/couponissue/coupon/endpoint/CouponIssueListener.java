package ex.couponissue.coupon.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ex.couponissue.coupon.dto.request.CouponIssueReq;
import ex.couponissue.coupon.service.CouponIssueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j(topic = "CouponIssueListener")
@Component
@RequiredArgsConstructor
public class CouponIssueListener {

    private final ObjectMapper objectMapper;
    private final CouponIssueService couponIssueService;

    @KafkaListener(topics = "coupon.issue", groupId = "coupon.issue.create")
    public void listen(String message) throws JsonProcessingException {
        CouponIssueReq request = objectMapper.readValue(message, CouponIssueReq.class);
        couponIssueService.issueWithPessimisticLock(request.couponId(), request.userId());
    }
}
