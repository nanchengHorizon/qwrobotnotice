package com.cnby.qw.template;

import com.cnby.qw.dto.WxRobotSendMsgDto;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
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
    String getRunningMsg(Method method, Object result);

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
    default StringBuilder getExceptionMsg(StringBuilder builder, Throwable ex) {
        if (Objects.isNull(ex.getStackTrace())) {
            return builder;
        }

        builder.append("\n")
                .append("## 异常信息：").append(ex.getMessage()).append("\n");
        for (StackTraceElement stackTraceElement : ex.getStackTrace()) {
            String msg = "> " + stackTraceElement.toString() + "\n";
            if (builder.toString().getBytes(StandardCharsets.UTF_8).length + msg.getBytes(StandardCharsets.UTF_8).length > 2048) {
                break;
            }
            builder.append(msg);
        }

        return builder;
    }
}
