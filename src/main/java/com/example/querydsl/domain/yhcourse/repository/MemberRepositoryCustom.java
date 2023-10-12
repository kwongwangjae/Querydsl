package com.example.querydsl.domain.yhcourse.repository;

import java.util.List;

import com.example.querydsl.domain.yhcourse.dto.MemberSearchCondition;
import com.example.querydsl.domain.yhcourse.dto.MemberTeamDto;

public interface MemberRepositoryCustom{
	List<MemberTeamDto> search(MemberSearchCondition condition);
}
