package com.penglecode.xmodule.myexample.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.common.support.ValidationAssert;
import com.penglecode.xmodule.common.util.ModelDecodeUtils;
import com.penglecode.xmodule.myexample.mapper.CategoryMapper;
import com.penglecode.xmodule.myexample.model.Category;
import com.penglecode.xmodule.myexample.service.CategoryService;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryMapper categoryMapper;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void createCategory(Category parameter) {
		ValidationAssert.notNull(parameter, "参数不能为空");
		categoryMapper.insertModel(parameter);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void updateCategory(Category parameter) {
		ValidationAssert.notNull(parameter, "参数不能为空");
		Map<String, Object> paramMap = parameter.mapBuilder()
												.withCategoryId()
												.withCategoryName()
												.withCreateTime()
												.withParentCategoryId()
												.build();
		categoryMapper.updateModelById(parameter.getCategoryId(), paramMap);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void deleteCategoryById(Long id) {
		ValidationAssert.notNull(id, "id不能为空");
		categoryMapper.deleteModelById(id);
	}

	@Override
	public Category getCategoryById(Long id) {
		return ModelDecodeUtils.decodeModel(categoryMapper.selectModelById(id));
	}

	@Override
	public List<Category> getCategoryListByPage(Category condition, Page page, Sort sort) {
		List<Category> dataList = ModelDecodeUtils.decodeModel(categoryMapper.selectModelPageListByExample(condition, sort, new RowBounds(page.getOffset(), page.getLimit())));
    	page.setTotalRowCount(categoryMapper.countModelPageListByExample(condition));
		return dataList;
	}

	@Override
	public List<Category> getAllCategoryList() {
		return ModelDecodeUtils.decodeModel(categoryMapper.selectAllModelList());
	}

}