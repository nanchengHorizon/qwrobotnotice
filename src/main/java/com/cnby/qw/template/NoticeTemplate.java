package com.cnby.qw.template;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 对于注解的自定义拓展模板
 *
 * @param <T> 注解class
 * @author Horizon
 * @date 2024/3/28 10:49
 */
public interface NoticeTemplate<T extends Annotation> {
    /**
     * 注解class
     *
     * @return
     */
    Class<T> getType();

    String getBeforeMsgHeader();

    String getRunningMsgHeader();

    String getExceptionMsgHeader();

    /**
     * 前置通知信息
     *
     * @param method
     * @return
     */
    String getBeforeMsg(Method method);

    /**
     * 运行完成通知信息
     *
     * @param method
     * @return
     */
    String getRunningMsg(Method method);

    /**
     * 运行异常通知信息
     *
     * @param method
     * @return
     */
    String getExceptionMsg(Method method, Throwable ex);

    /**
     * 默认的异常信息展示
     *
     * @param ex
     * @return
     */
    default String getExceptionMsg(Throwable ex) {
        if (Objects.nonNull(ex.getStackTrace())) {
            return "## 异常信息：\n" + Arrays.stream(ex.getStackTrace())
                    .map(stackTraceElement -> "> " + stackTraceElement.toString() + "\n")
                    .collect(Collectors.joining());
        }
        return "";
    }
}
