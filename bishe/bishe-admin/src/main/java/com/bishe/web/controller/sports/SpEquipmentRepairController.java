package com.bishe.web.controller.sports;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bishe.common.annotation.Log;
import com.bishe.common.core.controller.BaseController;
import com.bishe.common.core.domain.AjaxResult;
import com.bishe.common.core.page.TableDataInfo;
import com.bishe.common.enums.BusinessType;
import com.bishe.system.domain.sports.SpEquipmentRepair;
import com.bishe.system.service.sports.ISpEquipmentRepairService;

/**
 * 报修管理
 */
@RestController
@RequestMapping("/sports/repair")
public class SpEquipmentRepairController extends BaseController
{
    @Autowired
    private ISpEquipmentRepairService repairService;

    @PreAuthorize("@ss.hasPermi('sports:repair:list')")
    @GetMapping("/list")
    public TableDataInfo list(SpEquipmentRepair repair)
    {
        startPage();
        List<SpEquipmentRepair> list = repairService.selectRepairList(repair);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('sports:repair:query')")
    @GetMapping("/{repairId}")
    public AjaxResult getInfo(@PathVariable Long repairId)
    {
        return success(repairService.selectRepairById(repairId));
    }

    @PreAuthorize("@ss.hasPermi('sports:repair:edit')")
    @Log(title = "器材报修", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SpEquipmentRepair repair)
    {
        repair.setUpdateBy(getUsername());
        return toAjax(repairService.updateRepair(repair));
    }
}
