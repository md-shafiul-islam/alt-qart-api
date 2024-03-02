package com.altqart.services;

import java.util.List;

import com.altqart.req.model.RespBankWithdraw;

public interface WithDrawServices {

	public List<RespBankWithdraw> getAllWithdraw();

	public long getCount();

	public List<RespBankWithdraw> getAllPendingWithdraw(int strat, int size);

	public List<RespBankWithdraw> getAllWithdrawByStatus(int status, int strat, int size);

	public RespBankWithdraw getWithdarwalByPublicId(String id);

}
