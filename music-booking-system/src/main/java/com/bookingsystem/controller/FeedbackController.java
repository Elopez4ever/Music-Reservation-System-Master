package com.bookingsystem.controller;

import com.bookingsystem.dto.FeedbackQueryDTO;
import com.bookingsystem.pojo.Feedback;
import com.bookingsystem.pojo.PageResult;
import com.bookingsystem.pojo.Result;
import com.bookingsystem.service.FeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    /**
     * 提交反馈
     * @param feedback
     * @return
     */
    @PostMapping
    public Result submitFeedback(@RequestBody Feedback feedback) {
        feedbackService.submitFeedback(feedback);
        return Result.success();
    }

    /**
     * 获取所有反馈
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping
    public Result getAllFeedbacks(FeedbackQueryDTO feedbackQueryDTO) {
        PageResult<Feedback> pageResult = feedbackService.getAllFeedbacks(feedbackQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 批量删除反馈
     */
    @DeleteMapping
    public Result deleteFeedbacks(@RequestBody List<Long> ids) {
        log.info("删除反馈:{}", ids);
        feedbackService.deleteFeedbacks(ids);
        return Result.success();
    }

    /**
     * 设置反馈状态
     */
    @PutMapping("/status/{status}")
    public Result setStatus(Long id,@PathVariable Integer status) {
        log.info("设置反馈状态:{},id:{}", status, id);
        feedbackService.setStatus(status, id);
        return Result.success();
    }
}
