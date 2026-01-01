
package com.fmx.xiaomeng.modules.systemsetting.service.impl;

import com.fmx.xiaomeng.modules.admin.repository.dao.AdminUserTokenDOMapper;
import com.fmx.xiaomeng.modules.admin.repository.model.AdminUserTokenDO;
import com.fmx.xiaomeng.modules.systemsetting.entity.AdminUserTokenModel;
import com.fmx.xiaomeng.modules.systemsetting.entity.SysUserTokenEntity;
import com.fmx.xiaomeng.modules.systemsetting.oauth2.TokenGenerator;
import com.fmx.xiaomeng.modules.systemsetting.service.AdminUserTokenService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * @author honghu
 * @date 2025-12-20
 */
@Service("adminUserTokenService")
public class AdminUserTokenServiceImpl implements AdminUserTokenService {
	//12过期
	private final static int EXPIRE = 3600 * 12;

	@Autowired
	private AdminUserTokenDOMapper adminUserTokenDOMapper;

	@Override
	public AdminUserTokenModel createToken(long userId) {
		//生成一个token
		String token = TokenGenerator.generateValue();

		//当前时间
		Date now = new Date();
		//过期时间
		Date expireTime = new Date(now.getTime() + EXPIRE * 1000);

		//判断是否生成过token

		AdminUserTokenDO adminUserTokenDO = adminUserTokenDOMapper.queryByUserId(userId);
		if(adminUserTokenDO == null){
			adminUserTokenDO = new AdminUserTokenDO();
			adminUserTokenDO.setUserId(userId);
			adminUserTokenDO.setToken(token);
			adminUserTokenDO.setUpdateTime(now);
			adminUserTokenDO.setExpireTime(expireTime);

			//保存token
			adminUserTokenDOMapper.insertSelective(adminUserTokenDO);
		}else{
			adminUserTokenDO.setToken(token);
			adminUserTokenDO.setUpdateTime(now);
			adminUserTokenDO.setExpireTime(expireTime);
			//更新token
			adminUserTokenDOMapper.updateByPrimaryKeySelective(adminUserTokenDO);
		}
		AdminUserTokenModel tokenModel = new AdminUserTokenModel();
		tokenModel.setToken(token);
		tokenModel.setExpire(EXPIRE);

		return tokenModel;
	}

	@Override
	public void logout(long userId) {
		//生成一个token
		String token = TokenGenerator.generateValue();

		//修改token
		SysUserTokenEntity tokenEntity = new SysUserTokenEntity();
		tokenEntity.setUserId(userId);
		tokenEntity.setToken(token);

		AdminUserTokenDO adminUserTokenDO = new AdminUserTokenDO();
		BeanUtils.copyProperties(tokenEntity, adminUserTokenDO);
		adminUserTokenDOMapper.updateByPrimaryKeySelective(adminUserTokenDO);
	}
}
