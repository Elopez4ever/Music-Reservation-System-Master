package com.bookingsystem.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.util.Date;

/**
 * 系统基础设置实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicSetting implements Serializable {



    /**
     * 系统名称
     */
    private String systemName = "教室预约系统";

    /**
     * 系统LOGO URL
     */

    private String logo;

    /**
     * 网站图标 URL
     */

    private String favicon;

    /**
     * 主题色
     */

    private String primaryColor ;

    /**
     * 系统描述
     */

    private String description;

    /**
     * 版权信息
     */

    private String copyright;

    /**
     * 联系电话
     */
    private String phone;


    /**
     * 邮箱
     */
    private String email;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")

    private Date createdAt;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")

    private Date updatedAt;

    /**
     * 创建人ID
     */

    private Long createdBy;

    /**
     * 更新人ID
     */

    private Long updatedBy;

    /**
     * 数据更新前处理
     */

    public void prePersist() {
        this.createdAt = new Date();
        this.updatedAt = this.createdAt;
    }

    /**
     * 数据更新前处理
     */

    public void preUpdate() {
        this.updatedAt = new Date();
    }
}