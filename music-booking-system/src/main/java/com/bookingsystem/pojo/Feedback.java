package com.bookingsystem.pojo;

import com.bookingsystem.enums.FeedbackType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Feedback {
    private Long id;
    /**
     * 反馈类型：功能异常（BUG）、功能建议（SUGGESTION）、其他（OTHER）
     */
    private FeedbackType feedbackType;
    private Long userId;
    /**
     * 反馈内容（用户描述）
     */
    private String content;
    /**
     * 联系方式（邮箱或手机号）
     */
    private String contactInfo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer  status = 0;
}
