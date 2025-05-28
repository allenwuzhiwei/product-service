package com.nusiss.productservice.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

        assertEquals("http://example.com/img.jpg", result.getCoverImageUrl()); // ✅ 测试实际结果
    }


    @Test
    void testGetProductsPage_default() {
        assertDoesNotThrow(() -> productService.getAllProducts());
    }

}
