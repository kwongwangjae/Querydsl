package com.example.querydsl.domain.travel.repository;

import java.util.List;

import com.example.querydsl.domain.travel.dto.response.TravelResponseDto;
import com.example.querydsl.domain.travel.dto.response.TravelSearchResponseDto;

public interface TravelRepositoryCustom {
	List<TravelResponseDto> search(TravelSearchResponseDto travelSearchResponseDto);

}
