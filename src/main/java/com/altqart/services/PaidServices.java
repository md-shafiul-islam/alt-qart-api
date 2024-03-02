package com.altqart.services;

import java.util.List;
import java.util.Map;

import com.altqart.model.Paid;
import com.altqart.resp.model.RespPaid;

public interface PaidServices {

	public Paid getPaidById(String id);

	public RespPaid getPaidByPublicId(String id);

	public List<RespPaid> getAllRejectedPayed(int start, int size);

	public List<RespPaid> getAllPendingPayed(int start, int size);

	public List<RespPaid> getAllApprovePayed(int start, int size);

	public List<Paid> getAllPaid();

	public void getPaidInvoiceByPublicId(String id, Map<String, Object> map);

}
