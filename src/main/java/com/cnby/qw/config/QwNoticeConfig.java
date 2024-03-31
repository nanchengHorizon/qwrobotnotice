package com.cnby.qw.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Slf4j
@Getter
@Configuration
public class QwNoticeConfig {
    @Value("${qw.notice.robotKey:}")
    private String robotKey;
    @Value("${qw.notice.env:dev}")
    private String env;
    @Value("${qw.notice.traceIdName:traceId}")
    private String traceIdName;

    @PostConstruct
    private void printMsg() {
        log.info("初始化企微配置完成\nrobotKey={}\nenv={}\ntraceIdName={}", robotKey, env, traceIdName);
    }
}
