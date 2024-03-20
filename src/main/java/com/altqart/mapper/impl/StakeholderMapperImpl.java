package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperConverterServices;
import com.altqart.helper.services.HelperDateServices;
import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.BankAccountMapper;
import com.altqart.mapper.EsCreditMapper;
import com.altqart.mapper.EsDebitMapper;
import com.altqart.mapper.SaleMapper;
import com.altqart.mapper.StakeholderMapper;
import com.altqart.mapper.StoreMapper;
import com.altqart.model.BankAccount;
import com.altqart.model.EsCreditDetail;
import com.altqart.model.EsDebitDetail;
import com.altqart.model.Order;
import com.altqart.model.OrderItem;
import com.altqart.model.SaleItem;
import com.altqart.model.Stakeholder;
import com.altqart.model.StakeholderType;
import com.altqart.model.TempStakeholder;
import com.altqart.model.User;
import com.altqart.req.model.ReqDate;
import com.altqart.req.model.StakeholderReq;
import com.altqart.resp.model.RespAccount;
import com.altqart.resp.model.RespBankAccount;
import com.altqart.resp.model.RespEsCredit;
import com.altqart.resp.model.RespEsCreditDetail;
import com.altqart.resp.model.RespEsDebit;
import com.altqart.resp.model.RespEsStakeholder;
import com.altqart.resp.model.RespStakeholder;
import com.altqart.resp.model.RespStakeholderType;
import com.altqart.resp.model.StakeholderStatement;
import com.altqart.resp.model.StatementInf;
import com.altqart.services.StakeholderServices;
import com.altqart.services.StakeholderTypeServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StakeholderMapperImpl implements StakeholderMapper {

	@Autowired
	private StakeholderTypeServices stakeholderTypeServices;

	@Autowired
	private StakeholderServices stakeholderServices;

	@Autowired
	private HelperServices helperServices;

	@Autowired
	private EsCreditMapper esCreditMapper;

	@Autowired
	private EsDebitMapper esDebitMapper;

	@Autowired
	private SaleMapper saleMapper;

	@Autowired
	private HelperConverterServices converterServices;

	@Autowired
	private BankAccountMapper bankAccountMapper;

	@Autowired
	private HelperDateServices dateServices;

	@Autowired
	private StoreMapper storeMapper;

	@Override
	public List<RespStakeholder> mapAllRespStakeholder(List<Stakeholder> stakeholders) {

		if (stakeholders != null) {
			List<RespStakeholder> respStakeholders = new ArrayList<>();

			for (Stakeholder stakeholder : stakeholders) {
				RespStakeholder respStakeholder = mapRespStakeholder(stakeholder);

				if (respStakeholder != null) {
					respStakeholders.add(respStakeholder);
				}
			}

			return respStakeholders;
		}

		return null;

	}

	@Override
	public RespStakeholder mapRespStakeholderOnly(Stakeholder stakeholder) {

		if (stakeholder != null) {
			RespStakeholder respStakeholder = new RespStakeholder();
			respStakeholder.setDate(stakeholder.getDate());
			respStakeholder.setDescription(stakeholder.getDescription());
			respStakeholder.setEmail(stakeholder.getEmail());
			respStakeholder.setId(stakeholder.getPublicId());
			respStakeholder.setName(stakeholder.getName());
			respStakeholder.setPhoneNo(stakeholder.getPhoneNo());

			respStakeholder.setActive(stakeholder.isActive());
			respStakeholder.setApprove(stakeholder.getApprove());
			respStakeholder.setGenId(stakeholder.getGenId());
			return respStakeholder;
		}

		return null;
	}

	@Override
	public RespStakeholder mapRespStakeholder(Stakeholder stakeholder) {

		if (stakeholder != null) {

			RespStakeholder respStakeholder = mapRespStakeholderOnly(stakeholder);

			if (respStakeholder != null) {
				if (stakeholder.getTotalCreditAmount() != null) {
					respStakeholder.setCreditAmount(stakeholder.getTotalCreditAmount().doubleValue());
				}

				if (stakeholder.getTotalDebitAmount() != null) {
					respStakeholder.setDebitAmount(stakeholder.getTotalDebitAmount().doubleValue());
				}

				respStakeholder.setRespTypes(mapAllStakeholderType(stakeholder.getStakeholderTypes()));

				calculateDebitOrCreditBalance(stakeholder, respStakeholder);
				return respStakeholder;
			}

		}
		return null;
	}

	@Override
	public List<RespStakeholder> mapAllRespDebitorOrCreditorStakeholder(List<Stakeholder> stakeholders) {

		if (stakeholders != null) {

			List<RespStakeholder> respStakeholders = new ArrayList<>();

			for (Stakeholder stakeholder : stakeholders) {
				RespStakeholder respStakeholder = mapRespStakeholderDebitorOrCreditor(stakeholder);

				if (respStakeholder != null) {
					respStakeholders.add(respStakeholder);
				}
			}

			return respStakeholders;
		}

		return null;
	}

	@Override
	public RespStakeholder mapRespStakeholderDebitorOrCreditor(Stakeholder stakeholder) {

		if (stakeholder != null) {
			RespStakeholder respStakeholder = mapRespStakeholder(stakeholder);

			if (stakeholder != null) {

				calculateDebitOrCreditBalance(stakeholder, respStakeholder);

				return respStakeholder;
			}
		}

		return null;
	}

	@Override
	public void createAccountStatement(Stakeholder stakeholder, Map<String, Object> map) {

		List<EsCreditDetail> creditDetails = null;
		List<EsDebitDetail> debitDetails = null;
		List<StakeholderStatement> statements = new ArrayList<>();
		if (stakeholder.getCredit() != null) {
			creditDetails = stakeholder.getCredit().getCreditDetails();
		}

		if (stakeholder.getDebit() != null) {
			debitDetails = stakeholder.getDebit().getDebitDetails();
		}

		log.info("Credit Size " + creditDetails.size() + " Debit Size: " + debitDetails.size());

		for (EsDebitDetail debitDetail : debitDetails) {
			StakeholderStatement statement = mapEsDebitDetailToStakeholderStatement(debitDetail);

			if (statement != null) {
				statements.add(statement);
			}
		}

		for (EsCreditDetail creditDetail : creditDetails) {
			StakeholderStatement statement = mapEsCreditDetailToStakeholderStatement(creditDetail);

			if (statement != null) {
				statements.add(statement);
			}
		}

		shortStatementListByDate(statements);

		Map<String, Object> response = new HashMap<>();

		List<StakeholderStatement> calStatements = calculateStatements(statements, response);

		RespStakeholder respStakeholder = mapRespStakeholderDetailsForStatment(stakeholder);

		response.put("statement", calStatements);
		response.put("stakholder", respStakeholder);
		map.put("response", response);
		map.put("message", "Statement found");
		map.put("status", true);

	}

	@Override
	public void createAccountStatementBetweenDate(ReqDate reqDate, Stakeholder stakeholder, Map<String, Object> map) {

		List<EsCreditDetail> creditDetails = null;
		List<EsDebitDetail> debitDetails = null;
		List<StakeholderStatement> statements = new ArrayList<>();
		if (stakeholder.getCredit() != null) {
			creditDetails = stakeholder.getCredit().getCreditDetails();
		}

		if (stakeholder.getDebit() != null) {
			debitDetails = stakeholder.getDebit().getDebitDetails();
		}

		for (EsDebitDetail debitDetail : debitDetails) {
			StakeholderStatement statement = mapEsDebitDetailToStakeholderStatement(debitDetail);

			if (statement != null) {
				statements.add(statement);
			}
		}

		for (EsCreditDetail creditDetail : creditDetails) {
			StakeholderStatement statement = mapEsCreditDetailToStakeholderStatement(creditDetail);

			if (statement != null) {
				statements.add(statement);
			}
		}

		shortStatementListByDate(statements);

		Map<String, Object> response = new HashMap<>();

		List<StakeholderStatement> calStatements = calculateStatements(statements, response);

		List<StakeholderStatement> splitStatements = splitStatementByStartAndEndDate(calStatements, reqDate, response);

		RespStakeholder respStakeholder = mapRespStakeholderDetailsForStatment(stakeholder);

		response.put("statement", splitStatements);
		response.put("queryDate", reqDate);
		response.put("stakholder", respStakeholder);
		map.put("response", response);

		map.put("message", "Statement found between date");

	}

	@Override
	public List<RespStakeholderType> mapAllRespStakeholderType(List<StakeholderType> stakeholderTypes) {

		if (stakeholderTypes != null) {
			List<RespStakeholderType> respStakeholderTypes = new ArrayList<>();

			for (StakeholderType stakeholderType : stakeholderTypes) {

				RespStakeholderType respStakeholderType = mapStakeType(stakeholderType);

				if (respStakeholderType != null) {
					respStakeholderTypes.add(respStakeholderType);
				}
			}

			return respStakeholderTypes;
		}

		return null;
	}

	@Override
	public Stakeholder mapStakeholder(StakeholderReq stakeholderReq) {

		if (stakeholderReq != null) {
			Stakeholder stakeholder = new Stakeholder();
			stakeholder.setEmail(stakeholderReq.getEmail());
			stakeholder.setPhoneNo(stakeholderReq.getPhoneNo());
			stakeholder.setName(stakeholderReq.getName());

			if (!helperServices.isNullOrEmpty(stakeholderReq.getKey())) {
				setStakeholderType(stakeholderReq.getKey(), stakeholder);
			}

			stakeholder.setNote(stakeholderReq.getNote());
			stakeholder.setDescription(stakeholderReq.getDescription());
			stakeholder.setTotalCreditAmount(converterServices.getDoubleToBigDec(0));
			stakeholder.setTotalDebitAmount(converterServices.getDoubleToBigDec(0));
			stakeholder.setPublicId(helperServices.getGenPublicId());
			return stakeholder;
		}
		return null;
	}

	@Override
	public RespStakeholder mapStakeholderDetails(Stakeholder stakeholder) {
		if (stakeholder != null) {

			RespStakeholder respStakeholder = mapRespDebitorOrCreditorStakeholderDetails(stakeholder);

			if (respStakeholder != null) {

				respStakeholder.setOrders(saleMapper.mapAllOrderOnly(stakeholder.getOrders()));
			}

			respStakeholder.setAccounts(bankAccountMapper.mapAllRespBankAccountOnly(stakeholder.getBankAccounts()));

			return respStakeholder;
		}

		return null;
	}

	@Override
	public RespStakeholder mapRespDebitorOrCreditorStakeholderDetails(Stakeholder stakeholder) {

		RespStakeholder respStakeholder = mapRespStakeholderDebitorOrCreditor(stakeholder);

		if (stakeholder.getDebit() != null) {
			RespEsDebit esDebit = new RespEsDebit();
			esDebit.setAmount(stakeholder.getDebit().getAmount());
			esDebit.setDate(stakeholder.getDebit().getDate());
			esDebit.setNote(stakeholder.getDebit().getNote());
			esDebit.setId(stakeholder.getDebit().getPublicId());
			if (stakeholder.getDebit().getDebitDetails() != null) {
				log.info("Debit Record Size " + stakeholder.getDebit().getDebitDetails().size());
				esDebit.setDebitDetails(esDebitMapper.mapAllDebitDetals(stakeholder.getDebit().getDebitDetails()));
			}
			respStakeholder.setEsDebit(esDebit);
		}

		if (stakeholder.getCredit() != null) {
			RespEsCredit credit = new RespEsCredit();
			credit.setAmount(stakeholder.getCredit().getAmount());
			credit.setDate(stakeholder.getCredit().getDate());
			credit.setNote(stakeholder.getCredit().getNote());

			List<RespEsCreditDetail> creditsDetails = esCreditMapper
					.mapAllCreditDetals(stakeholder.getCredit().getCreditDetails());

			credit.setCreditDetails(creditsDetails);
			respStakeholder.setEsCredit(credit);
		}

		List<RespBankAccount> accounts = bankAccountMapper.mapAllRespBankAccountOnly(stakeholder.getBankAccounts());
		if (accounts != null) {
			respStakeholder.setAccounts(accounts);
		}

		return respStakeholder;
	}

	@Override
	public RespStakeholder mapEsStakeholder(Stakeholder stakeholder) {

		return mapRespStakeholder(stakeholder);
	}

	@Override
	public RespStakeholder mapStakeholderStatistics(Stakeholder stakeholder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TempStakeholder mapTempStakeholder(StakeholderReq stakeholderReq) {

		if (stakeholderReq != null) {

			TempStakeholder tempStakeholder = new TempStakeholder();

			tempStakeholder.setEmail(stakeholderReq.getEmail());
			tempStakeholder.setName(stakeholderReq.getName());
			tempStakeholder.setPhoneNo(stakeholderReq.getPhoneNo());
			tempStakeholder.setPublicId(stakeholderReq.getId());
			tempStakeholder.setDescription(stakeholderReq.getDescription());
			tempStakeholder.setActive(true);

			return tempStakeholder;
		}

		return null;
	}

	@Override
	public List<RespEsStakeholder> mapAllEsStakeholder(List<Stakeholder> stakeholders) {

		if (stakeholders != null) {

			List<RespEsStakeholder> respStakeholders = new ArrayList<>();

			for (Stakeholder stakeholder : stakeholders) {
				RespEsStakeholder respStakeholder = mapRepEsStakeholder(stakeholder);

				if (respStakeholder != null) {
					respStakeholders.add(respStakeholder);
				}
			}

			return respStakeholders;
		}

		return null;
	}

	@Override
	public RespEsStakeholder mapRepEsStakeholder(Stakeholder stakeholder) {

		if (stakeholder != null) {

			RespEsStakeholder esStakeholder = new RespEsStakeholder();

			esStakeholder.setDate(stakeholder.getDate());
			esStakeholder.setDescription(stakeholder.getDescription());
			esStakeholder.setEmail(stakeholder.getEmail());
			esStakeholder.setId(stakeholder.getPublicId());
			esStakeholder.setName(stakeholder.getName());
			esStakeholder.setPhoneNo(stakeholder.getPhoneNo());

			if (stakeholder.getTotalCreditAmount() != null) {
				esStakeholder.setTotalCreditAmount(stakeholder.getTotalCreditAmount().doubleValue());

			}

			if (stakeholder.getTotalDebitAmount() != null) {
				esStakeholder.setTotalDebitAmount(stakeholder.getTotalDebitAmount().doubleValue());

			}

			esStakeholder.setActive(stakeholder.isActive());
			esStakeholder.setApprove(stakeholder.getApprove());
			esStakeholder.setGenId(stakeholder.getGenId());

			esStakeholder.setRespTypes(mapAllStakeholderType(stakeholder.getStakeholderTypes()));
			esStakeholder.setRejectNote(stakeholder.getRejectNote());

			return esStakeholder;

		}

		return null;
	}

	public User mapStakeholderToUser(Stakeholder stakeholder) {

		if (stakeholder != null) {
			User user = new User();
			user.setEmail(stakeholder.getEmail());
			user.setName(stakeholder.getName());
			user.setPhoneNo(stakeholder.getPhoneNo());
			user.setPublicId(helperServices.getGenPublicId());

			return user;
		}
		return null;
	}

	@Override
	public List<StakeholderType> mapSetToListAllStakeholderType(Set<StakeholderType> stakeholderTypes) {
		List<StakeholderType> types = new ArrayList<>();
		if (stakeholderTypes != null) {
			for (StakeholderType stakeholderType : stakeholderTypes) {
				types.add(stakeholderType);
			}
		}

		return types;
	}

	@Override
	public void mapAllSaleItems(List<Order> orders, Map<String, Object> map) {

		if (orders != null) {

			Set<SaleItem> saleItems = new HashSet<>();

			for (Order order : orders) {

				Set<SaleItem> items = mapAllOrderSaleItem(order);

				if (items != null) {
					saleItems.addAll(items);
				}

			}

			map.put("status", true);
			map.put("response", saleItems);
			map.put("message", "Customer Sale Items found " + saleItems.size());
		}

	}

	@Override
	public void mapAllAccount(List<BankAccount> bankAccounts, Map<String, Object> map) {
		if (bankAccounts != null) {
			List<RespBankAccount> accounts = new ArrayList<>();
			for (BankAccount bankAccount : bankAccounts) {

				RespBankAccount account = bankAccountMapper.mapEsRespBankAccount(bankAccount);

				if (account != null) {
					accounts.add(account);
				}
			}

			map.put("status", true);
			map.put("response", accounts);
			map.put("message", "Customer Account found " + accounts.size());

		}

	}

	@Override
	public void mapAllAccountForOption(List<BankAccount> bankAccounts, Map<String, Object> map) {

		if (bankAccounts != null) {
			List<RespAccount> accounts = new ArrayList<>();
			for (BankAccount bankAccount : bankAccounts) {

				RespAccount account = bankAccountMapper.mapEsRespAccount(bankAccount);

				if (account != null) {
					accounts.add(account);
				}
			}

			map.put("status", true);
			map.put("response", accounts);
			map.put("message", "Customer Account found " + accounts.size());

		}

	}

	private Set<SaleItem> mapAllOrderSaleItem(Order order) {

		if (order != null) {
			Set<SaleItem> saleItems = new HashSet<>();

			if (order.getOrderItems() != null) {
				for (OrderItem orderItem : order.getOrderItems()) {
					SaleItem item = mapSaleItem(orderItem);

					if (item != null) {
						saleItems.add(item);
					}
				}
			}

			return saleItems;
		}

		return null;

	}

	private SaleItem mapSaleItem(OrderItem orderItem) {

		if (orderItem != null) {
			SaleItem saleItem = new SaleItem();

			

			return saleItem;
		}

		return null;
	}

	private List<StakeholderStatement> splitStatementByStartAndEndDate(List<StakeholderStatement> calStatements,
			ReqDate reqDate, Map<String, Object> response) {

		List<StakeholderStatement> splitStatements = new ArrayList<>();

		double totalCredit = 0, totalDebit = 0;
		boolean isFirst = false;
		for (StakeholderStatement statement : calStatements) {

			boolean isAfter = dateServices.isEqualOrAfterDate(statement.getDateGrupe(), reqDate.getDate());
			boolean isBefor = dateServices.isEqualOrBeforDate(statement.getDateGrupe(), reqDate.getEndDate());

			if (isAfter && isBefor) {

				if (!isFirst) {
					statement.setStart(true);
					splitStatements.add(statement);
					isFirst = true;
				} else {
					splitStatements.add(statement);
				}

				totalCredit = totalCredit + statement.getCredit();
				totalDebit = totalDebit + statement.getDebit();

			}
		}

		StatementInf statementInf = new StatementInf();
		statementInf.setTotalCreditAmount(totalCredit);
		statementInf.setTotalDebitAmount(totalDebit);
		statementInf.setDeffValue(totalDebit - totalCredit);

		log.info("Debit " + totalDebit + " Credit " + totalCredit + " Balance " + (totalDebit - totalCredit));

		response.put("statmentInf", statementInf);

		return splitStatements;
	}

	private List<StakeholderStatement> calculateStatements(List<StakeholderStatement> statements,
			Map<String, Object> response) {

		List<StakeholderStatement> calStatements = new ArrayList<>();
		if (statements != null) {

			double lastCreditBalance = 0, lastDebitBalance = 0, totalDebit = 0, totalCredit = 0;

			for (StakeholderStatement statement : statements) {

				totalCredit = totalCredit + statement.getCredit();
				totalDebit = totalDebit + statement.getDebit();
				log.info("C=" + statement.getCredit() + " D " + statement.getDebit() + "  ******************");
				if (statement.getCredit() > 0) {

					if (lastCreditBalance > 0) {
						log.info("LC= LC+C: " + lastCreditBalance + " + " + statement.getCredit() + "="
								+ (lastCreditBalance + statement.getCredit()));

						lastCreditBalance = lastCreditBalance + statement.getCredit();

					} else {
						if (lastDebitBalance > statement.getCredit()) {
							log.info("LD= LD-C: " + lastDebitBalance + " - " + statement.getCredit() + "="
									+ (lastDebitBalance - statement.getCredit()));
							lastDebitBalance = lastDebitBalance - statement.getCredit();
						} else {
							log.info("LC=C-LD: " + statement.getCredit() + " - " + lastDebitBalance + "="
									+ (statement.getCredit() - lastDebitBalance));
							lastCreditBalance = statement.getCredit() - lastDebitBalance;
							lastDebitBalance = 0;
							log.info("LD = 0 : " + lastDebitBalance);
						}
					}

				} else if (statement.getDebit() > 0) {

					if (lastDebitBalance > 0) {
						log.info("LD=LD+D " + lastDebitBalance + " + " + statement.getDebit() + " = "
								+ (lastDebitBalance + statement.getDebit()));
						lastDebitBalance = lastDebitBalance + statement.getDebit();
					} else {

						if (statement.getDebit() > lastCreditBalance) {
							log.info("LD=D-LC " + statement.getDebit() + " - " + lastCreditBalance + " = "
									+ (statement.getDebit() - lastCreditBalance));
							lastDebitBalance = statement.getDebit() - lastCreditBalance;
							lastCreditBalance = 0;

							log.info("LC = 0 : " + lastCreditBalance);
						} else {

							log.info("LC=LC-D: " + lastCreditBalance + " - " + statement.getDebit() + " = "
									+ (lastCreditBalance - statement.getDebit()));

							lastCreditBalance = lastCreditBalance - statement.getDebit();
						}
					}

				}

				statement.setCreditBalance(lastCreditBalance);
				statement.setDebitBalance(lastDebitBalance);

				calStatements.add(statement);
				log.info("*************** ************* ******************");
			}

			StatementInf statementInf = new StatementInf();
			statementInf.setTotalCreditAmount(totalCredit);
			statementInf.setTotalDebitAmount(totalDebit);
			statementInf.setDeffValue(totalDebit - totalCredit);

			log.info(" TD " + totalDebit + " TC -> " + totalCredit + " Balance: " + (totalDebit - totalCredit));

			response.put("statmentInf", statementInf);

		}

		return calStatements;
	}

	private void shortStatementListByDate(List<StakeholderStatement> statements) {
		Collections.sort(statements, new Comparator<StakeholderStatement>() {

			@Override
			public int compare(StakeholderStatement o1, StakeholderStatement o2) {
				if (o1 != null && o2 != null) {

					if (o1.getDate() != null) {
						return o1.getDate().compareTo(o2.getDate());
					}

				}
				return 0;
			}

		});
	}

	private RespStakeholder mapRespStakeholderDetailsForStatment(Stakeholder stakeholder) {
		RespStakeholder respStakeholder = mapRespStakeholder(stakeholder);
		return respStakeholder;
	}

	private StakeholderStatement mapEsCreditDetailToStakeholderStatement(EsCreditDetail creditDetail) {
		if (creditDetail != null) {
			StakeholderStatement statement = new StakeholderStatement();
			statement.setDescription(creditDetail.getNote());
			statement.setCredit(creditDetail.getAmount());

			statement.setDate(creditDetail.getDate());
			statement.setDateGrupe(creditDetail.getDateGroup());
			return statement;
		}

		return null;
	}

	private StakeholderStatement mapEsDebitDetailToStakeholderStatement(EsDebitDetail debitDetail) {

		if (debitDetail != null) {
			StakeholderStatement statement = new StakeholderStatement();

			statement.setDescription(debitDetail.getNote());
			statement.setDebit(debitDetail.getAmount());

			statement.setDate(debitDetail.getDate());
			statement.setDateGrupe(debitDetail.getDateGroup());
			return statement;
		}

		return null;
	}

	private void calculateDebitOrCreditBalance(Stakeholder stakeholder, RespStakeholder respStakeholder) {
		double dAmount = 0, cAmount = 0;

		if (stakeholder.getTotalDebitAmount() != null) {
			dAmount = stakeholder.getTotalDebitAmount().doubleValue();
		}

		if (stakeholder.getTotalCreditAmount() != null) {
			cAmount = stakeholder.getTotalCreditAmount().doubleValue();
		}

		if (dAmount >= cAmount) {
			respStakeholder.setDebitBalance(dAmount - cAmount);
		}

		if (cAmount > dAmount) {
			respStakeholder.setCreditBalance(cAmount - dAmount);
		}
	}

	private List<RespStakeholderType> mapAllStakeholderType(Set<StakeholderType> types) {

		List<RespStakeholderType> respStakeholderTypes = new ArrayList<RespStakeholderType>();
		if (types != null) {

			for (StakeholderType stakeholderType : types) {

				RespStakeholderType respStakeholderType = mapStakeType(stakeholderType);

				respStakeholderTypes.add(respStakeholderType);
			}

			return respStakeholderTypes;
		}

		return null;
	}

	private void setStakeholderType(String type, Stakeholder stakeholder) {
		StakeholderType stakeholderType = stakeholderTypeServices.getStakeholderTypeByKey(type);
		Set<StakeholderType> stakeholderTypes = new HashSet<>();
		stakeholderTypes.add(stakeholderType);
		stakeholder.setStakeholderTypes(stakeholderTypes);
	}

	private RespStakeholderType mapStakeType(StakeholderType stakeholderType) {

		RespStakeholderType respStakeholderType = new RespStakeholderType();
		respStakeholderType.setId(stakeholderType.getId());
		respStakeholderType.setName(stakeholderType.getName());
		respStakeholderType.setKey(stakeholderType.getKey());

		return respStakeholderType;
	}

	@Override
	public RespStakeholder mapCustomerStakeholderDetails(Stakeholder stakeholder, RespStakeholder respStakeholder) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public RespStakeholder mapRespStakeholderDetails(Stakeholder stakeholder) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Stakeholder mapStakeholderUpdateOrCreate(StakeholderReq stakeholder) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
