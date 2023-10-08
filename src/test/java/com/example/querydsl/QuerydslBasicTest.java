package com.example.querydsl;

import static com.example.querydsl.entity.QMember.*;
import static com.example.querydsl.entity.QTeam.*;
import static org.assertj.core.api.Assertions.*;
//option+ enter -> Qmember.member => member

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.querydsl.entity.Member;
import com.example.querydsl.entity.QMember;
import com.example.querydsl.entity.QTeam;
import com.example.querydsl.entity.Team;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

	JPAQueryFactory queryFactory;
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
		assertThat(findMember.getUsername()).isEqualTo("member1");
	}

	@Test
	public void startQuerydsl() {
		//member1을 찾아라.
		// JPAQueryFactory queryFactory = new JPAQueryFactory(em);
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
		assertThat(findMember.getUsername()).isEqualTo("member1");
	}

	@Test
	public void page(){
		// //List
		// List<Member> fetch = queryFactory
		// 	.selectFrom(member)
		// 	.fetch();
		// //단 건
		// Member findMember1 = queryFactory
		// 	.selectFrom(member)
		// 	.fetchOne();
		// //처음 한 건 조회
		// Member findMember2 = queryFactory
		// 	.selectFrom(member)
		// 	.fetchFirst();
		//페이징에서 사용
		QueryResults<Member> results = queryFactory
			.selectFrom(member)
			.fetchResults();

		// results.getTotal(); 문제가 쿼리가 한번 더 나감(하지만 복잡하고 중요한 페이징 쿼리에서는 이걸 사용하는게 맞음)
		// List<Member> content = results.getResults();

		//count 쿼리로 변경
		long count = queryFactory
			.selectFrom(member)
			.fetchCount();
	}

	/**
	 * 회원 정렬 순서
	 * 1. 회원 나이 내림차순(desc)
	 * 2. 회원 이름 올림차순(asc)
	 * 단 2에서 회원 이름이 없으면 마지막에 출력(nulls last)
	 */

	@Test
	public void sort() {
		em.persist(new Member(null, 100));
		em.persist(new Member("member5", 100));
		em.persist(new Member("member6", 100));
		List<Member> result = queryFactory
			.selectFrom(member)
			.where(member.age.eq(100))
			.orderBy(member.age.desc(), member.username.asc().nullsLast())
			.fetch();
		Member member5 = result.get(0);
		Member member6 = result.get(1);
		Member memberNull = result.get(2);
		assertThat(member5.getUsername()).isEqualTo("member5");
		assertThat(member6.getUsername()).isEqualTo("member6");
		assertThat(memberNull.getUsername()).isNull();
	}

	@Test
	public void paging() {
		QueryResults<Member> queryResults = queryFactory
			.selectFrom(member)
			.orderBy(member.username.desc())
			.offset(1)
			.limit(2)
			.fetchResults(); //결과를 총 출력 단건은 .fetch
		assertThat(queryResults.getTotal()).isEqualTo(4);
		assertThat(queryResults.getLimit()).isEqualTo(2);
		assertThat(queryResults.getOffset()).isEqualTo(1);
		assertThat(queryResults.getResults().size()).isEqualTo(2);
		//여기서 where절이 들어가면 성능상 어려운 부분이 생길 수 있어 분리해야할 수도 있다.
	}


	/**
	 * JPQL
	 * select
	 * COUNT(m), //회원수
	 * SUM(m.age), //나이 합
	 * AVG(m.age), //평균 나이
	 * MAX(m.age), //최대 나이
	 * MIN(m.age) //최소 나이
	 * from Member m
	 */
	@Test
	public void aggregation() throws Exception {
		List<Tuple> result = queryFactory //querydsl에서 제공하는 tuple->여러개의 타입이 있을 때 가져올 수 있다.
			.select(member.count(),
				member.age.sum(),
				member.age.avg(),
				member.age.max(),
				member.age.min())
			.from(member)
			.fetch();
		Tuple tuple = result.get(0);
		assertThat(tuple.get(member.count())).isEqualTo(4);
		assertThat(tuple.get(member.age.sum())).isEqualTo(100);
		assertThat(tuple.get(member.age.avg())).isEqualTo(25);
		assertThat(tuple.get(member.age.max())).isEqualTo(40);
		assertThat(tuple.get(member.age.min())).isEqualTo(10);

		//실무에서는 보통 tuple이 아닌 dto로 직접 뽑아온다
	}

	/**
	 * 팀의 이름과 각 팀의 평균 연령을 구해라.
	 */
	@Test
	public void group() throws Exception {
		List<Tuple> result = queryFactory
			.select(team.name, member.age.avg())
			.from(member)
			.join(member.team, team)
			.groupBy(team.name) //조건에서 가져옴 having으로 조건을 넣을 수 있음 여기서는 딱히...
			.fetch();
		Tuple teamA = result.get(0);
		Tuple teamB = result.get(1);
		assertThat(teamA.get(team.name)).isEqualTo("teamA");
		assertThat(teamA.get(member.age.avg())).isEqualTo(15);
		assertThat(teamB.get(team.name)).isEqualTo("teamB");
		assertThat(teamB.get(member.age.avg())).isEqualTo(35);
	}

	/**
	 * 팀 A에 소속된 모든 회원
	 */
	@Test
	public void join() throws Exception {
		QMember member = QMember.member;
		QTeam team = QTeam.team;
		List<Member> result = queryFactory
			.selectFrom(member)
			.join(member.team, team)
			.where(team.name.eq("teamA"))
			.fetch();
		assertThat(result)
			.extracting("username")
			.containsExactly("member1", "member2");
	}
}
