package com.example.demo.helper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.member.model.Member;
import com.google.common.base.Objects;

import jakarta.persistence.EntityManager;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class EntityEqualsHashCode {

	@Autowired
	private EntityManager em;

	@Autowired
	private DataBaseCleanUp dataBaseCleanUp;

	@AfterEach
	public void tearDown() {
		dataBaseCleanUp.truncateAllEntity();
	}

	@Test
	@DisplayName("실제 엔티티와 프록시 엔티티는 동일하다.")
	public void
	proxy1() {
		Member newMember = createMember();
		em.persist(newMember);
		em.flush();
		em.clear();

		Member proxyMember = em.getReference(Member.class, 1L);

		assert newMember.hashCode() == proxyMember.hashCode();
		assert proxyMember.hashCode() == newMember.hashCode();

		assert Objects.equal(newMember, proxyMember);
		assert Objects.equal(proxyMember, newMember);
	}

	@Test
	@DisplayName("실제 엔티티와 프록시 엔티티는 동등하다.")
	public void proxy2() {
		Member newMember = createMember();
		em.persist(newMember);
		em.flush();
		em.clear();

		Member proxyMember = em.getReference(Member.class, 1L);

		assert Objects.equal(proxyMember, newMember);
		assert Objects.equal(newMember, proxyMember);
	}

	@Test
	@DisplayName("id가 null인 new 상태 엔티티와 managed 상태 엔티티는 동일하다.")
	public void AlwaysEqualsNewEntity() {
		Member newMember = createMember();
		em.persist(newMember);
		em.flush();
		em.clear();
		Member newMemberWithIdNull = createMember();

		Member managedMember = em.find(Member.class, 1L);

		assert newMemberWithIdNull.hashCode() == managedMember.hashCode();
		assert managedMember.hashCode() == newMemberWithIdNull.hashCode();

		assert Objects.equal(newMemberWithIdNull, managedMember);
		assert Objects.equal(managedMember, newMemberWithIdNull);
	}

	private Member createMember() {
		return Member.builder()
			.id(null)
			.serviceId("명수")
			.nickname("닉네임 1")
			.password("은밀한 패스워드77")
			.build();
	}
}