package com.seu.mstc.utils;

import com.seu.mstc.result.ResultInfo;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;

/**
 * Created by hys on 2018/4/20.
 */
@Service
public class EmailUtils {

    private Properties pp;


    /**
     * 注册发送验证码
     *
     * @param captcha
     * @param email
     */
    public ResultInfo sendCaptcha(String captcha, String email) {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.host", "smtp.qq.com");

        prop.setProperty("mail.transport.protocol", "smtp");
        prop.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.setProperty("mail.smtp.socketFactory.port", "465");

        prop.put("mail.smtp.port", "465");//587
        prop.put("mail.user", "seu_xc@foxmail.com");//
        prop.put("mail.password", "obbjneqgottgfhfc");//
        this.pp = prop;
        Authenticator authenticator = new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = pp.getProperty("mail.user");
                String password = pp.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(pp, authenticator);
        MimeMessage message = new MimeMessage(session);
        try {
            InternetAddress form = new InternetAddress(pp.getProperty("mail.user"), "SEU_MSTC_Blog");

            message.setFrom(form);

            InternetAddress to = new InternetAddress(email);

            message.setRecipient(RecipientType.TO, to);

            message.setSubject("验证码提醒");

            message.setSentDate(new Date());


            MimeMultipart related = new MimeMultipart("related");

            MimeBodyPart content = new MimeBodyPart();


            content.setContent("欢迎注册SEU_MSTC_Blog，这是你的验证码：<span style='font-weight:bold;'>" + captcha + "</span>" + "<br/>若不是本人操作，请忽略本邮件。", "text/html;charset=UTF-8");

            related.addBodyPart(content);
            //related.addBodyPart(resource);

            message.setContent(related);
            Transport.send(message);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return ResultInfo.build(500, "发送验证码邮件发生错误！");
        }
        return ResultInfo.ok();
    }



}