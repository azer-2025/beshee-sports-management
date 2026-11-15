package com.bishe.system.domain.sports;

import java.io.Serializable;
import com.bishe.common.annotation.Excel;

/**
 * Procurement/scrap suggestion report row.
 */
public class SpSuggestionReport implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "统计月份")
    private String statMonth;

    @Excel(name = "器材ID")
    private Long assetId;

    @Excel(name = "器材名称")
    private String assetName;

    @Excel(name = "分类")
    private String categoryName;

    @Excel(name = "总库存")
    private Integer stockQty;

    @Excel(name = "可用库存")
    private Integer availableQty;

    @Excel(name = "月借阅量")
    private Integer monthBorrowCount;

    @Excel(name = "月报修量")
    private Integer monthRepairCount;

    @Excel(name = "累计报修量")
    private Integer totalRepairCount;

    @Excel(name = "已用年限")
    private Integer usedYears;

    @Excel(name = "累计折旧率(%)")
    private Double accumulatedDepRate;

    @Excel(name = "建议结论")
    private String suggestion;

    @Excel(name = "建议依据")
    private String suggestionReason;

    public String getStatMonth()
    {
        return statMonth;
    }

    public void setStatMonth(String statMonth)
    {
        this.statMonth = statMonth;
    }

    public Long getAssetId()
    {
        return assetId;
    }

    public void setAssetId(Long assetId)
    {
        this.assetId = assetId;
    }

    public String getAssetName()
    {
        return assetName;
    }

    public void setAssetName(String assetName)
    {
        this.assetName = assetName;
    }

    public String getCategoryName()
    {
        return categoryName;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
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

    public Integer getMonthBorrowCount()
    {
        return monthBorrowCount;
    }

    public void setMonthBorrowCount(Integer monthBorrowCount)
    {
        this.monthBorrowCount = monthBorrowCount;
    }

    public Integer getMonthRepairCount()
    {
        return monthRepairCount;
    }

    public void setMonthRepairCount(Integer monthRepairCount)
    {
        this.monthRepairCount = monthRepairCount;
    }

    public Integer getTotalRepairCount()
    {
        return totalRepairCount;
    }

    public void setTotalRepairCount(Integer totalRepairCount)
    {
        this.totalRepairCount = totalRepairCount;
    }

    public Integer getUsedYears()
    {
        return usedYears;
    }

    public void setUsedYears(Integer usedYears)
    {
        this.usedYears = usedYears;
    }

    public Double getAccumulatedDepRate()
    {
        return accumulatedDepRate;
    }

    public void setAccumulatedDepRate(Double accumulatedDepRate)
    {
        this.accumulatedDepRate = accumulatedDepRate;
    }

    public String getSuggestion()
    {
        return suggestion;
    }

    public void setSuggestion(String suggestion)
    {
        this.suggestion = suggestion;
    }

    public String getSuggestionReason()
    {
        return suggestionReason;
    }

    public void setSuggestionReason(String suggestionReason)
    {
        this.suggestionReason = suggestionReason;
    }
}
