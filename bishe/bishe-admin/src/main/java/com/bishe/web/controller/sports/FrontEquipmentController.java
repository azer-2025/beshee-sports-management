package com.bishe.web.controller.sports;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.bishe.common.core.controller.BaseController;
import com.bishe.common.core.domain.AjaxResult;
import com.bishe.common.core.page.TableDataInfo;
import com.bishe.common.utils.StringUtils;
import com.bishe.system.domain.sports.SpEquipmentAsset;
import com.bishe.system.service.sports.ISpEquipmentAssetService;
import com.bishe.system.service.sports.ISpEquipmentCategoryService;

/**
 * 前台-器材检索
 */
@RestController
@RequestMapping("/front/equipment")
@PreAuthorize("@ss.hasAnyRoles('admin,venue_manager,coach,student')")
public class FrontEquipmentController extends BaseController
{
    @Autowired
    private ISpEquipmentCategoryService categoryService;

    @Autowired
    private ISpEquipmentAssetService assetService;

    @GetMapping("/category/list")
    public AjaxResult categoryList()
    {
        return success(categoryService.selectEnabledCategories());
    }

    @GetMapping("/list")
    public TableDataInfo list(SpEquipmentAsset asset,
        @RequestParam(value = "classType", required = false) String classType,
        @RequestParam(value = "keyword", required = false) String keyword)
    {
        if (StringUtils.isNotEmpty(classType))
        {
            asset.setClassType(classType);
        }
        if (StringUtils.isNotEmpty(keyword))
        {
            asset.setSearchValue(keyword);
        }
        startPage();
        List<SpEquipmentAsset> list = assetService.selectAvailableAssetList(asset);
        return getDataTable(list);
    }

    @GetMapping("/{assetId}")
    public AjaxResult detail(@PathVariable Long assetId)
    {
        return success(assetService.selectAssetById(assetId));
    }
}
