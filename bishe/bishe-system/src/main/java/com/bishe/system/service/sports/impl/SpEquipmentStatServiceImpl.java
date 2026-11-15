package com.bishe.system.service.sports.impl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bishe.common.exception.ServiceException;
import com.bishe.system.domain.sports.SpSuggestionReport;
import com.bishe.system.mapper.sports.SpEquipmentStatMapper;
import com.bishe.system.service.sports.ISpEquipmentStatService;

/**
 * 器材统计Service实现
 */
@Service
public class SpEquipmentStatServiceImpl implements ISpEquipmentStatService
{
    private static final DateTimeFormatter MONTH_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM");

    @Autowired
    private SpEquipmentStatMapper statMapper;

    @Override
    public Map<String, Object> selectSummary()
    {
        return statMapper.selectSummary();
    }

    @Override
    public List<Map<String, Object>> selectUsageRanking()
    {
        return statMapper.selectUsageRanking();
    }

    @Override
    public List<Map<String, Object>> selectDamageReason()
    {
        return statMapper.selectDamageReason();
    }

    @Override
    public List<Map<String, Object>> selectMonthlyBorrowTrend()
    {
        return statMapper.selectMonthlyBorrowTrend();
    }

    @Override
    public List<SpSuggestionReport> selectSuggestionReport(String statMonth, String suggestionType, String keyword)
    {
        YearMonth ym = resolveStatMonth(statMonth);
        LocalDate monthStart = ym.atDay(1);
        LocalDate nextMonthStart = ym.plusMonths(1).atDay(1);
        LocalDate monthEnd = ym.atEndOfMonth();
        return statMapper.selectSuggestionReport(ym.format(MONTH_FORMAT),
            java.sql.Date.valueOf(monthStart),
            java.sql.Date.valueOf(nextMonthStart),
            java.sql.Date.valueOf(monthEnd),
            suggestionType,
            keyword);
    }

    private YearMonth resolveStatMonth(String statMonth)
    {
        if (statMonth == null || "".equals(statMonth.trim()))
        {
            return YearMonth.from(LocalDate.now());
        }
        try
        {
            LocalDate month = LocalDate.parse(statMonth.trim() + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return YearMonth.from(month.with(TemporalAdjusters.firstDayOfMonth()));
        }
        catch (DateTimeParseException ex)
        {
            throw new ServiceException("统计月份格式错误，应为yyyy-MM");
        }
    }
}
