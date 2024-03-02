package com.altqart.mapper;

import java.util.List;
import java.util.Map;

import com.altqart.model.Cart;
import com.altqart.model.CartItem;
import com.altqart.req.model.CartItemReq;
import com.altqart.req.model.CartReq;
import com.altqart.resp.model.RespCart;

public interface CartMapper {

	public void mapAllCart(List<Cart> carts, Map<String, Object> map);

	public RespCart mapRespCartOnly(Cart cart);

	public Cart mapCart(CartReq cartReq);

	public CartItem mapCartItem(CartItemReq cartItem);

	public RespCart mapRespCart(Cart cart);

	public List<CartItem> mapReqCartItem(CartReq cartReq, Cart cart);

}
