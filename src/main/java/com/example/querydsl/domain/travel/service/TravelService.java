package com.example.querydsl.domain.travel.service;

import org.springframework.stereotype.Service;

import com.example.querydsl.domain.travel.dto.request.TravelCreateRequestDto;
import com.example.querydsl.domain.travel.dto.response.TravelResponseDto;
import com.example.querydsl.domain.travel.entity.Travel;
import com.example.querydsl.domain.travel.mapper.TravelMapper;
import com.example.querydsl.domain.travel.repository.TravelRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TravelService {
	private final TravelMapper travelMapper;
	private final TravelRepository travelRepository;
	public TravelResponseDto createTravel(TravelCreateRequestDto travelCreateRequestDto){
		Travel newTravel = travelMapper.toCreateTravelToEntity(travelCreateRequestDto);
		Travel savedTravel = travelRepository.save(newTravel);
		return travelMapper.toTravelToDto(savedTravel);
	}

}
