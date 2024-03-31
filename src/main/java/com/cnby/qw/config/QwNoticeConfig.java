package com.cnby.qw.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QwNoticeConfig {
    @Value("${qw.notice.robotKey:}")
    private String robotKey;
    @Value("${qw.notice.env:dev}")
    private String env;
    @Value("${qw.notice.traceIdName:traceId}")
    public static String traceIdName;

    public String getRobotKey() {
        return robotKey;
    }

    public String getEnv() {
        return env;
    }
}
