package com.cnby.qw.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QwNotification {
    /**
     * 通知人
     * 指定所有人为"@all"
     *
     * @return 通知成员
     */
    String[] notifier() default {};

    boolean noticeBefore() default false;

    /**
     * <h2>运行完成通知</h2>
     * <p>注意:<ul>
     * <li>不管成功与否</li>
     * <li>与异常通知互斥，优先异常通知（原因：1、完成通知是在finally内 2、redis做过快通知检测）</li>
     * </ul></p>
     * <p>默认关闭此配置，不建议太频繁的通知，机器人只能一分钟通知20次超限会丢失消息</p>
     *
     * @return 是否启用
     */
    boolean noticeRunning() default false;

    /**
     * <h2>异常通知</h2>
     * <p>注意：<ul>
     * <li>不要在内抛异常!不要在内抛异常!不要在内抛异常!</li>
     * </ul>
     * </p>
     *
     * @return 是否启用
     */
    boolean noticeException() default true;

//    /**
//     * 自定义通知信息
//     *
//     * @return
//     */
//    String customMsg() default "";

//    /**
//     * 自定义通知群机器人key
//     *
//     * @return
//     */
//    String notificationGroup() default "";
}
