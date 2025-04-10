package com.nusiss.productservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/*
按照MyBatis Plus风格创建
Product 实体类，对应数据库中的 Products 表，用于存储商品的基本信息
*/
@Data // Lombok 注解，自动生成 getter、setter、toString 等方法
@TableName("Products") // 数据库中真实表名
public class Product {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Long sellerId;
    private String category;
    private String title;
    private String status;
    private Double rating;

    private LocalDateTime createDatetime;
    private LocalDateTime updateDatetime;
    private String createUser;
    private String updateUser;

    /*
     封面图 URL（从 ProductMedia 中动态查询）
     */
    @TableField(exist = false)
    private String coverImageUrl;

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }
}
