package com.example.querydsl.domain.yhcourse.repository;

import com.example.querydsl.domain.yhcourse.dto.MemberSearchCondition;
import com.example.querydsl.domain.yhcourse.dto.MemberTeamDto;
import com.example.querydsl.domain.yhcourse.dto.QMemberTeamDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

import static com.example.querydsl.domain.yhcourse.entity.QMember.*;
import static com.example.querydsl.domain.yhcourse.entity.QTeam.*;
import static org.springframework.util.StringUtils.*;

import java.util.List;

public class MemberRepositoryImpl implements MemberRepositoryCustom{
	private final JPAQueryFactory queryFactory;
	public MemberRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	//회원명, 팀명, 나이(ageGoe, ageLoe)
	public List<MemberTeamDto> search(MemberSearchCondition condition) {
		return queryFactory
			.select(new QMemberTeamDto(
				member.id,
				member.username,
				member.age,
				team.id,
				team.name))
			.from(member)
			.leftJoin(member.team, team)
			.where(usernameEq(condition.getUsername()),
				teamNameEq(condition.getTeamName()),
				ageGoe(condition.getAgeGoe()),
				ageLoe(condition.getAgeLoe()))
			.fetch();
	}

	private BooleanExpression usernameEq(String username) {
		return isEmpty(username) ? null : member.username.eq(username);
	}
	private BooleanExpression teamNameEq(String teamName) {
		return isEmpty(teamName) ? null : team.name.eq(teamName);
	}
	private BooleanExpression ageGoe(Integer ageGoe) {
		return ageGoe == null ? null : member.age.goe(ageGoe);
	}
	private BooleanExpression ageLoe(Integer ageLoe) {
		return ageLoe == null ? null : member.age.loe(ageLoe);
	} }




