package com.bishe.system.service.sports;

import java.util.List;
import java.util.Map;
import com.bishe.system.domain.sports.SpEquipmentBorrow;

/**
 * 器材借还Service接口
 */
public interface ISpEquipmentBorrowService
{
    public SpEquipmentBorrow selectBorrowById(Long borrowId);

    public List<SpEquipmentBorrow> selectBorrowList(SpEquipmentBorrow borrow);

    public List<SpEquipmentBorrow> selectMyBorrowList(SpEquipmentBorrow borrow);

    public List<SpEquipmentBorrow> selectOverdueBorrowList(SpEquipmentBorrow borrow);

    public Map<String, Object> selectOverdueDashboard();

    public List<Map<String, Object>> selectOverdueReasonStats();

    public int applyBorrow(SpEquipmentBorrow borrow);

    public int approveBorrow(SpEquipmentBorrow borrow);

    public int returnBorrow(Long borrowId, Long operatorUserId, String operatorName, boolean adminAction);

    public int cancelBorrow(Long borrowId, Long userId, String operatorName);

    public int fillOverdueReason(Long borrowId, String overdueReason, String operatorName);

    public int reviewBorrow(SpEquipmentBorrow borrow, Long userId, String operatorName);

    public void refreshOverdueStatus();
}
