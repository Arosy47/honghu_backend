package com.fmx.xiaomeng.common.log;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

//import com.taobao.eagleeye.EagleEye;

/**
 * 摘要日志
 */
public class DigestLog {

    /**
     * 摘要日志分隔符
     */
    private static final String DIGEST_LOG_SEPARATOR = "|";

    /**
     * 摘要内容分隔符
     */
    protected static final String CONTENT_SEPARATOR = "#";

    /**
     * 摘要日志字段使用空字符串占位
     */
    private static final String EMPTY_DIGEST_FIELD = "";

    ///**
    // * 0.traceId
    // */
    //private String traceId;

    /**
     * 1.场景
     */
    private String scene;

    /**
     * 2.业务身份
     */
    private String bizCode;

    /**
     * 3.日志码
     */
    private String logCode;

    /**
     * 开始时间
     */
    private long start;

    /**
     * 4.响应时间
     */
    private Long rt;

    /**
     * 5.摘要内容
     */
    private List<String> contents;

    private DigestLog() {
    }

    /**
     * 创建日志摘要对象
     *
     * @return
     */
    public static DigestLog create() {
        DigestLog digestLog = new DigestLog();
        //digestLog.traceId = EagleEye.getTraceId();
        digestLog.start = System.currentTimeMillis();
        return digestLog;
    }

    public DigestLog scene(String scene) {
        this.scene = scene;
        return this;
    }

    public DigestLog bizCode(String bizCode) {
        this.bizCode = bizCode;
        return this;
    }

    public DigestLog logCode(String logCode) {
        this.logCode = logCode;
        return this;
    }

    public DigestLog rt(Long rt) {
        this.rt = rt;
        return this;
    }

    public DigestLog addContent(String content) {
        if (Objects.isNull(contents)) {
            contents = new LinkedList<>();
        }
        contents.add(content);
        return this;
    }

    /**
     * 构建日志摘要
     *
     * @return
     */
    public String build() {
        StringJoiner joiner = new StringJoiner(DIGEST_LOG_SEPARATOR);

        //joiner.add(traceId);

        if (null != scene) {
            joiner.add(scene);
        } else {
            joiner.add(EMPTY_DIGEST_FIELD);
        }

        if (null != bizCode) {
            joiner.add(bizCode);
        } else {
            joiner.add(EMPTY_DIGEST_FIELD);
        }

        if (null != logCode) {
            joiner.add(logCode);
        } else {
            joiner.add(EMPTY_DIGEST_FIELD);
        }

        if (null != rt) {
            joiner.add(rt.toString());
        } else {
            joiner.add(String.valueOf(System.currentTimeMillis() - start));
        }

        String content = String.join(CONTENT_SEPARATOR, Optional.ofNullable(contents).orElse(Collections.emptyList()));
        joiner.add(content);

        // 使用｜结尾，兼容新增摘要字段
        return joiner + DIGEST_LOG_SEPARATOR;
    }
}
