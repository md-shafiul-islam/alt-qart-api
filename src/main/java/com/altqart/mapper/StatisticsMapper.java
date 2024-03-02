package com.altqart.mapper;

import java.util.Date;
import java.util.List;

import com.altqart.model.DailyStatistics;
import com.altqart.model.Store;
import com.altqart.resp.model.RespDailyStatistics;

public interface StatisticsMapper {

	public DailyStatistics createDailyStatistics(Store store, Date date, DailyStatistics lastStatisticsItem);

	public RespDailyStatistics mapDailyStatistics(DailyStatistics statistics);

	public List<RespDailyStatistics> mapAllDailyStatistics(List<DailyStatistics> statistics);

	public void mapReCalculateDailyStatistic(DailyStatistics prevDayStatistic, DailyStatistics dailyStatistic);

}
