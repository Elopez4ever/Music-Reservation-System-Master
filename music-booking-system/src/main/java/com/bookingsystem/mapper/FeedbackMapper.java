package com.bookingsystem.mapper;

import com.bookingsystem.dto.FeedbackQueryDTO;
import com.bookingsystem.pojo.Feedback;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface FeedbackMapper {

    @Insert("INSERT INTO feedbacks (feedback_type, user_id,content, contact_info, status,created_at, updated_at) " +
            "VALUES (#{feedbackType}, #{userId},#{content}, #{contactInfo},#{status}, #{createdAt}, #{updatedAt})")
    int insertFeedback(Feedback feedback);


    List<Feedback> getAllFeedbacks(FeedbackQueryDTO feedbackQueryDTO);

    void deleteFeedbacks(List<Long> ids);

    @Update("UPDATE feedbacks SET status = #{status},updated_at = #{updatedAt} WHERE id = #{id}")
    void update(Feedback feedback);
}
