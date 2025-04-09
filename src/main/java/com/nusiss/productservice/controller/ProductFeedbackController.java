package com.nusiss.productservice.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nusiss.productservice.config.ApiResponse;
import com.nusiss.productservice.entity.ProductFeedback;
import com.nusiss.productservice.service.ProductFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 ProductFeedback 控制器
 提供产品反馈的 RESTful 接口
 */
@RestController
@RequestMapping("/feedback")
public class ProductFeedbackController {

    @Autowired
    private ProductFeedbackService feedbackService;

    /*
     创建产品反馈
     @param feedback 前端传入的反馈对象
     @return 创建成功的反馈信息
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ProductFeedback>> createFeedback(@RequestBody ProductFeedback feedback) {
        ProductFeedback created = feedbackService.createFeedback(feedback);
        return ResponseEntity.status(201).body(new ApiResponse<>(true, "Feedback created successfully", created));
    }

    /*
     获取所有反馈
     @return 所有反馈列表
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductFeedback>>> getAllFeedback() {
        List<ProductFeedback> feedbackList = feedbackService.getAllFeedback();
        return ResponseEntity.ok(new ApiResponse<>(true, "All feedback retrieved", feedbackList));
    }

    /*
     根据反馈 ID 获取反馈详情
     @param id 反馈主键
     @return 反馈详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductFeedback>> getFeedbackById(@PathVariable Long id) {
        ProductFeedback feedback = feedbackService.getFeedbackById(id);
        if (feedback != null) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Feedback retrieved successfully", feedback));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Feedback not found", null));
        }
    }

    /*
     根据产品 ID 获取该产品的所有反馈
     @param productId 产品 ID
     @return 反馈列表
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<ProductFeedback>>> getFeedbackByProductId(@PathVariable Long productId) {
        List<ProductFeedback> feedbackList = feedbackService.getFeedbackByProductId(productId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Feedback for product retrieved", feedbackList));
    }

    /*
     更新反馈内容
     @param id 反馈 ID
     @param updated 更新后的反馈对象
     @return 更新后的反馈信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductFeedback>> updateFeedback(@PathVariable Long id, @RequestBody ProductFeedback updated) {
        updated.setId(id);
        boolean success = feedbackService.updateFeedback(updated);
        if (success) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Feedback updated successfully", updated));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Feedback not found", null));
        }
    }

    /*
     删除反馈
     @param id 反馈 ID
     @return 删除成功或失败信息
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteFeedback(@PathVariable Long id) {
        boolean success = feedbackService.deleteFeedback(id);
        if (success) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Feedback deleted successfully", null));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Feedback not found", null));
        }
    }

    /*
     扩展功能1 ：获取某个产品的平均评分
     @param productId 产品 ID
     @return 平均评分（double），若没有评分记录则返回 null
     */
    @GetMapping("/average-rating")
    public ResponseEntity<ApiResponse<Double>> getAverageRating(@RequestParam Long productId) {
        Double averageRating = feedbackService.getAverageRatingByProductId(productId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Average rating retrieved successfully", averageRating));
    }

    /*
     扩展功能2 ：获取某个产品的评论数
     @param productId 产品 ID
     @return 评论数（int）
     */
    @GetMapping("/comment-count")
    public ResponseEntity<ApiResponse<Integer>> getCommentCount(@RequestParam Long productId) {
        int commentCount = feedbackService.getCommentCountByProductId(productId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Comment count retrieved successfully", commentCount));
    }

    /*
     扩展功能 3：根据产品 ID 获取反馈（分页 + 排序）
     支持分页参数 page 和 size，排序参数 sortBy（如 rating、create_datetime），order（asc 或 desc）

     @param productId 产品 ID
     @param page 当前页码（默认 1）
     @param size 每页数量（默认 10）
     @param sortBy 排序字段（可选："rating"、"create_datetime"）
     @param order 排序方式（可选："asc"、"desc"）
     @return 分页后的反馈列表
    */
    @GetMapping("/by-product") // /feedback/by-product?productId=1&page=1&size=10&sortBy=rating&order=desc
    public ResponseEntity<ApiResponse<IPage<ProductFeedback>>> getFeedbackByProductIdWithPageAndSort //
    (
            @RequestParam Long productId,                        // 必填：产品 ID
            @RequestParam(defaultValue = "1") int page,          // 可选：页码，默认从第 1 页开始
            @RequestParam(defaultValue = "10") int size,         // 可选：每页记录数，默认显示 10 条
            @RequestParam(required = false) String sortBy,       // 可选：排序字段，如 rating、create_datetime
            @RequestParam(required = false) String order         // 可选：排序方式，asc（升序）或 desc（降序）
    )
    {
        // 调用 Service 方法，根据产品 ID 分页查询评论，同时应用排序逻辑
        IPage<ProductFeedback> feedbackPage = feedbackService.getFeedbackByProductIdWithPageAndSort(
                productId, page, size, sortBy, order
        );
        // 使用统一响应封装返回成功数据
        return ResponseEntity.ok(new ApiResponse<>(true, "Feedbacks retrieved successfully", feedbackPage));
    }

}
