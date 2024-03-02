package com.altqart.services.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.mapper.ReportMapper;
import com.altqart.model.OrderItem;
import com.altqart.resp.model.RespOrderItem;
import com.altqart.services.ReportsServices;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReportsServicesImpl implements ReportsServices {

	private SessionFactory sessionFactory;

	private CreateDatePack datePack = new CreateDatePack();

	@Autowired
	private ReportMapper reportMapper;

	@Autowired
	public void getSession(EntityManagerFactory factory) {

		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}

		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}

	@Override
	public List<RespOrderItem> getCurrentDayOrderItems() {
		return getAllDataByCurrentDateByGrupeColName("groupDate");
	}


	@SuppressWarnings("unchecked")
	public List<RespOrderItem> getAllDataByCurrentDateByGrupeColName(String dateCol) {

		List<RespOrderItem> items = null;

		Session session = sessionFactory.openSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<OrderItem> criteriaQuery = (criteriaBuilder.createQuery(OrderItem.class));
		Root<OrderItem> root = criteriaQuery.from(OrderItem.class);
		criteriaQuery.select(root);

		// DOTO: Date Modification

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		int daySt = day;

		if (day == 1) {

			int month = calendar.get(Calendar.MONTH);
			calendar.set(Calendar.MONTH, month - 1);

			int totalDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			calendar.set(Calendar.DAY_OF_MONTH, totalDayOfMonth);
			System.out.println("Prev Month Day: " + totalDayOfMonth);

		} else {

			calendar.set(Calendar.DAY_OF_MONTH, day - 1);
			System.out.println("Day OF Month: " + calendar.get(Calendar.DAY_OF_MONTH));
		}

		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		TimeZone timeZone = TimeZone.getDefault();
		calendar.setTimeZone(timeZone);
		Date sDate = calendar.getTime();

		if (daySt == 1) {

			int month = calendar.get(Calendar.MONTH);
			calendar.set(Calendar.MONTH, month + 1);

			int totalDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			System.out.println("Prev Month Day: " + totalDayOfMonth);

		} else {

			System.out.println("Date S: " + dateCol);
			calendar.set(Calendar.DAY_OF_MONTH, day);

		}

		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);

		Date eDate = calendar.getTime();

		criteriaQuery.where(criteriaBuilder.between(root.get(dateCol), sDate, eDate));

		// criteriaQuery.groupBy(root.get(colName));

		Query<OrderItem> query = session.createQuery(criteriaQuery);

		List<OrderItem> list = query.getResultList();

		if (list != null) {
			items = reportMapper.mapAllOrderItem(list);
		}

		session.clear();
		return items;
	}

}
