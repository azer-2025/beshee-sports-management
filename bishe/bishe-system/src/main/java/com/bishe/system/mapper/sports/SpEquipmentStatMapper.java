package com.bishe.system.mapper.sports;

import java.util.List;
import java.util.Map;
import java.util.Date;
import org.apache.ibatis.annotations.Param;
import com.bishe.system.domain.sports.SpSuggestionReport;

/**
 * 器材统计Mapper
 */
public interface SpEquipmentStatMapper
{
    public Map<String, Object> selectSummary();

    public List<Map<String, Object>> selectUsageRanking();

    public List<Map<String, Object>> selectDamageReason();

    public List<Map<String, Object>> selectMonthlyBorrowTrend();

    public List<SpSuggestionReport> selectSuggestionReport(@Param("statMonth") String statMonth,
        @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("referenceDate") Date referenceDate,
        @Param("suggestionType") String suggestionType, @Param("keyword") String keyword);
}
