package com.charan.global;

public class CSVContext {
    String csvpath;
    String email;
    String subject;
    String templateFilePath;

    public CSVContext () {

    }

    public String getCsvpath() {
        return csvpath;
    }

    public void setCsvpath(String csvpath) {
        this.csvpath = csvpath;
    }

    public String getEmail() {
        if (email == null) {
            return GlobalContext.getGlobalContext().getCredentialsUtil().getDefaultEmail();
        }
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTemplateFilePath() {
        return templateFilePath;
    }

    public void setTemplateFilePath(String templateFilePath) {
        this.templateFilePath = templateFilePath;
    }
}
