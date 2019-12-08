package com.zab.mmal.providerdb.controller;


import com.zab.mmal.api.entity.MmallPayInfo;
import com.zab.mmal.api.service.IMmallPayInfoService;
import com.zab.mmal.common.commons.ReturnData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zab
 * @since 2019-11-19
 */
@RestController
@RequestMapping("/provider/payinfo")
public class MmallPayInfoController {

    @Autowired
    private IMmallPayInfoService payInfoService;

    @PostMapping("addPayInfo")
    public ReturnData addPayInfo(@RequestBody MmallPayInfo payInfo) {
        return new ReturnData(payInfoService.save(payInfo));
    }

}
