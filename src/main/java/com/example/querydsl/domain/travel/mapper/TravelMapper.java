package com.example.querydsl.domain.travel.mapper;

import org.springframework.stereotype.Component;

import com.example.querydsl.domain.travel.dto.request.TravelCreateRequestDto;
import com.example.querydsl.domain.travel.dto.response.TravelResponseDto;
import com.example.querydsl.domain.travel.dto.response.TravelSearchResponseDto;
import com.example.querydsl.domain.travel.entity.Travel;

@Component
public class TravelMapper {
	public Travel toCreateTravelToEntity(TravelCreateRequestDto travelCreateRequestDto){
		return Travel.builder()
			.arriveLocation(travelCreateRequestDto.getArriveLocation())
			.endDate(travelCreateRequestDto.getEndDate())
			.number(travelCreateRequestDto.getNumber())
			.startDate(travelCreateRequestDto.getStartDate())
			.startLocation(travelCreateRequestDto.getStartLocation())
			.build();
	}

	public TravelResponseDto toTravelToDto(Travel travel){
		return TravelResponseDto.builder()
			.arriveLocation(travel.getArriveLocation())
			.endDate(travel.getEndDate())
			.number(travel.getNumber())
			.startDate(travel.getStartDate())
			.startLocation(travel.getStartLocation())
			.build();
	}

	public TravelSearchResponseDto toSearchTravelToDto(Travel travel){
		return TravelSearchResponseDto.builder()
			.arriveLocation(travel.getArriveLocation())
			.endDate(travel.getEndDate())
			.number(travel.getNumber())
			.startDate(travel.getStartDate())
			.startLocation(travel.getStartLocation())
			.build();
	}
}
