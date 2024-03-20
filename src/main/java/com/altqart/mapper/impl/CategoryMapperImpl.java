package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.altqart.mapper.CategoryMapper;
import com.altqart.model.Category;
import com.altqart.req.model.CategoryReq;
import com.altqart.resp.model.RespCategory;
import com.altqart.resp.model.SelectOption;

@Service
public class CategoryMapperImpl implements CategoryMapper {

	@Override
	public List<RespCategory> mapAllRespCategory(List<Category> categories) {

		if (categories != null) {
			List<RespCategory> respCategories = new ArrayList<>();

			for (Category category : categories) {
				RespCategory respCategory = mapRespCategoryOnly(category);

				if (respCategory != null) {
					respCategories.add(respCategory);
				}

			}

			return respCategories;
		}

		return null;
	}

	@Override
	public RespCategory mapRespCategoryOnly(Category category) {
		if (category != null) {
			RespCategory respCategory = new RespCategory();
			respCategory.setDescription(category.getDescription());
			respCategory.setName(category.getName());
			respCategory.setSubCat(category.isSub());
			respCategory.setId(category.getId());
			respCategory.setValue(category.getValue());
			return respCategory;
		}

		return null;
	}

	@Override
	public RespCategory mapRespCategory(Category category) {

		if (category != null) {
			RespCategory respCategory = mapRespCategoryOnly(category);

			if (respCategory != null) {

				if (category.getParent() != null) {
					respCategory.setParent(mapRespCategoryOnly(category.getParent()));
				}

				if (category.isSub()) {

					respCategory.setSubCats(mapAllRespCategoryOnly(category.getSubCategories()));
				}
			}

			return respCategory;
		}

		return null;
	}

	@Override
	public Category mapCategory(CategoryReq categoryReq) {
		if (categoryReq != null) {

			Category category = new Category();

			category.setDescription(categoryReq.getDescription());
			category.setName(categoryReq.getName());
			category.setValue(categoryReq.getValue());

			return category;
		}
		return null;
	}

	@Override
	public List<SelectOption> mapCategoryOptions(List<Category> categories) {

		return null;
	}

	@Override
	public List<RespCategory> mapAllRespCategoryOnly(List<Category> categories) {

		if (categories != null) {
			List<RespCategory> respCategories = new ArrayList<>();

			for (Category category : categories) {
				RespCategory respCategory = mapRespCategoryOnly(category);

				if (respCategory != null) {
					respCategories.add(respCategory);
				}

			}

			return respCategories;
		}

		return null;
	}

	@Override
	public Category mapCategoryImReq(List<CategoryReq> cats) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void mapAllCatAndSubCatIds(Category category, List<Integer> ids) {

		getAllCategoryIds(category, ids);

	}

	private void getAllCategoryIds(Category category, List<Integer> ids) {

		if (category.isSub()) {
			for (Category subCat : category.getSubCategories()) {

				ids.add(subCat.getId());
				if (subCat.isSub()) {
					getAllCategoryIds(subCat, ids);
				}
			}
		} else {
			ids.add(category.getId());
		}

	}

}
