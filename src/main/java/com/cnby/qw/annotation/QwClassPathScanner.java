package com.cnby.qw.annotation;

import cn.hutool.core.util.ArrayUtil;
import com.cnby.qw.aop.QwDevGroupNotificationAspect;
import com.cnby.qw.template.NoticeTemplate;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.Objects;
import java.util.Set;

public class QwClassPathScanner extends ClassPathBeanDefinitionScanner {

    private Class<?> templateClass;
    private Class<?> configClass;
    private Class<?> factoryClass;
    private final Class<?> aopClass = QwDevGroupNotificationAspect.class;
    private final String defaultPack = "com.cnby.qw";

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        if (ArrayUtil.isEmpty(basePackages)) {
            return super.doScan(ArrayUtil.append(basePackages, defaultPack));
        }
        return super.doScan(basePackages);
    }

    public void registerFilter() {
        if (Objects.nonNull(templateClass)) {
            this.addIncludeFilter(new AssignableTypeFilter(templateClass));
        }

        if (Objects.nonNull(configClass)) {
            this.addIncludeFilter(new AssignableTypeFilter(configClass));
        }

        if (Objects.nonNull(factoryClass)) {
            this.addIncludeFilter(new AssignableTypeFilter(factoryClass));
        }

        this.addIncludeFilter(new AssignableTypeFilter(aopClass));
    }

    public void setConfigClass(Class<?> configClass) {
        this.configClass = configClass;
    }

    public void setFactoryClass(Class<?> factoryClass) {
        this.factoryClass = factoryClass;
    }

    public void setTemplateClass(Class<?> templateClass) {
        this.templateClass = templateClass;
    }

    public QwClassPathScanner(BeanDefinitionRegistry registry) {
        super(registry);
        templateClass = NoticeTemplate.class;
    }

}
