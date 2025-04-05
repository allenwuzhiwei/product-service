package com.nusiss.productservice.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nusiss.productservice.dao.ProductMapper;
import com.nusiss.productservice.entity.Product;
import com.nusiss.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    // 查询所有商品
    @Override
    public List<Product> getAllProducts() {
        return productMapper.selectList(null);
    }

    // 根据id查询商品
    @Override
    public Product getProductById(Long id) {
        return productMapper.selectById(id);
    }

    // 创建商品
    @Override
    public Product createProduct(Product product) {
        productMapper.insert(product);
        return product;
    }

    // 更新商品
    @Override
    public Product updateProduct(Long id, Product product) {
        Product existing = productMapper.selectById(id);
        if (existing == null) return null;

        product.setId(id);
        productMapper.updateById(product);
        return product;
    }

    // 删除商品
    @Override
    public boolean deleteProduct(Long id) {
        Product existing = productMapper.selectById(id);
        if (existing == null) return false;

        return productMapper.deleteById(id) > 0;
    }

    //扩展功能 1.分页功能
    /*
     分页查询商品列表
     @param page 当前页码
     @param size 每页条数
     @return Page<Product> 分页结果对象
     */
    @Override
    public Page<Product> getProductPage(int page, int size) {
        Page<Product> pageRequest = new Page<>(page, size); // 创建分页对象
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>(); // 可以后续添加搜索条件
        return productMapper.selectPage(pageRequest, queryWrapper); // 执行分页查询
    }

    // 扩展功能 2.关键词搜索功能
    /*
     根据关键词搜索产品，模糊匹配 name 和 description 字段
     @param keyword 搜索关键词
     @return 匹配的产品列表
     */

    @Override
    public List<Product> searchProducts(String keyword) {
        // 构造查询条件
        //QueryWrapper 是 MyBatis Plus 提供的一个工具类，用于构建 SQL 查询条件
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        //创建了一个 QueryWrapper 对象，专门用于 Product 实体类。这个 queryWrapper 将用于添加查询条件。
        queryWrapper.lambda() //lambda() 方法返回一个支持 Lambda 表达式的查询构造器。
                // lambda() 方法返回一个支持 Lambda 表达式的查询构造器。
                .like(Product::getName, keyword) //Product::getName 是一个方法引用，指向 Product 类中的 getName() 方法。 keyword 是希望匹配的字符串。
                .or() //如果上一个条件（模糊匹配 name 字段）不满足，查询将继续检查下一个条件
                .like(Product::getDescription, keyword); //匹配description字段，检查 Product 实体中 description 字段是否包含 keyword查 Product 实体中 description 字段是否包含 keyword，

        // 执行查询
        return productMapper.selectList(queryWrapper);
    }

    // 扩展功能 3.多条件筛选功能
    /*
     根据多个条件筛选产品
     (支持单条件筛选，或者组合条件筛选)
     @param name 产品名称
     @param category 产品类别
     @param status 产品状态
     @param minPrice 最低价格
     @param maxPrice 最高价格
     @param rating 产品评分
     @return 匹配的产品列表
     */
    @Override
    public List<Product> filterProducts(String name, String category, String status, BigDecimal minPrice, BigDecimal maxPrice, Double rating) {
        // 使用 MyBatis Plus 提供的 QueryWrapper 构造动态查询条件
        QueryWrapper<Product> wrapper = new QueryWrapper<>();

        // 模糊查询 name（商品名称）
        if (name != null && !name.isEmpty()) {
            wrapper.like("name", name);
        }

        // 精确匹配 category（商品种类）
        if (category != null && !category.isEmpty()) {
            wrapper.eq("category", category);
        }

        // 精确匹配 status（商品状态）
        if (status != null && !status.isEmpty()) {
            wrapper.eq("status", status);
        }

        // 最低价格（大于等于）
        if (minPrice != null) {
            wrapper.ge("price", minPrice);
        }

        // 最高价格（小于等于）
        if (maxPrice != null) {
            wrapper.le("price", maxPrice);
        }

        // 评分（大于等于）
        if (rating != null) {
            wrapper.ge("rating", rating);
        }

        // 返回查询结果
        return productMapper.selectList(wrapper);
    }

    // 扩展功能 4.排序功能 - 支持根据指定字段进行升序/降序排序，可结合分页和筛选条件一起使用
    /*
     排序 + 筛选 + 分页功能实现，使用 MyBatis Plus 的 QueryWrapper 构建动态查询条件
     */

    @Override
    public IPage<Product> filterProductsWithSorting(
            String name,           // 商品名称
            String category,       // 商品分类
            String status,         // 商品状态
            BigDecimal minPrice,   // 最低价格
            BigDecimal maxPrice,   // 最高价格
            Double rating,         // 最低评分
            String sortBy,         // 排序字段（如 price、rating、create_datetime）
            String order,          // 排序方式（asc/desc）
            int page,              // 当前页码
            int size               // 每页条数
    ) {
        // 1. 构建分页对象，传入当前页码和每页数量
        Page<Product> pageObj = new Page<>(page, size); //如page = 1，表示第一页,size = 10，表示每页查 10 条数据

        // 2. 构建查询条件
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>(); //动态查询构造器

        // 3. 动态添加筛选条件（字段不为空才添加）
        if (StringUtils.hasText(name)) {
            queryWrapper.like("name", name); //用户传了 name，就在 SQL 中添加 name LIKE '%name%' 条件。如果 name 非空，则模糊匹配商品名称。
        }
        if (StringUtils.hasText(category)) {
            queryWrapper.eq("category", category);
        }
        if (StringUtils.hasText(status)) {
            queryWrapper.eq("status", status); //如果 status 非空，则过滤状态为这个值的产品。
        }
        if (minPrice != null) {
            queryWrapper.ge("price", minPrice); //如果 minPrice 非空，则过滤价格大于等于这个值的产品。
        }
        if (maxPrice != null) {
            queryWrapper.le("price", maxPrice); //如果 maxPrice 非空，则过滤价格小于等于这个值的产品。
        }
        if (rating != null) {
            queryWrapper.ge("rating", rating);
        }

        // 4. 排序逻辑（字段名必须存在于数据库表中）
        if (StringUtils.hasText(sortBy)) {
            boolean isAsc = "asc".equalsIgnoreCase(order); //判断 order 是否为 asc 大小写不敏感
            if (isAsc) {
                queryWrapper.orderByAsc(sortBy); //如果 order 为 asc，则按照升序排序。
            } else {
                queryWrapper.orderByDesc(sortBy); //如果 order 为 desc，则按照降序排序。
            }
        }

        // 5. 执行分页查询并返回结果
        return productMapper.selectPage(pageObj, queryWrapper);
    }
}
