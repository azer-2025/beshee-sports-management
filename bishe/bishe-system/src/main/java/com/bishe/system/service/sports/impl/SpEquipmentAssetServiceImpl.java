package com.bishe.system.service.sports.impl;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bishe.system.domain.sports.SpEquipmentAsset;
import com.bishe.system.mapper.sports.SpEquipmentAssetMapper;
import com.bishe.system.service.sports.ISpEquipmentAssetService;

/**
 * 器材台账Service实现
 */
@Service
public class SpEquipmentAssetServiceImpl implements ISpEquipmentAssetService
{
    @Autowired
    private SpEquipmentAssetMapper assetMapper;

    @Override
    public SpEquipmentAsset selectAssetById(Long assetId)
    {
        return assetMapper.selectAssetById(assetId);
    }

    @Override
    public List<SpEquipmentAsset> selectAssetList(SpEquipmentAsset asset)
    {
        return assetMapper.selectAssetList(asset);
    }

    @Override
    public List<SpEquipmentAsset> selectAvailableAssetList(SpEquipmentAsset asset)
    {
        return assetMapper.selectAvailableAssetList(asset);
    }

    @Override
    public int insertAsset(SpEquipmentAsset asset)
    {
        if (asset.getDepreciationRate() == null)
        {
            asset.setDepreciationRate(new BigDecimal("10.00"));
        }
        if (asset.getServiceLifeYear() == null)
        {
            asset.setServiceLifeYear(5);
        }
        if (asset.getStockQty() == null || asset.getStockQty() < 0)
        {
            asset.setStockQty(1);
        }
        if (asset.getAvailableQty() == null || asset.getAvailableQty() < 0)
        {
            asset.setAvailableQty(asset.getStockQty());
        }
        if (asset.getAvailableQty() > asset.getStockQty())
        {
            asset.setAvailableQty(asset.getStockQty());
        }
        if (asset.getAssetStatus() == null)
        {
            asset.setAssetStatus("0");
        }
        if (asset.getAuditStatus() == null)
        {
            asset.setAuditStatus("0");
        }
        return assetMapper.insertAsset(asset);
    }

    @Override
    public int updateAsset(SpEquipmentAsset asset)
    {
        if (asset.getStockQty() != null && asset.getStockQty() < 0)
        {
            asset.setStockQty(0);
        }
        if (asset.getAvailableQty() != null && asset.getAvailableQty() < 0)
        {
            asset.setAvailableQty(0);
        }
        if (asset.getStockQty() != null && asset.getAvailableQty() != null && asset.getAvailableQty() > asset.getStockQty())
        {
            asset.setAvailableQty(asset.getStockQty());
        }
        return assetMapper.updateAsset(asset);
    }

    @Override
    public int deleteAssetByIds(Long[] assetIds)
    {
        return assetMapper.deleteAssetByIds(assetIds);
    }

    @Override
    public int updateAvailableQty(Long assetId, Integer deltaQty)
    {
        return assetMapper.updateAvailableQty(assetId, deltaQty);
    }
}
