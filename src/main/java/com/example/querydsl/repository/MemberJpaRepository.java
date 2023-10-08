package com.example.querydsl.repository;

import static com.example.querydsl.entity.QMember.*;
import static com.example.querydsl.entity.QTeam.*;
import static org.springframework.util.StringUtils.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.example.querydsl.dto.MemberTeamDto;
import com.example.querydsl.dto.QMemberTeamDto;
import com.example.querydsl.entity.Member;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class MemberJpaRepository {

	private final EntityManager em;
	private final JPAQueryFactory queryFactory;
	public MemberJpaRepository(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}

	public void save(Member member) {
		em.persist(member);
	}
	public Optional<Member> findById(Long id) {
		Member findMember = em.find(Member.class, id);
		return Optional.ofNullable(findMember);
	}
	public List<Member> findAll() {
		return em.createQuery("select m from Member m", Member.class)
			.getResultList();
	}


	//Builder 사용
	//회원명, 팀명, 나이(ageGoe, ageLoe)
	// public List<MemberTeamDto> searchByBuilder(MemberJpaRepository condition) {
	//
	// 	BooleanBuilder builder = new BooleanBuilder();
	// 	if (hasText(condition.getUsername)) { //StringUtils.hasText (null, "")
	// 		builder.and(member.username.eq(condition.getUsername()));
	// 	}
	// 	if (hasText(condition.getTeamName())) {
	// 		builder.and(team.name.eq(condition.getTeamName()));
	// 	}
	// 	if (condition.getAgeGoe() != null) {
	// 		builder.and(member.age.goe(condition.getAgeGoe()));
	// 	}
	// 	if (condition.getAgeLoe() != null) {
	// 		builder.and(member.age.loe(condition.getAgeLoe()));
	// 	}
	// 	return queryFactory
	// 		.select(new QMemberTeamDto(
	// 			member.id,
	// 			member.username,
	// 			member.age,
	// 			team.id,
	// 			team.name))
	// 		.from(member)
	// 		.leftJoin(member.team, team)
	// 		.where(builder)
	// 		.fetch();
	// }

}
