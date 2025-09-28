package com.bookingsystem.service.impl;

import com.bookingsystem.dto.FeedbackQueryDTO;
import com.bookingsystem.mapper.FeedbackMapper;
import com.bookingsystem.pojo.Feedback;
import com.bookingsystem.pojo.PageResult;
import com.bookingsystem.service.FeedbackService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Override
    public void submitFeedback(Feedback feedback) {
        //TODo 用户id
        feedback.setCreatedAt(LocalDateTime.now());
        feedback.setUpdatedAt(LocalDateTime.now());
        feedbackMapper.insertFeedback(feedback);
    }

    @Override
    public PageResult<Feedback> getAllFeedbacks(FeedbackQueryDTO feedbackQueryDTO) {
        PageHelper.startPage(feedbackQueryDTO.getPage(), feedbackQueryDTO.getPageSize());
        List<Feedback> allFeedbacks = feedbackMapper.getAllFeedbacks(feedbackQueryDTO);
        Page<Feedback> p = (Page<Feedback>) allFeedbacks;
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    @Override
    public void deleteFeedbacks(List<Long> ids) {
        feedbackMapper.deleteFeedbacks(ids);
    }

    @Override
    public void setStatus(Integer status, Long id) {
        Feedback feedback = new Feedback();
        feedback.setId(id);
        feedback.setStatus(status);
        feedback.setUpdatedAt(LocalDateTime.now());
        feedbackMapper.update(feedback);
    }
}
