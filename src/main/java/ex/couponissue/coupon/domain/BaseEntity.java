package ex.couponissue.coupon.domain;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PostPersist;
import lombok.Getter;
import org.springframework.data.domain.Persistable;

@Getter
@MappedSuperclass
public abstract class BaseEntity implements Persistable<String> {

    private transient boolean isNew = true; // 새로운 엔티티 여부를 추적할 필드

    @Override
    public boolean isNew() {
        return this.isNew;
    }

    // persist 후에 기존 엔티티로 인식되도록 설정
    @PostPersist
    public void postPersist() {
        this.isNew = false;
    }
}
