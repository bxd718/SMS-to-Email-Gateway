package mz.ac.bxd.project.otp_sender;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailWorker extends Worker {

    public EmailWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String toEmail = getInputData().getString("toEmail");
        String subject = getInputData().getString("subject");
        String body = getInputData().getString("body");

        try {
            final String fromEmail = "SEUEMAIL";
            final String password = "SuaAPPMAIL";
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("SENDERMAIL"));
            message.setSubject(subject);
            message.setText(body);

            // Enviar o e-mail
            Transport.send(message);
            Log.d("EmailWorker", "E-mail enviado com sucesso.");
            return Result.success();
        } catch (Exception e) {
            Log.e("EmailWorker", "Erro ao enviar e-mail: " + e.getMessage(), e);
            return Result.failure();
        }
    }
}
