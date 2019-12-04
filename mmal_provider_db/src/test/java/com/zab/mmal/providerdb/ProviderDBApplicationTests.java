package com.zab.mmal.providerdb;

import com.zab.mmal.api.service.IMmallProductService;
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
    private IMmallProductService productService;

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
        productService.getProductDetail(26);

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }
}
