package com.fmx.xiaomeng.modules.admin.response;

import lombok.Data;

@Data
public class StatisticResponse {

//    private static final long serialVersionUID=1L;

    private Object totalUser;

    /**
     * 认证用户数
     */
    private Long authedUserCount;

    /**
     * 在线人数
     */
    private Long onlineUserCount;


    private Long totalPost;

    /**
     * 待审核数量
     */
    private Long totalPostOfReview;

    private Long newUserNum;

    private Long yesterdayNewUserNum;


    private Long newArticleNum;

    private Long yesterdayNewArticleNum;





    public void setTotalPostOfReview(long totalPostOfReview) { this.totalPostOfReview = totalPostOfReview; }
    public void setTotalPost(Long totalPost) { this.totalPost = totalPost; }
    public void setNewUserNum(Long newUserNum) { this.newUserNum = newUserNum; }
    public void setYesterdayNewUserNum(Long yesterdayNewUserNum) { this.yesterdayNewUserNum = yesterdayNewUserNum; }
    public void setTotalUser(Long totalUser) { this.totalUser = totalUser; }
    public void setNewArticleNum(Long newArticleNum) { this.newArticleNum = newArticleNum; }
    public void setYesterdayNewArticleNum(Long yesterdayNewArticleNum) { this.yesterdayNewArticleNum = yesterdayNewArticleNum; }
    public void setAuthedUserCount(Long authedUserCount) { this.authedUserCount = authedUserCount; }
    public void setOnlineUserCount(Long onlineUserCount) { this.onlineUserCount = onlineUserCount; }
}
