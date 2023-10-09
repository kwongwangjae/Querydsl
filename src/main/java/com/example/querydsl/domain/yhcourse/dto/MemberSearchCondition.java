package com.example.querydsl.domain.yhcourse.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class MemberSearchCondition {
	//회원명, 팀명, 나이(ageGoe, ageLoe)
	private String username;
	private String teamName;
	private Integer ageGoe;
	private Integer ageLoe;
}
