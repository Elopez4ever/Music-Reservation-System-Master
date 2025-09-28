package com.bookingsystem.mapper;

import com.bookingsystem.dto.CountReservationsDTO;
import com.bookingsystem.dto.ReservationQueryDTO;
import com.bookingsystem.pojo.Reservation;
import com.bookingsystem.pojo.Room;
import com.bookingsystem.pojo.TimeSlotReport;
import com.bookingsystem.qo.AvailableRoomQO;
import com.bookingsystem.vo.DayOfWeekCountVO;
import com.bookingsystem.vo.ReservationCountVO;
import com.bookingsystem.vo.UserReservationStatsVO;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// ReservationMapper.java
@Mapper
public interface ReservationMapper {
    @Select("SELECT * FROM reservations WHERE room_id = #{roomId} " +
            "AND DATE(start_time) = #{date} " +
            "AND status IN ('pending', 'approved', 'completed')")
    List<Reservation> findReservedSlots(@Param("roomId") Long roomId,
                                        @Param("date") String date);

    @Insert("INSERT INTO reservations(user_id, room_id, title, purpose, " +
            "start_time, end_time, attendees, remarks) " +
            "VALUES(#{userId}, #{roomId}, #{title}, #{purpose}, " +
            "#{startTime}, #{endTime}, #{attendees}, #{remarks})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int create(Reservation reservation);

    @Select("SELECT COUNT(*) FROM reservations " +
            "WHERE room_id = #{roomId} " +
            "AND status IN ('pending', 'approved', 'completed') " +
            "AND ((start_time < #{endTime} AND end_time > #{startTime}))" )
    int checkConflict(Long roomId, LocalDateTime startTime, LocalDateTime endTime);


    int countReserve(CountReservationsDTO countReservationsDTO);

    int countUser(CountReservationsDTO countReservationsDTO);

    int countRoom(CountReservationsDTO countReservationsDTO);

    @Select("SELECT * FROM reservations WHERE room_id = #{roomId}")
    List<Reservation> selectReservationsByRoomId(Long roomId);


    List<Room> selectAvailableByRoomTypeAndTimeQuantum(AvailableRoomQO availableRoomQO);

    List<Reservation> list(ReservationQueryDTO reservationQueryDTO);

    Reservation selectReservationsById(@Param("id") Long id);

    Integer update(Reservation reservation);

    @Select("SELECT\n" +
            "    s.status as name,\n" +
            "    COUNT(r.id) AS count\n" +
            "FROM\n" +
            "    (\n" +
            "        SELECT 'pending' AS status\n" +
            "        UNION ALL SELECT 'approved'\n" +
            "        UNION ALL SELECT 'rejected'\n" +
            "        UNION ALL SELECT 'cancelled'\n" +
            "        UNION ALL SELECT 'completed'\n" +
            "    ) AS s\n" +
            "        LEFT JOIN reservations r ON s.status = r.status\n" +
            "GROUP BY s.status\n" +
            "ORDER BY FIELD(s.status, 'pending', 'approved', 'rejected', 'cancelled', 'completed');")
    List<ReservationCountVO> getReservationStatusDistribution();

    @Select("SELECT\n" +
            "    ANY_VALUE(\n" +
            "            CASE DAYOFWEEK(start_time)\n" +
            "                WHEN 1 THEN 'Sunday'\n" +
            "                WHEN 2 THEN 'Monday'\n" +
            "                WHEN 3 THEN 'Tuesday'\n" +
            "                WHEN 4 THEN 'Wednesday'\n" +
            "                WHEN 5 THEN 'Thursday'\n" +
            "                WHEN 6 THEN 'Friday'\n" +
            "                WHEN 7 THEN 'Saturday'\n" +
            "                END\n" +
            "        ) AS dayOfWeek,\n" +
            "    COUNT(*) AS count\n" +
            "FROM reservations\n" +
            "GROUP BY DAYOFWEEK(start_time)\n" +
            "ORDER BY DAYOFWEEK(start_time);")
    List<DayOfWeekCountVO> countReservationsByDayOfWeek();


    @Select("SELECT CASE " +
            "WHEN HOUR(start_time) BETWEEN 8 AND 9 THEN '08:00 - 10:00' " +
            "WHEN HOUR(start_time) BETWEEN 10 AND 11 THEN '10:00 - 12:00' " +
            "WHEN HOUR(start_time) BETWEEN 12 AND 13 THEN '12:00 - 14:00' " +
            "WHEN HOUR(start_time) BETWEEN 14 AND 15 THEN '14:00 - 16:00' " +
            "WHEN HOUR(start_time) BETWEEN 16 AND 17 THEN '16:00 - 18:00' " +
            "WHEN HOUR(start_time) BETWEEN 18 AND 19 THEN '18:00 - 20:00' " +
            "WHEN HOUR(start_time) BETWEEN 20 AND 21 THEN '20:00 - 22:00' " +
            "END AS time_slot, " +
            "COUNT(*) AS reservation_count " +
            "FROM reservations " +
            "WHERE start_time >= #{startDateTime} " +
            "AND end_time <= #{endDateTime} " +
            "AND status = 'approved' " +
            "GROUP BY time_slot " +
            "ORDER BY time_slot")
    List<TimeSlotReport> getReservationCountsByTimeSlot(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime);

    // 获取已批准且当前时间超过预约结束时间的预约记录
    List<Reservation> getApprovedReservations(LocalDateTime now);

    UserReservationStatsVO getUserReservationStats(@Param("userId") Long userId);
}