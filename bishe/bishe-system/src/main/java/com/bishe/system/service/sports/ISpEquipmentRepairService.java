package com.bishe.system.service.sports;

import java.util.List;
import com.bishe.system.domain.sports.SpEquipmentRepair;

/**
 * 器材报修Service接口
 */
public interface ISpEquipmentRepairService
{
    public SpEquipmentRepair selectRepairById(Long repairId);

    public List<SpEquipmentRepair> selectRepairList(SpEquipmentRepair repair);

    public List<SpEquipmentRepair> selectMyRepairList(SpEquipmentRepair repair);

    public int insertRepair(SpEquipmentRepair repair);

    public int updateRepair(SpEquipmentRepair repair);
}
