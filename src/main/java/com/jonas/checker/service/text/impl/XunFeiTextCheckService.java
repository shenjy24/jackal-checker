package com.jonas.checker.service.text.impl;

import com.jonas.checker.common.CheckerType;
import com.jonas.checker.service.text.TextCheckService;
import com.jonas.checker.util.GsonUtil;
import com.jonas.checker.util.XunFeiUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shenjy
 * @createTime 2023/10/12 22:52
 * @description XunFeiTextCheckService
 */
@Slf4j
@Service(CheckerType.XUNFEI)
public class XunFeiTextCheckService implements TextCheckService {

    @Value("${text.xunfei.appId}")
    private String appId;
    @Value("${text.xunfei.apiSecret}")
    private String apiSecret;
    @Value("${text.xunfei.apiKey}")
    private String apiKey;
    @Value("${text.xunfei.apiUrl}")
    private String apiUrl;

    @Override
    public String check(String text) {
        String returnResult = "";
        try {
            // 获取参数
            Map<String, Object> json = new HashMap<>();
            json.put("content", text);
            json.put("is_match_all", 0); // 默认取值0，匹配到敏感词则不再匹配，不会返回所有敏感分类。
            // 获取鉴权
            Map<String, String> urlParams = XunFeiUtil.getAuth(appId, apiKey, apiSecret);
            // 发起请求
            returnResult = XunFeiUtil.doPostJson(apiUrl, urlParams, GsonUtil.toJson(json));
        } catch (Exception e) {
            log.error("讯飞文本校验异常, text={}, response={}", text, returnResult);
        }
        return returnResult;
    }
}
