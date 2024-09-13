package mz.ac.bxd.project.otp_sender;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        try {
            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");

                if (pdusObj != null) {
                    StringBuilder fullMessage = new StringBuilder();

                    for (int i = 0; i < pdusObj.length; i++) {
                        SmsMessage currentMessage = getIncomingMessage(pdusObj[i], bundle);
                        String message = currentMessage.getMessageBody();
                        fullMessage.append(message);

                        if (i == pdusObj.length - 1) {
                            String sender = currentMessage.getDisplayOriginatingAddress();
                            String completeMessage = "Sender: " + sender + " Message: " + fullMessage.toString();
                            Log.d("SmsReceiver", "Message : " + completeMessage);

                            if ("Absa".equalsIgnoreCase(sender) || "SIMORede".equalsIgnoreCase(sender)) {

                                // Preparar os dados para o Worker
                                Data emailData = new Data.Builder()
                                        .putString("toEmail", "SENDERMAIL")
                                        .putString("subject", "SMS Recebido")
                                        .putString("body", completeMessage)
                                        .build();

                                // Criar e enfileirar o trabalho de envio de e-mail
                                OneTimeWorkRequest emailWorkRequest = new OneTimeWorkRequest.Builder(EmailWorker.class)
                                        .setInputData(emailData)
                                        .build();

                                WorkManager.getInstance(context).enqueue(emailWorkRequest);
                            }
                        }
                    }
                } else {
                    Log.d("SmsReceiver", "onReceive: SMS is null");
                }
            }

        } catch (Exception e) {
            Log.d("SmsReceiver", "Exception smsReceiver: " + e.getMessage());
        }
    }

    private SmsMessage getIncomingMessage(Object object, Bundle bundle) {
        SmsMessage currentSMS;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            String format = bundle.getString("format");
            currentSMS = SmsMessage.createFromPdu((byte[]) object, format);
        } else {
            currentSMS = SmsMessage.createFromPdu((byte[]) object);
        }
        return currentSMS;
    }
}
