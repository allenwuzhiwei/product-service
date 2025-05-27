package com.nusiss.productservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nusiss.productservice.dao.ProductFeedbackMapper;
import com.nusiss.productservice.entity.ProductFeedback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductFeedbackServiceImplTest {

    @InjectMocks
    private ProductFeedbackServiceImpl feedbackService;

    @Mock
    private ProductFeedbackMapper productFeedbackMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateFeedback() {
        ProductFeedback feedback = new ProductFeedback();
        feedback.setRating(5);
        ProductFeedback result = feedbackService.createFeedback(feedback);
        verify(productFeedbackMapper).insert(feedback);
        assertEquals(5, result.getRating());
    }

    @Test
    void testGetFeedbackById() {
        ProductFeedback feedback = new ProductFeedback();
        feedback.setId(1L);
        when(productFeedbackMapper.selectById(1L)).thenReturn(feedback);
        ProductFeedback result = feedbackService.getFeedbackById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetAllFeedback() {
        when(productFeedbackMapper.selectList(null)).thenReturn(List.of(new ProductFeedback()));
        List<ProductFeedback> list = feedbackService.getAllFeedback();
        assertEquals(1, list.size());
    }

    @Test
    void testGetFeedbackByProductId() {
        when(productFeedbackMapper.selectList(any())).thenReturn(List.of(new ProductFeedback()));
        List<ProductFeedback> list = feedbackService.getFeedbackByProductId(1L);
        assertEquals(1, list.size());
    }

    @Test
    void testUpdateFeedback_Success() {
        when(productFeedbackMapper.updateById(any(ProductFeedback.class))).thenReturn(1);
        boolean result = feedbackService.updateFeedback(new ProductFeedback());
        assertTrue(result);
    }

    @Test
    void testUpdateFeedback_Fail() {
        when(productFeedbackMapper.updateById(any(ProductFeedback.class))).thenReturn(0);
        boolean result = feedbackService.updateFeedback(new ProductFeedback());
        assertFalse(result);
    }

    @Test
    void testDeleteFeedback_Success() {
        when(productFeedbackMapper.deleteById(1L)).thenReturn(1);
        assertTrue(feedbackService.deleteFeedback(1L));
    }

    @Test
    void testDeleteFeedback_Fail() {
        when(productFeedbackMapper.deleteById(1L)).thenReturn(0);
        assertFalse(feedbackService.deleteFeedback(1L));
    }

    @Test
    void testGetAverageRatingByProductId_WithRatings() {
        ProductFeedback f1 = new ProductFeedback();
        f1.setRating(4);
        ProductFeedback f2 = new ProductFeedback();
        f2.setRating(5);
        when(productFeedbackMapper.selectList(any())).thenReturn(List.of(f1, f2));

        Double avg = feedbackService.getAverageRatingByProductId(1L);
        assertEquals(4.5, avg);
    }

    @Test
    void testGetAverageRatingByProductId_EmptyList() {
        when(productFeedbackMapper.selectList(any())).thenReturn(List.of());
        assertNull(feedbackService.getAverageRatingByProductId(1L));
    }

    @Test
    void testGetCommentCountByProductId() {
        when(productFeedbackMapper.selectCount(any())).thenReturn(3L);
        int count = feedbackService.getCommentCountByProductId(1L);
        assertEquals(3, count);
    }

    @Test
    void testGetFeedbackByProductIdWithPageAndSort_RatingDesc() {
        ProductFeedback f = new ProductFeedback();
        Page<ProductFeedback> mockPage = new Page<>();
        mockPage.setRecords(List.of(f));

        when(productFeedbackMapper.selectPage(any(), any(LambdaQueryWrapper.class))).thenReturn(mockPage);

        IPage<ProductFeedback> result = feedbackService.getFeedbackByProductIdWithPageAndSort(
                1L, 1, 10, "rating", "desc");

        assertEquals(1, result.getRecords().size());
    }

    @Test
    void testGetFeedbackByProductIdWithPageAndSort_CreateTimeAsc() {
        ProductFeedback f = new ProductFeedback();
        f.setCreateDatetime(LocalDateTime.now());

        Page<ProductFeedback> mockPage = new Page<>();
        mockPage.setRecords(List.of(f));

        when(productFeedbackMapper.selectPage(any(), any(LambdaQueryWrapper.class))).thenReturn(mockPage);

        IPage<ProductFeedback> result = feedbackService.getFeedbackByProductIdWithPageAndSort(
                1L, 1, 10, "create_datetime", "asc");

        assertEquals(1, result.getRecords().size());
    }

    @Test
    void testGetFeedbackByProductIdWithPageAndSort_InvalidSortBy() {
        Page<ProductFeedback> mockPage = new Page<>();
        mockPage.setRecords(List.of(new ProductFeedback()));

        when(productFeedbackMapper.selectPage(any(), any(LambdaQueryWrapper.class))).thenReturn(mockPage);

        IPage<ProductFeedback> result = feedbackService.getFeedbackByProductIdWithPageAndSort(
                1L, 1, 10, "invalid_field", "desc");

        assertEquals(1, result.getRecords().size()); // 应该也能返回结果，但无排序
    }
}
