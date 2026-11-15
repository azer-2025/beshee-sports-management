package com.bishe.web.controller.sports;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
 * 前台-异常报修
 */
@RestController
@RequestMapping("/front/repair")
@PreAuthorize("@ss.hasAnyRoles('admin,venue_manager,coach,student')")
public class FrontRepairController extends BaseController
{
    @Autowired
    private ISpEquipmentRepairService repairService;

    @GetMapping("/my")
    public TableDataInfo myList(SpEquipmentRepair repair)
    {
        repair.setUserId(getUserId());
        startPage();
        List<SpEquipmentRepair> list = repairService.selectMyRepairList(repair);
        return getDataTable(list);
    }

    @GetMapping("/{repairId}")
    public AjaxResult detail(@PathVariable Long repairId)
    {
        SpEquipmentRepair repair = repairService.selectRepairById(repairId);
        if (repair != null && !getUserId().equals(repair.getUserId()))
        {
            return error("无权限查看该报修记录");
        }
        return success(repair);
    }

    @Log(title = "前台器材报修", businessType = BusinessType.INSERT)
    @PostMapping("/report")
    public AjaxResult report(@RequestBody SpEquipmentRepair repair)
    {
        repair.setUserId(getUserId());
        repair.setCreateBy(getUsername());
        return toAjax(repairService.insertRepair(repair));
    }
}
