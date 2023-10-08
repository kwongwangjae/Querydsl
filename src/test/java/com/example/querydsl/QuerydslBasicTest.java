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

import com.example.querydsl.dto.MemberDto;
import com.example.querydsl.dto.QMemberDto;
import com.example.querydsl.dto.UserDto;
import com.example.querydsl.entity.Member;
import com.example.querydsl.entity.QMember;
import com.example.querydsl.entity.QTeam;
import com.example.querydsl.entity.Team;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
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
		this.queryFactory = new JPAQueryFactory(em); //초기화
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
	public void page() {
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

	@Test
	public void findDtoBySetter() {
		List<MemberDto> result = queryFactory
			.select(Projections.bean(MemberDto.class,
				member.username,
				member.age))
			.from(member)
			.fetch();
		for (MemberDto memberDto : result) {
			System.out.println("memberDto = " + memberDto);
		}
	}

	@Test
	public void findDtoByField() { //getter, setter가 필요가 없음
		List<MemberDto> result = queryFactory
			.select(Projections.fields(MemberDto.class,
				member.username,
				member.age))
			.from(member)
			.fetch();
		for (MemberDto memberDto : result) {
			System.out.println("memberDto = " + memberDto);
		}
	}

	@Test
	public void findDtoByConstructor() { //생성된 dto와 같아야 함 생성자가 호출됨, runtime오류가 나옴(마지막에 오류가 발생)
		List<MemberDto> result = queryFactory
			.select(Projections.constructor(MemberDto.class,
				member.username,
				member.age))
			.from(member)
			.fetch();
		for (MemberDto memberDto : result) {
			System.out.println("memberDto = " + memberDto);
		}
	}

	@Test
	public void findUserDto(){
		QMember memberSub = new QMember("memberSub");
		List<UserDto> result = queryFactory
			.select(Projections.fields(UserDto.class,
					member.username.as("name"), //별칭이 다를 때 as로 별칭을 맞춤


					ExpressionUtils.as( //property나 field 접근생성 방식에서 이름이 다를 때 해결, 서브쿼리일 때
						JPAExpressions
							.select(memberSub.age.max())
							.from(memberSub), "age")
				)
			).from(member)
			.fetch();
	}

	@Test
	public void findDtoByQueryProjection(){ //Qdto에 있는 내용을 가져와서 해줌, 미리 오류를 발생시켜 줌
		List<MemberDto> result = queryFactory //constructor이랑 비슷하긴 함
			.select(new QMemberDto(member.username, member.age))
			.from(member)
			.fetch();

		for(MemberDto memberDto : result){
			System.out.println(memberDto);
		}
		//단점: Q파일이 있어야 함, dto자체가 querydsl에 의존성을 가지게 됨
	}

	@Test
	public void dynamicQuery_BooleanBuilder() throws Exception { //본인이 직접 q파일을 생성함 그래서 이미 만들어져있으면 문제가 발생
		String usernameParam = "member1";
		Integer ageParam = 10;

		List<Member> result = searchMember1(usernameParam, ageParam);
		Assertions.assertThat(result.size()).isEqualTo(1);
	}
	private List<Member> searchMember1(String usernameCond, Integer ageCond) {
		BooleanBuilder builder = new BooleanBuilder();
		if (usernameCond != null) {
			builder.and(member.username.eq(usernameCond));
		}
		if (ageCond != null) {
			builder.and(member.age.eq(ageCond));
		}
		return queryFactory
			.selectFrom(member)
			.where(builder)
			.fetch();
	}


	@Test
	public void 동적쿼리_WhereParam() throws Exception { //where조건에서 null은 무시된다.
		String usernameParam = "member1";
		Integer ageParam = 10;
		List<Member> result = searchMember2(usernameParam, ageParam);
		Assertions.assertThat(result.size()).isEqualTo(1);
	}
	private List<Member> searchMember2(String usernameCond, Integer ageCond) {
		return queryFactory
			.selectFrom(member)
			.where(usernameEq(usernameCond), ageEq(ageCond))  //개발할 때는 어차피 여기가 중요하다.
			//.where(allEq(usernameCond, ageCond)) 조립이 가능
			.fetch();
	}
	private BooleanExpression usernameEq(String usernameCond) { //메서드를 다른 쿼리에서도 재활용 할 수 있다.
		return usernameCond != null ? member.username.eq(usernameCond) : null;
	}
	private BooleanExpression ageEq(Integer ageCond) { //조립을 위해 BooleanExpression사용 원래 Predicate 였음
		return ageCond != null ? member.age.eq(ageCond) : null;
	}

	//광고 상태 isVaild, 날짜가 IN -> composition이 가능: isServiceable

	// private BooleanExpression allEq(String usernameCond, Integer ageCond) {
	// 	return isValid(usernameCond).and(DateBetweenIn(ageCond));
	// }

	private BooleanExpression allEq(String usernameCond, Integer ageCond) {
		return usernameEq(usernameCond).and(ageEq(ageCond));
	}




}

