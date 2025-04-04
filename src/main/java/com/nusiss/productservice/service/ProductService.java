package com.nusiss.productservice.service;

import com.nusiss.productservice.entity.Product;
import java.util.List;

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
}
