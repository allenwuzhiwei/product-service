package com.nusiss.productservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/*
 ProductFeedback 实体类
 对应数据库中的 product_feedback 表，用于存储用户对商品的反馈信息（评论与评分）
 */
@Data
@TableName("product_feedback")
public class ProductFeedback {

    @TableId(value = "id", type = IdType.AUTO) // 主键自增
    private Long id; // 反馈主键 ID

    private Long productId; // 对应的商品 ID（外键）

    private Long userId; // 提交反馈的用户 ID

    private Integer rating; // 评分（例如 1-5 星）

    private String comment; // 文字评论内容

    private LocalDateTime createDatetime; // 反馈创建时间

    private LocalDateTime updateDatetime; // 反馈最后更新时间

    private String createUser; // 创建人用户名

    private String updateUser; // 最后更新人用户名
}
