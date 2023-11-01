package br.com.kingsdevs.messages;

import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

public class EmailMessage {

	/* parametros para envio de email*/
	 
	private static final String CONTA = "xxxxxxxxxxxxxxxx";
	private static final String SENHA = "xxxxxxxxxxxxxxxx";
	private static final String SMTP = "smtp.gmail.com";
	private static final Integer PORTA = 587;
	
		public static void sendMessage(final String dest, final String assunto, final String mensagem) throws Exception {

			JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

			mailSender.setHost(SMTP);
			mailSender.setPort(PORTA);
			mailSender.setUsername(CONTA);
			mailSender.setPassword(SENHA);

			Properties javaMailProterties = new Properties();

			javaMailProterties.put("mail.smtp.starttls.enable", "true");
			javaMailProterties.put("mail.smtp.auth", "true");
			javaMailProterties.put("mail.smtp.transport.protocol", "smtp");
			javaMailProterties.put("mail.debug", "true");

			mailSender.setJavaMailProperties(javaMailProterties);
			
			MimeMessagePreparator preparator = new MimeMessagePreparator() {

				@Override
				public void prepare(MimeMessage mimeMessage) throws Exception {
					
					MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
					
					message.setTo(dest);
					message.setFrom(CONTA);
					message.setSubject(assunto);
					message.setText(mensagem);
				}
			};

			mailSender.send(preparator);
		}
	
	
}
