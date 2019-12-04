package com.zab.mmal.core.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zab.mmal.api.entity.MmallUser;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zab
 * @since 2019-11-19
 */
public interface MmallUserMapper extends BaseMapper<MmallUser> {

    String getQuestion(@Param("username") String userName);
}
