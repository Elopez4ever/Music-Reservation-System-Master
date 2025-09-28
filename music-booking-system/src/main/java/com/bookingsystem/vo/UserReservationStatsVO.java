package com.bookingsystem.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReservationStatsVO {
    private Integer totalCount;        // 总预约次数
    private Integer completedCount;    // 完成
    private Integer cancelledCount;    // 取消
    private Integer pendingCount;      // 待审核
    private Integer rejectedCount;     // 拒绝
}