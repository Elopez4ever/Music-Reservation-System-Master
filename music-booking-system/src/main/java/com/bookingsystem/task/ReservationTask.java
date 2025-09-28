package com.bookingsystem.task;

import com.bookingsystem.dto.ReservationQueryDTO;
import com.bookingsystem.mapper.ReservationMapper;
import com.bookingsystem.pojo.Reservation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class ReservationTask {
    @Autowired
    private ReservationMapper reservationMapper;

    //每小时执行一次
    @Scheduled(cron = "0 0 * * * ?")
    public void checkReservationStatus() {
        log.info("Checking reservation status...");
        List<Reservation> reservationList = reservationMapper.getApprovedReservations(LocalDateTime.now());
        log.info("Found {} approved reservations：{}.", reservationList.size(), reservationList);
        reservationList.forEach(reservation -> {
            reservation.setStatus("completed");
            reservationMapper.update(reservation);
        });
    }
}
