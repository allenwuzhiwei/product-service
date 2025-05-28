package com.nusiss.productservice.controller;

import com.nusiss.productservice.config.ApiResponse;
import com.nusiss.productservice.dao.ProductMapper;
import com.nusiss.productservice.entity.ProductMedia;
import com.nusiss.productservice.service.ProductMediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/*
 提供 ProductMedia 的 REST 接口
 */
@RestController
@RequestMapping("/media")
public class ProductMediaController {

    //  创建日志记录器
    private static final Logger logger = LoggerFactory.getLogger(ProductMediaController.class);

    /*
    注入 ProductMapper，用于检查productId是否存在
    */
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductMediaService productMediaService;

    @Value("${file.access.host}") // 从配置文件中读取文件访问主机地址
    private String fileAccessHost;

    /*
    思路：接收 MultipartFile（文件上传）+ productId → 生成 URL → 写入数据库
    创建媒体记录,使用上传图片生成的URL，并保存到数据库中
    @param productId 商品ID，必须已存在于 products 表中
    @param file      上传的图片文件
    @return 上传并创建成功的 ProductMedia 对象
     */
    @PostMapping("/uploadWithSave")
    public ApiResponse<ProductMedia> uploadAndSaveMedia(@RequestParam("productId") Long productId,
                                                        @RequestParam("file") MultipartFile file) {
        try {
            // Step 1: 验证商品是否存在
            if (productMapper.selectById(productId) == null) {
                return ApiResponse.fail("商品不存在，productId 无效");
            }

            // Step 2: 保存文件到resource文件夹下面的upload file目录中
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.contains(".")) {
                return ApiResponse.fail("上传失败：文件名为空或格式不合法");
            }
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

            String newFileName = UUID.randomUUID().toString() + suffix;

            // 使用项目路径拼接保存目录
            String projectRootPath = System.getProperty("user.dir");
            String uploadDir = projectRootPath + "/src/main/resources/static/uploadFile";

            File dest = new File(uploadDir, newFileName);
            file.transferTo(dest);

            // Step 3: 构建 URL
            String url = fileAccessHost + "/uploadFile/" + newFileName;

            // Step 4: 创建媒体记录
            ProductMedia media = new ProductMedia();
            media.setProductId(productId);
            media.setMediaType("image");
            media.setUrl(url);
            media.setCreateUser("system");
            media.setUpdateUser("system");
            media.setCreateDatetime(LocalDateTime.now());
            media.setUpdateDatetime(LocalDateTime.now());

            productMediaService.createProductMedia(media);

            return ApiResponse.success(media);
        }
        catch (Exception e) {
            logger.error("xxx失败", e);
            return ApiResponse.fail("xxx失败：" + e.getMessage());
        }

    }


    /*
     创建媒体记录(已有 URL 的媒体手动插入,使用外链图片)
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
     更新媒体记录，用于外部链接图片的插入更新
     @param media 要更新的媒体对象
     @return 更新是否成功
     */
    @PutMapping
    public ResponseEntity<ApiResponse<Boolean>> updateProductMedia(@RequestBody ProductMedia media) {
        boolean updated = productMediaService.updateProductMedia(media);
        return ResponseEntity.ok(new ApiResponse<>(updated, updated ? "Media updated" : "Update failed", updated));
    }

    @PutMapping("/replaceImage")
    public ApiResponse<ProductMedia> replaceProductImage(@RequestParam("mediaId") Long mediaId,
                                                         @RequestParam("file") MultipartFile file) {
        try {
            // 1. 查询旧媒体记录
            ProductMedia existing = productMediaService.getProductMediaById(mediaId);
            if (existing == null) {
                return ApiResponse.fail("未找到对应的媒体记录");
            }

            // 2. 删除旧文件
            String oldUrl = existing.getUrl();
            int lastSlash = oldUrl.lastIndexOf('/');
            String oldFileName = oldUrl.substring(lastSlash + 1);
            String projectRoot = System.getProperty("user.dir");
            String oldFilePath = projectRoot + "/src/main/resources/static/uploadFile/" + oldFileName;
            File oldFile = new File(oldFilePath);
            if (oldFile.exists()) {
                oldFile.delete();
            }

            // 3. 保存新文件
            String originalName = file.getOriginalFilename();
            if (originalName == null || !originalName.contains(".")) {
                return ApiResponse.fail("上传失败：文件名为空或格式不合法");
            }
            String suffix = originalName.substring(originalName.lastIndexOf("."));

            String newFileName = UUID.randomUUID().toString() + suffix;
            String uploadDir = projectRoot + "/src/main/resources/static/uploadFile";
            File dest = new File(uploadDir, newFileName);
            file.transferTo(dest);

            // 4. 构建新 URL 并更新记录
            String newUrl = fileAccessHost + "/uploadFile/" + newFileName;
            existing.setUrl(newUrl);
            existing.setUpdateDatetime(LocalDateTime.now());
            existing.setUpdateUser("system");

            boolean updated = productMediaService.updateProductMedia(existing);
            if (updated) {
                return ApiResponse.success(existing);
            } else {
                return ApiResponse.fail("数据库更新失败");
            }
        }
        catch (Exception e) {
            logger.error("替换图片失败", e);
            return ApiResponse.fail("替换图片失败：" + e.getMessage());
        }
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
