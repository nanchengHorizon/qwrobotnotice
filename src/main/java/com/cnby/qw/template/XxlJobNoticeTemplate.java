package com.cnby.qw.template;

import com.xxl.job.core.handler.annotation.XxlJob;

import java.lang.reflect.Method;
import java.util.Objects;

public class XxlJobNoticeTemplate implements NoticeTemplate<XxlJob> {

    @Override
    public Class<XxlJob> getType() {
        return XxlJob.class;
    }

    @Override
    public String getBeforeMsgHeader() {
        return "# 定时任务开始执行\n\n";
    }

    @Override
    public String getRunningMsgHeader() {
        return "# 定时任务执行完成\n\n";
    }

    @Override
    public String getExceptionMsgHeader() {
        return "# 定时任务执行异常\n\n";
    }

    @Override
    public String getBeforeMsg(Method method) {
        StringBuilder builder = new StringBuilder();
        // xxlJob 定时任务
        XxlJob xxlJob = method.getDeclaredAnnotation(XxlJob.class);
        String jobName = xxlJob.value();
        builder.append("> 任务名称：")
                .append(jobName)
                .append("\n");

        return builder.toString();
    }

    @Override
    public String getRunningMsg(Method method, Object result) {
        StringBuilder builder = new StringBuilder();
        // xxlJob 定时任务
        XxlJob xxlJob = method.getDeclaredAnnotation(XxlJob.class);
        String jobName = xxlJob.value();
        builder.append("> 任务名称：")
                .append(jobName)
                .append("\n");

        if (Objects.nonNull(result)) {
            builder.append("> 通知信息：\n")
                    .append(result)
                    .append("\n");
        }

        return builder.toString();
    }

    @Override
    public String getExceptionMsg(Method method, Throwable ex) {
        StringBuilder builder = new StringBuilder();
        // xxlJob 定时任务
        XxlJob xxlJob = method.getDeclaredAnnotation(XxlJob.class);
        String jobName = xxlJob.value();
        builder.append("> 任务名称：")
                .append(jobName)
                .append("\n");

        return this.getExceptionMsg(builder,ex).toString();
    }
}