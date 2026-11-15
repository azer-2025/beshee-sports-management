package com.bishe.system.domain.sports;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.bishe.common.core.domain.BaseEntity;

/**
 * 器材预约借还对象 sp_equipment_borrow
 */
public class SpEquipmentBorrow extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 借还ID */
    private Long borrowId;

    /** 器材ID */
    private Long assetId;

    /** 器材名称（查询返回） */
    private String assetName;

    /** 器材编码（查询返回） */
    private String assetCode;

    /** 申请人ID */
    private Long userId;

    /** 用户名（查询返回） */
    private String userName;

    /** 申请数量 */
    private Integer quantity;

    /** 预约开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reserveStartTime;

    /** 预约结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reserveEndTime;

    /** 借出时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date borrowTime;

    /** 预计归还时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expectedReturnTime;

    /** 实际归还时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date actualReturnTime;

    /** 状态 */
    private String status;

    /** 驳回原因 */
    private String rejectReason;

    /** 审批人 */
    private String approvalBy;

    /** 审批时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date approvalTime;

    /** 逾期天数 */
    private Integer overdueDays;

    /** 逾期原因回填 */
    private String overdueReason;

    /** 质量评分(1-5) */
    private Integer reviewQualityScore;

    /** 包装评分(1-5) */
    private Integer reviewPackageScore;

    /** 服务评分(1-5) */
    private Integer reviewServiceScore;

    /** 评价内容 */
    private String reviewContent;

    /** 评价时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reviewTime;

    /** 评价人 */
    private String reviewBy;

    /** 仅看未回填原因（查询用） */
    private String pendingReasonOnly;

    /** 删除标志 */
    private String delFlag;

    public Long getBorrowId()
    {
        return borrowId;
    }

    public void setBorrowId(Long borrowId)
    {
        this.borrowId = borrowId;
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

    public String getAssetCode()
    {
        return assetCode;
    }

    public void setAssetCode(String assetCode)
    {
        this.assetCode = assetCode;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public Integer getQuantity()
    {
        return quantity;
    }

    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
    }

    public Date getReserveStartTime()
    {
        return reserveStartTime;
    }

    public void setReserveStartTime(Date reserveStartTime)
    {
        this.reserveStartTime = reserveStartTime;
    }

    public Date getReserveEndTime()
    {
        return reserveEndTime;
    }

    public void setReserveEndTime(Date reserveEndTime)
    {
        this.reserveEndTime = reserveEndTime;
    }

    public Date getBorrowTime()
    {
        return borrowTime;
    }

    public void setBorrowTime(Date borrowTime)
    {
        this.borrowTime = borrowTime;
    }

    public Date getExpectedReturnTime()
    {
        return expectedReturnTime;
    }

    public void setExpectedReturnTime(Date expectedReturnTime)
    {
        this.expectedReturnTime = expectedReturnTime;
    }

    public Date getActualReturnTime()
    {
        return actualReturnTime;
    }

    public void setActualReturnTime(Date actualReturnTime)
    {
        this.actualReturnTime = actualReturnTime;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getRejectReason()
    {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason)
    {
        this.rejectReason = rejectReason;
    }

    public String getApprovalBy()
    {
        return approvalBy;
    }

    public void setApprovalBy(String approvalBy)
    {
        this.approvalBy = approvalBy;
    }

    public Date getApprovalTime()
    {
        return approvalTime;
    }

    public void setApprovalTime(Date approvalTime)
    {
        this.approvalTime = approvalTime;
    }

    public Integer getOverdueDays()
    {
        return overdueDays;
    }

    public void setOverdueDays(Integer overdueDays)
    {
        this.overdueDays = overdueDays;
    }

    public String getOverdueReason()
    {
        return overdueReason;
    }

    public void setOverdueReason(String overdueReason)
    {
        this.overdueReason = overdueReason;
    }

    public String getPendingReasonOnly()
    {
        return pendingReasonOnly;
    }

    public void setPendingReasonOnly(String pendingReasonOnly)
    {
        this.pendingReasonOnly = pendingReasonOnly;
    }

    public Integer getReviewQualityScore()
    {
        return reviewQualityScore;
    }

    public void setReviewQualityScore(Integer reviewQualityScore)
    {
        this.reviewQualityScore = reviewQualityScore;
    }

    public Integer getReviewPackageScore()
    {
        return reviewPackageScore;
    }

    public void setReviewPackageScore(Integer reviewPackageScore)
    {
        this.reviewPackageScore = reviewPackageScore;
    }

    public Integer getReviewServiceScore()
    {
        return reviewServiceScore;
    }

    public void setReviewServiceScore(Integer reviewServiceScore)
    {
        this.reviewServiceScore = reviewServiceScore;
    }

    public String getReviewContent()
    {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent)
    {
        this.reviewContent = reviewContent;
    }

    public Date getReviewTime()
    {
        return reviewTime;
    }

    public void setReviewTime(Date reviewTime)
    {
        this.reviewTime = reviewTime;
    }

    public String getReviewBy()
    {
        return reviewBy;
    }

    public void setReviewBy(String reviewBy)
    {
        this.reviewBy = reviewBy;
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
            .append("borrowId", getBorrowId())
            .append("assetId", getAssetId())
            .append("assetName", getAssetName())
            .append("assetCode", getAssetCode())
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("quantity", getQuantity())
            .append("reserveStartTime", getReserveStartTime())
            .append("reserveEndTime", getReserveEndTime())
            .append("borrowTime", getBorrowTime())
            .append("expectedReturnTime", getExpectedReturnTime())
            .append("actualReturnTime", getActualReturnTime())
            .append("status", getStatus())
            .append("rejectReason", getRejectReason())
            .append("approvalBy", getApprovalBy())
            .append("approvalTime", getApprovalTime())
            .append("overdueDays", getOverdueDays())
            .append("overdueReason", getOverdueReason())
            .append("reviewQualityScore", getReviewQualityScore())
            .append("reviewPackageScore", getReviewPackageScore())
            .append("reviewServiceScore", getReviewServiceScore())
            .append("reviewContent", getReviewContent())
            .append("reviewTime", getReviewTime())
            .append("reviewBy", getReviewBy())
            .append("pendingReasonOnly", getPendingReasonOnly())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
