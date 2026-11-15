package com.bishe.system.service.sports.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bishe.common.exception.ServiceException;
import com.bishe.system.domain.sports.SpEquipmentAsset;
import com.bishe.system.domain.sports.SpEquipmentRepair;
import com.bishe.system.mapper.sports.SpEquipmentRepairMapper;
import com.bishe.system.service.sports.ISpEquipmentAssetService;
import com.bishe.system.service.sports.ISpEquipmentRepairService;

/**
 * 器材报修Service实现
 */
@Service
public class SpEquipmentRepairServiceImpl implements ISpEquipmentRepairService
{
    @Autowired
    private SpEquipmentRepairMapper repairMapper;

    @Autowired
    private ISpEquipmentAssetService assetService;

    @Override
    public SpEquipmentRepair selectRepairById(Long repairId)
    {
        return repairMapper.selectRepairById(repairId);
    }

    @Override
    public List<SpEquipmentRepair> selectRepairList(SpEquipmentRepair repair)
    {
        return repairMapper.selectRepairList(repair);
    }

    @Override
    public List<SpEquipmentRepair> selectMyRepairList(SpEquipmentRepair repair)
    {
        return repairMapper.selectMyRepairList(repair);
    }

    @Override
    public int insertRepair(SpEquipmentRepair repair)
    {
        if (repair.getAssetId() == null)
        {
            throw new ServiceException("请选择报修器材");
        }
        SpEquipmentAsset asset = assetService.selectAssetById(repair.getAssetId());
        if (asset == null)
        {
            throw new ServiceException("器材不存在");
        }
        if ("3".equals(asset.getAssetStatus()) || "4".equals(asset.getAssetStatus()))
        {
            throw new ServiceException("该器材不可报修");
        }
        repair.setStatus("0");
        if (repair.getUrgency() == null)
        {
            repair.setUrgency("0");
        }
        if (repair.getFaultReason() == null)
        {
            repair.setFaultReason("0");
        }
        return repairMapper.insertRepair(repair);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateRepair(SpEquipmentRepair repair)
    {
        SpEquipmentRepair current = repairMapper.selectRepairById(repair.getRepairId());
        if (current == null)
        {
            throw new ServiceException("报修记录不存在");
        }
        if (repair.getStatus() != null && !"".equals(repair.getStatus()))
        {
            if ("1".equals(repair.getStatus()) || "2".equals(repair.getStatus()))
            {
                if (repair.getRepairTime() == null)
                {
                    repair.setRepairTime(new Date());
                }
                if (repair.getRepairBy() == null || "".equals(repair.getRepairBy()))
                {
                    repair.setRepairBy(repair.getUpdateBy());
                }
                SpEquipmentAsset asset = new SpEquipmentAsset();
                asset.setAssetId(current.getAssetId());
                asset.setAssetStatus("2");
                asset.setUpdateBy(repair.getUpdateBy());
                assetService.updateAsset(asset);
            }
            if ("3".equals(repair.getStatus()))
            {
                if (repair.getFinishTime() == null)
                {
                    repair.setFinishTime(new Date());
                }
                SpEquipmentAsset asset = new SpEquipmentAsset();
                asset.setAssetId(current.getAssetId());
                asset.setAssetStatus("0");
                asset.setUpdateBy(repair.getUpdateBy());
                assetService.updateAsset(asset);
            }
            if ("4".equals(repair.getStatus()) && repair.getFinishTime() == null)
            {
                repair.setFinishTime(new Date());
            }
        }
        return repairMapper.updateRepair(repair);
    }
}
