package com.penglecode.xmodule.myexample.service.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.common.util.ModelDecodeUtils;
import com.penglecode.xmodule.myexample.mapper.CategoryMapper;
import com.penglecode.xmodule.myexample.model.Category;
import com.penglecode.xmodule.myexample.service.CategoryService;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryMapper categoryServiceMapper;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void createCategory(Category parameter) {
		Assert.notNull(parameter, "参数不能为空");
		categoryServiceMapper.insertModel(parameter);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void updateCategory(Category parameter) {
		Assert.notNull(parameter, "参数不能为空");
		categoryServiceMapper.updateModelById(parameter);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void deleteCategoryById(Long id) {
		Assert.notNull(id, "id不能为空");
		categoryServiceMapper.deleteModelById(id);
	}

	@Override
	public Category getCategoryById(Long id) {
		return ModelDecodeUtils.decodeModel(categoryServiceMapper.selectModelById(id));
	}

	@Override
	public List<Category> getCategoryListByPage(Category condition, Page page, Sort sort) {
		List<Category> dataList = ModelDecodeUtils.decodeModel(categoryServiceMapper.selectModelPageListByExample(condition, sort, new RowBounds(page.getOffset(), page.getLimit())));
    	page.setTotalRowCount(categoryServiceMapper.countModelPageListByExample(condition));
		return dataList;
	}

	@Override
	public List<Category> getAllCategoryList() {
		return ModelDecodeUtils.decodeModel(categoryServiceMapper.selectAllModelList());
	}

}