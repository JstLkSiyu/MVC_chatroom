package chat.entity;

public class JsonUser {
    private String uid;
    private String uname;
    private String gender;
    private int age;

    /*
    Setter
     */

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    /*
    Getter
     */

    public String getUid() {
        return uid;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getUname() {
        return uname;
    }
}
