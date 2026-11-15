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
import com.bishe.system.domain.sports.SpEquipmentBorrow;
import com.bishe.system.service.sports.ISpEquipmentBorrowService;

/**
 * 借还审批中心
 */
@RestController
@RequestMapping("/sports/borrow")
public class SpEquipmentBorrowController extends BaseController
{
    @Autowired
    private ISpEquipmentBorrowService borrowService;

    @PreAuthorize("@ss.hasPermi('sports:borrow:list')")
    @GetMapping("/list")
    public TableDataInfo list(SpEquipmentBorrow borrow)
    {
        startPage();
        List<SpEquipmentBorrow> list = borrowService.selectBorrowList(borrow);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('sports:borrow:query')")
    @GetMapping("/{borrowId}")
    public AjaxResult getInfo(@PathVariable Long borrowId)
    {
        return success(borrowService.selectBorrowById(borrowId));
    }

    @PreAuthorize("@ss.hasPermi('sports:borrow:approve')")
    @Log(title = "借还审批", businessType = BusinessType.UPDATE)
    @PutMapping("/approve")
    public AjaxResult approve(@RequestBody SpEquipmentBorrow borrow)
    {
        borrow.setUpdateBy(getUsername());
        return toAjax(borrowService.approveBorrow(borrow));
    }

    @PreAuthorize("@ss.hasPermi('sports:borrow:return')")
    @Log(title = "借还审批", businessType = BusinessType.UPDATE)
    @PutMapping("/return/{borrowId}")
    public AjaxResult returnAsset(@PathVariable Long borrowId)
    {
        return toAjax(borrowService.returnBorrow(borrowId, getUserId(), getUsername(), true));
    }

    @PreAuthorize("@ss.hasPermi('sports:borrow:list')")
    @GetMapping("/overdue/list")
    public TableDataInfo overdueList(SpEquipmentBorrow borrow)
    {
        startPage();
        List<SpEquipmentBorrow> list = borrowService.selectOverdueBorrowList(borrow);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('sports:borrow:list')")
    @GetMapping("/overdue/dashboard")
    public AjaxResult overdueDashboard()
    {
        return success(borrowService.selectOverdueDashboard());
    }

    @PreAuthorize("@ss.hasPermi('sports:borrow:list')")
    @GetMapping("/overdue/reasonStats")
    public AjaxResult overdueReasonStats()
    {
        return success(borrowService.selectOverdueReasonStats());
    }

    @PreAuthorize("@ss.hasPermi('sports:borrow:approve')")
    @Log(title = "借还审批", businessType = BusinessType.UPDATE)
    @PutMapping("/overdue/reason")
    public AjaxResult fillOverdueReason(@RequestBody SpEquipmentBorrow borrow)
    {
        return toAjax(borrowService.fillOverdueReason(borrow.getBorrowId(), borrow.getOverdueReason(), getUsername()));
    }
}
