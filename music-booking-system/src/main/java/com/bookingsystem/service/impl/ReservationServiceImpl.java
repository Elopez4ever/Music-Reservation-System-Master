package com.bookingsystem.service.impl;

import com.bookingsystem.dto.AvaDTO;
import com.bookingsystem.dto.ReservationDTO;
import com.bookingsystem.dto.ReservationQueryDTO;
import com.bookingsystem.enums.ReservationStatusEnums;
import com.bookingsystem.exception.BusinessException;
import com.bookingsystem.mapper.ReservationMapper;
import com.bookingsystem.mapper.RoomMapper;
import com.bookingsystem.pojo.PageResult;
import com.bookingsystem.pojo.Reservation;
import com.bookingsystem.pojo.Room;
import com.bookingsystem.pojo.TimeSlot;
import com.bookingsystem.qo.AvailableRoomQO;
import com.bookingsystem.service.ReservationService;
import com.bookingsystem.vo.UserReservationStatsVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationMapper reservationMapper;
    @Autowired
    private RoomMapper roomMapper;

    // 获取某天已预约时段
    @Override
    public List<TimeSlot> getRoomAvailability(AvaDTO dto) {
        String dateStr = dto.getDate().format(DateTimeFormatter.ISO_DATE);
        List<Reservation> reservations = reservationMapper.findReservedSlots(dto.getRoomId(), dateStr);
        return reservations.stream()
                .map(r -> new TimeSlot(r.getStartTime(), r.getEndTime()))
                .collect(Collectors.toList());
    }


    // 创建预约
    @Override
    @Transactional
    public Reservation createReservation(ReservationDTO dto, Long userId) throws Exception {
        // 基础验证
        validateReservation(dto);


        // 检查冲突
        if (reservationMapper.checkConflict(dto.getRoomId(),
                dto.getStartTime(),
                dto.getEndTime()) > 0) {
            throw new Exception("时间段已被预约");
        }

        Reservation reservation = new Reservation();
        BeanUtils.copyProperties(dto, reservation);
        reservation.setUserId(userId);
        int i = reservationMapper.create(reservation);

        return reservation;

    }

    @Override
    public List<Room> getAvailableByRoomTypeAndTimeQuantum(AvailableRoomQO availableRoomQO) {

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setStartTime(availableRoomQO.getStartTime());
        reservationDTO.setEndTime(availableRoomQO.getEndTime());
        validateReservation(reservationDTO);
        List<Room> rooms = reservationMapper.selectAvailableByRoomTypeAndTimeQuantum(availableRoomQO);
        return rooms;
    }

    /**
     * 查询用户预约
     */
    @Override
    public PageResult<Reservation> list(ReservationQueryDTO reservationQueryDTO) {
        reservationQueryDTO.setEndDate(reservationQueryDTO.getStartDate());
        reservationQueryDTO.setStartTime(reservationQueryDTO.getStartDateTime());
        reservationQueryDTO.setEndTime(reservationQueryDTO.getEndDateTime());
        PageHelper.startPage(reservationQueryDTO.getPageNum(), reservationQueryDTO.getPageSize());
        List<Reservation> list = reservationMapper.list(reservationQueryDTO);
        Page<Reservation> page = (Page<Reservation>) list;
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    @Override
    public Reservation getReservationById(Long id) {
        Reservation reservation = reservationMapper.selectReservationsById(id);
        return reservation;
    }

    @Override
    public Integer cancelReservation(Long id, String remark) {

        Reservation reservation = new Reservation();
        reservation.setId(id);
        reservation.setStatus(ReservationStatusEnums.CANCELLED.getCode());
        reservation.setRemarks("取消原因：" + remark);
        Integer update = reservationMapper.update(reservation);
        return update;
    }

    @Override
    public void updateReservation(Long id, Reservation reservation) {
        int i = 0;
        reservation.setId(id);
        reservationMapper.update(reservation);
    }

    @Override
    public UserReservationStatsVO getUserReservationStats(Long userId) {
        return reservationMapper.getUserReservationStats(userId);
    }

    private void validateReservation(ReservationDTO dto) {
        //验证预约时间是否在当前时间之前
        if (dto.getStartTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException("开始时间不能早于当前时间");
        }
        // 验证时间有效性
        if (dto.getStartTime().isAfter(dto.getEndTime())) {
            throw new BusinessException("开始时间不能晚于结束时间");
        }

        // 禁止跨天
        if (!isSameDay(dto.getStartTime(), dto.getEndTime())) {
            throw new BusinessException("不可跨天预约");
        }

        // 七天限制
        LocalDateTime maxTime = LocalDateTime.now().plusDays(7);
        if (dto.getEndTime().isAfter(maxTime)) {
            throw new BusinessException("预约时间不能超过7天");
        }

        // 检查该预约时间段是否在数据库中存在
//        List<Reservation> reservationListDb = reservationMapper.selectReservationsByRoomId(dto.getRoomId());
//        for (Reservation reservation : reservationListDb) {
//            LocalDateTime startTimeDb = reservation.getStartTime();
//            LocalDateTime endTimeDb = reservation.getEndTime();
////            if (!((dto.getStartTime().isAfter(startTimeDb) || dto.getStartTime().isEqual(startTimeDb)) && (dto.getEndTime().isBefore(endTimeDb) || dto.getEndTime().isEqual(endTimeDb) ))){
////                throw new BusinessException("该时间段已被预约,请查询其他时间段");
////            }
//            if (dto.getStartTime().isAfter(startTimeDb) && dto.getEndTime().isBefore(endTimeDb)) {
//                throw new BusinessException("该时间段已被预约,请查询其他时间段");
//            }
//            if (dto.getEndTime().isAfter(startTimeDb) && dto.getEndTime().isBefore(endTimeDb)) {
//                throw new BusinessException("该时间段已被预约,请查询其他时间段");
//            }
//        }

        // 验证教室存在
        if (dto.getRoomId() != null){
            Room room = roomMapper.getById(dto.getRoomId());
            if (room == null || room.getStatus() != 1) {
                throw new BusinessException("教室不可用");
            }
        }

    }

    //判断是否是同一天
    private boolean isSameDay(LocalDateTime date1, LocalDateTime date2) {
        LocalDate localDate1 = date1.toLocalDate();
        LocalDate localDate2 = date2.toLocalDate();
        return localDate1.isEqual(localDate2);
    }

}
