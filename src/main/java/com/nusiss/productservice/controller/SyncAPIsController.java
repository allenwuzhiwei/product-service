package com.nusiss.productservice.controller;

import com.nusiss.commonservice.entity.Permission;
import com.nusiss.commonservice.feign.UserFeignClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/permissions")
public class SyncAPIsController {

    /*private final RequestMappingHandlerMapping handlerMapping;

    @Autowired
    public SyncAPIsController(ApplicationContext context) {
        this.handlerMapping = context.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
    }

    @Autowired
    private UserFeignClient userFeignClient;

    @PostMapping("/paged/endpoints")
    public ResponseEntity<Map<String, Object>> listAllApiEndpoints(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String name) {

        List<String> allEndpoints = new ArrayList<>();

        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo info = entry.getKey();

            Set<PathPattern> pathPatterns = Optional.ofNullable(info.getPathPatternsCondition())
                    .map(condition -> condition.getPatterns())
                    .orElse(Collections.emptySet());

            Set<RequestMethod> methods = info.getMethodsCondition().getMethods();

            for (PathPattern pattern : pathPatterns) {
                String path = pattern.getPatternString();
                if (methods.isEmpty()) {
                    allEndpoints.add("ALL:" + path);
                } else {
                    for (RequestMethod method : methods) {
                        allEndpoints.add(method.name() + ":" + path);
                        if(StringUtils.isNotBlank(path) && !path.contains("/v3")){
                            String desc = generateDescription(method.name(), path);
                            Permission p = new Permission();
                            p.setEndpoint("/products"+path);
                            p.setMethod(method.name());
                            p.setDescription(desc);
                            p.setCreateUser("System");
                            p.setUpdateUser("System");
                            LocalDateTime now = LocalDateTime.now();
                            String formattedCurrentDateTime = now.format(formatter);
                            p.setCreateDatetime(formattedCurrentDateTime);
                            p.setUpdateDatetime(formattedCurrentDateTime);
                            userFeignClient.createPermission(p);
                        }
                    }
                }
            }
        }

        // Filter and paginate
        List<String> filtered = allEndpoints.stream()
                .filter(endpoint -> endpoint.toLowerCase().contains(name.toLowerCase()))
                .sorted()
                .collect(Collectors.toList());

        int start = page * size;
        int end = Math.min(start + size, filtered.size());
        List<String> paged = (start < end) ? filtered.subList(start, end) : Collections.emptyList();

        Map<String, Object> response = new HashMap<>();
        response.put("content", paged);
        response.put("totalElements", filtered.size());
        response.put("totalPages", (int) Math.ceil((double) filtered.size() / size));
        response.put("page", page);

        return ResponseEntity.ok(Collections.singletonMap("data", response));
    }

    public String generateDescription(String method, String endpoint) {
        // Step 1: Extract resource
        String resource = endpoint.replaceFirst("^/api/", "");
        resource = resource.replaceAll("\\{[^}]+\\}", ""); // remove {id}, etc.
        resource = resource.replaceAll("//+", "/"); // clean double slashes
        resource = resource.replaceAll("^/|/$", ""); // remove leading/trailing slash
        String[] parts = resource.split("/");

        String resourceName = parts.length > 0 ? parts[0] : "resource";
        resourceName = resourceName.replace("-", " "); // optional formatting

        // Step 2: Map HTTP method to action
        String action;
        switch (method.toUpperCase()) {
            case "GET":
                action = endpoint.contains("{") ? "Get" : "Get list of";
                break;
            case "POST":
                action = "Save";
                break;
            case "PUT":
                action = "Update";
                break;
            case "DELETE":
                action = "Delete";
                break;
            default:
                action = "Call";
        }

        // Step 3: Assemble
        return action + " " + resourceName;
    }*/

}
