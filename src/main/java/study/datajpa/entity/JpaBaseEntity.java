package study.datajpa.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass //상속 인데 속성만 슉 가져가는 느낌이라고 ㅎㅎ....흠 ㅎㅎ....
public class JpaBaseEntity {
    @Column(updatable = false)
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @PrePersist //persit시에 작동 --> .save()
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updatedDate = now;
    }

    @PreUpdate  //flush 시에 작동
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }
}
