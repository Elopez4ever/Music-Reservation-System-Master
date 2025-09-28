package com.bookingsystem.service.impl;

import com.bookingsystem.dto.AnnQueryDTO;
import com.bookingsystem.mapper.AnnMapper;
import com.bookingsystem.pojo.Announcement;
import com.bookingsystem.pojo.PageResult;
import com.bookingsystem.service.AnnService;
import com.bookingsystem.vo.AnnQueryVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnnServiceImpl implements AnnService {
    @Autowired
    private AnnMapper annMapper;
    @Override
    public PageResult<AnnQueryVo> list(AnnQueryDTO annQueryDTO) {
        PageHelper.startPage(annQueryDTO.getPage(),annQueryDTO.getPageSize());
        List<AnnQueryVo> list = annMapper.list(annQueryDTO);
        Page<AnnQueryVo> page = (Page<AnnQueryVo>) list;
        return new PageResult<>(page.getTotal(),page.getResult());
    }

    @Override
    public void save(Announcement announcement) {
        announcement.setCreatedAt(LocalDateTime.now());
        announcement.setUpdatedAt(LocalDateTime.now());
        announcement.setPublisherId(1L); //ToDo 发布人id后期需要修改
        announcement.setViewCount(0L);
        if (announcement.getStatus() == 1){
            announcement.setStartTime(LocalDateTime.now());
        }
        annMapper.save(announcement);
    }

    @Override
    public Announcement getById(Long id) {
        return annMapper.getById(id);
    }

    @Override
    public void update(Announcement announcement) {
        announcement.setUpdatedAt(LocalDateTime.now());
        announcement.setViewCount(0L);
        annMapper.update(announcement);
    }

    @Override
    public void delete(Long[] ids) {
        annMapper.delete(ids);
    }

    @Override
    public void incrementViewCount(Long id) {
        Announcement announcement = annMapper.getById(id);
        if (announcement != null){
            annMapper.updateViewCount(id);
        }else {
            throw new RuntimeException("公告不存在");
        }
    }

    @Override
    public void setStatus(Integer status, Long id) {
        Announcement announcement = new Announcement();
        announcement.setId(id);
        announcement.setStatus(status);
        announcement.setStartTime(LocalDateTime.now());
        announcement.setUpdatedAt(LocalDateTime.now());
        annMapper.update(announcement);
    }
}
