package com.fmx.xiaomeng.modules.admin.repository.dao;
import com.fmx.xiaomeng.modules.admin.repository.model.AdShowRecordDO;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface AdShowRecordDOMapper {
    int deleteByPrimaryKey(Long id);
    int insert(AdShowRecordDO record);
    int insertSelective(AdShowRecordDO record);
    AdShowRecordDO selectByPrimaryKey(Long id);
    int updateByPrimaryKeySelective(AdShowRecordDO record);
    int updateByPrimaryKey(AdShowRecordDO record);
}