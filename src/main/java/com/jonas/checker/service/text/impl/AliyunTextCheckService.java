package com.jonas.checker.service.text.impl;

import com.aliyun.green20220302.Client;
import com.aliyun.green20220302.models.TextModerationRequest;
import com.aliyun.green20220302.models.TextModerationResponse;
import com.aliyun.green20220302.models.TextModerationResponseBody;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.jonas.checker.common.AliyunTextCheckType;
import com.jonas.checker.common.CheckerType;
import com.jonas.checker.service.text.TextCheckService;
import com.jonas.checker.util.GsonUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shenjy
 * @createTime 2023/10/13 0:31
 * @description AliyunTextCheckService
 */
@Slf4j
@Service(CheckerType.ALIYUN)
public class AliyunTextCheckService implements TextCheckService {

    @Value("${text.aliyun.key}")
    private String key;
    @Value("${text.aliyun.secret}")
    private String secret;
    @Value("${text.aliyun.region}")
    private String region;
    @Value("${text.aliyun.endpoint}")
    private String endpoint;

    private Config config;
    private Client client;

    @PostConstruct
    public void initClient() throws Exception {
        config = new Config();
        config.setAccessKeyId(key);
        config.setAccessKeySecret(secret);
        config.setRegionId(region);
        config.setEndpoint(endpoint);
        //连接时超时时间，单位毫秒（ms）。
        config.setReadTimeout(6000);
        //读取时超时时间，单位毫秒（ms）。
        config.setConnectTimeout(3000);
        // 注意，此处实例化的client请尽可能重复使用，避免重复建立连接，提升检测性能
        client = new Client(config);
    }

    @Override
    public String check(String text) {
        Map<String, Object> json = new HashMap<>();
        json.put("content", text);
        TextModerationRequest request = new TextModerationRequest();
        request.setService(AliyunTextCheckType.COMMENT_DETECTION);
        request.setServiceParameters(GsonUtil.toJson(json));
        try {
            // 创建RuntimeObject实例并设置运行参数。
            RuntimeOptions runtime = new RuntimeOptions();
            runtime.readTimeout = 10000;
            runtime.connectTimeout = 10000;
            // 调用方法获取检测结果。
            TextModerationResponse response = client.textModerationWithOptions(request, runtime);

            // 自动路由。
            if (response != null) {
                // 服务端错误，区域切换到cn-beijing。
                if (500 == response.getStatusCode() || (response.getBody() != null && 500 == (response.getBody().getCode()))) {
                    // 接入区域和地址请根据实际情况修改。
                    config.setRegionId("cn-beijing");
                    config.setEndpoint("green-cip.cn-beijing.aliyuncs.com");
                    client = new Client(config);
                    response = client.textModerationWithOptions(request, runtime);
                }
            }
            // 打印检测结果。
            if (response != null) {
                if (response.getStatusCode() == 200) {
                    TextModerationResponseBody result = response.getBody();
                    log.info(GsonUtil.toJson(result));
                    Integer code = result.getCode();
                    if (code != null && code == 200) {
                        TextModerationResponseBody.TextModerationResponseBodyData data = result.getData();
                        log.info("labels = [" + data.getLabels() + "]");
                        log.info("reason = [" + data.getReason() + "]");
                    } else {
                        log.info("text moderation not success. code:" + code);
                    }
                } else {
                    log.info("response not success. status:" + response.getStatusCode());
                }
            }
            return GsonUtil.toJson(response);
        } catch (Exception e) {
            log.error("阿里云文本内容检测异常", e);
        }
        return "";
    }
}
