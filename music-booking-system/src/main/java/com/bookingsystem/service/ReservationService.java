package com.bookingsystem.service;

import com.bookingsystem.dto.AvaDTO;
import com.bookingsystem.dto.ReservationDTO;
import com.bookingsystem.dto.ReservationQueryDTO;
import com.bookingsystem.pojo.*;
import com.bookingsystem.qo.AvailableRoomQO;
import com.bookingsystem.vo.UserReservationStatsVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReservationService {
    List<TimeSlot> getRoomAvailability(AvaDTO dto);

    // 创建预约
    @Transactional
    Reservation createReservation(ReservationDTO dto, Long userId) throws Exception;

    List<Room> getAvailableByRoomTypeAndTimeQuantum(AvailableRoomQO availableRoomQO);

    /**
     * 预约查询
     * @return
     */
    PageResult<Reservation>list(ReservationQueryDTO reservationQueryDTO);

    Reservation getReservationById(Long id);

    /**
     * 取消预约
     * @param id
     * @param remark
     * @return
     */
    Integer cancelReservation(Long id, String remark);

    /**
     * 更新预约
     * @param id
     * @param reservation
     */
    void updateReservation(Long id, Reservation reservation);

    UserReservationStatsVO getUserReservationStats(Long userId);
}
