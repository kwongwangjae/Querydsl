package com.example.querydsl.domain.travel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.querydsl.domain.travel.entity.Travel;

@Repository
public interface TravelRepository extends JpaRepository<Travel,Long> {

}
