package com.cnby.qw.annotation;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;

@Slf4j
public class QwTemplateScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {
    private ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(QwTemplateScan.class.getName()));
        QwClassPathScanner scanner = new QwClassPathScanner(registry);
        scanner.setResourceLoader(resourceLoader);

        // 加载template模板类
        Class<?> templateClass = annoAttrs.getClass("templateClass");
        scanner.setTemplateClass(templateClass);
        // 加载template工厂类
        Class<?> factoryClass = annoAttrs.getClass("factoryClass");
        scanner.setFactoryClass(factoryClass);
        // 加载配置类
        Class<?> configClass = annoAttrs.getClass("configClass");
        scanner.setConfigClass(configClass);

        scanner.registerFilter();

        String[] basePackages = annoAttrs.getStringArray("basePackages");

        String[] pks = Arrays.stream(basePackages)
                .filter(StringUtils::isNotBlank)
                .toArray(String[]::new);

        scanner.doScan(pks);
    }
}
