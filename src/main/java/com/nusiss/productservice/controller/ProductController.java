package com.nusiss.productservice.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nusiss.productservice.config.ApiResponse;
import com.nusiss.productservice.entity.Product;
import com.nusiss.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    /*
     扩展功能2 接口：关键词搜索产品接口
     仅支持根据产品名称（name）字段进行模糊搜索
     @param keyword 关键词（用于模糊匹配产品名称）
     @return 匹配的产品列表
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Product>>> searchProducts(@RequestParam String keyword) {
        List<Product> result = productService.searchProducts(keyword); // 调用 service 进行搜索
        return ResponseEntity.ok(new ApiResponse<>(true, "Products matched successfully", result));
    }


    /*
    扩展功能3 接口：多条件筛选产品接口(筛选支持单条件筛选，或者组合条件筛选)
    支持根据名称、类别、状态、价格范围、最低评分进行高级筛选
    @param name 产品名称
    @param category 产品类别
    @param status 产品状态
    @param minPrice 最低价格
    @param maxPrice 最高价格
    @param rating 最低评分
    @return 匹配的产品列表
     */

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<Product>>> filterProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Double rating
    ) {
        List<Product> products = productService.filterProducts(name, category, status, minPrice, maxPrice, rating);
        return ResponseEntity.ok(new ApiResponse<>(true, "Products filtered successfully", products));
    }

    /*
    扩展功能4 接口：排序功能（可组合分页和多条件筛选）
     * @param name 商品名称（模糊搜索）
     * @param category 商品分类
     * @param status 商品状态（如 AVAILABLE / UNAVAILABLE）
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @param rating 最低评分
     * @param sortBy 排序字段（如 price, rating, create_datetime）
     * @param order 排序方式（asc 或 desc）
     * @param page 当前页码（从 1 开始）
     * @param size 每页数量
     * @return ApiResponse 分页后的商品结果
     */
    @GetMapping("/sort")
    public ResponseEntity<ApiResponse<IPage<Product>>> filterProductsWithSorting(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Double rating,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        IPage<Product> products = productService.filterProductsWithSorting(
                name, category, status, minPrice, maxPrice, rating,
                sortBy, order, page, size
        );

        return ResponseEntity.ok(new ApiResponse<>(true, "Products filtered successfully", products));
    }

    /*
     推荐接口：猜你喜欢（同分类热门商品）
     */
    @GetMapping("/recommend/related/{productId}")
    public ResponseEntity<ApiResponse<List<Product>>> recommendRelatedProducts(@PathVariable Long productId) {
        List<Product> recommended = productService.getRelatedProducts(productId, 5);
        return ResponseEntity.ok(ApiResponse.success(recommended));
    }

    /*
    推荐接口：热门商品推荐(基于用户购买过的商品)
    */
    @GetMapping("/recommend/user/{userId}/top")
    public ResponseEntity<ApiResponse<List<Product>>> recommendByUserOrder(@PathVariable Long userId) {
        List<Product> recommended = productService.getTopRecommendedProductsByUser(userId, 5);
        return ResponseEntity.ok(ApiResponse.success(recommended));
    }



}
