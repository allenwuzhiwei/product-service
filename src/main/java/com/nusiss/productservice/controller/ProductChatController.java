package com.nusiss.productservice.controller;

import com.nusiss.productservice.dao.ProductMapper;
import com.nusiss.productservice.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/*
 ChatBox 后端接口控制器：支持前端按钮点击触发推荐商品、平台介绍、促销信息等功能
 */
@RestController
@RequestMapping("/api/chat")
public class ProductChatController {

    @Autowired
    private ProductMapper productMapper;

    /*
     推荐评分最高的5个手机
     URL: /api/chat/recommend-smartphones
     */
    @GetMapping("/recommend-smartphones")
    public Map<String, Object> recommendSmartphones() {
        return recommendTopProducts("Smartphones", "以下是我们推荐的热门手机：\n");
    }

    /*
     推荐评分最高的5个平板
     URL: /api/chat/recommend-pads
     */
    @GetMapping("/recommend-pads")
    public Map<String, Object> recommendPads() {
        return recommendTopProducts("Pad", "以下是我们推荐的热门平板电脑：\n");
    }

    /*
     推荐评分最高的5个笔记本
     URL: /api/chat/recommend-laptops
     */
    @GetMapping("/recommend-laptops")
    public Map<String, Object> recommendLaptops() {
        return recommendTopProducts("Laptops", "以下是我们推荐的热门笔记本电脑：\n");
    }

    /*
     平台介绍信息
     URL: /api/chat/about
     */
    @GetMapping("/about")
    public Map<String, Object> aboutUs() {
        String intro = "我们智能电商平台致力于为用户提供个性化购物体验与优质服务。" +
                "平台整合商品、库存、支付、推荐等多个微服务模块，欢迎体验！";
        return responseText(intro);
    }

    /*
     当前促销信息
     URL: /api/chat/promotions
     */
    @GetMapping("/promotions")
    public Map<String, Object> getPromotions() {
        String promotions = "🎉 当前促销活动：\n" +
                "- 全场满200减30\n" +
                "- 学生专享95折\n" +
                "- 限时秒杀：部分热门商品低至5折，先到先得！";
        return responseText(promotions);
    }

    /*
     内部通用推荐逻辑：查找分类下评分最高的前5个商品
     */
    private Map<String, Object> recommendTopProducts(String category, String title) {
        List<Product> productList = productMapper.findTop5ByCategoryOrderByRatingDesc(category);
        if (productList.isEmpty()) {
            return responseText("当前暂无该类商品（分类：" + category + "）");
        }

        StringBuilder sb = new StringBuilder(title);
        for (Product p : productList) {
            sb.append("- ").append(p.getName())
                    .append("（￥").append(p.getPrice())
                    .append("，评分 ").append(p.getRating()).append("）\n");
        }
        return responseText(sb.toString());
    }

    /*
     封装统一响应格式
     */
    private Map<String, Object> responseText(String text) {
        return Map.of("parts", List.of(
                Map.of("type", "text", "text", text)
        ));
    }
}
