package com.example.querydsl.domain.travel.dto.response;

import com.querydsl.core.annotations.QueryProjection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelResponseDto {
	private Long id;
	private String startLocation;
	private String arriveLocation;
	private String startDate;
	private String endDate;
	private Integer number;

	@QueryProjection
	public TravelResponseDto(Long id, String startLocation, String arriveLocation, String startDate, String endDate, Integer number){{
		this.id = id;
		this.startLocation = startLocation;
		this.arriveLocation = arriveLocation;
		this.startDate = startDate;
		this.endDate = endDate;
		this.number = number;
	}
	}

}
