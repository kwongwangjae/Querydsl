package com.example.querydsl.domain.travel.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Profile("local") //로컬환경에서만 돌아감
@Component
@RequiredArgsConstructor
public class initTravel {
}
