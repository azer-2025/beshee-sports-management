package com.bishe.web.controller.sports;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.bishe.common.core.controller.BaseController;
import com.bishe.common.core.domain.AjaxResult;
import com.bishe.common.utils.poi.ExcelUtil;
import com.bishe.system.domain.sports.SpSuggestionReport;
import com.bishe.system.service.sports.ISpEquipmentStatService;

/**
 * 器材统计分析
 */
@RestController
@RequestMapping("/sports/stat")
public class SpEquipmentStatController extends BaseController
{
    @Autowired
    private ISpEquipmentStatService statService;

    @PreAuthorize("@ss.hasPermi('sports:stat:list')")
    @GetMapping("/summary")
    public AjaxResult summary()
    {
        return success(statService.selectSummary());
    }

    @PreAuthorize("@ss.hasPermi('sports:stat:query')")
    @GetMapping("/usageRank")
    public AjaxResult usageRank()
    {
        return success(statService.selectUsageRanking());
    }

    @PreAuthorize("@ss.hasPermi('sports:stat:query')")
    @GetMapping("/damageReason")
    public AjaxResult damageReason()
    {
        return success(statService.selectDamageReason());
    }

    @PreAuthorize("@ss.hasPermi('sports:stat:query')")
    @GetMapping("/monthlyTrend")
    public AjaxResult monthlyTrend()
    {
        return success(statService.selectMonthlyBorrowTrend());
    }

    @PreAuthorize("@ss.hasPermi('sports:stat:query')")
    @GetMapping("/suggestion")
    public AjaxResult suggestion(
        @RequestParam(value = "statMonth", required = false) String statMonth,
        @RequestParam(value = "suggestionType", required = false) String suggestionType,
        @RequestParam(value = "keyword", required = false) String keyword)
    {
        return success(statService.selectSuggestionReport(statMonth, suggestionType, keyword));
    }

    @PreAuthorize("@ss.hasPermi('sports:stat:export')")
    @PostMapping("/suggestion/export")
    public void exportSuggestion(HttpServletResponse response,
        @RequestParam(value = "statMonth", required = false) String statMonth,
        @RequestParam(value = "suggestionType", required = false) String suggestionType,
        @RequestParam(value = "keyword", required = false) String keyword)
    {
        List<SpSuggestionReport> list = statService.selectSuggestionReport(statMonth, suggestionType, keyword);
        ExcelUtil<SpSuggestionReport> util = new ExcelUtil<SpSuggestionReport>(SpSuggestionReport.class);
        util.exportExcel(response, list, "采购报废建议报告");
    }
}
