package com.bookingsystem.dto;

import lombok.Data;

@Data
public class AnnQueryDTO {
    private Long id;
    private String type;
    private Integer status;
    private String keyword;
    private String title;
    private Integer page = 1;
    private Integer pageSize = 10;
    private Integer pageNum = 1;
}
