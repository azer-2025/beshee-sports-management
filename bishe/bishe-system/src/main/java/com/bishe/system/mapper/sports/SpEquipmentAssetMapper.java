package com.bishe.system.mapper.sports;

import java.util.List;
import com.bishe.system.domain.sports.SpEquipmentAsset;
import org.apache.ibatis.annotations.Param;

/**
 * 器材台账Mapper
 */
public interface SpEquipmentAssetMapper
{
    public SpEquipmentAsset selectAssetById(Long assetId);

    public List<SpEquipmentAsset> selectAssetList(SpEquipmentAsset asset);

    public List<SpEquipmentAsset> selectAvailableAssetList(SpEquipmentAsset asset);

    public int insertAsset(SpEquipmentAsset asset);

    public int updateAsset(SpEquipmentAsset asset);

    public int deleteAssetByIds(Long[] assetIds);

    public int updateAvailableQty(@Param("assetId") Long assetId, @Param("deltaQty") Integer deltaQty);
}
