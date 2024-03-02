package com.altqart.mapper;

import java.util.List;

import com.altqart.model.Category;
import com.altqart.req.model.CategoryReq;
import com.altqart.resp.model.RespCategory;
import com.altqart.resp.model.SelectOption;

public interface CategoryMapper {

	public List<RespCategory> mapAllRespCategory(List<Category> categories);

	public RespCategory mapRespCategory(Category category);

	public Category mapCategory(CategoryReq categoryReq);

	public List<SelectOption> mapCategoryOptions(List<Category> categories);

	public List<RespCategory> mapAllRespCategoryOnly(List<Category> categories);

	public Category mapCategoryImReq(List<CategoryReq> cats);

	public RespCategory mapRespCategoryOnly(Category category);

}
