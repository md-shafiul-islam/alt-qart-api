package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.BrandMapper;
import com.altqart.mapper.CategoryMapper;
import com.altqart.mapper.ColorMapper;
import com.altqart.mapper.ImageGalleryMapper;
import com.altqart.mapper.MaterialMapper;
import com.altqart.mapper.MeasurementMapper;
import com.altqart.mapper.MetaDataMapper;
import com.altqart.mapper.ProductMapper;
import com.altqart.mapper.RatingMapper;
import com.altqart.mapper.SizeMapper;
import com.altqart.mapper.SpecificationMapper;
import com.altqart.model.ImageGallery;
import com.altqart.model.MeasurementStandard;
import com.altqart.model.Product;
import com.altqart.model.ProductDescription;
import com.altqart.model.Size;
import com.altqart.model.Specification;
import com.altqart.model.Variant;
import com.altqart.req.model.ImageGalleryReq;
import com.altqart.req.model.ProductDescriptionReq;
import com.altqart.req.model.ProductReq;
import com.altqart.req.model.SpecificationReq;
import com.altqart.req.model.VariantReq;
import com.altqart.resp.model.RespColor;
import com.altqart.resp.model.RespDetailProduct;
import com.altqart.resp.model.RespItemColor;
import com.altqart.resp.model.RespItemSize;
import com.altqart.resp.model.RespMeasurementStandard;
import com.altqart.resp.model.RespMinProduct;
import com.altqart.resp.model.RespMinVariant;
import com.altqart.resp.model.RespProduct;
import com.altqart.resp.model.RespProductDescription;
import com.altqart.resp.model.RespSize;
import com.altqart.resp.model.RespVariant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductMapperImpl implements ProductMapper {

	@Autowired
	private CategoryMapper categoryMapper;

	@Autowired
	private BrandMapper brandMapper;

	@Autowired
	private MaterialMapper materialMapper;

	@Autowired
	private RatingMapper ratingMapper;

	@Autowired
	private MeasurementMapper measurementMapper;

	@Autowired
	private SpecificationMapper specificationMapper;

	@Autowired
	private MetaDataMapper metaDataMapper;

	@Autowired
	private ImageGalleryMapper imageGalleryMapper;

	@Autowired
	private HelperServices helperServices;

	@Autowired
	private ColorMapper colorMapper;

	@Autowired
	private SizeMapper sizeMapper;

	@Override
	public Product mapProductReq(ProductReq productReq) {

		if (productReq != null) {

			Product product = new Product();

			product.setAliasName(product.getAliasName());
			product.setDiscountStatus(productReq.isDiscountStatus());
			product.setTitle(productReq.getTitle());
			product.setBnTitle(productReq.getBnTitle());
			product.setUpcoming(productReq.isUpcoming());

			product.setImages(mapProductImage(productReq.getImages()));

			product.setMetaDatas(metaDataMapper.mapProductMetaDatas(productReq.getMetaDatas()));

			product.setDescriptions(mapProductDescriptions(productReq.getDescritions()));

			return product;

		}

		return null;
	}

	private List<ProductDescription> mapProductDescriptions(List<ProductDescriptionReq> descritions) {

		if (descritions != null) {

			List<ProductDescription> descriptions = new ArrayList<>();

			for (ProductDescriptionReq productDescriptionReq : descritions) {

				ProductDescription description = new ProductDescription();
				description.setContent(productDescriptionReq.getContent());
				description.setLang(productDescriptionReq.getLang());

				descriptions.add(description);
			}

			return descriptions;
		}

		return null;
	}

	private Set<ImageGallery> mapProductImage(List<ImageGalleryReq> images) {

		if (images != null) {

			Set<ImageGallery> galleries = new HashSet<>();

			for (ImageGalleryReq imageGalleryReq : images) {
				ImageGallery gallery = new ImageGallery();
				gallery.setAltTag(imageGalleryReq.getAltTag());
				gallery.setId(imageGalleryReq.getId());
				gallery.setLocation(imageGalleryReq.getLocation());
				gallery.setName(imageGalleryReq.getName());
				gallery.setTitle(imageGalleryReq.getTitle());

				galleries.add(gallery);
			}

			return galleries;
		}

		return null;
	}

	@Override
	public List<RespProduct> mapAllRespProduct(List<Product> products) {

		if (products != null) {
			List<RespProduct> respProducts = new ArrayList<>();

			for (Product product : products) {
				RespProduct respProduct = mapProductOnly(product);

				if (respProduct != null) {
					respProducts.add(respProduct);
				}
			}

			return respProducts;
		}

		return null;
	}

	@Override
	public RespProduct mapRespProduct(Product product) {

		if (product != null) {
			RespProduct respProduct = mapProductOnly(product);

			if (respProduct != null) {

				respProduct.setDescriptions(mapProductDescription(product.getDescriptions()));

				respProduct.setImages(imageGalleryMapper.mapAllRespImage(product.getImages()));

				respProduct.setMaterials(materialMapper.mapAllRespMaterialOnly(product.getMaterials()));

				respProduct.setMeasurement(measurementMapper.mapRespMeasurementOnly(product.getMeasurement()));

				respProduct.setMetaDatas(metaDataMapper.mapProductRespMetaDatas(product.getMetaDatas()));

				respProduct.setRatings(ratingMapper.mapAllRespRating(product.getRatings()));

				respProduct.setSpecifications(specificationMapper.mapRespSpecifications(product.getSpecifications()));

				respProduct.setVariants(mapVariants(product.getVariants()));

			}

			return respProduct;
		}

		return null;
	}

	@Override
	public RespDetailProduct mapRespDetailsProduct(Product product) {

		if (product != null) {

			RespDetailProduct detailProduct = new RespDetailProduct();

			detailProduct.setAliasName(product.getAliasName());
			detailProduct.setAvgRating(product.getAvgRating());

			detailProduct.setBnTitle(product.getBnTitle());
			detailProduct.setTitle(product.getTitle());
			detailProduct.setId(product.getPublicId());

			detailProduct.setCreateDate(product.getCreateDate());
			detailProduct.setDiscountStatus(product.isDiscountStatus());
			detailProduct.setUpcoming(product.isUpcoming());
			detailProduct.setUpdateDate(product.getUpdateDate());
			detailProduct.setVideoUrl(product.getVideoUrl());

			if (product.getCategory() != null) {
				detailProduct.setCategory(categoryMapper.mapRespCategoryOnly(product.getCategory()));

			}

			if (product.getBrand() != null) {
				detailProduct.setBrand(brandMapper.mapBrandOnly(product.getBrand()));
			}

			detailProduct.setDescriptions(mapProductDescription(product.getDescriptions()));

			detailProduct.setImages(imageGalleryMapper.mapAllRespImage(product.getImages()));

			detailProduct.setMaterials(materialMapper.mapAllRespMaterialOnly(product.getMaterials()));

			detailProduct.setMeasurement(measurementMapper.mapRespMeasurementOnly(product.getMeasurement()));

			detailProduct.setMetaDatas(metaDataMapper.mapProductRespMetaDatas(product.getMetaDatas()));

			detailProduct.setRatings(ratingMapper.mapAllRespRating(product.getRatings()));

			detailProduct.setSpecifications(specificationMapper.mapRespSpecifications(product.getSpecifications()));

			detailProduct.setColors(colorMapper.mapItemColor(product.getItemColors()));

			mapVariantsAndSizesColors(product.getVariants(), detailProduct);

			return detailProduct;

		}

		return null;
	}

	private void mapVariantsAndSizesColors(List<Variant> variants, RespDetailProduct detailProduct) {

		if (variants != null) {
			List<RespVariant> respVariants = new ArrayList<>();

			Set<RespSize> respSizes = new HashSet<>();
			int count = 0;

			for (Variant variant : variants) {
				RespVariant respVariant = mapRespVariantOnly(variant);

				if (respVariant != null) {

					if (count == 0) {
						detailProduct.setVariant(respVariant);
						if (variant.getSize() != null) {
							detailProduct.setStandard(mapRespMeasurementStandard(variant.getSize().getStandard()));
						}

					}

					if (variant.isFeature()) {
						detailProduct.setVariant(respVariant);
						if (variant.getSize() != null) {
							detailProduct.setStandard(mapRespMeasurementStandard(variant.getSize().getStandard()));
						}
					}

					RespSize itemSize = sizeMapper.mapRespSizeOnly(variant.getSize());

					if (itemSize != null) {
						respSizes.add(itemSize);
					}

					respVariants.add(respVariant);
				}
				count++;
			}
			detailProduct.setSizes(respSizes);
			detailProduct.setVariants(respVariants);
		}

	}

	private RespMeasurementStandard mapRespMeasurementStandard(MeasurementStandard standard) {

		if (standard != null) {

			RespMeasurementStandard measurementStandard = new RespMeasurementStandard();
			measurementStandard.setId(standard.getId());
			measurementStandard.setName(standard.getName());
			measurementStandard.setValue(standard.getKey());

			return measurementStandard;
		}
		return null;
	}

	@Override
	public List<RespMinProduct> mapAllMinRespProductDetals(List<Product> products) {

		if (products != null) {

			List<RespMinProduct> minProducts = new ArrayList<>();

			for (Product product : products) {
				RespMinProduct minProduct = mapMinProduct(product);

				if (minProduct != null) {
					minProducts.add(minProduct);
				}
			}
			return minProducts;
		}

		return null;
	}

	private RespMinProduct mapMinProduct(Product product) {

		if (product != null) {

			RespMinProduct minProduct = new RespMinProduct();
			minProduct.setAliasName(product.getAliasName());
			minProduct.setAvgRating(product.getAvgRating());
			minProduct.setBnTitle(product.getBnTitle());
			minProduct.setDiscountStatus(product.isDiscountStatus());
			minProduct.setId(product.getPublicId());
			minProduct.setTitle(product.getTitle());
			minProduct.setUpcoming(product.isUpcoming());
			minProduct.setUpdateDate(product.getUpdateDate());

			if (product.getBrand() != null) {
				minProduct.setBrand(product.getBrand().getName());
			}

			if (product.getDescriptions() != null) {
				List<RespProductDescription> descriptions = new ArrayList<>();

				for (ProductDescription item : product.getDescriptions()) {
					RespProductDescription description = mapProductDescriptionShort(item);

					if (description != null) {
						descriptions.add(description);
					}
				}
				minProduct.setDescriptions(descriptions);
			}

			if (product.getVariants() != null) {
				minProduct.setVariant(getSingleVarianViaMinProduct(product.getVariants()));
			}

			return minProduct;

		}

		return null;
	}

	private RespMinVariant getSingleVarianViaMinProduct(List<Variant> variants) {

		if (variants != null) {

			RespMinVariant respVariant = null;

			int count = 0;
			for (Variant variant : variants) {

				if (count == 0) {
					respVariant = mapMinRespVariantOnly(variant);
				} else if (variant.isAvailable()) {
					respVariant = mapMinRespVariantOnly(variant);
				}
				count++;
			}

			return respVariant;
		}
		return null;
	}

	private RespMinVariant mapMinRespVariantOnly(Variant variant) {

		if (variant != null) {
			RespMinVariant respVariant = new RespMinVariant();
			respVariant.setAvailable(variant.isAvailable());
			respVariant.setBarCode(variant.getBarCode());

			if (variant.getColor() != null) {
				respVariant.setColor(variant.getColor().getName());
			}

			respVariant.setDicount(variant.isDicount());
			respVariant.setDicountPrice(variant.getDicountPrice());
			respVariant.setFeature(variant.isFeature());
			respVariant.setFreeItems(variant.getFreeItems());
			respVariant.setId(variant.getPublicId());
			respVariant.setImageUrl(variant.getImageUrl());
			respVariant.setPrice(variant.getPrice());
			respVariant.setQty(variant.getQty());

			if (variant.getSize() != null) {
				respVariant.setSize(variant.getSize().getName());
			}

			respVariant.setSku(variant.getSku());

			return respVariant;
		}

		return null;
	}

	private RespProductDescription mapProductDescriptionShort(ProductDescription item) {

		if (item != null) {
			RespProductDescription description = new RespProductDescription();
			description.setContent(helperServices.getShortText(item.getContent(), 60));
			description.setLang(item.getLang());
			description.setId(item.getId());

			return description;

		}

		return null;
	}

	private List<RespVariant> mapVariants(List<Variant> variants) {

		if (variants != null) {
			List<RespVariant> respVariants = new ArrayList<>();

			for (Variant variant : variants) {
				RespVariant respVariant = mapRespVariantOnly(variant);

				if (respVariant != null) {
					respVariants.add(respVariant);
				}
			}

			return respVariants;
		}
		return null;
	}

	public RespVariant mapRespVariantOnly(Variant variant) {

		if (variant != null) {
			RespVariant respVariant = new RespVariant();
			respVariant.setAvailable(variant.isAvailable());
			respVariant.setBarCode(variant.getBarCode());
			respVariant.setDiscount(variant.isDicount());
			respVariant.setDiscountPrice(variant.getDicountPrice());
			respVariant.setFreeItems(variant.getFreeItems());
			respVariant.setId(variant.getPublicId());
			respVariant.setImageUrl(variant.getImageUrl());
			respVariant.setPrice(variant.getPrice());
			respVariant.setQty(variant.getQty());

			if (variant.getSize() != null) {
				respVariant.setSize(mapRespSizeOnly(variant.getSize()));
			}

			if (variant.getColor() != null) {
				RespColor color = colorMapper.mapRespColor(variant.getColor());
				respVariant.setColor(color);

			}

			respVariant.setSku(variant.getSku());
			respVariant.setFeature(variant.isFeature());

			return respVariant;
		}

		return null;
	}

	private RespSize mapRespSize(Size size) {

		if (size != null) {

			RespSize respSize = new RespSize();
			respSize.setCount(size.getCount());
			respSize.setId(size.getId());
			respSize.setName(size.getName());
			respSize.setValue(size.getSKey());

			if (size.getStandard() != null) {
				respSize.setStandard(mapRespStandard(size.getStandard()));
			}
			return respSize;
		}

		return null;
	}

	private RespSize mapRespSizeOnly(Size size) {

		if (size != null) {

			RespSize respSize = new RespSize();
			respSize.setCount(size.getCount());
			respSize.setId(size.getId());
			respSize.setName(size.getName());
			respSize.setValue(size.getSKey());

			return respSize;
		}

		return null;
	}

	private RespMeasurementStandard mapRespStandard(MeasurementStandard standard) {

		if (standard != null) {
			RespMeasurementStandard measurementStandard = new RespMeasurementStandard();
			measurementStandard.setId(standard.getId());
			measurementStandard.setName(standard.getName());
			measurementStandard.setValue(standard.getKey());

			return measurementStandard;
		}

		return null;
	}

	private List<RespProductDescription> mapProductDescription(List<ProductDescription> descriptions) {

		if (descriptions != null) {
			List<RespProductDescription> productDescriptions = new ArrayList<>();

			for (ProductDescription productDescription : descriptions) {

				RespProductDescription description = mapRespProductDescriptionOnly(productDescription);

				if (description != null) {
					productDescriptions.add(description);
				}
			}

			return productDescriptions;
		}

		return null;
	}

	public RespProductDescription mapRespProductDescriptionOnly(ProductDescription productDescription) {

		if (productDescription != null) {
			RespProductDescription description = new RespProductDescription();
			description.setContent(productDescription.getContent());
			description.setId(productDescription.getId());
			description.setLang(productDescription.getLang());

			return description;
		}

		return null;
	}

	@Override
	public List<RespProduct> mapAllRespProductDetals(List<Product> products) {
		if (products != null) {
			List<RespProduct> respProducts = new ArrayList<>();

			for (Product product : products) {
				RespProduct respProduct = mapRespProduct(product);

				if (respProduct != null) {
					respProducts.add(respProduct);
				}
			}

			return respProducts;
		}
		return null;
	}

	@Override
	public RespProduct mapProductOnly(Product product) {

		if (product != null) {

			RespProduct reProduct = new RespProduct();
			reProduct.setBnTitle(product.getBnTitle());
			reProduct.setTitle(product.getTitle());
			reProduct.setId(product.getPublicId());
			reProduct.setAliasName(product.getAliasName());
			reProduct.setAvgRating(product.getAvgRating());

			reProduct.setTitle(product.getTitle());
			reProduct.setCreateDate(product.getCreateDate());
			reProduct.setDiscountStatus(product.isDiscountStatus());
			reProduct.setUpcoming(product.isUpcoming());
			reProduct.setUpdateDate(product.getUpdateDate());
			reProduct.setVideoUrl(product.getVideoUrl());

			if (product.getCategory() != null) {
				reProduct.setCategory(categoryMapper.mapRespCategoryOnly(product.getCategory()));

			}

			if (product.getBrand() != null) {
				reProduct.setBrand(brandMapper.mapBrandOnly(product.getBrand()));
			}

			return reProduct;
		}

		return null;
	}

	@Override
	public Variant mapVariant(VariantReq variantReq) {
		if (variantReq != null) {
			Variant variant = new Variant();
			variant.setAvailable(true);
			variant.setPublicId(helperServices.getGenPublicId());
			variant.setDicount(variantReq.isDicount());
			variant.setDicountPrice(variantReq.getDicountPrice());
			variant.setFreeItems(variantReq.getFreeItems());
			variant.setImageUrl(variantReq.getImageUrl());
			variant.setPrice(variantReq.getPrice());
			variant.setQty(variantReq.getQty());

			variant.setSku(variantReq.getSku());

			return variant;
		}
		return null;
	}

	@Override
	public Specification mapSpecification(SpecificationReq specificationReq) {

		if (specificationReq != null) {
			Specification specification = new Specification();
			specification.setDescription(specificationReq.getDescription());
			specification.setFeature(specificationReq.isFeature());
			specification.setValue(specificationReq.getValue());

			return specification;
		}
		return null;
	}

}
