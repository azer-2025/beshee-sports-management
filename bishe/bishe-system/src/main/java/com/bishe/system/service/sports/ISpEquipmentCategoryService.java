package com.bishe.system.service.sports;

import java.util.List;
import com.bishe.system.domain.sports.SpEquipmentCategory;

/**
 * 器材分类Service接口
 */
public interface ISpEquipmentCategoryService
{
    public SpEquipmentCategory selectCategoryById(Long categoryId);

    public List<SpEquipmentCategory> selectCategoryList(SpEquipmentCategory category);

    public List<SpEquipmentCategory> selectEnabledCategories();

    public int insertCategory(SpEquipmentCategory category);

    public int updateCategory(SpEquipmentCategory category);

    public int deleteCategoryByIds(Long[] categoryIds);
}
