package com.pay.pingpg;

import java.util.HashMap;
import java.util.Map;

import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.APIConnectionException;
import com.pingplusplus.exception.APIException;
import com.pingplusplus.exception.AuthenticationException;
import com.pingplusplus.exception.ChannelException;
import com.pingplusplus.exception.InvalidRequestException;
import com.pingplusplus.exception.PingppException;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Refund;

public class ChargeUtils {
	/**
	 * pingpp 管理平台对应的 API key
	 * 测试 sk_test_48uLOSzH4u5GTGyjPGz9G8uH
	 * 正式 sk_live_PizTS8eb1K48OyP4S4GmLu14
	 */
	public static String apiKey = "sk_live_PizTS8eb1K48OyP4S4GmLu14";
	/**
	 * pingpp 管理平台对应的应用 ID
	 * 测试 app_SyLS84GSWbL0mX1C 
	 * 正式 app_PWvjn1LWnjn1bXDW
	 */
	public static String appId = "app_PWvjn1LWnjn1bXDW";
	
	private static ChargeUtils chargeUtils;
	private ChargeUtils(){}
	public synchronized static ChargeUtils getInstance(){
		if(chargeUtils==null){
			chargeUtils = new ChargeUtils();
		}
		return chargeUtils;
	}
	/**
     * 创建 Charge
     * 
     * 创建 Charge 用户需要组装一个 map 对象作为参数传递给 Charge.create();
     * map 里面参数的具体说明请参考：https://pingxx.com/document/api#api-c-new
     * 
     * order_no required 商户订单号，适配每个渠道对此参数的要求，必须在商户系统内唯一。
     * 			(alipay: 1-64 位， wx: 1-32 位，bfb: 1-20 位，upacp: 8-40 位，
     * 			yeepay_wap:1-50 位，jdpay_wap:1-30 位，推荐使用 8-20 位，
     * 			要求数字或字母，不允许特殊字符)。
     * app required 支付使用的 app 对象的 id。
     * channel required 支付使用的第三方支付渠道,取值范围如下:
      			alipay:支付宝手机支付
				alipay_wap:支付宝手机网页支付
				alipay_qr:支付宝扫码支付
				apple_pay:Apple Pay
				bfb:百度钱包移动快捷支付
				bfb_wap:百度钱包手机网页支付
				upacp:银联全渠道支付（2015 年 1 月 1 日后的银联新商户使用。若有疑问，请与 Ping++ 或者相关的收单行联系）
				upacp_wap:银联全渠道手机网页支付（2015 年 1 月 1 日后的银联新商户使用。若有疑问，请与 Ping++ 或者相关的收单行联系）
				upmp:银联手机支付（限个人工作室和 2014 年之前的银联老客户使用。若有疑问，请与 Ping++ 或者相关的收单行联系）
				upmp_wap:银联手机网页支付（限个人工作室和 2014 年之前的银联老客户使用。若有疑问，请与 Ping++ 或者相关的收单行联系）
				wx:微信支付
				wx_pub:微信公众账号支付
				wx_pub_qr:微信公众账号扫码支付
				yeepay_wap:易宝手机网页支付
				jdpay_wap:京东手机网页支付
     * amount required 订单总金额, 单位为对应币种的最小货币单位，例如：人民币为分（如订单总金额为 1 元，此处请填 100）。
     * client required 发起支付请求终端的 IP 地址，格式为 IPV4，如: 127.0.0.1。
     * currency required 三位 ISO 货币代码，目前仅支持人民币 cny。
     * subject required 商品的标题，该参数最长为 32 个 Unicode 字符，银联全渠道（upacp/upacp_wap）限制在 32 个字节。
     * body required 商品的描述信息，该参数最长为 128 个 Unicode 字符，yeepay_wap 对于该参数长度限制为 100 个 Unicode 字符。
     * extra optional 特定渠道发起交易时需要的额外参数以及部分渠道支付成功返回的额外参数。
     			alipay:(支付宝手机支付)。支付完成将额外返回付款用户的支付宝账号 buyer_account 。
				alipay_wap:(支付宝手机网页支付)。参数 success_url[string] 为支付成功的回调地址，required；
						参数 cancel_url[string] 为支付取消的回调地址，optional；
						支付完成将额外返回付款用户的支付宝账号 buyer_account 。
				wx:(微信支付)。支付完成将额外返回付款用户的 open_id 。
				wx_pub:(微信公众账号支付)。参数 open_id[string] 为用户在商户 appid 下的唯一标识，required；
				upacp_wap:(银联全渠道 手机网页支付)。参数 result_url[string] 为支付完成的回调地址，required；
				upmp_wap:(银联 手机网页支付)。参数 result_url[string] 为支付完成的回调地址，required；
				bfb_wap:(百度钱包 手机网页支付)。参数 result_url[string] 为支付完成的回调地址，required；
						参数 bfb_login[boolean] 为是否需要登录百度钱包来进行支付，required；
				apple_pay:参数 payment_token[string] 为支付所需的支付令牌，从 client 获得，required；
				wx_pub_qr:(微信公众账号扫码支付)。参数 product_id[string] 为商品 ID，1-32 位字符串。
						此 id 为二维码中包含的商品 ID，商户自行维护，required；
						支付完成将额外返回付款用户的 open_id 。
				yeepay_wap:(易宝手机网页支付)。参数 product_category[string] 为商品类别码，详见商品类型码表，required；
						参数 identity_id[string] 为用户标识,商户生成的用户账号唯一标识，最长 50 位字符串，required；
						参数 identity_type[int] 为用户标识类型，详见用户标识类型码表，required；
						参数 terminal_type[int] 为终端类型，对应取值 0:IMEI, 1:MAC, 2:UUID, 3:other，required；
						参数 terminal_id[string] 为终端 ID，required；
						参数 user_ua[string] 为用户使用的移动终端的 UserAgent 信息，required；
						参数 result_url[string] 为前台通知地址，required。
				jdpay_wap:(京东手机网页支付)。参数 success_url[string] 为支付成功页面跳转路径，required；
						参数 fail_url[string] 为支付失败页面跳转路径，required；
						参数 token[string] 为用户交易令牌，用于识别用户信息，支付成功后会调用 success_url 返回给商户。
						商户可以记录这个 token 值，当用户再次支付的时候传入该 token，用户无需再次输入银行卡信息，
						直接输入短信验证码进行支付。32 位字符串，optional；
     * time_expire: optional 订单失效时间，用 Unix 时间戳表示。时间范围在订单创建后的 1 分钟到 15 天，默认为 1 天，
      					创建时间以 Ping++ 服务器时间为准。 该参数不适用于微信旧渠道支付，微信新渠道支付可支持该参数设置；
      					银联对该参数的有效值限制为 1 小时内。
	 *	metadata: optional 参考 Metadata 元数据。
	 *	description: optional 订单附加说明，最多 255 个 Unicode 字符。
     * @return
     */
    public Charge charge(Long amount, String subject, String  body, String orderNo, String channel, String clientIp, Long timeExpire) {
    	Pingpp.apiKey = apiKey;
        Charge charge = null;
        Map<String, Object> chargeMap = new HashMap<String, Object>();
        chargeMap.put("amount", amount);
        chargeMap.put("currency", "cny");
        chargeMap.put("subject", subject);
        chargeMap.put("body", body);
        chargeMap.put("order_no", orderNo);
        chargeMap.put("channel", channel);
        chargeMap.put("client_ip", clientIp);
        chargeMap.put("time_expire", timeExpire);
        Map<String, String> app = new HashMap<String, String>();
        app.put("id",appId);
        chargeMap.put("app", app);
        try {
            //发起交易请求
            charge = Charge.create(chargeMap);
        } catch (PingppException e) {
            e.printStackTrace();
        }
        return charge;
    }
    
    public Charge getWeixinCharge(Long amount, String subject, String  body, String orderNo, String channel,String currency, String clientIp,Map<String, String> extra, Long timeExpire) {
    	Pingpp.apiKey = apiKey;
        Charge charge = null;
        Map<String, Object> chargeMap = new HashMap<String, Object>();
        chargeMap.put("amount", amount);
        chargeMap.put("currency", "cny");
        chargeMap.put("subject", subject);
        chargeMap.put("body", body);
        chargeMap.put("order_no", orderNo);
        chargeMap.put("channel", channel);
        chargeMap.put("client_ip", clientIp);
        chargeMap.put("currency", currency);
        chargeMap.put("extra", extra);
        chargeMap.put("time_expire", timeExpire);
        Map<String, String> app = new HashMap<String, String>();
        app.put("id",appId);
        chargeMap.put("app", app);
        try {
            //发起交易请求
            charge = Charge.create(chargeMap);
        } catch (PingppException e) {
            e.printStackTrace();
        }
        return charge;
    }
    public Charge getWeixinAppiontmentCharge(Long amount, String subject, String  body, String orderNo, String channel,String currency, String clientIp,Map<String, String> extra, Long timeExpire,String description) {
    	Pingpp.apiKey = apiKey;
        Charge charge = null;
        Map<String, Object> chargeMap = new HashMap<String, Object>();
        chargeMap.put("amount", amount);
        chargeMap.put("currency", "cny");
        chargeMap.put("subject", subject);
        chargeMap.put("body", body);
        chargeMap.put("order_no", orderNo);
        chargeMap.put("channel", channel);
        chargeMap.put("client_ip", clientIp);
        chargeMap.put("currency", currency);
        chargeMap.put("extra", extra);
        chargeMap.put("time_expire", timeExpire);
        Map<String, String> app = new HashMap<String, String>();
        app.put("id",appId);
        chargeMap.put("app", app);
        chargeMap.put("description", description);
        try {
            //发起交易请求
            charge = Charge.create(chargeMap);
        } catch (PingppException e) {
            e.printStackTrace();
        }
        return charge;
    }
    public Charge getWeixinReCharge(Long amount, String subject, String  body, String orderNo, String channel,String currency, String clientIp,Map<String, String> extra, Long timeExpire,String description) {
    	Pingpp.apiKey = apiKey;
        Charge charge = null;
        Map<String, Object> chargeMap = new HashMap<String, Object>();
        chargeMap.put("amount", amount);
        chargeMap.put("currency", "cny");
        chargeMap.put("subject", subject);
        chargeMap.put("body", body);
        chargeMap.put("order_no", orderNo);
        chargeMap.put("channel", channel);
        chargeMap.put("client_ip", clientIp);
        chargeMap.put("currency", currency);
        chargeMap.put("extra", extra);
        chargeMap.put("time_expire", timeExpire);
        Map<String, String> app = new HashMap<String, String>();
        app.put("id",appId);
        chargeMap.put("app", app);
        chargeMap.put("description", description);
        try {
            //发起交易请求
            charge = Charge.create(chargeMap);
        } catch (PingppException e) {
            e.printStackTrace();
        }
        return charge;
    }
    
    /**
	 * 退款处理
	 * */
	public Refund refund(String chid,Long amount,String description){
		Pingpp.apiKey = apiKey;
		Charge ch;
		try {
			ch = Charge.retrieve(chid);
			Map<String, Object> refundMap = new HashMap<String, Object>();
		    refundMap.put("amount", amount);
		    refundMap.put("description", description);
		    return ch.getRefunds().create(refundMap);
		} catch (AuthenticationException e) {
			e.printStackTrace();
		} catch (InvalidRequestException e) {
			e.printStackTrace();
		} catch (APIConnectionException e) {
			e.printStackTrace();
		} catch (APIException e) {
			e.printStackTrace();
		} catch (ChannelException e) {
			e.printStackTrace();
		}
	    return null;
	}
    
	public Charge retrieve(String chargeid) {
		try {
			Pingpp.apiKey = apiKey;
			Charge charge = Charge.retrieve(chargeid);
			return charge;

		} catch (PingppException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static void main(String[] args) {
		
		Charge c = ChargeUtils.getInstance().retrieve("ch_Wr9GWTvnnzTGiLuPuH1eX9a1");
		System.out.println(c.getPaid());
		
	}
    
}
