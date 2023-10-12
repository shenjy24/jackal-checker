package com.jonas.checker.component;

import com.jonas.checker.service.text.TextCheckService;
import com.jonas.checker.service.text.TextCheckServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author shenjy
 * @createTime 2023/10/12 23:55
 * @description TextChecker
 */
@Component
@RequiredArgsConstructor
public class TextChecker {

    private final TextCheckServiceFactory factory;

    public String check(String text) {
        TextCheckService service = factory.getService();
        return service.check(text);
    }
}
