
package com.altqart.controller.cart;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.altqart.helper.services.HelperAuthenticationServices;
import com.altqart.helper.services.HelperServices;
import com.altqart.model.User;
import com.altqart.req.model.CartChooseReq;
import com.altqart.req.model.CartItemReq;
import com.altqart.req.model.CartReq;
import com.altqart.req.model.CouponApplyReq;
import com.altqart.resp.model.RespCart;
import com.altqart.services.CartServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/carts")
public class RestCartController {

	@Autowired
	private HelperServices helperServices;

	@Autowired
	private HelperAuthenticationServices authenticationServices;

	@Autowired
	private CartServices cartServices;

	@GetMapping
	public ResponseEntity<?> getAllCart(@RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(name = "size", defaultValue = "-1") int size) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " Cart not found");

		cartServices.getAllCart(map, start, size);

		return ResponseEntity.ok(map);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getAllCart(@PathVariable("id") String id) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " Cart not found");

		String cartId = id;

		if (!helperServices.isValidAndLenghtCheck(id, 34)) {
			return ResponseEntity.ok(map);
		}
		
		RespCart respCart = cartServices.getRespCartById(cartId);

		if (respCart != null) {
			map.put("response", respCart);
			map.put("status", true);
			map.put("message", " Cart found by ID");
		}
		return ResponseEntity.ok(map);
	}

	@PostMapping(value = "/{id}/coupon")
	public ResponseEntity<?> getApplyCouponCart(@PathVariable("id") String id, @RequestBody CouponApplyReq coupon) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " Coupon Apply failed");

		if (helperServices.isValidAndLenghtCheck(id, 34)) {
			cartServices.getApplyCoupon(coupon, map);
			boolean status = (boolean) map.get("status");
			if (!status) {
				map.put("response", cartServices.getRespCartById(id));
			}
		}

		return ResponseEntity.ok(map);
	}

	@PostMapping
	public ResponseEntity<?> addCart(@RequestBody CartReq cart) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " Cart add failed");

		cartServices.addCart(cart, map);

		return ResponseEntity.ok(map);
	}

	@PostMapping(value = "/{id}/items")
	public ResponseEntity<?> addCartItem(@RequestBody CartItemReq cart, @PathVariable("id") String id) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " Cart Item add failed");

		cartServices.addCartItem(cart, map);

		return ResponseEntity.ok(map);
	}

	@PostMapping(value = "/{id}/increment")
	public ResponseEntity<?> incrementCartItem(@RequestBody CartItemReq cart, @PathVariable("id") String id) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " Cart Item increment failed");

		log.info(" Cart Item increment ...");

		if (helperServices.isValidAndLenghtCheck(id, 32)) {
			cartServices.incrementCartItem(cart, map);
		}

		return ResponseEntity.ok(map);
	}

	@PostMapping(value = "/{id}/decrement")
	public ResponseEntity<?> decrementCartItem(@RequestBody CartItemReq cart, @PathVariable("id") String id) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " Cart Item decrement ...");
		if (helperServices.isValidAndLenghtCheck(id, 32)) {
			cartServices.decrementCartItem(cart, map);
		}

		return ResponseEntity.ok(map);
	}

	@PostMapping(value = "/{id}/delete")
	public ResponseEntity<?> deleteCartItem(@RequestBody CartItemReq cart, @PathVariable("id") String id) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " Cart Item Delete ...");
		if (helperServices.isValidAndLenghtCheck(id, 32)) {
			cartServices.deleteCartItem(cart, map);
		}

		return ResponseEntity.ok(map);
	}

	@DeleteMapping(value = "/{id}/items/{iId}")
	public ResponseEntity<?> removeCartItem(@RequestBody CartItemReq cart, @PathVariable("id") String id,
			@PathVariable("iId") String itemId) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " Cart Item not found");

		cartServices.removeCartItem(cart, map);

		return ResponseEntity.ok(map);
	}

	@PutMapping(value = "/{id}/items/{iId}")
	public ResponseEntity<?> updateCartItem(@RequestBody CartItemReq cart, @PathVariable("id") String id,
			@PathVariable("iId") String itemId) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " Update cart item failed");

		cartServices.updateCartItem(cart, map);

		return ResponseEntity.ok(map);
	}

	@GetMapping(value = "/user/{id}")
	public ResponseEntity<?> getCartByUser(@PathVariable("id") String id) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " Update cart item failed");

		User user = authenticationServices.getCurrentUser();

		if (helperServices.isEqualAndFirstOneIsNotNull(id, user.getPublicId())) {
			cartServices.getCartByUser(user, map);
		}

		return ResponseEntity.ok(map);
	}

	@PutMapping
	public ResponseEntity<?> updateCart(@RequestBody CartReq cart) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " Cart Update faileed");

		cartServices.updateCart(cart, map);

		return ResponseEntity.ok(map);
	}

	@PostMapping(value = "/{id}/choose")
	public ResponseEntity<?> getCartChoose(@RequestBody CartChooseReq cartChoose, @PathVariable("id") String id) {

		Map<String, Object> map = new HashMap<>();

		map.put("response", null);
		map.put("status", false);
		map.put("message", " Update cart item failed");

		if (helperServices.isEqualAndFirstOneIsNotNull(id, cartChoose.getId())) {

			if (cartChoose.getType() > 0) {
				cartServices.toggleCartItem(cartChoose, map);
			} else if (cartChoose.getType() == -1) {
				cartServices.toggleAllCartItem(cartChoose, map);

			}
		}

		return ResponseEntity.ok(map);
	}

}
