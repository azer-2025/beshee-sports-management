package com.bishe.system.service.sports;

import java.util.List;
import java.util.Map;
import com.bishe.system.domain.sports.SpSuggestionReport;

/**
 * 器材统计Service接口
 */
public interface ISpEquipmentStatService
{
    public Map<String, Object> selectSummary();

    public List<Map<String, Object>> selectUsageRanking();

    public List<Map<String, Object>> selectDamageReason();

    public List<Map<String, Object>> selectMonthlyBorrowTrend();

    public List<SpSuggestionReport> selectSuggestionReport(String statMonth, String suggestionType, String keyword);
}
