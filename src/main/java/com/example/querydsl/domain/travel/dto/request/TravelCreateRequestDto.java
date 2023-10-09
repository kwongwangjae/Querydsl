package com.example.querydsl.domain.travel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class TravelCreateRequestDto {
	private String startLocation;
	private String arriveLocation;
	private String startDate;
	private String endDate;
	private Integer number;
}
