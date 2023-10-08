package com.example.querydsl;

import static com.example.querydsl.entity.QMember.*;
//option+ enter -> Qmember.member => member

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.querydsl.entity.Member;
import com.example.querydsl.entity.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {
	@PersistenceContext
	EntityManager em;
	@BeforeEach
	public void before() {
		Team teamA = new Team("teamA");
		Team teamB = new Team("teamB");
		em.persist(teamA);
		em.persist(teamB);
		Member member1 = new Member("member1", 10, teamA);
		Member member2 = new Member("member2", 20, teamA);
		Member member3 = new Member("member3", 30, teamB);
		Member member4 = new Member("member4", 40, teamB);
		em.persist(member1);
		em.persist(member2);
		em.persist(member3);
		em.persist(member4);
	}

	@Test
	public void startJPQL() {
		//member1을 찾아라.
		String qlString =
			"select m from Member m " +
				"where m.username = :username";
		Member findMember = em.createQuery(qlString, Member.class)
			.setParameter("username", "member1")
			.getSingleResult();
		Assertions.assertThat(findMember.getUsername()).isEqualTo("member1");
	}

	@Test
	public void startQuerydsl() {
		//member1을 찾아라.
		JPAQueryFactory queryFactory = new JPAQueryFactory(em);
		//필드 밖으로 뺴서 JPAQueryFactory queryFactory;도 가능
		// QMember m = new QMember("m");
		// Member findMember = queryFactory
		// 	.select(m)
		// 	.from(m)
		// 	.where(m.username.eq("member1"))//파라미터 바인딩 처리
		// 	.fetchOne();

		Member findMember = queryFactory
			.select(member)
			.from(member)
			.where(member.username.eq("member1"))
			.fetchOne();
		Assertions.assertThat(findMember.getUsername()).isEqualTo("member1");
	}
}
