package com.zab.mmal.protal.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.zab.mmal.api.entity.MmallOrder;
import com.zab.mmal.api.entity.MmallUser;
import com.zab.mmal.common.commons.ReturnData;
import com.zab.mmal.common.config.SessionAttribute;
import com.zab.mmal.common.enums.OrderSatus;
import com.zab.mmal.common.enums.SysCodeMsg;
import com.zab.mmal.protal.alipay.config.Configs;
import com.zab.mmal.protal.dopay.DoPayFlow;
import com.zab.mmal.protal.fegin.ProtalFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping("/service/order")
@Slf4j
public class POrderController {

    @Autowired
    private ProtalFeignService protalFeignService;
    @Autowired
    private DoPayFlow doPayClient;

    /**
     * 支付所选的订单
     *
     * @param request
     * @param orderNo
     * @return
     */
    @GetMapping("pay/{orderNo}")
    public ReturnData pay(HttpServletRequest request, @PathVariable Long orderNo) {
        MmallUser currentUser = SessionAttribute.currentUser(request.getSession());
        MmallOrder order = protalFeignService.getOrder(currentUser.getId(), orderNo);
        if (null == order) {
            return new ReturnData(SysCodeMsg.FAIL.getCode(), "没有查询到该订单:" + orderNo);
        }

        String path = request.getServletContext().getRealPath("upload");
        Map<String, Object> result = doPayClient.trade_precreate(path, order);
        return new ReturnData(result);
    }

    /**
     * 支付宝回调接口，不需要登录
     *
     * @param request
     * @return
     */
    @RequestMapping("alipayCallback")
    public ReturnData alipayCallback(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> paramsMaP = request.getParameterMap();
        for (Iterator it = paramsMaP.keySet().iterator(); it.hasNext(); ) {
            String key = (String) it.next();
            String[] values = paramsMaP.get(key);
            String valueStr = "";
            for (String value : values) {
                valueStr = values.length == 1 ? valueStr + value : valueStr + value + ",";
            }
            params.put(key, valueStr);
        }

        log.info("支付宝回调,sign:{},trade_status:{},参数:{}", params.get("sign"), params.get("trade_status"), params.toString());

        // 验证回调的正确性，是否是支付宝发的
        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
            if (!alipayRSACheckedV2) {
                return new ReturnData(SysCodeMsg.FAIL.getCode(), "非法请求，验证不通过，如果再次请求，系统即将通知网警");
            }
        } catch (AlipayApiException e) {
            log.error("支付宝验证回调异常", e);
        }

        return doPayClient.callBack(params);
    }

    /**
     * 查看订单支付状态
     *
     * @param request
     * @param orderNo
     * @return
     */
    @GetMapping("getOrderPayStatus/{orderNo}")
    public ReturnData getOrderPayStatus(HttpServletRequest request, @PathVariable Long orderNo) {
        MmallUser currentUser = SessionAttribute.currentUser(request.getSession());
        MmallOrder order = protalFeignService.getOrder(currentUser.getId(), orderNo);

        if (null == order) {
            return new ReturnData(SysCodeMsg.FAIL.getCode(), "没有查询到该订单:" + orderNo);
        }

        if (order.getStatus() >= OrderSatus.PAID.getCode()) {
            return new ReturnData(true);
        }

        return new ReturnData(false);
    }

    /**
     * 创建订单
     *
     * @param request
     * @param shippingId
     * @return
     */
    @PostMapping("creatOrder/{shippingId}")
    public ReturnData creatOrder(HttpServletRequest request, @PathVariable Integer shippingId) {
        MmallUser currentUser = SessionAttribute.currentUser(request.getSession());
        return protalFeignService.creatOrder(currentUser.getId(), shippingId);
    }

    /**
     * 取消订单
     *
     * @param request
     * @param orderNo
     * @return
     */
    @PostMapping("cancelOrder/{orderNo}")
    public ReturnData cancelOrder(HttpServletRequest request, @PathVariable Long orderNo) {
        MmallUser currentUser = SessionAttribute.currentUser(request.getSession());
        return protalFeignService.cancelOrder(currentUser.getId(), orderNo);
    }

    /**
     * 查询已勾选的购物车订单
     *
     * @param request
     * @return
     */
    @GetMapping("getOrderCartProduct")
    public ReturnData getOrderCartProduct(HttpServletRequest request) {
        MmallUser currentUser = SessionAttribute.currentUser(request.getSession());
        return protalFeignService.getOrderCartProduct(currentUser.getId());
    }

    @GetMapping("getOrderDetails/{orderNo}")
    public ReturnData getOrderDetails(HttpServletRequest request, @PathVariable Long orderNo) {
        MmallUser currentUser = SessionAttribute.currentUser(request.getSession());
        return protalFeignService.getOrderDetails(currentUser.getId(), orderNo);
    }

    @GetMapping("pageOrder")
    public ReturnData pageOrder(HttpServletRequest request, @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                @RequestParam(required = false, defaultValue = "1") Integer pageNo) {
        MmallUser currentUser = SessionAttribute.currentUser(request.getSession());
        return protalFeignService.pageOrder(currentUser.getId(), pageSize, pageNo);
    }

}