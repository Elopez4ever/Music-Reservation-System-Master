package com.bookingsystem.service;

import com.bookingsystem.dto.AnnQueryDTO;
import com.bookingsystem.pojo.Announcement;
import com.bookingsystem.pojo.PageResult;
import com.bookingsystem.vo.AnnQueryVo;

public interface AnnService {
    PageResult<AnnQueryVo> list(AnnQueryDTO annQueryDTO);

    void save(Announcement announcement);

    Announcement getById(Long id);

    void update(Announcement announcement);

    void delete(Long[] ids);

    void incrementViewCount(Long id);

    void setStatus(Integer status, Long id);
}
