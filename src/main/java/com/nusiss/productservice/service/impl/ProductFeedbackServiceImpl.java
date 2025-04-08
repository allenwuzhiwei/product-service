
package com.nusiss.productservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nusiss.productservice.dao.ProductFeedbackMapper;
import com.nusiss.productservice.entity.ProductFeedback;
import com.nusiss.productservice.service.ProductFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 ProductFeedbackService 实现类，负责处理产品反馈的具体业务逻辑
 */
@Service
public class ProductFeedbackServiceImpl implements ProductFeedbackService {

    @Autowired
    private ProductFeedbackMapper productFeedbackMapper;

    /*
     创建新的产品反馈
     @param feedback 用户提交的反馈对象
     @return 创建成功的反馈记录
     */
    @Override
    public ProductFeedback createFeedback(ProductFeedback feedback) {
        productFeedbackMapper.insert(feedback);
        return feedback;
    }

    /*
     根据反馈 ID 获取反馈详情
     @param id 反馈主键
     @return 反馈对象，若不存在返回 null
     */
    @Override
    public ProductFeedback getFeedbackById(Long id) {
        return productFeedbackMapper.selectById(id);
    }

    /*
     获取所有反馈信息
     @return 所有反馈列表
     */
    @Override
    public List<ProductFeedback> getAllFeedback() {
        return productFeedbackMapper.selectList(null);
    }

    /*
     根据产品 ID 获取所有该产品的用户反馈
     @param productId 产品 ID
     @return 针对该产品的反馈列表
     */
    @Override
    public List<ProductFeedback> getFeedbackByProductId(Long productId) {
        QueryWrapper<ProductFeedback> wrapper = new QueryWrapper<>();
        wrapper.eq("product_id", productId);
        return productFeedbackMapper.selectList(wrapper);
    }

    /*
     更新产品反馈信息
     @param feedback 更新后的反馈对象
     @return 是否更新成功
     */
    @Override
    public boolean updateFeedback(ProductFeedback feedback) {
        return productFeedbackMapper.updateById(feedback) > 0;
    }

    /*
     根据反馈 ID 删除反馈记录
     @param id 反馈主键 ID
     @return 是否删除成功
     */
    @Override
    public boolean deleteFeedback(Long id) {
        return productFeedbackMapper.deleteById(id) > 0;
    }
}

