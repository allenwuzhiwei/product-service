package com.nusiss.productservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nusiss.productservice.entity.ProductMedia;
import org.apache.ibatis.annotations.Mapper;

/*
 ProductMedia 的 Mapper 接口
 继承 MyBatis Plus 的 BaseMapper，提供基本的 CRUD 功能
 */
@Mapper
public interface ProductMediaMapper extends BaseMapper<ProductMedia> {
}
