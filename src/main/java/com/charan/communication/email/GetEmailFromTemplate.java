package com.charan.communication.email;

import com.charan.communication.csv.HeaderIndex;
import com.charan.global.CSVContext;
import com.charan.global.GlobalContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class GetEmailFromTemplate {
    String title;
    String author;
    String email;
    String templatePath;
    GlobalContext globalContext = GlobalContext.getGlobalContext();
    CSVContext csvContext;

    public GetEmailFromTemplate(String title, String author, String email) {
        this.title = title;
        this.author = author;
        this.email = email;
    }

    public GetEmailFromTemplate() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = "\"" + title + "\"";
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTemplateFilePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public CSVContext getCsvContext() {
        return csvContext;
    }

    public void setCsvContext(CSVContext csvContext) {
        this.csvContext = csvContext;
    }

    public Email process() {
        String template = readTemplate();
        template = updateTemplate(template);
        Email email = new Email();
        email.setTo(new String[]{getEmail()});
        email.setSubject(generateSubject()); // getTitle());
        email.setBody(template);
        email.setFrom(csvContext.getEmail());
        email.setPwd(globalContext.getCredentialsUtil().getPassword(email.getFrom()));
//        email.setFrom(globalContext.getCredentialsUtil().getDefaultEmail());
//        email.setPwd(globalContext.getCredentialsUtil().getDefaultPassword());
//        email.setFrom("editor.jcrm@clinicalstudiesjournal.com");
//        email.setPwd("test123!@#");
//        email.setFrom("charant.lgp@gmail.com");
//        email.setPwd("baaaalaiah");
//        email.setTo(new String[]{"charant.me.csa@gmail.com"});

        return email;
    }

    public String readTemplate() {
        String template = "";
        try{
//            byte[] stream = Files.readAllBytes(Paths.get(templatePath));
//            template = new String(stream);
//            System.out.println(templatePath);
//            List<String> allLines = Files.readAllLines(Paths.get(templatePath));
            Path path = Paths.get(csvContext.getTemplateFilePath());
            List<String> allLines = Files.readAllLines(path);
            template = String.join("<br/>", allLines);
        } catch (Exception e) {
            System.out.println("Exception while reading template: " + e.getMessage());
        }
        return template;
    }

    private String updateTemplate(String template) {
        template = template.replaceAll("<NAME>", getAuthor());
        template = template.replaceAll("<TITLE>", getTitle());
        return template;
    }

    public Email extractEmail(String[] contents) {
        setTitle(contents[HeaderIndex.TITLE.getPosition()]);
        setAuthor(contents[HeaderIndex.AUTHOR.getPosition()]);
        setEmail(contents[HeaderIndex.EMAIL.getPosition()]);
        return process();
    }

    public void setHeaderIndices(String[] contents) {
        setTitle(contents[HeaderIndex.TITLE.getPosition()]);
        setAuthor(contents[HeaderIndex.AUTHOR.getPosition()]);
        setEmail(contents[HeaderIndex.EMAIL.getPosition()]);
    }
    public String generateSubject() {
        String subject = csvContext.getSubject();
        String shortTitle = "";
        String[] titleWords = getTitle().split(" ");
        if (subject.indexOf("<TITLE>") == -1)
            return subject;
        int i = 0;
        while (i<8) {
            if (i >= titleWords.length) {
                break;
            }
            shortTitle = shortTitle + " " + titleWords[i];
            i++;
        }
        subject = subject.replace("<TITLE>", shortTitle);
        subject = subject + "\"";
        return subject;
    }
}
