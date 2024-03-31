package com.cnby.qw.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class WxSendMarkdownContentDto {
    @Builder.Default
    private String type = "markdown";
    private String content;
}
