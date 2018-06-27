package com.charan.communication.email;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSendService implements Runnable{

    public Email em;

    public Email getEm() {
        return em;
    }

    public void setEm(Email em) {
        this.em = em;
    }

    public void sendEmail(Email em) {
        sendFromGMail(em.getFrom(), em.getPwd(), em.getTo(), em.getSubject(), em.getBody());
    }

    // editor@clinicalstudiesjournal.com
    // row123!@#
    private static void sendFromGMail(String from, String pass, String[] to, String subject, String body) {
//        System.out.println("Sending email : " + subject);
        Properties props = System.getProperties();
        String host;
        if (from.indexOf("gmail") != -1) {
            host = "smtp.gmail.com";
        } else {
            host = "smtp.clinicalstudiesjournal.com";
        }
//        host = "localhost";
//        String port = "1025"; // "587"
        String port = "587";
//        String host = "smtp.gmail.com";
//        String host = "smtp.clinicalstudiesjournal.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
//            message.setText(body);
            message.setContent(body, "text/html; charset=utf-8");
            message.setSentDate(new Date());

            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            if (from.indexOf("gmail") != -1) { // || from.indexOf("clinicalstudiesjournal") != -1) {
                return;
            }


            // Copy message to "Sent Items" folder as read
//            host = "imap.clinicalstudiesjournal.com";
            host = "imap.gmail.com";
            if (from.indexOf("gmail") != -1) {
                host = "imap.gmail.com";
            } else {
                host = "imap.clinicalstudiesjournal.com";
            }
            Store store = session.getStore("imap");
            if (from.indexOf("gmail") != -1) {
                store.connect(host, 993, from, pass);
            } else {
                store.connect(host, from, pass);
            }

            Folder[] f = store.getDefaultFolder().list();
//            for(Folder fd:f)
//                System.out.println(">> "+fd.getName());

            Folder folder = store.getFolder("Sent Items");
            folder.open(Folder.READ_WRITE);
            message.setFlag(Flags.Flag.SEEN, false);
            folder.appendMessages(new Message[] {message});
            store.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }


    public void run() {
        sendEmail(em);
    }
}
