package com.limeri.leon.common;

import com.limeri.leon.Models.Profesional;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailSender {
   public static boolean sendMail(Profesional profesional, String asunto, String cuerpo, File adjunto){
       try {
           String para = profesional.getCorreo();

           Properties props = new Properties();
           props.put("mail.smtp.auth", "true");
           props.put("mail.smtp.starttls.enable", "true");
           props.put("mail.smtp.host", "smtp.gmail.com");
           props.put("mail.smtp.port", "587");

           Session session = Session.getInstance(props,
                   new javax.mail.Authenticator() {
                       protected PasswordAuthentication getPasswordAuthentication() {
                           return new PasswordAuthentication("aplicacionleon@gmail.com", "equipolimeri");
                       }
                   });

           Message message = new MimeMessage(session);
           message.setFrom(new InternetAddress("aplicacionleon@gmail.com"));
           message.setRecipients(Message.RecipientType.TO,
                   InternetAddress.parse(para));
           message.setSubject(asunto);
           message.setText(cuerpo);

           if (adjunto != null) {
               Multipart multipart = new MimeMultipart();

               MimeBodyPart messageBodyPart = new MimeBodyPart();
               messageBodyPart.setText(cuerpo);
               messageBodyPart.setContent(cuerpo, "text/html");
               multipart.addBodyPart(messageBodyPart);

               MimeBodyPart adjuntoBodyPart = new MimeBodyPart();
               String file = adjunto.getAbsolutePath();
               String fileName = adjunto.getName();

               DataSource source = new FileDataSource(file);
               adjuntoBodyPart.setDataHandler(new DataHandler(source));
               adjuntoBodyPart.setFileName(fileName);
               multipart.addBodyPart(adjuntoBodyPart);
               message.setContent(multipart);
           }

           Transport.send(message);

           return true;
       } catch (MessagingException e) {
           e.printStackTrace();
           return false;
       }
    }
}