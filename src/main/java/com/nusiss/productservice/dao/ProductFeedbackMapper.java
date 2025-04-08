package com.nusiss.productservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nusiss.productservice.entity.ProductFeedback;
import org.apache.ibatis.annotations.Mapper;

/*
 ProductFeedbackMapper 接口
 继承 MyBatis Plus 的 BaseMapper，自动拥有基本的 CRUD 操作方法
 */
@Mapper
public interface ProductFeedbackMapper extends BaseMapper<ProductFeedback> {
}
