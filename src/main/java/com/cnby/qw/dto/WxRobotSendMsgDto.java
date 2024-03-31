package com.cnby.qw.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WxRobotSendMsgDto {
    private String msgtype;
    private WxSendContentDto markdown;
    private List<String> mentioned_list;

    public void build(WxSendMarkdownContentDto dto) {
        this.setMsgtype(dto.getType());
        this.setMarkdown(WxSendContentDto.builder().content(dto.getContent()).build());
    }
}
