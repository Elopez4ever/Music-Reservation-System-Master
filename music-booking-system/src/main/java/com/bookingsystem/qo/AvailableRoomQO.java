package com.bookingsystem.qo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 功能：
 * 作者： lapis
 * 邮箱： lapis17@qq.com
 */
@Data
@Builder
public class AvailableRoomQO {

    /**
     *教室类型
     */
    private  String roomTypeId;


    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;


    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;
}
