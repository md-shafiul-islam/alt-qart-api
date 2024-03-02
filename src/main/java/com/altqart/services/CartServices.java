package com.altqart.services;

import java.util.Map;

import com.altqart.model.Cart;
import com.altqart.model.User;
import com.altqart.req.model.CartChooseReq;
import com.altqart.req.model.CartItemReq;
import com.altqart.req.model.CartReq;
import com.altqart.resp.model.RespCart;

public interface CartServices {

	public void getAllCart(Map<String, Object> map, int start, int size);

	public void addCart(CartReq cart, Map<String, Object> map);

	public void updateCart(CartReq cart, Map<String, Object> map);

	public void addCartItem(CartItemReq cartItem, Map<String, Object> map);

	public void removeCartItem(CartItemReq cartItem, Map<String, Object> map);

	public void updateCartItem(CartItemReq cart, Map<String, Object> map);

	public Cart getCartById(String id);

	public RespCart getRespCartById(String id);

	public RespCart getRespCartOnlyById(String id);

	public String getUserCartId();

	public void incrementCartItem(CartItemReq cart, Map<String, Object> map);

	public void decrementCartItem(CartItemReq cart, Map<String, Object> map);

	public void deleteCartItem(CartItemReq cart, Map<String, Object> map);

	public void getCartByUser(User user, Map<String, Object> map);

	public void toggleCartItem(CartChooseReq cartChoose, Map<String, Object> map);

	public void toggleAllCartItem(CartChooseReq cartChoose, Map<String, Object> map);


}
