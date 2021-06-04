package net.proj.springboot.model;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "sessions")
public class EmployeeSessions {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "emailId")
    private String emailId;

    @Column(name = "dateId")
    private String dateId;
    
    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getDateId() {
        return dateId;
    }

    public void setDateId(String dateId) {
        this.dateId = dateId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
