package com.nusiss.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nusiss.productservice.dao.ProductMapper;
import com.nusiss.productservice.entity.ProductMedia;
import com.nusiss.productservice.service.ProductMediaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import com.nusiss.productservice.entity.Product;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductMediaController.class)
class ProductMediaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductMapper productMapper;

    @MockBean
    private ProductMediaService productMediaService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductMedia media;

    @BeforeEach
    void setUp() {
        media = new ProductMedia();
        media.setId(1L);
        media.setProductId(1L);
        media.setUrl("http://localhost/uploadFile/old.jpg");
        media.setUpdateDatetime(LocalDateTime.now());
    }

    @Test
    void testCreateProductMedia() throws Exception {
        Mockito.when(productMediaService.createProductMedia(any())).thenReturn(media);

        mockMvc.perform(post("/media")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(media)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllMedia() throws Exception {
        Mockito.when(productMediaService.getAllProductMedia()).thenReturn(List.of(media));

        mockMvc.perform(get("/media"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetMediaById() throws Exception {
        Mockito.when(productMediaService.getProductMediaById(1L)).thenReturn(media);

        mockMvc.perform(get("/media/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetMediaByProductId() throws Exception {
        Mockito.when(productMediaService.getMediaByProductId(1L)).thenReturn(List.of(media));

        mockMvc.perform(get("/media/product/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateProductMedia() throws Exception {
        Mockito.when(productMediaService.updateProductMedia(any())).thenReturn(true);

        mockMvc.perform(put("/media")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(media)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testDeleteProductMedia() throws Exception {
        Mockito.when(productMediaService.deleteProductMedia(1L)).thenReturn(true);

        mockMvc.perform(delete("/media/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testUploadAndSaveMedia_Success() throws Exception {
        Product mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setName("Test Product");

        Mockito.when(productMapper.selectById(1L)).thenReturn(mockProduct);

        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "fake-image".getBytes());

        mockMvc.perform(multipart("/media/uploadWithSave")
                        .file(file)
                        .param("productId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testUploadAndSaveMedia_ProductNotFound() throws Exception {
        Mockito.when(productMapper.selectById(99L)).thenReturn(null);

        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "fake-image".getBytes());

        mockMvc.perform(multipart("/media/uploadWithSave")
                        .file(file)
                        .param("productId", "99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void testReplaceProductImage_SuccessWithOldFile() throws Exception {
        Mockito.when(productMediaService.getProductMediaById(1L)).thenReturn(media);
        Mockito.when(productMediaService.updateProductMedia(any())).thenReturn(true);

        // 旧文件存在模拟（可根据实际情况决定是否真的创建文件）
        File fakeOldFile = new File(System.getProperty("user.dir") + "/src/main/resources/static/uploadFile/old.jpg");
        fakeOldFile.getParentFile().mkdirs();
        fakeOldFile.createNewFile();

        MockMultipartFile file = new MockMultipartFile("file", "new.jpg", "image/jpeg", "fake-image".getBytes());

        mockMvc.perform(multipart("/media/replaceImage")
                        .file(file)
                        .param("mediaId", "1")
                        .with(req -> { req.setMethod("PUT"); return req; }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // 清理
        fakeOldFile.delete();
    }

    @Test
    void testReplaceProductImage_MediaNotFound() throws Exception {
        Mockito.when(productMediaService.getProductMediaById(999L)).thenReturn(null);

        MockMultipartFile file = new MockMultipartFile("file", "new.jpg", "image/jpeg", "fake-image".getBytes());

        mockMvc.perform(multipart("/media/replaceImage")
                        .file(file)
                        .param("mediaId", "999")
                        .with(req -> { req.setMethod("PUT"); return req; }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void testReplaceProductImage_UpdateFail() throws Exception {
        Mockito.when(productMediaService.getProductMediaById(1L)).thenReturn(media);
        Mockito.when(productMediaService.updateProductMedia(any())).thenReturn(false);

        MockMultipartFile file = new MockMultipartFile("file", "new.jpg", "image/jpeg", "fake-image".getBytes());

        mockMvc.perform(multipart("/media/replaceImage")
                        .file(file)
                        .param("mediaId", "1")
                        .with(req -> { req.setMethod("PUT"); return req; }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));
    }
}
