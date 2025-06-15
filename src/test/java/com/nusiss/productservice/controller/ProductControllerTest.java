package com.nusiss.productservice.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.nusiss.productservice.entity.Product;
import com.nusiss.productservice.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /products - 所有商品")
    void testGetAllProducts() throws Exception {
        Product sample = new Product();
        when(productService.getAllProducts()).thenReturn(List.of(sample));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("GET /products/{id} - 商品存在")
    void testGetProductById_Found() throws Exception {
        Product product = new Product();
        product.setId(1L);
        when(productService.getProductById(1L)).thenReturn(product);

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    @DisplayName("GET /products/{id} - 商品不存在")
    void testGetProductById_NotFound() throws Exception {
        when(productService.getProductById(99L)).thenReturn(null);

        mockMvc.perform(get("/products/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("POST /products - 创建商品")
    void testCreateProduct() throws Exception {
        Product input = new Product();
        input.setName("Sample");
        when(productService.createProduct(any())).thenReturn(input);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Sample"));
    }

    @Test
    @DisplayName("PUT /products/{id} - 更新成功")
    void testUpdateProduct_Success() throws Exception {
        Product updated = new Product();
        updated.setId(1L);
        updated.setName("Updated");

        when(productService.updateProduct(eq(1L), any())).thenReturn(updated);

        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Updated"));
    }

    @Test
    @DisplayName("PUT /products/{id} - 更新失败")
    void testUpdateProduct_NotFound() throws Exception {
        when(productService.updateProduct(eq(99L), any())).thenReturn(null);

        mockMvc.perform(put("/products/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Product())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("DELETE /products/{id} - 删除成功")
    void testDeleteProduct_Success() throws Exception {
        when(productService.deleteProduct(1L)).thenReturn(true);

        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("DELETE /products/{id} - 删除失败")
    void testDeleteProduct_NotFound() throws Exception {
        when(productService.deleteProduct(99L)).thenReturn(false);

        mockMvc.perform(delete("/products/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /products/page - 分页查询")
    void testGetProductsByPage() throws Exception {
        Page<Product> mockPage = new Page<>();
        mockPage.setRecords(List.of(new Product()));
        when(productService.getProductPage(1, 10)).thenReturn(mockPage);

        mockMvc.perform(get("/products/page?page=1&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("GET /products/search - 关键词搜索")
    void testSearchProducts() throws Exception {
        when(productService.searchProducts("test")).thenReturn(List.of(new Product()));

        mockMvc.perform(get("/products/search?keyword=test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("GET /products/filter - 多条件筛选")
    void testFilterProducts() throws Exception {
        when(productService.filterProducts(any(), any(), any(), any(), any(), any()))
                .thenReturn(List.of(new Product()));

        mockMvc.perform(get("/products/filter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("GET /products/sort - 条件排序")
    void testFilterProductsWithSorting() throws Exception {
        IPage<Product> mockPage = new Page<>();
        mockPage.setRecords(List.of(new Product()));
        when(productService.filterProductsWithSorting(
                        any(), any(), any(), any(), any(), any(),
                        any(), any(), anyInt(), anyInt()))
                .thenReturn(mockPage);

        mockMvc.perform(get("/products/sort"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetRelatedProducts() throws Exception {
        Product p = new Product();
        p.setId(2L);
        p.setName("Phone A");
        p.setCategory("Smartphones");
        p.setRating(4.8);

        List<Product> mockList = List.of(p);

        when(productService.getRelatedProducts(1L, 5)).thenReturn(mockList);

        mockMvc.perform(get("/products/recommend/related/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(2));
    }

    @Test
    void testGetRecommendedProductsByUser() throws Exception {
        Product p = new Product();
        p.setId(7L);
        p.setName("New Phone");
        p.setCategory("Smartphones");
        p.setRating(4.9);

        List<Product> mockList = List.of(p);

        when(productService.getTopRecommendedProductsByUser(1L, 5)).thenReturn(mockList);

        mockMvc.perform(get("/products/recommend/user/1/top"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(7));
    }
}
