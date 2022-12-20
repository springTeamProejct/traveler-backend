package traveler.travel.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import traveler.travel.util.NcpUtil;
import traveler.travel.util.RedisUtil;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SmsService {

    private final NcpUtil ncpUtil;
    private final RedisUtil redisUtil;

    @Value("${ncp-sms.serviceId}")
    private String serviceId;
    @Value("${ncp-sms.senderPhoneNum}")
    private String senderPhoneNum;


    // 인증 문자 발송
    public SmsResponseDto sendAuthCode(String to) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException, URISyntaxException {

        String code = generateAuthCode();

        Long time = System.currentTimeMillis();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", ncpUtil.getAccessKey());
        headers.set("x-ncp-apigw-signature-v2",
                ncpUtil.makeSignature("POST", "/sms/v2/services/" + this.serviceId + "/messages", time));


        MessageDto message = MessageDto.builder()
                .to(to)
                .content("[TRAVELER]인증번호:" + code)
                .build();

        List<MessageDto> messages = new ArrayList<>();
        messages.add(message);

        SmsRequestDto request = SmsRequestDto.builder()
                .from(senderPhoneNum)
                .content(message.getContent())
                .messages(messages)
                .build();


        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(request);
        HttpEntity<String> httpBody = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        SmsResponseDto response = restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/" + this.serviceId + "/messages"),
                httpBody, SmsResponseDto.class);

        if (response.getStatusCode().equals("202")) {
            saveAuthCode(to, code);
        }

        return response;
    }

    // 인증 번호 생성
    public String generateAuthCode() {
        StringBuilder code = new StringBuilder();
        Random rnd = new Random();

        for (int i = 0; i < 6; i++) { // 인증코드 6자리
            code.append((rnd.nextInt(10)));
        }
        return code.toString();
    }

    // Redis 에 인증번호 저장
    public void saveAuthCode(String key, String value) {
        redisUtil.setValueWithExpireTime(key, value);
    }

    @Getter
    @Builder
    private static class MessageDto {
        private String to;
        private String content;
    }


    @Builder
    @Getter
    private static class SmsRequestDto {
        private final String type = "SMS";
        private String from;
        private String content;
        List<MessageDto> messages;

    }

    @Getter
    public static class SmsResponseDto {
        private String requestId;
        private String requestTime;
        private String statusCode;
        private String statusName;
    }
}
