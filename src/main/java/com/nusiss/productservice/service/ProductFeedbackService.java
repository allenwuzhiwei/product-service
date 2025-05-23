package com.nusiss.productservice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
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

    /*
     扩展功能1：
     获取某个产品的平均评分
     @param productId 产品 ID
     @return 平均评分（若无评分记录返回 null）
     */
    Double getAverageRatingByProductId(Long productId);

    /*
     扩展功能2：
     获取某个产品的评论数量
     @param productId 产品 ID
     @return 评论数（int）
     */
    int getCommentCountByProductId(Long productId);

    /*
     扩展功能 3：根据产品 ID 获取反馈（支持分页 + 排序）

     @param productId 产品 ID
     @param page 当前页码（从 1 开始）
     @param size 每页数量
     @param sortBy 排序字段，例如 "rating", "create_datetime"
     @param order 排序方式，"asc" 表示升序，"desc" 表示降序
     @return 分页后的反馈记录
     */
    IPage<ProductFeedback> getFeedbackByProductIdWithPageAndSort(Long productId, int page, int size, String sortBy, String order);

}
