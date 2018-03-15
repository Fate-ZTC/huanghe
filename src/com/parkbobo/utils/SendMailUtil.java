package com.parkbobo.utils;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class SendMailUtil {
	@SuppressWarnings("static-access")
	public static void sendMail(MailModel mailModel){
		// 获得属性，并生成Session对象 
		Properties props = new Properties();
		Session sendsession;
		Transport transport;
		MimeMessage message = null;
		BodyPart messageBodyPart = new MimeBodyPart();
		Multipart multipart = new MimeMultipart();
		try {
			sendsession = Session.getInstance(props, null);
			//向属性中写入SMTP服务器的地址
			props.put("mail.smtp.host", mailModel.getSmtp());
			//设置SMTP服务器需要权限认证
			props.put("mail.smtp.auth", "true");
			//设置输出调试信息
			// sendsession.setDebug(true);
			//根据Session生成Message对象
			message = new MimeMessage(sendsession);
			//设置发信人地址
			message.setFrom(new InternetAddress(mailModel.getFrom()));
			//设置收信人地址
			String toList = mailModel.getTo();
			InternetAddress[] iaToList = new InternetAddress().parse(toList);
			message.setRecipients(Message.RecipientType.TO, iaToList);
			if (mailModel.getCc() != null) {
				String ccList = mailModel.getCc();
				InternetAddress[] iaCCList = new InternetAddress()
						.parse(ccList);
				message.setRecipients(Message.RecipientType.CC, iaCCList);
			}
			if (mailModel.getBcc() != null) {
				String bccList = mailModel.getBcc();
				InternetAddress[] iaBCCList = new InternetAddress()
						.parse(bccList);
				message.setRecipients(Message.RecipientType.BCC, iaBCCList);
			}
			//设置e-mail标题 
			message.setSubject(mailModel.getTitle());
			//设置e-mail发送时间
			message.setSentDate(new Date());
			//设置e-mail内容
			message.setText(mailModel.getContent());
			//建立第一部分：文本正文
			messageBodyPart.setContent(mailModel.getContent(), "text/html;charset=gbk");//给BodyPart对象设置内容和格式/编码方式    
			multipart.addBodyPart(messageBodyPart);
			if (!mailModel.getAttachment().equals("")) {
				// 建立第二部分：附件     
				messageBodyPart = new MimeBodyPart();
				// 获得附件
				DataSource source = new FileDataSource(mailModel.getAttachment());
				//设置附件的数据处理器
				messageBodyPart.setDataHandler(new DataHandler(source));
				// 设置附件文件名
				messageBodyPart.setFileName(mailModel.getAttachment());
				// 加入第二部分
				multipart.addBodyPart(messageBodyPart);
			}
			// 将多部分内容放到e-mail中
			message.setContent(multipart);

			//保存对于e-mail的修改
			message.saveChanges();
			//根据Session生成Transport对象
			transport = sendsession.getTransport("smtp");
			//连接到SMTP服务器
			transport.connect(mailModel.getSmtp(),mailModel.getFromName(),mailModel.getFrompsw());
			//发送e-mail
			transport.sendMessage(message, message.getAllRecipients());
			//关闭Transport连接
			transport.close();
		} catch (MessagingException m) {
			System.out.println(m.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
