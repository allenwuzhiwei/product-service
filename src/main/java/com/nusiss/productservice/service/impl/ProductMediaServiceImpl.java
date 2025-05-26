package com.nusiss.productservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nusiss.productservice.entity.ProductMedia;
import com.nusiss.productservice.dao.ProductMediaMapper;
import com.nusiss.productservice.service.ProductMediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/*
 ProductMediaService 的实现类
 */
@Service
public class ProductMediaServiceImpl implements ProductMediaService {

    @Autowired
    private ProductMediaMapper productMediaMapper;

    /*
     创建新的 ProductMedia 记录
     @param media ProductMedia 实体对象
     @return 创建成功的对象
     */
    @Override
    public ProductMedia createProductMedia(ProductMedia media) {
        int rows = productMediaMapper.insert(media);
        return rows > 0 ? media : null;
    }

    /*
     根据 ID 获取单个媒体信息
     @param id 媒体主键 ID
     @return 对应的媒体对象，若不存在返回 null
     */
    @Override
    public ProductMedia getProductMediaById(Long id) {
        return productMediaMapper.selectById(id);
    }

    /*
     获取所有媒体信息列表
     @return 所有 ProductMedia 记录
     */
    @Override
    public List<ProductMedia> getAllProductMedia() {
        return productMediaMapper.selectList(null);
    }

    /*
     根据产品 ID 查询该产品关联的所有媒体记录（图片、视频等）
     @param productId 产品 ID
     @return 对应产品的媒体列表
     */
    @Override
    public List<ProductMedia> getMediaByProductId(Long productId) {
        QueryWrapper<ProductMedia> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId);
        return productMediaMapper.selectList(queryWrapper);
    }

    /*
     更新媒体信息（用于外部图片URL的使用）
     @param media 包含更新信息的 ProductMedia 对象
     @return 更新成功返回 true，失败返回 false
     */
    @Override
    public boolean updateProductMedia(ProductMedia media) {
        return productMediaMapper.updateById(media) > 0;
    }

    /*
    根据 ID 删除媒体记录，同时删除本地图片文件
    @param id 媒体 ID
    @return 删除成功返回 true，失败返回 false
    */
    @Override
    public boolean deleteProductMedia(Long id) {
        // 1. 查询媒体记录，获取 URL
        ProductMedia media = productMediaMapper.selectById(id);
        if (media == null) {
            return false;
        }

        // 2. 提取图片文件名
        String url = media.getUrl();
        int lastSlashIndex = url.lastIndexOf('/');
        String fileName = url.substring(lastSlashIndex + 1); // 例：abc123.png

        // 3. 拼接本地路径
        String projectRootPath = System.getProperty("user.dir");
        String filePath = projectRootPath + "/src/main/resources/static/uploadFile/" + fileName;

        // 4. 删除本地图片文件
        File file = new File(filePath);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                System.err.println("警告：图片文件删除失败：" + filePath);
            }
        } else {
            System.err.println("警告：图片文件不存在，跳过删除：" + filePath);
        }

        // 5. 删除数据库记录
        return productMediaMapper.deleteById(id) > 0;
    }

}
