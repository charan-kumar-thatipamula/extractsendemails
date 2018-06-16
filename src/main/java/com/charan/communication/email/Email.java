package com.charan.communication.email;

public class Email {
    private String from;
    private String[] to;
    private String subject;
    private String body;
    private String pwd;

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void generateEmail() {

    }

    public String toString() {
        return "From: " + getFrom() + "; To: " + getTo()[0] + "; Subject: " + getSubject();
    }
//    public void sendEmail() {
//        System.out.println("**********Sending email**********");
//        System.out.println("From: " + getFrom());
//        System.out.println("To: " + getTo());
//        System.out.println("Subject: " + getSubject());
//        System.out.println("Body: " + getBody());
//        System.out.println("*********************************");
//    }
}
