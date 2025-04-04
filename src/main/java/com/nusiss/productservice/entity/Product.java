package com.nusiss.productservice.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/*
Product 实体类，对应数据库中的 Products 表，用于存储商品的基本信息
*/
@Data // Lombok 注解，自动生成 getter、setter、toString 等方法
@Entity // 声明这是一个 JPA 实体
@Table(name = "Products") // 指定对应的数据库表名
public class Product {

    @Id // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增主键策略
    private Long id; // 商品 ID

    private String name; // 商品名称
    private String description; // 商品描述

    private BigDecimal price; // 商品价格
    private Integer stock; // 商品库存数量

    @Column(name = "seller_id") // 数据库字段是 seller_id
    private Long sellerId; // 卖家 ID

    private String category; // 商品分类
    private String title; // 商品标题
    private String status; // 商品状态（如上架、下架等）

    private Double rating; // 商品评分（用户平均评分）

    @Column(name = "create_datetime")
    private LocalDateTime createDatetime; // 创建时间

    @Column(name = "update_datetime")
    private LocalDateTime updateDatetime; // 更新时间

    @Column(name = "create_user")
    private String createUser; // 创建人

    @Column(name = "update_user")
    private String updateUser; // 更新人
}
