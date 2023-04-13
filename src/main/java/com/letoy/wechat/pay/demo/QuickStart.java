package com.letoy.wechat.pay.demo;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.payments.jsapi.model.Amount;
import com.wechat.pay.java.service.payments.jsapi.model.Payer;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayResponse;

/** JSAPI 下单为例 */
public class QuickStart {

    /** 商户号 */
    public static String merchantId = "1638473710";
    /** 商户API私钥路径 */
    public static String privateKeyPath = "/cert/apiclient_key.pem";
    /** 商户证书序列号 */
    public static String merchantSerialNumber = "57A36EC301F7253920E9CBBB11D950B9E708EBCA";
    /** 商户APIV3密钥 */
    public static String apiV3key = "ChinaLetoy2023ChinaLetoyChinaLet";

    public static void main(String[] args) {
        Config config =
                new RSAAutoCertificateConfig.Builder()
                        .merchantId(merchantId)
                        .privateKeyFromPath(privateKeyPath)
                        .merchantSerialNumber(merchantSerialNumber)
                        .apiV3Key(apiV3key)
                        .build();
        JsapiService service = new JsapiService.Builder().config(config).build();
        // request.setXxx(val)设置所需参数，具体参数可见Request定义
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        amount.setTotal(100);
        request.setAmount(amount);
        request.setAppid("wx632d1d9ad7cab2e4");
        request.setMchid("1638473710");
        request.setDescription("测试商品标题");
        request.setNotifyUrl("https://letoy.tech/LeOrder/ReceiveMsg");
        request.setOutTradeNo("out_trade_no_001");
        Payer payer = new Payer();
        payer.setOpenid("oLTPCuN5a-nBD4rAL_fa********");
        request.setPayer(payer);
        PrepayResponse response = service.prepay(request);
        System.out.println(response.getPrepayId());
    }
}