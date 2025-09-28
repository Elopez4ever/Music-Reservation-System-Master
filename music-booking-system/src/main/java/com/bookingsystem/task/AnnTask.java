package com.bookingsystem.task;

import com.bookingsystem.dto.AnnQueryDTO;
import com.bookingsystem.mapper.AnnMapper;
import com.bookingsystem.pojo.Announcement;
import com.bookingsystem.vo.AnnQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class AnnTask {
    @Autowired
    private AnnMapper annMapper;
    //每天自动检查当前时间是否超过公告过期时间，若是则修改其状态为已过期
    //每小时自动执行一次
    @Scheduled(cron = "0 0 * * * ?")
    public void checkAnnStatus() {
        log.info("开始检查公告状态");
        //获取当前时间
        LocalDateTime now = LocalDateTime.now();
        //遍历公告列表，判断每个公告的过期时间是否小于当前时间，若是则修改其状态为已过期
        //获取公告列表
        AnnQueryDTO annQueryDTO = new AnnQueryDTO();
        annQueryDTO.setStatus(1);
        List<AnnQueryVo> lists = annMapper.list(annQueryDTO);
        for (AnnQueryVo annQueryVo : lists) {
            log.info("检查公告:{}", annQueryVo);
            Announcement announcement = new Announcement();
            BeanUtils.copyProperties(annQueryVo,announcement);
            log.info("公告:{}",announcement);
            if (announcement.getEndTime().isBefore(now)) {
                announcement.setStatus(2);
                annMapper.update(announcement);
            }
        }
        log.info("结束检查公告状态");
    }
}
