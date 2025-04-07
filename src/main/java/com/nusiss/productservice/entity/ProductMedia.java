package com.nusiss.productservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/*
 ProductMedia 实体类 - 用于存储商品的媒体资源（图片/视频等）
 */
@Data
@TableName("ProductMedia")
public class ProductMedia {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id; // 主键ID

    private Long productId; // 关联的产品ID
    private String mediaType; // 媒体类型，如 image、video
    private String url; // 媒体资源URL

    private LocalDateTime createDatetime; // 创建时间
    private LocalDateTime updateDatetime; // 更新时间
    private String createUser; // 创建人
    private String updateUser; // 更新人
}
