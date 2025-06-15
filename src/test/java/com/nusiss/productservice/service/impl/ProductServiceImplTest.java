package com.nusiss.productservice.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nusiss.commonservice.feign.OrderFeignClient;
import com.nusiss.productservice.dao.ProductMapper;
import com.nusiss.productservice.dao.ProductMediaMapper;
import com.nusiss.productservice.entity.Product;
import com.nusiss.productservice.entity.ProductMedia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;


import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductMediaMapper productMediaMapper;

    @Mock
    private OrderFeignClient orderFeignClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = List.of(new Product());
        when(productMapper.selectList(null)).thenReturn(products);
        when(productMediaMapper.selectOne(any())).thenReturn(null);

        List<Product> result = productService.getAllProducts();
        assertEquals(1, result.size());
    }

    @Test
    void testGetProductById() {
        Product p = new Product();
        p.setId(1L);
        when(productMapper.selectById(1L)).thenReturn(p);
        when(productMediaMapper.selectOne(any())).thenReturn(null);

        Product result = productService.getProductById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void testCreateProduct() {
        Product p = new Product();
        productService.createProduct(p);
        verify(productMapper, times(1)).insert(p);
    }

    @Test
    void testUpdateProduct_Found() {
        Product p = new Product();
        when(productMapper.selectById(1L)).thenReturn(new Product());

        Product result = productService.updateProduct(1L, p);
        assertEquals(1L, result.getId());
        verify(productMapper).updateById(p);
    }

    @Test
    void testUpdateProduct_NotFound() {
        when(productMapper.selectById(999L)).thenReturn(null);
        Product result = productService.updateProduct(999L, new Product());
        assertNull(result);
    }

    @Test
    void testDeleteProduct_Success() {
        when(productMapper.selectById(1L)).thenReturn(new Product());
        when(productMapper.deleteById(1L)).thenReturn(1);

        assertTrue(productService.deleteProduct(1L));
    }

    @Test
    void testDeleteProduct_NotFound() {
        when(productMapper.selectById(1L)).thenReturn(null);
        assertFalse(productService.deleteProduct(1L));
    }

    @Test
    void testGetProductPage() {
        Page<Product> mockPage = new Page<>();
        mockPage.setRecords(List.of(new Product()));
        when(productMapper.selectPage(any(), any())).thenReturn(mockPage);
        when(productMediaMapper.selectOne(any())).thenReturn(null);

        Page<Product> result = productService.getProductPage(1, 10);
        assertEquals(1, result.getRecords().size());
    }

    @Test
    void testSearchProducts() {
        List<Product> list = List.of(new Product());
        when(productMapper.selectList(any())).thenReturn(list);
        when(productMediaMapper.selectOne(any())).thenReturn(null);

        List<Product> result = productService.searchProducts("abc");
        assertEquals(1, result.size());
    }

    @Test
    void testFilterProducts_AllConditions() {
        List<Product> list = List.of(new Product());
        when(productMapper.selectList(any())).thenReturn(list);
        when(productMediaMapper.selectOne(any())).thenReturn(null);

        List<Product> result = productService.filterProducts("name", "cat", "AVAILABLE",
                BigDecimal.valueOf(10), BigDecimal.valueOf(100), 4.0);
        assertEquals(1, result.size());
    }

    @Test
    void testFilterProductsWithSorting_Desc() {
        Page<Product> mockPage = new Page<>();
        mockPage.setRecords(List.of(new Product()));
        when(productMapper.selectPage(any(), any())).thenReturn(mockPage);
        when(productMediaMapper.selectOne(any())).thenReturn(null);

        IPage<Product> result = productService.filterProductsWithSorting(
                "name", "cat", "AVAILABLE",
                BigDecimal.valueOf(10), BigDecimal.valueOf(100), 4.0,
                "price", "desc", 1, 10
        );

        assertEquals(1, result.getRecords().size());
    }

    @Test
    void testFilterProductsWithSorting_Asc() {
        Page<Product> mockPage = new Page<>();
        mockPage.setRecords(List.of(new Product()));
        when(productMapper.selectPage(any(), any())).thenReturn(mockPage);
        when(productMediaMapper.selectOne(any())).thenReturn(null);

        IPage<Product> result = productService.filterProductsWithSorting(
                null, null, null,
                null, null, null,
                "price", "asc", 1, 10
        );

        assertEquals(1, result.getRecords().size());
    }

    @Test
    void testSetCoverImage_ValidProduct() {
        Product p = new Product();
        p.setId(1L);
        ProductMedia media = new ProductMedia();
        media.setUrl("http://example.com/img.jpg");

        when(productMapper.selectById(1L)).thenReturn(p); // mock product
        when(productMediaMapper.selectOne(any())).thenReturn(media); // mock media

        Product result = productService.getProductById(1L);

        assertEquals("http://example.com/img.jpg", result.getCoverImageUrl()); // 测试实际结果
    }


    @Test
    void testGetProductsPage_default() {
        assertDoesNotThrow(() -> productService.getAllProducts());
    }

    @Test
    void testGetRelatedProducts() {
        // 模拟当前商品
        Product product = new Product();
        product.setId(1L);
        product.setCategory("Smartphones");

        // 模拟推荐商品列表
        Product p1 = new Product();
        p1.setId(2L);
        p1.setName("Phone A");
        p1.setCategory("Smartphones");
        p1.setPrice(new BigDecimal("2999"));
        p1.setRating(4.8);

        Product p2 = new Product();
        p2.setId(3L);
        p2.setName("Phone B");
        p2.setCategory("Smartphones");
        p2.setPrice(new BigDecimal("3999"));
        p2.setRating(4.0);

        List<Product> related = List.of(p1, p2);

        // 模拟封面图
        ProductMedia media = new ProductMedia();
        media.setUrl("http://example.com/cover1.jpg");

        // Mock 行为
        when(productMapper.selectById(1L)).thenReturn(product);
        when(productMapper.selectList(any())).thenReturn(related);
        when(productMediaMapper.selectOne(any())).thenReturn(media);

        // 调用方法
        List<Product> result = productService.getRelatedProducts(1L, 5);

        // 断言
        assertEquals(2, result.size());
        assertEquals("Phone A", result.get(0).getName());
        assertEquals("http://example.com/cover1.jpg", result.get(0).getCoverImageUrl());
    }

    @Test
    void testGetTopRecommendedProductsByUser() {
        Long userId = 1L;
        List<Long> purchasedIds = List.of(1L, 3L, 5L);

        // 模拟购买过的商品
        Product p1 = new Product();
        p1.setId(1L);
        p1.setCategory("Smartphones");
        p1.setRating(4.5);

        Product p2 = new Product();
        p2.setId(3L);
        p2.setCategory("Smartphones");
        p2.setRating(4.0);

        Product p3 = new Product();
        p3.setId(5L);
        p3.setCategory("Laptops");
        p3.setRating(4.8);

        List<Product> purchasedProducts = List.of(p1, p2, p3);

        // 模拟推荐商品
        Product rec = new Product();
        rec.setId(7L);
        rec.setName("New Phone");
        rec.setCategory("Smartphones");
        rec.setRating(4.9);

        List<Product> recommended = List.of(rec);

        // 模拟封面图
        ProductMedia media = new ProductMedia();
        media.setUrl("http://example.com/newphone.jpg");

        // Mock 行为
        when(orderFeignClient.getProductIdsByUserId(userId)).thenReturn(purchasedIds);
        when(productMapper.selectBatchIds(purchasedIds)).thenReturn(purchasedProducts);
        when(productMapper.selectList(any())).thenReturn(recommended);
        when(productMediaMapper.selectOne(any())).thenReturn(media);

        // 调用方法
        List<Product> result = productService.getTopRecommendedProductsByUser(userId, 5);

        // 断言
        assertEquals(1, result.size());
        assertEquals("New Phone", result.get(0).getName());
        assertEquals("http://example.com/newphone.jpg", result.get(0).getCoverImageUrl());
    }


}
