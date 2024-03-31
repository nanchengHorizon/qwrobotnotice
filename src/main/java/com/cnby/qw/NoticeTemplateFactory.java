package com.cnby.qw;

import com.cnby.qw.template.NoticeTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class NoticeTemplateFactory<T extends Annotation> implements BeanPostProcessor {
    private final Map<Class<T>, NoticeTemplate<T>> strategyMap = new ConcurrentHashMap<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof NoticeTemplate) {
            NoticeTemplate strategy = (NoticeTemplate) bean;
            if (Objects.isNull(strategy.getType())) {
                throw new RuntimeException("错误的注解类型" + beanName);
            }
            strategyMap.put(strategy.getType(), strategy);
            log.info("postProcessAfterInitialization 初始化企微通知模板 strategy={}", strategy.getClass().getName());
        }
        return bean;
    }

    public NoticeTemplate getStrategy(Class<T> tarClass) {
        if (Objects.isNull(tarClass)) {
            return null;
        }
        if (!strategyMap.containsKey(tarClass)) {
            return null;
        }
        return strategyMap.get(tarClass);
    }
}