package com.zab.mmal.core.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zab.mmal.api.entity.MmallCart;
import com.zab.mmal.api.service.IMmallCartService;
import com.zab.mmal.core.mapper.MmallCartMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zab
 * @since 2019-11-19
 */
@Service
public class MmallCartServiceImpl extends ServiceImpl<MmallCartMapper, MmallCart> implements IMmallCartService {

}
