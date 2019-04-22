package com.wtj.util;

import com.wtj.entity.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
@Component
public class EmailUtil {

	@Value("${spring.mail.username}")
	private  String from;

	@Autowired
	private JavaMailSender javaMailSender;

	public  boolean send(Email email){
		System.out.println("email:"+email);
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
		try {
			messageHelper.setFrom(from,"天猫商城激活邮件");
			messageHelper.setTo(email.getTo());
			messageHelper.setSubject(email.getSubject());
			messageHelper.setText(email.getContent(),true);
			messageHelper.setSentDate(email.getCreateTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		javaMailSender.send(mimeMessage);
		return true;
	}
}
