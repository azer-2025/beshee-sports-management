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
import com.bishe.system.domain.sports.SpEquipmentCategory;
import com.bishe.system.service.sports.ISpEquipmentCategoryService;

/**
 * 器材分类管理
 */
@RestController
@RequestMapping("/sports/category")
public class SpEquipmentCategoryController extends BaseController
{
    @Autowired
    private ISpEquipmentCategoryService categoryService;

    @PreAuthorize("@ss.hasPermi('sports:category:list')")
    @GetMapping("/list")
    public TableDataInfo list(SpEquipmentCategory category)
    {
        startPage();
        List<SpEquipmentCategory> list = categoryService.selectCategoryList(category);
        return getDataTable(list);
    }

    @GetMapping("/enabled")
    public AjaxResult enabledList()
    {
        return success(categoryService.selectEnabledCategories());
    }

    @PreAuthorize("@ss.hasPermi('sports:category:query')")
    @GetMapping("/{categoryId}")
    public AjaxResult getInfo(@PathVariable Long categoryId)
    {
        return success(categoryService.selectCategoryById(categoryId));
    }

    @PreAuthorize("@ss.hasPermi('sports:category:add')")
    @Log(title = "器材分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SpEquipmentCategory category)
    {
        category.setCreateBy(getUsername());
        return toAjax(categoryService.insertCategory(category));
    }

    @PreAuthorize("@ss.hasPermi('sports:category:edit')")
    @Log(title = "器材分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SpEquipmentCategory category)
    {
        category.setUpdateBy(getUsername());
        return toAjax(categoryService.updateCategory(category));
    }

    @PreAuthorize("@ss.hasPermi('sports:category:remove')")
    @Log(title = "器材分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{categoryIds}")
    public AjaxResult remove(@PathVariable Long[] categoryIds)
    {
        return toAjax(categoryService.deleteCategoryByIds(categoryIds));
    }
}
