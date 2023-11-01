package com.example.jwtcloud.services;

import com.example.jwtcloud.constantes.EmailConstant;
import com.sun.mail.smtp.SMTPTransport;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;

import static com.example.jwtcloud.constantes.EmailConstant.*;
import static javax.mail.Message.RecipientType.CC;
import static javax.mail.Message.RecipientType.TO;

@Service
public class EmailService {

    private Session getEmailSession() {
        Properties properties = System.getProperties();
        properties.put(EmailConstant.SMTP_HOST, EmailConstant.GMAIL_SMTP_SERVER);
        properties.put(SMTP_AUTH, true);
        properties.put(SMTP_PORT, SMTP_PORT);
        properties.put(SMTP_STARTTLS_ENABLE, true);
        properties.put(SMTP_STARTTLS_REQUIRED, true);
        return Session.getInstance(properties, null);
    }


    public void envoyerNouveuPasswordEmail(String nom, String password, String email) throws MessagingException {
        Message message = createEmail(nom, password, email);
        SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
        smtpTransport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);
        smtpTransport.sendMessage(message, message.getAllRecipients());

        smtpTransport.close();
    }


    private Message createEmail(String nom, String password, String email) throws MessagingException {
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(TO, InternetAddress.parse(email, false));
        message.setRecipients(CC, InternetAddress.parse(CC_EMAIL, false));
        message.setSubject(EMAIL_SUBJECT);
        message.setText("Bonjour " + nom + ", \n \n Votre nouveau mot de passe est :" + password +
                "\n \n L'équipe de support technique" +
                ""+
                "\n \n ZIGANI Alphonse Ingénieur informaticien 76 00 48 47/ 70 85 89 16");
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }


    private Message createEmailWithAttachment(String nom, String password, String email, String filePath) throws MessagingException {
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
        message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(CC_EMAIL, false));
        message.setSubject(EMAIL_SUBJECT);

        // Créez le contenu HTML (comme mentionné précédemment)
        String messageContent = "Bonjour " + nom + ","
                + "Votre nouveau mot de passe est : <strong>" + password + "</strong><br/><br/>"
                + "L'équipe de support<br/><br/>"
                + "ZIGANI Alphonse Ingénieur informaticien 76 00 48 47/ 70 85 89 16";

        // Créez une partie pour le contenu HTML
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(messageContent, "text/html");

        // Créez une partie pour le fichier joint
        BodyPart attachmentBodyPart = new MimeBodyPart();

        // Spécifiez le chemin du fichier que vous souhaitez joindre
        String filename = "votre_document.pdf"; // Remplacez cela par le nom de votre fichier PDF
        DataSource source = new FileDataSource(filePath);
        attachmentBodyPart.setDataHandler(new DataHandler(source));
        attachmentBodyPart.setFileName(filename);

        // Créez un Multipart pour combiner le contenu HTML et le fichier joint
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        multipart.addBodyPart(attachmentBodyPart);

        // Définissez le contenu du message comme le Multipart
        message.setContent(multipart);

        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }

}
