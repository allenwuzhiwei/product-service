package com.nusiss.productservice.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nusiss.productservice.config.ApiResponse;
import com.nusiss.productservice.entity.ProductFeedback;
import com.nusiss.productservice.service.ProductFeedbackService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductFeedbackController.class)
class ProductFeedbackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductFeedbackService feedbackService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateFeedback() throws Exception {
        ProductFeedback feedback = new ProductFeedback();
        feedback.setRating(4);
        Mockito.when(feedbackService.createFeedback(any())).thenReturn(feedback);

        mockMvc.perform(post("/feedback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(feedback)))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetAllFeedback() throws Exception {
        Mockito.when(feedbackService.getAllFeedback()).thenReturn(List.of(new ProductFeedback()));

        mockMvc.perform(get("/feedback"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetFeedbackById_Found() throws Exception {
        ProductFeedback fb = new ProductFeedback();
        fb.setId(1L);
        Mockito.when(feedbackService.getFeedbackById(1L)).thenReturn(fb);

        mockMvc.perform(get("/feedback/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void testGetFeedbackById_NotFound() throws Exception {
        Mockito.when(feedbackService.getFeedbackById(99L)).thenReturn(null);

        mockMvc.perform(get("/feedback/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetFeedbackByProductId() throws Exception {
        Mockito.when(feedbackService.getFeedbackByProductId(1L))
                .thenReturn(List.of(new ProductFeedback()));

        mockMvc.perform(get("/feedback/product/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateFeedback_Success() throws Exception {
        ProductFeedback update = new ProductFeedback();
        update.setRating(5);
        Mockito.when(feedbackService.updateFeedback(any())).thenReturn(true);

        mockMvc.perform(put("/feedback/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateFeedback_Fail() throws Exception {
        Mockito.when(feedbackService.updateFeedback(any())).thenReturn(false);

        mockMvc.perform(put("/feedback/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ProductFeedback())))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteFeedback_Success() throws Exception {
        Mockito.when(feedbackService.deleteFeedback(1L)).thenReturn(true);

        mockMvc.perform(delete("/feedback/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteFeedback_Fail() throws Exception {
        Mockito.when(feedbackService.deleteFeedback(99L)).thenReturn(false);

        mockMvc.perform(delete("/feedback/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAverageRating() throws Exception {
        Mockito.when(feedbackService.getAverageRatingByProductId(1L)).thenReturn(4.5);

        mockMvc.perform(get("/feedback/average-rating?productId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(4.5));
    }

    @Test
    void testGetCommentCount() throws Exception {
        Mockito.when(feedbackService.getCommentCountByProductId(1L)).thenReturn(5);

        mockMvc.perform(get("/feedback/comment-count?productId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(5));
    }

    @Test
    void testGetFeedbackByProductIdWithPageAndSort() throws Exception {
        Page<ProductFeedback> page = new Page<>();
        page.setRecords(List.of(new ProductFeedback()));
        Mockito.when(feedbackService.getFeedbackByProductIdWithPageAndSort(anyLong(), anyInt(), anyInt(), any(), any()))
                .thenReturn(page);

        mockMvc.perform(get("/feedback/by-product?productId=1"))
                .andExpect(status().isOk());
    }
}
