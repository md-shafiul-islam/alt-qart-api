package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperConverterServices;
import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.StatisticsMapper;
import com.altqart.model.DailyStatistics;
import com.altqart.model.Store;
import com.altqart.resp.model.RespDailyStatistics;
import com.altqart.services.OrderServices;
import com.altqart.services.StockServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StatisticsMapperImpl implements StatisticsMapper {

	@Autowired
	private OrderServices orderServices;

	@Autowired
	private StockServices stockServices;

	@Autowired
	private HelperServices helperServices;

	@Autowired
	private HelperConverterServices converterServices;

	@Override
	public RespDailyStatistics mapDailyStatistics(DailyStatistics statistics) {

		if (statistics != null) {
			RespDailyStatistics dailyStatistics = new RespDailyStatistics();

			return dailyStatistics;
		}

		return null;
	}

	@Override
	public DailyStatistics createDailyStatistics(Store store, Date date, DailyStatistics lastStatisticsItem) {

		DailyStatistics dailyStatistics = new DailyStatistics();
		if (date == null) {
			date = new Date();
		}

		return dailyStatistics;
	}

	@Override
	public void mapReCalculateDailyStatistic(DailyStatistics prevDayStatistic, DailyStatistics dailyStatistic) {

		if (dailyStatistic != null) {
			double cashCredit = 0, cashDebit = 0, cashCreditBalance = 0, cashDebitBalance = 0, closeingBalance = 0,
					openingBalance = 0, totalAssetDebit = 0, totalAssetCredit = 0;

		}

	}

	@Override
	public List<RespDailyStatistics> mapAllDailyStatistics(List<DailyStatistics> statistics) {

		if (statistics != null) {

			List<RespDailyStatistics> dailyStatistics = new ArrayList<>();

			for (DailyStatistics statistic : statistics) {
				RespDailyStatistics dailyStatistic = mapDailyStatistics(statistic);

				if (dailyStatistic != null) {
					dailyStatistics.add(dailyStatistic);
				}
			}

			return dailyStatistics;
		}
		return null;
	}

	// Public End

}
