package com.fmx.xiaomeng.modules.admin.repository.dao;
import com.fmx.xiaomeng.modules.admin.repository.model.AdRuleDO;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface AdRuleDOMapper {
    int deleteByPrimaryKey(Long id);
    int insert(AdRuleDO record);
    int insertSelective(AdRuleDO record);
    AdRuleDO selectByPrimaryKey(Long id);
    int updateByPrimaryKeySelective(AdRuleDO record);
    int updateByPrimaryKey(AdRuleDO record);
}