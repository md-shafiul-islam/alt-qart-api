package com.altqart.mapper.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperConverterServices;
import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.MethodAndTransactionMapper;
import com.altqart.mapper.SaleMapper;
import com.altqart.mapper.UserMapper;
import com.altqart.model.Order;
import com.altqart.model.OrderItem;
import com.altqart.model.Product;
import com.altqart.model.Status;
import com.altqart.req.model.MethodAndTransactionReq;
import com.altqart.req.model.OrderItemReq;
import com.altqart.req.model.OrderReq;
import com.altqart.resp.model.RespOrder;
import com.altqart.resp.model.RespOrderItem;
import com.altqart.resp.model.RespProduct;
import com.altqart.resp.model.RespStatus;
import com.altqart.services.OrderServices;
import com.altqart.services.ProductServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SaleMapperImpl implements SaleMapper {

	@Autowired
	private ProductServices productServices;

	@Autowired
	private HelperConverterServices converterServices;

	@Autowired
	private HelperServices helperServices;

	@Autowired
	private OrderServices orderServices;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private MethodAndTransactionMapper transactionMapper;

	// Create Order Start
	@Override
	public Order mapOrder(OrderReq orderReq) {

		if (orderReq != null) {

			Order Order = new Order();

			if (orderReq.getDate() != null) {
				Order.setDate(orderReq.getDate());
				Order.setGrupeDate(orderReq.getDate());
			}
//			Order.setBusinessId(orderReq.getBusiness());

			setOrderItems(orderReq.getProducts(), Order);

			if (Order.getOrderItems() != null) {
				if (Order.getOrderItems().size() == 0) {
					return null;
				}
			} else {
				return null;
			}

			Order.setDiscountPercent(orderReq.getDiscountRate());

			// Calculate Discount and Set

			Order.setShippingAndHandlingAmount(orderReq.getShippingAndHandlingAmount());
			Order.setGrandTotal(0);// TODO:Calculate Sale Grand Total

			double totalAmount = 0;
			if (orderReq.getMethodTransactions() != null) {

				for (MethodAndTransactionReq transactionReq : orderReq.getMethodTransactions()) {
					totalAmount = totalAmount + transactionReq.getAmount();
				}
			}
			Order.setMethodTransactions(transactionMapper.mapAllMethodAndTransaction(orderReq.getMethodTransactions()));

//			Order.setInvNo(helperServices.getDateGenSaleIdUsingCount(count));

//			if (Order.getGrandTotal() == totalSaleAmount) {
//				return Order;
//			}

		}

		return null;
	}

	private double getTotalAmountWithCost(Order Order) {

		if (Order != null) {

//			return Order.getShippingAndHandlingAmount() + Order.getOtherAmount() + Order.getTotalAmountVat();
		}

		return 0;
	}

	@Override
	public RespOrder mapSaleInvoiceDetails(Order dbOrder) {
		RespOrder order = mapOrderDetails(dbOrder);

		if (order != null) {
			order.setUser(userMapper.mapRespEsUser(dbOrder.getUser()));
		}
		return order;
	}

	@Override
	public RespOrder mapOrderDetails(Order order) {

		if (order != null) {

			RespOrder respOrder = mapRespOrderOnly(order);

			if (respOrder != null) {
				if (order.getOrderItems() != null) {
//					respOrder.setOrderItems(mapAllOrderItemsWithProduct(order.getOrderItems()));
				}

				return respOrder;
			}
		}
		return null;
	}

	@Override
	public List<RespOrder> mapAllOrderByStatus(List<Order> Order, int status) {
		if (Order != null) {

			List<RespOrder> respOrder = new ArrayList<>();

			for (Order order : Order) {

				if (order.getStatus() != null) {
					if (status == order.getStatus().getId()) {
//						RespOrder respOrder = mapRespOrderOnly(order);

						if (respOrder != null) {
//							respOrder.add(respOrder);
						}
					}
				}

			}

			return respOrder;
		}

		return null;
	}

	private List<RespOrderItem> mapAllOrderItemsWithProduct(List<OrderItem> orderItems) {

		if (orderItems != null) {
			List<RespOrderItem> RespOrderItem = new ArrayList<>();

			for (OrderItem oItem : orderItems) {
				RespOrderItem item = mapOrderItemWithProduct(oItem);

				if (item != null) {
					RespOrderItem.add(item);

				}
			}
			return RespOrderItem;
		}

		return null;
	}

	public List<RespOrderItem> mapAllOrderItemsOnly(List<OrderItem> orderItems) {

		if (orderItems != null) {
			List<RespOrderItem> items = new ArrayList<>();

			for (OrderItem oItem : orderItems) {
				RespOrderItem item = mapOrderItemOnly(oItem);

				if (item != null) {
					items.add(item);
				}
			}

			return items;
		}

		return null;
	}

	public RespOrderItem mapOrderItemOnly(OrderItem orderItem) {

		if (orderItem != null) {

			RespOrderItem respOrderItem = new RespOrderItem();

			respOrderItem.setDate(orderItem.getDate());
			respOrderItem.setExpWarranty(orderItem.getExpWarranty());
			respOrderItem.setGroupDate(orderItem.getGroupDate());
			respOrderItem.setId(orderItem.getPublicId());
			respOrderItem.setLoss(orderItem.getLoss());
			respOrderItem.setPrice(orderItem.getPrice());
			respOrderItem.setProfit(orderItem.getProfit());
			respOrderItem.setPurchaseBarCode(orderItem.getPurchaseBarCode());
			respOrderItem.setQty(orderItem.getQty());
			respOrderItem.setReturnQty(orderItem.getReturnQty());
			respOrderItem.setReturnStatus(orderItem.getReturnStatus());
			respOrderItem.setSubTotal(orderItem.getSubTotal());
			respOrderItem.setTotalCostOfGoods(orderItem.getTotalCostOfGoods());
			respOrderItem.setTotalLoss(orderItem.getTotalLoss());

			return respOrderItem;
		}
		return null;
	}

	public RespOrderItem mapOrderItemWithProduct(OrderItem orderItem) {

		if (orderItem != null) {

			RespOrderItem respOrderItem = mapOrderItemOnly(orderItem);

			if (respOrderItem != null) {
				RespProduct respProduct = mapRespProductOnly(orderItem.getProduct());
//				respProduct.setItem(respItem);
				respOrderItem.setProduct(respProduct);
			}

			return respOrderItem;
		}
		return null;
	}

	public RespProduct mapRespProductOnly(Product product) {

		if (product != null) {
			RespProduct respProduct = new RespProduct();

//			respProduct.setLastUpdate(product.getLastUpdate());
			respProduct.setId(product.getPublicId());
			return respProduct;
		}

		return null;
	}

	private double getOrderDueAmount(Order Order, double payAmount) {

		double changeAmount = 0;
		double dueAmount = 0;

		if (Order != null) {
//			Order.setCullectedAmount(payAmount);

//			if (Order.getNetSaleAmount() >= payAmount) {
//				dueAmount = (Order.getNetSaleAmount() - payAmount);
//
//				dueAmount = converterServices.getRoundTwoDecPoint(dueAmount);
//				Order.setPayAmount(payAmount);
//			} else {
//				changeAmount = (payAmount - Order.getNetSaleAmount());
//
//				changeAmount = converterServices.getRoundTwoDecPoint(changeAmount);
//
//				Order.setPayAmount(Order.getNetSaleAmount());
//				Order.setChangeAmount(changeAmount);
//			}

		}
		return dueAmount;
	}

	private BigDecimal getOrderTotalLoss(Order Order) {

		if (Order != null) {

//			double tLoss = (Order.getTotalProductCost() - Order.getNetSaleAmount());

//			if (tLoss > 0) {
//				return new BigDecimal(tLoss);
//			}
		}

		return new BigDecimal(0);
	}

	private double getRevenueAmount(Order Order) {
//		double netReve = (Order.getNetSaleAmount() - Order.getTotalProductCost());

//		if (netReve > 0) {
//			return netReve;
//		}
		return 0;
	}

	private double getNetSaleAmount(Order Order, OrderReq orderReq) {
		double netSaleAmnt = 0;
		if (Order != null && orderReq != null) {
//			if (orderReq.getTotalAmntWithCost() == Order.getTotalSaleAmountWithTotalCost()) {
//				netSaleAmnt = (Order.getTotalSaleAmountWithTotalCost() - Order.getLsessAdjustment());
//			}

		} else {
			if (orderReq.isAdjStatus()) {
				netSaleAmnt = orderReq.getGrandTotal();
//
//				double lessAdj = Order.getTotalSaleAmountWithTotalCost() - orderReq.getGrandTotal();
//				Order.setLsessAdjustment(lessAdj);
			}
		}

		return converterServices.getRoundTwoDecPoint(netSaleAmnt);
	}

	private double getTotalAmountWithVat(Order Order) {

//		double totalVat = (Order.getAmountAfterDiscount() * Order.getVatRate()) / 100;

//		totalVat = converterServices.getRoundTwoDecPoint(totalVat);

//		Order.setTotalVat(totalVat);
//		double totalAmntWiVat = Order.getAmountAfterDiscount() + totalVat;

		return 0; //converterServices.getRoundTwoDecPoint(totalAmntWiVat);
	}

	private double getAfterDiscountTotalAmount(Order Order, double discount) {
		double amount = 0;
		if (Order != null) {
			amount = Order.getItemTotal();
			double totalDscount = (Order.getDiscountPercent() * Order.getItemTotal()) / 100;
			totalDscount = converterServices.getRoundTwoDecPoint(totalDscount);

			if (discount == totalDscount) {
				Order.setDiscount(totalDscount);
			} else {

				double discountRate = (discount / Order.getItemTotal()) * 100;

				Order.setDiscount(discount);
				Order.setDiscountPercent(converterServices.getRoundTwoDecPoint(discountRate));
			}

			amount = (Order.getItemTotal() - Order.getDiscount());
		}

		return amount;
	}

	private void setOrderItems(List<OrderItemReq> products, Order Order) {

		log.info("setOrderItems ... ");
		if (Order != null && products != null) {

			log.info("Order Items setting ... ");
			List<OrderItem> items = new ArrayList<>();

			double itemTotal = 0;
			double totalCostOfGoods = 0;

			for (OrderItemReq reqOrderItem : products) {

				log.info("Product Public Id " + reqOrderItem.getProduct());

				if (reqOrderItem != null) {
					Product product = null; //productServices.getProductByPublicId(reqOrderItem.getProduct());

					if (product != null) {
//						log.info("Product Stock Qty " + product.getQty());
//						Purchase purchase = null;
					

						

						OrderItem item = null;// mapOrderItem(Order, reqOrderItem, product, purchase);

						if (item != null) {
							item.setPublicId(helperServices.getGenPublicId());
							items.add(item);
							double subTotal = item.getSubTotal() != null ? item.getSubTotal().doubleValue() : 0;
							itemTotal = (itemTotal + subTotal);
							totalCostOfGoods = (totalCostOfGoods + item.getTotalCostOfGoods());
						}
					}
				}

			}

			log.info("Order Items Size " + items.size());

			Order.setOrderItems(items);
			Order.setItemTotal(converterServices.getRoundTwoDecPoint(itemTotal));
		}

	}

	private OrderItem mapOrderItem(Order Order, OrderItemReq reqOrderItem, Product product) {

		if (reqOrderItem != null) {

			OrderItem item = new OrderItem();

			item.setOrder(Order);

			item.setPrice(reqOrderItem.getSalePrice());
			item.setQty(reqOrderItem.getQty());

			item.setSubTotal(getSubtotal(reqOrderItem.getQty(), reqOrderItem.getSalePrice()));

		
			item.setTotalLoss(getMultyTwo(item.getLoss(), item.getQty()));
			item.setTotalProfit(getMultyTwo(item.getQty(), item.getProfit()));

//			double purchasePrice = purchase.getPurchacePrice() != null ? purchase.getPurchacePrice().doubleValue() : 0;
//			item.setTotalCostOfGoods(getMultyTwo(item.getQty(), purchasePrice));
			item.setProduct(product);
			item.setPurchaseBarCode(reqOrderItem.getPurchaseBarCode());

			item.setDate(new Date());
			item.setGroupDate(new Date());
			return item;
		}

		return null;

	}

	private double getMultyTwo(double a, double b) {

		return (a * b);
	}

	private double subtractDubBigDec(double a, BigDecimal b) {
		double defAmnt = 0;
		if (b != null) {
			defAmnt = (a - b.doubleValue());
		}

		return Math.abs(defAmnt);
	}

	private BigDecimal getSubtotal(double qty, double salePrice) {
		double total = qty * salePrice;
		return new BigDecimal(converterServices.getRoundTwoDecPoint(total));
	}

	// Create Order End

	@Override
	public List<RespOrder> mapAllOrder(List<Order> Order) {

		if (Order != null) {
			log.info("Order Size " + Order.size());
			List<RespOrder> respOrder = new ArrayList<>();

			for (Order order : Order) {
//				RespOrder respOrder = mapRespOrderOnly(order);

				if (respOrder != null) {
//					respOrder.add(respOrder);
				}
			}

			return respOrder;
		}

		return null;
	}

	@Override
	public RespOrder mapRespOrderOnly(Order order) {

		if (order != null) {
			RespOrder respOrder = mapRepOrderOnlyWithoutCustomer(order);

			if (order.getStakeholder() != null) {
//				respOrder.setCustomer(customerMapper.mapRespCustomer(order.getStakeholder()));
			}

			return respOrder;
		}

		return null;
	}

	@Override
	public List<RespOrder> mapAllOrderOnly(List<Order> Order) {

		if (Order != null) {

			List<RespOrder> respOrder = new ArrayList<>();

			for (Order order : Order) {
//				RespOrder respOrder = mapRepOrderOnlyWithoutCustomer(order);

				if (order != null) {
//					respOrder.add(respOrder);
				}

			}

			return respOrder;
		}

		return null;
	}

	private RespOrder mapRepOrderOnlyWithoutCustomer(Order order) {
		RespOrder respOrder = new RespOrder();


		respOrder.setDate(order.getDate());
		respOrder.setDiscount(order.getDiscount());
		respOrder.setDiscountPercent(order.getDiscountPercent());

		respOrder.setGrupeDate(order.getGrupeDate());

		respOrder.setId(order.getPublicId());
		respOrder.setItemTotal(order.getItemTotal());
		respOrder.setInvNo(order.getInvNo());

		respOrder.setShippingAndHandlingAmount(order.getShippingAndHandlingAmount());

		respOrder.setGrandTotal(order.getGrandTotal());
		respOrder.setNetGrandTotal(order.getNetGrandTotal());
		if (order.getStatus() != null) {
			respOrder.setStatus(mapStatus(order.getStatus()));
		}

		if (order.getMethodTransactions() != null) {
			respOrder.setMethodTransactions(
					transactionMapper.mapAllRespMethodAndTransaction(order.getMethodTransactions()));
		}
		return respOrder;
	}

	private RespStatus mapStatus(Status status) {
		if (status != null) {
			RespStatus respStatus = new RespStatus();
			respStatus.setId(status.getId());
			respStatus.setName(status.getName());

			return respStatus;
		}
		return null;
	}
}
