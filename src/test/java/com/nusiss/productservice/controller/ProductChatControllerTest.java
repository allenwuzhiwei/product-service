package com.nusiss.productservice.controller;

import com.nusiss.productservice.dao.ProductMapper;
import com.nusiss.productservice.entity.Product;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;

@WebMvcTest(ProductChatController.class)
public class ProductChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductMapper productMapper;

    @Test
    void testRecommendSmartphones() throws Exception {
        mockTopProducts("Smartphones");
        mockMvc.perform(get("/api/chat/recommend-smartphones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parts[0].text", containsString("热门手机")))
                .andExpect(jsonPath("$.parts[0].text", containsString("iPhone")));
    }

    @Test
    void testRecommendPads() throws Exception {
        mockTopProducts("Pad");
        mockMvc.perform(get("/api/chat/recommend-pads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parts[0].text", containsString("热门平板")))
                .andExpect(jsonPath("$.parts[0].text", containsString("iPad")));
    }

    @Test
    void testRecommendLaptops() throws Exception {
        mockTopProducts("Laptops");
        mockMvc.perform(get("/api/chat/recommend-laptops"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parts[0].text", containsString("笔记本")))
                .andExpect(jsonPath("$.parts[0].text", containsString("MacBook")));
    }

    @Test
    void testAboutUs() throws Exception {
        mockMvc.perform(get("/api/chat/about"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parts[0].text", containsString("个性化购物体验")))
                .andExpect(jsonPath("$.parts[0].text", containsString("微服务模块")));
    }

    @Test
    void testPromotions() throws Exception {
        mockMvc.perform(get("/api/chat/promotions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parts[0].text", containsString("促销")))
                .andExpect(jsonPath("$.parts[0].text", containsString("限时秒杀")));
    }

    // ===== 辅助方法：模拟分类商品 =====
    private void mockTopProducts(String category) {
        Product p1 = new Product();
        p1.setId(1L);
        p1.setName("iPhone");
        p1.setCategory(category);
        p1.setPrice(new BigDecimal("6999"));
        p1.setRating(4.9);

        Product p2 = new Product();
        p2.setId(2L);
        p2.setName("iPad");
        p2.setCategory(category);
        p2.setPrice(new BigDecimal("3999"));
        p2.setRating(4.8);

        Product p3 = new Product();
        p3.setId(3L);
        p3.setName("MacBook");
        p3.setCategory(category);
        p3.setPrice(new BigDecimal("9999"));
        p3.setRating(4.7);

        List<Product> mockProducts = List.of(p1, p2, p3);
        Mockito.when(productMapper.findTop5ByCategoryOrderByRatingDesc(category)).thenReturn(mockProducts);
    }

}
