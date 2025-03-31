package com.nusiss.productservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nusiss.commonservice.entity.User;
import com.nusiss.commonservice.feign.UserFeignClient;
import com.nusiss.productservice.client.InventoryClient;
import com.nusiss.productservice.config.ApiResponse;
import com.nusiss.productservice.domain.dto.ProductDTO;
import com.nusiss.productservice.domain.dto.ProductPageQueryDTO;
import com.nusiss.productservice.domain.entity.Product;
import com.nusiss.productservice.domain.entity.ProductImage;
import com.nusiss.productservice.mapper.ImageMapper;
import com.nusiss.productservice.mapper.ProductMapper;
import com.nusiss.productservice.result.PageApiResponse;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ProductServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class ProductServiceImplDiffblueTest {
    @MockBean
    private ImageMapper imageMapper;

    @MockBean
    private InventoryClient inventoryClient;

    @MockBean
    private ProductMapper productMapper;

    @Autowired
    private ProductServiceImpl productServiceImpl;

    @MockBean
    private UserFeignClient userFeignClient;

    /**
     * Test {@link ProductServiceImpl#save(String, ProductDTO)} with
     * {@code authToken}, {@code productDTO}.
     * <p>
     * Method under test: {@link ProductServiceImpl#save(String, ProductDTO)}
     */
    @Test
    @DisplayName("Test save(String, ProductDTO) with 'authToken', 'productDTO'")
    void testSaveWithAuthTokenProductDTO() {
        // Arrange
        when(productMapper.insert(Mockito.<Product>any())).thenReturn(1);
        com.nusiss.productservice.config.ApiResponse<String> successResult = com.nusiss.productservice.config.ApiResponse
                .success();
        when(inventoryClient.add(Mockito.<String>any(), Mockito.<Long>any(), anyInt())).thenReturn(successResult);
        when(userFeignClient.getCurrentUserInfo(Mockito.<String>any()))
                .thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(200)));

        ProductDTO productDTO = new ProductDTO();
        productDTO.setAvailableStock(1);
        productDTO.setCategoryId(1L);
        productDTO.setDescription("The characteristics of someone or something");
        productDTO.setImageUrls(new ArrayList<>());
        productDTO.setName("Name");
        productDTO.setPrice(new BigDecimal("2.3"));
        productDTO.setProductId(1L);
        productDTO.setSellerId(1L);

        // Act
        productServiceImpl.save("ABC123", productDTO);

        // Assert
        verify(productMapper).insert(isA(Product.class));
        verify(userFeignClient, atLeast(1)).getCurrentUserInfo(eq("ABC123"));
        verify(inventoryClient).add(eq("ABC123"), eq(1L), eq(1));
    }

    /**
     * Test {@link ProductServiceImpl#save(String, ProductDTO)} with
     * {@code authToken}, {@code productDTO}.
     * <ul>
     *   <li>Given {@link ImageMapper} {@link BaseMapper#insert(Object)} return
     * one.</li>
     *   <li>Then calls {@link HttpEntity#getBody()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link ProductServiceImpl#save(String, ProductDTO)}
     */
    @Test
    @DisplayName("Test save(String, ProductDTO) with 'authToken', 'productDTO'; given ImageMapper insert(Object) return one; then calls getBody()")
    void testSaveWithAuthTokenProductDTO_givenImageMapperInsertReturnOne_thenCallsGetBody() {
        // Arrange
        when(productMapper.insert(Mockito.<Product>any())).thenReturn(1);
        when(imageMapper.insert(Mockito.<ProductImage>any())).thenReturn(1);
        com.nusiss.productservice.config.ApiResponse<String> successResult = com.nusiss.productservice.config.ApiResponse
                .success();
        when(inventoryClient.add(Mockito.<String>any(), Mockito.<Long>any(), anyInt())).thenReturn(successResult);
        ResponseEntity<com.nusiss.commonservice.config.ApiResponse<User>> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getBody())
                .thenReturn(new com.nusiss.commonservice.config.ApiResponse<>(true, "Not all who wander are lost", new User()));
        when(responseEntity.getStatusCode()).thenReturn(HttpStatusCode.valueOf(200));
        when(userFeignClient.getCurrentUserInfo(Mockito.<String>any())).thenReturn(responseEntity);

        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add("foo");

        ProductDTO productDTO = new ProductDTO();
        productDTO.setAvailableStock(1);
        productDTO.setCategoryId(1L);
        productDTO.setDescription("The characteristics of someone or something");
        productDTO.setImageUrls(imageUrls);
        productDTO.setName("Name");
        productDTO.setPrice(new BigDecimal("2.3"));
        productDTO.setProductId(1L);
        productDTO.setSellerId(1L);

        // Act
        productServiceImpl.save("ABC123", productDTO);

        // Assert
        verify(imageMapper).insert(isA(ProductImage.class));
        verify(productMapper).insert(isA(Product.class));
        verify(userFeignClient, atLeast(1)).getCurrentUserInfo(eq("ABC123"));
        verify(inventoryClient).add(eq("ABC123"), eq(1L), eq(1));
        verify(responseEntity, atLeast(1)).getBody();
        verify(responseEntity, atLeast(1)).getStatusCode();
    }

    /**
     * Test {@link ProductServiceImpl#save(String, ProductDTO)} with
     * {@code authToken}, {@code productDTO}.
     * <ul>
     *   <li>Given {@link ResponseEntity} {@link ResponseEntity#getStatusCode()}
     * return {@code null}.</li>
     * </ul>
     * <p>
     * Method under test: {@link ProductServiceImpl#save(String, ProductDTO)}
     */
    @Test
    @DisplayName("Test save(String, ProductDTO) with 'authToken', 'productDTO'; given ResponseEntity getStatusCode() return 'null'")
    void testSaveWithAuthTokenProductDTO_givenResponseEntityGetStatusCodeReturnNull() {
        // Arrange
        when(productMapper.insert(Mockito.<Product>any())).thenReturn(1);
        com.nusiss.productservice.config.ApiResponse<String> successResult = com.nusiss.productservice.config.ApiResponse
                .success();
        when(inventoryClient.add(Mockito.<String>any(), Mockito.<Long>any(), anyInt())).thenReturn(successResult);
        ResponseEntity<com.nusiss.commonservice.config.ApiResponse<User>> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCode()).thenReturn(null);
        when(userFeignClient.getCurrentUserInfo(Mockito.<String>any())).thenReturn(responseEntity);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setAvailableStock(1);
        productDTO.setCategoryId(1L);
        productDTO.setDescription("The characteristics of someone or something");
        productDTO.setImageUrls(new ArrayList<>());
        productDTO.setName("Name");
        productDTO.setPrice(new BigDecimal("2.3"));
        productDTO.setProductId(1L);
        productDTO.setSellerId(1L);

        // Act
        productServiceImpl.save("ABC123", productDTO);

        // Assert
        verify(productMapper).insert(isA(Product.class));
        verify(userFeignClient, atLeast(1)).getCurrentUserInfo(eq("ABC123"));
        verify(inventoryClient).add(eq("ABC123"), eq(1L), eq(1));
        verify(responseEntity, atLeast(1)).getStatusCode();
    }

    /**
     * Test {@link ProductServiceImpl#pageQueryConsumer(ProductPageQueryDTO)}.
     * <p>
     * Method under test:
     * {@link ProductServiceImpl#pageQueryConsumer(ProductPageQueryDTO)}
     */
    @Test
    @DisplayName("Test pageQueryConsumer(ProductPageQueryDTO)")
    void testPageQueryConsumer() {
        // Arrange
        when(productMapper.selectPage(Mockito.<IPage<Product>>any(), Mockito.<Wrapper<Product>>any()))
                .thenReturn(new Page<>());
        ProductPageQueryDTO productPageQueryDTO = mock(ProductPageQueryDTO.class);
        when(productPageQueryDTO.getPage()).thenReturn(1);
        when(productPageQueryDTO.getPageSize()).thenReturn(3);
        when(productPageQueryDTO.getCategoryId()).thenReturn(1L);
        when(productPageQueryDTO.getDescription()).thenReturn("The characteristics of someone or something");
        when(productPageQueryDTO.getName()).thenReturn("");
        doNothing().when(productPageQueryDTO).setAvailableStock(anyInt());
        doNothing().when(productPageQueryDTO).setCategoryId(Mockito.<Long>any());
        doNothing().when(productPageQueryDTO).setDescription(Mockito.<String>any());
        doNothing().when(productPageQueryDTO).setName(Mockito.<String>any());
        doNothing().when(productPageQueryDTO).setPage(anyInt());
        doNothing().when(productPageQueryDTO).setPageSize(anyInt());
        doNothing().when(productPageQueryDTO).setPrice(anyDouble());
        doNothing().when(productPageQueryDTO).setProductId(Mockito.<Long>any());
        doNothing().when(productPageQueryDTO).setSellerId(Mockito.<Long>any());
        productPageQueryDTO.setAvailableStock(1);
        productPageQueryDTO.setCategoryId(1L);
        productPageQueryDTO.setDescription("The characteristics of someone or something");
        productPageQueryDTO.setName("Name");
        productPageQueryDTO.setPage(1);
        productPageQueryDTO.setPageSize(3);
        productPageQueryDTO.setPrice(10.0d);
        productPageQueryDTO.setProductId(1L);
        productPageQueryDTO.setSellerId(1L);

        // Act
        PageApiResponse actualPageQueryConsumerResult = productServiceImpl.pageQueryConsumer(productPageQueryDTO);

        // Assert
        verify(productMapper).selectPage(isA(IPage.class), isA(Wrapper.class));
        verify(productPageQueryDTO, atLeast(1)).getCategoryId();
        verify(productPageQueryDTO, atLeast(1)).getDescription();
        verify(productPageQueryDTO).getName();
        verify(productPageQueryDTO).getPage();
        verify(productPageQueryDTO).getPageSize();
        verify(productPageQueryDTO).setAvailableStock(eq(1));
        verify(productPageQueryDTO).setCategoryId(eq(1L));
        verify(productPageQueryDTO).setDescription(eq("The characteristics of someone or something"));
        verify(productPageQueryDTO).setName(eq("Name"));
        verify(productPageQueryDTO).setPage(eq(1));
        verify(productPageQueryDTO).setPageSize(eq(3));
        verify(productPageQueryDTO).setPrice(eq(10.0d));
        verify(productPageQueryDTO).setProductId(eq(1L));
        verify(productPageQueryDTO).setSellerId(eq(1L));
        assertEquals(0L, actualPageQueryConsumerResult.getTotal());
        assertTrue(actualPageQueryConsumerResult.getRecords().isEmpty());
    }

    /**
     * Test {@link ProductServiceImpl#pageQueryConsumer(ProductPageQueryDTO)}.
     * <ul>
     *   <li>When {@link ProductPageQueryDTO} (default constructor) AvailableStock is
     * one.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link ProductServiceImpl#pageQueryConsumer(ProductPageQueryDTO)}
     */
    @Test
    @DisplayName("Test pageQueryConsumer(ProductPageQueryDTO); when ProductPageQueryDTO (default constructor) AvailableStock is one")
    void testPageQueryConsumer_whenProductPageQueryDTOAvailableStockIsOne() {
        // Arrange
        when(productMapper.selectPage(Mockito.<IPage<Product>>any(), Mockito.<Wrapper<Product>>any()))
                .thenReturn(new Page<>());

        ProductPageQueryDTO productPageQueryDTO = new ProductPageQueryDTO();
        productPageQueryDTO.setAvailableStock(1);
        productPageQueryDTO.setCategoryId(1L);
        productPageQueryDTO.setDescription("The characteristics of someone or something");
        productPageQueryDTO.setName("Name");
        productPageQueryDTO.setPage(1);
        productPageQueryDTO.setPageSize(3);
        productPageQueryDTO.setPrice(10.0d);
        productPageQueryDTO.setProductId(1L);
        productPageQueryDTO.setSellerId(1L);

        // Act
        PageApiResponse actualPageQueryConsumerResult = productServiceImpl.pageQueryConsumer(productPageQueryDTO);

        // Assert
        verify(productMapper).selectPage(isA(IPage.class), isA(Wrapper.class));
        assertEquals(0L, actualPageQueryConsumerResult.getTotal());
        assertTrue(actualPageQueryConsumerResult.getRecords().isEmpty());
    }

    /**
     * Test {@link ProductServiceImpl#pageQueryConsumer(ProductPageQueryDTO)}.
     * <ul>
     *   <li>When {@link ProductPageQueryDTO}
     * {@link ProductPageQueryDTO#getDescription()} return empty string.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link ProductServiceImpl#pageQueryConsumer(ProductPageQueryDTO)}
     */
    @Test
    @DisplayName("Test pageQueryConsumer(ProductPageQueryDTO); when ProductPageQueryDTO getDescription() return empty string")
    void testPageQueryConsumer_whenProductPageQueryDTOGetDescriptionReturnEmptyString() {
        // Arrange
        when(productMapper.selectPage(Mockito.<IPage<Product>>any(), Mockito.<Wrapper<Product>>any()))
                .thenReturn(new Page<>());
        ProductPageQueryDTO productPageQueryDTO = mock(ProductPageQueryDTO.class);
        when(productPageQueryDTO.getPage()).thenReturn(1);
        when(productPageQueryDTO.getPageSize()).thenReturn(3);
        when(productPageQueryDTO.getCategoryId()).thenReturn(1L);
        when(productPageQueryDTO.getDescription()).thenReturn("");
        when(productPageQueryDTO.getName()).thenReturn("Name");
        doNothing().when(productPageQueryDTO).setAvailableStock(anyInt());
        doNothing().when(productPageQueryDTO).setCategoryId(Mockito.<Long>any());
        doNothing().when(productPageQueryDTO).setDescription(Mockito.<String>any());
        doNothing().when(productPageQueryDTO).setName(Mockito.<String>any());
        doNothing().when(productPageQueryDTO).setPage(anyInt());
        doNothing().when(productPageQueryDTO).setPageSize(anyInt());
        doNothing().when(productPageQueryDTO).setPrice(anyDouble());
        doNothing().when(productPageQueryDTO).setProductId(Mockito.<Long>any());
        doNothing().when(productPageQueryDTO).setSellerId(Mockito.<Long>any());
        productPageQueryDTO.setAvailableStock(1);
        productPageQueryDTO.setCategoryId(1L);
        productPageQueryDTO.setDescription("The characteristics of someone or something");
        productPageQueryDTO.setName("Name");
        productPageQueryDTO.setPage(1);
        productPageQueryDTO.setPageSize(3);
        productPageQueryDTO.setPrice(10.0d);
        productPageQueryDTO.setProductId(1L);
        productPageQueryDTO.setSellerId(1L);

        // Act
        PageApiResponse actualPageQueryConsumerResult = productServiceImpl.pageQueryConsumer(productPageQueryDTO);

        // Assert
        verify(productMapper).selectPage(isA(IPage.class), isA(Wrapper.class));
        verify(productPageQueryDTO, atLeast(1)).getCategoryId();
        verify(productPageQueryDTO).getDescription();
        verify(productPageQueryDTO, atLeast(1)).getName();
        verify(productPageQueryDTO).getPage();
        verify(productPageQueryDTO).getPageSize();
        verify(productPageQueryDTO).setAvailableStock(eq(1));
        verify(productPageQueryDTO).setCategoryId(eq(1L));
        verify(productPageQueryDTO).setDescription(eq("The characteristics of someone or something"));
        verify(productPageQueryDTO).setName(eq("Name"));
        verify(productPageQueryDTO).setPage(eq(1));
        verify(productPageQueryDTO).setPageSize(eq(3));
        verify(productPageQueryDTO).setPrice(eq(10.0d));
        verify(productPageQueryDTO).setProductId(eq(1L));
        verify(productPageQueryDTO).setSellerId(eq(1L));
        assertEquals(0L, actualPageQueryConsumerResult.getTotal());
        assertTrue(actualPageQueryConsumerResult.getRecords().isEmpty());
    }

    /**
     * Test
     * {@link ProductServiceImpl#pageQueryMerchant(String, ProductPageQueryDTO)}.
     * <p>
     * Method under test:
     * {@link ProductServiceImpl#pageQueryMerchant(String, ProductPageQueryDTO)}
     */
    @Test
    @DisplayName("Test pageQueryMerchant(String, ProductPageQueryDTO)")
    void testPageQueryMerchant() {
        // Arrange
        when(productMapper.selectPage(Mockito.<IPage<Product>>any(), Mockito.<Wrapper<Product>>any()))
                .thenReturn(new Page<>());
        when(userFeignClient.getCurrentUserInfo(Mockito.<String>any()))
                .thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(200)));

        ProductPageQueryDTO productPageQueryDTO = new ProductPageQueryDTO();
        productPageQueryDTO.setAvailableStock(1);
        productPageQueryDTO.setCategoryId(1L);
        productPageQueryDTO.setDescription("The characteristics of someone or something");
        productPageQueryDTO.setName("Name");
        productPageQueryDTO.setPage(1);
        productPageQueryDTO.setPageSize(3);
        productPageQueryDTO.setPrice(10.0d);
        productPageQueryDTO.setProductId(1L);
        productPageQueryDTO.setSellerId(1L);

        // Act
        PageApiResponse actualPageQueryMerchantResult = productServiceImpl.pageQueryMerchant("ABC123", productPageQueryDTO);

        // Assert
        verify(productMapper).selectPage(isA(IPage.class), isA(Wrapper.class));
        verify(userFeignClient).getCurrentUserInfo(eq("ABC123"));
        assertEquals(0L, actualPageQueryMerchantResult.getTotal());
        assertTrue(actualPageQueryMerchantResult.getRecords().isEmpty());
    }

    /**
     * Test
     * {@link ProductServiceImpl#pageQueryMerchant(String, ProductPageQueryDTO)}.
     * <ul>
     *   <li>Given {@code null}.</li>
     *   <li>When {@link ProductPageQueryDTO}
     * {@link ProductPageQueryDTO#getCategoryId()} return {@code null}.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link ProductServiceImpl#pageQueryMerchant(String, ProductPageQueryDTO)}
     */
    @Test
    @DisplayName("Test pageQueryMerchant(String, ProductPageQueryDTO); given 'null'; when ProductPageQueryDTO getCategoryId() return 'null'")
    void testPageQueryMerchant_givenNull_whenProductPageQueryDTOGetCategoryIdReturnNull() {
        // Arrange
        when(productMapper.selectPage(Mockito.<IPage<Product>>any(), Mockito.<Wrapper<Product>>any()))
                .thenReturn(new Page<>());
        ResponseEntity<com.nusiss.commonservice.config.ApiResponse<User>> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getBody())
                .thenReturn(new com.nusiss.commonservice.config.ApiResponse<>(true, "Not all who wander are lost", new User()));
        when(responseEntity.getStatusCode()).thenReturn(HttpStatusCode.valueOf(200));
        when(userFeignClient.getCurrentUserInfo(Mockito.<String>any())).thenReturn(responseEntity);
        ProductPageQueryDTO productPageQueryDTO = mock(ProductPageQueryDTO.class);
        when(productPageQueryDTO.getPage()).thenReturn(1);
        when(productPageQueryDTO.getPageSize()).thenReturn(3);
        when(productPageQueryDTO.getCategoryId()).thenReturn(null);
        when(productPageQueryDTO.getName()).thenReturn("Name");
        doNothing().when(productPageQueryDTO).setAvailableStock(anyInt());
        doNothing().when(productPageQueryDTO).setCategoryId(Mockito.<Long>any());
        doNothing().when(productPageQueryDTO).setDescription(Mockito.<String>any());
        doNothing().when(productPageQueryDTO).setName(Mockito.<String>any());
        doNothing().when(productPageQueryDTO).setPage(anyInt());
        doNothing().when(productPageQueryDTO).setPageSize(anyInt());
        doNothing().when(productPageQueryDTO).setPrice(anyDouble());
        doNothing().when(productPageQueryDTO).setProductId(Mockito.<Long>any());
        doNothing().when(productPageQueryDTO).setSellerId(Mockito.<Long>any());
        productPageQueryDTO.setAvailableStock(1);
        productPageQueryDTO.setCategoryId(1L);
        productPageQueryDTO.setDescription("The characteristics of someone or something");
        productPageQueryDTO.setName("Name");
        productPageQueryDTO.setPage(1);
        productPageQueryDTO.setPageSize(3);
        productPageQueryDTO.setPrice(10.0d);
        productPageQueryDTO.setProductId(1L);
        productPageQueryDTO.setSellerId(1L);

        // Act
        PageApiResponse actualPageQueryMerchantResult = productServiceImpl.pageQueryMerchant("ABC123", productPageQueryDTO);

        // Assert
        verify(productMapper).selectPage(isA(IPage.class), isA(Wrapper.class));
        verify(userFeignClient).getCurrentUserInfo(eq("ABC123"));
        verify(productPageQueryDTO).getCategoryId();
        verify(productPageQueryDTO, atLeast(1)).getName();
        verify(productPageQueryDTO).getPage();
        verify(productPageQueryDTO).getPageSize();
        verify(productPageQueryDTO).setAvailableStock(eq(1));
        verify(productPageQueryDTO).setCategoryId(eq(1L));
        verify(productPageQueryDTO).setDescription(eq("The characteristics of someone or something"));
        verify(productPageQueryDTO).setName(eq("Name"));
        verify(productPageQueryDTO).setPage(eq(1));
        verify(productPageQueryDTO).setPageSize(eq(3));
        verify(productPageQueryDTO).setPrice(eq(10.0d));
        verify(productPageQueryDTO).setProductId(eq(1L));
        verify(productPageQueryDTO).setSellerId(eq(1L));
        verify(responseEntity).getBody();
        verify(responseEntity).getStatusCode();
        assertEquals(0L, actualPageQueryMerchantResult.getTotal());
        assertTrue(actualPageQueryMerchantResult.getRecords().isEmpty());
    }

    /**
     * Test
     * {@link ProductServiceImpl#pageQueryMerchant(String, ProductPageQueryDTO)}.
     * <ul>
     *   <li>Given {@code null}.</li>
     *   <li>When {@link ProductPageQueryDTO} {@link ProductPageQueryDTO#getName()}
     * return {@code null}.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link ProductServiceImpl#pageQueryMerchant(String, ProductPageQueryDTO)}
     */
    @Test
    @DisplayName("Test pageQueryMerchant(String, ProductPageQueryDTO); given 'null'; when ProductPageQueryDTO getName() return 'null'")
    void testPageQueryMerchant_givenNull_whenProductPageQueryDTOGetNameReturnNull() {
        // Arrange
        when(productMapper.selectPage(Mockito.<IPage<Product>>any(), Mockito.<Wrapper<Product>>any()))
                .thenReturn(new Page<>());
        ResponseEntity<com.nusiss.commonservice.config.ApiResponse<User>> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getBody())
                .thenReturn(new com.nusiss.commonservice.config.ApiResponse<>(true, "Not all who wander are lost", new User()));
        when(responseEntity.getStatusCode()).thenReturn(HttpStatusCode.valueOf(200));
        when(userFeignClient.getCurrentUserInfo(Mockito.<String>any())).thenReturn(responseEntity);
        ProductPageQueryDTO productPageQueryDTO = mock(ProductPageQueryDTO.class);
        when(productPageQueryDTO.getPage()).thenReturn(1);
        when(productPageQueryDTO.getPageSize()).thenReturn(3);
        when(productPageQueryDTO.getCategoryId()).thenReturn(1L);
        when(productPageQueryDTO.getName()).thenReturn(null);
        doNothing().when(productPageQueryDTO).setAvailableStock(anyInt());
        doNothing().when(productPageQueryDTO).setCategoryId(Mockito.<Long>any());
        doNothing().when(productPageQueryDTO).setDescription(Mockito.<String>any());
        doNothing().when(productPageQueryDTO).setName(Mockito.<String>any());
        doNothing().when(productPageQueryDTO).setPage(anyInt());
        doNothing().when(productPageQueryDTO).setPageSize(anyInt());
        doNothing().when(productPageQueryDTO).setPrice(anyDouble());
        doNothing().when(productPageQueryDTO).setProductId(Mockito.<Long>any());
        doNothing().when(productPageQueryDTO).setSellerId(Mockito.<Long>any());
        productPageQueryDTO.setAvailableStock(1);
        productPageQueryDTO.setCategoryId(1L);
        productPageQueryDTO.setDescription("The characteristics of someone or something");
        productPageQueryDTO.setName("Name");
        productPageQueryDTO.setPage(1);
        productPageQueryDTO.setPageSize(3);
        productPageQueryDTO.setPrice(10.0d);
        productPageQueryDTO.setProductId(1L);
        productPageQueryDTO.setSellerId(1L);

        // Act
        PageApiResponse actualPageQueryMerchantResult = productServiceImpl.pageQueryMerchant("ABC123", productPageQueryDTO);

        // Assert
        verify(productMapper).selectPage(isA(IPage.class), isA(Wrapper.class));
        verify(userFeignClient).getCurrentUserInfo(eq("ABC123"));
        verify(productPageQueryDTO, atLeast(1)).getCategoryId();
        verify(productPageQueryDTO).getName();
        verify(productPageQueryDTO).getPage();
        verify(productPageQueryDTO).getPageSize();
        verify(productPageQueryDTO).setAvailableStock(eq(1));
        verify(productPageQueryDTO).setCategoryId(eq(1L));
        verify(productPageQueryDTO).setDescription(eq("The characteristics of someone or something"));
        verify(productPageQueryDTO).setName(eq("Name"));
        verify(productPageQueryDTO).setPage(eq(1));
        verify(productPageQueryDTO).setPageSize(eq(3));
        verify(productPageQueryDTO).setPrice(eq(10.0d));
        verify(productPageQueryDTO).setProductId(eq(1L));
        verify(productPageQueryDTO).setSellerId(eq(1L));
        verify(responseEntity).getBody();
        verify(responseEntity).getStatusCode();
        assertEquals(0L, actualPageQueryMerchantResult.getTotal());
        assertTrue(actualPageQueryMerchantResult.getRecords().isEmpty());
    }

    /**
     * Test
     * {@link ProductServiceImpl#pageQueryMerchant(String, ProductPageQueryDTO)}.
     * <ul>
     *   <li>Given {@link ResponseEntity} {@link ResponseEntity#getStatusCode()}
     * return {@code null}.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link ProductServiceImpl#pageQueryMerchant(String, ProductPageQueryDTO)}
     */
    @Test
    @DisplayName("Test pageQueryMerchant(String, ProductPageQueryDTO); given ResponseEntity getStatusCode() return 'null'")
    void testPageQueryMerchant_givenResponseEntityGetStatusCodeReturnNull() {
        // Arrange
        when(productMapper.selectPage(Mockito.<IPage<Product>>any(), Mockito.<Wrapper<Product>>any()))
                .thenReturn(new Page<>());
        ResponseEntity<com.nusiss.commonservice.config.ApiResponse<User>> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCode()).thenReturn(null);
        when(userFeignClient.getCurrentUserInfo(Mockito.<String>any())).thenReturn(responseEntity);

        ProductPageQueryDTO productPageQueryDTO = new ProductPageQueryDTO();
        productPageQueryDTO.setAvailableStock(1);
        productPageQueryDTO.setCategoryId(1L);
        productPageQueryDTO.setDescription("The characteristics of someone or something");
        productPageQueryDTO.setName("Name");
        productPageQueryDTO.setPage(1);
        productPageQueryDTO.setPageSize(3);
        productPageQueryDTO.setPrice(10.0d);
        productPageQueryDTO.setProductId(1L);
        productPageQueryDTO.setSellerId(1L);

        // Act
        PageApiResponse actualPageQueryMerchantResult = productServiceImpl.pageQueryMerchant("ABC123", productPageQueryDTO);

        // Assert
        verify(productMapper).selectPage(isA(IPage.class), isA(Wrapper.class));
        verify(userFeignClient).getCurrentUserInfo(eq("ABC123"));
        verify(responseEntity).getStatusCode();
        assertEquals(0L, actualPageQueryMerchantResult.getTotal());
        assertTrue(actualPageQueryMerchantResult.getRecords().isEmpty());
    }

    /**
     * Test
     * {@link ProductServiceImpl#pageQueryMerchant(String, ProductPageQueryDTO)}.
     * <ul>
     *   <li>When {@link ProductPageQueryDTO}
     * {@link ProductPageQueryDTO#getCategoryId()} return one.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link ProductServiceImpl#pageQueryMerchant(String, ProductPageQueryDTO)}
     */
    @Test
    @DisplayName("Test pageQueryMerchant(String, ProductPageQueryDTO); when ProductPageQueryDTO getCategoryId() return one")
    void testPageQueryMerchant_whenProductPageQueryDTOGetCategoryIdReturnOne() {
        // Arrange
        when(productMapper.selectPage(Mockito.<IPage<Product>>any(), Mockito.<Wrapper<Product>>any()))
                .thenReturn(new Page<>());
        ResponseEntity<com.nusiss.commonservice.config.ApiResponse<User>> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getBody())
                .thenReturn(new com.nusiss.commonservice.config.ApiResponse<>(true, "Not all who wander are lost", new User()));
        when(responseEntity.getStatusCode()).thenReturn(HttpStatusCode.valueOf(200));
        when(userFeignClient.getCurrentUserInfo(Mockito.<String>any())).thenReturn(responseEntity);
        ProductPageQueryDTO productPageQueryDTO = mock(ProductPageQueryDTO.class);
        when(productPageQueryDTO.getPage()).thenReturn(1);
        when(productPageQueryDTO.getPageSize()).thenReturn(3);
        when(productPageQueryDTO.getCategoryId()).thenReturn(1L);
        when(productPageQueryDTO.getName()).thenReturn("Name");
        doNothing().when(productPageQueryDTO).setAvailableStock(anyInt());
        doNothing().when(productPageQueryDTO).setCategoryId(Mockito.<Long>any());
        doNothing().when(productPageQueryDTO).setDescription(Mockito.<String>any());
        doNothing().when(productPageQueryDTO).setName(Mockito.<String>any());
        doNothing().when(productPageQueryDTO).setPage(anyInt());
        doNothing().when(productPageQueryDTO).setPageSize(anyInt());
        doNothing().when(productPageQueryDTO).setPrice(anyDouble());
        doNothing().when(productPageQueryDTO).setProductId(Mockito.<Long>any());
        doNothing().when(productPageQueryDTO).setSellerId(Mockito.<Long>any());
        productPageQueryDTO.setAvailableStock(1);
        productPageQueryDTO.setCategoryId(1L);
        productPageQueryDTO.setDescription("The characteristics of someone or something");
        productPageQueryDTO.setName("Name");
        productPageQueryDTO.setPage(1);
        productPageQueryDTO.setPageSize(3);
        productPageQueryDTO.setPrice(10.0d);
        productPageQueryDTO.setProductId(1L);
        productPageQueryDTO.setSellerId(1L);

        // Act
        PageApiResponse actualPageQueryMerchantResult = productServiceImpl.pageQueryMerchant("ABC123", productPageQueryDTO);

        // Assert
        verify(productMapper).selectPage(isA(IPage.class), isA(Wrapper.class));
        verify(userFeignClient).getCurrentUserInfo(eq("ABC123"));
        verify(productPageQueryDTO, atLeast(1)).getCategoryId();
        verify(productPageQueryDTO, atLeast(1)).getName();
        verify(productPageQueryDTO).getPage();
        verify(productPageQueryDTO).getPageSize();
        verify(productPageQueryDTO).setAvailableStock(eq(1));
        verify(productPageQueryDTO).setCategoryId(eq(1L));
        verify(productPageQueryDTO).setDescription(eq("The characteristics of someone or something"));
        verify(productPageQueryDTO).setName(eq("Name"));
        verify(productPageQueryDTO).setPage(eq(1));
        verify(productPageQueryDTO).setPageSize(eq(3));
        verify(productPageQueryDTO).setPrice(eq(10.0d));
        verify(productPageQueryDTO).setProductId(eq(1L));
        verify(productPageQueryDTO).setSellerId(eq(1L));
        verify(responseEntity).getBody();
        verify(responseEntity).getStatusCode();
        assertEquals(0L, actualPageQueryMerchantResult.getTotal());
        assertTrue(actualPageQueryMerchantResult.getRecords().isEmpty());
    }

    /**
     * Test {@link ProductServiceImpl#update(String, ProductDTO)} with
     * {@code authToken}, {@code productDTO}.
     * <p>
     * Method under test: {@link ProductServiceImpl#update(String, ProductDTO)}
     */
    @Test
    @DisplayName("Test update(String, ProductDTO) with 'authToken', 'productDTO'")
    void testUpdateWithAuthTokenProductDTO() {
        // Arrange
        when(productMapper.updateById(Mockito.<Product>any())).thenReturn(1);
        when(imageMapper.delete(Mockito.<Wrapper<ProductImage>>any())).thenReturn(1);
        com.nusiss.productservice.config.ApiResponse<String> successResult = com.nusiss.productservice.config.ApiResponse
                .success();
        when(inventoryClient.update(Mockito.<String>any(), Mockito.<Long>any(), anyInt())).thenReturn(successResult);
        when(userFeignClient.getCurrentUserInfo(Mockito.<String>any()))
                .thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(200)));

        ProductDTO productDTO = new ProductDTO();
        productDTO.setAvailableStock(1);
        productDTO.setCategoryId(1L);
        productDTO.setDescription("The characteristics of someone or something");
        productDTO.setImageUrls(new ArrayList<>());
        productDTO.setName("Name");
        productDTO.setPrice(new BigDecimal("2.3"));
        productDTO.setProductId(1L);
        productDTO.setSellerId(1L);

        // Act
        productServiceImpl.update("ABC123", productDTO);

        // Assert
        verify(imageMapper).delete(isA(Wrapper.class));
        verify(productMapper).updateById(isA(Product.class));
        verify(userFeignClient).getCurrentUserInfo(eq("ABC123"));
        verify(inventoryClient).update(eq("ABC123"), eq(1L), eq(1));
    }

    /**
     * Test {@link ProductServiceImpl#update(String, ProductDTO)} with
     * {@code authToken}, {@code productDTO}.
     * <ul>
     *   <li>Given {@link ResponseEntity} {@link ResponseEntity#getStatusCode()}
     * return {@code null}.</li>
     * </ul>
     * <p>
     * Method under test: {@link ProductServiceImpl#update(String, ProductDTO)}
     */
    @Test
    @DisplayName("Test update(String, ProductDTO) with 'authToken', 'productDTO'; given ResponseEntity getStatusCode() return 'null'")
    void testUpdateWithAuthTokenProductDTO_givenResponseEntityGetStatusCodeReturnNull() {
        // Arrange
        when(productMapper.updateById(Mockito.<Product>any())).thenReturn(1);
        when(imageMapper.delete(Mockito.<Wrapper<ProductImage>>any())).thenReturn(1);
        com.nusiss.productservice.config.ApiResponse<String> successResult = com.nusiss.productservice.config.ApiResponse
                .success();
        when(inventoryClient.update(Mockito.<String>any(), Mockito.<Long>any(), anyInt())).thenReturn(successResult);
        ResponseEntity<com.nusiss.commonservice.config.ApiResponse<User>> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCode()).thenReturn(null);
        when(userFeignClient.getCurrentUserInfo(Mockito.<String>any())).thenReturn(responseEntity);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setAvailableStock(1);
        productDTO.setCategoryId(1L);
        productDTO.setDescription("The characteristics of someone or something");
        productDTO.setImageUrls(new ArrayList<>());
        productDTO.setName("Name");
        productDTO.setPrice(new BigDecimal("2.3"));
        productDTO.setProductId(1L);
        productDTO.setSellerId(1L);

        // Act
        productServiceImpl.update("ABC123", productDTO);

        // Assert
        verify(imageMapper).delete(isA(Wrapper.class));
        verify(productMapper).updateById(isA(Product.class));
        verify(userFeignClient).getCurrentUserInfo(eq("ABC123"));
        verify(inventoryClient).update(eq("ABC123"), eq(1L), eq(1));
        verify(responseEntity).getStatusCode();
    }

    /**
     * Test {@link ProductServiceImpl#update(String, ProductDTO)} with
     * {@code authToken}, {@code productDTO}.
     * <ul>
     *   <li>Then calls {@link BaseMapper#insert(Object)}.</li>
     * </ul>
     * <p>
     * Method under test: {@link ProductServiceImpl#update(String, ProductDTO)}
     */
    @Test
    @DisplayName("Test update(String, ProductDTO) with 'authToken', 'productDTO'; then calls insert(Object)")
    void testUpdateWithAuthTokenProductDTO_thenCallsInsert() {
        // Arrange
        when(productMapper.updateById(Mockito.<Product>any())).thenReturn(1);
        when(imageMapper.insert(Mockito.<ProductImage>any())).thenReturn(1);
        when(imageMapper.delete(Mockito.<Wrapper<ProductImage>>any())).thenReturn(1);
        com.nusiss.productservice.config.ApiResponse<String> successResult = com.nusiss.productservice.config.ApiResponse
                .success();
        when(inventoryClient.update(Mockito.<String>any(), Mockito.<Long>any(), anyInt())).thenReturn(successResult);
        ResponseEntity<com.nusiss.commonservice.config.ApiResponse<User>> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getBody())
                .thenReturn(new com.nusiss.commonservice.config.ApiResponse<>(true, "Not all who wander are lost", new User()));
        when(responseEntity.getStatusCode()).thenReturn(HttpStatusCode.valueOf(200));
        when(userFeignClient.getCurrentUserInfo(Mockito.<String>any())).thenReturn(responseEntity);

        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add("foo");

        ProductDTO productDTO = new ProductDTO();
        productDTO.setAvailableStock(1);
        productDTO.setCategoryId(1L);
        productDTO.setDescription("The characteristics of someone or something");
        productDTO.setImageUrls(imageUrls);
        productDTO.setName("Name");
        productDTO.setPrice(new BigDecimal("2.3"));
        productDTO.setProductId(1L);
        productDTO.setSellerId(1L);

        // Act
        productServiceImpl.update("ABC123", productDTO);

        // Assert
        verify(imageMapper).delete(isA(Wrapper.class));
        verify(imageMapper).insert(isA(ProductImage.class));
        verify(productMapper).updateById(isA(Product.class));
        verify(userFeignClient, atLeast(1)).getCurrentUserInfo(eq("ABC123"));
        verify(inventoryClient).update(eq("ABC123"), eq(1L), eq(1));
        verify(responseEntity, atLeast(1)).getBody();
        verify(responseEntity, atLeast(1)).getStatusCode();
    }

    /**
     * Test {@link ProductServiceImpl#deleteById(Long)}.
     * <p>
     * Method under test: {@link ProductServiceImpl#deleteById(Long)}
     */
    @Test
    @DisplayName("Test deleteById(Long)")
    void testDeleteById() {
        // Arrange
        when(productMapper.deleteById(Mockito.<Serializable>any())).thenReturn(1);
        when(imageMapper.delete(Mockito.<Wrapper<ProductImage>>any())).thenReturn(1);
        ApiResponse<String> successResult = ApiResponse.success();
        when(inventoryClient.delete(Mockito.<Long>any())).thenReturn(successResult);

        // Act
        productServiceImpl.deleteById(1L);

        // Assert
        verify(imageMapper).delete(isA(Wrapper.class));
        verify(productMapper).deleteById(isA(Serializable.class));
        verify(inventoryClient).delete(eq(1L));
    }

    /**
     * Test {@link ProductServiceImpl#queryById(Long)}.
     * <ul>
     *   <li>Then return ImageUrls Empty.</li>
     * </ul>
     * <p>
     * Method under test: {@link ProductServiceImpl#queryById(Long)}
     */
    @Test
    @DisplayName("Test queryById(Long); then return ImageUrls Empty")
    void testQueryById_thenReturnImageUrlsEmpty() {
        // Arrange
        Product product = new Product();
        product.setAvailableStock(1);
        product.setCategoryId(1L);
        product.setCreateDatetime(mock(Timestamp.class));
        product.setCreateUser("Create User");
        product.setDescription("The characteristics of someone or something");
        product.setName("Name");
        product.setPrice(new BigDecimal("2.3"));
        product.setProductId(1L);
        product.setProductImages(new ArrayList<>());
        product.setSellerId(1L);
        product.setUpdateDatetime(mock(Timestamp.class));
        product.setUpdateUser("2020-03-01");
        when(productMapper.selectById(Mockito.<Serializable>any())).thenReturn(product);
        when(imageMapper.selectList(Mockito.<Wrapper<ProductImage>>any())).thenReturn(new ArrayList<>());

        // Act
        ProductDTO actualQueryByIdResult = productServiceImpl.queryById(1L);

        // Assert
        verify(productMapper).selectById(isA(Serializable.class));
        verify(imageMapper).selectList(isA(Wrapper.class));
        assertEquals("Name", actualQueryByIdResult.getName());
        assertEquals("The characteristics of someone or something", actualQueryByIdResult.getDescription());
        assertEquals(1, actualQueryByIdResult.getAvailableStock());
        assertEquals(1L, actualQueryByIdResult.getCategoryId().longValue());
        assertEquals(1L, actualQueryByIdResult.getProductId().longValue());
        assertEquals(1L, actualQueryByIdResult.getSellerId().longValue());
        assertTrue(actualQueryByIdResult.getImageUrls().isEmpty());
        BigDecimal expectedPrice = new BigDecimal("2.3");
        assertEquals(expectedPrice, actualQueryByIdResult.getPrice());
    }

    /**
     * Test {@link ProductServiceImpl#queryById(Long)}.
     * <ul>
     *   <li>Then return ImageUrls size is one.</li>
     * </ul>
     * <p>
     * Method under test: {@link ProductServiceImpl#queryById(Long)}
     */
    @Test
    @DisplayName("Test queryById(Long); then return ImageUrls size is one")
    void testQueryById_thenReturnImageUrlsSizeIsOne() {
        // Arrange
        Product product = new Product();
        product.setAvailableStock(1);
        product.setCategoryId(1L);
        product.setCreateDatetime(mock(Timestamp.class));
        product.setCreateUser("Create User");
        product.setDescription("The characteristics of someone or something");
        product.setName("Name");
        product.setPrice(new BigDecimal("2.3"));
        product.setProductId(1L);
        product.setProductImages(new ArrayList<>());
        product.setSellerId(1L);
        product.setUpdateDatetime(mock(Timestamp.class));
        product.setUpdateUser("2020-03-01");
        when(productMapper.selectById(Mockito.<Serializable>any())).thenReturn(product);

        ProductImage productImage = new ProductImage();
        productImage.setCreateDatetime(mock(Timestamp.class));
        productImage.setCreateUser("product_id");
        productImage.setImageId(1L);
        productImage.setImageUrl("https://example.org/example");
        productImage.setProductId(1L);
        productImage.setUpdateDatetime(mock(Timestamp.class));
        productImage.setUpdateUser("2020-03-01");

        ArrayList<ProductImage> productImageList = new ArrayList<>();
        productImageList.add(productImage);
        when(imageMapper.selectList(Mockito.<Wrapper<ProductImage>>any())).thenReturn(productImageList);

        // Act
        ProductDTO actualQueryByIdResult = productServiceImpl.queryById(1L);

        // Assert
        verify(productMapper).selectById(isA(Serializable.class));
        verify(imageMapper).selectList(isA(Wrapper.class));
        assertEquals("Name", actualQueryByIdResult.getName());
        assertEquals("The characteristics of someone or something", actualQueryByIdResult.getDescription());
        List<String> imageUrls = actualQueryByIdResult.getImageUrls();
        assertEquals(1, imageUrls.size());
        assertEquals("https://example.org/example", imageUrls.get(0));
        assertEquals(1, actualQueryByIdResult.getAvailableStock());
        assertEquals(1L, actualQueryByIdResult.getCategoryId().longValue());
        assertEquals(1L, actualQueryByIdResult.getProductId().longValue());
        assertEquals(1L, actualQueryByIdResult.getSellerId().longValue());
        BigDecimal expectedPrice = new BigDecimal("2.3");
        assertEquals(expectedPrice, actualQueryByIdResult.getPrice());
    }

    /**
     * Test {@link ProductServiceImpl#deleteFile(String)}.
     * <ul>
     *   <li>When {@code /directory/foo.txt}.</li>
     * </ul>
     * <p>
     * Method under test: {@link ProductServiceImpl#deleteFile(String)}
     */
    @Test
    @DisplayName("Test deleteFile(String); when '/directory/foo.txt'")
    void testDeleteFile_whenDirectoryFooTxt() {
        // Arrange, Act and Assert
        assertFalse(productServiceImpl.deleteFile("/directory/foo.txt"));
    }

    /**
     * Test {@link ProductServiceImpl#deleteFile(String)}.
     * <ul>
     *   <li>When empty string.</li>
     * </ul>
     * <p>
     * Method under test: {@link ProductServiceImpl#deleteFile(String)}
     */
    @Test
    @DisplayName("Test deleteFile(String); when empty string")
    void testDeleteFile_whenEmptyString() {
        // Arrange, Act and Assert
        assertFalse(productServiceImpl.deleteFile(""));
    }

    /**
     * Test {@link ProductServiceImpl#queryCurrentUser(String)}.
     * <p>
     * Method under test: {@link ProductServiceImpl#queryCurrentUser(String)}
     */
    @Test
    @DisplayName("Test queryCurrentUser(String)")
    void testQueryCurrentUser() {
        // Arrange
        when(userFeignClient.getCurrentUserInfo(Mockito.<String>any()))
                .thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(200)));

        // Act
        String actualQueryCurrentUserResult = productServiceImpl.queryCurrentUser("ABC123");

        // Assert
        verify(userFeignClient).getCurrentUserInfo(eq("ABC123"));
        assertEquals("system", actualQueryCurrentUserResult);
    }

    /**
     * Test {@link ProductServiceImpl#queryCurrentUser(String)}.
     * <ul>
     *   <li>Then calls {@link ResponseEntity#getStatusCode()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link ProductServiceImpl#queryCurrentUser(String)}
     */
    @Test
    @DisplayName("Test queryCurrentUser(String); then calls getStatusCode()")
    void testQueryCurrentUser_thenCallsGetStatusCode() {
        // Arrange
        ResponseEntity<com.nusiss.commonservice.config.ApiResponse<User>> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCode()).thenReturn(null);
        when(userFeignClient.getCurrentUserInfo(Mockito.<String>any())).thenReturn(responseEntity);

        // Act
        String actualQueryCurrentUserResult = productServiceImpl.queryCurrentUser("ABC123");

        // Assert
        verify(userFeignClient).getCurrentUserInfo(eq("ABC123"));
        verify(responseEntity).getStatusCode();
        assertEquals("system", actualQueryCurrentUserResult);
    }
}
