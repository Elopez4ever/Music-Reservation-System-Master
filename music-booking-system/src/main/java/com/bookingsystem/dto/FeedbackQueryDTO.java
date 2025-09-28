package com.bookingsystem.dto;

import lombok.Data;

@Data
public class FeedbackQueryDTO {
    private String feedbackType;
    private Integer status;
    private Integer page = 1; //默认第一页
    private Integer pageSize = 10; //默认每页10条数据
}
