package com.bishe.system.mapper.sports;

import com.bishe.system.domain.sports.SpUserProfileExt;

/**
 * 用户扩展档案Mapper
 */
public interface SpUserProfileExtMapper
{
    public SpUserProfileExt selectByUserId(Long userId);

    public int insertProfileExt(SpUserProfileExt profileExt);

    public int updateProfileExt(SpUserProfileExt profileExt);
}

