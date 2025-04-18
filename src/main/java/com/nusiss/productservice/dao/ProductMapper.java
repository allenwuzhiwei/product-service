package com.nusiss.productservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nusiss.productservice.entity.Product;
import org.apache.ibatis.annotations.Mapper;

/*
Product 数据访问层
ProductMapper 接口，继承 MyBatis-Plus 的 BaseMapper
自动提供对 Product 实体的 CRUD 操作，所以无需手动编写 SQL
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    // 注BaseMapper 已经包含常用 CRUD
}
