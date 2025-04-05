package com.nusiss.productservice.service;

import com.nusiss.productservice.entity.Product;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nusiss.productservice.entity.Product;


public interface ProductService {

    // 查询所有商品
    List<Product> getAllProducts();

    // 根据 ID 查询商品
    Product getProductById(Long id);

    // 新增商品
    Product createProduct(Product product);

    // 修改商品
    Product updateProduct(Long id, Product product);

    // 删除商品
    boolean deleteProduct(Long id);

    /*扩展功能
    1.分页功能 - 分页查询商品列表
    @param page 当前页码，从1开始
    @param size 每页条数
    @return Page<Product> 分页结果
    */
    Page<Product> getProductPage(int page, int size); // 添加分页功能，使用MyBatis Plus 提供的的分页工具类

     /*
     2.关键词搜索功能 - 根据关键词搜索产品，匹配 name 和 description 字段
     @param keyword 搜索关键词
     @return 匹配的产品列表
     */
     List<Product> searchProducts(String keyword);

     /*
     2.多条件筛选功能 - 支持通过 name、category、status、minPrice、maxPrice、rating - 支持单条件筛选，或者组合条件筛选
     @param name 商品名称
     @param category 商品种类
     @param status 商品状态
     @param minPrice 最低价格（大于等于）
     @param maxPrice 最高价格（小于等于）
     @param rating 最低评分（大于等于）
     @return 匹配的产品列表
    */
     List<Product> filterProducts(String name, String category, String status, BigDecimal minPrice, BigDecimal maxPrice, Double rating);

}