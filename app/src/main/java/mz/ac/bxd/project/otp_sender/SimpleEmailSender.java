package mz.ac.bxd.project.otp_sender;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import android.util.Log;

public class SimpleEmailSender {

    // Método para enviar e-mails usando JavaMail
    public static void sendEmail(String toEmail, String subject, String body) {
        final String fromEmail = "SEUEMAIL";
        final String password = "SuaAPPAssMAIL";

        // Configurações do servidor SMTP
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Autenticação da conta do e-mail
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            // Cria a mensagem de e-mail
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));  // Remetente
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));  // Destinatário
            message.setSubject(subject);  // Assunto do e-mail
            message.setText(body);  // Corpo do e-mail

            // Enviar o e-mail
            Transport.send(message);
            Log.d("SimpleEmailSender", "E-mail enviado com sucesso.");

        } catch (Exception e) {
            Log.e("SimpleEmailSender", "Erro ao enviar e-mail: " + e.getMessage(), e);
        }
    }
}
