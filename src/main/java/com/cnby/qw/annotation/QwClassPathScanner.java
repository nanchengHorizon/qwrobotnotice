package com.cnby.qw.annotation;

import com.cnby.qw.template.NoticeTemplate;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.Objects;
import java.util.Set;

public class QwClassPathScanner extends ClassPathBeanDefinitionScanner {

    private Class<?> templateClass;

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        return super.doScan(basePackages);
    }

    public void registerFilter() {
        if (Objects.nonNull(templateClass)) {
            this.addIncludeFilter(new AssignableTypeFilter(templateClass));
        }
    }

    public void setTemplateClass(Class<?> templateClass) {
        this.templateClass = templateClass;
    }

    public QwClassPathScanner(BeanDefinitionRegistry registry) {
        super(registry);
        templateClass = NoticeTemplate.class;
    }

}
