package com.example.querydsl.repository;

import static com.example.querydsl.entity.QMember.*;
import static com.example.querydsl.entity.QTeam.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.querydsl.dto.MemberTeamDto;
import com.example.querydsl.dto.QMemberTeamDto;
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
	//Builder 사용
	//회원명, 팀명, 나이(ageGoe, ageLoe)
	public List<MemberTeamDto> searchByBuilder(MemberJpaRepository condition) {
		return queryFactory
			.select(new QMemberTeamDto(
				member.id,
				member.username,
				member.age,
				team.id,
				team.name))
			.from(member)
			.leftJoin(member.team, team)
			.fetch();
	}

}
