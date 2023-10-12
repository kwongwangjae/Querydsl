package com.example.querydsl.domain.travel.repository;

import static com.example.querydsl.domain.travel.entity.QTravel.*;
import static org.springframework.util.StringUtils.*;

import java.util.List;

import org.springframework.util.StringUtils;

import com.example.querydsl.domain.travel.dto.response.QTravelResponseDto;
import com.example.querydsl.domain.travel.dto.response.TravelResponseDto;
import com.example.querydsl.domain.travel.dto.response.TravelSearchResponseDto;
import com.example.querydsl.domain.travel.entity.QTravel;
import com.example.querydsl.domain.travel.entity.Travel;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class TravelRepositoryImpl implements TravelRepositoryCustom{

	private final JPAQueryFactory queryFactory;

	public TravelRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	@Override
	public List<TravelResponseDto> search(TravelSearchResponseDto travelSearchResponseDto) {

		BooleanBuilder builder = new BooleanBuilder();
		if (hasText(travelSearchResponseDto.getArriveLocation())) {
			builder.and(travel.arriveLocation.eq(travelSearchResponseDto.getArriveLocation()));
		}
		if (hasText(travelSearchResponseDto.getStartDate())) {
			builder.and(travel.startDate.eq(travelSearchResponseDto.getStartDate()));
		}
		if (hasText(travelSearchResponseDto.getEndDate())) {
			builder.and(travel.endDate.eq(travelSearchResponseDto.getEndDate()));
		}
		if (hasText(travelSearchResponseDto.getStartLocation())) {
			builder.and(travel.startLocation.eq(travelSearchResponseDto.getStartLocation()));
		}
		if (travelSearchResponseDto.getNumber() != null)  {
			builder.and(travel.number.goe(travelSearchResponseDto.getNumber()));
		}

		return queryFactory
			.select(new QTravelResponseDto(
				travel.id.as("travelId"),
				travel.arriveLocation,
				travel.startDate,
				travel.endDate,
				travel.startLocation,
				travel.number))
			.from(travel)
			.where(builder)
			.fetch();
	}

	// @Override
	// public List<TravelResponseDto> search(TravelSearchResponseDto travelSearchResponseDto){
	// 	return queryFactory
	// 		.select(new QTravelResponseDto(
	// 			travel.id.as("travelId"),
	// 			travel.arriveLocation,
	// 			travel.startDate,
	// 			travel.endDate,
	// 			travel.startLocation,
	// 			travel.number))
	// 		.from(travel)
	// 		.where(arriveLocationEq(travelSearchResponseDto.getArriveLocation()),
	// 			startDateEq(travelSearchResponseDto.getStartDate()),
	// 			endDateEq(travelSearchResponseDto.getEndDate()),
	// 			startLocationEq(travelSearchResponseDto.getStartLocation()),
	// 			numberEq(travelSearchResponseDto.getNumber()))
	// 		.fetch();
	// }
	//
	// private BooleanExpression arriveLocationEq(String arriveLocation) {
	// 	return hasText(arriveLocation)? travel.arriveLocation.eq(arriveLocation): null;
	// }
	//
	//
	// private BooleanExpression numberEq(Integer number) {
	// 	return number != null ? travel.number.goe(number) : null;
	// }
	//
	// private BooleanExpression startLocationEq(String startLocation) {  //BooleanExpression 조합을 위해서 변경
	// 	return hasText(startLocation)? travel.arriveLocation.eq(startLocation): null;
	// }
	//
	// private BooleanExpression endDateEq(String endDate) {
	// 	return hasText(endDate)? travel.arriveLocation.eq(endDate): null;
	// }
	//
	// private BooleanExpression startDateEq(String startDate) {
	// 	return hasText(startDate)? travel.arriveLocation.eq(startDate): null;
	// }

}
