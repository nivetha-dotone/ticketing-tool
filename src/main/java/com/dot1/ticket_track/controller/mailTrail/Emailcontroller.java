package com.dot1.ticket_track.controller.mailTrail;


import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.Map;

@Service
public class Emailcontroller {

    private  final JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public Emailcontroller(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendAssignmentEmailGroup(List<String>  to, Map<String, Object> model, String subject) throws Exception {
      try{
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        Context context = new Context();
        context.setVariables(model);

        String htmlContent = templateEngine.process("email-templateGroupClient.html", context);
        helper.setFrom("mayurinvideo29@gmail.com");
        helper.setTo(to.toArray(new String[0]));
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
      } catch (Exception e) {
          throw new RuntimeException(e);
      }

    }

    public void sendAssignmentEmailwithEmployee(String  to, Map<String, Object> model, String subject) throws Exception {
      try{
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        Context context = new Context();
        context.setVariables(model);

        String htmlContent = templateEngine.process("email-templateAssign.html", context);
        helper.setFrom("mayurinvideo29@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
      } catch (Exception e) {
          throw new RuntimeException(e);
      }

    }

    public void sendChangedEmailwithClientCme(String  to, Map<String, Object> model, String subject) throws Exception {
      try{
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        Context context = new Context();
        context.setVariables(model);

        String htmlContent = templateEngine.process("email-templateResolvedTikcet.html", context);
        helper.setFrom("mayurinvideo29@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        mailSender.send(message);

      } catch (Exception e) {
          throw new RuntimeException(e);
      }

    }

    public void sendONHOLDEmailwithClientCme(String  to, Map<String, Object> model, String subject) throws Exception {
      try{
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        Context context = new Context();
        context.setVariables(model);

        String htmlContent = templateEngine.process("email-templateONHOLDTikcet.html", context);
        helper.setFrom("mayurinvideo29@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        mailSender.send(message);

      } catch (Exception e) {
          throw new RuntimeException(e);
      }

    }

    public void sendClosedEmailWithEmployee(String  to, Map<String, Object> model, String subject) throws Exception {
      try{
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        Context context = new Context();
        context.setVariables(model);

        String htmlContent = templateEngine.process("email-templateCloseTikcet.html", context);
        helper.setFrom("mayurinvideo29@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        mailSender.send(message);

      } catch (Exception e) {
          throw new RuntimeException(e);
      }

    }
    public void passwordUpdatesuccess(String  to, Map<String, Object> model, String subject) throws Exception {
      try{
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        Context context = new Context();
        context.setVariables(model);

        String htmlContent = templateEngine.process("emailUpdatedPassword.html", context);
        helper.setFrom("mayurinvideo29@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        mailSender.send(message);

      } catch (Exception e) {
          throw new RuntimeException(e);
      }

    }
    public void passwordUpdatesuccessByAdmin(String  to, Map<String, Object> model, String subject) throws Exception {
      try{
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        Context context = new Context();
        context.setVariables(model);

        String htmlContent = templateEngine.process("emailUpdatedPasswordByAdmin.html", context);
        helper.setFrom("mayurinvideo29@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        mailSender.send(message);

      } catch (Exception e) {
          throw new RuntimeException(e);
      }

    }



//    @PostMapping("/send")
//    public ResponseEntity<String> sendEmail() {
//       try{
//
//        SimpleMailMessage message=new SimpleMailMessage();
//        message.setFrom("mayurinvideo29@gmail.com");
//        message.setTo("mayurinvideo29@gmail.com");
//        message.setSubject("Test Email");
//        message.setText("This is a test email sent from Spring Boot application.");
//        mailSender.send(message);
//           return ResponseEntity.ok("Mail sent successfully");
//        } catch (Exception e) {
//           e.printStackTrace(); // 🔍 This will print the full error in logs
//           return ResponseEntity.ok("Authentication failed");
//       }
//       }
}
