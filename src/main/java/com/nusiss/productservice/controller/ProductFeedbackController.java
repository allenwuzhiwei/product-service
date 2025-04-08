package com.nusiss.productservice.controller;

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
}
