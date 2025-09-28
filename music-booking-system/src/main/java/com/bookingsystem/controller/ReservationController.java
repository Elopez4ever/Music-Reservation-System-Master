package com.bookingsystem.controller;

import com.bookingsystem.dto.AvaDTO;
import com.bookingsystem.dto.ReservationDTO;
import com.bookingsystem.dto.ReservationQueryDTO;
import com.bookingsystem.pojo.CancelReason;
import com.bookingsystem.pojo.Reservation;
import com.bookingsystem.pojo.Result;
import com.bookingsystem.pojo.TimeSlot;
import com.bookingsystem.qo.AvailableRoomQO;
import com.bookingsystem.service.ReservationService;
import com.bookingsystem.vo.UserReservationStatsVO;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/reservations")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;


    /**
     * 取消预约
     * @param id
     * @param remark
     * @return
     */
    @PutMapping("/{id}/cancel")
    public Result cancelReservation(@PathVariable Long id, @RequestBody CancelReason cancelReason){
        String remark = cancelReason.getRemarks();
        log.info("取消预约:{}", remark);
        return Result.success(reservationService.cancelReservation(id,remark));
    }

    @GetMapping("/{id}")
    public Result getReservationById(@PathVariable Long id){
        return Result.success(reservationService.getReservationById(id));
    }

    /**
     * 查询预约列表
     * @return
     */
    @GetMapping("/list")
    public Result list(ReservationQueryDTO reservationQueryDTO) {
        log.info("查询预约列表:{}", reservationQueryDTO);
        return Result.success(reservationService.list(reservationQueryDTO));
    }


    // 获取可用时段
    @GetMapping("/availability")
    public Result getAvailability( AvaDTO dto) {
        log.info("获取可用时段:{}", dto);
        List<TimeSlot> lists = reservationService.getRoomAvailability(dto);
        return Result.success(lists);
    }

    // 创建预约
    @PostMapping
    public Result createReservation(@RequestBody  ReservationDTO dto) {
        try {
            log.info("创建预约:{}", dto);
            //TODO userId 写死了
            return Result.success( reservationService.createReservation(dto,dto.getUserId()));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 查询指定时间段内空闲的教室
     * @param availableRoomQO
     * @return
     */
    @GetMapping("/getAvailableByRoomTypeAndTimeQuantum")
    public Result getAvailableByRoomTypeAndTimeQuantum(AvailableRoomQO availableRoomQO){
//        return null;
        return Result.success(reservationService.getAvailableByRoomTypeAndTimeQuantum(availableRoomQO));
    }

    /**
     * 更新预约
     */
    @PutMapping("{id}")
    public Result updateReservation(@PathVariable Long id, @RequestBody Reservation reservation){
        log.info("更新预约:{}", reservation);
        reservationService.updateReservation(id,reservation);
        return Result.success();
    }

    @GetMapping("/userStatistics")
    public Result getUserStats(@RequestParam Long userId) {
        UserReservationStatsVO stats = reservationService.getUserReservationStats(userId);
        return Result.success(stats);
    }
}
