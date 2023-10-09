package com.example.querydsl.domain.travel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.querydsl.domain.travel.dto.request.TravelCreateRequestDto;
import com.example.querydsl.domain.travel.dto.response.TravelResponseDto;
import com.example.querydsl.domain.travel.service.TravelService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TravelController {
	private final TravelService travelService;

	@PostMapping("/travel")
	private ResponseEntity<TravelResponseDto> createTravel(@RequestBody TravelCreateRequestDto travelCreateRequestDto){
		TravelResponseDto travelResponseDto = travelService.createTravel(travelCreateRequestDto);
		return ResponseEntity.ok(travelResponseDto);
	}

}
