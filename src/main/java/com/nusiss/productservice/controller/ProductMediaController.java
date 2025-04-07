package com.nusiss.productservice.controller;

import com.nusiss.productservice.config.ApiResponse;
import com.nusiss.productservice.entity.ProductMedia;
import com.nusiss.productservice.service.ProductMediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 提供 ProductMedia 的 REST 接口
 */
@RestController
@RequestMapping("/media")
public class ProductMediaController {

    @Autowired
    private ProductMediaService productMediaService;

    /*
     创建媒体记录
     @param media 媒体对象
     @return 创建成功的媒体信息
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ProductMedia>> createProductMedia(@RequestBody ProductMedia media) {
        ProductMedia created = productMediaService.createProductMedia(media);
        return ResponseEntity.ok(new ApiResponse<>(true, "Media created successfully", created));
    }

    /*
     获取所有媒体记录
     @return 媒体列表
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductMedia>>> getAllMedia() {
        List<ProductMedia> mediaList = productMediaService.getAllProductMedia();
        return ResponseEntity.ok(new ApiResponse<>(true, "All media retrieved", mediaList));
    }

    /*
     根据 ID 获取单个媒体信息
     @param id 媒体主键 ID
     @return 媒体信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductMedia>> getMediaById(@PathVariable Long id) {
        ProductMedia media = productMediaService.getProductMediaById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Media found", media));
    }

    /*
     根据 ProductId 查询关联的媒体（图片、视频）
     @param productId 产品 ID
     @return 所有媒体记录
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<ProductMedia>>> getMediaByProductId(@PathVariable Long productId) {
        List<ProductMedia> mediaList = productMediaService.getMediaByProductId(productId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Media by product retrieved", mediaList));
    }

    /*
     更新媒体记录
     @param media 要更新的媒体对象
     @return 更新是否成功
     */
    @PutMapping
    public ResponseEntity<ApiResponse<Boolean>> updateProductMedia(@RequestBody ProductMedia media) {
        boolean updated = productMediaService.updateProductMedia(media);
        return ResponseEntity.ok(new ApiResponse<>(updated, updated ? "Media updated" : "Update failed", updated));
    }

    /*
     删除媒体记录
     @param id 媒体 ID
     @return 删除是否成功
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteProductMedia(@PathVariable Long id) {
        boolean deleted = productMediaService.deleteProductMedia(id);
        return ResponseEntity.ok(new ApiResponse<>(deleted, deleted ? "Media deleted" : "Delete failed", deleted));
    }
}
