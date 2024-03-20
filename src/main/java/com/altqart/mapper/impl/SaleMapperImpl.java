package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperConverterServices;
import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.AddressMapper;
import com.altqart.mapper.MethodAndTransactionMapper;
import com.altqart.mapper.SaleMapper;
import com.altqart.mapper.UserMapper;
import com.altqart.model.Address;
import com.altqart.model.Cart;
import com.altqart.model.CartItem;
import com.altqart.model.Coupon;
import com.altqart.model.Order;
import com.altqart.model.OrderItem;
import com.altqart.model.Product;
import com.altqart.model.ShippingAddress;
import com.altqart.model.Status;
import com.altqart.model.Variant;
import com.altqart.req.model.MethodAndTransactionReq;
import com.altqart.req.model.OrderPlaceReq;
import com.altqart.req.model.OrderReq;
import com.altqart.resp.model.RespMinOrder;
import com.altqart.resp.model.RespMinOrderItem;
import com.altqart.resp.model.RespMinVariant;
import com.altqart.resp.model.RespOrder;
import com.altqart.resp.model.RespOrderItem;
import com.altqart.resp.model.RespProduct;
import com.altqart.resp.model.RespStatus;
import com.altqart.services.AddressServices;
import com.altqart.services.CartServices;
import com.altqart.services.CouponServices;
import com.altqart.services.OrderServices;
import com.altqart.services.ProductServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SaleMapperImpl implements SaleMapper {

	@Autowired
	private ProductServices productServices;

	@Autowired
	private CouponServices couponServices;

	@Autowired
	private CartServices cartServices;

	@Autowired
	private AddressServices addressServices;

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

	@Autowired
	private AddressMapper addressMapper;

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

//			setOrderItems(orderReq.getProducts(), Order);

			if (Order.getOrderItems() != null) {
				if (Order.getOrderItems().size() == 0) {
					return null;
				}
			} else {
				return null;
			}

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

	@Override
	public Order mapPlaceOrder(OrderPlaceReq orderPlaceReq) {

		if (orderPlaceReq != null) {

			try {
				Order order = new Order();

				Address address = addressServices.getAddressById(orderPlaceReq.getAddress());
				Cart cart = cartServices.getForOrderPlace(orderPlaceReq.getCart());
				if (address == null || cart == null) {
					return null;
				}

				Coupon coupon = null;

				if (!helperServices.isNullOrEmpty(cart.getCouponCode())) {
					coupon = couponServices.getCouponByCode(cart.getCouponCode());
				}

				ShippingAddress shippingAddress = new ShippingAddress();
				shippingAddress.setAddress(address);
				shippingAddress.setHome(address.getFullAddress());
				order.setShippingAddress(shippingAddress);

				List<OrderItem> items = new ArrayList<>();

				double totalAmount = 0, itemDiscount = 0, totalQty = 0;

				for (CartItem cartItem : cart.getCartItems()) {

					if (cartItem.isChoose()) {
						OrderItem item = mapOrderItemViaCart(cartItem);

						itemDiscount += cartItem.getPrice() - cartItem.getDiscountPrice();

						if (item != null) {
							item.setOrder(order);
							items.add(item);
							totalAmount += item.getSubTotal();
							totalQty += item.getQty();

						} else {
							throw new Exception("Order Items Not Set");
						}
					}

				}

				order.setItemDiscount(itemDiscount);

				order.setOrderItems(items);
				order.setSubTotal(totalAmount);

				order.setTotalQty(totalQty);
				order.setShippingAndHandlingAmount(orderPlaceReq.getShipping());
				order.setPaidAmount(orderPlaceReq.getPaidAmount());

				if (coupon != null) {

					couponApply(coupon, cart, order);

					if (order.getCouponDiscount() > 0) {
						order.setCouponCode(coupon.getCode());
					}
				}

				order.setTotalAmount(order.getSubTotal() - order.getCouponDiscount());
				if (!helperServices.isNullOrEmpty(orderPlaceReq.getNote())) {
					order.setNote(orderPlaceReq.getNote());
					order.setNoteCost(10);
				}

				order.setGrandTotal(
						order.getTotalAmount() + order.getNoteCost() + order.getShippingAndHandlingAmount());

				double creditAmount = order.getGrandTotal();
				if (order.getPaidAmount() > 0) {
					creditAmount = order.getGrandTotal() - order.getPaidAmount();

					if (orderPlaceReq.getMethodTransactions() != null) {

						order.setMethodTransactions(
								transactionMapper.mapAllMethodAndTransaction(orderPlaceReq.getMethodTransactions()));
					}
				}

				order.setCodAmount(creditAmount);
				order.setCodAmount(creditAmount);

				order.setPublicId(helperServices.getGenPublicId());
				order.setInvNo(helperServices.getInvIdbyStakeholderGenId());
				return order;

			} catch (Exception e) {
				return null;
			}

		}

		return null;
	}

	private void couponApply(Coupon coupon, Cart cart, Order order) {
		// Check Amount ant Quantity

		double couponAmount = 0, couponPar = 0, discountAmount = 0;

		if (coupon.getApplyAmount() < 0 && coupon.getApplyQty() < 0) {
			couponAmount = coupon.getAmount();
			couponPar = coupon.getPercentage();

		} else if (coupon.getApplyAmount() < 0 && coupon.getApplyQty() > 0) {

			if (order.getTotalQty() >= coupon.getApplyQty()) {
				couponAmount = coupon.getAmount();
				couponPar = coupon.getPercentage();
			}

		} else if (coupon.getApplyAmount() > 0 && coupon.getApplyQty() < 0) {
			if (order.getTotalAmount() >= coupon.getApplyAmount()) {
				couponAmount = coupon.getAmount();
				couponPar = coupon.getPercentage();
			}
		}

		if (couponAmount > 0) {
			discountAmount = couponAmount;
		} else {

			if (couponPar > 0) {

				discountAmount = converterServices.getAmountUsingPercentageTotal(couponPar, order.getSubTotal());
			}
		}

		order.setCouponDiscount(discountAmount);
		order.setCouponPar(couponPar);

	}

	private OrderItem mapOrderItemViaCart(CartItem cartItem) {

		if (cartItem != null) {
			Variant variant = cartItem.getVariant();
			OrderItem item = new OrderItem();

			double price = variant.getDicountPrice() > 0 ? variant.getDicountPrice() : variant.getPrice();
			item.setPrice(price);
			item.setQty(cartItem.getQty());
			item.setVariant(variant);
			item.setSubTotal(price * item.getQty());
			item.setPublicId(helperServices.getGenPublicId());

			return item;
		}

		return null;
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
			respOrderItem.setPrice(orderItem.getPrice());
			respOrderItem.setQty(orderItem.getQty());
			respOrderItem.setReturn(orderItem.isReturn());
			respOrderItem.setReturnQty(orderItem.getReturnQty());
			respOrderItem.setSubTotal(orderItem.getSubTotal());

			return respOrderItem;
		}
		return null;
	}

	public RespOrderItem mapOrderItemWithProduct(OrderItem orderItem) {

		if (orderItem != null) {

			RespOrderItem respOrderItem = mapOrderItemOnly(orderItem);

			if (respOrderItem != null) {
//				respProduct.setItem(respItem);
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

	@Override
	public List<RespOrder> mapAllOrder(List<Order> orders) {

		if (orders != null) {
			List<RespOrder> respOrders = new ArrayList<>();

			for (Order order : orders) {
				RespOrder respOrder = mapRespOrderOnly(order);

				if (respOrder != null) {
					respOrders.add(respOrder);
				}
			}

			return respOrders;
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

	@Override
	public List<RespMinOrder> mapAllRespMinOrder(List<Order> orders) {

		List<RespMinOrder> minOrders = new ArrayList<>();

		if (orders != null) {

			for (Order order : orders) {
				RespMinOrder minOrder = mapRespMinOrder(order);

				if (minOrder != null) {
					minOrders.add(minOrder);
				}
			}
		}

		return minOrders;
	}

	private RespMinOrder mapRespMinOrder(Order order) {

		if (order != null) {
			RespMinOrder respOrder = new RespMinOrder();

			respOrder.setCodAmount(order.getCodAmount());
			respOrder.setCouponCode(order.getCouponCode());
			respOrder.setCouponDiscount(order.getCouponDiscount());
			respOrder.setCouponPar(order.getCouponPar());
			respOrder.setCreditAmount(order.getCreditAmount());
			respOrder.setDate(order.getDate());
			respOrder.setGrupeDate(order.getGrupeDate());

			respOrder.setGrandTotal(order.getGrandTotal());

			respOrder.setId(order.getPublicId());
			respOrder.setInvNo(order.getInvNo());
			respOrder.setItemDiscount(order.getItemDiscount());
			respOrder.setNote(order.getNote());
			respOrder.setNoteCost(order.getNoteCost());
			respOrder.setPaidAmount(order.getPaidAmount());

			respOrder.setReturnStatus(order.getReturnStatus());
			respOrder.setShippingAddress(addressMapper.mapRespShippingAddressOnly(order.getShippingAddress()));
			respOrder.setShippingAndHandlingAmount(order.getShippingAndHandlingAmount());
			respOrder.setShippingDiscount(order.getShippingDiscount());

			respOrder.setSubTotal(order.getSubTotal());

			respOrder.setTotalAmount(order.getTotalAmount());
			respOrder.setTotalQty(order.getTotalQty());

			if (order.getStatus() != null) {
				respOrder.setStatus(mapStatus(order.getStatus()));
			}

			if (order.getMethodTransactions() != null) {
				respOrder.setMethodTransactions(
						transactionMapper.mapAllRespMethodAndTransaction(order.getMethodTransactions()));
			}

			if (order.getOrderItems() != null) {
				respOrder.setOrderItems(mapAllMinRespOrderItem(order.getOrderItems()));
			}
			return respOrder;
		}

		return null;
	}

	private List<RespMinOrderItem> mapAllMinRespOrderItem(List<OrderItem> orderItems) {

		if (orderItems != null) {

			List<RespMinOrderItem> items = new ArrayList<>();

			for (OrderItem orderItem : orderItems) {
				RespMinOrderItem item = mapRespMinOrderItem(orderItem);

				if (item != null) {
					items.add(item);
				}
			}

			return items;
		}

		return null;
	}

	private RespMinOrderItem mapRespMinOrderItem(OrderItem orderItem) {

		if (orderItem != null) {

			RespMinOrderItem respOrderItem = new RespMinOrderItem();

			respOrderItem.setDate(orderItem.getDate());
			respOrderItem.setExpWarranty(orderItem.getExpWarranty());
			respOrderItem.setGroupDate(orderItem.getGroupDate());
			respOrderItem.setId(orderItem.getPublicId());
			respOrderItem.setPrice(orderItem.getPrice());
			respOrderItem.setQty(orderItem.getQty());
			respOrderItem.setReturn(orderItem.isReturn());
			respOrderItem.setReturnQty(orderItem.getReturnQty());
			respOrderItem.setSubTotal(orderItem.getSubTotal());
			respOrderItem.setVariant(mapRespMinVariant(orderItem.getVariant()));

			return respOrderItem;
		}

		return null;
	}

	private RespMinVariant mapRespMinVariant(Variant variant) {

		if (variant != null) {

			RespMinVariant minVariant = new RespMinVariant();

			minVariant.setAvailable(variant.isAvailable());
			minVariant.setBarCode(variant.getBarCode());
			minVariant.setDicountPrice(variant.getDicountPrice());
			minVariant.setFeature(variant.isFeature());
			minVariant.setFreeItems(variant.getFreeItems());
			minVariant.setId(variant.getPublicId());
			minVariant.setImageUrl(variant.getImageUrl());
			minVariant.setPrice(variant.getPrice());
			minVariant.setQty(variant.getQty());

			if (variant.getSize() != null) {
				String size = "";
				if (variant.getSize().getStandard() != null) {
					size += variant.getSize().getStandard().getName();
				}

				size += " " + variant.getSize().getName();
				minVariant.setSize(size);
			}

			if (variant.getColor() != null) {
				minVariant.setColor(variant.getColor().getName());
			}

			minVariant.setSku(variant.getSku());

			if (variant.getProduct() != null) {
				minVariant.setTitle(variant.getProduct().getTitle());
			}

			return minVariant;

		}

		return null;
	}

	private RespOrder mapRepOrderOnlyWithoutCustomer(Order order) {
		RespOrder respOrder = new RespOrder();

		respOrder.setCodAmount(order.getCodAmount());
		respOrder.setCouponCode(order.getCouponCode());
		respOrder.setCouponDiscount(order.getCouponDiscount());
		respOrder.setCouponPar(order.getCouponPar());
		respOrder.setCreditAmount(order.getCreditAmount());
		respOrder.setDate(order.getDate());
		respOrder.setGrupeDate(order.getGrupeDate());

		respOrder.setGrandTotal(order.getGrandTotal());

		respOrder.setId(order.getPublicId());
		respOrder.setInvNo(order.getInvNo());
		respOrder.setItemDiscount(order.getItemDiscount());
		respOrder.setNote(order.getNote());
		respOrder.setNoteCost(order.getNoteCost());
		respOrder.setPaidAmount(order.getPaidAmount());

		respOrder.setReturnStatus(order.getReturnStatus());
		respOrder.setShippingAddress(addressMapper.mapRespShippingAddressOnly(order.getShippingAddress()));
		respOrder.setShippingAndHandlingAmount(order.getShippingAndHandlingAmount());
		respOrder.setShippingDiscount(order.getShippingDiscount());

		respOrder.setSubTotal(order.getSubTotal());

		respOrder.setTotalAmount(order.getTotalAmount());
		respOrder.setTotalQty(order.getTotalQty());

		if (order.getStatus() != null) {
			respOrder.setStatus(mapStatus(order.getStatus()));
		}

		if (order.getMethodTransactions() != null) {
			respOrder.setMethodTransactions(
					transactionMapper.mapAllRespMethodAndTransaction(order.getMethodTransactions()));
		}

		if (order.getOrderItems() != null) {
			respOrder.setOrderItems(mapAllRespOrderItemOnly(order.getOrderItems()));
		}
		return respOrder;
	}

	private List<RespOrderItem> mapAllRespOrderItemOnly(List<OrderItem> orderItems) {

		if (orderItems != null) {

			List<RespOrderItem> respOrderItems = new ArrayList<>();

			for (OrderItem orderItem : orderItems) {

				RespOrderItem item = mapOrderItemOnly(orderItem);

				if (item != null) {
					respOrderItems.add(item);
				}
			}

			return respOrderItems;
		}

		return null;
	}

	private RespOrderItem mapOrderItemWithVariant(OrderItem orderItem) {

		RespOrderItem item = mapOrderItemOnly(orderItem);

		if (item != null) {

		}

		return item;
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
