package com.cnby.qw.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WxSendMsgDto {
    private String touser;
    private String msgtype;
    private String agentid;
    private WxSendContentDto markdown;
}
