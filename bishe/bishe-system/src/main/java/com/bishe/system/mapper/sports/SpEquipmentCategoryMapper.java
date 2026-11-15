package com.bishe.system.mapper.sports;

import java.util.List;
import com.bishe.system.domain.sports.SpEquipmentCategory;

/**
 * 器材分类Mapper
 */
public interface SpEquipmentCategoryMapper
{
    public SpEquipmentCategory selectCategoryById(Long categoryId);

    public List<SpEquipmentCategory> selectCategoryList(SpEquipmentCategory category);

    public List<SpEquipmentCategory> selectEnabledCategories();

    public int insertCategory(SpEquipmentCategory category);

    public int updateCategory(SpEquipmentCategory category);

    public int deleteCategoryByIds(Long[] categoryIds);
}
