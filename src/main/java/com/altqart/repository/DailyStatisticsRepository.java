package com.altqart.repository;

import org.springframework.data.repository.CrudRepository;

import com.altqart.model.DailyStatistics;

public interface DailyStatisticsRepository extends CrudRepository<DailyStatistics, Integer> {

}
