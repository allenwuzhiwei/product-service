package com.nusiss.productservice.service;

import com.nusiss.productservice.entity.ProductFeedback;

import java.util.List;

/*
 ProductFeedback 服务层接口
 提供产品反馈相关的业务方法定义
 */
public interface ProductFeedbackService {

    /*
     创建新的产品反馈
     @param feedback 用户提交的反馈对象
     @return 创建成功的反馈记录
     */
    ProductFeedback createFeedback(ProductFeedback feedback);

    /*
     根据反馈 ID 获取反馈详情
     @param id 反馈主键
     @return 反馈对象，若不存在返回 null
     */
    ProductFeedback getFeedbackById(Long id);

    /*
     获取所有反馈信息
     @return 所有反馈列表
     */
    List<ProductFeedback> getAllFeedback();

    /*
     根据产品 ID 获取所有该产品的用户反馈
     @param productId 产品 ID
     @return 针对该产品的反馈列表
     */
    List<ProductFeedback> getFeedbackByProductId(Long productId);

    /*
     更新产品反馈信息
     @param feedback 更新后的反馈对象
     @return 是否更新成功
     */
    boolean updateFeedback(ProductFeedback feedback);

    /*
     根据反馈 ID 删除反馈记录
     @param id 反馈主键 ID
     @return 是否删除成功
     */
    boolean deleteFeedback(Long id);
}
