package chat.entity;


import java.sql.Timestamp;
import java.util.ArrayList;

public class User {
    private String uid;
    private String uname;
    private String password;
    private Timestamp birthday;
    private String gender;

    /* Getter */
    public String getUid() {
        return uid;
    }
    public String getUname() {
        return uname;
    }
    public String getPassword() {
        return password;
    }
    public Timestamp getBirthday() {
        return birthday;
    }
    public String getGender() {
        return gender;
    }

    /* Setter */
    public void setUid(String uid) {
        this.uid = uid;
    }
    public void setUname(String uname) {
        this.uname = uname;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setBirthday(Timestamp birthday) {
        this.birthday = birthday;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
}
