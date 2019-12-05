package com.zab.mmal.providerdb;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.zab.mmal.api.entity.MmallCart;
import com.zab.mmal.api.entity.MmallShipping;
import com.zab.mmal.api.service.IMmallCartService;
import com.zab.mmal.api.service.IMmallProductService;
import com.zab.mmal.api.service.IMmallShippingService;
import com.zab.mmal.common.utils.Base64Util;
import com.zab.mmal.common.utils.MD5Utils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProviderDBApplicationTests implements ApplicationContextAware {

    @Resource
    private IMmallShippingService mmallShippingService;

    public static void main(String[] args) {
        try {
            System.err.println(MD5Utils.MD5EncodeUtf8("zab_123"));
            System.err.println(Base64Util.strEncode("zab_123"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void contextLoads() {
        MmallShipping mmallShipping = new MmallShipping();
        mmallShipping.setReceiverName("吉利");
        System.err.println(JSON.toJSONString(mmallShippingService.addShipping(mmallShipping)));

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }
}
