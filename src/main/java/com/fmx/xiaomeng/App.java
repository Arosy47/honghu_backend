package com.fmx.xiaomeng;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
//如果碰到这个问题可以看这个链接
//https://blog.csdn.net/weixin_47765791/article/details/118309975
//springBoot在2.0以后RedisTemplate底层默认使用的是Lettuce客户端操作Redis的,
//        而Lettuce是使用netty进行网络通信的，netty如果没有指定堆外内存，
//        默认使用Options中的-Mmx300m,Lettuce的bug会导致内存不够(没有及时的释放内存)，
//        导致堆外内存溢出，就算调大了内存早晚还是会产生该问题



// 有登录功能，和微信的登录不一样，像云朵朵一样，需要校园认证

//好好想想不同学校，同个城市怎么实现信息互通，分库分表是否可以按照两个字段，城市和学校。  所有学校的数据都放到一个数据库里吗，
//问问chatGPT对于论坛系统，文章数据库架构应该怎么设计，怎么分库分表。

//tomcat 配置最大连接数，慕课

//同一个用户token过期时间7天，



//后台管理，如果未登录重定向怎么做到的，看看慕课


//错误码规范，返回给前端的总是未知错误，
// 日志设置，logback.xml，找个方法能方便查看服务器日志，以及服务器数据库（navicat）


//oss配置
//
//todo解决


//接入地理位置
//接入交友模块，

//积分，每日登录，发帖   邀请好友

//数据库加索引

//邀请表(Invitation): 存储邀请信息，例如哪个用户邀请了哪个用户加入论坛。
//
//        积分规则表(Score_Rule): 存储积分的获得、消耗和失效规则，例如发布一篇帖子可以获得10积分，一篇被设为精华的帖子可以额外获得20积分等。
//
//        积分历史表(Score_History): 存储用户的积分历史记录，例如哪个用户在哪个时间点获得了多少积分，或者因为什么原因扣除了多少积分等。
//
//        用户积分表(User_Score): 存储用户的积分信息，例如哪个用户的当前积分是多少，属于哪个积分等级等。


//默认头像！！！！

//用户性别

//同学圈

/**
 * Hello world!
 *
 */
@SpringBootApplication(scanBasePackages = {"com.fmx.xiaomeng"})
@MapperScan("com.fmx.xiaomeng.modules.*.repository.dao")
@EnableScheduling
public class App {

    public static void main( String[] args ) {
        SpringApplication.run(App.class,args);
    }


    // TODO: 2023/2/12 日志规范 

}
