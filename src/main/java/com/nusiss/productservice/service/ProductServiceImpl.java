package com.nusiss.productservice.service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nusiss.productservice.dao.ProductMapper;
import com.nusiss.productservice.entity.Product;
import com.nusiss.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // 对Product Service进行服务实现，并且注入到 Spring 容器中
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public Product getById(Long id) {
        return productMapper.selectById(id); // 通过 MyBatis Plus 方法获取
    }

    @Override
    public List<Product> getAll() {
        return productMapper.selectList(new QueryWrapper<>()); // 查询全部
    }

    @Override
    public Product createProduct(Product product) {
        productMapper.insert(product); // 插入数据
        return product;
    }

    @Override
    public Product updateProduct(Product product) {
        productMapper.updateById(product); // 根据 ID 更新
        return product;
    }

    @Override
    public boolean deleteProductById(Long id) {
        return productMapper.deleteById(id) > 0; // 删除成功返回 true
    }
}