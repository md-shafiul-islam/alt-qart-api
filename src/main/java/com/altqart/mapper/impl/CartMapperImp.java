package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.CartMapper;
import com.altqart.model.Cart;
import com.altqart.model.CartItem;
import com.altqart.model.ImageGallery;
import com.altqart.model.Product;
import com.altqart.model.Variant;
import com.altqart.req.model.CartItemReq;
import com.altqart.req.model.CartReq;
import com.altqart.resp.model.RespCart;
import com.altqart.resp.model.RespCartItem;
import com.altqart.services.ProductServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CartMapperImp implements CartMapper {

	@Autowired
	private ProductServices productServices;

	@Autowired
	private HelperServices helperServices;

	@Override
	public void mapAllCart(List<Cart> carts, Map<String, Object> map) {

		if (carts != null) {
			List<RespCart> respCarts = new ArrayList<>();

			for (Cart cart : carts) {
				RespCart respCart = mapRespCartOnly(cart);

				if (respCart != null) {
					respCarts.add(respCart);
				}
			}

			if (respCarts.size() > 0) {
				map.put("status", true);
				map.put("message", respCarts.size() + " Cart found");
				map.put("response", respCarts);
			}
		}

	}

	@Override
	public RespCart mapRespCartOnly(Cart cart) {

		if (cart != null) {

			RespCart respCart = new RespCart();

			respCart.setCouponCode(cart.getCouponCode());
			respCart.setChooseAll(cart.isChoose());
			respCart.setCouponPar(cart.getCouponPar());
			respCart.setChooseAmount(cart.getChooseAmount());
			respCart.setChooseQty(cart.getChooseQty());
			respCart.setCouponDiscount(cart.getCouponDiscount());

			respCart.setGrandTotal(cart.getGrandTotal());

			respCart.setDate(cart.getDate());
			respCart.setDiscount(cart.getDiscount());
			respCart.setId(cart.getPublicId());

			if (cart.getStakeholder() != null) {
				respCart.setStakeholder(cart.getStakeholder().getPublicId());
			}
			respCart.setTotalAmount(cart.getTotalAmount());
			respCart.setTotalQty(cart.getTotalQty());

			if (respCart.getTotalQty() == 0) {
				respCart.setTotalQty(cart.getCartItems().size());
			}

			respCart.setUpdateDate(cart.getUpdateDate());
			return respCart;
		}

		return null;
	}

	@Override
	public Cart mapCart(CartReq cartReq) {

		if (cartReq != null) {

			Cart cart = new Cart();
			cart.setCouponCode(cartReq.getCouponCode());
			cart.setDiscount(cartReq.getDiscount());
			cart.setPublicId(cartReq.getId());
			cart.setTotalAmount(cartReq.getTotalAmount());
			cart.setTotalQty(cartReq.getTotalQty());
			cart.setPublicId(helperServices.getGenPublicId());

			List<CartItem> cartItems = mapReqCartItem(cartReq, cart);
			cart.setCartItems(cartItems);

			return cart;
		}

		return null;
	}

	@Override
	public List<CartItem> mapReqCartItem(CartReq cartReq, Cart cart) {
		List<CartItem> cartItems = new ArrayList<>();

		double totalAmount = 0, totalQty = 0, totalChooseAmount = 0, chooseQty = 0;

		if (cartReq != null) {
			for (CartItemReq itemReq : cartReq.getCartItemReqs()) {
				CartItem cartItem = mapCartItem(itemReq);
				cartItem.setPublicId(helperServices.getGenPublicId());
				cartItem.setCart(cart);

				cartItems.add(cartItem);

				double itemPrice = 0;
				if (cartItem.getDiscountPrice() > 0) {
					itemPrice = cartItem.getDiscountPrice();
				} else {
					itemPrice = cartItem.getPrice();
				}
				totalAmount = totalAmount + itemPrice;
				totalQty = totalQty + cartItem.getQty();

				if (cartItem.isChoose()) {
					totalChooseAmount = totalChooseAmount + itemPrice;
					chooseQty = chooseQty + cartItem.getQty();
				}

			}
		}

		cart.setChooseAmount(totalChooseAmount);
		cart.setChooseQty(chooseQty);
		if (cart.getTotalAmount() != totalAmount) {
			cart.setTotalAmount(totalAmount);
		}

		cart.setGrandTotal(totalAmount);
		if (cart.getChooseQty() > 0) {
			cart.setGrandTotal(totalChooseAmount);

		}

		return cartItems;
	}

	@Override
	public CartItem mapCartItem(CartItemReq itemReq) {

		if (itemReq != null) {
			CartItem cartItem = new CartItem();
			cartItem.setPublicId(helperServices.getGenPublicId());
			cartItem.setDiscountPrice(itemReq.getDiscountPrice());
			cartItem.setQty(itemReq.getQty() > 0 ? itemReq.getQty() : 1);

			Variant variant = productServices.getVariantById(itemReq.getVariant());
			Product product = productServices.getProductByStrId(itemReq.getProduct());

			cartItem.setProduct(product);
			if (variant != null) {

				cartItem.setPrice(variant.getPrice());
				cartItem.setDiscountPrice(variant.getDicountPrice());
				cartItem.setVariant(variant);
			}

			if (cartItem.getDiscountPrice() > 0) {
				cartItem.setSubTotal(cartItem.getDiscountPrice() * cartItem.getQty());
			} else {
				cartItem.setSubTotal(cartItem.getPrice() * cartItem.getQty());
			}

			cartItem.setChoose(true);
			return cartItem;
		}

		return null;
	}

	@Override
	public RespCart mapRespCart(Cart cart) {

		if (cart != null) {
			RespCart respCart = mapRespCartOnly(cart);
			if (respCart != null) {

				List<RespCartItem> respCartItems = new ArrayList<>();
				double totalQty = 0, subTotal = 0, totalWeight = 0;

				for (CartItem cartItem : cart.getCartItems()) {
					totalQty = totalQty + cartItem.getQty();
					subTotal = subTotal + cartItem.getSubTotal();

					RespCartItem respCartItem = mapRespCartItem(cartItem);

					if (cartItem != null) {

						if (respCartItem.getWeight() > 0) {
							totalWeight = totalWeight + respCartItem.getWeight();
						}
					}

					respCartItems.add(respCartItem);

				}

				respCart.setTotalWeight(totalWeight);
				respCart.setTotalQty(totalQty);
				respCart.setTotalAmount(cart.getTotalAmount());
				respCart.setCartItems(respCartItems);

				if (cart.getTotalQty() > 0) {
					respCart.setTotalQty(cart.getTotalQty());
				}

				if (cart.getTotalAmount() > 0) {

					respCart.setTotalAmount(cart.getTotalAmount());
				}

				respCart.setDiscount(cart.getDiscount());
			}

			return respCart;
		}
		return null;
	}

	private RespCartItem mapRespCartItem(CartItem cartItem) {

		if (cartItem != null) {
			RespCartItem item = new RespCartItem();
			item.setDiscountPrice(cartItem.getDiscountPrice());
			item.setId(cartItem.getPublicId());
			item.setPrice(cartItem.getPrice());
			item.setDiscountPrice(cartItem.getDiscountPrice());
			item.setSubTotal(cartItem.getSubTotal());
			item.setQty(cartItem.getQty());

			if (cartItem.getProduct() != null) {

				item.setProduct(cartItem.getProduct().getPublicId());
				item.setTitle(cartItem.getProduct().getTitle());

				if (cartItem.getProduct().getMeasurement() != null) {
					item.setWeight(cartItem.getProduct().getMeasurement().getWeight());
				}

				if (cartItem.getProduct().getImages() != null) {

					Iterator<ImageGallery> imageGallery = cartItem.getProduct().getImages().iterator();

					ImageGallery image = imageGallery.next();

					if (image != null) {
						item.setImage(image.getLocation());
					}

				}

			}

			if (cartItem.getVariant() != null) {
				item.setVariant(cartItem.getVariant().getPublicId());

				if (cartItem.getVariant().getColor() != null) {
					item.setColor(cartItem.getVariant().getColor().getName());

				}

				if (cartItem.getVariant().getSize() != null) {
					item.setSize(cartItem.getVariant().getSize().getName());
				}

				item.setStkQty(cartItem.getVariant().getQty());
			}

			item.setChoose(cartItem.isChoose());
			return item;
		}

		return null;
	}

}
