package com.altqart.services;

import java.util.Map;

import com.altqart.model.Coupon;
import com.altqart.req.model.CouponActionReq;
import com.altqart.req.model.CouponReq;

public interface CouponServices {

	public Coupon getCouponByCode(String code);

	public void action(CouponActionReq actionReq, Map<String, Object> map);

	public void add(CouponReq couponReq, Map<String, Object> map);

	public void getOne(String id, Map<String, Object> map);

	public void getAllCouponByType(int start, int size, int type, Map<String, Object> map);

	public void getRespCouponByCode(String code, Map<String, Object> map);

}
