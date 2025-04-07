package com.nusiss.productservice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nusiss.productservice.entity.ProductMedia;


import java.util.List;

/*
 ProductMedia 业务逻辑层接口
 提供 ProductMedia 的 CRUD 操作接口定义
 */
public interface ProductMediaService {

    /*
     创建新的 ProductMedia 记录
     @param media ProductMedia 实体对象
     @return 创建成功的对象
     */
    ProductMedia createProductMedia(ProductMedia media);

    /*
     根据 ID 获取单个媒体信息
     @param id 媒体主键 ID
     @return 对应的媒体对象，若不存在返回 null
     */
    ProductMedia getProductMediaById(Long id);

    /*
     获取所有媒体信息列表
     @return 所有 ProductMedia 记录
     */
    List<ProductMedia> getAllProductMedia();

    /*
     根据产品 ID 查询该产品关联的所有媒体记录（图片、视频等）
     @param productId 产品 ID
     @return 对应产品的媒体列表
     */
    List<ProductMedia> getMediaByProductId(Long productId);

    /*
     更新媒体信息
     @param media 包含更新信息的 ProductMedia 对象
     @return 更新成功返回 true，失败返回 false
     */
    boolean updateProductMedia(ProductMedia media);

    /*
     根据 ID 删除媒体记录
     @param id 媒体 ID
     @return 删除成功返回 true，失败返回 false
     */
    boolean deleteProductMedia(Long id);
}
