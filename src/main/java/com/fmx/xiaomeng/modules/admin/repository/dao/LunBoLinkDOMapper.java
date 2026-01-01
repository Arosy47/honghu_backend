package com.fmx.xiaomeng.modules.admin.repository.dao;
import com.fmx.xiaomeng.modules.admin.repository.model.LunBoLinkDO;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface LunBoLinkDOMapper {
    int deleteByPrimaryKey(Long id);
    int insert(LunBoLinkDO record);
    int insertSelective(LunBoLinkDO record);
    LunBoLinkDO selectByPrimaryKey(Long id);
    int updateByPrimaryKeySelective(LunBoLinkDO record);
    int updateByPrimaryKey(LunBoLinkDO record);
}