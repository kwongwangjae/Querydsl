package com.example.querydsl.domain.yhcourse.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MemberDto {
	private String username;
	private int age;

	@QueryProjection
	public MemberDto(String username, int age){  //dto도 Q타입 생성
		this.username = username;
		this.age = age;
	}

}
