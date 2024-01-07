package com.masikga.itwc.common.jpa;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public abstract class MemberTimeBaseEntity extends TimeBaseEntity {

	@CreatedBy
	private Long createdBy;

	@LastModifiedBy
	private Long updatedBy;
}
