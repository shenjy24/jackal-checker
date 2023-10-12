package com.jonas.checker.service.text;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shenjy
 * @createTime 2023/10/12 22:47
 * @description TextCheckServiceFactory
 */
@Component
public class TextCheckServiceFactory {

    @Value("${text.service}")
    private String textCheckService;

    @Resource
    private final Map<String, TextCheckService> map = new ConcurrentHashMap<>();

    public TextCheckService getService() {
        if (!map.containsKey(textCheckService)) {
            throw new RuntimeException("找不到对应的服务：" + textCheckService);
        }
        return map.get(textCheckService);
    }

    public TextCheckService getService(String service) {
        if (!map.containsKey(service)) {
            throw new RuntimeException("找不到对应的服务：" + service);
        }
        return map.get(service);
    }
}
