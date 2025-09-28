package com.bookingsystem.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnnQueryVo {
    private  Long id;
    private String title;
    private String type;
    private Integer status;
    private String content;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer viewCount;

}
