package com.zab.mmal.protal.dopay;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.google.common.collect.Lists;
import com.zab.mmal.api.entity.MmallOrder;
import com.zab.mmal.api.entity.MmallOrderItem;
import com.zab.mmal.api.entity.MmallPayInfo;
import com.zab.mmal.common.commons.ReturnData;
import com.zab.mmal.common.config.FtpConfiguration;
import com.zab.mmal.common.enums.OrderSatus;
import com.zab.mmal.common.enums.PayPlatform;
import com.zab.mmal.common.enums.SysCodeMsg;
import com.zab.mmal.common.enums.TradeStatus;
import com.zab.mmal.common.exceptions.WrongDataException;
import com.zab.mmal.common.utils.BigDecimalUtil;
import com.zab.mmal.common.utils.DateTimeUtil;
import com.zab.mmal.common.utils.FTPUtil;
import com.zab.mmal.common.utils.JudgeUtil;
import com.zab.mmal.protal.alipay.config.Configs;
import com.zab.mmal.protal.alipay.model.ExtendParams;
import com.zab.mmal.protal.alipay.model.GoodsDetail;
import com.zab.mmal.protal.alipay.model.builder.AlipayTradePrecreateRequestBuilder;
import com.zab.mmal.protal.alipay.model.result.AlipayF2FPrecreateResult;
import com.zab.mmal.protal.alipay.service.AlipayTradeService;
import com.zab.mmal.protal.alipay.service.impl.AlipayTradeServiceImpl;
import com.zab.mmal.protal.alipay.utils.ZxingUtils;
import com.zab.mmal.protal.fegin.ProtalFeignService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单流程
 */
@Slf4j
@Component
public class DoPayFlow {

    @Value("${alipay.call.back}")
    private String callBack;
    @Autowired
    private FTPUtil ftpUtil;
    @Resource
    private FtpConfiguration ftpConfiguration;
    @Autowired
    private ProtalFeignService protalFeignService;

    public Map<String, Object> trade_precreate(String path, MmallOrder order) {
        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = order.getOrderNo().toString();

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = new StringBuilder().append("happymmal扫码支付，订单号:").append(outTradeNo).toString();

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment().toString();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = new StringBuilder().append("订单:").append(outTradeNo).append("购买商品共").append(totalAmount).toString();

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 查询系统订单明细
        List<MmallOrderItem> orderItemList = protalFeignService.getOrderItemList(order.getUserId(), order.getOrderNo());
        if (JudgeUtil.isEmpty(orderItemList)) {
            throw new WrongDataException("没有对应的订单明细:{}", order.getOrderNo());
        }

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();

        for (MmallOrderItem orderItem : orderItemList) {
            // 创建并添加第一条商品信息，如用户购买的产品为“黑人牙刷”，单价为5.00元，单价就是500分，购买了两件
            GoodsDetail goods = GoodsDetail.newInstance(orderItem.getProductId().toString(), orderItem.getProductName(),
                    BigDecimalUtil.mul(orderItem.getCurrentUnitPrice().doubleValue(), new Double(100).doubleValue()).longValue(), orderItem.getQuantity());
            goodsDetailList.add(goods);
        }


        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl(callBack)//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setGoodsDetailList(goodsDetailList);

        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("zfbinfo.properties");

        /** 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        AlipayTradeService tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        Map<String, Object> resultMap = new HashMap<>();
        String msg;
        String qrUrl = null;
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

                File folder = new File(path);
                if (!folder.exists()) {
                    folder.setWritable(true);
                    folder.mkdir();
                }

                // 需要修改为运行机器上的路径
                // 细节path没有/
                String qrPath = String.format(path + "/qr-%s.png", response.getOutTradeNo());
                String qtFileName = String.format("qr-%s.png", response.getOutTradeNo());
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrPath);

                File targetFile = new File(path, qtFileName);
                try {
                    ftpUtil.uploadFile("img", Lists.newArrayList(targetFile));
                } catch (IOException e) {
                    log.error("上传二维码异常", e);
                    throw new RuntimeException("上传二维码异常", e);
                }

                qrUrl = ftpConfiguration.getServerHttpPrefix() + targetFile.getName();
                msg = "支付宝预下单成功!!!";
                log.info("filePath:" + qrPath);
            case FAILED:
                msg = "支付宝预下单失败!!!";
                log.error("支付宝预下单失败!!!");
                break;
            case UNKNOWN:
                msg = "系统异常，预下单状态未知!!!";
                log.error("系统异常，预下单状态未知!!!");
                break;
            default:
                msg = "不支持的交易状态，交易返回异常!!!";
                log.error("不支持的交易状态，交易返回异常!!!");
                break;
        }
        resultMap.put("qrUrl", qrUrl);
        resultMap.put("msg", msg);
        return resultMap;
    }

    public ReturnData callBack(Map<String, String> params) {
        Long orderNo = Long.parseLong(params.get("out_trade_no"));
        String tradeNo = params.get("trade_no");
        // 支付宝交易状态
        String tradeStatus = params.get("trade_status");

        MmallOrder order = protalFeignService.getOrderByNo(orderNo);
        if (null == order) {
            return new ReturnData(SysCodeMsg.FAIL.getCode(), "不是本系统订单:" + orderNo + "，回调忽略");
        }

        // todo 校验金额
        BigDecimal payment = order.getPayment();
        BigDecimal alipayment = new BigDecimal(params.get("total_amount"));
        if (!JudgeUtil.isDBEq(BigDecimalUtil.sub(payment.doubleValue(), alipayment.doubleValue()).intValue(), 0)) {
            return new ReturnData(SysCodeMsg.FAIL.getCode(), "金额不一致，不是本系统订单:" + orderNo + "，回调忽略");
        }

        if (order.getStatus() >= OrderSatus.PAID.getCode()) {
            return new ReturnData("支付宝重复调用");
        }

        if (TradeStatus.TRADE_SUCCESS.name().equalsIgnoreCase(tradeStatus)) {
            // 如果是交易成功了
            MmallOrder updateOrder = new MmallOrder();
            updateOrder.setOrderNo(order.getOrderNo());
            updateOrder.setStatus(OrderSatus.PAID.getCode());
            updateOrder.setPaymentTime(DateTimeUtil.strToDate(params.get("gmt_payment")));
            protalFeignService.updateOrderStatus(updateOrder);
        }

        // 创建支付信息
        MmallPayInfo payInfo = new MmallPayInfo();
        payInfo.setOrderNo(orderNo);
        payInfo.setUserId(order.getUserId());
        payInfo.setPayPlatform(PayPlatform.ALIPAY.getCode());
        payInfo.setPlatformNumber(tradeNo);
        payInfo.setPlatformStatus(tradeStatus);

        return protalFeignService.addPayInfo(payInfo);
    }

    // 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }

}
