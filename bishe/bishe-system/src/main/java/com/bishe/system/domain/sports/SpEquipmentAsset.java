package com.bishe.system.domain.sports;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.bishe.common.core.domain.BaseEntity;

/**
 * 体育器材台账对象 sp_equipment_asset
 */
public class SpEquipmentAsset extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 器材ID */
    private Long assetId;

    /** 分类ID */
    private Long categoryId;

    /** 分类名称（查询返回） */
    private String categoryName;

    /** 器材大类（查询返回） */
    private String classType;

    /** 器材名称 */
    private String assetName;

    /** 器材编码 */
    private String assetCode;

    /** 条码 */
    private String barcode;

    /** 品牌 */
    private String brand;

    /** 采购日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date purchaseDate;

    /** 采购价 */
    private BigDecimal purchasePrice;

    /** 年折旧率 */
    private BigDecimal depreciationRate;

    /** 使用年限 */
    private Integer serviceLifeYear;

    /** 状态 */
    private String assetStatus;

    /** 审核状态 */
    private String auditStatus;

    /** 存放位置 */
    private String location;

    /** 总库存 */
    private Integer stockQty;

    /** 可用库存 */
    private Integer availableQty;

    /** 器材图片 */
    private String coverImage;

    /** 删除标志 */
    private String delFlag;

    public Long getAssetId()
    {
        return assetId;
    }

    public void setAssetId(Long assetId)
    {
        this.assetId = assetId;
    }

    public Long getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
    }

    public String getCategoryName()
    {
        return categoryName;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public String getClassType()
    {
        return classType;
    }

    public void setClassType(String classType)
    {
        this.classType = classType;
    }

    public String getAssetName()
    {
        return assetName;
    }

    public void setAssetName(String assetName)
    {
        this.assetName = assetName;
    }

    public String getAssetCode()
    {
        return assetCode;
    }

    public void setAssetCode(String assetCode)
    {
        this.assetCode = assetCode;
    }

    public String getBarcode()
    {
        return barcode;
    }

    public void setBarcode(String barcode)
    {
        this.barcode = barcode;
    }

    public String getBrand()
    {
        return brand;
    }

    public void setBrand(String brand)
    {
        this.brand = brand;
    }

    public Date getPurchaseDate()
    {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate)
    {
        this.purchaseDate = purchaseDate;
    }

    public BigDecimal getPurchasePrice()
    {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice)
    {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getDepreciationRate()
    {
        return depreciationRate;
    }

    public void setDepreciationRate(BigDecimal depreciationRate)
    {
        this.depreciationRate = depreciationRate;
    }

    public Integer getServiceLifeYear()
    {
        return serviceLifeYear;
    }

    public void setServiceLifeYear(Integer serviceLifeYear)
    {
        this.serviceLifeYear = serviceLifeYear;
    }

    public String getAssetStatus()
    {
        return assetStatus;
    }

    public void setAssetStatus(String assetStatus)
    {
        this.assetStatus = assetStatus;
    }

    public String getAuditStatus()
    {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus)
    {
        this.auditStatus = auditStatus;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public Integer getStockQty()
    {
        return stockQty;
    }

    public void setStockQty(Integer stockQty)
    {
        this.stockQty = stockQty;
    }

    public Integer getAvailableQty()
    {
        return availableQty;
    }

    public void setAvailableQty(Integer availableQty)
    {
        this.availableQty = availableQty;
    }

    public String getCoverImage()
    {
        return coverImage;
    }

    public void setCoverImage(String coverImage)
    {
        this.coverImage = coverImage;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("assetId", getAssetId())
            .append("categoryId", getCategoryId())
            .append("categoryName", getCategoryName())
            .append("classType", getClassType())
            .append("assetName", getAssetName())
            .append("assetCode", getAssetCode())
            .append("barcode", getBarcode())
            .append("brand", getBrand())
            .append("purchaseDate", getPurchaseDate())
            .append("purchasePrice", getPurchasePrice())
            .append("depreciationRate", getDepreciationRate())
            .append("serviceLifeYear", getServiceLifeYear())
            .append("assetStatus", getAssetStatus())
            .append("auditStatus", getAuditStatus())
            .append("location", getLocation())
            .append("stockQty", getStockQty())
            .append("availableQty", getAvailableQty())
            .append("coverImage", getCoverImage())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
