package com.altqart.mapper;
import java.util.List;

import com.altqart.model.Product;
import com.altqart.model.Specification;
import com.altqart.model.Variant;
import com.altqart.req.model.ProductReq;
import com.altqart.req.model.SpecificationReq;
import com.altqart.req.model.VariantReq;
import com.altqart.resp.model.RespDetailProduct;
import com.altqart.resp.model.RespMinProduct;
import com.altqart.resp.model.RespProduct;

public interface ProductMapper {

	public Product mapProductReq(ProductReq product);

	public List<RespProduct> mapAllRespProduct(List<Product> products);

	public RespProduct mapRespProduct(Product product);

	public RespProduct mapProductOnly(Product product);

	public List<RespProduct> mapAllRespProductDetals(List<Product> products);

	public Variant mapVariant(VariantReq variantReq);

	public Specification mapSpecification(SpecificationReq specificationReq);

	public List<RespMinProduct> mapAllMinRespProductDetals(List<Product> products);

	public RespDetailProduct mapRespDetailsProduct(Product product);


}
