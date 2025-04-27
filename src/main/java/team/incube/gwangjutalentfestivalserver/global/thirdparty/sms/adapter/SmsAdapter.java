package team.incube.gwangjutalentfestivalserver.global.thirdparty.sms.adapter;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import team.incube.gwangjutalentfestivalserver.global.thirdparty.sms.dto.SendSmsRequest;
import team.incube.gwangjutalentfestivalserver.global.thirdparty.sms.dto.SendSmsResponse;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Base64;

@Component
public class SmsAdapter {
    // TODO 환경변수 가져오기 설정
    private String serviceId;
    private String accessKey;
    private String secretKey;

    public void sendSms(SendSmsRequest request) {
        WebClient webClient = WebClient.builder().build();
        long currentTimeMillis = new Timestamp(System.currentTimeMillis()).getTime();

        SendSmsResponse response = webClient.post()
            .uri("https://sens.apigw.ntruss.com/sms/v2/services/{serviceId}/messages", serviceId)
            .header("Content-Type", "application/json")
            .header("x-ncp-apigw-timestamp", String.valueOf(currentTimeMillis))
            .header("x-ncp-iam-access-key", accessKey)
            .header("x-ncp-apigw-signature-v2", secretKey)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(SendSmsResponse.class)
            .block();

        // TODO response에 따라 예외 처리
    }

    private String makeSignature(String timestampString, String urlString, HttpMethod httpMethod) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {

        String space = " ";
        String newLine = "\n";
        String method = httpMethod.name();

        String message = method +
                space +
                urlString +
                newLine +
                timestampString +
                newLine +
                accessKey;

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(rawHmac);
    }
}
