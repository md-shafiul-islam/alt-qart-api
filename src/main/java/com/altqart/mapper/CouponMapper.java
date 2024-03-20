package com.altqart.mapper;

import com.altqart.model.Coupon;
import com.altqart.req.model.CouponReq;
import com.altqart.req.model.RespCoupon;

public interface CouponMapper {

	public Coupon mapCoupon(CouponReq couponReq);

	public RespCoupon mapResCoupon(Coupon coupon);

}
