package chat.entity;


import java.sql.Timestamp;
import java.util.ArrayList;

public class User {
    public static ArrayList<User> connect_users = new ArrayList<>();

    private String uid;
    private String uname;
    private String password;
    private Timestamp birthday;
    private String gender;
    private boolean connected;
    private Long mostSignBits;
    private Long leastSignBits;

    public static User findUserByUid(String uid) {
        for(User user : connect_users) {
            if(user.uid.equals(uid)) {
                return user;
            }
        }
        return null;
    }

    /* Getter */
    public String getUid() {
        return uid;
    }
    public String getUname() {
        return uname;
    }
    public boolean isConnected() {
        return connected;
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
    public Long getMostSignBits() {
        return mostSignBits;
    }
    public Long getLeastSignBits() {
        return leastSignBits;
    }

    /* Setter */
    public void setUid(String uid) {
        this.uid = uid;
    }
    public void setConnected(boolean connected) {
        this.connected = connected;
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
    public void setMostSignBits(Long mostSignBits) {
        this.mostSignBits = mostSignBits;
    }
    public void setLeastSignBits(Long leastSignBits) {
        this.leastSignBits = leastSignBits;
    }
}
