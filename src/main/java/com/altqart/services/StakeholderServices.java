package com.altqart.services;

import java.util.List;
import java.util.Map;

import com.altqart.model.Paid;
import com.altqart.model.Receive;
import com.altqart.model.Stakeholder;
import com.altqart.model.StakeholderType;
import com.altqart.model.TempStakeholder;
import com.altqart.req.model.AddressReq;
import com.altqart.req.model.ApproveReq;
import com.altqart.req.model.EsActive;
import com.altqart.req.model.PaidReq;
import com.altqart.req.model.PaymentApproveReq;
import com.altqart.req.model.ReceiveReq;
import com.altqart.req.model.ReqDate;
import com.altqart.req.model.StakeholderReq;
import com.altqart.req.model.UserEmployeeReq;
import com.altqart.resp.model.RespEsStakeholder;
import com.altqart.resp.model.RespStakeholder;

public interface StakeholderServices {

	public boolean save(Stakeholder stakeHolder);

	public boolean updateStakeholder(StakeholderReq stakeHolder);

	public boolean approveUpdateStakeholder(Stakeholder stakeHolder);

	public void saveBySale(Stakeholder nStakeholder);

	public boolean createApproveUpdateStakeholder(Stakeholder stakeHolder, TempStakeholder tempStakeholder);

	public long getCount();

	public Stakeholder getStakeholderById(int id);

	public Stakeholder getStakeholderByPhoneNo(String phoneNo);

	public Stakeholder getStakeholderByName(String name);

	public Stakeholder getStakeholderPublicId(String id);

	public RespStakeholder getStakeholderPublicIdDetails(String id);

	public Stakeholder getStakeholderByGenId(String id);

	public RespStakeholder getStakeholderStatistics(String id);

	public RespStakeholder getCreditorStakeholder(String id);

	public boolean payCreditor(Stakeholder stakeHolder, PaidReq payed);

	public boolean reciveDebtor(Stakeholder stakeHolder, ReceiveReq receive);

	public List<RespStakeholder> getAllDebitorStakeholders();

	public List<RespStakeholder> getAllCreditorStakeholders();

	public List<RespStakeholder> getAllRejectedStakeholder(int start, int size);

	public List<RespStakeholder> getAllActiveStakeholder(int start, int size);

	public List<RespStakeholder> getAllStakeholderPending(int start, int size);

	public List<RespStakeholder> getAllStakeholderOnly();

	public boolean rejectStakeholder(Stakeholder stakeholder, String rejectNote);

	public boolean activeOrInActiveStakeholder(Stakeholder stakeholder, boolean status);

	public List<RespStakeholder> getRespAllDebitorStakeholders(int start, int size);

	public List<RespStakeholder> getRespAllCreditorStakeholders(int i, int j);

	public List<RespStakeholder> getPagingStakeholderByStakeType(String key, int start, int size);

	public boolean rejectUpdateStakeholder(Stakeholder stakeholder, String rejectNote);

	public boolean approveDeleteStakeholder(Stakeholder stakeholder);

	public boolean rejectDeleteStakeholder(Stakeholder stakeholder, String rejectNote);

	public RespStakeholder getRespStakeholderCreditorByPublicId(String id);

	public RespStakeholder getRespStakeholderDebtorByPublicId(String id);

	public boolean approvePayCreditor(Paid paid, PaymentApproveReq paymentApprove, Map<String, Object> map);

	public boolean approveReciveDebtor(Receive receive, PaymentApproveReq paymentApprove, Map<String, Object> map);

	public RespStakeholder getStakeholderCustomerByPublicId(String id);

	public List<RespStakeholder> getAllApproveStakeholder(int start, int size);

	public List<RespStakeholder> getPendingStakeholderByStakeTypeNotEqualKey(String key, int start, int size);

	public List<RespStakeholder> getPagingStakeholderByStakeTypeStatus(int status, String key, int start, int size);

	public List<RespEsStakeholder> getAllRespEsStakholder(String string, int status);

	public boolean rejectPayCreditor(Paid paid, PaymentApproveReq approveReq);

	public List<RespStakeholder> getCheckStakeholderByPhoneNo(String phoneNo);

	public RespStakeholder getDebitorOrCreditoStakeholderPublicId(String id);

	public boolean updateStakeholderToUserTypee(UserEmployeeReq userEmployee);

	public boolean isUpdateRequestedActive(String id);

	public RespStakeholder getDebitorOrCreditorDetails(String id);

	public RespStakeholder getRespStakeholderDetailsByPublicId(String id);

	public List<RespStakeholder> getAllRepsStakeholderUsingStatusType(int i, String type);

	public List<RespStakeholder> getAllRespStakeholderUsingStatus(int i);

	public boolean addTypeToStakeholder(StakeholderType stakeType, Stakeholder stakeholder);

	public List<RespStakeholder> getPagingStakeholderByStatus(int status, int start, int size);

	public List<RespStakeholder> getAllRespStakeholderUpdatePending();

	public List<RespStakeholder> getPagingStakeholderUpdatePending(int start, int size);

	public RespStakeholder getUpdatePendingStakeholderByID(String id);

	public TempStakeholder getActiveTempStakeholderByStakeholder(String id);

	public RespStakeholder getStakeholderByPublicIdOnly(String stakeholder);

	public void getStakeholderStatementByPublicId(String id, Map<String, Object> map);

	public void getStakeholderStatementDateByPublicId(String id, Map<String, Object> map, ReqDate reqDate);

	public void activeOrInActiveStakeholderById(EsActive esActive, Map<String, Object> map);

	public boolean removeTypeToStakeholder(StakeholderType stakeholderType, Stakeholder stakeholder);

	public void getStakeholderAllSaleItems(String id, Map<String, Object> map);

	public void getStakeholderAllAccount(String id, Map<String, Object> map);

	public void getStakeholderAllPaidMethods(String id, Map<String, Object> map);

	public Stakeholder getStakeholderByPublicId(String id);

	public void getAllStakeholderAddress(Map<String, Object> map);

	public void getAddStakeholderAddress(Map<String, Object> map, AddressReq addressReq);

	public void updateStakeholderAddress(Map<String, Object> map, AddressReq addressReq);

}
