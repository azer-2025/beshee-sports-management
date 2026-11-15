package com.bishe.system.service.sports;

import com.bishe.system.domain.sports.SpUserProfileExt;

/**
 * 用户扩展档案Service接口
 */
public interface ISpUserProfileExtService
{
    public SpUserProfileExt selectByUserId(Long userId);

    public int saveByUserId(SpUserProfileExt profileExt);
}

