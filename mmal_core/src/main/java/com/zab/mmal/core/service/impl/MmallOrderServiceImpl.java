package com.zab.mmal.core.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zab.mmal.api.entity.MmallOrder;
import com.zab.mmal.api.service.IMmallOrderService;
import com.zab.mmal.core.mapper.MmallOrderMapper;
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
public class MmallOrderServiceImpl extends ServiceImpl<MmallOrderMapper, MmallOrder> implements IMmallOrderService {

}
