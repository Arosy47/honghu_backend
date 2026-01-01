package com.fmx.xiaomeng.modules.application.service.impl;

import com.fmx.xiaomeng.modules.application.repository.dao.OnlineTimeStatisticDOMapper;
import com.fmx.xiaomeng.modules.application.repository.model.OnlineTimeStatisticDO;
import com.fmx.xiaomeng.modules.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/

@Service
public class OnlineTimeStatisticServiceImpl {

    @Autowired
    private OnlineTimeStatisticDOMapper dao;

    @Autowired
    private UserService userService;

    public void recordOnlineTime(Long userId, String dateKey, Integer onlineDuration) {
        log.info("##记录在线时长:  " + dateKey + "---" + onlineDuration);

        OnlineTimeStatisticDO exist = dao.select(userId, dateKey);
        if (Objects.nonNull(exist)) {
            return;
        }

        OnlineTimeStatisticDO onlineTimeStatisticDO = new OnlineTimeStatisticDO();
        onlineTimeStatisticDO.setUserId(userId);
        onlineTimeStatisticDO.setDateKey(dateKey);
        onlineTimeStatisticDO.setOnlineDuration(onlineDuration);
        dao.insertSelective(onlineTimeStatisticDO);

        int durationMinutes = onlineDuration / (60 * 1000);
        int score = Math.min(durationMinutes, 50);
        // userService.addScore(userId, score, ScoreActionEnum.ONLINE_TIME);
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OnlineTimeStatisticServiceImpl.class);
}
