package com.nusiss.productservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nusiss.productservice.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/*
Product 数据访问层
ProductMapper 接口，继承 MyBatis-Plus 的 BaseMapper
自动提供对 Product 实体的 CRUD 操作，所以无需手动编写 SQL
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    /*
     查询指定分类下评分最高的前 5 个商品
     */
    @Select("SELECT * FROM Products WHERE category = #{category} ORDER BY rating DESC LIMIT 5")
    List<Product> findTop5ByCategoryOrderByRatingDesc(@Param("category") String category);
}
