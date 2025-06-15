package com.nusiss.productservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nusiss.productservice.dao.ProductMediaMapper;
import com.nusiss.productservice.entity.ProductMedia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductMediaServiceImplTest {

    @InjectMocks
    private ProductMediaServiceImpl productMediaService;

    @Mock
    private ProductMediaMapper productMediaMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProductMedia_Success() {
        ProductMedia media = new ProductMedia();
        when(productMediaMapper.insert(media)).thenReturn(1);

        ProductMedia result = productMediaService.createProductMedia(media);
        assertNotNull(result);
    }

    @Test
    void testCreateProductMedia_Fail() {
        ProductMedia media = new ProductMedia();
        when(productMediaMapper.insert(media)).thenReturn(0);

        ProductMedia result = productMediaService.createProductMedia(media);
        assertNull(result);
    }

    @Test
    void testGetProductMediaById() {
        ProductMedia media = new ProductMedia();
        media.setId(1L);
        when(productMediaMapper.selectById(1L)).thenReturn(media);

        ProductMedia result = productMediaService.getProductMediaById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetAllProductMedia() {
        when(productMediaMapper.selectList(null)).thenReturn(List.of(new ProductMedia()));
        List<ProductMedia> result = productMediaService.getAllProductMedia();
        assertEquals(1, result.size());
    }

    @Test
    void testGetMediaByProductId() {
        when(productMediaMapper.selectList(any(QueryWrapper.class))).thenReturn(List.of(new ProductMedia()));
        List<ProductMedia> result = productMediaService.getMediaByProductId(1L);
        assertEquals(1, result.size());
    }

    @Test
    void testUpdateProductMedia_Success() {
        when(productMediaMapper.updateById(any(ProductMedia.class))).thenReturn(1);
        boolean result = productMediaService.updateProductMedia(new ProductMedia());
        assertTrue(result);
    }

    @Test
    void testUpdateProductMedia_Fail() {
        when(productMediaMapper.selectList(any(QueryWrapper.class))).thenReturn(List.of(new ProductMedia()));
        boolean result = productMediaService.updateProductMedia(new ProductMedia());
        assertFalse(result);
    }

    @Test
    void testDeleteProductMedia_FileExistsAndDeleted() throws Exception {
        ProductMedia media = new ProductMedia();
        media.setId(1L);
        media.setUrl("http://localhost/uploadFile/testfile.jpg");

        // 真实创建临时文件模拟本地存在
        String projectRoot = System.getProperty("user.dir");
        String fullPath = projectRoot + "/src/main/resources/static/uploadFile/testfile.jpg";
        File file = new File(fullPath);
        file.getParentFile().mkdirs();
        file.createNewFile();

        when(productMediaMapper.selectById(1L)).thenReturn(media);
        when(productMediaMapper.deleteById(1L)).thenReturn(1);

        boolean result = productMediaService.deleteProductMedia(1L);
        assertTrue(result);

        // 清理
        assertFalse(file.exists()); // 确保文件已删除
    }

    @Test
    void testDeleteProductMedia_FileNotExists() {
        ProductMedia media = new ProductMedia();
        media.setId(2L);
        media.setUrl("http://localhost/uploadFile/notexist.jpg");

        when(productMediaMapper.selectById(2L)).thenReturn(media);
        when(productMediaMapper.deleteById(2L)).thenReturn(1);

        boolean result = productMediaService.deleteProductMedia(2L);
        assertTrue(result);
    }

    @Test
    void testDeleteProductMedia_RecordNotFound() {
        when(productMediaMapper.selectById(99L)).thenReturn(null);
        boolean result = productMediaService.deleteProductMedia(99L);
        assertFalse(result);
    }
}
