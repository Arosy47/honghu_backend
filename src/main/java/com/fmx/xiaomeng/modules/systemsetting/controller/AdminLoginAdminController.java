
package com.fmx.xiaomeng.modules.systemsetting.controller;


import com.fmx.xiaomeng.common.response.Result;
import com.fmx.xiaomeng.modules.systemsetting.entity.AdminUserModel;
import com.fmx.xiaomeng.modules.systemsetting.entity.AdminUserTokenModel;
import com.fmx.xiaomeng.modules.systemsetting.form.SysLoginForm;
import com.fmx.xiaomeng.modules.systemsetting.service.AdminUserService;
import com.fmx.xiaomeng.modules.systemsetting.service.AdminUserTokenService;
import com.fmx.xiaomeng.modules.systemsetting.service.SysCaptchaService;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;


/**
 * @Description 后台管理员登录模块
 * @Date 2025-12-20
 * @Author honghu
 **/
@RestController
//@RequestMapping("/app/user")
public class AdminLoginAdminController extends AbstractAdminController {
	@Autowired
	private AdminUserService adminUserService;
	@Autowired
	private AdminUserTokenService adminUserTokenService;
	@Autowired
	private SysCaptchaService sysCaptchaService;


////	@GetMapping("/home")
////	@ResponseBody
//	@GetMapping({"/","home","index.html"})
//	public String home() {
//		return "index";
////		return "<!doctype html>\n" +
////				"<html>\n" +
////				"<head>\n" +
////				"    <meta charset=\"utf-8\">\n" +
////				"    <title>网站无法访问！</title>\n" +
////				"    <style>\n" +
////				"        .container {\n" +
////				"            width: 60%;\n" +
////				"            margin: 10% auto 0;\n" +
////				"            background-color: #f0f0f0;\n" +
////				"            padding: 2% 5%;\n" +
////				"            border-radius: 10px\n" +
////				"        }\n" +
////				"\n" +
////				"        ul {\n" +
////				"            padding-left: 20px;\n" +
////				"        }\n" +
////				"\n" +
////				"            ul li {\n" +
////				"                line-height: 2.3\n" +
////				"            }\n" +
////				"\n" +
////				"        a {\n" +
////				"            color: #20a53a\n" +
////				"        }\n" +
////				"    </style>\n" +
////				"</head>\n" +
////				"<body>\n" +
////				"    <div class=\"container\">\n" +
////				"        <h1>网站无法访问！</h1>\n" +
////				"    </div>\n" +
////				"<iframe frameborder=\"0\" scrolling=\"no\" style=\"width: 100%;height:1080px;\"></iframe>\n" +
////				"<center style=\"padding: 0;margin:0;list-style: none\">\n" +
////				"    <br><a href=\"https://beian.miit.gov.cn/\" target=\"_blank\">鲁ICP备2024056421号-1</a>\n" +
////				"</center>\n" +
////				"</body>\n" +
////				"</html>";
//
//
//
////				"\n" +
////				"<!DOCTYPE html>\n" +
////				"<html lang=\"en\">\n" +
////				"<head>\n" +
////				"    <meta charset=\"UTF-8\">\n" +
////				"    <title>我的网站</title>\n" +
////				"</head>\n" +
////				"<body>\n" +
////				"\n" +
////				"<div class=\"foot_bot\">\n" +
////				"    <font><a style=\"color:#4f4f4f;\"></a>无法访问</font>\n" +
////				"</div>\n" +
////				"\n" +
////				"\n" +
////				"<iframe frameborder=\"0\" scrolling=\"no\" style=\"width: 100%;height:1080px;\"></iframe>\n" +
////				"<center style=\"padding: 0;margin:0;list-style: none\">\n" +
////				"    <br><a href=\"https://beian.miit.gov.cn/\" target=\"_blank\">鲁ICP备2024056421号-1</a>\n" +
////				"</center>\n" +
////				"\n" +
////				"\n" +
////				"</body>\n" +
////				"\n" +
////				"\n" +
////				"</html>\n";
//
//	}

	@GetMapping({"/","home","index.html"})
	public Resource home() {
//		return "index";
		return new ClassPathResource("static/index.html");

	}

	/**
	 * 验证码
	 * @param response
	 * @param uuid
	 * @throws IOException
	 */
	@GetMapping("captcha.jpg")
	public void captcha(HttpServletResponse response, String uuid)throws IOException {
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");
		ServletOutputStream out = null;
		try {
			//获取图片验证码
			BufferedImage image = sysCaptchaService.getCaptcha(uuid);

			out = response.getOutputStream();
			ImageIO.write(image, "jpg", out);
		}catch (Exception e){
			throw e;
		}finally{
			IOUtils.closeQuietly(out);
		}
	}


	/**
	 * 登录
	 * @param form
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/sys/login")
	public Result login(@RequestBody SysLoginForm form)throws IOException {
		boolean captcha = sysCaptchaService.validate(form.getUuid(), form.getCaptcha());
		if(!captcha){
			return Result.error("验证码不正确");
		}

		//用户信息
		AdminUserModel user = adminUserService.queryByUserName(form.getUsername());

		//账号不存在、密码错误
		if(user == null || !user.getPassword().equals(new Sha256Hash(form.getPassword(), user.getSalt()).toHex())) {
			return Result.error("账号或密码不正确");
		}

		//账号锁定
		if(user.getStatus() == 0){
			return Result.error("账号已被锁定,请联系管理员");
		}
		//生成token，并保存到数据库
        AdminUserTokenModel token = adminUserTokenService.createToken(user.getUserId());
        return Result.ok(token);
	}


	/**
	 * 退出
	 * @return
	 */
	@GetMapping("/sys/logout")
	public Result logout() {
		adminUserTokenService.logout(getUserId());
		return Result.ok();
	}

}
