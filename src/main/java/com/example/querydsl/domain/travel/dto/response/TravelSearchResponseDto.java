package com.example.querydsl.domain.travel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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
