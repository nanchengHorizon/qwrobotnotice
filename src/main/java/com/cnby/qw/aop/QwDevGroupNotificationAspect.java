package com.cnby.qw.aop;

import com.cnby.qw.NoticeTemplateFactory;
import com.cnby.qw.QwDevGroupComponent;
import com.cnby.qw.config.QwNoticeConfig;
import com.cnby.qw.dto.WxRobotSendMsgDto;
import com.cnby.qw.dto.WxSendMarkdownContentDto;
import com.cnby.qw.template.NoticeTemplate;
import com.cnby.qw.utils.MemoryCacheUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class QwDevGroupNotificationAspect {
    private final QwDevGroupComponent qwDevGroupComponent;
    private final NoticeTemplateFactory factory;
    private final QwNoticeConfig qwNoticeConfig;

    @Pointcut("@annotation(QwNotification)")
    public void pointCut() {

    }

    private String getTraceId() {
        String traceId = MDC.get(qwNoticeConfig.getTraceIdName());
        if (StringUtils.isBlank(traceId)) {
            traceId = UUID.randomUUID().toString().replace("-", "");
            MDC.put(qwNoticeConfig.getTraceIdName(), traceId);
        }
        return traceId;
    }

    @Around("pointCut()")
    public void over(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        QwNotification qwNotification = method.getDeclaredAnnotation(QwNotification.class);
        WxRobotSendMsgDto msgDto = this.assemblyNoticeInfo(qwNotification);

        // 计算执行时长
        String traceId = getTraceId();
        long l = System.currentTimeMillis();
        MemoryCacheUtil.TIME_CACHE.put(traceId, l);

        // 前置通知
        if (qwNotification.noticeBefore()) {
            this.noticeBefore(method, msgDto);
        }

        try {
            Object proceed = joinPoint.proceed();
        } catch (Throwable ex) {

            // 异常通知
            if (qwNotification.noticeException()) {
                this.noticeException(method, msgDto, ex);
            }

            throw ex;
        } finally {

            // 成功通知
            if (qwNotification.noticeRunning()) {
                this.noticeRunning(method, msgDto);
            }

        }
    }

    private void noticeBefore(Method method, WxRobotSendMsgDto msgDto) {
        StringBuilder msg = new StringBuilder();
        for (Annotation annotation : method.getAnnotations()) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            NoticeTemplate strategy = factory.getStrategy(annotationType);
            if (Objects.nonNull(strategy)) {
                msg.append(strategy.getBeforeMsgHeader());
                msg.append("> 环境：")
                        .append(qwNoticeConfig.getEnv())
                        .append("\n");
                msg.append("> traceId：")
                        .append(getTraceId())
                        .append("\n");
                msg.append(strategy.getBeforeMsg(method));
                break;
            }
        }
        msgDto.build(WxSendMarkdownContentDto.builder()
                .content(msg.toString()).build());
        qwDevGroupComponent.notificationGroup(msgDto);
    }

    private void noticeRunning(Method method, WxRobotSendMsgDto msgDto) {
        String traceId = getTraceId();
        if (MemoryCacheUtil.ERROR_CACHE.contains(traceId)) {
            log.warn("已经推送了异常通知，无需通知完成消息");
            return;
        }
        StringBuilder msg = new StringBuilder();
        for (Annotation annotation : method.getAnnotations()) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            NoticeTemplate strategy = factory.getStrategy(annotationType);
            if (Objects.nonNull(strategy)) {
                msg.append(strategy.getRunningMsgHeader());
                msg.append("> 环境：")
                        .append(qwNoticeConfig.getEnv())
                        .append("\n");
                String spendTime = computingTime();
                if (StringUtils.isNotBlank(spendTime)) {
                    msg.append("> 耗时：")
                            .append(spendTime).append("s")
                            .append("\n");
                }
                msg.append("> traceId：")
                        .append(traceId)
                        .append("\n");
                msg.append(strategy.getRunningMsg(method));
                break;
            }
        }
        msgDto.build(WxSendMarkdownContentDto.builder()
                .content(msg.toString()).build());
        qwDevGroupComponent.notificationGroup(msgDto);
    }

    private void noticeException(Method method, WxRobotSendMsgDto msgDto, Throwable ex) {
        StringBuilder msg = new StringBuilder();
        String traceId = getTraceId();
        String methodName = method.getDeclaringClass().getName() + method.getName();
        Integer count = MemoryCacheUtil.ERROR_TIMES_CACHE.getAntSet(methodName);
        for (Annotation annotation : method.getAnnotations()) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            NoticeTemplate strategy = factory.getStrategy(annotationType);
            if (Objects.nonNull(strategy)) {
                msg.append(strategy.getExceptionMsgHeader());
                msg.append("> 环境：")
                        .append(qwNoticeConfig.getEnv())
                        .append("\n");
                msg.append("> traceId：")
                        .append(traceId)
                        .append("\n");
                msg.append("> 一分钟内错误次数：")
                        .append(count)
                        .append("\n");
                String spendTime = computingTime();

                if (StringUtils.isNotBlank(spendTime)) {
                    msg.append("> 耗时：")
                            .append(spendTime).append("s")
                            .append("\n");
                }
                msg.append(strategy.getExceptionMsg(method, ex));
                break;
            }
        }
        msgDto.build(WxSendMarkdownContentDto.builder()
                .content(msg.toString()).build());
        qwDevGroupComponent.notificationGroup(msgDto);
        MemoryCacheUtil.ERROR_CACHE.put(traceId, traceId);

    }

    private WxRobotSendMsgDto assemblyNoticeInfo(QwNotification qwNotification) {
        WxRobotSendMsgDto msgDto = new WxRobotSendMsgDto();
        // 企微通知注解
        String[] notifier = qwNotification.notifier();
        msgDto.setMentioned_list(Arrays.asList(notifier));
        return msgDto;
    }

    /**
     * 计算耗时
     *
     * @return
     */
    private String computingTime() {
        String traceId = MDC.get(qwNoticeConfig.getTraceIdName());
        if (StringUtils.isBlank(traceId)) {
            return "";
        }

        Object startTime = MemoryCacheUtil.TIME_CACHE.get(traceId);
        if (Objects.isNull(startTime)) {
            return "";
        }

        return new BigDecimal(System.currentTimeMillis() + "")
                .subtract(new BigDecimal(startTime.toString()))
                .divide(new BigDecimal("1000"), 2, RoundingMode.HALF_UP).toString();
    }
}
