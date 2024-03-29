package com.penglecode.xmodule.myexample.model;

import com.penglecode.xmodule.common.codegen.Id;
import com.penglecode.xmodule.common.codegen.Model;
import com.penglecode.xmodule.common.support.BaseModel;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 商品分类 (t_category) 实体类
 * 
 * @author Mybatis-Generator
 * @date	2019年06月27日 下午 13:31:54
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

    public MapBuilder mapBuilder() {
        return new MapBuilder();
    }

    /**
     * Auto generated by Mybatis Generator
     */
    public class MapBuilder {
         
        private final Map<String, Object> modelProperties = new LinkedHashMap<String,Object>();

        MapBuilder() {
            
        }

        public MapBuilder withCategoryId(Long ... categoryId) {
            modelProperties.put("categoryId", BaseModel.first(categoryId, getCategoryId()));
            return this;
        }

        public MapBuilder withCategoryName(String ... categoryName) {
            modelProperties.put("categoryName", BaseModel.first(categoryName, getCategoryName()));
            return this;
        }

        public MapBuilder withParentCategoryId(Long ... parentCategoryId) {
            modelProperties.put("parentCategoryId", BaseModel.first(parentCategoryId, getParentCategoryId()));
            return this;
        }

        public MapBuilder withCreateTime(String ... createTime) {
            modelProperties.put("createTime", BaseModel.first(createTime, getCreateTime()));
            return this;
        }

        public Map<String, Object> build() {
            return modelProperties;
        }
    }
}