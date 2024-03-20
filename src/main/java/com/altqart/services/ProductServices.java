package com.altqart.services;

import java.util.List;
import java.util.Map;

import com.altqart.model.Category;
import com.altqart.model.Product;
import com.altqart.model.Variant;
import com.altqart.req.model.ProductCatReq;
import com.altqart.req.model.ProductReq;
import com.altqart.resp.model.RespDetailProduct;
import com.altqart.resp.model.RespMinProduct;
import com.altqart.resp.model.RespProduct;

public interface ProductServices {

	public List<RespProduct> getAllProducts(int start, int size);

	public Product getProductById(int id);

	public List<Product> getProductsByCategory(Category category);

	public List<Product> getProductByCatId(int id);

	public long getCount();

	public List<Product> getAllLowStockQtyProduct();

	public RespProduct getProductByPublicId(String id);

	public List<Product> getAllAvailableProduct();

	public List<Product> getAllAvailableProductsByItemId(String id);

	public Variant getVariantById(String variant);

	public void add(ProductReq productReq, Map<String, Object> map);

	public void update(ProductReq productReq, Map<String, Object> map);

	public List<RespMinProduct> getAllMinRespProduct(int start, int size);

	public Product getProductByStrId(String product);

	public RespDetailProduct getDetailsProductByPublicId(String id);

	public void getRespProductExceptCategory(ProductCatReq catReq, Map<String, Object> map);

	public void getAllRespMinProductByCategory(String value, Map<String, Object> map);

	public List<RespMinProduct> getRandomMinRespProduct(int start, int size);

}
