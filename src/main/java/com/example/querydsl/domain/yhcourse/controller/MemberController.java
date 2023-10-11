package com.example.querydsl.domain.yhcourse.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.querydsl.domain.yhcourse.dto.MemberSearchCondition;
import com.example.querydsl.domain.yhcourse.dto.MemberTeamDto;
import com.example.querydsl.domain.yhcourse.repository.MemberJpaRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberController {
	private final MemberJpaRepository memberJpaRepository;
	@GetMapping("/v1/members")
	public List<MemberTeamDto> searchMemberV1(MemberSearchCondition condition)
	{
	return memberJpaRepository.search(condition);
	}
}
