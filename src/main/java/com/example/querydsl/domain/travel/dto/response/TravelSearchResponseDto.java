package com.example.querydsl.domain.travel.dto.response;

import com.querydsl.core.annotations.QueryProjection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelSearchResponseDto {
	private String startLocation;
	private String arriveLocation;
	private String startDate;
	private String endDate;
	private Integer number;
}
