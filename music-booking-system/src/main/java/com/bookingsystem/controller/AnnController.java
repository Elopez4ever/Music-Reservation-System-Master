package com.bookingsystem.controller;

import com.bookingsystem.annotation.Log;
import com.bookingsystem.dto.AnnQueryDTO;
import com.bookingsystem.pojo.Announcement;
import com.bookingsystem.pojo.PageResult;
import com.bookingsystem.pojo.Result;
import com.bookingsystem.service.impl.AnnServiceImpl;
import com.bookingsystem.vo.AnnQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/announcements")
public class AnnController {
    @Autowired
    private AnnServiceImpl annService;

    /**
     * 获取公告列表(含条件查询)
     * @param annQueryDTO
     * @return
     */
    @GetMapping
    public Result list(AnnQueryDTO annQueryDTO) {
        log.info("获取公告列表(含条件查询):{}", annQueryDTO);
        annQueryDTO.setPage(annQueryDTO.getPageNum());
        PageResult<AnnQueryVo> pageResult = annService.list(annQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 添加公告
     */
    @Log(module = "公告管理", type = "添加公告", description = "添加新的公告")
    @PostMapping
    public Result save(@RequestBody Announcement announcement) {
        log.info("添加公告:{}", announcement);
        annService.save(announcement);
        return Result.success();
    }

    /**
     * 根据id获取公告信息
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        log.info("根据id获取公告信息:{}", id);
        Announcement announcement = annService.getById(id);
        return Result.success(announcement);
    }

    /**
     * 修改公告
     * @param announcement
     * @return
     */
    @Log(module = "公告管理", type = "修改公告", description = "修改新的公告")
    @PutMapping
    public Result update(@RequestBody Announcement announcement) {
        log.info("修改公告:{}", announcement);
        annService.update(announcement);
        return Result.success();
    }

    /**
     * 删除公告
     * @param ids
     * @return
     */
    @Log(module = "公告管理", type = "删除公告", description = "删除新的公告")
    @DeleteMapping
    public Result delete(@RequestBody Long[] ids) {
        log.info("删除公告:{}", ids);
        annService.delete(ids);
        return Result.success();
    }

    /**
     * 阅读量统计
     * @param id
     * @return
     */
    @PostMapping("/{id}/view")
    public Result incrementViewCount(@PathVariable Long id) {
        log.info("查看公告:{}", id);
        try {
            annService.incrementViewCount(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 设置发布状态
     */
    @Log(module = "公告管理", type = "设置发布状态", description = "设置新的发布状态")
    @PostMapping("/status/{status}")
    public Result setStatus(@PathVariable Integer status, Long id) {
        log.info("设置发布状态:{},{}", status, id);
        try {
            annService.setStatus(status, id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

}
