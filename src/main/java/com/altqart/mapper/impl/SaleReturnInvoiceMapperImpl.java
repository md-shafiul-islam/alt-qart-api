package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperConverterServices;
import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.MethodAndTransactionMapper;
import com.altqart.mapper.SaleReturnInvoiceMapper;
import com.altqart.model.OrderItem;
import com.altqart.model.Product;
import com.altqart.model.SaleReturnInvoice;
import com.altqart.model.SaleReturnItem;
import com.altqart.model.Stakeholder;
import com.altqart.req.model.SaleReturnItemReq;
import com.altqart.req.model.SaleReturnReq;
import com.altqart.resp.model.RespSaleReturn;
import com.altqart.resp.model.RespSaleReturnItem;
import com.altqart.services.OrderServices;
import com.altqart.services.StakeholderServices;
import com.altqart.services.StockServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SaleReturnInvoiceMapperImpl implements SaleReturnInvoiceMapper {

	@Autowired
	private HelperServices helperServices;

	@Autowired
	private HelperConverterServices converterServices;

	@Autowired
	private OrderServices orderServices;

	@Autowired
	private StockServices stockServices;

	@Autowired
	private MethodAndTransactionMapper methodAndTransactionMapper;

	@Autowired
	private StakeholderServices stakeholderServices;

	@Override
	public List<RespSaleReturn> mapAllSaleReturnInvoice(List<SaleReturnInvoice> saleReturnInvoices) {

		if (saleReturnInvoices != null) {

			List<RespSaleReturn> respSaleReturns = new ArrayList<>();

			for (SaleReturnInvoice returnInvoice : saleReturnInvoices) {
				RespSaleReturn respSaleReturn = mapSaleReturnInvoiceOnly(returnInvoice);

				if (respSaleReturn != null) {
					respSaleReturns.add(respSaleReturn);
				}
			}

			return respSaleReturns;

		}

		return null;
	}

	@Override
	public RespSaleReturn mapSaleReturnInvoice(SaleReturnInvoice saleReturnInvoice) {

		if (saleReturnInvoice != null) {

			RespSaleReturn respSaleReturn = mapSaleReturnInvoiceOnly(saleReturnInvoice);

			if (respSaleReturn != null) {
				respSaleReturn.setReturnItems(mapAllSaleReturnItem(saleReturnInvoice.getReturnItems()));
			}

			return respSaleReturn;
		}

		return null;
	}

	@Override
	public SaleReturnInvoice mapSaleReturnInvoice(SaleReturnReq saleReturnReq, Map<String, Object> map)
			throws Exception {

		if (saleReturnReq != null) {

			SaleReturnInvoice invoice = new SaleReturnInvoice();

			invoice.setChangeAmount(saleReturnReq.getChangeAmount());
			invoice.setCreditAmount(saleReturnReq.getCreditAmount());

			Date date = new Date();

			if (saleReturnReq.getDate() != null) {
				date = saleReturnReq.getDate();
			}

			invoice.setDate(date);
			invoice.setGroupDate(date);

			if (helperServices.isValidAndLenghtCheck(saleReturnReq.getStakeholder(), 32)) {
//				Stakeholder stakeholder = stakeholderServices.getStakeholderPublicId(saleReturnReq.getStakeholder());
//				invoice.setStakeholder(stakeholder);
			}

			double itemsTotal = 0, subReturnFees = 0, grandTotal = 0, creditAmount = 0, changeAmount = 0,
					totalProfit = 0, totalLoss = 0;

			List<SaleReturnItem> returnItems = new ArrayList<>();
			if (saleReturnReq.getReturnItems() != null) {
				for (SaleReturnItemReq returnItemReq : saleReturnReq.getReturnItems()) {

					SaleReturnItem item = mapSaleReturnItem(returnItemReq);

					if (item != null) {
						item.setDate(date);
						returnItems.add(item);

						itemsTotal = itemsTotal + item.getSubTotal();
						totalProfit = totalProfit + item.getTotalProfit();
						totalLoss = totalLoss + item.getTotalLoss();
					}
				}
			}

			if (returnItems.size() == 0) {
				return null;
			}
			invoice.setReturnItems(returnItems);
			invoice.setPaidAmount(saleReturnReq.getPaidAmount());
			invoice.setLessAdustment(saleReturnReq.getLessAdustment());
			invoice.setNote(saleReturnReq.getNote());
			invoice.setPaidAmount(saleReturnReq.getPaidAmount());
			invoice.setPublicId(helperServices.getGenPublicId());

//			invoice.setMethodAndTransactions(
//					methodAndTransactionMapper.mapAllMethodAndTransaction(saleReturnReq.getMethodAndTransactions()));

			if (itemsTotal == saleReturnReq.getItemsTotal()) {

				invoice.setItemsTotal(saleReturnReq.getItemsTotal());
				invoice.setReturnFees(saleReturnReq.getReturnFees());
				invoice.setSubReturnFees(saleReturnReq.getSubReturnFees());
			} else {
				invoice.setItemsTotal(itemsTotal);
				invoice.setReturnFees(saleReturnReq.getReturnFees());

				subReturnFees = itemsTotal - saleReturnReq.getReturnFees();
				invoice.setSubReturnFees(subReturnFees);

				invoice.setLessAdustment(saleReturnReq.getLessAdustment());
				grandTotal = subReturnFees - saleReturnReq.getLessAdustment();
				invoice.setGrandTotal(grandTotal);

			}

			if (grandTotal == saleReturnReq.getGrandTotal()) {
				invoice.setGrandTotal(saleReturnReq.getGrandTotal());
			} else {
				invoice.setGrandTotal(grandTotal);
			}

			if (invoice.getGrandTotal() >= saleReturnReq.getPaidAmount()) {
				creditAmount = invoice.getGrandTotal() - saleReturnReq.getPaidAmount();
				changeAmount = 0;
			} else {
				changeAmount = saleReturnReq.getPaidAmount() - invoice.getGrandTotal();
				creditAmount = 0;
			}
			invoice.setCreditAmount(creditAmount);
			invoice.setChangeAmount(changeAmount);

			invoice.setSaleRevenue(totalProfit - totalLoss);
			invoice.setTotalLoss(totalLoss);
			invoice.setTotalProfit(totalProfit);

			if (invoice.getItemsTotal() == 0 || invoice.getGrandTotal() == 0) {
				return null;
			}

			return invoice;
		}

		return null;
	}

	@Override
	public SaleReturnItem mapSaleReturnItem(SaleReturnItemReq returnItemReq) throws Exception {

		if (returnItemReq != null) {

			OrderItem orderItem = null;

			log.info("returnItemReq ID " + returnItemReq.getId() + " Item  " + returnItemReq.getItem()
					+ " returnItemReq.getOrderItem() " + returnItemReq.getOrderItem());

			if (returnItemReq.getOrderItem() > 0) {
				orderItem = orderServices.getOrderItemById(returnItemReq.getOrderItem());

				if (orderItem == null) {
					return null;
				}
			}

			if (orderItem == null) {
				throw new Exception("Sale Retur Mapper Item Not found");
			}

			Product product = null;

			double vat = 0, discount = 0, itemsTotal = 0, subTotal = 0;

			SaleReturnItem item = new SaleReturnItem();

			item.setDiscountRate(returnItemReq.getDiscountRate());

			itemsTotal = returnItemReq.getQty() * returnItemReq.getPrice();
			discount = converterServices.getDiscount(returnItemReq.getDiscountRate(), itemsTotal);
			itemsTotal = itemsTotal - discount;
			vat = converterServices.getAmountUsingPercentageTotal(returnItemReq.getVatRate(), itemsTotal);
			subTotal = itemsTotal + vat;

			item.setDiscountRate(returnItemReq.getDiscountRate());
			item.setQty(returnItemReq.getQty());
			item.setPrice(returnItemReq.getPrice());

			if (converterServices.getRoundTwoDecPoint(subTotal) == returnItemReq.getSubTotal()) {
				item.setDsicount(returnItemReq.getDsicount());
				item.setItemSub(returnItemReq.getItemSub());
				item.setSubDsicount(returnItemReq.getSubDsicount());
				item.setSubTotal(returnItemReq.getSubTotal());
			} else {
				item.setDsicount(discount);
				item.setItemSub(subTotal);
				item.setSubDsicount(itemsTotal - discount);
				item.setSubTotal(subTotal);
			}

			item.setOrderItem(orderItem);

			item.setPublicId(helperServices.getGenPublicId());
			return item;

		}

		return null;
	}

	@Override
	public RespSaleReturn mapSaleReturnInvoiceOnly(SaleReturnInvoice returnInvoice) {

		if (returnInvoice != null) {
			RespSaleReturn respSaleReturn = new RespSaleReturn();

			respSaleReturn.setApprove(returnInvoice.getApprove());
			respSaleReturn.setChangeAmount(returnInvoice.getChangeAmount());

			respSaleReturn.setDate(returnInvoice.getDate());

			respSaleReturn.setGrandTotal(returnInvoice.getGrandTotal());
			respSaleReturn.setGroupDate(returnInvoice.getGroupDate());

			respSaleReturn.setId(returnInvoice.getPublicId());

			respSaleReturn.setItemsTotal(returnInvoice.getItemsTotal());

			respSaleReturn.setLessAdustment(returnInvoice.getLessAdustment());

			respSaleReturn.setNote(returnInvoice.getNote());

			respSaleReturn.setPaidAmount(returnInvoice.getPaidAmount());

			respSaleReturn.setReturnFees(returnInvoice.getReturnFees());

			respSaleReturn.setSaleRevenue(returnInvoice.getSaleRevenue());
			respSaleReturn.setSubReturnFees(returnInvoice.getSubReturnFees());

			respSaleReturn.setTotalLoss(returnInvoice.getTotalLoss());
			respSaleReturn.setTotalProfit(returnInvoice.getTotalProfit());
			respSaleReturn.setTotalDiscount(returnInvoice.getTotalDiscount());
			respSaleReturn.setPrevCredit(returnInvoice.getPrevCredit());
			respSaleReturn.setPrevDebit(returnInvoice.getPrevDebit());
			return respSaleReturn;
		}

		return null;
	}

	@Override
	public List<RespSaleReturnItem> mapAllSaleReturnItem(List<SaleReturnItem> returnItems) {

		if (returnItems != null) {

			List<RespSaleReturnItem> items = new ArrayList<>();

			for (SaleReturnItem saleReturnItem : returnItems) {

				RespSaleReturnItem item = mapSaleReturnItemOnly(saleReturnItem);

				if (item != null) {
					items.add(item);
				}
			}

			return items;
		}

		return null;
	}

	@Override
	public RespSaleReturnItem mapSaleReturnItemOnly(SaleReturnItem saleReturnItem) {

		if (saleReturnItem != null) {
			RespSaleReturnItem item = new RespSaleReturnItem();
			item.setDate(saleReturnItem.getDate());
			item.setDiscountRate(saleReturnItem.getDiscountRate());
			item.setDsicount(saleReturnItem.getDsicount());
			item.setId(saleReturnItem.getPublicId());
			item.setItemSub(saleReturnItem.getItemSub());
			item.setLoss(saleReturnItem.getLoss());
			item.setName(saleReturnItem.getName());
			item.setPrice(saleReturnItem.getPrice());
			item.setProfit(saleReturnItem.getProfit());
			item.setQty(saleReturnItem.getQty());
			item.setSubDsicount(saleReturnItem.getSubDsicount());
			item.setSubTotal(saleReturnItem.getSubTotal());

			return item;
		}

		return null;
	}
}
