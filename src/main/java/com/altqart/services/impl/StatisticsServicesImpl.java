package com.altqart.services.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperAuthenticationServices;
import com.altqart.helper.services.HelperDateServices;
import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.StatisticsMapper;
import com.altqart.model.DailyStatistics;
import com.altqart.model.Store;
import com.altqart.model.User;
import com.altqart.repository.DailyStatisticsRepository;
import com.altqart.resp.model.RespDailyStatistics;
import com.altqart.services.StatisticsServices;
import com.altqart.services.StoreServices;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StatisticsServicesImpl implements StatisticsServices {

	@Autowired
	private StoreServices storeServices;

	@Autowired
	private StatisticsMapper statisticsMapper;

	@Autowired
	private HelperServices helperServices;

	@Autowired
	private HelperDateServices dateServices;

	@Autowired
	private HelperAuthenticationServices authenticationServices;

	private SessionFactory sessionFactory;

	@Autowired
	private DailyStatisticsRepository dailyStatisticsRepository;

	@Autowired
	public void getSession(EntityManagerFactory factory) {

		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}

		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}

	@Override
	public List<RespDailyStatistics> getDailyStatisticsByStore(String id) {

		List<RespDailyStatistics> dailyStatistics = null;
		Store store = null;

		if (!helperServices.isValidAndLenghtCheck(id, 30)) {
			store = storeServices.getStoreById(id);
		}

		if (store == null) {
			store = storeServices.getDefaultStore();
		}

		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<DailyStatistics> query = builder.createQuery(DailyStatistics.class);

			Root<DailyStatistics> root = query.from(DailyStatistics.class);

			query.where(builder.equal(root.get("business").get("id"), store.getId()));
			query.orderBy(builder.desc(root.get("id")));
			TypedQuery<DailyStatistics> typedQuery = session.createQuery(query);

			List<DailyStatistics> statistics = typedQuery.getResultList();

			dailyStatistics = statisticsMapper.mapAllDailyStatistics(statistics);

		} catch (Exception e) {
			System.out.println("Run Action Catch Session Order Group!!");

			e.printStackTrace();
		}

		session.clear();
		session.close();
		return dailyStatistics;
	}

	@Override
	public Map<String, Object> getCalculateDailyStatisticsByStore(Map<String, Object> map, String id) {

		DailyStatistics dailyStatistics = null;
		Store store = null;
		Date date = new Date();

		if (!helperServices.isValidAndLenghtCheck(id, 30)) {
			store = storeServices.getStoreById(id);
		}

		if (store == null) {
			store = storeServices.getDefaultStore();
		}

		if (store != null) {

			dailyStatistics = getThisDayDailyStatisticsByBusiness(id);

			if (dailyStatistics == null) {
				DailyStatistics lastStatistic = getLastAddedDailyStatistics();

				if (lastStatistic != null) {
					if (dateServices.isToDay(lastStatistic.getDateGroup())) {
						dailyStatistics = lastStatistic;
					}

				}
			}

			if (dailyStatistics != null) {
				log.info("Re-Calculate Statistics :) ");
				reCalculateDailyStatistics(dailyStatistics, map);
			} else {
				DailyStatistics lastStatistics = getLastAddedDailyStatistics();

				if (lastStatistics != null) {

					log.info("Last Statistics Found :) ");

					if (dateServices.isPrevDay(lastStatistics.getDateGroup())) {
						log.info("Last Statistics Is Previousday :) ");
						createAndSaveThisDayStatistics(store, date, lastStatistics, map);
					} else {
						startCalculatingLastCalculateDate(map, dateServices.getNextDate(lastStatistics.getDateGroup()),
								lastStatistics);
						log.info("Last Statistics Not Previousday :) ");
					}

				} else {
					log.info("Last Statistics Not Found :) ");
					startCalculatingLastCalculateDate(map, null, null);
				}

			}

		}

		return map;
	}

	@Override
	public void getDailyStatisticsById(Map<String, Object> map, String id) {

		RespDailyStatistics dailyStatistic = null;
		boolean status = false;
		Session session = sessionFactory.openSession();

		try {

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<DailyStatistics> query = builder.createQuery(DailyStatistics.class);

			Root<DailyStatistics> root = query.from(DailyStatistics.class);

			query.where(builder.equal(root.get("publicId"), id));
			query.orderBy(builder.desc(root.get("id")));
			TypedQuery<DailyStatistics> typedQuery = session.createQuery(query);

			DailyStatistics statistic = typedQuery.getSingleResult();

			if (statistic != null) {
				dailyStatistic = statisticsMapper.mapDailyStatistics(statistic);
			} else {
				throw new Exception("Daily Statistic not found by id");
			}
			map.put("message", "Statistic found by ID");
			status = true;
		} catch (Exception e) {
			System.out.println("Run Action Catch Session Order Group!!");

			e.printStackTrace();
			map.put("message", e.getMessage());
		}

		session.clear();
		session.close();
		map.put("status", status);
		map.put("response", dailyStatistic);

	}

	@Override
	public DailyStatistics getLastAddedDailyStatistics() {

		long lastId = dailyStatisticsRepository.count();
		if (lastId > 0) {

			Optional<DailyStatistics> optional = dailyStatisticsRepository.findById((int) lastId);

			if (optional.isPresent() && !optional.isEmpty()) {
				return optional.get();
			}
		}

		return null;
	}

	@Override
	public void startCalculatingLastCalculateDate(Map<String, Object> map, Date startDate,
			DailyStatistics lastStatistic) {

		log.info("Calculating Statistics Start Last Calculated Date ...  " + startDate);

		map.put("status", false);
		map.put("response", null);
		map.put("message", "Business Statistics Create Failed !!");

		List<DailyStatistics> dailyStatistics = null;

		if (lastStatistic != null) {

			log.info("SCLCD: Statistics Found Date " + lastStatistic.getDateGroup());

			dailyStatistics = getStartCalculatingDailyStatisticsByDate(
					dateServices.getNextDate(lastStatistic.getDateGroup()), null, lastStatistic);

		} else {
			log.info("SCLCD: Statistics Not Found");

			if (startDate != null) {
				log.info("SCLCD: Statistics Found Start Date ... " + startDate);
				dailyStatistics = getStartCalculatingDailyStatisticsByDate(startDate, null, null);

			} else {
				log.info("SCLCD: Statistics Found Start Cash account date ... ");

			}

		}

		boolean status = saveAllDailyStatistics(dailyStatistics);
		map.put("response", dailyStatistics);
		if (dailyStatistics != null && status) {
			if (dailyStatistics.size() > 0) {

				map.put("status", true);
				map.put("response", statisticsMapper.mapAllDailyStatistics(dailyStatistics));
				map.put("message", "Business Statistics Created");
			}
		}

	}

	// Public End

	private void reCalculateDailyStatistics(DailyStatistics dailyStatistics, Map<String, Object> map) {

		map.put("status", false);
		map.put("response", null);
		map.put("message", "Business statistics Re-Calculating failed");

		if (dailyStatistics != null) {

			Session session = sessionFactory.openSession();
			Transaction transaction = null;

			try {
				transaction = session.beginTransaction();
				int prevId = dailyStatistics.getId() - 1;

				DailyStatistics dbPrevDayStatistic = null;
				DailyStatistics dbDailyStatistic = session.get(DailyStatistics.class, dailyStatistics.getId());

				if (dbDailyStatistic == null) {
					throw new Exception("Re-Calculate Daily Statistic not found");
				}

				if (prevId > 0) {
					dbPrevDayStatistic = session.get(DailyStatistics.class, prevId);
				}

				if (dbPrevDayStatistic != null) {
					if (dateServices.isPrevDay(dbPrevDayStatistic.getDateGroup())) {
						statisticsMapper.mapReCalculateDailyStatistic(dbPrevDayStatistic, dbDailyStatistic);
					} else {
						statisticsMapper.mapReCalculateDailyStatistic(null, dbDailyStatistic);
					}
				} else {
					if (dbDailyStatistic != null) {
						statisticsMapper.mapReCalculateDailyStatistic(null, dbDailyStatistic);
					}
				}

				session.update(dbDailyStatistic);
				transaction.commit();
				map.put("status", true);
				map.put("response", statisticsMapper.mapDailyStatistics(dbDailyStatistic));
				map.put("message", "Business statistics Re-Calculated :)");

			} catch (Exception e) {

				if (transaction != null) {
					transaction.rollback();
				}
				map.put("status", false);
				map.put("response", null);
				map.put("message", "Business statistics Re-Calculated failed");
				e.printStackTrace();
			}
		}

	}

	private void createAndSaveThisDayStatistics(Store store, Date date, DailyStatistics lastStatistics,
			Map<String, Object> map) {

		map.put("status", false);
		map.put("response", null);
		map.put("message", "Today Statistics Calculated faild !!");

		DailyStatistics statistics = statisticsMapper.createDailyStatistics(store, date, lastStatistics);
		statistics.setPublicId(helperServices.getGenPublicId());
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			session.save(statistics);

			transaction.commit();

			if (statistics.getId() > 0) {
				DailyStatistics statistic = session.get(DailyStatistics.class, statistics.getId());

				map.put("status", true);
				map.put("response", statisticsMapper.mapDailyStatistics(statistic));
				map.put("message", "Today Statistics Calculate and save");
			}

		} catch (Exception e) {

			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		session.clear();
		session.close();
	}

	private boolean saveAllDailyStatistics(List<DailyStatistics> dailyStatistics) {

		boolean status = false;
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			if (dailyStatistics != null) {
				for (DailyStatistics statistic : dailyStatistics) {
					statistic.setPublicId(helperServices.getGenPublicId());
					statistic.setFinalize(true);
					session.persist(statistic);
				}
			}

			transaction.commit();
			status = true;
		} catch (Exception e) {

			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			status = false;
		}
		session.clear();
		session.close();
		return status;
	}

	private List<DailyStatistics> getStartCalculatingDailyStatisticsByDate(Date startDate, String bId,
			DailyStatistics lastStatistic) {

		List<DailyStatistics> dailyStatistics = new ArrayList<>();

		if (startDate != null) {

			log.info("Start Mapping Statistics " + startDate);
			Date currentDate = new Date();
			Date calculatingDate = startDate;
			DailyStatistics lastItem = lastStatistic;
			Store store = storeServices.getStoreById(bId);
			User user = authenticationServices.getCurrentUser();
			if (store == null && user != null) {

				if (user.getStore() != null) {
					store = authenticationServices.getCurrentUser().getStore();
				}

			}

			while (!dateServices.isGreater(currentDate, calculatingDate)) {

				DailyStatistics statistics = statisticsMapper.createDailyStatistics(store, calculatingDate, lastItem);

				dailyStatistics.add(statistics);

				lastItem = statistics;
				calculatingDate = dateServices.getNextDate(calculatingDate);

				log.info("Next Calculating Date: " + calculatingDate);

			}

		}

		return dailyStatistics;
	}

	private DailyStatistics getThisDayDailyStatisticsByBusiness(String id) {

		DailyStatistics dailyStatistics = null;
		Store store = null;

		if (!helperServices.isValidAndLenghtCheck(id, 30)) {
			store = storeServices.getStoreById(id);
		}

		if (store == null) {
			store = storeServices.getDefaultStore();
		}

		Session session = sessionFactory.openSession();
		try {
			Calendar calender = dateServices.getCalenderByDate(new Date());

			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<DailyStatistics> criteriaQuery = criteriaBuilder.createQuery(DailyStatistics.class);

			Root<DailyStatistics> root = criteriaQuery.from(DailyStatistics.class);

			criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(root.get("dateGroup"), calender.getTime()),
					criteriaBuilder.equal(root.get("store").get("publicId"), store.getPublicId())));

			Query<DailyStatistics> typedQuery = session.createQuery(criteriaQuery);

			dailyStatistics = typedQuery.getSingleResult();

		} catch (Exception e) {

			log.info("Get Statistics By business ID " + e.getMessage());
//			e.printStackTrace();
		}
		session.clear();
		session.close();

		return dailyStatistics;
	}

}
