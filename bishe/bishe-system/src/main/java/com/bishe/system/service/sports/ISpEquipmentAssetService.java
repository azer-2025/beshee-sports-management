package com.bishe.system.service.sports;

import java.util.List;
import com.bishe.system.domain.sports.SpEquipmentAsset;

/**
 * 器材台账Service接口
 */
public interface ISpEquipmentAssetService
{
    public SpEquipmentAsset selectAssetById(Long assetId);

    public List<SpEquipmentAsset> selectAssetList(SpEquipmentAsset asset);

    public List<SpEquipmentAsset> selectAvailableAssetList(SpEquipmentAsset asset);

    public int insertAsset(SpEquipmentAsset asset);

    public int updateAsset(SpEquipmentAsset asset);

    public int deleteAssetByIds(Long[] assetIds);

    public int updateAvailableQty(Long assetId, Integer deltaQty);
}
