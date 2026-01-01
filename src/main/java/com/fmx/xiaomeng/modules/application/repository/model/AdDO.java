package com.fmx.xiaomeng.modules.application.repository.model;
import lombok.Data;
import java.util.Date;
@Data
public class AdDO {
    private Long id;
    private Date createTime;
    private Date updateTime;
    private String content;
    private String adNavigate;

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getAdNavigate() { return adNavigate; }
    public void setAdNavigate(String adNavigate) { this.adNavigate = adNavigate; }
}