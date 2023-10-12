package com.jonas.checker.controller;

import com.jonas.checker.component.TextChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shenjy
 * @createTime 2023/10/12 23:56
 * @description CheckController
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/check")
public class CheckController {
    private final TextChecker textChecker;

    /**
     * 文本校验
     *
     * @param text 原文
     * @return 译文
     */
    @PostMapping("/text")
    public String textCheck(String text) {
        return textChecker.check(text);
    }
}
