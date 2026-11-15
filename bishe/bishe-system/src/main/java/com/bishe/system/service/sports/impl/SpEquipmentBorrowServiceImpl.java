package com.bishe.system.service.sports.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bishe.common.exception.ServiceException;
import com.bishe.common.utils.StringUtils;
import com.bishe.system.domain.sports.SpEquipmentAsset;
import com.bishe.system.domain.sports.SpEquipmentBorrow;
import com.bishe.system.mapper.sports.SpEquipmentBorrowMapper;
import com.bishe.system.service.sports.ISpEquipmentAssetService;
import com.bishe.system.service.sports.ISpEquipmentBorrowService;

/**
 * 器材借还Service实现
 */
@Service
public class SpEquipmentBorrowServiceImpl implements ISpEquipmentBorrowService
{
    @Autowired
    private SpEquipmentBorrowMapper borrowMapper;

    @Autowired
    private ISpEquipmentAssetService assetService;

    @Override
    public SpEquipmentBorrow selectBorrowById(Long borrowId)
    {
        return borrowMapper.selectBorrowById(borrowId);
    }

    @Override
    public List<SpEquipmentBorrow> selectBorrowList(SpEquipmentBorrow borrow)
    {
        refreshOverdueStatus();
        return borrowMapper.selectBorrowList(borrow);
    }

    @Override
    public List<SpEquipmentBorrow> selectMyBorrowList(SpEquipmentBorrow borrow)
    {
        refreshOverdueStatus();
        return borrowMapper.selectMyBorrowList(borrow);
    }

    @Override
    public List<SpEquipmentBorrow> selectOverdueBorrowList(SpEquipmentBorrow borrow)
    {
        refreshOverdueStatus();
        return borrowMapper.selectOverdueBorrowList(borrow);
    }

    @Override
    public Map<String, Object> selectOverdueDashboard()
    {
        refreshOverdueStatus();
        return borrowMapper.selectOverdueDashboard();
    }

    @Override
    public List<Map<String, Object>> selectOverdueReasonStats()
    {
        refreshOverdueStatus();
        return borrowMapper.selectOverdueReasonStats();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int applyBorrow(SpEquipmentBorrow borrow)
    {
        if (borrow.getAssetId() == null)
        {
            throw new ServiceException("请选择要预约的器材");
        }
        if (borrow.getQuantity() == null || borrow.getQuantity() <= 0)
        {
            throw new ServiceException("预约数量必须大于0");
        }
        SpEquipmentAsset asset = assetService.selectAssetById(borrow.getAssetId());
        if (asset == null)
        {
            throw new ServiceException("器材不存在或已删除");
        }
        if (!"1".equals(asset.getAuditStatus()))
        {
            throw new ServiceException("该器材未通过审核，暂不可预约");
        }
        if ("2".equals(asset.getAssetStatus()) || "3".equals(asset.getAssetStatus()) || "4".equals(asset.getAssetStatus()))
        {
            throw new ServiceException("该器材当前不可预约");
        }
        if (asset.getAvailableQty() == null || asset.getAvailableQty() < borrow.getQuantity())
        {
            throw new ServiceException("库存不足，当前可用库存：" + (asset.getAvailableQty() == null ? 0 : asset.getAvailableQty()));
        }
        if (borrow.getExpectedReturnTime() == null && borrow.getReserveEndTime() != null)
        {
            borrow.setExpectedReturnTime(borrow.getReserveEndTime());
        }
        borrow.setStatus("0");
        return borrowMapper.insertBorrow(borrow);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int approveBorrow(SpEquipmentBorrow borrow)
    {
        SpEquipmentBorrow current = borrowMapper.selectBorrowById(borrow.getBorrowId());
        if (current == null)
        {
            throw new ServiceException("借还记录不存在");
        }
        if (!"0".equals(current.getStatus()))
        {
            throw new ServiceException("仅待审批记录可执行审批");
        }
        if ("1".equals(borrow.getStatus()))
        {
            SpEquipmentAsset asset = assetService.selectAssetById(current.getAssetId());
            if (asset == null)
            {
                throw new ServiceException("器材不存在");
            }
            Integer delta = 0 - current.getQuantity();
            if (assetService.updateAvailableQty(current.getAssetId(), delta) <= 0)
            {
                throw new ServiceException("可用库存不足，无法通过审批");
            }
            current.setStatus("3");
            current.setBorrowTime(new Date());
            current.setApprovalTime(new Date());
            current.setApprovalBy(borrow.getUpdateBy());
            current.setRejectReason("");
            if (borrow.getExpectedReturnTime() != null)
            {
                current.setExpectedReturnTime(borrow.getExpectedReturnTime());
            }
            current.setUpdateBy(borrow.getUpdateBy());
            return borrowMapper.updateBorrow(current);
        }
        if ("2".equals(borrow.getStatus()))
        {
            current.setStatus("2");
            current.setRejectReason(borrow.getRejectReason());
            current.setApprovalTime(new Date());
            current.setApprovalBy(borrow.getUpdateBy());
            current.setUpdateBy(borrow.getUpdateBy());
            return borrowMapper.updateBorrow(current);
        }
        throw new ServiceException("审批状态不合法");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int returnBorrow(Long borrowId, Long operatorUserId, String operatorName, boolean adminAction)
    {
        SpEquipmentBorrow current = borrowMapper.selectBorrowById(borrowId);
        if (current == null)
        {
            throw new ServiceException("借还记录不存在");
        }
        if (!"3".equals(current.getStatus()) && !"5".equals(current.getStatus()))
        {
            throw new ServiceException("当前记录不处于借出中/逾期状态");
        }
        if (!adminAction && !current.getUserId().equals(operatorUserId))
        {
            throw new ServiceException("仅可归还本人借阅记录");
        }
        current.setStatus("4");
        current.setActualReturnTime(new Date());
        current.setUpdateBy(operatorName);
        int updated = borrowMapper.updateBorrow(current);
        if (updated > 0)
        {
            if (assetService.updateAvailableQty(current.getAssetId(), current.getQuantity()) <= 0)
            {
                throw new ServiceException("归还库存回补失败");
            }
        }
        return updated;
    }

    @Override
    public int cancelBorrow(Long borrowId, Long userId, String operatorName)
    {
        SpEquipmentBorrow current = borrowMapper.selectBorrowById(borrowId);
        if (current == null)
        {
            throw new ServiceException("借还记录不存在");
        }
        if (!current.getUserId().equals(userId))
        {
            throw new ServiceException("仅可取消本人预约");
        }
        if (!"0".equals(current.getStatus()))
        {
            throw new ServiceException("仅待审批预约可取消");
        }
        current.setStatus("2");
        current.setRejectReason("用户主动取消");
        current.setUpdateBy(operatorName);
        return borrowMapper.updateBorrow(current);
    }

    @Override
    public int fillOverdueReason(Long borrowId, String overdueReason, String operatorName)
    {
        if (borrowId == null)
        {
            throw new ServiceException("借还记录不能为空");
        }
        if (StringUtils.isBlank(overdueReason))
        {
            throw new ServiceException("请填写逾期原因");
        }
        SpEquipmentBorrow current = borrowMapper.selectBorrowById(borrowId);
        if (current == null)
        {
            throw new ServiceException("借还记录不存在");
        }
        if (current.getOverdueDays() == null || current.getOverdueDays() <= 0)
        {
            throw new ServiceException("当前记录不是逾期记录，无需回填原因");
        }
        current.setOverdueReason(overdueReason.trim());
        current.setUpdateBy(operatorName);
        return borrowMapper.updateBorrow(current);
    }
    @Override
    public int reviewBorrow(SpEquipmentBorrow borrow, Long userId, String operatorName)
    {
        if (borrow == null || borrow.getBorrowId() == null)
        {
            throw new ServiceException("Borrow record cannot be null");
        }
        SpEquipmentBorrow current = borrowMapper.selectBorrowById(borrow.getBorrowId());
        if (current == null)
        {
            throw new ServiceException("Borrow record does not exist");
        }
        if (current.getUserId() == null || !current.getUserId().equals(userId))
        {
            throw new ServiceException("You can only review your own borrow record");
        }
        if (!"4".equals(current.getStatus()))
        {
            throw new ServiceException("Only returned records can be reviewed");
        }
        if (current.getReviewQualityScore() != null || current.getReviewPackageScore() != null
            || current.getReviewServiceScore() != null)
        {
            throw new ServiceException("This record has already been reviewed");
        }
        validateReviewScore(borrow.getReviewQualityScore(), "Quality score");
        validateReviewScore(borrow.getReviewPackageScore(), "Package score");
        validateReviewScore(borrow.getReviewServiceScore(), "Service score");

        String reviewContent = StringUtils.trimToNull(borrow.getReviewContent());
        if (reviewContent != null && reviewContent.length() > 500)
        {
            throw new ServiceException("Review content length cannot exceed 500 chars");
        }
        current.setReviewQualityScore(borrow.getReviewQualityScore());
        current.setReviewPackageScore(borrow.getReviewPackageScore());
        current.setReviewServiceScore(borrow.getReviewServiceScore());
        current.setReviewContent(reviewContent);
        current.setReviewTime(new Date());
        current.setReviewBy(operatorName);
        current.setUpdateBy(operatorName);
        return borrowMapper.updateBorrow(current);
    }

    private void validateReviewScore(Integer score, String fieldLabel)
    {
        if (score == null || score < 1 || score > 5)
        {
            throw new ServiceException(fieldLabel + " must be between 1 and 5");
        }
    }

    @Override
    public void refreshOverdueStatus()
    {
        borrowMapper.refreshOverdueStatus();
    }
}
