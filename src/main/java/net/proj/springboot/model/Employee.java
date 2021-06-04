package net.proj.springboot.model;
import javax.persistence.*;

@Entity
@Table(name="employees")
public class Employee {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "userName")
    private String userName;
    @Column(name = "emailId")
    private String emailId;
    @Column(name = "password")
    private String password;

    public Employee(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUsername(String name) {
        this.userName = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String email) {
        this.emailId = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
