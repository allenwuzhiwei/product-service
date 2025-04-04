package com.nusiss.productservice.controller;

import com.nusiss.productservice.config.ApiResponse;
import com.nusiss.productservice.dao.ProductMapper;
import com.nusiss.productservice.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
1. 提供 Product 实体的 CRUD 接口
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductMapper productMapper;

    // 获取所有产品
    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        List<Product> products = productMapper.selectList(null); // 查询所有产品
        return ResponseEntity.ok(new ApiResponse<>(true, "The product list retrieved successfully!!!", products)); // 返回成功响应
    }

    // 根据 ID 获取产品
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable Long id) {
        Product product = productMapper.selectById(id);
        if (product != null) {
            return ResponseEntity.ok(new ApiResponse<>(true, "The product retrieved successfully", product)); // 返回成功响应
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Product not found", null)); // 返回失败响应
        }
    }

    // 创建新产品
    @PostMapping
    public ResponseEntity<ApiResponse<Product>> createProduct(@RequestBody Product product) {
        productMapper.insert(product); //创建新产品
        return ResponseEntity.status(201).body(new ApiResponse<>(true, "Product created successfully", product));
    }

    // 更新产品信息
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(@PathVariable Long id, @RequestBody Product updated) {
        Product existing = productMapper.selectById(id);
        if (existing != null) {
            updated.setId(id); // 保证更新的是同一个产品
            productMapper.updateById(updated); //根据产品ID更新
            return ResponseEntity.ok(new ApiResponse<>(true, "Product updated successfully", updated));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Product not found", null));
        }
    }

    // 删除产品
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable Long id) {
        Product product = productMapper.selectById(id);
        if (product != null) {
            productMapper.deleteById(id); //根据产品ID进行删除
            return ResponseEntity.ok(new ApiResponse<>(true, "Product deleted successfully", null));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Product not found", null));
        }
    }
}
