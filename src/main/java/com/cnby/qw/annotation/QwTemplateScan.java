package com.cnby.qw.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(QwTemplateScannerRegistrar.class)
public @interface QwTemplateScan {
    String[] basePackages() default {};
}
