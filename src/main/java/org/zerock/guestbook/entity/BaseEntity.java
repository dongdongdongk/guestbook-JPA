package org.zerock.guestbook.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(value = { AuditingEntityListener.class})
@Getter
abstract class BaseEntity {
    @CreatedDate // 엔티티의 생성 시간을 자동으로 관리하고 엔티티가 언제 생성되었는지 추적하기 위한 기능
    @Column(name = "regdate", updatable = false)   // updatable = false로 설정되어 엔티티를 업데이트할 때 수정되지 않도록한다
    private LocalDateTime regDate;
    @LastModifiedDate  // 엔티티의 수정 시간을 자동으로 관리하고 엔티티가 언제 수정되었는지 추적하기 위한 기능을 제공
    @Column(name = "moddate")
    private LocalDateTime modDate;
}
