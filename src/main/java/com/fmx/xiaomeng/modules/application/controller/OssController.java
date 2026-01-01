package com.fmx.xiaomeng.modules.application.controller;

import com.fmx.xiaomeng.common.response.Result;
import com.fmx.xiaomeng.modules.application.controller.response.CosCredentialModel;
import com.fmx.xiaomeng.modules.application.controller.response.CosPolicyModel;
// import com.qcloud.cos.auth.COSSigner;
import com.tencent.cloud.CosStsClient;
import com.tencent.cloud.Response;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TreeMap;

/**
 * @Description 腾讯cos临时认证模块
 * @Date 2025-12-20
 * @Author honghu
 **/
@RestController
@RequestMapping("/app/cos")
public class OssController {

    @Value("${cos.secretId}")
    private String SECRET_ID;

    @Value("${cos.secretKey}")
    private String SECRET_KEY;

    @Value("${cos.budgetName}")
    private String BUDGET_NAME;


    /**
     * 获取cos临时认证
     * type: article/organization/chat....
     *
     * @return
     */
    @GetMapping("/getCredential")
    public Result<CosCredentialModel> getCredential() {
        TreeMap<String, Object> config = new TreeMap<String, Object>();

        try {
            config.put("secretId", SECRET_ID);
            config.put("secretKey", SECRET_KEY);
            config.put("durationSeconds", 7200);
            config.put("bucket", BUDGET_NAME);
            config.put("region", "ap-shanghai");
            config.put("allowPrefixes", new String[]{
                    "*"
            });

            String[] allowActions = new String[]{
                    // 简单上传
                    "name/cos:PutObject",
                    "name/cos:DeleteObject"
            };
            config.put("allowActions", allowActions);

            Response response = CosStsClient.getCredential(config);
            CosCredentialModel cosCredentialModel = new CosCredentialModel();

            cosCredentialModel.setTmpSecretId(response.credentials.tmpSecretId);
            cosCredentialModel.setTmpSecretKey(response.credentials.tmpSecretKey);
            cosCredentialModel.setSessionToken(response.credentials.sessionToken);

            long startTime = new Date().getTime() / 1000;
            long expiredTime = startTime + 7000;
            cosCredentialModel.setStartTime(startTime);
            cosCredentialModel.setExpiredTime(expiredTime);
            return Result.ok(cosCredentialModel);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("no valid secret !");
        }
    }

    private String generateCosKey(String type, String ext) {
        SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyyMMdd");
        String ymd = ymdFormat.format(new Date());

        Random random = new Random();
        int r = random.nextInt(1000000);
        String formattedRandom = String.format("%06d", r);

        String cosKey = type + "/" + ymd + "/" + ymd + "_" + formattedRandom;
        if (StringUtils.isNotBlank(ext)) {
            cosKey += "." + ext;
        }
        return cosKey;
    }
}
