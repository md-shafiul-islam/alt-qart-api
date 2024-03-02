package com.altqart.mapper;

import java.util.List;

import com.altqart.model.Paid;
import com.altqart.model.Receive;
import com.altqart.req.model.PaidReq;
import com.altqart.req.model.ReceiveReq;
import com.altqart.resp.model.RespPaid;

public interface PaidMapper {

	public List<RespPaid> mapAllRespPayed(List<Paid> paids);

	public RespPaid mapRespPayed(Paid paid);

	public Paid mapPayed(PaidReq payed);

	public Receive mapReceive(ReceiveReq receive);

	public RespPaid mapRespPaid(Paid paid);

	public List<RespPaid> mapAllRespPaid(List<Paid> paids);

}
