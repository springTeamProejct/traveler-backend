package traveler.travel.domain.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import traveler.travel.global.util.RedisUtil;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
@EnableAsync
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;

    @Value("${spring.mail.username}")
    private String from;

    // 인증 메일 본문 생성
    public MimeMessage generateAuthMessage(String to, String code) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, to); // 받는 사람
        message.setSubject("[TRAVELER] 이메일 인증"); // 메일 제목

        // 메일 본문
        String msg = "";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 확인</h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 회원가입 화면에서 입력해주세요.</p>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += code;
        msg += "</td></tr></tbody></table></div>";

        message.setText(msg, "utf-8", "html"); //내용, charset타입, subtype
        message.setFrom(new InternetAddress(from ,"TRAVELER")); //보내는 사람의 메일 주소, 보내는 사람 이름

        return message;
    }

    // 인증 메일 발송
    @Async
    public String sendAuthMail(String to) throws Exception {
        String code = generateAuthCode();
        MimeMessage message = generateAuthMessage(to, code);
        try {
            // 메일 발송
            mailSender.send(message);
            // redis 에 (메일번호, 인증번호) 저장
            redisUtil.setValueWithExpireTime(to, code);
        } catch (MailException e) {
            e.printStackTrace();
        }
        return code;
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
}
