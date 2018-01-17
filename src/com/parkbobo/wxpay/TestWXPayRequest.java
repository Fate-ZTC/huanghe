package com.parkbobo.wxpay;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.OkHttpClient;

/**
 * Created by ariesun on 2017/4/20.
 */
public class TestWXPayRequest {

    public static void main(String[] args) throws Exception {

        String result;
        WXPayConfigImpl config = WXPayConfigImpl.getInstance();
        WXPayRequest wxPayRequest = new WXPayRequest(config);

        String data2 = "<xml><sign_type>HMAC-SHA256</sign_type><nonce_str>7261bbcf236b11e7a2bd484520356fdc</nonce_str><sign>6010521E041EA6F378D25B85CCD5DA6871E34E9BBBC441E7BC4744C3CBF27AEB</sign><op_user_id>100</op_user_id><mch_id>11473623</mch_id><out_trade_no>20161909105959000000111108</out_trade_no><total_fee>1</total_fee><appid>wxab8acb865bb1637e</appid><out_refund_no>20161909105959000000111108</out_refund_no><refund_fee>1</refund_fee></xml>";
        result = wxPayRequest.requestWithCert("/secapi/pay/refund", "1213uuid", data2, 10000, 10000, true);
        HttpsRequest httpsRequest = new HttpsRequest();
        String sendPost = httpsRequest.sendPost("/secapi/pay/refund",data2);
        System.out.println(sendPost);
        
        OkHttpClient client=new OkHttpClient.Builder()
        					
        					.build();

    }
    /**

     * 设置证书文件

     *

     * @param certificates

     */
    public void setCertificates(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {

                }
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");

            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);
            sslContext.init
                    (
                            null,
                            trustManagerFactory.getTrustManagers(),
                            new SecureRandom()
                    );
//            mOkHttpClient.setSslSocketFactory(sslContext.getSocketFactory());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
