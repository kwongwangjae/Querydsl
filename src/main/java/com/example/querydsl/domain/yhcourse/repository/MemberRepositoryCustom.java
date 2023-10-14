package com.example.querydsl.domain.yhcourse.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.querydsl.domain.yhcourse.dto.MemberSearchCondition;
import com.example.querydsl.domain.yhcourse.dto.MemberTeamDto;

public interface MemberRepositoryCustom{
	List<MemberTeamDto> search(MemberSearchCondition condition);

	Page<MemberTeamDto> searchPageSimple(MemberSearchCondition condition,
		Pageable pageable);
	Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition,
		Pageable pageable);
}
