package com.altqart.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.altqart.model.DailyStatistics;
import com.altqart.resp.model.RespDailyStatistics;

public interface StatisticsServices {

	public DailyStatistics getLastAddedDailyStatistics();

	public List<RespDailyStatistics> getDailyStatisticsByStore(String id);

	public Map<String, Object> getCalculateDailyStatisticsByStore(Map<String, Object> map, String id);

	public void startCalculatingLastCalculateDate(Map<String, Object> map, Date date, DailyStatistics lastStatistic);

	public void getDailyStatisticsById(Map<String, Object> map, String id);

}
