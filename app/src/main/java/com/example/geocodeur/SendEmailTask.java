package com.example.geocodeur;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmailTask extends AsyncTask<Void, Void, Boolean> {

    private final String toEmail;
    private final String subject;
    private final String body;

    public SendEmailTask(String toEmail, String subject, String body) {
        this.toEmail = toEmail;
        this.subject = subject;
        this.body = body;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            Properties props = new Properties();
            props.setProperty("mail.smtp.host", "smtp.titan.email");
            props.setProperty("mail.smtp.port", "587");
            props.setProperty("mail.smtp.auth", "true");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.ssl.trust", "smtp.titan.email");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("abdelmajid.merabti@ensafez.com", "Ensaf2019@@");
                }
            });
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress("abdelmajid.merabti@ensafez.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(this.toEmail));
            message.setSubject(this.subject);
            message.setText(this.body);
            Transport.send(message);


            return true;
        } catch (Exception ex) {
            Log.e("SendEmailTask", "Erreur lors de l'envoi de l'e-mail", ex);
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            //    Toast.makeText(, "L'e-mail a été envoyé avec succès", Toast.LENGTH_LONG).show();

            // ...
        } else {
            // Code pour indiquer qu'il y a eu une erreur lors de l'envoi de l'email
            // ...
        }
    }
}
