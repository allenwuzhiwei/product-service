package com.nusiss.productservice.service.impl;

import com.nusiss.productservice.dao.ProductMapper;
import com.nusiss.productservice.entity.Product;
import com.nusiss.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

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
}
