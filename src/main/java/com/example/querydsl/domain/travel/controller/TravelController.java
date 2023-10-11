	package com.example.querydsl.domain.travel.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.querydsl.domain.travel.dto.request.TravelCreateRequestDto;
import com.example.querydsl.domain.travel.dto.response.TravelResponseDto;
import com.example.querydsl.domain.travel.dto.response.TravelSearchResponseDto;
import com.example.querydsl.domain.travel.repository.TravelRepository;
import com.example.querydsl.domain.travel.service.TravelService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TravelController {
	private final TravelService travelService;
	private final TravelRepository travelRepository;

	@PostMapping("/travel")
	private ResponseEntity<TravelResponseDto> createTravel(@RequestBody TravelCreateRequestDto travelCreateRequestDto){
		TravelResponseDto travelResponseDto = travelService.createTravel(travelCreateRequestDto);
		return ResponseEntity.ok(travelResponseDto);
	}

	@GetMapping("/search")
	public List<TravelResponseDto> searchTravel(TravelSearchResponseDto travelSearchResponseDto) {
		return travelRepository.search(travelSearchResponseDto);
	}

	// @GetMapping("/v1/members")
	// public List<MemberTeamDto> searchMemberV1(MemberSearchCondition condition)
	// {
	// return memberJpaRepository.search(condition);
	// }



}
