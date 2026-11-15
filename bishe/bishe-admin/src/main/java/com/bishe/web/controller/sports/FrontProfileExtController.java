package com.bishe.web.controller.sports;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bishe.common.annotation.Log;
import com.bishe.common.core.controller.BaseController;
import com.bishe.common.core.domain.AjaxResult;
import com.bishe.common.enums.BusinessType;
import com.bishe.system.domain.sports.SpUserProfileExt;
import com.bishe.system.service.sports.ISpUserProfileExtService;

/**
 * 前台-用户扩展档案
 */
@RestController
@RequestMapping("/front/profile/ext")
@PreAuthorize("@ss.hasAnyRoles('admin,venue_manager,coach,student')")
public class FrontProfileExtController extends BaseController
{
    @Autowired
    private ISpUserProfileExtService profileExtService;

    @GetMapping
    public AjaxResult getMyProfileExt()
    {
        SpUserProfileExt profileExt = profileExtService.selectByUserId(getUserId());
        if (profileExt == null)
        {
            profileExt = new SpUserProfileExt();
            profileExt.setUserId(getUserId());
        }
        return success(profileExt);
    }

    @Log(title = "前台扩展档案维护", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateMyProfileExt(@RequestBody SpUserProfileExt profileExt)
    {
        profileExt.setUserId(getUserId());
        profileExt.setCreateBy(getUsername());
        profileExt.setUpdateBy(getUsername());
        return toAjax(profileExtService.saveByUserId(profileExt));
    }
}

