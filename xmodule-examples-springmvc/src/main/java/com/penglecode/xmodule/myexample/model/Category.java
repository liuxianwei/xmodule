package com.penglecode.xmodule.myexample.model;

import com.penglecode.xmodule.common.codegen.Id;
import com.penglecode.xmodule.common.codegen.Model;
import com.penglecode.xmodule.common.support.BaseModel;

/**
 * 商品分类 (t_category) 实体类
 * 
 * @author PengPeng
 * @date	2019年03月04日 上午 10:16:25
 */
@Model(name="商品分类")
public class Category implements BaseModel<Category> {
     
    private static final long serialVersionUID = 1L;

    /** 分类ID */
    @Id
    private Long categoryId;

    /** 分类名称 */
    private String categoryName;

    /** 父分类ID */
    private Long parentCategoryId;

    /** create_time */
    private String createTime;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Long parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}