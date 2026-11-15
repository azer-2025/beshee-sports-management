package com.bishe.system.service.sports.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bishe.system.domain.sports.SpUserProfileExt;
import com.bishe.system.mapper.sports.SpUserProfileExtMapper;
import com.bishe.system.service.sports.ISpUserProfileExtService;

/**
 * 用户扩展档案Service实现
 */
@Service
public class SpUserProfileExtServiceImpl implements ISpUserProfileExtService
{
    @Autowired
    private SpUserProfileExtMapper profileExtMapper;

    @Override
    public SpUserProfileExt selectByUserId(Long userId)
    {
        return profileExtMapper.selectByUserId(userId);
    }

    @Override
    public int saveByUserId(SpUserProfileExt profileExt)
    {
        SpUserProfileExt current = profileExtMapper.selectByUserId(profileExt.getUserId());
        if (current == null)
        {
            return profileExtMapper.insertProfileExt(profileExt);
        }
        return profileExtMapper.updateProfileExt(profileExt);
    }
}

