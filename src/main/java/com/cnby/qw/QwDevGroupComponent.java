package com.cnby.qw;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.cnby.qw.config.QwNoticeConfig;
import com.cnby.qw.dto.WxRobotSendMsgDto;
import com.cnby.qw.dto.WxSendContentDto;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class QwDevGroupComponent {

    private final QwNoticeConfig qwNoticeConfig;

    public void notificationGroup(WxRobotSendMsgDto msgDto) {
        notificationGroup(msgDto, qwNoticeConfig.getRobotKey());
    }

    public void notificationGroup(WxRobotSendMsgDto msgDto, String robotKey) {
        if (StringUtils.isBlank(robotKey)) {
            log.error("robotKey is null");
            return;
        }
        try {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=" + robotKey;
            HttpRequest post = HttpUtil.createPost(url);
            post.body(JSONObject.toJSONString(msgDto));
            log.info("notificationGroup url={} body={}", url, JSONObject.toJSON(msgDto));
            HttpResponse execute = post.execute();
            String res = execute.body();
            log.info("notificationGroup 群机器人res={}", res);
        } catch (Exception e) {
            log.error("---------------------发送群机器人消息失败---------------------");
        }
    }

    public String notificationGroupThrow(WxRobotSendMsgDto msgDto, String robotKey) {
        if (StringUtils.isBlank(robotKey)) {
            log.error("robotKey is null");
            return robotKey;
        }
        String res = "";
        try {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=" + robotKey;
            HttpRequest post = HttpUtil.createPost(url);
            post.body(JSONObject.toJSONString(msgDto));
            log.info("notificationGroup url={} body={}", url, JSONObject.toJSON(msgDto));
            HttpResponse execute = post.execute();
            res = execute.body();
            log.info("notificationGroup 群机器人res={}", res);
        } catch (Exception e) {
            log.error("---------------------发送群机器人消息失败---------------------");
        }

        if (StringUtils.isBlank(res)) {
            throw new RuntimeException( "请求企微失败");
        } else if (!JSONObject.parseObject(res).getInteger("errcode").equals(0)) {
            throw new RuntimeException( "发送到企微失败");
        }
        return res;
    }

    public void notificationGroup(List<String> notifyQwUserId, HttpServletRequest requst, Exception e) {
        if ("dev".equals(qwNoticeConfig.getEnv())) {
            log.info("本地调试不通知");
            return;
        }

        if (CollectionUtils.isEmpty(notifyQwUserId)) {
            notifyQwUserId = Lists.newArrayList("chenKaiHorizon");
        }

        StringBuilder builder = new StringBuilder();
        builder.append("# !!警告").append(qwNoticeConfig.getEnv()).append("!!\n\n");
        builder.append("> 请求方式：").append(requst.getMethod()).append("\n");
        builder.append("> 请求地址：").append(requst.getRequestURI()).append("\n");
        builder.append("> traceId：").append(MDC.get(qwNoticeConfig.getTraceIdName())).append("\n");
        builder.append("> 异常信息：").append(e).append("\n");

        WxRobotSendMsgDto msgDto = WxRobotSendMsgDto.builder()
                .msgtype("markdown")
                .mentioned_list(notifyQwUserId)
                .markdown(WxSendContentDto.builder()
                        .content(builder.toString()).build()).build();
        this.notificationGroup(msgDto);
    }
}
