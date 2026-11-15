package com.bishe.system.mapper.sports;

import java.util.List;
import java.util.Map;
import com.bishe.system.domain.sports.SpEquipmentBorrow;

/**
 * 器材借还Mapper
 */
public interface SpEquipmentBorrowMapper
{
    public SpEquipmentBorrow selectBorrowById(Long borrowId);

    public List<SpEquipmentBorrow> selectBorrowList(SpEquipmentBorrow borrow);

    public List<SpEquipmentBorrow> selectMyBorrowList(SpEquipmentBorrow borrow);

    public List<SpEquipmentBorrow> selectOverdueBorrowList(SpEquipmentBorrow borrow);

    public Map<String, Object> selectOverdueDashboard();

    public List<Map<String, Object>> selectOverdueReasonStats();

    public int insertBorrow(SpEquipmentBorrow borrow);

    public int updateBorrow(SpEquipmentBorrow borrow);

    public int refreshOverdueStatus();
}
