package com.bishe.web.controller.sports;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.bishe.system.domain.sports.SpEquipmentBorrow;
import com.bishe.system.service.sports.ISpEquipmentBorrowService;

/**
 * 前台-预约借还
 */
@RestController
@RequestMapping("/front/borrow")
@PreAuthorize("@ss.hasAnyRoles('admin,venue_manager,coach,student')")
public class FrontBorrowController extends BaseController
{
    @Autowired
    private ISpEquipmentBorrowService borrowService;

    @GetMapping("/my")
    public TableDataInfo myList(SpEquipmentBorrow borrow)
    {
        borrow.setUserId(getUserId());
        startPage();
        List<SpEquipmentBorrow> list = borrowService.selectMyBorrowList(borrow);
        return getDataTable(list);
    }

    @Log(title = "前台借还预约", businessType = BusinessType.INSERT)
    @PostMapping("/apply")
    public AjaxResult apply(@RequestBody SpEquipmentBorrow borrow)
    {
        borrow.setUserId(getUserId());
        borrow.setCreateBy(getUsername());
        return toAjax(borrowService.applyBorrow(borrow));
    }

    @Log(title = "前台借还取消", businessType = BusinessType.UPDATE)
    @PutMapping("/cancel/{borrowId}")
    public AjaxResult cancel(@PathVariable Long borrowId)
    {
        return toAjax(borrowService.cancelBorrow(borrowId, getUserId(), getUsername()));
    }

    @Log(title = "前台借还归还", businessType = BusinessType.UPDATE)
    @PutMapping("/return/{borrowId}")
    public AjaxResult returnAsset(@PathVariable Long borrowId)
    {
        return toAjax(borrowService.returnBorrow(borrowId, getUserId(), getUsername(), false));
    }

    @Log(title = "前台借还评价", businessType = BusinessType.UPDATE)
    @PutMapping("/review")
    public AjaxResult review(@RequestBody SpEquipmentBorrow borrow)
    {
        return toAjax(borrowService.reviewBorrow(borrow, getUserId(), getUsername()));
    }
}
