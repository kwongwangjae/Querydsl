package com.example.querydsl.domain.travel.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Travel {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;
	private String startLocation;
	private String arriveLocation;
	private String startDate;
	private String endDate;
	private Integer number;

	@Builder
	public Travel(String startLocation, String arriveLocation, String startDate, String endDate, Integer number){
		this.startLocation = startLocation;
		this.arriveLocation = arriveLocation;
		this.startDate = startDate;
		this.endDate = endDate;
		this.number = number;
	}


}
