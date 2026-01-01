package com.fmx.xiaomeng.modules.application.service.model;
import lombok.Data;
@Data
public class OssFileModel {
    private String key;
    private String url;
    private Integer width;
    private Integer height;
    private Long size;
    private String format;
    public OssFileModel() {}
    public OssFileModel(String key, String url) { this.key = key; this.url = url; }
    public void setUrl(String url) { this.url = url; }
    public String getUrl() { return url; }
}