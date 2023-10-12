package com.example.querydsl.domain.yhcourse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.querydsl.domain.yhcourse.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
	List<Member> findByUsername(String username);
}
