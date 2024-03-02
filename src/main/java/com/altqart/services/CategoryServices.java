package com.altqart.services;

import java.util.List;
import java.util.Map;

import com.altqart.model.Category;
import com.altqart.req.model.CategoryReq;
import com.altqart.resp.model.RespCategory;

public interface CategoryServices {

	public Category findByName(String name);

	public void addCategory(CategoryReq categoryReq, Map<String, Object> map);

	public List<Category> getAllCategories();

	public RespCategory getRespCategoryById(int id);

	public Iterable<Category> getCategoryList();

	public long getCount();

	public Category getCategoryByValue(String value);

	public List<RespCategory> getAllRespCategory();

	public void removeCategory(String id, Map<String, Object> map);

	public void updateCategory(CategoryReq categoryReq, Map<String, Object> map);

	public RespCategory getRespCategoryByValue(String value);

	public List<RespCategory> getAllSubRespCategoryById(int id);

}
