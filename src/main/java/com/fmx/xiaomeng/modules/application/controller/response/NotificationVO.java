package com.fmx.xiaomeng.modules.application.controller.response;

import com.fmx.xiaomeng.modules.application.service.model.*;
import lombok.Data;

@Data
public class NotificationVO {

    private Long id;

    private Long ArticleId;



    //被回复或者点赞的评论id，
    private Long commentId;
    /**
     * 触发通知的用户id
     */
    private Long userId;

    /**
     * 触发通知的用户昵称
     */
    private String userNickName;

    /**
     * 头像
     */
    private String avatar;

    // 通知对象ID
    private Long targetUserId;

    // 类型: MESSAGE,REPLY, COMMENT, COLLECT, TOPIC_UP, COMMENT_UP
    /**
     * @see com.fmx.xiaomeng.common.enums.NotificationTypeEnum
     */
    private Integer noticeType;

    private String createTime;

    /**
     * 是否已读
     */
    private Boolean alreadyRead;

    /**
     * 是否已删除
     */
    private Boolean hasDelete;


    //这个内容是指：**点赞了你的文章，**评论了你 ×
    //这里面保存回复，评论的内容，点赞不保存内容，但保存记录
    //前端根据type来提示：***点赞了你的评论/文章   ***评论了你  ***私信了你
// TODO: 2023/2/18 再确认下
    //这个内容是评论或者回复的内容
    private String content;

    //具体的点赞人信息（名字），评论内容等,聚合在这里，DO模型只有上面的字段
    private ArticleModel articleModel;
    private ArticleCommentModel articleCommentModel;


    private UserModel userModel;

    /**
     * 该消息的发出者是不是所属主题的创建者，比如帖子的作者或者局长
     */
    private Boolean isOwner;
}
