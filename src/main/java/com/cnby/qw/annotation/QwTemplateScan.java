package com.cnby.qw.annotation;

import com.cnby.qw.NoticeTemplateFactory;
import com.cnby.qw.config.QwNoticeConfig;
import com.cnby.qw.template.NoticeTemplate;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(QwTemplateScannerRegistrar.class)
public @interface QwTemplateScan {
    Class<? extends NoticeTemplate> templateClass() default NoticeTemplate.class;

    Class<? extends NoticeTemplateFactory> factoryClass() default NoticeTemplateFactory.class;

    Class<? extends QwNoticeConfig> configClass() default QwNoticeConfig.class;

    String[] basePackages() default {};
}
