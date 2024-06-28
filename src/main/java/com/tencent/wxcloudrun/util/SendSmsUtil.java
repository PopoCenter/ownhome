package com.tencent.wxcloudrun.util;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.util.Objects;

/**
 * @author dongdongxie
 * @date 2024/5/6
 */

@Slf4j
public class SendSmsUtil {



//    public static void main(String[] args) {
//        SendSmsRequest request = new SendSmsRequest();
//        request.setPhone("15210235657");
//
//        request.setSmsSdkAppId("1400788564");
//
//        request.setSecretId("");
//        request.setSecretKey("");
//
//        request.setSignName("蓝图数字化工具");
//        request.setTemplateId("1662625");
//        // 这个值，要看你的模板中是否预留了占位符，如果没有则不需要设置
//        request.setTemplateParamSet(new String[]{"模板中的参数值，如果没有则为空"});
//        SendSmsUtil.sendSms(request);
//    }

        public static Boolean sendSms(SendSmsRequest request) {
            Credential cred = new Credential(request.getSecretId(), request.getSecretKey());

            SmsClient client = new SmsClient(cred, "ap-guangzhou");

            final var req = new com.tencentcloudapi.sms.v20210111.models.SendSmsRequest();
            req.setPhoneNumberSet(new String[]{"+86" + request.getPhone()});
            req.setSmsSdkAppId(request.getSmsSdkAppId());
            req.setSignName(request.getSignName());
            req.setTemplateId(request.getTemplateId());
            req.setTemplateParamSet(request.getTemplateParamSet());

            SendSmsResponse res = null;
            try {
                res = client.SendSms(req);
            } catch (TencentCloudSDKException e) {
                log.error("发送短信出错：", e);
                return Boolean.FALSE;
            }
            log.info("发送短信结果 response={}", SendSmsResponse.toJsonString(res));

            if (Objects.nonNull(res.getSendStatusSet()) && res.getSendStatusSet().length > 0 && "Ok".equals(res.getSendStatusSet()[0].getCode())){
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }

        /**
         * 参数对象
         */
        @Data
        public static class SendSmsRequest {
            /**
             * 电话
             */
            private String phone;

            /**
             * 短信签名内容，必须填写已审核通过的签名
             */
            private String signName;

            /**
             * 模板 ID: 必须填写已审核通过的模板 ID
             */
            private String templateId;

            /**
             * 模板参数: 模板参数的个数需要与 TemplateId 对应模板的变量个数保持一致，若无模板参数，则设置为空
             */
            private String[] templateParamSet;

            /**
             * 应用id
             */
            private String smsSdkAppId;
            /**
             * 云api密钥中的 secretId
             */
            private String secretId;

            /**
             * 云api密钥中的 secretKey
             */
            private String secretKey;
        }


}
