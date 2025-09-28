package com.bookingsystem.controller;

import com.bookingsystem.dto.CountReservationsDTO;
import com.bookingsystem.dto.RoomTypeUsageDTO;
import com.bookingsystem.dto.RoomUsageSummaryDTO;
import com.bookingsystem.pojo.Result;
import com.bookingsystem.dto.TypeCountDTO;
import com.bookingsystem.pojo.TimeSlotReport;
import com.bookingsystem.service.ReportService;
import com.bookingsystem.vo.DayOfWeekCountVO;
import com.bookingsystem.vo.ReservationCountVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/reports")
public class ReportController {
    @Autowired
    private ReportService reportService;
    /**
     * 统计预约总数
     * range：TODAY、WEEK、MONTH、YEAR 默认为TODAY
     */
    @GetMapping("/countReservations")
    public Result countReservations( CountReservationsDTO countReservationsDTO) {
        if (countReservationsDTO.getStart() != null && countReservationsDTO.getEnd() != null){
            try {
                validateDateRange(countReservationsDTO.getStart(), countReservationsDTO.getEnd());
            } catch (Exception e) {
                return Result.error(e.getMessage());
            }
        }
        Integer num = reportService.countReservations(countReservationsDTO);
        return Result.success(num);
    }

    // 日期范围校验
    private void validateDateRange(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("开始日期不能晚于结束日期");
        }
        if (ChronoUnit.DAYS.between(start, end) > 365) {
            throw new IllegalArgumentException("最大查询范围不能超过一年");
        }
    }

    /**
     * 预约通过率
     */
    @GetMapping("/approvalRate")
    public Result approvalRate( CountReservationsDTO countReservationsDTO) {
        log.info("approvalRate:{}", countReservationsDTO);
        if (countReservationsDTO.getStart() != null && countReservationsDTO.getEnd() != null){
            try {
                validateDateRange(countReservationsDTO.getStart(), countReservationsDTO.getEnd());
            } catch (Exception e) {
                return Result.error(e.getMessage());
            }
        }
        double rate = reportService.approvalRate(countReservationsDTO);
        DecimalFormat df = new DecimalFormat("#.####");
        rate = Double.parseDouble(df.format(rate));
        return Result.success(rate);
    }

    /**
     * 活跃用户数
     */
    @GetMapping("/activeUsers")
    public Result activeUsers( CountReservationsDTO countReservationsDTO) {
        if (countReservationsDTO.getStart() != null && countReservationsDTO.getEnd() != null){
            try {
                validateDateRange(countReservationsDTO.getStart(), countReservationsDTO.getEnd());
            } catch (Exception e) {
                return Result.error(e.getMessage());
            }
        }
        int userNum = reportService.activeUsers(countReservationsDTO);
        return Result.success(userNum);
    }

    /**
     * 注册用户数
     */
    @GetMapping("/registeredUsers")
    public Result registeredUsers() {
        int userNum = reportService.registeredUsers();
        return Result.success(userNum);
    }

    /**
     * 待审核预约数
     */
    @GetMapping("/pendingReservations")
    public Result pendingReservations() {
        int num = reportService.pendingReservations();
        return Result.success(num);
    }

    /**
     * 教室使用率
     */
    @GetMapping("/classroomUsageRate")
    public Result classroomUsageRate( CountReservationsDTO countReservationsDTO) {
        if (countReservationsDTO.getStart() != null && countReservationsDTO.getEnd() != null){
            try {
                validateDateRange(countReservationsDTO.getStart(), countReservationsDTO.getEnd());
            } catch (Exception e) {
                return Result.error(e.getMessage());
            }
        }
        double rate = reportService.classroomUsageRate(countReservationsDTO);
        DecimalFormat df = new DecimalFormat("#.####");
        rate = Double.parseDouble(df.format(rate));
        return Result.success(rate);
    }

    /**
     *教室使用分布
     */
    @GetMapping("/classroomDistribution")
    public Result classroomDistribution() {
        List<TypeCountDTO> lists = reportService.getTypeStatistics();
        return Result.success(lists);
    }

    /**
     * 预约状态分布
     */
    @GetMapping("/reservationStatusDistribution")
    public Result reservationStatusDistribution() {
        List<ReservationCountVO> lists = reportService.getReservationStatusDistribution();
        return Result.success(lists);
    }

    /**
     * 预约趋势
     * @return
     */
    @GetMapping("/weekly")
    public Result getWeeklyStatistics() {
        List<DayOfWeekCountVO> lists = reportService.getWeeklyStatistics();
        for (DayOfWeekCountVO item : lists) {
            switch (item.getDayOfWeek()){
                case "Sunday":item.setDayOfWeek("周日");
                break;
                case "Monday":item.setDayOfWeek("周一");
                break;
                case "Tuesday":item.setDayOfWeek("周二");
                break;
                case "Wednesday":item.setDayOfWeek("周三");
                break;
                case "Thursday":item.setDayOfWeek("周四");
                break;
                case "Friday":item.setDayOfWeek("周五");
                break;
                case "Saturday":item.setDayOfWeek("周六");
                break;
            }
        }
        return Result.success(lists);
    }

    /**
     * 热门时段分析
     */
    @GetMapping("/time-slot-report")
    public Result getTimeSlotReport(
            @RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
            @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {

        List<TimeSlotReport> reports = reportService.getReservationCounts(start, end);

        return Result.success(reports);
    }


    /**
     * 教室使用情况
     * @param start
     * @param end
     * @return
     */
    @GetMapping("/usage")
    public Result getUsageSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        RoomUsageSummaryDTO roomUsageSummary = reportService.getRoomUsageSummary(start, end);
        return Result.success(roomUsageSummary);
    }

    @GetMapping("/typeusage")
    public Result getTypeUsageReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        List<RoomTypeUsageDTO> roomTypeUsageStats = reportService.getUsageStats(start, end);
        return Result.success(roomTypeUsageStats);
    }
}
