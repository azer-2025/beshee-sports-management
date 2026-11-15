package com.bishe.system.service.sports.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bishe.system.domain.sports.SpEquipmentCategory;
import com.bishe.system.mapper.sports.SpEquipmentCategoryMapper;
import com.bishe.system.service.sports.ISpEquipmentCategoryService;

/**
 * 器材分类Service实现
 */
@Service
public class SpEquipmentCategoryServiceImpl implements ISpEquipmentCategoryService
{
    @Autowired
    private SpEquipmentCategoryMapper categoryMapper;

    @Override
    public SpEquipmentCategory selectCategoryById(Long categoryId)
    {
        return categoryMapper.selectCategoryById(categoryId);
    }

    @Override
    public List<SpEquipmentCategory> selectCategoryList(SpEquipmentCategory category)
    {
        return categoryMapper.selectCategoryList(category);
    }

    @Override
    public List<SpEquipmentCategory> selectEnabledCategories()
    {
        return categoryMapper.selectEnabledCategories();
    }

    @Override
    public int insertCategory(SpEquipmentCategory category)
    {
        if (category.getOrderNum() == null)
        {
            category.setOrderNum(0);
        }
        if (category.getStatus() == null)
        {
            category.setStatus("0");
        }
        return categoryMapper.insertCategory(category);
    }

    @Override
    public int updateCategory(SpEquipmentCategory category)
    {
        return categoryMapper.updateCategory(category);
    }

    @Override
    public int deleteCategoryByIds(Long[] categoryIds)
    {
        return categoryMapper.deleteCategoryByIds(categoryIds);
    }
}
