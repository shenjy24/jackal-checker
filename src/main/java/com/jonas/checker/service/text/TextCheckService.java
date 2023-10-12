package com.jonas.checker.service.text;

/**
 * @author shenjy
 * @createTime 2023/10/12 22:41
 * @description 文本内容校验
 */
public interface TextCheckService {

    /**
     * 文本内容校验
     *
     * @param text 文本
     * @return
     */
    String check(String text);
}
