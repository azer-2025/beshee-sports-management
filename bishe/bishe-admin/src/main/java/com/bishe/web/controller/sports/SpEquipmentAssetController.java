package com.bishe.web.controller.sports;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bishe.common.annotation.Log;
import com.bishe.common.core.controller.BaseController;
import com.bishe.common.core.domain.AjaxResult;
import com.bishe.common.core.page.TableDataInfo;
import com.bishe.common.enums.BusinessType;
import com.bishe.system.domain.sports.SpEquipmentAsset;
import com.bishe.system.service.sports.ISpEquipmentAssetService;

/**
 * 器材台账管理
 */
@RestController
@RequestMapping("/sports/asset")
public class SpEquipmentAssetController extends BaseController
{
    @Autowired
    private ISpEquipmentAssetService assetService;

    @PreAuthorize("@ss.hasPermi('sports:asset:list')")
    @GetMapping("/list")
    public TableDataInfo list(SpEquipmentAsset asset)
    {
        startPage();
        List<SpEquipmentAsset> list = assetService.selectAssetList(asset);
        return getDataTable(list);
    }

    @GetMapping("/available/list")
    public AjaxResult availableList(SpEquipmentAsset asset)
    {
        return success(assetService.selectAvailableAssetList(asset));
    }

    @PreAuthorize("@ss.hasPermi('sports:asset:query')")
    @GetMapping("/{assetId}")
    public AjaxResult getInfo(@PathVariable Long assetId)
    {
        return success(assetService.selectAssetById(assetId));
    }

    @PreAuthorize("@ss.hasPermi('sports:asset:add')")
    @Log(title = "器材台账", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SpEquipmentAsset asset)
    {
        asset.setCreateBy(getUsername());
        return toAjax(assetService.insertAsset(asset));
    }

    @PreAuthorize("@ss.hasPermi('sports:asset:edit')")
    @Log(title = "器材台账", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SpEquipmentAsset asset)
    {
        asset.setUpdateBy(getUsername());
        return toAjax(assetService.updateAsset(asset));
    }

    @PreAuthorize("@ss.hasPermi('sports:asset:offline')")
    @Log(title = "器材台账", businessType = BusinessType.UPDATE)
    @PutMapping("/offline/{assetIds}")
    public AjaxResult batchOffline(@PathVariable Long[] assetIds)
    {
        int rows = 0;
        for (Long assetId : assetIds)
        {
            SpEquipmentAsset asset = new SpEquipmentAsset();
            asset.setAssetId(assetId);
            asset.setAssetStatus("4");
            asset.setUpdateBy(getUsername());
            rows += assetService.updateAsset(asset);
        }
        return toAjax(rows);
    }

    @PreAuthorize("@ss.hasPermi('sports:asset:audit')")
    @Log(title = "器材台账", businessType = BusinessType.UPDATE)
    @PutMapping("/audit")
    public AjaxResult audit(@RequestBody SpEquipmentAsset asset)
    {
        asset.setUpdateBy(getUsername());
        return toAjax(assetService.updateAsset(asset));
    }

    @PreAuthorize("@ss.hasPermi('sports:asset:remove')")
    @Log(title = "器材台账", businessType = BusinessType.DELETE)
    @DeleteMapping("/{assetIds}")
    public AjaxResult remove(@PathVariable Long[] assetIds)
    {
        return toAjax(assetService.deleteAssetByIds(assetIds));
    }
}
