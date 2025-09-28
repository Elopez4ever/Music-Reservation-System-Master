package com.bookingsystem.service;

import com.bookingsystem.dto.FeedbackQueryDTO;
import com.bookingsystem.pojo.Feedback;
import com.bookingsystem.pojo.PageResult;

import java.util.List;

public interface FeedbackService {
    void submitFeedback(Feedback feedback);
    PageResult<Feedback> getAllFeedbacks(FeedbackQueryDTO feedbackQueryDTO);

    void deleteFeedbacks(List<Long> ids);

    void setStatus(Integer status, Long id);
}
