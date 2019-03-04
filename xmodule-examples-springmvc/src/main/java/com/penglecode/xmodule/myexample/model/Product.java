package com.penglecode.xmodule.myexample.model;

import com.penglecode.xmodule.common.codegen.Id;
import com.penglecode.xmodule.common.codegen.Model;
import com.penglecode.xmodule.common.support.BaseModel;

/**
 * 商品信息 (t_product) 实体类
 * 
 * @author PengPeng
 * @date	2019年03月04日 上午 10:16:24
 */
@Model(name="商品信息")
public class Product implements BaseModel<Product> {
     
    private static final long serialVersionUID = 1L;

    /** 商品ID */
    @Id
    private Long productId;

    /** 商品名称 */
    private String productName;

    /** 商品分类：1-实物商品，0-虚拟商品 */
    private Integer productType;

    /** 商品单价 */
    private Double unitPrice;

    /** 商品库存 */
    private Integer inventory;

    /** 商品主要分类 */
    private Long mainCategoryId;

    /** 创建时间 */
    private String createTime;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public Long getMainCategoryId() {
        return mainCategoryId;
    }

    public void setMainCategoryId(Long mainCategoryId) {
        this.mainCategoryId = mainCategoryId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}