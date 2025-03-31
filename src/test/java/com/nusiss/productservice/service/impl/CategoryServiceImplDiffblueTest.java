package com.nusiss.productservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
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
import com.nusiss.commonservice.config.ApiResponse;
import com.nusiss.commonservice.entity.User;
import com.nusiss.commonservice.feign.UserFeignClient;
import com.nusiss.productservice.domain.dto.CategoryDTO;
import com.nusiss.productservice.domain.dto.CategoryPageQueryDTO;
import com.nusiss.productservice.domain.entity.Category;
import com.nusiss.productservice.domain.entity.Product;
import com.nusiss.productservice.exception.DeletionNotAllowedException;
import com.nusiss.productservice.mapper.CategoryMapper;
import com.nusiss.productservice.mapper.ProductMapper;
import com.nusiss.productservice.result.PageApiResponse;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {CategoryServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class CategoryServiceImplDiffblueTest {
    @MockBean
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryServiceImpl categoryServiceImpl;

    @MockBean
    private ProductMapper productMapper;

    @MockBean
    private UserFeignClient userFeignClient;

    /**
     * Test {@link CategoryServiceImpl#save(String, CategoryDTO)} with
     * {@code authToken}, {@code categoryDTO}.
     * <p>
     * Method under test: {@link CategoryServiceImpl#save(String, CategoryDTO)}
     */
    @Test
    @DisplayName("Test save(String, CategoryDTO) with 'authToken', 'categoryDTO'")
    void testSaveWithAuthTokenCategoryDTO() {
        // Arrange
        when(categoryMapper.insert(Mockito.<Category>any())).thenReturn(1);
        when(userFeignClient.getCurrentUserInfo(Mockito.<String>any()))
                .thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(200)));

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(1L);
        categoryDTO.setCategoryName("Category Name");

        // Act
        categoryServiceImpl.save("ABC123", categoryDTO);

        // Assert
        verify(categoryMapper).insert(isA(Category.class));
        verify(userFeignClient, atLeast(1)).getCurrentUserInfo(eq("ABC123"));
    }

    /**
     * Test {@link CategoryServiceImpl#save(String, CategoryDTO)} with
     * {@code authToken}, {@code categoryDTO}.
     * <ul>
     *   <li>Then calls {@link ResponseEntity#getStatusCode()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link CategoryServiceImpl#save(String, CategoryDTO)}
     */
    @Test
    @DisplayName("Test save(String, CategoryDTO) with 'authToken', 'categoryDTO'; then calls getStatusCode()")
    void testSaveWithAuthTokenCategoryDTO_thenCallsGetStatusCode() {
        // Arrange
        when(categoryMapper.insert(Mockito.<Category>any())).thenReturn(1);
        ResponseEntity<ApiResponse<User>> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCode()).thenReturn(null);
        when(userFeignClient.getCurrentUserInfo(Mockito.<String>any())).thenReturn(responseEntity);

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(1L);
        categoryDTO.setCategoryName("Category Name");

        // Act
        categoryServiceImpl.save("ABC123", categoryDTO);

        // Assert
        verify(categoryMapper).insert(isA(Category.class));
        verify(userFeignClient, atLeast(1)).getCurrentUserInfo(eq("ABC123"));
        verify(responseEntity, atLeast(1)).getStatusCode();
    }

    /**
     * Test {@link CategoryServiceImpl#save(String, CategoryDTO)} with
     * {@code authToken}, {@code categoryDTO}.
     * <ul>
     *   <li>Then throw {@link DeletionNotAllowedException}.</li>
     * </ul>
     * <p>
     * Method under test: {@link CategoryServiceImpl#save(String, CategoryDTO)}
     */
    @Test
    @DisplayName("Test save(String, CategoryDTO) with 'authToken', 'categoryDTO'; then throw DeletionNotAllowedException")
    void testSaveWithAuthTokenCategoryDTO_thenThrowDeletionNotAllowedException() {
        // Arrange
        when(categoryMapper.insert(Mockito.<Category>any())).thenThrow(new DeletionNotAllowedException("system"));
        when(userFeignClient.getCurrentUserInfo(Mockito.<String>any()))
                .thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(200)));

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(1L);
        categoryDTO.setCategoryName("Category Name");

        // Act and Assert
        assertThrows(DeletionNotAllowedException.class, () -> categoryServiceImpl.save("ABC123", categoryDTO));
        verify(categoryMapper).insert(isA(Category.class));
        verify(userFeignClient, atLeast(1)).getCurrentUserInfo(eq("ABC123"));
    }

    /**
     * Test {@link CategoryServiceImpl#pageQuery(CategoryPageQueryDTO)}.
     * <ul>
     *   <li>Given empty string.</li>
     *   <li>Then calls {@link CategoryPageQueryDTO#getCategoryId()}.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link CategoryServiceImpl#pageQuery(CategoryPageQueryDTO)}
     */
    @Test
    @DisplayName("Test pageQuery(CategoryPageQueryDTO); given empty string; then calls getCategoryId()")
    void testPageQuery_givenEmptyString_thenCallsGetCategoryId() {
        // Arrange
        when(categoryMapper.selectPage(Mockito.<IPage<Category>>any(), Mockito.<Wrapper<Category>>any()))
                .thenReturn(new Page<>());
        CategoryPageQueryDTO categoryPageQueryDTO = mock(CategoryPageQueryDTO.class);
        when(categoryPageQueryDTO.getPage()).thenReturn(1);
        when(categoryPageQueryDTO.getPageSize()).thenReturn(3);
        when(categoryPageQueryDTO.getCategoryId()).thenReturn(1L);
        when(categoryPageQueryDTO.getCategoryName()).thenReturn("");
        doNothing().when(categoryPageQueryDTO).setCategoryId(Mockito.<Long>any());
        doNothing().when(categoryPageQueryDTO).setCategoryName(Mockito.<String>any());
        doNothing().when(categoryPageQueryDTO).setPage(anyInt());
        doNothing().when(categoryPageQueryDTO).setPageSize(anyInt());
        categoryPageQueryDTO.setCategoryId(1L);
        categoryPageQueryDTO.setCategoryName("Category Name");
        categoryPageQueryDTO.setPage(1);
        categoryPageQueryDTO.setPageSize(3);

        // Act
        PageApiResponse actualPageQueryResult = categoryServiceImpl.pageQuery(categoryPageQueryDTO);

        // Assert
        verify(categoryMapper).selectPage(isA(IPage.class), isA(Wrapper.class));
        verify(categoryPageQueryDTO, atLeast(1)).getCategoryId();
        verify(categoryPageQueryDTO).getCategoryName();
        verify(categoryPageQueryDTO).getPage();
        verify(categoryPageQueryDTO).getPageSize();
        verify(categoryPageQueryDTO).setCategoryId(eq(1L));
        verify(categoryPageQueryDTO).setCategoryName(eq("Category Name"));
        verify(categoryPageQueryDTO).setPage(eq(1));
        verify(categoryPageQueryDTO).setPageSize(eq(3));
        assertEquals(0L, actualPageQueryResult.getTotal());
        assertTrue(actualPageQueryResult.getRecords().isEmpty());
    }

    /**
     * Test {@link CategoryServiceImpl#pageQuery(CategoryPageQueryDTO)}.
     * <ul>
     *   <li>When {@link CategoryPageQueryDTO} (default constructor) CategoryId is
     * one.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link CategoryServiceImpl#pageQuery(CategoryPageQueryDTO)}
     */
    @Test
    @DisplayName("Test pageQuery(CategoryPageQueryDTO); when CategoryPageQueryDTO (default constructor) CategoryId is one")
    void testPageQuery_whenCategoryPageQueryDTOCategoryIdIsOne() {
        // Arrange
        when(categoryMapper.selectPage(Mockito.<IPage<Category>>any(), Mockito.<Wrapper<Category>>any()))
                .thenReturn(new Page<>());

        CategoryPageQueryDTO categoryPageQueryDTO = new CategoryPageQueryDTO();
        categoryPageQueryDTO.setCategoryId(1L);
        categoryPageQueryDTO.setCategoryName("Category Name");
        categoryPageQueryDTO.setPage(1);
        categoryPageQueryDTO.setPageSize(3);

        // Act
        PageApiResponse actualPageQueryResult = categoryServiceImpl.pageQuery(categoryPageQueryDTO);

        // Assert
        verify(categoryMapper).selectPage(isA(IPage.class), isA(Wrapper.class));
        assertEquals(0L, actualPageQueryResult.getTotal());
        assertTrue(actualPageQueryResult.getRecords().isEmpty());
    }

    /**
     * Test {@link CategoryServiceImpl#deleteById(Long)}.
     * <p>
     * Method under test: {@link CategoryServiceImpl#deleteById(Long)}
     */
    @Test
    @DisplayName("Test deleteById(Long)")
    void testDeleteById() {
        // Arrange
        when(productMapper.selectList(Mockito.<Wrapper<Product>>any()))
                .thenThrow(new DeletionNotAllowedException("category_id"));

        // Act and Assert
        assertThrows(DeletionNotAllowedException.class, () -> categoryServiceImpl.deleteById(1L));
        verify(productMapper).selectList(isA(Wrapper.class));
    }

    /**
     * Test {@link CategoryServiceImpl#deleteById(Long)}.
     * <ul>
     *   <li>Given {@link CategoryMapper} {@link BaseMapper#deleteById(Serializable)}
     * return one.</li>
     *   <li>Then calls {@link BaseMapper#deleteById(Serializable)}.</li>
     * </ul>
     * <p>
     * Method under test: {@link CategoryServiceImpl#deleteById(Long)}
     */
    @Test
    @DisplayName("Test deleteById(Long); given CategoryMapper deleteById(Serializable) return one; then calls deleteById(Serializable)")
    void testDeleteById_givenCategoryMapperDeleteByIdReturnOne_thenCallsDeleteById() {
        // Arrange
        when(categoryMapper.deleteById(Mockito.<Serializable>any())).thenReturn(1);
        when(productMapper.selectList(Mockito.<Wrapper<Product>>any())).thenReturn(new ArrayList<>());

        // Act
        categoryServiceImpl.deleteById(1L);

        // Assert
        verify(categoryMapper).deleteById(isA(Serializable.class));
        verify(productMapper).selectList(isA(Wrapper.class));
    }

    /**
     * Test {@link CategoryServiceImpl#deleteById(Long)}.
     * <ul>
     *   <li>Given {@link Product} (default constructor) AvailableStock is one.</li>
     * </ul>
     * <p>
     * Method under test: {@link CategoryServiceImpl#deleteById(Long)}
     */
    @Test
    @DisplayName("Test deleteById(Long); given Product (default constructor) AvailableStock is one")
    void testDeleteById_givenProductAvailableStockIsOne() {
        // Arrange
        Product product = new Product();
        product.setAvailableStock(1);
        product.setCategoryId(1L);
        product.setCreateDatetime(mock(Timestamp.class));
        product.setCreateUser("category_id");
        product.setDescription("The characteristics of someone or something");
        product.setName("category_id");
        product.setPrice(new BigDecimal("2.3"));
        product.setProductId(1L);
        product.setProductImages(new ArrayList<>());
        product.setSellerId(1L);
        product.setUpdateDatetime(mock(Timestamp.class));
        product.setUpdateUser("2020-03-01");

        ArrayList<Product> productList = new ArrayList<>();
        productList.add(product);
        when(productMapper.selectList(Mockito.<Wrapper<Product>>any())).thenReturn(productList);

        // Act and Assert
        assertThrows(DeletionNotAllowedException.class, () -> categoryServiceImpl.deleteById(1L));
        verify(productMapper).selectList(isA(Wrapper.class));
    }

    /**
     * Test {@link CategoryServiceImpl#update(String, CategoryDTO)} with
     * {@code authToken}, {@code categoryDTO}.
     * <p>
     * Method under test: {@link CategoryServiceImpl#update(String, CategoryDTO)}
     */
    @Test
    @DisplayName("Test update(String, CategoryDTO) with 'authToken', 'categoryDTO'")
    void testUpdateWithAuthTokenCategoryDTO() {
        // Arrange
        when(categoryMapper.updateById(Mockito.<Category>any())).thenReturn(1);
        when(userFeignClient.getCurrentUserInfo(Mockito.<String>any()))
                .thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(200)));

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(1L);
        categoryDTO.setCategoryName("Category Name");

        // Act
        categoryServiceImpl.update("ABC123", categoryDTO);

        // Assert
        verify(categoryMapper).updateById(isA(Category.class));
        verify(userFeignClient).getCurrentUserInfo(eq("ABC123"));
    }

    /**
     * Test {@link CategoryServiceImpl#update(String, CategoryDTO)} with
     * {@code authToken}, {@code categoryDTO}.
     * <ul>
     *   <li>Then calls {@link ResponseEntity#getStatusCode()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link CategoryServiceImpl#update(String, CategoryDTO)}
     */
    @Test
    @DisplayName("Test update(String, CategoryDTO) with 'authToken', 'categoryDTO'; then calls getStatusCode()")
    void testUpdateWithAuthTokenCategoryDTO_thenCallsGetStatusCode() {
        // Arrange
        when(categoryMapper.updateById(Mockito.<Category>any())).thenReturn(1);
        ResponseEntity<ApiResponse<User>> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCode()).thenReturn(null);
        when(userFeignClient.getCurrentUserInfo(Mockito.<String>any())).thenReturn(responseEntity);

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(1L);
        categoryDTO.setCategoryName("Category Name");

        // Act
        categoryServiceImpl.update("ABC123", categoryDTO);

        // Assert
        verify(categoryMapper).updateById(isA(Category.class));
        verify(userFeignClient).getCurrentUserInfo(eq("ABC123"));
        verify(responseEntity).getStatusCode();
    }

    /**
     * Test {@link CategoryServiceImpl#update(String, CategoryDTO)} with
     * {@code authToken}, {@code categoryDTO}.
     * <ul>
     *   <li>Then throw {@link DeletionNotAllowedException}.</li>
     * </ul>
     * <p>
     * Method under test: {@link CategoryServiceImpl#update(String, CategoryDTO)}
     */
    @Test
    @DisplayName("Test update(String, CategoryDTO) with 'authToken', 'categoryDTO'; then throw DeletionNotAllowedException")
    void testUpdateWithAuthTokenCategoryDTO_thenThrowDeletionNotAllowedException() {
        // Arrange
        when(categoryMapper.updateById(Mockito.<Category>any())).thenThrow(new DeletionNotAllowedException("system"));
        when(userFeignClient.getCurrentUserInfo(Mockito.<String>any()))
                .thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(200)));

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(1L);
        categoryDTO.setCategoryName("Category Name");

        // Act and Assert
        assertThrows(DeletionNotAllowedException.class, () -> categoryServiceImpl.update("ABC123", categoryDTO));
        verify(categoryMapper).updateById(isA(Category.class));
        verify(userFeignClient).getCurrentUserInfo(eq("ABC123"));
    }

    /**
     * Test {@link CategoryServiceImpl#queryCurrentUser(String)}.
     * <p>
     * Method under test: {@link CategoryServiceImpl#queryCurrentUser(String)}
     */
    @Test
    @DisplayName("Test queryCurrentUser(String)")
    void testQueryCurrentUser() {
        // Arrange
        when(userFeignClient.getCurrentUserInfo(Mockito.<String>any()))
                .thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(200)));

        // Act
        String actualQueryCurrentUserResult = categoryServiceImpl.queryCurrentUser("ABC123");

        // Assert
        verify(userFeignClient).getCurrentUserInfo(eq("ABC123"));
        assertEquals("system", actualQueryCurrentUserResult);
    }

    /**
     * Test {@link CategoryServiceImpl#queryCurrentUser(String)}.
     * <ul>
     *   <li>Then calls {@link ResponseEntity#getStatusCode()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link CategoryServiceImpl#queryCurrentUser(String)}
     */
    @Test
    @DisplayName("Test queryCurrentUser(String); then calls getStatusCode()")
    void testQueryCurrentUser_thenCallsGetStatusCode() {
        // Arrange
        ResponseEntity<ApiResponse<User>> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCode()).thenReturn(null);
        when(userFeignClient.getCurrentUserInfo(Mockito.<String>any())).thenReturn(responseEntity);

        // Act
        String actualQueryCurrentUserResult = categoryServiceImpl.queryCurrentUser("ABC123");

        // Assert
        verify(userFeignClient).getCurrentUserInfo(eq("ABC123"));
        verify(responseEntity).getStatusCode();
        assertEquals("system", actualQueryCurrentUserResult);
    }
}
