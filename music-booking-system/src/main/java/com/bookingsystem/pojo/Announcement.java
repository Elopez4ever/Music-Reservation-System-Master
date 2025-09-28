package com.bookingsystem.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Announcement {
    private Long id; //公告id
    private String title; //标题
    private String content; //内容
    private Long publisherId; //发布者id
    private Integer priority; //优先级
    private Integer status; //状态
    private String type; //类型
    private LocalDateTime startTime; //开始时间
    private LocalDateTime endTime; //结束时间
    private Long viewCount; //浏览量
    private LocalDateTime createdAt; //创建时间
    private LocalDateTime updatedAt; //更新时间
}