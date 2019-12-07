package com.zab.mmal.protal.controller;

import com.zab.mmal.api.entity.MmallOrder;
import com.zab.mmal.api.entity.MmallUser;
import com.zab.mmal.common.commons.ReturnData;
import com.zab.mmal.common.config.SessionAttribute;
import com.zab.mmal.common.enums.SysCodeMsg;
import com.zab.mmal.common.utils.JudgeUtil;
import com.zab.mmal.protal.dopay.DoPayClient;
import com.zab.mmal.protal.fegin.ProtalFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/service/order")
public class POrderController {

    @Autowired
    private ProtalFeignService protalFeignService;
    @Autowired
    private DoPayClient doPayClient;

    @GetMapping("pay/{orderNo}")
    public ReturnData pay(HttpServletRequest request, @PathVariable Long orderNo) {
        MmallUser currentUser = SessionAttribute.currentUser(request.getSession());
        ReturnData returnData = protalFeignService.pay(currentUser.getId(), orderNo);
        if (JudgeUtil.isDBEq(returnData.getCode(), SysCodeMsg.SUCCESS.getCode())) {
            return new ReturnData(SysCodeMsg.FAIL.getCode(), "系统出错，支付失败");
        }

        MmallOrder order = (MmallOrder) returnData.getData();
        if (null == order) {
            return new ReturnData(SysCodeMsg.FAIL.getCode(), "没有查询到该订单:" + orderNo);
        }

        String path = request.getServletContext().getRealPath("upload");
        Map<String, Object> result = doPayClient.trade_precreate(path, order);
        return new ReturnData(result);
    }

}