package com.nusiss.productservice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nusiss.productservice.config.ApiResponse;
import com.nusiss.productservice.entity.Product;
import com.nusiss.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
产品controller，负责处理与 Product 实体相关的 REST API 请求
 */
@RestController
@RequestMapping("/products") // 所有接口以 /products 开头
public class ProductController {

    @Autowired
    private ProductService productService; // 注入 ProductService 接口（由 Spring 自动注入实现类）

    /*
     获取所有产品列表
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(new ApiResponse<>(true, "The product list retrieved successfully", products));
    }

    /*
     根据 ID 获取单个产品信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(new ApiResponse<>(true, "The product retrieved successfully", product));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Product not found", null));
        }
    }

    /*
     创建新产品
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Product>> createProduct(@RequestBody Product product) {
        Product created = productService.createProduct(product);
        return ResponseEntity.status(201).body(new ApiResponse<>(true, "Product created successfully", created));
    }

    /*
     更新指定 ID 的产品
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(@PathVariable Long id, @RequestBody Product updated) {
        Product updatedProduct = productService.updateProduct(id, updated);
        if (updatedProduct != null) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Product updated successfully", updatedProduct));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Product not found", null));
        }
    }

    /*
     删除指定 ID 的产品
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable Long id) {
        boolean deleted = productService.deleteProduct(id);
        if (deleted) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Product deleted successfully", null));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Product not found", null));
        }
    }

    /*
     扩展功能1 接口：分页查询产品列表接口
     */
    @GetMapping("/page")
    public ResponseEntity<ApiResponse<Page<Product>>> getProductsByPage(
            @RequestParam(defaultValue = "1") int page,       // 当前页码，默认第1页
            @RequestParam(defaultValue = "10") int size       // 每页显示条数，默认10条
    ) {
        Page<Product> resultPage = productService.getProductPage(page, size);
        return ResponseEntity.ok(new ApiResponse<>(true, "Paged products retrieved successfully", resultPage));
    }
}
