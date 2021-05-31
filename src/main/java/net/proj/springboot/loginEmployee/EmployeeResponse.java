package net.proj.springboot.loginEmployee;
import javax.persistence.Entity;

public class EmployeeResponse extends StatusResponse {
    private String userName;
    private String emailId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}



