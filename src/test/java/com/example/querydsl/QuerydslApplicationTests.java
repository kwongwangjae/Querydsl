package com.example.querydsl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import com.example.querydsl.domain.yhcourse.entity.Hello;
import com.example.querydsl.entity.QHello;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@Commit
class QuerydslApplicationTests {

	@Autowired
	EntityManager em;

	@Test
	void contextLoads() {
		Hello hello = new Hello();
		em.persist(hello);

		JPAQueryFactory query = new JPAQueryFactory(em);
		QHello qHello = QHello.hello; //Querydsl Q타입 동작 확인
		Hello result = query
			.selectFrom(qHello)
			.fetchOne();
		Assertions.assertThat(result).isEqualTo(hello);
		//Assertions: 테스트 결과를 검증 assertThat(result),.isEqualTo(hello)-> 결과비교
		//lombok 동작 확인 (hello.getId())
		Assertions.assertThat(result.getId()).isEqualTo(hello.getId());
	}

}
