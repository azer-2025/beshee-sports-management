package com.bishe.web.controller.sports;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.bishe.common.core.controller.BaseController;
import com.bishe.common.core.domain.AjaxResult;
import com.bishe.system.domain.SysNotice;
import com.bishe.system.service.ISysNoticeService;

/**
 * 前台-系统通知公告
 */
@RestController
@RequestMapping("/front/notice")
@PreAuthorize("@ss.hasAnyRoles('admin,venue_manager,coach,student')")
public class FrontNoticeController extends BaseController
{
    @Autowired
    private ISysNoticeService noticeService;

    @GetMapping("/recent")
    public AjaxResult recent(@RequestParam(value = "limit", required = false) Integer limit)
    {
        int maxLimit = (limit == null || limit <= 0) ? 8 : Math.min(limit, 30);
        SysNotice query = new SysNotice();
        query.setStatus("0");
        List<SysNotice> list = new ArrayList<>(noticeService.selectNoticeList(query));
        list.sort(Comparator.comparing(SysNotice::getCreateTime, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
        if (list.size() > maxLimit)
        {
            list = list.subList(0, maxLimit);
        }
        return success(list);
    }
}

