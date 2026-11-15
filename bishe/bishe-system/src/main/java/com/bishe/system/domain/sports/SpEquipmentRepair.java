package com.bishe.system.domain.sports;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.bishe.common.core.domain.BaseEntity;

/**
 * 器材报修对象 sp_equipment_repair
 */
public class SpEquipmentRepair extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 报修ID */
    private Long repairId;

    /** 器材ID */
    private Long assetId;

    /** 器材名称（查询返回） */
    private String assetName;

    /** 报修人ID */
    private Long userId;

    /** 用户名（查询返回） */
    private String userName;

    /** 故障描述 */
    private String faultDesc;

    /** 故障图片 */
    private String faultImage;

    /** 损耗原因 */
    private String faultReason;

    /** 紧急程度 */
    private String urgency;

    /** 状态 */
    private String status;

    /** 处理反馈 */
    private String processNote;

    /** 维修处理人 */
    private String repairBy;

    /** 受理时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date repairTime;

    /** 完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finishTime;

    /** 删除标志 */
    private String delFlag;

    public Long getRepairId()
    {
        return repairId;
    }

    public void setRepairId(Long repairId)
    {
        this.repairId = repairId;
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

    public String getFaultDesc()
    {
        return faultDesc;
    }

    public void setFaultDesc(String faultDesc)
    {
        this.faultDesc = faultDesc;
    }

    public String getFaultImage()
    {
        return faultImage;
    }

    public void setFaultImage(String faultImage)
    {
        this.faultImage = faultImage;
    }

    public String getFaultReason()
    {
        return faultReason;
    }

    public void setFaultReason(String faultReason)
    {
        this.faultReason = faultReason;
    }

    public String getUrgency()
    {
        return urgency;
    }

    public void setUrgency(String urgency)
    {
        this.urgency = urgency;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getProcessNote()
    {
        return processNote;
    }

    public void setProcessNote(String processNote)
    {
        this.processNote = processNote;
    }

    public String getRepairBy()
    {
        return repairBy;
    }

    public void setRepairBy(String repairBy)
    {
        this.repairBy = repairBy;
    }

    public Date getRepairTime()
    {
        return repairTime;
    }

    public void setRepairTime(Date repairTime)
    {
        this.repairTime = repairTime;
    }

    public Date getFinishTime()
    {
        return finishTime;
    }

    public void setFinishTime(Date finishTime)
    {
        this.finishTime = finishTime;
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
            .append("repairId", getRepairId())
            .append("assetId", getAssetId())
            .append("assetName", getAssetName())
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("faultDesc", getFaultDesc())
            .append("faultImage", getFaultImage())
            .append("faultReason", getFaultReason())
            .append("urgency", getUrgency())
            .append("status", getStatus())
            .append("processNote", getProcessNote())
            .append("repairBy", getRepairBy())
            .append("repairTime", getRepairTime())
            .append("finishTime", getFinishTime())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
