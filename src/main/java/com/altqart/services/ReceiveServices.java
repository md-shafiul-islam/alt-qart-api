package com.altqart.services;

import java.util.List;
import java.util.Map;

import com.altqart.model.Receive;
import com.altqart.resp.model.RespReceive;

public interface ReceiveServices {

	public Receive getReceiveById(String id);

	public RespReceive getReceiveByPublicId(String id);

	public List<RespReceive> getAllRejected(int start, int size);

	public List<RespReceive> getAllPending(int start, int size);

	public List<RespReceive> getAllApprove(int start, int size);

	public void getReceiveInvoiceByPublicId(String id, Map<String, Object> map);

}
