package com.nusiss.productservice.service.impl;

import com.nusiss.productservice.dao.ProductMapper;
import com.nusiss.productservice.entity.Product;
import com.nusiss.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<Product> getAllProducts() {
        return productMapper.selectList(null);
    }

    @Override
    public Product getProductById(Long id) {
        return productMapper.selectById(id);
    }

    @Override
    public Product createProduct(Product product) {
        productMapper.insert(product);
        return product;
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        Product existing = productMapper.selectById(id);
        if (existing == null) return null;

        product.setId(id);
        productMapper.updateById(product);
        return product;
    }

    @Override
    public boolean deleteProduct(Long id) {
        Product existing = productMapper.selectById(id);
        if (existing == null) return false;

        return productMapper.deleteById(id) > 0;
    }
}
