package com.fmx.xiaomeng.modules.application.service.model;

import lombok.Data;

@Data
public class AdModel {
    private AdContentModel content;
    private AdNavigateModel adNavigate;

    public AdContentModel getContent() { return content; }
    public void setContent(AdContentModel content) { this.content = content; }
    public AdNavigateModel getAdNavigate() { return adNavigate; }
    public void setAdNavigate(AdNavigateModel adNavigate) { this.adNavigate = adNavigate; }
}
