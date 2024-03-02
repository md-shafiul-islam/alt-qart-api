package com.altqart.mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.altqart.model.BankAccount;
import com.altqart.model.Order;
import com.altqart.model.Stakeholder;
import com.altqart.model.StakeholderType;
import com.altqart.model.TempStakeholder;
import com.altqart.model.User;
import com.altqart.req.model.ReqDate;
import com.altqart.req.model.StakeholderReq;
import com.altqart.resp.model.RespEsStakeholder;
import com.altqart.resp.model.RespStakeholder;
import com.altqart.resp.model.RespStakeholderType;

public interface StakeholderMapper {

	public List<RespStakeholder> mapAllRespStakeholder(List<Stakeholder> stakeholders);

	public RespStakeholder mapRespStakeholder(Stakeholder stakeholder);

	public Stakeholder mapStakeholder(StakeholderReq stakeholderReq);

	public RespStakeholder mapStakeholderStatistics(Stakeholder stakeholder);

	public RespStakeholder mapEsStakeholder(Stakeholder stakeholder);

	public RespStakeholder mapStakeholderDetails(Stakeholder stakeholder);

	public TempStakeholder mapTempStakeholder(StakeholderReq supplier);

	public List<RespStakeholder> mapAllRespDebitorOrCreditorStakeholder(List<Stakeholder> stakeholders);

	public List<RespEsStakeholder> mapAllEsStakeholder(List<Stakeholder> stakeholders);

	public RespStakeholder mapRespStakeholderDebitorOrCreditor(Stakeholder data);

	public RespEsStakeholder mapRepEsStakeholder(Stakeholder stakeholder);

	public RespStakeholder mapRespDebitorOrCreditorStakeholderDetails(Stakeholder stakeholder);

	public RespStakeholder mapCustomerStakeholderDetails(Stakeholder stakeholder, RespStakeholder respStakeholder);

	public User mapStakeholderToUser(Stakeholder stakeholder);

	public RespStakeholder mapRespStakeholderOnly(Stakeholder stakeholder);

	public List<RespStakeholderType> mapAllRespStakeholderType(List<StakeholderType> stakeholders);

	public void createAccountStatement(Stakeholder stakeholder, Map<String, Object> map);

	public void createAccountStatementBetweenDate(ReqDate reqDate, Stakeholder stakeholder, Map<String, Object> map);

	public Stakeholder mapStakeholderUpdateOrCreate(StakeholderReq stakeholder);

	public List<StakeholderType> mapSetToListAllStakeholderType(Set<StakeholderType> stakeholderTypes);

	public void mapAllSaleItems(List<Order> orders, Map<String, Object> map);

	public void mapAllAccount(List<BankAccount> bankAccounts, Map<String, Object> map);

	public void mapAllAccountForOption(List<BankAccount> bankAccounts, Map<String, Object> map);

	public RespStakeholder mapRespStakeholderDetails(Stakeholder stakeholder);

}
