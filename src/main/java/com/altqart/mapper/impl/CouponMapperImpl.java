package com.altqart.mapper.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.mapper.CouponMapper;
import com.altqart.mapper.UserMapper;
import com.altqart.model.Coupon;
import com.altqart.req.model.CouponReq;
import com.altqart.req.model.RespCoupon;
import com.altqart.resp.model.RespUser;

@Service
public class CouponMapperImpl implements CouponMapper {

	@Autowired
	private UserMapper userMapper;

	@Override
	public Coupon mapCoupon(CouponReq couponReq) {

		if (couponReq != null) {

			Coupon coupon = new Coupon();
			coupon.setAmount(couponReq.getAmount());
			coupon.setApplyAmount(couponReq.getApplyAmount());
			coupon.setApplyQty(couponReq.getApplyQty());
			coupon.setCode(couponReq.getCode());
			coupon.setExpireDate(couponReq.getExpireDate());
			coupon.setPercentage(couponReq.getPercentage());

			return coupon;
		}

		return null;
	}

	@Override
	public RespCoupon mapResCoupon(Coupon coupon) {

		if (coupon != null) {
			RespCoupon respCoupon = mapRespCouponOnly(coupon);

			if (respCoupon != null) {
				respCoupon.setUser(userMapper.mapRespUserOnly(coupon.getUser()));
			}

			return respCoupon;

		}
		return null;
	}

	public RespCoupon mapRespCouponOnly(Coupon coupon) {

		if (coupon != null) {
			RespCoupon respCoupon = new RespCoupon();
			respCoupon.setActive(coupon.isActive());
			respCoupon.setAmount(coupon.getAmount());
			respCoupon.setApplyAmount(coupon.getApplyAmount());
			respCoupon.setApplyQty(coupon.getApplyQty());
			respCoupon.setCode(coupon.getCode());
			respCoupon.setCount(coupon.getCount());
			respCoupon.setCreateDate(coupon.getCreateDate());
			respCoupon.setExpireDate(coupon.getExpireDate());
			respCoupon.setId(coupon.getPublicId());
			respCoupon.setPercentage(coupon.getPercentage());
			respCoupon.setValid(coupon.isValid());

			return respCoupon;
		}

		return null;
	}
}
