package com.charan.util.creds;

import com.charan.global.GlobalContext;

import java.util.HashMap;
import java.util.Map;

public class CredentialsUtil {
   private Map<String, String> emailCreds;
   private String defaultEmail;
   private String defaultPassword;

    public String getDefaultEmail() {
        return defaultEmail;
    }

    public void setDefaultEmail(String defaultEmail) {
        this.defaultEmail = defaultEmail;
    }

    public String getDefaultPassword() {
        return defaultPassword;
    }

    public void setDefaultPassword(String defaultPassword) {
        this.defaultPassword = defaultPassword;
    }

    public CredentialsUtil() {
       emailCreds = new HashMap<>();
       defaultCreds();
   }

   private void defaultCreds() {

   }

   public void addEmailCreds(String email, String password) {
        emailCreds.put(email, password);
   }
    public void setDefaultCreds() {
        setDefaultEmail("editor.jcrm@clinicalstudiesjournal.com");
        setDefaultPassword("test123!@#");
        addEmailCreds(getDefaultEmail(), getDefaultPassword());
    }

    public String getPassword(String email) {
        return emailCreds.get(email);
    }
}
