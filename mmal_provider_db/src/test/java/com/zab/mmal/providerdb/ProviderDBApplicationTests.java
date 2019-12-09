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
import org.apache.commons.lang.StringUtils;
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
            String s = "/service/order/alipayCallback";
            String path = "/service/order/alipayCallback&version=1.0&app_id=2016101500695336&sign_type=RSA2&timestamp=2019-12-09";
            System.err.println(StringUtils.contains(path, s));

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
        System.err.println(JSON.toJSONString(mmallShippingService.pageShipping(17, 10, 1, mmallShipping)));

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }
}
