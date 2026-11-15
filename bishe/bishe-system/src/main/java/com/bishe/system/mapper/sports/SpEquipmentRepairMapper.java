package com.bishe.system.mapper.sports;

import java.util.List;
import com.bishe.system.domain.sports.SpEquipmentRepair;

/**
 * 器材报修Mapper
 */
public interface SpEquipmentRepairMapper
{
    public SpEquipmentRepair selectRepairById(Long repairId);

    public List<SpEquipmentRepair> selectRepairList(SpEquipmentRepair repair);

    public List<SpEquipmentRepair> selectMyRepairList(SpEquipmentRepair repair);

    public int insertRepair(SpEquipmentRepair repair);

    public int updateRepair(SpEquipmentRepair repair);
}
